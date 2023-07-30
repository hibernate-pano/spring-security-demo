package com.example.springsecuritydemo.security.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * BeforeLoginFilter
 * 前置过滤器
 *
 * @author Panbo
 * @create_time 2023/7/30 17:09
 */
@Slf4j
@Component
public class BeforeLoginFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 在登录成功后执行
        log.info("前置过滤器 execute");
        // 继续执行过滤
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
