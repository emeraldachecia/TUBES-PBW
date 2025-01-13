package com.runtracker.config;

import com.runtracker.security.CustomSessionAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

@Configuration
public class SecurityConfig {

      private final CustomSessionAuthenticationFilter customSessionAuthenticationFilter;

      public SecurityConfig(CustomSessionAuthenticationFilter customSessionAuthenticationFilter) {
            this.customSessionAuthenticationFilter = customSessionAuthenticationFilter;
      }

      // Hash password untuk disimpan di database
      @Bean
      public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
      }

      @Bean
      public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                        .csrf().disable()
                        .authorizeHttpRequests(auth -> auth
                                    .requestMatchers("/", "/home", "/css/**", "/js/**", "/img/**", "/image/**",
                                                "/webjars/**")
                                    .permitAll()
                                    .requestMatchers("/user/signup", "/user/signin", "/upload", "/uploadStatus")
                                    .permitAll()
                                    .anyRequest().authenticated())
                        .addFilterBefore(customSessionAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                        .sessionManagement(session -> session
                                    .maximumSessions(1)) // Hanya 1 sesi aktif per pengguna
                        .formLogin().disable() // Nonaktifkan login bawaan Spring Security
                        .exceptionHandling(exception -> exception
                                    .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/user/signin"))
                                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                                          response.sendRedirect("/user/signin");
                                    }));

            return http.build();
      }
}
