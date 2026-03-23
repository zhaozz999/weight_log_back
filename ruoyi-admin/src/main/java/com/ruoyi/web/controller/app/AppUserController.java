package com.ruoyi.web.controller.app;

import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.system.domain.AppUser;
import com.ruoyi.system.service.IAppUserService;
import com.ruoyi.web.domain.request.app.AppUserCreateRequest;
import com.ruoyi.web.domain.request.app.AppUserUpdateRequest;
import com.ruoyi.web.domain.response.app.AppUserResponse;

/**
 * 用户管理样板接口（增删改查）。
 */
@RestController
@RequestMapping("/app/users")
public class AppUserController
{
    @Autowired
    private IAppUserService appUserService;

    @GetMapping
    public Result<List<AppUserResponse>> list(@RequestParam(required = false) String nickName)
    {
        List<AppUserResponse> data = appUserService.selectUserList(nickName)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return Result.success(data);
    }

    @GetMapping("/{userId}")
    public Result<AppUserResponse> detail(@PathVariable Long userId)
    {
        AppUser appUser = appUserService.selectByUserId(userId);
        if (appUser == null)
        {
            return Result.fail(HttpStatus.NOT_FOUND, "用户不存在");
        }
        return Result.success(toResponse(appUser));
    }

    @PostMapping
    public Result<AppUserResponse> create(@Valid @RequestBody AppUserCreateRequest request)
    {
        AppUser appUser = new AppUser();
        BeanUtils.copyProperties(request, appUser);
        AppUser created = appUserService.createUser(appUser);
        return Result.success("创建成功", toResponse(created));
    }

    @PutMapping("/{userId}")
    public Result<AppUserResponse> update(@PathVariable Long userId, @Valid @RequestBody AppUserUpdateRequest request)
    {
        AppUser appUser = new AppUser();
        BeanUtils.copyProperties(request, appUser);
        AppUser updated = appUserService.updateUser(userId, appUser);
        return Result.success("更新成功", toResponse(updated));
    }

    @DeleteMapping("/{userId}")
    public Result<Void> delete(@PathVariable Long userId)
    {
        appUserService.deleteUser(userId);
        return Result.success("删除成功", null);
    }

    private AppUserResponse toResponse(AppUser appUser)
    {
        AppUserResponse response = new AppUserResponse();
        BeanUtils.copyProperties(appUser, response);
        return response;
    }
}
