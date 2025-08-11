# GXZ Gateway - 共享臻选网关服务

## 概述

`gxz-gateway` 是共享臻选微服务架构中的API网关服务，基于Spring Cloud Gateway构建，提供统一的入口点来处理所有客户端请求，并将它们路由到相应的后端微服务。

## 主要功能

### 1. 路由管理
- 基于路径的智能路由
- 支持负载均衡
- 动态路由配置
- 服务发现集成

### 2. 安全认证
- JWT令牌验证
- 用户身份认证
- 权限控制
- 安全路径配置

### 3. 限流保护
- 基于IP的请求限流
- 防止API恶意调用
- 可配置的限流策略
- 优雅的限流响应

### 4. 跨域支持
- CORS配置
- 支持所有主流浏览器
- 灵活的跨域策略

### 5. 监控与日志
- 请求日志记录
- 性能监控
- 健康检查
- 异常处理

## 技术栈

- **Spring Cloud Gateway**: 网关核心框架
- **Spring Cloud LoadBalancer**: 负载均衡
- **Nacos**: 服务注册与发现
- **Spring Boot Actuator**: 监控和管理
- **Lombok**: 简化代码
- **Jackson**: JSON处理

## 项目结构

```
gxz-gateway/
├── src/main/java/com/gongxiang/zhenxuan/gateway/
│   ├── GatewayApplication.java          # 启动类
│   ├── config/
│   │   └── GatewayConfig.java           # 网关配置
│   ├── controller/
│   │   └── HealthController.java        # 健康检查控制器
│   ├── exception/
│   │   └── GlobalExceptionHandler.java  # 全局异常处理
│   └── filter/
│       ├── AuthFilter.java              # 认证过滤器
│       ├── GlobalRequestLogFilter.java  # 请求日志过滤器
│       └── RateLimitFilter.java         # 限流过滤器
├── src/main/resources/
│   └── application.yml                  # 配置文件
├── start.sh                            # 启动脚本
├── pom.xml                             # Maven配置
└── README.md                           # 项目文档
```

## 路由配置

网关配置了以下服务路由：

| 服务 | 路径前缀 | 目标服务 | 说明 |
|------|----------|----------|------|
| 用户服务 | `/api/user/**` | `gxz-user` | 用户注册、登录、信息管理 |
| 商户服务 | `/api/merchant/**` | `gxz-merchant` | 商户注册、认证、管理 |
| 商品服务 | `/api/goods/**` | `gxz-goods` | 商品信息、分类、搜索 |
| 订单服务 | `/api/order/**` | `gxz-order` | 订单创建、查询、管理 |
| 支付服务 | `/api/payment/**` | `gxz-payment` | 支付处理、退款 |
| 营销服务 | `/api/promotion/**` | `gxz-promotion` | 优惠券、活动管理 |
| 配送服务 | `/api/delivery/**` | `gxz-delivery` | 物流跟踪、配送管理 |
| 结算服务 | `/api/settlement/**` | `gxz-settlement` | 商户结算、财务管理 |
| 钱包服务 | `/api/wallet/**` | `gxz-wallet` | 用户钱包、余额管理 |
| 内容服务 | `/api/content/**` | `gxz-content` | 内容管理、推荐 |
| 管理后台 | `/api/admin/**` | `gxz-admin` | 系统管理、运营工具 |

## 安全配置

### 免认证路径
以下路径无需JWT认证：
- `/api/user/login` - 用户登录
- `/api/user/register` - 用户注册
- `/api/user/captcha` - 验证码
- `/api/merchant/login` - 商户登录
- `/api/merchant/register` - 商户注册
- `/api/goods/list` - 商品列表
- `/api/goods/detail` - 商品详情
- `/api/content/banner` - 轮播图
- `/api/content/notice` - 公告
- `/actuator/**` - 监控端点

### JWT认证
- 请求头：`Authorization: Bearer <token>`
- 自动提取用户信息并添加到请求头：
  - `X-User-Id`: 用户ID
  - `X-User-Type`: 用户类型

## 限流配置

- **限流策略**: 每个IP每分钟最多100个请求
- **限流窗口**: 1分钟
- **限流响应**: HTTP 429 Too Many Requests
- **限流头信息**:
  - `X-RateLimit-Limit`: 限制数量
  - `X-RateLimit-Remaining`: 剩余请求数
  - `X-RateLimit-Reset`: 重置时间

## 启动方式

### 1. 开发环境
```bash
# 使用Maven启动
mvn spring-boot:run

# 或者使用启动脚本
./start.sh start
```

### 2. 生产环境
```bash
# 构建JAR包
mvn clean package

# 启动服务
./start.sh start

# 查看状态
./start.sh status

# 查看日志
./start.sh logs

# 停止服务
./start.sh stop

# 重启服务
./start.sh restart
```

## 监控端点

- **健康检查**: `GET /health`
- **就绪检查**: `GET /health/ready`
- **存活检查**: `GET /health/live`
- **网关路由**: `GET /actuator/gateway/routes`
- **应用信息**: `GET /actuator/info`

## 配置说明

### 核心配置
- **端口**: 8080
- **服务名**: gxz-gateway
- **注册中心**: Nacos (localhost:8848)

### 环境变量
- `JAVA_OPTS`: JVM参数 (默认: -Xms512m -Xmx1024m -XX:+UseG1GC)
- `SPRING_PROFILES_ACTIVE`: 环境配置 (默认: dev)

## 开发指南

### 添加新的路由
1. 在 `application.yml` 中添加路由配置
2. 如需特殊处理，可添加自定义过滤器
3. 更新安全配置（如需要）

### 自定义过滤器
1. 实现 `GlobalFilter` 接口
2. 添加 `@Component` 注解
3. 设置合适的执行顺序

### 异常处理
- 全局异常处理器会自动处理各种异常
- 返回统一的JSON格式错误响应
- 记录详细的错误日志

## 注意事项

1. **服务依赖**: 确保Nacos注册中心已启动
2. **端口冲突**: 默认端口8080，如有冲突请修改配置
3. **内存配置**: 根据实际负载调整JVM内存参数
4. **日志管理**: 定期清理日志文件，避免磁盘空间不足
5. **安全配置**: 生产环境请更新JWT密钥和CORS配置

## 故障排查

### 常见问题
1. **服务无法启动**: 检查端口占用和Nacos连接
2. **路由失败**: 确认目标服务已注册到Nacos
3. **认证失败**: 检查JWT令牌格式和有效性
4. **跨域问题**: 确认CORS配置是否正确

### 日志查看
```bash
# 实时查看日志
./start.sh logs

# 查看错误日志
grep "ERROR" logs/gxz-gateway.log

# 查看特定时间段日志
grep "2024-01-01" logs/gxz-gateway.log
```

## 版本信息

- **当前版本**: 1.0.0-SNAPSHOT
- **Spring Boot**: 2.7.x
- **Spring Cloud**: 2021.x
- **Java版本**: 8+

---

**作者**: gongxiang-zhenxuan  
**创建时间**: 2024年  
**最后更新**: 2024年