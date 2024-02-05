package com.gdpdemo.GDPSprint1Project;

import java.util.Random;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gdpdemo.GDPSprint1Project.Repository.HomeRepository;
import com.gdpdemo.GDPSprint1Project.service.EmailService;



@Controller
public class HomeController {

	@Autowired
	private HomeRepository homeRepository;
	
	@Autowired
	private EmailService emailservice;
	
	
	private EncryptDecrypt encyptDecrypt;
	
	

	ModelAndView mv = new ModelAndView();

	@RequestMapping("/")
	public String home() {
		return "Home";
	}
	
	@RequestMapping("/createpost")
	public String createPost() {
		return "createpost";
	}

	@RequestMapping("/Demo")
	public String Demo(Model model) {
		model.addAttribute("user", new Home());
		return "Demo";
	}

	@RequestMapping("/category")
	public String category() {
		return "category";
	}
	@RequestMapping("/dummy")
	public String dummy() {
		return "dummy";
	}

	@RequestMapping("/send-otp")
	public String sendOtp(@RequestParam("email") String email, HttpSession session) throws MessagingException {
		System.out.println("EMAIL" + email);
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		System.out.println(otp);
		emailservice.sendOtpMessage(email, otp);
		session.setAttribute("myotp", otp);
		session.setAttribute("email", email);
		return "verifyOtp";
	}

	@RequestMapping("/resetSendOtp")
	public String resetSendOtp(@RequestParam("email") String email, HttpSession session) throws MessagingException {
		System.out.println("EMAIL" + email);
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		System.out.println(otp);
		emailservice.sendOtpMessage(email, otp);
		session.setAttribute("myotp", otp);
		session.setAttribute("email", email);
		return "ResetverifyOtp";
	}

	@RequestMapping("/SignUp")
	public String SignUp(Model model) {
		model.addAttribute("user", new Home());
		return "SignUp";
	}

	@RequestMapping(value = "/ForgotPassword", method = RequestMethod.POST)
	public String registeredUser(@Validated @ModelAttribute("user") Home user, BindingResult result, Model model,
			HttpSession session) {
		try {
			if (!user.getPassword().contains(user.getRePassword())) {
				session.setAttribute("message", "Re-type your password correctly!!");
				throw new Exception("Re-type your password correctly!!");
			}

			if (result.hasErrors()) {
				System.out.println("Error" + result.toString());
				model.addAttribute("user", user);
				return "Demo";
			}
			
			Home home = this.homeRepository.getUserByUserName(user.getEmail());
			if(home==null) {
				System.out.println("USER" + user);
				
				String password = user.getPassword();
				System.out.println("password************"+ password);
				
			BCryptPasswordEncoder bcyrpt = new BCryptPasswordEncoder();
			String encryptedPwd = bcyrpt.encode(user.getPassword());
			user.setPassword(encryptedPwd);
			user.setRePassword(encryptedPwd);        
			Home save = homeRepository.save(user);
				model.addAttribute("user", new Home());
			session.setAttribute("email", user.getEmail());
			return "ForgotPassword";
			}
			if (user.getEmail().contains(home.getEmail())) {
				session.setAttribute("message", "User with this email already exists");
				return "Demo";
			}
			String firstName = home.getFirstName();
			emailservice.sendPasswordChangedMessage(user.getEmail(), user.getFirstName());
			// session.setAttribute("message", new Message("Successfully Registered!!!",
			// "alert-success"));
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", e.getMessage());
			return "Demo";
		}
		return "Demo";
	}

	
	@RequestMapping("/ForgotPassword")
	public String ForgotPassword() {
		return "ForgotPassword";
	}

	@RequestMapping("/ResetPassword")
	public String ResetPassword() {
		return "ResetPassword";
	}

	@RequestMapping("/ChangePassword")
	public String ChangePassword() {
		return "ChangePassword";
	}

	@RequestMapping("/library")
	public String library() {
		return "library";
	}

	@RequestMapping("/Login")
	public String Login(Model model) {
		model.addAttribute("title", "Login Page");
		return "Login";
	}

	@RequestMapping("/myposts")
	public String MyPost(Model model) {
		model.addAttribute("title", "My Post");
		
		return "myposts";
	}

	
	
	@RequestMapping(value = "/category", method = RequestMethod.POST)
	public String loginUser(@Valid @ModelAttribute("user") Home user, BindingResult result, Model model,
			HttpSession session) {
		try {
			if (user.getEmail() == "" || user.getPassword() == "") {
				throw new Exception("Please Enter your Credentials!!");
			}

			/*
			 * if (result.hasErrors()) { System.out.println("Error" + result.toString());
			 * model.addAttribute("user", user); return "Login"; }
			 */

			System.out.println("USER" + user.getEmail());

			
			Home home = this.homeRepository.getUserByUserName(user.getEmail());
			System.out.println("Home" + home.getEmail());
			
			if (home == null) {
				session.setAttribute("message", "User does not exits with this email !!");
				return "Login";
			} else {
				BCryptPasswordEncoder bcyrpt = new BCryptPasswordEncoder();
				
				if(!bcyrpt.matches(user.getPassword(), home.getPassword())){
					session.setAttribute("message","Please Enter Correct Password");
					return "Login";
				}
				
			}
			session.setAttribute("firstName", home.getFirstName());
			session.setAttribute("lastName", home.getLastName());
			session.setAttribute("email",home.getEmail());
			return "category";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message(e.getMessage(), "alert-danger"));
			return "Login";
		}

	}
	 

	@RequestMapping(value = "/verify-otp", method = RequestMethod.POST)
	public String validateOtp(@RequestParam("otp") int otpnum, HttpSession session) {
		Integer myOtp = (int) session.getAttribute("myotp");
		String email = (String) session.getAttribute("email");
		if (myOtp == otpnum) {
			Home home = this.homeRepository.getUserByUserName(email);
			session.setAttribute("message", new Message("You have Successfully Registered!!!", "alert-success"));
			return "Sucess";
		} else {
			session.setAttribute("message", "You have entered wrong otp");
			return "verifyOtp";
		}

	}

	@RequestMapping(value = "/resetverifyotp", method = RequestMethod.POST)
	public String resetValidateOtp(@RequestParam("otp") int otpnum, HttpSession session) {
		Integer myOtp = (int) session.getAttribute("myotp");
		String email = (String) session.getAttribute("email");
		if (myOtp == otpnum) {
			Home home = this.homeRepository.getUserByUserName(email);
			if (home == null) {
				session.setAttribute("message", "User does not exits with this email !!");
				return "ResetPassword";
			} else {
				session.setAttribute("message", new Message("You have Successfully Registered!!!", "alert-success"));
			}
			return "password_change_form";
		} else {
			session.setAttribute("message", "You have entered wrong otp");
			return "ResetverifyOtp";
		}

	}

	@RequestMapping(value = "/change-password", method = RequestMethod.POST)
	public String changePassword(@RequestParam("newpassword") String newpassword, HttpSession session) throws MessagingException {
		String email = (String) session.getAttribute("email");
		Home home = this.homeRepository.getUserByUserName(email);
		
		BCryptPasswordEncoder bcyrpt = new BCryptPasswordEncoder();
		String encryptedPwd = bcyrpt.encode(newpassword);
		home.setPassword(encryptedPwd);
		home.setRePassword(encryptedPwd);        
		
		this.homeRepository.save(home);
		session.setAttribute("message", new Message("Password Successfully Changed", "alert-success"));
		String firstName = home.getFirstName();
		emailservice.sendPasswordChangedMessage(email, firstName);
		return "ResetSuccess";

	}
	


	
}
