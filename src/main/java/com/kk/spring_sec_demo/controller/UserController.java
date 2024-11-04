package com.kk.spring_sec_demo.controller;

import com.kk.spring_sec_demo.model.User;
import com.kk.spring_sec_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    
    /**
     * Register a user.
     *
     * @param user the user to register
     * @return the registered user
     */
    @PostMapping(value = "/register", consumes = "application/json")
    public User register(@RequestBody User user) {
        userService.saveUser(user);
        return user;
    }

    @PostMapping(value = "/login")
    public String login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return "Login successful";
        } else {
            return "Login failed";
        }
    }
}
