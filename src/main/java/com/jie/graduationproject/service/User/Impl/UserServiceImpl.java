package com.jie.graduationproject.service.User.Impl;

import com.jie.graduationproject.exception.ForbiddenException;
import com.jie.graduationproject.model.dto.LoginRegisterDTO;
import com.jie.graduationproject.model.dto.PasswordUpdateDTO;
import com.jie.graduationproject.model.dto.ResetPasswordDTO;
import com.jie.graduationproject.model.dto.UserInfoDTO;
import com.jie.graduationproject.model.entity.User;
import com.jie.graduationproject.repository.UserRepository;
import com.jie.graduationproject.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import jakarta.annotation.PostConstruct;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }
    
    @PostConstruct
    public void initAdminUser() {
        try {
            // 检查是否有ADMIN权限的用户
            long adminCount = userRepository.countByRole("ADMIN");
            
            if (adminCount == 0) {
                System.out.println("数据库中无ADMIN用户，正在创建默认管理员...");
                
                // 检查admin用户是否已存在（可能角色不是ADMIN）
                Optional<User> existingAdminOpt = userRepository.findUserByUsername("admin");
                
                if (existingAdminOpt.isPresent()) {
                    // 如果admin用户存在但角色不是ADMIN，更新为ADMIN
                    User adminUser = existingAdminOpt.get();
                    if (!"ADMIN".equals(adminUser.getRole())) {
                        adminUser.setRole("ADMIN");
                        adminUser.setPassword(passwordEncoder.encode("123456"));
                        userRepository.save(adminUser);
                        System.out.println("已更新admin用户为ADMIN角色，密码重置为123456");
                    }
                } else {
                    // 创建新的admin用户
                    User adminUser = new User();
                    adminUser.setUsername("admin");
                    adminUser.setPassword(passwordEncoder.encode("123456"));
                    adminUser.setRole("ADMIN");
                    adminUser.setPhone("13800138000");
                    
                    userRepository.save(adminUser);
                    System.out.println("已创建默认管理员用户: admin/123456 (ADMIN角色)");
                }
                
                System.out.println("默认管理员信息:");
                System.out.println("用户名: admin");
                System.out.println("密码: 123456");
                System.out.println("角色: ADMIN");
            } else {
                System.out.println("数据库中已有 " + adminCount + " 个ADMIN用户");
            }
            
        } catch (Exception e) {
            System.err.println("初始化管理员用户时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public ResponseEntity<?> userRegister(LoginRegisterDTO loginRegisterDTO) {
        try {
            if (userRepository.existsUserByUsername(loginRegisterDTO.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("该用户名已被注册，请更换用户名");
            }


            User newUser = new User();
            newUser.setUsername(loginRegisterDTO.getUsername());
            newUser.setPassword(passwordEncoder.encode("123456"));
            newUser.setRole("STORE_KEEPER");

            userRepository.save(newUser);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("用户 " + loginRegisterDTO.getUsername() + " 注册成功（密码：123456，角色：STORE_KEEPER）");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("注册失败：" + e.getMessage());
        }
    }

    //删除用户
    @Override
    @Transactional
    public ResponseEntity<?> deleteUser(String username){

        try {
            // 检查用户是否存在
            if (!userRepository.existsUserByUsername(username)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("用户名为" + username + "的用户不存在");
            }
            
            // 使用自定义查询删除用户
            userRepository.deleteByUsername(username);
            return ResponseEntity.ok("用户删除成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("删除用户失败：" + e.getMessage());
        }
    }

    //修改用户密码
    @Override
    public ResponseEntity<?> changeUserPassword(String username, PasswordUpdateDTO passwordUpdateDTO) {
//        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new RuntimeException("该用户不存在" + username));
//        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
//        userRepository.save(user);
        try {
            User user = userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new RuntimeException("用户名为" + username + "的用户不存在"));

            if (!passwordEncoder.matches(passwordUpdateDTO.getOriginalPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("原密码错误");
            }

            user.setPassword(passwordEncoder.encode(passwordUpdateDTO.getNewPassword()));
            userRepository.save(user);

            return ResponseEntity.ok("密码修改成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //获取用户信息
    @Override
    @Transactional
    public ResponseEntity<?> getUserInfo() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                throw new RuntimeException("用户未认证");
            }
            String username = authentication.getName();

            User targetUser = userRepository.findUserByUsername(username).orElseThrow(() -> new RuntimeException("该用户不存在" + username));
            if(!targetUser.getUsername().equals(username)) {
                boolean isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

                if(!isAdmin) {
                    throw new ForbiddenException("无权访问该id用户信息");
                }
            }

            UserInfoDTO targetInfo = new UserInfoDTO(
                    targetUser.getId(),
                    targetUser.getUsername(),
                    targetUser.getRole()
            );
            targetInfo.setPhone(targetUser.getPhone());

            return ResponseEntity.ok(targetInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //修改用户信息（用户权限与用户电话）
    @Override
    public ResponseEntity<?> changeUserInfo(String username, String role,  String phone){
        try {
            // 获取当前登录用户
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("用户未认证");
            }

            // 从数据库获取当前用户信息以检查角色
            String currentUsername = authentication.getName();
            User currentUser = userRepository.findUserByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("当前用户不存在"));

            // 检查目标用户是否存在
            User targetUser = userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new RuntimeException("用户名为" + username + "的用户不存在"));

            // 权限检查：管理员可以修改任何用户，普通用户只能修改自己的信息
            boolean isAdmin = "ADMIN".equals(currentUser.getRole());
            boolean isSelf = currentUsername.equals(username);
            
            if (!isAdmin && !isSelf) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("权限不足：只能修改自己的信息");
            }

            // 普通用户不能修改角色，只能修改电话
            if (!isAdmin && role != null && !role.equals(targetUser.getRole())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("权限不足：不能修改用户角色");
            }

            // 更新用户信息
            if (role != null && !role.isEmpty()) {
                targetUser.setRole(role);
            }
            if (phone != null) {
                targetUser.setPhone(phone);
            }

            userRepository.save(targetUser);

            return ResponseEntity.ok("用户信息更新成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("更新用户信息失败：" + e.getMessage());
        }
    }

    // 修改当前用户密码（需要原密码）
    @Override
    public ResponseEntity<?> updatePassword(PasswordUpdateDTO passwordUpdateDTO) {
        try {
            // 获取当前登录用户
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("用户未认证");
            }
            
            String username = authentication.getName();
            User user = userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            // 验证原密码
            if (!passwordEncoder.matches(passwordUpdateDTO.getOriginalPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("原密码错误");
            }

            // 更新密码
            user.setPassword(passwordEncoder.encode(passwordUpdateDTO.getNewPassword()));
            userRepository.save(user);

            return ResponseEntity.ok("密码修改成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("密码修改失败：" + e.getMessage());
        }
    }

    // 管理员重置其他用户密码（重置为123456）
    @Override
    public ResponseEntity<?> resetPassword(ResetPasswordDTO resetPasswordDTO) {
        try {
            // 检查目标用户是否存在
            User targetUser = userRepository.findUserByUsername(resetPasswordDTO.getUsername())
                    .orElseThrow(() -> new RuntimeException("用户 " + resetPasswordDTO.getUsername() + " 不存在"));

            // 重置密码为123456
            targetUser.setPassword(passwordEncoder.encode("123456"));
            userRepository.save(targetUser);

            return ResponseEntity.ok("用户 " + resetPasswordDTO.getUsername() + " 的密码已重置为123456");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("密码重置失败：" + e.getMessage());
        }
    }

    // 获取所有用户（仅管理员）
    @Override
    public ResponseEntity<?> getAllUsers() {
        try {
            // 获取当前登录用户
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("用户未认证");
            }

            // 从数据库获取当前用户信息以检查角色
            String username = authentication.getName();
            User currentUser = userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            // 检查是否为管理员
            if (!"ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("权限不足：需要管理员权限");
            }

            // 获取所有用户
            List<User> users = userRepository.findAll();
            
            // 转换为DTO列表，排除密码等敏感信息
            List<UserInfoDTO> userList = users.stream()
                    .map(user -> new UserInfoDTO(
                            user.getId(),
                            user.getUsername(),
                            user.getRole()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(userList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("获取用户列表失败：" + e.getMessage());
        }
    }

    // 当前用户自行修改电话
    @Override
    public ResponseEntity<?> updateCurrentUserPhone(String phone) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("用户未认证");
            }

            String username = authentication.getName();
            User user = userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            user.setPhone(phone);
            userRepository.save(user);

            return ResponseEntity.ok("电话修改成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("电话修改失败：" + e.getMessage());
        }
    }
}
