package com.kk.spring_sec_demo.service;

import com.kk.spring_sec_demo.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // symmetric encryption shared between server and client
    private static final String SECRET_KEY = "Zd0plgZIxp6HYP7/cM1uXhbOyUXHTRxxIL/2fSD+5vk=";
    private String secretKey;

//    public JwtService(String secretKey) {
//        this.secretKey = generateSecretKey();
//    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        // Generate token   
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 3))
                .signWith(getKey(), io.jsonwebtoken.SignatureAlgorithm.HS256).compact();

    }

    /**
     * Get the secret key used for signing the JWT.
     * <p>
     * The {@link #secretKey} is a Base64 encoded string that is decoded and
     * used to create a {@link Key} instance.
     *
     * @return the secret key
     */
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a new secret key for signing JWTs.
     * <p>
     * This method uses a {@link KeyGenerator} to create a new secret key
     * with a length of 256 bits. The key is then encoded using Base64.
     * <p>
     * Just run this to generate a new secret key for signing JWTs while debugging the application to get a new secret key.
     *
     * @return the generated secret key as a Base64 encoded string
     * @throws RuntimeException if an error occurs during key generation
     */
    private String generateSecretKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            keyGen.init(256);
            SecretKey secretKey = keyGen.generateKey();
//            System.out.println("secretKey.toString() = " + secretKey.toString());
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("Error while generating secret key", e);
        }
    }

    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody();
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}

