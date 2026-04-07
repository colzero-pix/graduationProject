package com.jie.graduationproject.service.User;

import com.jie.graduationproject.model.dto.LoginRegisterDTO;
import com.jie.graduationproject.model.dto.PasswordUpdateDTO;
import com.jie.graduationproject.model.dto.ResetPasswordDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {

    public ResponseEntity<?> userRegister(LoginRegisterDTO loginRegisterDTO);

    public ResponseEntity<?> deleteUser(String username);

    public ResponseEntity<?> changeUserPassword(String username, PasswordUpdateDTO passwordUpdateDTO);

    public ResponseEntity<?> getUserInfo();

    public ResponseEntity<?> changeUserInfo(String username, String role, String phone);

    public ResponseEntity<?> updatePassword(PasswordUpdateDTO passwordUpdateDTO);

    public ResponseEntity<?> resetPassword(ResetPasswordDTO resetPasswordDTO);

    public ResponseEntity<?> getAllUsers();
}
