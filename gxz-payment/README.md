# GXZ Payment - 共享臻选支付服务

## 概述

`gxz-payment` 是共享臻选微服务架构中的支付管理服务，负责处理支付渠道集成、支付流程管理、退款处理、对账服务等核心支付相关功能。

## 主要功能

### 💳 支付管理
- **多渠道支付**: 支持微信、支付宝、银联等多种支付方式
- **支付流程**: 统一的支付流程和状态管理
- **支付安全**: 支付数据加密和安全验证
- **支付限额**: 支付金额限制和风控管理

### 🔄 退款管理
- **退款申请**: 支持全额和部分退款
- **退款审核**: 退款申请审核和批准流程
- **退款处理**: 自动化退款处理和状态跟踪
- **退款对账**: 退款数据对账和异常处理

### 📊 对账服务
- **日对账**: 每日交易数据对账
- **差异处理**: 对账差异识别和处理
- **报表生成**: 支付报表和统计分析
- **数据同步**: 与第三方支付平台数据同步

### 🛡️ 风控管理
- **风险识别**: 异常交易识别和拦截
- **限额控制**: 用户和商户支付限额管理
- **黑名单**: 风险用户和设备黑名单
- **反欺诈**: 支付欺诈检测和防护

## 技术栈

- **Java**: 8
- **Spring Boot**: 2.7.x
- **Spring Cloud**: 2021.x
- **MyBatis Plus**: ORM 框架
- **MySQL**: 主数据库
- **Redis**: 缓存和分布式锁
- **RocketMQ**: 消息队列
- **XXL-Job**: 定时任务
- **Nacos**: 服务注册发现
- **Hutool**: 工具类库

## 模块结构

```
gxz-payment/
├── payment-api/                 # 支付服务 API 模块
│   ├── src/main/java/
│   │   └── com/gongxiang/zhenxuan/payment/api/
│   │       ├── dto/            # 数据传输对象
│   │       ├── vo/             # 视图对象
│   │       ├── enums/          # 枚举定义
│   │       └── feign/          # Feign 客户端接口
│   └── pom.xml
├── payment-provider/            # 支付服务提供者模块
│   ├── src/main/java/
│   │   └── com/gongxiang/zhenxuan/payment/
│   │       ├── controller/     # 控制器层
│   │       ├── service/        # 服务层
│   │       ├── mapper/         # 数据访问层
│   │       ├── entity/         # 实体类
│   │       ├── config/         # 配置类
│   │       ├── channel/        # 支付渠道
│   │       ├── strategy/       # 支付策略
│   │       ├── job/            # 定时任务
│   │       └── PaymentApplication.java
│   ├── src/main/resources/
│   │   ├── application.yml     # 配置文件
│   │   └── mapper/            # MyBatis 映射文件
│   └── pom.xml
└── pom.xml
```

## 核心功能模块

### 支付处理
```java
// 创建支付订单
PaymentCreateDTO createDTO = new PaymentCreateDTO();
createDTO.setOrderId(1L);
createDTO.setPayType(PayType.WECHAT);
createDTO.setPayAmount(new BigDecimal("99.00"));
createDTO.setSubject("商品支付");
Result<PaymentVO> result = paymentService.createPayment(createDTO);

// 查询支付状态
Result<PaymentStatusVO> statusResult = paymentService.queryPaymentStatus("PAY202401010001");
```

### 退款处理
```java
// 申请退款
RefundCreateDTO refundDTO = new RefundCreateDTO();
refundDTO.setPaymentId(1L);
refundDTO.setRefundAmount(new BigDecimal("50.00"));
refundDTO.setRefundReason("用户申请退款");
Result<RefundVO> refundResult = refundService.createRefund(refundDTO);

// 查询退款状态
Result<RefundStatusVO> refundStatusResult = refundService.queryRefundStatus("REF202401010001");
```

### 对账处理
```java
// 执行日对账
ReconciliationDTO reconDTO = new ReconciliationDTO();
reconDTO.setReconciliationDate(LocalDate.now().minusDays(1));
reconDTO.setPayChannel(PayChannel.WECHAT);
Result<ReconciliationVO> reconResult = reconciliationService.executeReconciliation(reconDTO);
```

