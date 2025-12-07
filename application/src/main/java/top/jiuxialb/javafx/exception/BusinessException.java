package top.jiuxialb.javafx.exception;

/**
 * 业务异常类
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    private int code = 500; // 默认错误码
    
    public BusinessException() {
        super("服务器内部错误");
    }
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
    
    public BusinessException(Throwable cause) {
        super(cause);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
}