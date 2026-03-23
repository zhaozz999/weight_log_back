package com.ruoyi.web.domain.response;

import java.util.Set;
import com.ruoyi.system.domain.AppUser;

public class AuthCurrentUserResponse
{
    private AppUser user;

    private Set<String> permissions;

    public AppUser getUser()
    {
        return user;
    }

    public void setUser(AppUser user)
    {
        this.user = user;
    }

    public Set<String> getPermissions()
    {
        return permissions;
    }

    public void setPermissions(Set<String> permissions)
    {
        this.permissions = permissions;
    }
}