## API 接口

### 支付管理接口
- `POST /api/payment/create` - 创建支付订单
- `GET /api/payment/{paymentNo}/status` - 查询支付状态
- `POST /api/payment/callback/{channel}` - 支付回调接口
- `POST /api/payment/cancel` - 取消支付
- `GET /api/payment/methods` - 获取支付方式

### 退款管理接口
- `POST /api/refund/create` - 创建退款申请
- `GET /api/refund/{refundNo}/status` - 查询退款状态
- `POST /api/refund/callback/{channel}` - 退款回调接口
- `GET /api/refund/list` - 获取退款列表

### 对账服务接口
- `POST /api/reconciliation/execute` - 执行对账
- `GET /api/reconciliation/{date}/result` - 获取对账结果
- `GET /api/reconciliation/differences` - 获取对账差异
- `POST /api/reconciliation/fix` - 修复对账差异

### 风控管理接口
- `POST /api/risk/check` - 风险检查
- `GET /api/risk/rules` - 获取风控规则
- `PUT /api/risk/rules` - 更新风控规则
- `POST /api/risk/blacklist` - 添加黑名单

## 支付渠道配置

### 微信支付配置
```yaml
payment:
  wechat:
    app-id: ${WECHAT_APP_ID}
    mch-id: ${WECHAT_MCH_ID}
    api-key: ${WECHAT_API_KEY}
    cert-path: ${WECHAT_CERT_PATH}
    notify-url: ${WECHAT_NOTIFY_URL}
```

### 支付宝配置
```yaml
payment:
  alipay:
    app-id: ${ALIPAY_APP_ID}
    private-key: ${ALIPAY_PRIVATE_KEY}
    public-key: ${ALIPAY_PUBLIC_KEY}
    notify-url: ${ALIPAY_NOTIFY_URL}
    return-url: ${ALIPAY_RETURN_URL}
```

### 银联支付配置
```yaml
payment:
  unionpay:
    mer-id: ${UNIONPAY_MER_ID}
    cert-path: ${UNIONPAY_CERT_PATH}
    cert-password: ${UNIONPAY_CERT_PASSWORD}
    notify-url: ${UNIONPAY_NOTIFY_URL}
```

## 数据库配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gxz_payment?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:123456}
    driver-class-name: com.mysql.cj.jdbc.Driver
```

## 启动方式

### 开发环境
```bash
# 启动依赖服务
docker-compose up -d mysql redis rocketmq

# 启动支付服务
cd payment-provider
mvn spring-boot:run
```

### 生产环境
```bash
# 构建项目
mvn clean package -DskipTests

# 启动服务
java -jar payment-provider/target/payment-provider-1.0.0.jar
```

## 监控端点

- **健康检查**: `GET /actuator/health`
- **服务信息**: `GET /actuator/info`
- **指标监控**: `GET /actuator/metrics`
- **日志级别**: `GET /actuator/loggers`

## 数据库设计

### 支付订单表 (t_payment_order)
```sql
CREATE TABLE t_payment_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    payment_no VARCHAR(32) UNIQUE NOT NULL,
    order_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    merchant_id BIGINT NOT NULL,
    pay_type TINYINT NOT NULL,
    pay_channel VARCHAR(20) NOT NULL,
    pay_amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'CNY',
    subject VARCHAR(255) NOT NULL,
    body VARCHAR(500),
    status TINYINT DEFAULT 0,
    third_party_no VARCHAR(100),
    notify_url VARCHAR(255),
    return_url VARCHAR(255),
    expire_time DATETIME,
    pay_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 退款订单表 (t_refund_order)
