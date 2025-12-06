package top.jiuxialb.javafx.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 审计日志工具类
 * 用于记录用户操作、系统事件等关键信息
 *
 * @author zhangjiahao
 * @since 1.0.0
 */
public class AuditLogger {

    private static final Logger logger = LoggerFactory.getLogger("AUDIT_LOGGER");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 记录用户登录操作
     */
    public static void logLogin(String username, String email, boolean success, String message) {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        logger.info("[LOGIN] {} - 用户: {}, 邮箱: {}, 结果: {}, 信息: {}",
                timestamp, username, email, success ? "成功" : "失败", message);
    }

    /**
     * 记录用户注册操作
     */
    public static void logRegister(String username, String email, boolean success, String message) {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        logger.info("[REGISTER] {} - 用户: {}, 邮箱: {}, 结果: {}, 信息: {}",
                timestamp, username, email, success ? "成功" : "失败", message);
    }

    /**
     * 记录表单提交操作
     */
    public static void logFormSubmit(String formType, String user, String data) {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        logger.info("[FORM_SUBMIT] {} - 表单类型: {}, 用户: {}, 数据: {}",
                timestamp, formType, user, data);
    }

    /**
     * 记录表单切换操作
     */
    public static void logFormSwitch(String fromForm, String toForm, String user) {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        logger.info("[FORM_SWITCH] {} - 从 {} 切换到 {}, 用户: {}",
                timestamp, fromForm, toForm, user);
    }

    /**
     * 记录用户退出操作
     */
    public static void logLogout(String username, String reason) {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        logger.info("[LOGOUT] {} - 用户: {}, 原因: {}", timestamp, username, reason);
    }

    /**
     * 记录系统异常
     */
    public static void logSystemError(String operation, String user, String error) {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        logger.error("[SYSTEM_ERROR] {} - 操作: {}, 用户: {}, 错误: {}",
                timestamp, operation, user, error);
    }

    /**
     * 记录数据库操作
     */
    public static void logDatabaseOperation(String operation, String table, String details) {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        logger.info("[DATABASE] {} - 操作: {}, 表: {}, 详情: {}",
                timestamp, operation, table, details);
    }

    /**
     * 记录页面导航
     */
    public static void logNavigation(String fromPage, String toPage, String user) {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        logger.info("[NAVIGATION] {} - 从 {} 导航到 {}, 用户: {}",
                timestamp, fromPage, toPage, user);
    }

    /**
     * 记录用户操作
     */
    public static void logUserAction(String action, String user, String details) {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        logger.info("[USER_ACTION] {} - 操作: {}, 用户: {}, 详情: {}",
                timestamp, action, user, details);
    }
}