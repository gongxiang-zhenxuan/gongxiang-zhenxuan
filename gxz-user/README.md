# GXZ User - 共享臻选用户服务

## 概述

`gxz-user` 是共享臻选微服务架构中的用户管理服务，负责处理用户注册、登录、个人信息管理、权限控制等核心用户相关功能。

## 主要功能

### 👤 用户管理
- **用户注册**: 支持手机号、邮箱注册
- **用户登录**: 多种登录方式（密码、短信验证码、第三方登录）
- **个人信息**: 用户资料管理和修改
- **账户安全**: 密码修改、账户锁定/解锁

### 🔐 认证授权
- **JWT 认证**: 基于 JWT 的无状态认证
- **角色管理**: 用户角色分配和权限控制
- **权限验证**: 细粒度的权限验证机制
- **会话管理**: 用户会话状态管理

### 📱 第三方集成
- **微信登录**: 微信授权登录
- **支付宝登录**: 支付宝授权登录
- **短信服务**: 验证码发送和验证
- **邮件服务**: 邮件通知和验证

## 技术栈

- **Java**: 17+
- **Spring Boot**: 3.x
- **Spring Cloud**: 2023.x
- **Spring Security**: 安全框架
- **MyBatis Plus**: ORM 框架
- **MySQL**: 主数据库
- **Redis**: 缓存和会话存储
- **Nacos**: 服务注册发现
- **JWT**: 认证令牌

## 模块结构

```
gxz-user/
├── user-api/                    # 用户服务 API 模块
│   ├── src/main/java/
│   │   └── com/gongxiang/zhenxuan/user/api/
│   │       ├── dto/            # 数据传输对象
│   │       ├── vo/             # 视图对象
│   │       ├── enums/          # 枚举定义
│   │       └── feign/          # Feign 客户端接口
│   └── pom.xml
├── user-provider/               # 用户服务提供者模块
│   ├── src/main/java/
│   │   └── com/gongxiang/zhenxuan/user/
│   │       ├── controller/     # 控制器层
│   │       ├── service/        # 服务层
│   │       ├── mapper/         # 数据访问层
│   │       ├── entity/         # 实体类
│   │       ├── config/         # 配置类
│   │       └── UserApplication.java
│   ├── src/main/resources/
│   │   ├── application.yml     # 配置文件
│   │   └── mapper/            # MyBatis 映射文件
│   └── pom.xml
└── pom.xml
```

## 核心功能模块

### 用户注册登录
```java
// 用户注册
UserRegisterDTO registerDTO = new UserRegisterDTO();
registerDTO.setPhone("13800138000");
registerDTO.setPassword("password123");
Result<UserVO> result = userService.register(registerDTO);

// 用户登录
UserLoginDTO loginDTO = new UserLoginDTO();
loginDTO.setPhone("13800138000");
loginDTO.setPassword("password123");
Result<LoginVO> loginResult = userService.login(loginDTO);
```

### 权限验证
```java
// 检查用户权限
@PreAuthorize("hasRole('ADMIN')")
public Result<List<UserVO>> getAllUsers() {
    return userService.getAllUsers();
}

// 检查资源权限
@PreAuthorize("hasPermission(#userId, 'USER', 'READ')")
public Result<UserVO> getUserById(@PathVariable Long userId) {
    return userService.getUserById(userId);
}
```

## API 接口

### 用户管理接口
- `POST /api/user/register` - 用户注册
- `POST /api/user/login` - 用户登录
- `POST /api/user/logout` - 用户登出
- `GET /api/user/profile` - 获取用户信息
- `PUT /api/user/profile` - 更新用户信息
- `PUT /api/user/password` - 修改密码

### 认证授权接口
- `POST /api/auth/refresh` - 刷新令牌
- `GET /api/auth/verify` - 验证令牌
- `POST /api/auth/sms/send` - 发送短信验证码
- `POST /api/auth/sms/verify` - 验证短信验证码

### 第三方登录接口
- `GET /api/oauth/wechat/url` - 获取微信登录 URL
- `POST /api/oauth/wechat/callback` - 微信登录回调
- `GET /api/oauth/alipay/url` - 获取支付宝登录 URL
- `POST /api/oauth/alipay/callback` - 支付宝登录回调

## 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gxz_user?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:123456}
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### Redis 配置
```yaml
spring:
  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}
    password: ${REDIS_PASSWORD:}
    database: 1
```

### JWT 配置
```yaml
jwt:
  secret: ${JWT_SECRET:gxz-user-secret-key}
  expiration: 86400000  # 24小时
  refresh-expiration: 604800000  # 7天
```

## 启动方式

### 开发环境
```bash
# 启动 MySQL 和 Redis
docker-compose up -d mysql redis

# 启动用户服务
cd user-provider
mvn spring-boot:run
```

### 生产环境
```bash
# 构建项目
mvn clean package -DskipTests

# 启动服务
java -jar user-provider/target/user-provider-1.0.0.jar
```

## 监控端点

- **健康检查**: `GET /actuator/health`
- **服务信息**: `GET /actuator/info`
- **指标监控**: `GET /actuator/metrics`
- **日志级别**: `GET /actuator/loggers`

## 数据库设计

### 用户表 (t_user)
```sql
CREATE TABLE t_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    phone VARCHAR(20) UNIQUE,
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50),
    avatar VARCHAR(255),
    gender TINYINT DEFAULT 0,
    birthday DATE,
    status TINYINT DEFAULT 1,
    last_login_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 角色表 (t_role)
```sql
CREATE TABLE t_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) UNIQUE NOT NULL,
    role_code VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255),
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## 开发指南

### 添加新的用户功能
1. 在 `user-api` 模块定义 DTO 和 VO
2. 在 `user-provider` 模块实现业务逻辑
3. 添加相应的数据库表和映射
4. 编写单元测试和集成测试

### 集成第三方登录
1. 配置第三方应用信息
2. 实现 OAuth 回调处理
3. 用户信息映射和绑定
4. 测试登录流程

## 安全考虑

- 🔒 密码使用 BCrypt 加密存储
- 🔒 JWT 令牌设置合理的过期时间
- 🔒 敏感接口添加访问频率限制
- 🔒 用户输入进行严格的参数校验
- 🔒 记录用户操作日志

## 故障排查

### 常见问题
1. **登录失败**: 检查用户名密码、账户状态
2. **令牌过期**: 使用刷新令牌重新获取
3. **权限不足**: 检查用户角色和权限配置
4. **第三方登录失败**: 检查应用配置和网络连接

### 日志查看
```bash
# 查看应用日志
tail -f logs/user-provider.log

# 查看错误日志
grep "ERROR" logs/user-provider.log
```

## 版本信息

- **当前版本**: 1.0.0
- **最低 JDK 版本**: 17
- **Spring Boot 版本**: 3.x
- **Spring Cloud 版本**: 2023.x

---

**维护团队**: 共享臻选开发团队  
**创建时间**: 2024年  
**最后更新**: 2024年