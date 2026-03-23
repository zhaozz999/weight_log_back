package com.ruoyi.web.controller.app;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.web.domain.request.MiniappLoginBody;
import com.ruoyi.web.domain.response.AuthCurrentUserResponse;
import com.ruoyi.web.domain.response.AuthLoginResponse;
import com.ruoyi.web.service.AppAuthService;

@RestController
@RequestMapping("/app/auth")
public class AppAuthController
{
    @Autowired
    private AppAuthService appAuthService;

    @PostMapping("/login")
    public Result<AuthLoginResponse> login(@Valid @RequestBody MiniappLoginBody loginBody)
    {
        return Result.success("登录成功", appAuthService.login(loginBody));
    }

    @GetMapping("/me")
    public Result<AuthCurrentUserResponse> me()
    {
        return Result.success(appAuthService.currentUser());
    }

    @PostMapping("/logout")
    public Result<Void> logout()
    {
        return Result.success("退出成功", null);
    }
}
