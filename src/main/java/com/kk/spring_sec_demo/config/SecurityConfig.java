package com.kk.spring_sec_demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    // some guides to configure spring security:
    //    https://javadevjournal.com/spring-security/spring-security-authentication-providers/
    //    https://medium.com/@anitalakhadze/spring-boot-security-database-authentication-e0661296071
    //    https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/index.html
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Creates and configures a {@link DaoAuthenticationProvider} with
     * the application's {@link UserDetailsService} and a no-operation
     * password encoder.
     *
     * @return a configured {@link AuthenticationProvider} instance
     */
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        //provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());// to save plain text password in database
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        return provider;
    }

    /**
     * <p>Configures the {@link SecurityFilterChain} to secure all
     * endpoints with HTTP Basic authentication. CSRF protection is
     * disabled, and all requests require authentication.</p>
     *
     * <p>Each request will have a different session, due to the
     * configuration of {@link SessionCreationPolicy#STATELESS}.</p>
     *
     * @param http the {@link HttpSecurity} to configure
     * @return a configured {@link SecurityFilterChain}
     * @throws Exception if the bean cannot be created
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/register", "/login").permitAll()// allow registration only from /register without authentication
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())// HTTP Basic authentication
                //.formLogin(Customizer.withDefaults()) // form based login
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));//session stateless so every request will have a different session
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

//    /**
//     * Creates an in-memory user details manager containing two users: user and admin,
//     * both with password "password". User has role USER, and admin has role ADMIN.
//     *
//     * @return an in-memory user details manager
//     */
//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("password")
//                .roles("ADMIN")
//                .build();
//        UserDetails[] users = new UserDetails[]{user, admin};
//        return new InMemoryUserDetailsManager(users);
//    }

}

