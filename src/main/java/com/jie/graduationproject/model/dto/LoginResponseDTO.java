package com.jie.graduationproject.model.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {

    private String token;

    private String message;

    private String username;

    private String role;

    public LoginResponseDTO(String token, String message, String username, String role) {
        this.token = token;
        this.message = message;
        this.username = username;
        this.role = role;
    }
}
