package com.example.springsecuritydemo.security;

import com.example.springsecuritydemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.annotation.Resource;

/**
 * SecurityConfiguration
 *
 * @author Panbo
 * @create_time 2023/4/5 19:48
 */
@Configuration
@Slf4j
public class SecurityConfiguration {

    @Resource
    private UserService userService;

    /**
     * 创建密码加密类
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 创建 AuthenticationEntryPoint
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            // 认证失败处理
            log.error("认证失败，authException = [{}]", authException.toString());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("认证失败");
        };
    }

    /**
     * 创建 AccessDeniedHandler
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            // 授权失败处理
            log.error("授权失败，accessDeniedException = [{}]", accessDeniedException.toString());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("授权失败");
        };
    }

    /**
     * 创建SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 开启session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        // 开启exceptionHandling
        http.exceptionHandling()
                // 认证失败处理
                .authenticationEntryPoint(authenticationEntryPoint())
                // 授权失败处理
                .accessDeniedHandler(accessDeniedHandler());
        // 所有请求都需要认证
        http.authorizeRequests()
                .anyRequest().authenticated();
        // 开启表单登录
        http.formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .permitAll()
                .usernameParameter("username")
                .passwordParameter("password");
        // 开启注销
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html")
                // 清除认证信息
                .invalidateHttpSession(true)
                // 清除cookie
                .clearAuthentication(true);
        // 开启记住我
        http.rememberMe()
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(60 * 60 * 24 * 7);
        // 关闭csrf
        http.csrf().disable();
        return http.build();
    }

    /**
     * 创建 AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthenticationProvider() {
            // 认证方法
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                // 从Authentication中获取用户名和登录凭证
                String username = authentication.getName();
                String password = authentication.getCredentials().toString();
                UserDetails user = userService.loadUserByUsername(username);
                // 密码加密
                if (passwordEncoder().matches(password, user.getPassword())) {
                    // 密码正确，返回一个认证成功的Authentication
                    log.info("登陆成功，User = [{}]", username);
                    return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
                } else {
                    // 密码错误，抛出异常
                    log.info("登陆失败，User = [{}]", username);
                    throw new BadCredentialsException("密码错误");
                }
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return authentication.equals(UsernamePasswordAuthenticationToken.class);
            }
        }

                ;
    }
}
