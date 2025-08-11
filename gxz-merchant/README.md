# GXZ Merchant - 共享臻选商户服务

## 概述

`gxz-merchant` 是共享臻选微服务架构中的商户管理服务，负责处理商户入驻、认证、店铺管理、商户信息维护等核心商户相关功能。

## 主要功能

### 🏪 商户管理
- **商户入驻**: 商户注册和资质审核
- **商户认证**: 身份验证和资质认证
- **店铺管理**: 店铺信息设置和维护
- **商户等级**: 商户等级评定和权益管理

### 📋 资质管理
- **证照管理**: 营业执照、许可证等证照管理
- **审核流程**: 多级审核和状态跟踪
- **资质到期**: 证照到期提醒和续期
- **合规检查**: 定期合规性检查

### 💰 结算管理
- **结算配置**: 商户结算周期和方式设置
- **费率管理**: 平台费率和优惠政策
- **账户管理**: 商户收款账户管理
- **对账服务**: 交易对账和结算明细

### 📊 数据统计
- **经营数据**: 销售额、订单量等经营指标
- **商户画像**: 商户特征分析和标签
- **排行榜**: 商户排名和评比
- **报表导出**: 各类经营报表生成

## 技术栈

- **Java**: 8
- **Spring Boot**: 2.7.x
- **Spring Cloud**: 2021.x
- **MyBatis Plus**: ORM 框架
- **MySQL**: 主数据库
- **Redis**: 缓存存储
- **Elasticsearch**: 搜索引擎
- **RocketMQ**: 消息队列
- **Nacos**: 服务注册发现

## 模块结构

```
gxz-merchant/
├── merchant-api/                # 商户服务 API 模块
│   ├── src/main/java/
│   │   └── com/gongxiang/zhenxuan/merchant/api/
│   │       ├── dto/            # 数据传输对象
│   │       ├── vo/             # 视图对象
│   │       ├── enums/          # 枚举定义
│   │       └── feign/          # Feign 客户端接口
│   └── pom.xml
├── merchant-provider/           # 商户服务提供者模块
│   ├── src/main/java/
│   │   └── com/gongxiang/zhenxuan/merchant/
│   │       ├── controller/     # 控制器层
│   │       ├── service/        # 服务层
│   │       ├── mapper/         # 数据访问层
│   │       ├── entity/         # 实体类
│   │       ├── config/         # 配置类
│   │       └── MerchantApplication.java
│   ├── src/main/resources/
│   │   ├── application.yml     # 配置文件
│   │   └── mapper/            # MyBatis 映射文件
│   └── pom.xml
└── pom.xml
```

## 核心功能模块

### 商户入驻
```java
// 商户注册
MerchantRegisterDTO registerDTO = new MerchantRegisterDTO();
registerDTO.setMerchantName("测试商户");
registerDTO.setContactPhone("13800138000");
registerDTO.setBusinessLicense("91110000000000000X");
Result<MerchantVO> result = merchantService.register(registerDTO);

// 提交审核
MerchantAuditDTO auditDTO = new MerchantAuditDTO();
auditDTO.setMerchantId(1L);
auditDTO.setAuditType(AuditType.QUALIFICATION);
Result<Void> auditResult = merchantService.submitAudit(auditDTO);
```

### 店铺管理
```java
// 创建店铺
ShopCreateDTO shopDTO = new ShopCreateDTO();
shopDTO.setShopName("测试店铺");
shopDTO.setShopType(ShopType.FLAGSHIP);
shopDTO.setAddress("北京市朝阳区");
Result<ShopVO> shopResult = shopService.createShop(shopDTO);

// 更新店铺信息
ShopUpdateDTO updateDTO = new ShopUpdateDTO();
updateDTO.setShopId(1L);
updateDTO.setShopName("新店铺名称");
Result<Void> updateResult = shopService.updateShop(updateDTO);
```

## API 接口

### 商户管理接口
- `POST /api/merchant/register` - 商户注册
- `GET /api/merchant/{id}` - 获取商户信息
- `PUT /api/merchant/{id}` - 更新商户信息
- `POST /api/merchant/{id}/audit` - 提交审核
- `GET /api/merchant/{id}/status` - 获取审核状态

### 店铺管理接口
- `POST /api/shop/create` - 创建店铺
- `GET /api/shop/{id}` - 获取店铺信息
- `PUT /api/shop/{id}` - 更新店铺信息
- `DELETE /api/shop/{id}` - 删除店铺
- `GET /api/shop/list` - 获取店铺列表

### 资质管理接口
- `POST /api/qualification/upload` - 上传资质文件
- `GET /api/qualification/{merchantId}` - 获取商户资质
- `PUT /api/qualification/{id}` - 更新资质信息
- `POST /api/qualification/{id}/verify` - 资质验证

### 结算管理接口
- `GET /api/settlement/config/{merchantId}` - 获取结算配置
- `PUT /api/settlement/config/{merchantId}` - 更新结算配置
- `GET /api/settlement/account/{merchantId}` - 获取结算账户
- `POST /api/settlement/account` - 添加结算账户

