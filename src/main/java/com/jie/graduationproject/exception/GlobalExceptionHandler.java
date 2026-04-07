package com.jie.graduationproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {

    //处理未授权异常（登录）401
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<?> handleUnauthorized(UnauthorizedAccessException ex) {
        return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    //处理权限不足异常403
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbiddenAccess(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    //用户不存在异常（404）
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    //用户名重复异常（409）
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<?> handUsernameConfiict(UsernameAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }


}
