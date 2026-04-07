package com.jie.graduationproject.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordUpdateDTO {

    @NotBlank(message = "旧密码不能为空")
    private String originalPassword;

    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}
