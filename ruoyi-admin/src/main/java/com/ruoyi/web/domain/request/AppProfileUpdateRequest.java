package com.ruoyi.web.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AppProfileUpdateRequest
{
    @NotBlank(message = "nickName is required")
    @Size(max = 64, message = "nickName length must be <= 64")
    private String nickName;

    @Size(max = 255, message = "avatarUrl length must be <= 255")
    private String avatarUrl;

    @Size(max = 16, message = "gender length must be <= 16")
    private String gender;

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

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }
}
