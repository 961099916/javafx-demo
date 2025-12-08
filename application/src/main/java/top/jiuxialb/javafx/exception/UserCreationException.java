package top.jiuxialb.javafx.exception;

/**
 * 用户创建异常
 */
public class UserCreationException extends BusinessException {
    private static final long serialVersionUID = 1L;

    public UserCreationException() {
        super(500, "用户创建失败");
    }

    public UserCreationException(String message) {
        super(500, message);
    }

    public UserCreationException(String message, Throwable cause) {
        super(500, message, cause);
    }
}