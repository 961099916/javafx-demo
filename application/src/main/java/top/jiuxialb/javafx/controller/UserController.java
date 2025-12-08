package top.jiuxialb.javafx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.jiuxialb.javafx.common.Result;
import top.jiuxialb.javafx.entity.User;
import top.jiuxialb.javafx.exception.UserNotFoundException;
import top.jiuxialb.javafx.service.UserService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;
    
    /**
     * 获取所有用户
     */
    @GetMapping
    public Result<List<User>> getAllUsers() {
        try {
            List<User> users = userService.list();
            return Result.success(users);
        } catch (Exception e) {
            // 打印堆栈信息
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("获取用户列表失败: {}", sw.toString());
            return Result.fail("获取用户列表失败");
        }
    }
    
    /**
     * 分页获取用户
     */
    @GetMapping("/page")
    public Result<Page<User>> getUsersByPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            Page<User> pageObj = new Page<>(page, size);
            Page<User> result = userService.page(pageObj);
            return Result.success(result);
        } catch (Exception e) {
            // 打印堆栈信息
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("分页获取用户失败: {}", sw.toString());
            return Result.fail("分页获取用户失败");
        }
    }
    
    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getById(id);
            if (user != null) {
                return Result.success(user);
            } else {
                throw new UserNotFoundException(id);
            }
        } catch (UserNotFoundException e) {
            throw e; // Re-throw to be handled by GlobalExceptionHandler
        } catch (Exception e) {
            // 打印堆栈信息
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("获取用户详情失败: {}", sw.toString());
            return Result.fail("获取用户详情失败");
        }
    }
    
    /**
     * 创建用户
     */
    @PostMapping
    public Result<User> createUser(@Valid @RequestBody User user) {
        try {
            boolean saved = userService.save(user);
            if (saved) {
                return Result.success(user, "用户创建成功");
            } else {
                return Result.fail("用户创建失败");
            }
        } catch (Exception e) {
            // 打印堆栈信息
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("创建用户失败: {}", sw.toString());
            return Result.fail("创建用户失败");
        }
    }
    
    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public Result<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        try {
            user.setId(id);
            boolean updated = userService.updateById(user);
            if (updated) {
                // 再次查询获取更新后的用户信息
                User updatedUser = userService.getById(id);
                return Result.success(updatedUser, "用户更新成功");
            } else {
                // 如果没有更新成功，检查用户是否存在
                User existingUser = userService.getById(id);
                if (existingUser == null) {
                    throw new UserNotFoundException(id);
                }
                return Result.fail("用户更新失败");
            }
        } catch (UserNotFoundException e) {
            throw e; // Re-throw to be handled by GlobalExceptionHandler
        } catch (Exception e) {
            // 打印堆栈信息
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("更新用户失败: {}", sw.toString());
            return Result.fail("更新用户失败");
        }
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        try {
            boolean removed = userService.removeById(id);
            if (removed) {
                return Result.success(null, "用户删除成功");
            } else {
                throw new UserNotFoundException(id);
            }
        } catch (UserNotFoundException e) {
            throw e; // Re-throw to be handled by GlobalExceptionHandler
        } catch (Exception e) {
            // 打印堆栈信息
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("删除用户失败: {}", sw.toString());
            return Result.fail("删除用户失败");
        }
    }
    
    /**
     * 根据邮箱查询用户
     */
    @GetMapping("/email/{email}")
    public Result<User> getUserByEmail(@PathVariable String email) {
        try {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("email", email);
            User user = userService.getOne(queryWrapper);
            if (user != null) {
                return Result.success(user);
            } else {
                return Result.fail(404, "用户不存在");
            }
        } catch (Exception e) {
            // 打印堆栈信息
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("根据邮箱查询用户失败: {}", sw.toString());
            return Result.fail("根据邮箱查询用户失败");
        }
    }
}