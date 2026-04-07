package com.jie.graduationproject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/date")
    public Map<String, Object> testDate() {
        Map<String, Object> result = new HashMap<>();

        // 测试 LocalDateTime
        result.put("localDateTime", LocalDateTime.now());

        // 测试 LocalDate
        result.put("localDate", LocalDate.now());

        // 测试带毫秒的时间
        result.put("now", LocalDateTime.now().withNano(0)); // 去掉纳秒

        return result;
    }
}