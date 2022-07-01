/*
package com.aliDns.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yingtian.jdxapplet.core.filter.LoginAuthenticationFilter;
import com.yingtian.jdxapplet.service.AuthService;
import com.yingtian.jdxapplet.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.PrintWriter;


/**
 * security配置类
 * @author zhou-zc
 */
/*
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    AuthService userService;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }
    @Bean
    LoginAuthenticationFilter loginAuthenticationFilter() throws Exception {
        LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter();
        loginAuthenticationFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter out = response.getWriter();
            out.write(new ObjectMapper().writeValueAsString("123"));
            out.flush();
            out.close();
        });
        loginAuthenticationFilter.setAuthenticationFailureHandler((request, response, exception) -> {
            String errMsg = exception.getMessage();
            if (exception instanceof LockedException) {
                errMsg = "账户被锁定，请联系管理员!";
            } else if (exception instanceof CredentialsExpiredException) {
                errMsg = "密码过期，请联系管理员!";
            } else if (exception instanceof AccountExpiredException) {
                errMsg = "账户过期，请联系管理员!";
            } else if (exception instanceof DisabledException) {
                errMsg = "账户被禁用，请联系管理员!";
            } else if (exception instanceof BadCredentialsException) {
                errMsg = "用户名或者密码输入错误，请重新输入!";
            }
            ResponseUtil.responseExceptionError(response,500,errMsg);
        });
        loginAuthenticationFilter.setFilterProcessesUrl("/login");
        loginAuthenticationFilter.setAuthenticationManager(authenticationManager());
        return loginAuthenticationFilter;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/verifyCode.jpg","/auth/login").permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
                    ResponseUtil.responseExceptionError(response,500,"还未登录");
                })
                .and().csrf().disable()
                .addFilter(loginAuthenticationFilter());
    }


}
*/
