package com.teoali.atcal.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  // TODO PROD
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf().disable()
        .authorizeHttpRequests()
        .requestMatchers("/auth/welcome").permitAll()
        .requestMatchers("/register").permitAll()
        .requestMatchers("/").permitAll()
        .and()
        .authorizeHttpRequests().requestMatchers("/**").authenticated()
        .and()
        .authorizeHttpRequests().requestMatchers("/**").authenticated()
        .and().formLogin().loginPage("/login").permitAll()
        .and().build();
  }

//  @Bean
//  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    return http.authorizeHttpRequests().anyRequest().permitAll()
//        .and().build();
//  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
