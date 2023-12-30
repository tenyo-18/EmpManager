package com.example.empmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder)
      throws Exception {
    authenticationManagerBuilder
        .inMemoryAuthentication()
        .withUser("admin")
        .password(passwordEncoder().encode("password"))
        .roles("ADMIN");
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .exceptionHandling()
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, "/api/employees")
        .hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/api/employees/*")
        .hasRole("ADMIN")
        .and()
        .formLogin()
        .defaultSuccessUrl("/success.html", true)
        .failureHandler(new SimpleUrlAuthenticationFailureHandler())
        .and()
        .logout();
  }
}
