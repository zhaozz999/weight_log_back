package com.ruoyi.framework.web.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.DemoModeException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.html.EscapeUtil;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AccessDeniedException.class)
    public Result<Void> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request)
    {
        log.error("Access denied: {}", request.getRequestURI(), e);
        return Result.fail(HttpStatus.FORBIDDEN, "无权限访问该资源");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request)
    {
        log.error("Method not supported: {} {}", e.getMethod(), request.getRequestURI(), e);
        return Result.fail(HttpStatus.BAD_METHOD, "请求方法不支持");
    }

    @ExceptionHandler(ServiceException.class)
    public Result<Void> handleServiceException(ServiceException e)
    {
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        return StringUtils.isNotNull(code) ? Result.fail(code, e.getMessage()) : Result.fail(e.getMessage());
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public Result<Void> handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request)
    {
        log.error("Missing path variable on request: {}", request.getRequestURI(), e);
        return Result.fail(HttpStatus.BAD_REQUEST, "路径参数缺失: " + e.getVariableName());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request)
    {
        String value = Convert.toStr(e.getValue());
        if (StringUtils.isNotEmpty(value))
        {
            value = EscapeUtil.clean(value);
        }
        String requiredType = e.getRequiredType() == null ? "unknown" : e.getRequiredType().getName();
        log.error("Argument type mismatch on request: {}", request.getRequestURI(), e);
        return Result.fail(HttpStatus.BAD_REQUEST, String.format(
                "参数[%s]应为[%s]，实际收到[%s]",
                e.getName(),
                requiredType,
                value));
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException e, HttpServletRequest request)
    {
        log.error("Runtime exception on request: {}", request.getRequestURI(), e);
        return Result.fail(HttpStatus.ERROR, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e, HttpServletRequest request)
    {
        log.error("Unhandled exception on request: {}", request.getRequestURI(), e);
        return Result.fail(HttpStatus.ERROR, "服务器内部错误");
    }

    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return Result.fail(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError() == null
                ? "Validation failed"
                : e.getBindingResult().getFieldError().getDefaultMessage();
        return Result.fail(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(DemoModeException.class)
    public Result<Void> handleDemoModeException(DemoModeException e)
    {
        return Result.fail(HttpStatus.FORBIDDEN, "演示模式不允许执行该操作");
    }
}
