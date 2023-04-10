package com.example.springsecuritydemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SampleController
 *
 * @author Panbo
 * @create_time 2023/4/5 18:16
 */
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
}
