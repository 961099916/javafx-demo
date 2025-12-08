package top.jiuxialb.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Spring Boot与JavaFX集成的应用程序主类
 * 
 * @author zhangjiahao
 * @since 1.0.0
 */
@SpringBootApplication
public class SpringBootJavaFXApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(SpringBootJavaFXApplication.class);
    
    // Spring应用上下文
    private static ConfigurableApplicationContext applicationContext;
    
    public static void main(String[] args) {
        // 检查是否包含"--web-only"参数
        boolean webOnlyMode = false;
        for (String arg : args) {
            if ("--web-only".equals(arg)) {
                webOnlyMode = true;
                break;
            }
        }
        
        if (webOnlyMode) {
            // 只启动Spring Boot Web应用模式
            logger.info("Starting in web-only mode...");
            applicationContext = new SpringApplicationBuilder(SpringBootJavaFXApplication.class)
                    .headless(true)
                    .run(args);
        } else {
            // 启动JavaFX应用，这将间接启动Spring Boot应用
            logger.info("Starting in JavaFX mode...");
            // 使用嵌套类来避免在web-only模式下加载JavaFX类
            JavaFXApplication.launchWithArgs(args);
        }
    }
    
    /**
     * 嵌套的JavaFX应用类，避免在web-only模式下加载JavaFX相关类
     */
    public static class JavaFXApplication extends Application {
        
        private static String[] savedArgs;
        
        public static void launchWithArgs(String[] args) {
            savedArgs = args;
            Application.launch(JavaFXApplication.class, args);
        }
        
        @Override
        public void init() throws Exception {
            try {
                // 检查并创建数据库目录
                checkAndCreateDatabaseDirectory();
                
                // 在JavaFX应用初始化时启动Spring Boot应用
                applicationContext = new SpringApplicationBuilder()
                        .sources(SpringBootJavaFXApplication.class)
                        .headless(false)
                        .run(savedArgs);
            } catch (Exception e) {
                // 打印详细的错误信息到控制台
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("Spring Boot startup error: " + sw.toString());
                throw e;
            }
        }
        
        /**
         * 检查并创建数据库持久化目录
         */
        private void checkAndCreateDatabaseDirectory() {
            try {
                // 获取用户主目录
                String userHome = System.getProperty("user.home");
                // 构建数据库目录路径
                String databaseDirPath = userHome + File.separator + ".javafx-demo";
                
                // 创建File对象
                File databaseDir = new File(databaseDirPath);
                
                // 检查目录是否存在，如果不存在则创建
                if (!databaseDir.exists()) {
                    boolean created = databaseDir.mkdirs();
                    if (created) {
                        logger.info("成功创建数据库目录: {}", databaseDirPath);
                    } else {
                        logger.error("创建数据库目录失败: {}", databaseDirPath);
                    }
                } else {
                    logger.info("数据库目录已存在: {}", databaseDirPath);
                }
            } catch (Exception e) {
                logger.error("检查或创建数据库目录时发生错误: {}", e.getMessage(), e);
            }
        }
        
        @Override
        public void start(Stage primaryStage) throws Exception {
            try {
                // 创建WebView并加载web/index.html
                WebView webView = new WebView();
                // 启用JavaScript
                webView.getEngine().setJavaScriptEnabled(true);
                
                try {
                    // 加载修复后的index.html
                    String webUrl = getClass().getResource("/web/index.html").toExternalForm();
                    webView.getEngine().load(webUrl);
                } catch (Exception e) {
                    try {
                        // 如果index.html加载失败，尝试加载简单测试页面
                        String simpleUrl = getClass().getResource("/web/simple.html").toExternalForm();
                        webView.getEngine().load(simpleUrl);
                    } catch (Exception ex) {
                        // 如果都无法加载，则显示默认内容
                        webView.getEngine().loadContent("<html><body><h1>JavaFX WebView Test</h1><p>WebView is working correctly.</p><p>Current time: " + new java.util.Date() + "</p></body></html>");
                    }
                }
                
                // 创建场景
                Scene scene = new Scene(webView, 800, 600);
                
                // 配置主窗口
                primaryStage.setTitle("JiuXialb");
                primaryStage.setScene(scene);
                primaryStage.setMinWidth(600);
                primaryStage.setMinHeight(400);
                
                // 显示窗口
                primaryStage.show();
                
                // 添加关闭事件处理
                primaryStage.setOnCloseRequest(event -> {
                    try {
                        stop();
                    } catch (Exception e) {
                        // 异常处理
                    }
                    System.exit(0);
                });
                
            } catch (Exception e) {
                // 打印详细的错误信息到控制台
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("JavaFX startup error: " + sw.toString());
                
                throw e;
            }
        }
        
        @Override
        public void stop() throws Exception {
            try {
                // 关闭Spring应用上下文
                if (applicationContext != null) {
                    applicationContext.close();
                }
                super.stop();
            } catch (Exception e) {
                // 异常处理
            }
        }
    }
    
    // 获取Spring应用上下文
    public static ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }
}