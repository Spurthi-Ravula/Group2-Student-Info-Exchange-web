package com.example.SMS.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.SMS.dto.CertificateAssistanceForm;

import jakarta.mail.MessagingException;

public interface EmailService {
	
	void sendOtpMessage(String to, int OTP) throws MessagingException;
	
	void sendCertificateMessage(CertificateAssistanceForm certificateAssistanceForm, MultipartFile file) throws Exception;

}
