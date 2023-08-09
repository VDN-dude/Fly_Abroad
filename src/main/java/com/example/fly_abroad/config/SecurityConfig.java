package com.example.fly_abroad.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String LOGIN_ENDPOINT = "/user/login";
    private static final String[] REG_ENDPOINTS = {
            "/user/register",
            "/airline/register"
    };
    private static final String[] PUBLIC_URLS = {
            "/",
            "/airline/register"
    };
    private static final String LOGOUT_SUCCESS_URL = "/";

    private static final String AIRLINE_ADMIN_URLS = "/airline/**";

    @SneakyThrows
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        return httpSecurity
                .authorizeRequests()
                .antMatchers(AIRLINE_ADMIN_URLS).hasAuthority("AIRLINE_ADMIN")
                .antMatchers(REG_ENDPOINTS).permitAll()
                .antMatchers(PUBLIC_URLS).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage(LOGIN_ENDPOINT).permitAll()
                .and()
                .logout().permitAll().logoutSuccessUrl(LOGOUT_SUCCESS_URL)
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
