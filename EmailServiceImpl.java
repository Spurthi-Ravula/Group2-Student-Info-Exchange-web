package com.example.SMS.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.SMS.dto.CertificateAssistanceForm;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {
 
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendOtpMessage(String to, int OTP) throws MessagingException {
	
		 MimeMessage msg = javaMailSender.createMimeMessage();
	     MimeMessageHelper helper = new MimeMessageHelper(msg, true);
	     helper.setTo(to);
	     String subject = "Here's your One Time Password (OTP)";
	     String content = "<p>Hello " + to + "</p>"
	                + "<p>For security reason, you're required to use the following "
	                + "One Time Password to login:</p>"
	                + "<p><b>" + OTP + "</b></p>";
	         
	     helper.setSubject(subject); 
	     helper.setText(content, true);
	     javaMailSender.send(msg);
   }
	
	public void sendCertificateMessage(CertificateAssistanceForm certificateAssistanceForm, MultipartFile file) throws Exception {

	    MimeMessage msg = javaMailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(msg, true);
	    helper.setTo(certificateAssistanceForm.getEmailId());

	    LocalDate currentDate = LocalDate.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    String formattedDate = currentDate.format(formatter);

	    String subject = "New Certification Assistance is added!";
	    String content = "<html><body>"
	            + "<p>New Certification Assistance request for type "
	            + "<b>" + certificateAssistanceForm.getRequestType() + "</b>"
	            + " was added successfully on " 
	            + formattedDate + "</p>"
	            + "<p> Description: "+ certificateAssistanceForm.getDescription() + "</p>"
	            + "</body></html>";

	    helper.setSubject(subject); 
	    helper.setText(content, true);

	    // Attach the file
	    if (file != null && !file.isEmpty()) {
	        ByteArrayResource fileResource = new ByteArrayResource(file.getBytes());
	        helper.addAttachment(file.getOriginalFilename(), fileResource);
	    }

	    javaMailSender.send(msg);
	}
	
}