## 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gxz_merchant?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
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
    database: 2
```

### Elasticsearch 配置
```yaml
spring:
  elasticsearch:
    uris: ${ES_URIS:http://localhost:9200}
    username: ${ES_USERNAME:}
    password: ${ES_PASSWORD:}
```

### 消息队列配置
```yaml
rocketmq:
  name-server: ${ROCKETMQ_NAME_SERVER:localhost:9876}
  producer:
    group: merchant-producer-group
  consumer:
    group: merchant-consumer-group
```

## 启动方式

### 开发环境
```bash
# 启动依赖服务
docker-compose up -d mysql redis elasticsearch rocketmq

# 启动商户服务
cd merchant-provider
mvn spring-boot:run
```

### 生产环境
```bash
# 构建项目
mvn clean package -DskipTests

# 启动服务
java -jar merchant-provider/target/merchant-provider-1.0.0.jar
```

## 监控端点

- **健康检查**: `GET /actuator/health`
- **服务信息**: `GET /actuator/info`
- **指标监控**: `GET /actuator/metrics`
- **日志级别**: `GET /actuator/loggers`

## 数据库设计

### 商户表 (t_merchant)
```sql
CREATE TABLE t_merchant (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    merchant_code VARCHAR(32) UNIQUE NOT NULL,
    merchant_name VARCHAR(100) NOT NULL,
    merchant_type TINYINT NOT NULL,
    contact_name VARCHAR(50),
    contact_phone VARCHAR(20),
    contact_email VARCHAR(100),
    business_license VARCHAR(50),
    legal_person VARCHAR(50),
    registered_capital DECIMAL(15,2),
    business_scope TEXT,
    status TINYINT DEFAULT 0,
    audit_status TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 店铺表 (t_shop)
```sql
CREATE TABLE t_shop (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    merchant_id BIGINT NOT NULL,
    shop_code VARCHAR(32) UNIQUE NOT NULL,
    shop_name VARCHAR(100) NOT NULL,
    shop_type TINYINT NOT NULL,
    shop_logo VARCHAR(255),
    shop_banner VARCHAR(255),
    description TEXT,
    province VARCHAR(20),
    city VARCHAR(20),
    district VARCHAR(20),
    address VARCHAR(255),
    longitude DECIMAL(10,7),
    latitude DECIMAL(10,7),
    business_hours VARCHAR(100),
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 资质表 (t_qualification)
```sql
CREATE TABLE t_qualification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    merchant_id BIGINT NOT NULL,
    qualification_type TINYINT NOT NULL,
    qualification_name VARCHAR(100),
    qualification_number VARCHAR(100),
    file_url VARCHAR(255),
    issue_date DATE,
    expire_date DATE,
    status TINYINT DEFAULT 0,
    audit_status TINYINT DEFAULT 0,
    audit_remark VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## 业务流程

### 商户入驻流程
1. **注册申请**: 商户提交基本信息
2. **资质上传**: 上传营业执照等证照
3. **初审**: 平台进行资料初审
4. **实地核查**: 必要时进行实地验证
5. **审核通过**: 开通商户权限
6. **店铺创建**: 创建店铺并上架商品

### 审核流程
1. **提交审核**: 商户提交审核申请
2. **自动检查**: 系统自动进行基础检查
3. **人工审核**: 审核人员进行详细审核
4. **审核结果**: 通知审核结果
5. **申诉处理**: 处理审核申诉

## 开发指南

### 添加新的商户功能
1. 在 `merchant-api` 模块定义接口和 DTO
2. 在 `merchant-provider` 模块实现业务逻辑
3. 添加相应的数据库表和映射
4. 编写单元测试和集成测试

### 集成第三方服务
1. 配置第三方服务信息
2. 实现服务调用接口
3. 处理异常和重试机制
4. 添加监控和日志

## 安全考虑

- 🔒 商户敏感信息加密存储
- 🔒 API 接口权限控制
- 🔒 文件上传安全检查
- 🔒 数据访问权限隔离
- 🔒 操作日志记录和审计

## 性能优化

- ⚡ 商户信息缓存策略
- ⚡ 数据库查询优化
- ⚡ 异步处理耗时操作
- ⚡ 分页查询和索引优化
- ⚡ 静态资源 CDN 加速

## 故障排查

### 常见问题
1. **审核失败**: 检查资质文件和信息完整性
2. **店铺创建失败**: 检查商户状态和权限
3. **数据同步异常**: 检查消息队列和网络连接
4. **搜索功能异常**: 检查 Elasticsearch 服务状态

### 日志查看
```bash
# 查看应用日志
tail -f logs/merchant-provider.log

# 查看审核日志
grep "AUDIT" logs/merchant-provider.log

# 查看错误日志
grep "ERROR" logs/merchant-provider.log
```

## 版本信息

- **当前版本**: 1.0.0
- **最低 JDK 版本**: 8
- **Spring Boot 版本**: 2.7.x
- **Spring Cloud 版本**: 2021.x

---

**维护团队**: 共享臻选开发团队  
**创建时间**: 2024年  
**最后更新**: 2024年