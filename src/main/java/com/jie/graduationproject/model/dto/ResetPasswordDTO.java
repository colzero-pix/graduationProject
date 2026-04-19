package com.jie.graduationproject.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordDTO {

    private String username;

}
