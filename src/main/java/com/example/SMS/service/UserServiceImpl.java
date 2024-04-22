package com.example.SMS.service;

import com.example.SMS.dto.CertificateAssistanceForm;
import com.example.SMS.dto.ChangePasswordForm;
import com.example.SMS.dto.UserDto;
import com.example.SMS.entity.Request;
import com.example.SMS.entity.User;
import com.example.SMS.repository.RequestRepository;
import com.example.SMS.repository.UserRepository;
import lombok.SneakyThrows;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private RequestRepository requestRepository;
	private EmailService emailService;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
			RequestRepository requestRepository, EmailService emailService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.requestRepository = requestRepository;
		this.emailService = emailService;
	}

	@Override
	public void saveUser(UserDto userDto) {
		User user = new User();
		user.setName(userDto.getFirstName() + " " + userDto.getLastName());
		user.setEmail(userDto.getEmail());
		// encrypt the password using spring security
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));

		userRepository.save(user);
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public List<UserDto> findAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream().map((user) -> convertEntityToDto(user)).collect(Collectors.toList());
	}

	private UserDto convertEntityToDto(User user) {
		UserDto userDto = new UserDto();
		String[] name = user.getName().split(" ");
		userDto.setFirstName(name[0]);
		userDto.setLastName(name[1]);
		userDto.setEmail(user.getEmail());
		return userDto;
	}

	@SneakyThrows
	@Override
	public String getLoggedInUserEmail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
			return userDetails.getUsername();
		} else {
			throw new Exception("User not logged in");
		}
	}

	@Override
	@SneakyThrows
	public boolean sendForgotPasswordEmail(String email) {
		User user = userRepository.findByEmail(email);
		if (user == null)
			throw new Exception("Invalid email");

		// Generate a random 6-digit number
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);

		// send the email
		emailService.sendOtpMessage(email, otp);

		user.setForgotPasswordCode(passwordEncoder.encode(String.valueOf(otp)));

		userRepository.save(user);
		return true;
	}

	@Override
	@SneakyThrows
	public boolean changePassword(ChangePasswordForm request) {
		User user = userRepository.findByEmail(request.getEmail());
		if (user == null)
			throw new Exception("Invalid email");

		if (user.getForgotPasswordCode() != null
				&& passwordEncoder.matches(request.getCode(), user.getForgotPasswordCode())) {
			user.setPassword(passwordEncoder.encode(request.getPassword()));
			userRepository.save(user);
			return true;
		} else
			throw new Exception("Invalid code");
	}

	@Override
	@SneakyThrows
	public void addRequest(CertificateAssistanceForm certificateAssistanceForm, MultipartFile file) {

		User user = findUserByEmail(getLoggedInUserEmail());
		if (!certificateAssistanceForm.getEmailId().equalsIgnoreCase("s559190@nwmissouri.edu")) {
			throw new Exception("Invalid email.");
		}

		if (certificateAssistanceForm.getId() != null) {
			Request request = requestRepository.getRequestByUserAndId(user, certificateAssistanceForm.getId())
					.orElseThrow(() -> new Exception("Invalid Event id"));

			request.setRequestType(certificateAssistanceForm.getRequestType());
			request.setDescription(certificateAssistanceForm.getDescription());
			if (!file.isEmpty())
				request.setImage(file.getBytes());

			requestRepository.save(request);
			// send the email
			emailService.sendCertificateMessage(certificateAssistanceForm, file);
		} else {
			Request request = Request.builder().user(user).requestType(certificateAssistanceForm.getRequestType())
					.description(certificateAssistanceForm.getDescription()).build();

			if (!file.isEmpty())
				request.setImage(file.getBytes());
			else
				throw new Exception("Please select file");

			requestRepository.save(request);

			// send the email
			emailService.sendCertificateMessage(certificateAssistanceForm, file);
		}
	}

	public List<Request> getUserRequests() {
		User user = findUserByEmail(getLoggedInUserEmail());
		List<Request> userRequests = requestRepository.findAllByUser(user);
		return userRequests;
	}

	@Override
	@SneakyThrows
	public void deleteRequest(Long RequestId) {
		User user = findUserByEmail(getLoggedInUserEmail());
		Request Request = requestRepository.getRequestByUserAndId(user, RequestId)
				.orElseThrow(() -> new Exception("Invalid Request id"));
		requestRepository.delete(Request);
	}
}
