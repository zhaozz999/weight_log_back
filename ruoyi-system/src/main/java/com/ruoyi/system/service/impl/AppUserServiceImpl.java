package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.AppUser;
import com.ruoyi.system.mapper.AppUserMapper;
import com.ruoyi.system.service.IAppUserService;

@Service
public class AppUserServiceImpl implements IAppUserService
{
    @Autowired
    private AppUserMapper appUserMapper;

    @Override
    public List<AppUser> selectUserList(String nickName)
    {
        return appUserMapper.selectUserList(nickName);
    }

    @Override
    public AppUser selectByUserId(Long userId)
    {
        return appUserMapper.selectByUserId(userId);
    }

    @Override
    public AppUser selectByOpenId(String openId)
    {
        return appUserMapper.selectByOpenId(openId);
    }

    @Override
    public AppUser createUser(AppUser appUser)
    {
        Date now = DateUtils.getNowDate();
        AppUser duplicate = appUserMapper.selectByOpenId(appUser.getOpenId());
        if (duplicate != null)
        {
            throw new ServiceException("openId already exists", HttpStatus.BAD_REQUEST);
        }

        appUser.setStatus(StringUtils.isNotEmpty(appUser.getStatus()) ? appUser.getStatus() : Constants.SUCCESS);
        appUser.setCreateTime(now);
        appUser.setUpdateTime(now);
        appUserMapper.insertAppUser(appUser);
        return appUserMapper.selectByUserId(appUser.getUserId());
    }

    @Override
    public AppUser updateUser(Long userId, AppUser appUser)
    {
        AppUser current = appUserMapper.selectByUserId(userId);
        if (current == null)
        {
            throw new ServiceException("User not found", HttpStatus.NOT_FOUND);
        }
        current.setNickName(appUser.getNickName());
        current.setAvatarUrl(appUser.getAvatarUrl());
        current.setGender(appUser.getGender());
        current.setStatus(appUser.getStatus());
        current.setRemark(appUser.getRemark());
        current.setUpdateTime(DateUtils.getNowDate());
        appUserMapper.updateAppUser(current);
        return appUserMapper.selectByUserId(userId);
    }

    @Override
    public AppUser updateProfile(Long userId, String nickName, String avatarUrl, String gender)
    {
        AppUser current = appUserMapper.selectByUserId(userId);
        if (current == null)
        {
            throw new ServiceException("User not found", HttpStatus.NOT_FOUND);
        }
        current.setNickName(nickName);
        current.setAvatarUrl(avatarUrl);
        current.setGender(gender);
        current.setUpdateTime(DateUtils.getNowDate());
        appUserMapper.updateAppUser(current);
        return appUserMapper.selectByUserId(userId);
    }

    @Override
    public void deleteUser(Long userId)
    {
        AppUser current = appUserMapper.selectByUserId(userId);
        if (current == null)
        {
            throw new ServiceException("User not found", HttpStatus.NOT_FOUND);
        }
        appUserMapper.deleteByUserId(userId);
    }

    @Override
    public AppUser createOrUpdateFromLogin(String openId, String nickName, String avatarUrl)
    {
        Date now = DateUtils.getNowDate();
        AppUser appUser = appUserMapper.selectByOpenId(openId);
        if (appUser == null)
        {
            appUser = new AppUser();
            appUser.setOpenId(openId);
            appUser.setNickName(StringUtils.isNotEmpty(nickName) ? nickName : "MiniAppUser");
            appUser.setAvatarUrl(avatarUrl);
            appUser.setGender(null);
            appUser.setStatus(Constants.SUCCESS);
            appUser.setLastLoginTime(now);
            appUser.setCreateTime(now);
            appUser.setUpdateTime(now);
            appUserMapper.insertAppUser(appUser);
            return appUser;
        }

        if (StringUtils.isNotEmpty(nickName))
        {
            appUser.setNickName(nickName);
        }
        if (StringUtils.isNotEmpty(avatarUrl))
        {
            appUser.setAvatarUrl(avatarUrl);
        }
        appUser.setLastLoginTime(now);
        appUser.setUpdateTime(now);
        appUserMapper.updateAppUser(appUser);
        return appUserMapper.selectByUserId(appUser.getUserId());
    }
}
