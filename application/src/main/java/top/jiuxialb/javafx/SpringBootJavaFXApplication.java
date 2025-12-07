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

import java.io.PrintWriter;
import java.io.StringWriter;

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
    
    public static void main(String[] args) {
        try {
            logger.info("Starting Spring Boot JavaFX Web Application");
            logger.info("Java version: " + System.getProperty("java.version"));
            logger.info("JavaFX version: " + System.getProperty("javafx.version", "Unknown"));
            logger.info("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));

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
            logger.info("Spring Boot JavaFX Application initialized");
        } catch (Exception e) {
            logger.error("Failed to initialize JavaFX application", e);
            throw e;
        }
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            logger.info("Starting JavaFX Web UI");
            
            // 创建WebView并加载web/index.html
            WebView webView = new WebView();
            // 启用JavaScript
            webView.getEngine().setJavaScriptEnabled(true);
            
            try {
                // 加载修复后的index.html
                String webUrl = getClass().getResource("/web/index.html").toExternalForm();
                logger.info("Attempting to load web application from: {}", webUrl);
                webView.getEngine().load(webUrl);
                logger.info("Successfully loaded web application from: {}", webUrl);
            } catch (Exception e) {
                logger.error("Failed to load web application", e);
                try {
                    // 如果index.html加载失败，尝试加载简单测试页面
                    String simpleUrl = getClass().getResource("/web/simple.html").toExternalForm();
                    logger.info("Attempting to load simple test page from: {}", simpleUrl);
                    webView.getEngine().load(simpleUrl);
                    logger.info("Successfully loaded simple test page from: {}", simpleUrl);
                } catch (Exception ex) {
                    logger.error("Failed to load simple test page", ex);
                    // 如果都无法加载，则显示默认内容
                    webView.getEngine().loadContent("<html><body><h1>JavaFX WebView Test</h1><p>WebView is working correctly.</p><p>Current time: " + new java.util.Date() + "</p></body></html>");
                }
            }
            
            // 创建场景
            Scene scene = new Scene(webView, 800, 600);
            
            // 配置主窗口
            primaryStage.setTitle("JavaFX Web Application");
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
                    logger.error("Error while stopping application", e);
                }
                System.exit(0);
            });
            
            logger.info("JavaFX Web Application started successfully");
        } catch (Exception e) {
            logger.error("Failed to start JavaFX Web Application", e);
            
            // 打印详细的错误信息到控制台
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.err.println("JavaFX startup error: " + sw.toString());
            
            throw e;
        }
    }
    
    /**
     * 读取资源文件内容
     * @param resourcePath 资源路径
     * @return 文件内容
     */
    private String readResourceFile(String resourcePath) {
        try {
            java.io.InputStream inputStream = getClass().getResourceAsStream(resourcePath);
            if (inputStream != null) {
                java.util.Scanner scanner = new java.util.Scanner(inputStream, "UTF-8").useDelimiter("\\A");
                String content = scanner.hasNext() ? scanner.next() : "";
                scanner.close();
                return content;
            }
        } catch (Exception e) {
            logger.error("Failed to read resource file: {}", resourcePath, e);
        }
        return null;
    }
    
    @Override
    public void stop() throws Exception {
        try {
            // 关闭Spring应用上下文
            if (applicationContext != null) {
                applicationContext.close();
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