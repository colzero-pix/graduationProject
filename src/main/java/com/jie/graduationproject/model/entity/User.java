package com.jie.graduationproject.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Entity
@Table(name = "user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "用户名不能为空")
    @Column(name = "username", nullable = false)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "phone")
    private String phone;

}
