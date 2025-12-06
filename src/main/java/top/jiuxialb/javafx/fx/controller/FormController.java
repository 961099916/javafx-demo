package top.jiuxialb.javafx.fx.controller;

import top.jiuxialb.javafx.entity.User;
import top.jiuxialb.javafx.fx.service.FormService;
import top.jiuxialb.javafx.util.AuditLogger;
import top.jiuxialb.javafx.util.PageNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 表单控制器类
 * 
 * @author zhangjiahao
 * @since 1.0.0
 */
@Component
public class FormController {
    
    private static final Logger logger = LoggerFactory.getLogger(FormController.class);
    
    private final FormService formService;
    private final PageNavigator pageNavigator;
    
    // UI组件 - 登录表单
    @FXML
    private GridPane loginForm;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    
    // UI组件 - 注册表单
    @FXML
    private GridPane registerForm;
    @FXML
    private TextField nameField;
    @FXML
    private TextField registerEmailField;
    @FXML
    private PasswordField registerPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Spinner<Integer> ageSpinner;
    
    // UI组件 - 其他
    @FXML
    private Label formTitleLabel;
    @FXML
    private Button switchFormButton;
    @FXML
    private Button submitFormButton;
    @FXML
    private Label statusLabel;
    
    // 表单状态标志
    private boolean isLoginForm = true;
    
    public FormController(FormService formService, PageNavigator pageNavigator) {
        this.formService = formService;
        this.pageNavigator = pageNavigator;
    }
    
    @FXML
    public void initialize() {
        setupFormControls();
    }
    
