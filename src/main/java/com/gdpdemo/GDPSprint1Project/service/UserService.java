package com.example.SMS.service;

import com.example.SMS.dto.UserDto;
import com.example.SMS.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();

    String getLoggedInUserEmail();
}