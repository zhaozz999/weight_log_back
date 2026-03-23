package com.ruoyi.web.domain.request;

import jakarta.validation.constraints.Size;

public class MiniappLoginBody
{
    @Size(max = 200, message = "微信登录 code 长度不能超过 200")
    private String code;

    @Size(max = 64, message = "开发环境 openId 长度不能超过 64")
    private String devOpenId;

    @Size(max = 64, message = "昵称长度不能超过 64")
    private String nickName;

    @Size(max = 255, message = "头像地址长度不能超过 255")
    private String avatarUrl;

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getDevOpenId()
    {
        return devOpenId;
    }

    public void setDevOpenId(String devOpenId)
    {
        this.devOpenId = devOpenId;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getAvatarUrl()
    {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl)
    {
        this.avatarUrl = avatarUrl;
    }
}
