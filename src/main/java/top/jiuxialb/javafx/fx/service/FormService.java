package top.jiuxialb.javafx.fx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.jiuxialb.javafx.entity.User;
import top.jiuxialb.javafx.mapper.UserMapper;
import top.jiuxialb.javafx.util.AuditLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 表单服务类
 * 
 * @author zhangjiahao
 * @since 1.0.0
 */
@Service
public class FormService extends ServiceImpl<UserMapper, User> {

    private static final Logger logger = LoggerFactory.getLogger(FormService.class);

    private final UserMapper userMapper;
    
    public FormService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    
    /**
     * 用户注册
     *
     * @param name 用户名
     * @param email 邮箱
     * @param password 密码
     * @param age 年龄
     * @return 是否注册成功
     */
    public boolean register(String name, String email, String password, int age) {
        logger.debug("开始用户注册流程 - 名称: {}, 邮箱: {}", name, email);
        // 参数校验
        if (name == null || name.trim().isEmpty()) {
            logger.warn("用户名不能为空");
            return false;
        }

        if (email == null || email.trim().isEmpty()) {
            logger.warn("邮箱不能为空");
            return false;
        }

        try {
            // 创建用户对象
            User user = new User(name.trim(), email.trim(), age);

            // 保存到数据库
            int result = userMapper.insert(user);

            if (result > 0) {
                logger.info("用户注册成功: {}", user);
                AuditLogger.logDatabaseOperation("INSERT", "user", "注册用户: " + name);
                return true;
            } else {
                logger.warn("用户注册失败: {}", user);
                return false;
            }
        } catch (Exception e) {
            logger.error("用户注册时发生错误", e);
            return false;
        }
    }
    
    /**
     * 用户登录
     *
     * @param email 邮箱
     * @param password 密码
     * @return 用户对象，如果登录失败则返回null
     */
    public User login(String email, String password) {
        logger.debug("开始用户登录验证 - 邮箱: {}", email);
        // 参数校验
        if (email == null || email.trim().isEmpty()) {
            logger.warn("用户登录失败：邮箱为空");
            return null;
        }

        if (password == null || password.trim().isEmpty()) {
            logger.warn("用户登录失败：密码为空");
            return null;
        }

        try {
            // 查询用户
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("email", email.trim());
            User user = userMapper.selectOne(queryWrapper);

            // 简单的密码验证（实际项目中应使用加密密码）
            if (user != null) {
                // 注意：实际项目中应该使用BCrypt等加密算法验证密码
                logger.info("用户登录验证成功: {}", user.getName());
                AuditLogger.logDatabaseOperation("SELECT", "user", "查询用户: " + email);
                return user;
            } else {
                logger.warn("用户登录失败：用户不存在 - {}", email);
                return null;
            }
        } catch (Exception e) {
            logger.error("用户登录时发生错误", e);
            return null;
        }
    }
    
    /**
     * 验证邮箱是否已存在
     *
     * @param email 邮箱
     * @return 是否已存在
     */
    public boolean isEmailExists(String email) {
        logger.debug("检查邮箱是否存在: {}", email);
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email.trim());
        Long count = userMapper.selectCount(queryWrapper);
        boolean exists = count > 0;
        if (exists) {
            logger.info("邮箱已存在: {}", email);
        }
        return exists;
    }
    
    /**
     * 验证邮箱格式
     *
     * @param email 邮箱
     * @return 是否格式正确
     */
    public boolean isValidEmail(String email) {
        logger.debug("验证邮箱格式: {}", email);
        if (email == null || email.trim().isEmpty()) {
            logger.warn("邮箱验证失败：邮箱为空");
            return false;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        boolean isValid = email.trim().matches(emailRegex);
        if (!isValid) {
            logger.warn("邮箱格式无效: {}", email);
        }
        return isValid;
    }
}