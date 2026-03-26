package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.AppUser;

public interface IAppUserService
{
    List<AppUser> selectUserList(String nickName);

    AppUser selectByUserId(Long userId);

    AppUser selectByOpenId(String openId);

    AppUser createUser(AppUser appUser);

    AppUser updateUser(Long userId, AppUser appUser);

    AppUser updateProfile(Long userId, String nickName, String avatarUrl, String gender);

    void deleteUser(Long userId);

    AppUser createOrUpdateFromLogin(String openId, String nickName, String avatarUrl);
}
