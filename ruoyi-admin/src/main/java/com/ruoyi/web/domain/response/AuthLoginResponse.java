package com.ruoyi.web.domain.response;

import com.ruoyi.system.domain.AppUser;

public class AuthLoginResponse
{
    private String tokenType;

    private String token;

    private AppUser user;

    public String getTokenType()
    {
        return tokenType;
    }

    public void setTokenType(String tokenType)
    {
        this.tokenType = tokenType;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public AppUser getUser()
    {
        return user;
    }

    public void setUser(AppUser user)
    {
        this.user = user;
    }
}
