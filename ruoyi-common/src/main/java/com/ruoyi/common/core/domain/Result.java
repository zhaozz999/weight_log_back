package com.ruoyi.common.core.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import com.ruoyi.common.constant.HttpStatus;

/**
 * 统一接口返回体。
 *
 * @param <T> 数据类型
 */
public class Result<T> implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    private int code;

    private String message;

    private T data;

    private LocalDateTime timestamp;

    public Result()
    {
        this.timestamp = LocalDateTime.now();
    }

    public Result(int code, String message, T data)
    {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> Result<T> success()
    {
        return new Result<>(HttpStatus.SUCCESS, "操作成功", null);
    }

    public static <T> Result<T> success(T data)
    {
        return new Result<>(HttpStatus.SUCCESS, "操作成功", data);
    }

    public static <T> Result<T> success(String message, T data)
    {
        return new Result<>(HttpStatus.SUCCESS, message, data);
    }

    public static <T> Result<T> fail(String message)
    {
        return new Result<>(HttpStatus.ERROR, message, null);
    }

    public static <T> Result<T> fail(int code, String message)
    {
        return new Result<>(code, message, null);
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public LocalDateTime getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp)
    {
        this.timestamp = timestamp;
    }
}