```sql
CREATE TABLE t_refund_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    refund_no VARCHAR(32) UNIQUE NOT NULL,
    payment_id BIGINT NOT NULL,
    payment_no VARCHAR(32) NOT NULL,
    refund_amount DECIMAL(10,2) NOT NULL,
    refund_reason VARCHAR(255),
    status TINYINT DEFAULT 0,
    third_party_refund_no VARCHAR(100),
    refund_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 对账记录表 (t_reconciliation_record)
```sql
CREATE TABLE t_reconciliation_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    reconciliation_date DATE NOT NULL,
    pay_channel VARCHAR(20) NOT NULL,
    total_count INT DEFAULT 0,
    total_amount DECIMAL(15,2) DEFAULT 0,
    success_count INT DEFAULT 0,
    success_amount DECIMAL(15,2) DEFAULT 0,
    difference_count INT DEFAULT 0,
    difference_amount DECIMAL(15,2) DEFAULT 0,
    status TINYINT DEFAULT 0,
    file_path VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_date_channel (reconciliation_date, pay_channel)
);
```

### 风控规则表 (t_risk_rule)
```sql
CREATE TABLE t_risk_rule (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    rule_name VARCHAR(100) NOT NULL,
    rule_type TINYINT NOT NULL,
    rule_condition TEXT NOT NULL,
    rule_action TINYINT NOT NULL,
    priority INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    description VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## 业务流程

### 支付流程
1. **创建支付**: 接收支付请求创建支付订单
2. **渠道路由**: 根据支付方式路由到对应渠道
3. **风险检查**: 执行风控规则检查
4. **调用支付**: 调用第三方支付接口
5. **状态更新**: 根据支付结果更新状态
6. **通知回调**: 通知业务系统支付结果

### 退款流程
1. **退款申请**: 接收退款申请
2. **退款审核**: 审核退款申请合法性
3. **调用退款**: 调用第三方退款接口
4. **状态跟踪**: 跟踪退款处理状态
5. **退款完成**: 退款到账完成

### 对账流程
1. **下载对账单**: 从第三方平台下载对账文件
2. **数据解析**: 解析对账文件数据
3. **数据比对**: 与本地交易数据比对
4. **差异识别**: 识别对账差异记录
5. **差异处理**: 处理和修复差异数据

## 支付渠道适配

### 渠道接口统一
```java
public interface PaymentChannel {
    PayResult pay(PayRequest request);
    PayStatusResult queryStatus(String paymentNo);
    RefundResult refund(RefundRequest request);
    RefundStatusResult queryRefundStatus(String refundNo);
}
```

### 微信支付适配
```java
@Component
public class WechatPaymentChannel implements PaymentChannel {
    @Override
    public PayResult pay(PayRequest request) {
        // 微信支付实现
    }
}
```

## 安全措施

### 数据加密
- 支付敏感信息 AES 加密存储
- 传输数据 HTTPS 加密
- API 签名验证

### 访问控制
- IP 白名单限制
- API 访问频率限制
- 权限验证和授权

### 风险控制
- 异常交易监控
- 支付限额控制
- 设备指纹识别

## 性能优化

- ⚡ 支付数据缓存策略
- ⚡ 异步处理支付回调
- ⚡ 数据库读写分离
- ⚡ 支付渠道负载均衡
- ⚡ 批量处理对账数据

## 故障排查

### 常见问题
1. **支付失败**: 检查支付渠道配置和网络连接
2. **回调异常**: 检查回调 URL 和签名验证
3. **对账差异**: 检查时间同步和数据格式
4. **退款失败**: 检查退款条件和余额

### 日志查看
```bash
# 查看应用日志
tail -f logs/payment-provider.log

# 查看支付日志
grep "PAYMENT" logs/payment-provider.log

# 查看退款日志
grep "REFUND" logs/payment-provider.log

# 查看对账日志
grep "RECONCILIATION" logs/payment-provider.log

# 查看错误日志
grep "ERROR" logs/payment-provider.log
```

## 开发指南

### 添加新的支付渠道
1. 实现 `PaymentChannel` 接口
2. 配置渠道参数和证书
3. 实现支付和退款逻辑
4. 添加回调处理
5. 编写单元测试

### 添加风控规则
1. 定义规则条件和动作
2. 实现规则执行引擎
3. 配置规则优先级
4. 测试规则效果

## 版本信息

- **当前版本**: 1.0.0
- **最低 JDK 版本**: 8
- **Spring Boot 版本**: 2.7.x
- **Spring Cloud 版本**: 2021.x

---

**维护团队**: 共享臻选开发团队  
**创建时间**: 2024年  
**最后更新**: 2024年