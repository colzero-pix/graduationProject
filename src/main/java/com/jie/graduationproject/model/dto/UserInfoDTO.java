package com.jie.graduationproject.model.dto;

import lombok.Data;

@Data
public class UserInfoDTO {

    private String username;

    private String role;

    private String phone;

    public UserInfoDTO(String username, String role, String phone) {
        this.username = username;
        this.role = role;
        this.phone = phone;
    }
}
