package top.jiuxialb.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Spring Boot与JavaFX集成的应用程序主类
 * 
 * @author zhangjiahao
 * @since 1.0.0
 */
@SpringBootApplication
public class SpringBootJavaFXApplication extends Application {
    
    private static final Logger logger = LoggerFactory.getLogger(SpringBootJavaFXApplication.class);
    
    // Spring应用上下文
    private static ConfigurableApplicationContext applicationContext;
    
    // JavaFX应用上下文
    private ConfigurableApplicationContext javaFxApplicationContext;
    
    public static void main(String[] args) {
        try {
            // 创建数据库目录
            try {
                String dbDir = System.getProperty("user.home") + "/.javafx-demo";
                Path dbPath = Paths.get(dbDir);
                if (!Files.exists(dbPath)) {
                    Files.createDirectories(dbPath);
                    logger.info("Created database directory: " + dbDir);
                }
            } catch (Exception e) {
                logger.error("Failed to create database directory", e);
            }

            // 添加系统属性以支持Apple Silicon和其他平台
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.forceGPU", "true");
            System.setProperty("prism.allowhidpi", "true");
            System.setProperty("javafx.verbose", "true");
            System.setProperty("prism.verbose", "true");

            logger.info("Starting Spring Boot JavaFX Application");
            logger.info("Java version: " + System.getProperty("java.version"));
            logger.info("JavaFX version: " + System.getProperty("javafx.version", "Unknown"));
            logger.info("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
            logger.info("Architecture: " + System.getProperty("os.arch"));

            // 启动Spring Boot应用并获取应用上下文
            applicationContext = new SpringApplicationBuilder(SpringBootJavaFXApplication.class)
                    .headless(false)
                    .run(args);

            logger.info("Spring Boot application started successfully");

            // 启动JavaFX应用
            Application.launch(args);
        } catch (Exception e) {
            logger.error("Failed to start application", e);
            
            // 打印详细的错误信息到控制台
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.err.println("Application startup error: " + sw.toString());
            
            System.exit(1);
        }
    }
    
    @Override
    public void init() throws Exception {
        try {
            // 获取Spring应用上下文
            this.javaFxApplicationContext = applicationContext;
            logger.info("Spring Boot JavaFX Application initialized");
        } catch (Exception e) {
            logger.error("Failed to initialize JavaFX application", e);
            throw e;
        }
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            logger.info("Starting JavaFX UI");
            
            // 通过Spring上下文加载FXML控制器
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/simple-form.fxml"));
            loader.setControllerFactory(javaFxApplicationContext::getBean);
            
            // 加载简化表单FXML文件
            Parent root = loader.load();
            
            // 创建场景
            Scene scene = new Scene(root, 800, 600);
            
            // 改进CSS资源加载方式，处理Spring Boot可执行JAR中的资源路径
            try {
                // 方法1：使用相对路径加载CSS
                scene.getStylesheets().add(getClass().getResource("/css/bootstrap-javafx.css").toString());
                logger.info("Successfully loaded CSS using standard path");
            } catch (Exception e1) {
                logger.warn("Failed to load CSS using standard path", e1);
                try {
                    // 方法2：尝试使用不同的路径格式
                    String cssResource = "css/bootstrap-javafx.css";
                    scene.getStylesheets().add(getClass().getClassLoader().getResource(cssResource).toString());
                    logger.info("Successfully loaded CSS using classloader path");
                } catch (Exception e2) {
                    logger.warn("Failed to load CSS using classloader path", e2);
                    try {
                        // 方法3：尝试使用绝对路径
                        scene.getStylesheets().add("/css/bootstrap-javafx.css");
                        logger.info("Successfully loaded CSS using absolute path");
                    } catch (Exception e3) {
                        logger.warn("Failed to load CSS using absolute path", e3);
                        // 如果所有方法都失败，记录警告但继续运行（没有样式）
                        logger.warn("Unable to load CSS stylesheet, continuing without styles");
                    }
                }
            }
            
            // 配置主窗口
            primaryStage.setTitle("Simple JavaFX Form Application with Spring Boot");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(600);
            primaryStage.setMinHeight(400);
            
            // 显示窗口
            primaryStage.show();
            
            // 添加这一行以保持应用程序运行
            primaryStage.setOnCloseRequest(event -> {
                try {
                    stop();
                } catch (Exception e) {
                    logger.error("Error while stopping application", e);
                }
                System.exit(0);
            });
            
            logger.info("JavaFX Application started successfully");
        } catch (Exception e) {
            logger.error("Failed to start JavaFX Application", e);
            
            // 打印详细的错误信息到控制台
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.err.println("JavaFX startup error: " + sw.toString());
            
            throw e;
        }
    }
    
    @Override
    public void stop() throws Exception {
        try {
            // 关闭Spring应用上下文
            if (javaFxApplicationContext != null) {
                javaFxApplicationContext.close();
                logger.info("Spring application context closed");
            }
            super.stop();
        } catch (Exception e) {
            logger.error("Error while stopping application", e);
        }
    }
    
    // 获取Spring应用上下文
    public static ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }
}