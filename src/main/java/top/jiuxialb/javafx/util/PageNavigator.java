package top.jiuxialb.javafx.util;

import top.jiuxialb.javafx.entity.User;
import top.jiuxialb.javafx.fx.controller.HomeController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 页面导航工具类
 * 
 * @author zhangjiahao
 * @since 1.0.0
 */
@Component
public class PageNavigator {
    
    private static final Logger logger = LoggerFactory.getLogger(PageNavigator.class);
    
    private final ApplicationContext applicationContext;
    
    public PageNavigator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    
    /**
     * 跳转到首页
     * 
     * @param currentStage 当前窗口
     * @param user 登录用户
     */
    public void navigateToHome(Stage currentStage, User user) {
        if (user == null) {
            throw new IllegalArgumentException("用户信息不能为空");
        }
        
        try {
            // 加载首页FXML
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/home.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            
            Parent root = loader.load();
            
            // 获取首页控制器并设置当前用户
            Object controller = loader.getController();
            if (controller instanceof HomeController) {
                ((HomeController) controller).setCurrentUser(user);
            }
            
            // 创建首页窗口
            Stage homeStage = new Stage();
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/css/bootstrap-javafx.css").toExternalForm());
            
            homeStage.setTitle("首页 - 欢迎，" + user.getName());
            homeStage.setScene(scene);
            homeStage.setMinWidth(600);
            homeStage.setMinHeight(400);
            
            // 关闭当前窗口
            if (currentStage != null) {
                currentStage.close();
            }
            
            // 显示首页窗口
            homeStage.show();
        } catch (Exception e) {
            logger.error("跳转到首页时发生错误", e);
            throw new RuntimeException("跳转到首页时发生错误: " + e.getMessage(), e);
        }
    }
    
    /**
     * 跳转到登录页
     * 
     * @param currentStage 当前窗口
     */
    public void navigateToLogin(Stage currentStage) {
        try {
            // 加载登录FXML
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/simple-form.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            
            Parent root = loader.load();
            
            // 创建登录窗口
            Stage loginStage = new Stage();
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/css/bootstrap-javafx.css").toExternalForm());
            
            loginStage.setTitle("用户登录");
            loginStage.setScene(scene);
            loginStage.setMinWidth(600);
            loginStage.setMinHeight(400);
            
            // 关闭当前窗口
            if (currentStage != null) {
                currentStage.close();
            }
            
            // 显示登录窗口
            loginStage.show();
        } catch (Exception e) {
            logger.error("跳转到登录页时发生错误", e);
            throw new RuntimeException("跳转到登录页时发生错误: " + e.getMessage(), e);
        }
    }
}