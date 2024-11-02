package com.kk.spring_sec_demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloContoller {
    @GetMapping("/hello")
    public String hello(HttpServletRequest httpServletRequest) {
        return "hello world, session : " + httpServletRequest.getSession().getId();
    }

    @GetMapping("/about")
    public String about(HttpServletRequest httpServletRequest) {
        return "about, session : " + httpServletRequest.getSession().getId();
    }
}
