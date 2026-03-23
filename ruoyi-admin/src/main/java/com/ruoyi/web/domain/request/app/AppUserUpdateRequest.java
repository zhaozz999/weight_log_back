package com.ruoyi.web.domain.request.app;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AppUserUpdateRequest
{
    @NotBlank(message = "nickName 不能为空")
    @Size(max = 64, message = "nickName 长度不能超过 64")
    private String nickName;

    @Size(max = 255, message = "avatarUrl 长度不能超过 255")
    private String avatarUrl;

    @Size(max = 1, message = "status 长度不能超过 1")
    private String status;

    @Size(max = 255, message = "remark 长度不能超过 255")
    private String remark;

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

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }
}
