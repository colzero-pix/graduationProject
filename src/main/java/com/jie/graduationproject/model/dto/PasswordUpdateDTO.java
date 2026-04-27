package com.jie.graduationproject.model.dto;

import jakarta.validation.constraints.NotBlank;

public class PasswordUpdateDTO {

    @NotBlank(message = "旧密码不能为空")
    private String originalPassword;

    @NotBlank(message = "新密码不能为空")
    private String newPassword;

    public PasswordUpdateDTO() {
    }

    public String getOriginalPassword() { return originalPassword; }
    public void setOriginalPassword(String originalPassword) { this.originalPassword = originalPassword; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
