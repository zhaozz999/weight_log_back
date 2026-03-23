package com.ruoyi.framework.web.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.UserAgentUtils;
import com.ruoyi.common.utils.ip.AddressUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenService
{
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    @Value("${token.header}")
    private String header;

    @Value("${token.secret}")
    private String secret;

    @Value("${token.expireTime}")
    private int expireTime;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    public LoginUser getLoginUser(HttpServletRequest request)
    {
        String token = getToken(request);
        if (StringUtils.isEmpty(token))
        {
            return null;
        }
        try
        {
            return buildLoginUser(token, parseToken(token));
        }
        catch (Exception e)
        {
            log.error("Failed to parse login token: {}", e.getMessage());
            return null;
        }
    }

    public void setLoginUser(LoginUser loginUser)
    {
    }

    public void delLoginUser(String token)
    {
    }

    public String createToken(LoginUser loginUser)
    {
        setUserAgent(loginUser);
        String token = createToken(buildClaims(loginUser));
        loginUser.setToken(token);
        return token;
    }

    public void verifyToken(LoginUser loginUser)
    {
    }

    public void refreshToken(LoginUser loginUser)
    {
    }

    public void setUserAgent(LoginUser loginUser)
    {
        String userAgent = ServletUtils.getRequest().getHeader("User-Agent");
        String ip = IpUtils.getIpAddr();
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(UserAgentUtils.getBrowser(userAgent));
        loginUser.setOs(UserAgentUtils.getOperatingSystem(userAgent));
    }

    public String getUsernameFromToken(String token)
    {
        return parseToken(token).getSubject();
    }

    private String createToken(Map<String, Object> claims)
    {
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + expireTime * MILLIS_MINUTE);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject((String) claims.get(Constants.JWT_USERNAME))
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Claims parseToken(String token)
    {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private String getToken(HttpServletRequest request)
    {
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX))
        {
            return token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    private Map<String, Object> buildClaims(LoginUser loginUser)
    {
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.JWT_USERID, loginUser.getUserId());
        claims.put(Constants.JWT_USERNAME, loginUser.getUsername());
        claims.put(Constants.JWT_NICKNAME, loginUser.getUser().getNickName());
        claims.put(Constants.JWT_AVATAR, loginUser.getUser().getAvatar());
        claims.put(Constants.JWT_AUTHORITIES, loginUser.getPermissions());
        return claims;
    }

    @SuppressWarnings("unchecked")
    private LoginUser buildLoginUser(String token, Claims claims)
    {
        SysUser user = new SysUser();
        user.setUserId(parseLong(claims.get(Constants.JWT_USERID)));
        user.setUserName(claims.getSubject());
        user.setNickName((String) claims.get(Constants.JWT_NICKNAME));
        user.setAvatar((String) claims.get(Constants.JWT_AVATAR));
        user.setStatus(Constants.SUCCESS);
        user.setDelFlag(Constants.SUCCESS);
        user.setRoles(Collections.emptyList());

        Set<String> permissions = extractPermissions(claims.get(Constants.JWT_AUTHORITIES));
        LoginUser loginUser = new LoginUser(user.getUserId(), null, user, permissions);
        loginUser.setToken(token);
        if (claims.getIssuedAt() != null)
        {
            loginUser.setLoginTime(claims.getIssuedAt().getTime());
        }
        if (claims.getExpiration() != null)
        {
            loginUser.setExpireTime(claims.getExpiration().getTime());
        }
        return loginUser;
    }

    private Long parseLong(Object value)
    {
        if (value == null)
        {
            return null;
        }
        return Long.parseLong(String.valueOf(value));
    }

    @SuppressWarnings("unchecked")
    private Set<String> extractPermissions(Object permissions)
    {
        if (permissions instanceof Set)
        {
            return (Set<String>) permissions;
        }
        if (permissions instanceof Collection<?> collection)
        {
            return collection.stream().map(String::valueOf).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }
}
