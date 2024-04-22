package com.example.SMS.dto;

import lombok.Data;

@Data
public class CertificateAssistanceForm {

	private Long id;
    private String requestType;
    private String description;
    private String emailId;
}
