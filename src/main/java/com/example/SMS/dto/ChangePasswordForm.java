package com.example.SMS.dto;

import lombok.Data;

@Data
public class ChangePasswordForm {

    private String email;
    private String code;
    private String password;

}
