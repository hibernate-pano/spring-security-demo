package com.example.springsecuritydemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springsecuritydemo.po.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * UserMapper 用户mapper
 *
 * @author Panbo
 * @create_time 2023/4/8 10:40
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
