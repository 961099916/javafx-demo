package top.jiuxialb.javafx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.jiuxialb.javafx.entity.User;
import top.jiuxialb.javafx.mapper.UserMapper;
import top.jiuxialb.javafx.service.UserService;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    // 继承ServiceImpl后已拥有常用的CRUD方法
    // 可以在此处实现自定义的业务逻辑
}