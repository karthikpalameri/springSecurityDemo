package com.kk.spring_sec_demo.service;

import com.kk.spring_sec_demo.model.User;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    
    public String generateToken(User user) {
        // Generate token   
        return Jwts.builder()
                .setClaims(c)
        return null;
    }
}
