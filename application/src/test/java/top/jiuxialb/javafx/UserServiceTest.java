package top.jiuxialb.javafx;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import top.jiuxialb.javafx.entity.User;
import top.jiuxialb.javafx.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
    "spring.main.web-application-type=none"
})
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:sqlite::memory:",
    "spring.flyway.enabled=false"
})
public class UserServiceTest {
    
    @Autowired
    private UserService userService;
    
    @Test
    public void testCreateAndRetrieveUser() {
        // 创建用户
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setAge(25);
        
        try {
            // 保存用户
            boolean saved = userService.save(user);
            assertTrue(saved);
            assertNotNull(user.getId());
            
            // 根据ID检索用户
            User retrievedUser = userService.getById(user.getId());
            assertNotNull(retrievedUser);
            assertEquals("Test User", retrievedUser.getName());
            assertEquals("test@example.com", retrievedUser.getEmail());
            assertEquals(25, retrievedUser.getAge());
            
            // 清理数据
            boolean removed = userService.removeById(user.getId());
            assertTrue(removed);
        } catch (Exception e) {
            fail("测试执行过程中出现异常: " + e.getMessage());
        }
    }
    
    @Test
    public void testUpdateUser() {
        // 创建用户
        User user = new User();
        user.setName("Original Name");
        user.setEmail("original@example.com");
        user.setAge(30);
        
        try {
            // 保存用户
            boolean saved = userService.save(user);
            assertTrue(saved);
            assertNotNull(user.getId());
            
            // 更新用户信息
            user.setName("Updated Name");
            user.setAge(35);
            boolean updated = userService.updateById(user);
            assertTrue(updated);
            
            // 验证更新后的信息
            User updatedUser = userService.getById(user.getId());
            assertNotNull(updatedUser);
            assertEquals("Updated Name", updatedUser.getName());
            assertEquals("original@example.com", updatedUser.getEmail()); // 邮箱不应改变
            assertEquals(35, updatedUser.getAge());
            
            // 清理数据
            boolean removed = userService.removeById(user.getId());
            assertTrue(removed);
        } catch (Exception e) {
            fail("测试执行过程中出现异常: " + e.getMessage());
        }
    }
    
    @Test
    public void testDeleteUser() {
        // 创建用户
        User user = new User();
        user.setName("To Be Deleted");
        user.setEmail("delete@example.com");
        user.setAge(40);
        
        try {
            // 保存用户
            boolean saved = userService.save(user);
            assertTrue(saved);
            assertNotNull(user.getId());
            
            // 删除用户
            boolean deleted = userService.removeById(user.getId());
            assertTrue(deleted);
            
            // 验证用户已被删除
            User deletedUser = userService.getById(user.getId());
            assertNull(deletedUser);
        } catch (Exception e) {
            fail("测试执行过程中出现异常: " + e.getMessage());
        }
    }
}