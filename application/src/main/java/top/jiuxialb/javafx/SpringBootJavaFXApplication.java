package top.jiuxialb.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
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
    
    // Spring应用上下文
    private static ConfigurableApplicationContext applicationContext;
    
    public static void main(String[] args) {
        // 启动JavaFX应用，这将间接启动Spring Boot应用
        Application.launch(SpringBootJavaFXApplication.class, args);
    }
    
    @Override
    public void init() throws Exception {
        try {
            // 在JavaFX应用初始化时启动Spring Boot应用
            applicationContext = new SpringApplicationBuilder()
                    .sources(SpringBootJavaFXApplication.class)
                    .headless(false)
                    .run(getParameters().getRaw().toArray(new String[0]));
        } catch (Exception e) {
            // 打印详细的错误信息到控制台
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.err.println("Spring Boot startup error: " + sw.toString());
            throw e;
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
                    // 异常处理
                }
                System.exit(0);
            });
            
        } catch (Exception e) {
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
            if (applicationContext != null) {
                applicationContext.close();
            }
            super.stop();
        } catch (Exception e) {
            // 异常处理
        }
    }
    
    // 获取Spring应用上下文
    public static ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }
}