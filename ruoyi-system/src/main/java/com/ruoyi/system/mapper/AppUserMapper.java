package com.ruoyi.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.AppUser;

public interface AppUserMapper
{
    List<AppUser> selectUserList(@Param("nickName") String nickName);

    AppUser selectByUserId(Long userId);

    AppUser selectByOpenId(String openId);

    int insertAppUser(AppUser appUser);

    int updateAppUser(AppUser appUser);

    int deleteByUserId(Long userId);
}
