package com.example.springsecuritydemo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SampleController
 *
 * @author Panbo
 * @create_time 2023/4/5 18:16
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class SampleController {

    /**
     * hello
     *
     * @return String
     */
    @GetMapping("hello")
    public String hello() {
        return "hello";
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("printCurrentUser")
    public void getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication.name = [{}]", authentication.getName());
        log.info("authentication.authorities = [{}]", authentication.getAuthorities());
        log.info("authentication.credentials = [{}]", authentication.getCredentials());
        log.info("authentication.details = [{}]", authentication.getDetails());
        log.info("authentication.principal = [{}]", authentication.getPrincipal());
    }
}
