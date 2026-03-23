package com.ruoyi.framework.security.handle;

import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.ServletUtils;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint
{
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException
    {
        response.setStatus(HttpStatus.UNAUTHORIZED);
        String message = "登录状态已失效，请重新登录";
        ServletUtils.renderString(response, JSON.toJSONString(Result.fail(HttpStatus.UNAUTHORIZED, message)));
    }
}
