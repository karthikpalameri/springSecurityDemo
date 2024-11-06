package com.kk.spring_sec_demo.controller;

import com.kk.spring_sec_demo.model.User;
import com.kk.spring_sec_demo.service.JwtService;
import com.kk.spring_sec_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

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


    /**
     * Authenticates a user by checking their credentials against the database.
     * <p>
     * This method uses the AuthenticationManager to authenticate the user. The AuthenticationManager
     * delegates to a DaoAuthenticationProvider, which uses a UserDetailsService to retrieve the user's
     * details from the database. The UserDetailsService uses a UserRepository to query the database.
     * <p>
     * The DaoAuthenticationProvider then checks the provided password against the stored password using
     * a PasswordEncoder. If the credentials match, an authenticated Authentication object is returned.
     *
     * @param authentication the UsernamePasswordAuthenticationToken to authenticate
     * @return the authenticated Authentication object, or null if authentication fails
     */
    @PostMapping(value = "/login")
    public String login(@RequestBody User user) {
        try {
            // Before generating the token, we need to authenticate the user
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(user);
            } else {
                return "Login failed";
            }
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }
}
