# templateBack

后端模块（Spring Boot + MyBatis），当前用于提供微信小程序模板 API。

## 当前配置

- 默认端口：`8086`
- 默认 profile：`dev`
- `dev` 数据库：`mysql://localhost:3306/unit_template`（root/root）
- 统一响应：`Result<T>`
- 认证：微信登录 + JWT

## 启动

```bash
cd templateBack
mvn -pl ruoyi-admin -am spring-boot:run
```

## 关键能力

- `/app/auth/login`：登录
- `/app/auth/me`：当前用户
- `/app/users`：用户管理 CRUD 样板

详细说明请查看仓库根目录 `README.md`。