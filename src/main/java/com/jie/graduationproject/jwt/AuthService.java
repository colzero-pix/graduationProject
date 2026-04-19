package com.jie.graduationproject.jwt;

import com.jie.graduationproject.exception.UnauthorizedAccessException;
import com.jie.graduationproject.model.dto.LoginRegisterDTO;
import com.jie.graduationproject.model.dto.LoginResponseDTO;
import com.jie.graduationproject.model.entity.User;
import com.jie.graduationproject.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public LoginResponseDTO authenticateUser(LoginRegisterDTO loginRegisterDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRegisterDTO.getUsername(),
                            loginRegisterDTO.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findUserByUsername(loginRegisterDTO.getUsername())
                    .orElseThrow(() -> new UnauthorizedAccessException("用户未找到"));

            String jwt = jwtUtil.generateToken(loginRegisterDTO.getUsername());

            return new LoginResponseDTO(jwt, "登录成功", user.getUsername(), user.getRole());

        }catch (BadCredentialsException e) {
            throw new UnauthorizedAccessException("用户名或者密码错误");
        }
    }
}
