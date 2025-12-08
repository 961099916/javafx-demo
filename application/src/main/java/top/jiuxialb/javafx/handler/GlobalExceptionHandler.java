package top.jiuxialb.javafx.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.jiuxialb.javafx.common.Result;
import top.jiuxialb.javafx.exception.BusinessException;
import top.jiuxialb.javafx.exception.UserNotFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * 处理用户未找到异常
     */
    @ExceptionHandler(UserNotFoundException.class)
    public Result<Object> handleUserNotFoundException(UserNotFoundException e) {
        // 打印堆栈信息
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        logger.error("User not found exception: {}", sw.toString());
        
        return Result.fail(e.getCode(), e.getMessage());
    }
    
    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Object> handleBusinessException(BusinessException e) {
        // 打印堆栈信息
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        logger.error("Business exception: {}", sw.toString());
        
        return Result.fail(e.getCode(), e.getMessage());
    }
    
    /**
     * 处理参数验证异常(MethodArgumentNotValidException)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 打印堆栈信息
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        logger.error("Method argument validation exception: {}", sw.toString());
        
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errorMessage = new StringBuilder();
        
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errorMessage.append(fieldError.getDefaultMessage()).append("; ");
        }
        
        return Result.validateFail(errorMessage.toString());
    }
    
    /**
     * 处理参数验证异常(BindException)
     */
    @ExceptionHandler(BindException.class)
    public Result<Object> handleBindException(BindException e) {
        // 打印堆栈信息
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        logger.error("Bind exception: {}", sw.toString());
        
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errorMessage = new StringBuilder();
        
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errorMessage.append(fieldError.getDefaultMessage()).append("; ");
        }
        
        return Result.validateFail(errorMessage.toString());
    }
    
    /**
     * 处理系统异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Object> handleException(Exception e) {
        // 打印堆栈信息
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        logger.error("System exception: {}", sw.toString());
        
        return Result.fail("服务器内部错误，请联系管理员");
    }
}