    /**
     * 初始化表单控件
     */
    private void setupFormControls() {
        SpinnerValueFactory<Integer> ageFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 120, 25);
        ageSpinner.setValueFactory(ageFactory);
    }
    
    /**
     * 切换表单（登录/注册）
     */
    @FXML
    private void handleSwitchForm() {
        String fromForm = isLoginForm ? "登录" : "注册";
        String toForm = isLoginForm ? "注册" : "登录";

        logger.info("用户切换表单：从{}切换到{}", fromForm, toForm);
        AuditLogger.logFormSwitch(fromForm, toForm, getCurrentUser());

        if (isLoginForm) {
            // 切换到注册表单
            loginForm.setVisible(false);
            registerForm.setVisible(true);
            formTitleLabel.setText("用户注册");
            switchFormButton.setText("切换到登录");
            submitFormButton.setText("注册");
            isLoginForm = false;
        } else {
            // 切换到登录表单
            registerForm.setVisible(false);
            loginForm.setVisible(true);
            formTitleLabel.setText("用户登录");
            switchFormButton.setText("切换到注册");
            submitFormButton.setText("登录");
            isLoginForm = true;
        }

        // 清除表单数据
        clearForm();
        statusLabel.setText("欢迎使用本系统");
    }
    
    /**
     * 处理表单提交
     */
    @FXML
    private void handleSubmitForm() {
        String formType = isLoginForm ? "登录" : "注册";
        logger.info("用户提交表单，类型：{}", formType);
        AuditLogger.logFormSubmit(formType, getCurrentUser(), "提交" + formType + "表单");

        if (isLoginForm) {
            handleLogin();
        } else {
            handleRegister();
        }
    }
    
    /**
     * 处理登录
     */
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        logger.info("用户尝试登录，邮箱：{}", email);

        // 验证表单
        if (email.isEmpty() || password.isEmpty()) {
            logger.warn("登录验证失败：字段为空");
            showAlert("验证错误", "请填写所有必填字段。");
            return;
        }

        // 使用服务验证邮箱格式
        if (!formService.isValidEmail(email)) {
            logger.warn("登录验证失败：邮箱格式无效 - {}", email);
            showAlert("验证错误", "请输入有效的邮箱地址。");
            return;
        }

        statusLabel.setText("正在验证登录信息...");

        // 使用服务处理登录
        processLogin(email, password);
    }
    
    /**
     * 处理注册
     */
    private void handleRegister() {
        String name = nameField.getText();
        String email = registerEmailField.getText();
        String password = registerPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        int age = ageSpinner.getValue();

        logger.info("用户尝试注册，名称：{}，邮箱：{}", name, email);

        // 验证表单
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            logger.warn("注册验证失败：字段为空");
            showAlert("验证错误", "请填写所有必填字段。");
            return;
        }

        // 使用服务验证邮箱格式
        if (!formService.isValidEmail(email)) {
            logger.warn("注册验证失败：邮箱格式无效 - {}", email);
            showAlert("验证错误", "请输入有效的邮箱地址。");
            return;
        }

        // 检查密码是否匹配
        if (!password.equals(confirmPassword)) {
            logger.warn("注册验证失败：密码不匹配");
            showAlert("验证错误", "两次输入的密码不匹配。");
            return;
        }

        // 检查邮箱是否已存在
        if (formService.isEmailExists(email)) {
            logger.warn("注册验证失败：邮箱已存在 - {}", email);
            showAlert("验证错误", "该邮箱地址已存在。");
            return;
        }

        statusLabel.setText("正在注册用户...");

        // 使用服务处理注册
        processRegistration(name, email, password, age);
    }
    
    /**
     * 处理登录逻辑
     */
    private void processLogin(String email, String password) {
        logger.debug("开始处理登录逻辑 - {}", email);
        try {
            // 使用Spring服务验证用户
            User user = formService.login(email, password);

            if (user != null) {
                // 登录成功，跳转到首页
                javafx.application.Platform.runLater(() -> {
                    try {
                        logger.info("用户登录成功：{}", user.getName());
                        AuditLogger.logLogin(user.getName(), email, true, "登录成功");
                        // 获取当前窗口
                        Stage currentStage = (Stage) statusLabel.getScene().getWindow();
                        // 使用页面导航工具类跳转到首页
                        pageNavigator.navigateToHome(currentStage, user);
                    } catch (Exception e) {
                        logger.error("跳转到首页时发生错误", e);
                        AuditLogger.logSystemError("跳转首页", user != null ? user.getName() : "unknown", e.getMessage());
                        statusLabel.setText("跳转失败: " + e.getMessage());
                        showAlert("错误", "跳转到首页时发生错误: " + e.getMessage());
                    }
                });
            } else {
                // 登录失败
                logger.warn("用户登录失败：邮箱或密码错误 - {}", email);
                AuditLogger.logLogin("unknown", email, false, "邮箱或密码错误");
                javafx.application.Platform.runLater(() -> {
                    statusLabel.setText("登录失败");
                    showAlert("错误", "邮箱或密码错误。");
                });
            }

        } catch (Exception e) {
            logger.error("登录过程中发生错误", e);
            AuditLogger.logSystemError("用户登录", email, e.getMessage());
            javafx.application.Platform.runLater(() -> {
                statusLabel.setText("登录失败");
                showAlert("错误", "登录过程中发生错误: " + e.getMessage());
            });
        }
    }
    
    /**
     * 处理注册逻辑
     */
    private void processRegistration(String name, String email, String password, int age) {
        logger.debug("开始处理注册逻辑 - 用户名: {}", name);
        try {
            // 使用Spring服务处理注册
            boolean success = formService.register(name, email, password, age);

            if (success) {
                // 注册成功
                logger.info("用户注册成功：{}", name);
                AuditLogger.logRegister(name, email, true, "注册成功");
                javafx.application.Platform.runLater(() -> {
                    statusLabel.setText("注册成功!");
                    showAlert("成功", "用户注册成功!");
                    // 自动切换到登录表单
                    handleSwitchForm();
                });
            } else {
                // 注册失败
                logger.warn("用户注册失败：{}", name);
                AuditLogger.logRegister(name, email, false, "注册失败");
                javafx.application.Platform.runLater(() -> {
                    statusLabel.setText("注册失败");
                    showAlert("错误", "用户注册失败。");
                });
            }

        } catch (Exception e) {
            logger.error("注册过程中发生错误", e);
            AuditLogger.logSystemError("用户注册", name, e.getMessage());
            javafx.application.Platform.runLater(() -> {
                statusLabel.setText("注册失败");
                showAlert("错误", "注册过程中发生错误: " + e.getMessage());
            });
        }
    }
    
    /**
     * 清除表单数据
     */
    private void clearForm() {
        logger.debug("清除表单数据");
        // 清除登录表单
        emailField.clear();
        passwordField.clear();

        // 清除注册表单
        nameField.clear();
        registerEmailField.clear();
        registerPasswordField.clear();
        confirmPasswordField.clear();
        ageSpinner.getValueFactory().setValue(25);
    }
    
    /**
     * 显示提示框
     *
     * @param title 标题
     * @param message 消息
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * 获取当前用户（简化版，返回当前表单中的邮箱或名称）
     */
    private String getCurrentUser() {
        if (isLoginForm) {
            return emailField.getText().isEmpty() ? "anonymous" : emailField.getText();
        } else {
            return nameField.getText().isEmpty() ? "anonymous" : nameField.getText();
        }
    }
}