package top.jiuxialb.javafx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.jiuxialb.javafx.common.Result;
import top.jiuxialb.javafx.entity.User;
import top.jiuxialb.javafx.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
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
                return Result.fail(404, "用户不存在");
            }
        } catch (Exception e) {
            return Result.fail("获取用户详情失败");
        }
    }
    
    /**
     * 创建用户
     */
    @PostMapping
    public Result<User> createUser(@RequestBody User user) {
        try {
            // 简单的数据校验
            if (user.getName() == null || user.getName().trim().isEmpty()) {
                return Result.validateFail("用户名不能为空");
            }
            
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                return Result.validateFail("邮箱不能为空");
            }
            
            boolean saved = userService.save(user);
            if (saved) {
                return Result.success(user, "用户创建成功");
            } else {
                return Result.fail("用户创建失败");
            }
        } catch (Exception e) {
            return Result.fail("创建用户失败");
        }
    }
    
    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            // 检查用户是否存在
            User existingUser = userService.getById(id);
            if (existingUser == null) {
                return Result.fail(404, "用户不存在");
            }
            
            user.setId(id);
            boolean updated = userService.updateById(user);
            if (updated) {
                return Result.success(user, "用户更新成功");
            } else {
                return Result.fail("用户更新失败");
            }
        } catch (Exception e) {
            return Result.fail("更新用户失败");
        }
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        try {
            // 检查用户是否存在
            User existingUser = userService.getById(id);
            if (existingUser == null) {
                return Result.fail(404, "用户不存在");
            }
            
            boolean removed = userService.removeById(id);
            if (removed) {
                return Result.success(null, "用户删除成功");
            } else {
                return Result.fail("用户删除失败");
            }
        } catch (Exception e) {
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
            return Result.fail("根据邮箱查询用户失败");
        }
    }
}