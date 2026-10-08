package com.example.delivery.global.config;

import com.example.delivery.global.security.JwtAuthenticationFilter;
import com.example.delivery.global.security.JwtAuthorizationFilter;
import com.example.delivery.global.security.JwtUtil;
import com.example.delivery.global.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableJpaAuditing
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(jwtUtil, authenticationManager);

        JwtAuthorizationFilter jwtAuthorizationFilter =
                new JwtAuthorizationFilter(jwtUtil, userDetailsService);

        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/api/user/signup").permitAll()
                                .requestMatchers("/api/user/login").permitAll()

                                .requestMatchers("/api/menus/**").hasRole("OWNER")
                                .requestMatchers("/api/menulist/**").permitAll()

                                .requestMatchers("/api/orders").hasRole("CUSTOMER")
                                .requestMatchers("/api/orderlist").hasAnyRole("CUSTOMER", "OWNER")
                                .requestMatchers("/api/orderlist/*/cancel").hasRole("CUSTOMER")
                                .requestMatchers("/api/orderlist/*/status").hasRole("OWNER")

                                .requestMatchers("/api/payment").hasRole("CUSTOMER")
                                .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthorizationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterAt(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
