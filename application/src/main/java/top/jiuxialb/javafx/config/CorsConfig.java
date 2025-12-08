package top.jiuxialb.javafx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许任何来源的请求
        config.addAllowedOriginPattern("*");
        // 允许任何头信息
        config.addAllowedHeader("*");
        // 允许任何HTTP方法（GET, POST, PUT, DELETE等）
        config.addAllowedMethod("*");
        // 允许携带凭证信息（如cookies）
        config.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有路径应用CORS配置
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}