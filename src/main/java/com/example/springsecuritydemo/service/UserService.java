package com.example.springsecuritydemo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.springsecuritydemo.mapper.UserMapper;
import com.example.springsecuritydemo.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * UserService 用户服务
 *
 * @author Panbo
 * @create_time 2023/4/8 10:45
 */
@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户表是否有此用户名对应的用户，如果有一个则返回，如果没有则抛出异常，如果有多个则抛出异常
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        List<User> list = this.userMapper.selectList(queryWrapper);

        if (CollectionUtils.isEmpty(list)) {
            log.error("用户不存在，username = [{}]", username);
            throw new UsernameNotFoundException("用户不存在");
        } else if (list.size() > 1) {
            log.error("用户不唯一，username = [{}]", username);
            throw new UsernameNotFoundException("用户不唯一");
        } else {
            log.info("用户存在，username = [{}]", username);
            return list.get(0);
        }
    }
}
