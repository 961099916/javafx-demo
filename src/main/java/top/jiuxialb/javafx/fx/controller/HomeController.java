package top.jiuxialb.javafx.fx.controller;

import top.jiuxialb.javafx.entity.User;
import top.jiuxialb.javafx.fx.service.FormService;
import top.jiuxialb.javafx.util.PageNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 首页控制器类
 * 
 * @author zhangjiahao
 * @since 1.0.0
 */
@Component
public class HomeController {
    
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    
    private final FormService formService;
    private final PageNavigator pageNavigator;
    
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label ageLabel;
    @FXML
    private Label statusLabel;
    
    // 当前登录用户
    private User currentUser;
    
    public HomeController(FormService formService, PageNavigator pageNavigator) {
        this.formService = formService;
        this.pageNavigator = pageNavigator;
    }
    
    /**
     * 设置当前用户
     * 
     * @param user 当前用户
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateUserInfo();
    }
    
    /**
     * 更新用户信息显示
     */
    private void updateUserInfo() {
        if (currentUser != null) {
            welcomeLabel.setText("欢迎回来，" + currentUser.getName() + "！");
            nameLabel.setText(currentUser.getName());
            emailLabel.setText(currentUser.getEmail());
            ageLabel.setText(currentUser.getAge() != null ? currentUser.getAge().toString() : "未设置");
        }
    }
    
    /**
     * 处理刷新按钮点击
     */
    @FXML
    private void handleRefresh() {
        statusLabel.setText("正在刷新用户信息...");
        
        // 重新从数据库获取用户信息
        if (currentUser != null) {
            User updatedUser = formService.getById(currentUser.getId());
            if (updatedUser != null) {
                this.currentUser = updatedUser;
                updateUserInfo();
                statusLabel.setText("用户信息已刷新");
                logger.info("用户信息已刷新: {}", currentUser.getName());
            } else {
                statusLabel.setText("刷新失败");
                logger.warn("刷新用户信息失败: {}", currentUser.getName());
            }
        }
    }
    
    /**
     * 处理退出登录按钮点击
     */
    @FXML
    private void handleLogout() {
        statusLabel.setText("正在退出登录...");
        
        // 获取当前窗口并跳转到登录页
        try {
            logger.info("用户退出登录: {}", currentUser != null ? currentUser.getName() : "未知用户");
            Stage currentStage = (Stage) statusLabel.getScene().getWindow();
            pageNavigator.navigateToLogin(currentStage);
        } catch (Exception e) {
            logger.error("退出登录时发生错误", e);
            statusLabel.setText("退出登录失败: " + e.getMessage());
        }
    }
}