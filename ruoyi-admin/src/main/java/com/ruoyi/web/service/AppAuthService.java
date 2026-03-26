package com.ruoyi.web.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.domain.AppUser;
import com.ruoyi.system.service.IAppUserService;
import com.ruoyi.web.domain.request.AppProfileUpdateRequest;
import com.ruoyi.web.domain.request.MiniappLoginBody;
import com.ruoyi.web.domain.response.AuthCurrentUserResponse;
import com.ruoyi.web.domain.response.AuthLoginResponse;

@Service
public class AppAuthService
{
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    @Value("${wx.miniapp.app-id:}")
    private String appId;

    @Value("${wx.miniapp.app-secret:}")
    private String appSecret;

    @Value("${wx.miniapp.mock-login:true}")
    private boolean mockLogin;

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private TokenService tokenService;

    /**
     * 小程序登录。
     * 1. 使用 code 换取 openId（开发环境可走 devOpenId）。
     * 2. 创建或更新用户。
     * 3. 生成 JWT 返回给前端保存。
     */
    public AuthLoginResponse login(MiniappLoginBody loginBody)
    {
        String openId = resolveOpenId(loginBody);
        AppUser appUser = appUserService.createOrUpdateFromLogin(openId, loginBody.getNickName(), loginBody.getAvatarUrl());

        LoginUser loginUser = buildLoginUser(appUser);
        String token = tokenService.createToken(loginUser);

        AuthLoginResponse response = new AuthLoginResponse();
        response.setTokenType(Constants.TOKEN_PREFIX.trim());
        response.setToken(token);
        response.setUser(appUser);
        return response;
    }

    public AuthCurrentUserResponse currentUser()
    {
        AppUser appUser = appUserService.selectByUserId(SecurityUtils.getUserId());
        if (appUser == null)
        {
            throw new ServiceException("User not found");
        }
        AuthCurrentUserResponse response = new AuthCurrentUserResponse();
        response.setUser(appUser);
        response.setPermissions(Collections.emptySet());
        return response;
    }

    public AuthCurrentUserResponse updateCurrentUser(AppProfileUpdateRequest request)
    {
        AppUser appUser = appUserService.updateProfile(
                SecurityUtils.getUserId(),
                request.getNickName(),
                request.getAvatarUrl(),
                request.getGender());
        AuthCurrentUserResponse response = new AuthCurrentUserResponse();
        response.setUser(appUser);
        response.setPermissions(Collections.emptySet());
        return response;
    }

    private LoginUser buildLoginUser(AppUser appUser)
    {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(appUser.getUserId());
        sysUser.setUserName(appUser.getOpenId());
        sysUser.setNickName(appUser.getNickName());
        sysUser.setAvatar(appUser.getAvatarUrl());
        sysUser.setStatus(Constants.SUCCESS);
        sysUser.setDelFlag(Constants.SUCCESS);
        sysUser.setRoles(Collections.emptyList());
        return new LoginUser(appUser.getUserId(), null, sysUser, Collections.emptySet());
    }

    private String resolveOpenId(MiniappLoginBody loginBody)
    {
        if (mockLogin && StringUtils.isNotEmpty(loginBody.getDevOpenId()))
        {
            return loginBody.getDevOpenId();
        }
        if (StringUtils.isEmpty(loginBody.getCode()))
        {
            throw new ServiceException("Mini app login code is required");
        }
        if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(appSecret))
        {
            throw new ServiceException("wx.miniapp.app-id or wx.miniapp.app-secret is not configured");
        }

        HttpRequest request = HttpRequest.newBuilder(URI.create(buildCode2SessionUrl(loginBody.getCode())))
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();
        try
        {
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            JSONObject jsonObject = JSON.parseObject(response.body());
            if (jsonObject == null)
            {
                throw new ServiceException("WeChat login returned an empty response");
            }
            if (jsonObject.getIntValue("errcode") != 0)
            {
                throw new ServiceException("WeChat login failed: " + jsonObject.getString("errmsg"));
            }
            String openId = jsonObject.getString("openid");
            if (StringUtils.isEmpty(openId))
            {
                throw new ServiceException("WeChat login did not return openid");
            }
            return openId;
        }
        catch (IOException e)
        {
            throw new ServiceException("Failed to call WeChat login API: " + e.getMessage());
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            throw new ServiceException("WeChat login request interrupted");
        }
    }

    private String buildCode2SessionUrl(String code)
    {
        return "https://api.weixin.qq.com/sns/jscode2session?appid="
                + encode(appId)
                + "&secret="
                + encode(appSecret)
                + "&js_code="
                + encode(code)
                + "&grant_type=authorization_code";
    }

    private String encode(String value)
    {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
