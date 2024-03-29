package com.image.product.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable()
                .authorizeRequests().antMatchers("/productList.html","/list/{id}","/register")
                .permitAll() .anyRequest().authenticated()
                .and()
                .formLogin() .loginPage("/login")
                .permitAll()
                .and()
                .logout() .invalidateHttpSession(true)
                .clearAuthentication(true) .permitAll();
//                .antMatcher("/**").authorizeRequests()
//                .antMatchers("/list/{id}").permitAll()
//                .antMatchers("/").permitAll()
//                .antMatchers("/productList.html").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .oauth2Login();
    }

}
