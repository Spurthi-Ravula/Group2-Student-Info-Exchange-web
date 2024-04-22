package com.example.SMS.service;

import com.example.SMS.dto.CertificateAssistanceForm;
import com.example.SMS.dto.ChangePasswordForm;
import com.example.SMS.dto.UserDto;
import com.example.SMS.entity.Request;
import com.example.SMS.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();

    String getLoggedInUserEmail();

    boolean sendForgotPasswordEmail(String email);

    boolean changePassword(ChangePasswordForm request);

    void addRequest(CertificateAssistanceForm certificateAssistanceForm, MultipartFile file);
    
    List<Request> getUserRequests();
    
    void deleteRequest(Long requestId);
}