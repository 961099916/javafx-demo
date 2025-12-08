package top.jiuxialb.javafx.exception;

/**
 * 用户未找到异常
 */
public class UserNotFoundException extends BusinessException {
    private static final long serialVersionUID = 1L;

    public UserNotFoundException() {
        super(404, "用户不存在");
    }

    public UserNotFoundException(String message) {
        super(404, message);
    }

    public UserNotFoundException(Long userId) {
        super(404, "用户不存在，ID: " + userId);
    }
}