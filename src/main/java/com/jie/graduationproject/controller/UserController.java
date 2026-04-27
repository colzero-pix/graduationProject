package com.jie.graduationproject.controller;

import com.jie.graduationproject.exception.UnauthorizedAccessException;
import com.jie.graduationproject.model.dto.LoginResponseDTO;
import com.jie.graduationproject.model.dto.LoginRegisterDTO;
import com.jie.graduationproject.model.dto.PasswordUpdateDTO;
import com.jie.graduationproject.model.dto.ResetPasswordDTO;
import com.jie.graduationproject.jwt.AuthService;
import com.jie.graduationproject.service.User.Impl.UserServiceImpl;
import com.jie.graduationproject.service.User.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    //登录
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRegisterDTO loginRegisterDTO) {
        try {
            LoginResponseDTO response = authService.authenticateUser(loginRegisterDTO);
            return ResponseEntity.ok(response);
        } catch (UnauthorizedAccessException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", HttpStatus.UNAUTHORIZED.value());
            errorResponse.put("message", e.getMessage());
            errorResponse.put("success", false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", e.getMessage());
            errorResponse.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

    }

    //注册（仅管理员）
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> userRegister(@RequestBody @Valid LoginRegisterDTO loginRegisterDTO) {
        return userService.userRegister(loginRegisterDTO);
    }

    //获取用户信息
    @GetMapping("/information")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserInfo() {
        return userServiceImpl.getUserInfo();
    }

    //用户自行修改电话
    @PutMapping("/phone")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updatePhone(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        return userServiceImpl.updateCurrentUserPhone(phone);
    }

    //修改密码（需要原密码）
    @PutMapping("/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid PasswordUpdateDTO passwordUpdateDTO) {
        return userServiceImpl.updatePassword(passwordUpdateDTO);
    }

    //管理员重置其他用户密码（重置为123456）
    @PutMapping("/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO) {
        return userServiceImpl.resetPassword(resetPasswordDTO);
    }

    //获取所有用户（仅管理员）
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {

        return userServiceImpl.getAllUsers();
    }

    //删除用户（仅管理员）
    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        return userServiceImpl.deleteUser(username);
    }

    //更新用户信息（仅管理员）
    @PutMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody Map<String, String> userData) {
        String role = userData.get("role");
        String phone = userData.get("phone");
        return userServiceImpl.changeUserInfo(username, role, phone);
    }
}
