# GXZ Wallet - 共享臻选钱包服务

## 概述

`gxz-wallet` 是共享臻选微服务架构中的钱包服务，负责处理用户钱包管理、余额充值、消费扣款、转账交易、积分管理、优惠券管理、资金安全等核心钱包相关功能。

## 主要功能

### 💰 钱包管理
- **账户创建**: 用户钱包账户自动创建
- **余额管理**: 可用余额、冻结余额管理
- **多币种支持**: 支持人民币、积分、优惠券等
- **账户安全**: 支付密码、实名认证、风控检查

### 💳 充值服务
- **在线充值**: 支持支付宝、微信、银行卡充值
- **充值优惠**: 充值赠送、满减活动
- **充值记录**: 完整的充值历史记录
- **自动到账**: 充值成功自动到账处理

### 🛒 消费扣款
- **订单支付**: 订单消费余额扣款
- **预授权**: 订单预扣款和释放
- **分期支付**: 支持分期付款功能
- **退款处理**: 消费退款自动处理

### 🔄 转账交易
- **用户转账**: 用户间余额转账
- **商户转账**: 向商户账户转账
- **批量转账**: 批量转账处理
- **转账限额**: 转账金额和频次限制

### 🎯 积分系统
- **积分获取**: 消费获得积分奖励
- **积分消费**: 积分抵扣订单金额
- **积分兑换**: 积分兑换优惠券或商品
- **积分过期**: 积分有效期管理

### 🎫 优惠券管理
- **优惠券发放**: 系统自动发放优惠券
- **优惠券使用**: 订单使用优惠券抵扣
- **优惠券转赠**: 用户间优惠券转赠
- **优惠券过期**: 优惠券有效期管理

## 技术栈

- **Java**: 8
- **Spring Boot**: 2.7.x
- **Spring Cloud**: 2021.x
- **MyBatis Plus**: ORM 框架
- **MySQL**: 主数据库
- **Redis**: 缓存和分布式锁
- **RocketMQ**: 消息队列
- **Redisson**: 分布式锁
- **Spring Security**: 安全框架
- **Nacos**: 服务注册发现

## 模块结构

```
gxz-wallet/
├── wallet-api/                 # 钱包服务 API 模块
│   ├── src/main/java/
│   │   └── com/gongxiang/zhenxuan/wallet/api/
│   │       ├── dto/            # 数据传输对象
│   │       ├── vo/             # 视图对象
│   │       ├── enums/          # 枚举定义
│   │       └── feign/          # Feign 客户端接口
│   └── pom.xml
├── wallet-provider/            # 钱包服务提供者模块
│   ├── src/main/java/
│   │   └── com/gongxiang/zhenxuan/wallet/
│   │       ├── controller/     # 控制器层
│   │       ├── service/        # 服务层
│   │       ├── mapper/         # 数据访问层
│   │       ├── entity/         # 实体类
│   │       ├── config/         # 配置类
│   │       ├── security/       # 安全相关
│   │       ├── transaction/    # 交易处理
│   │       ├── points/         # 积分系统
│   │       ├── coupon/         # 优惠券系统
│   │       ├── risk/           # 风控系统
│   │       └── WalletApplication.java
│   ├── src/main/resources/
│   │   ├── application.yml     # 配置文件
│   │   └── mapper/            # MyBatis 映射文件
│   └── pom.xml
└── pom.xml
```

## 核心功能模块

### 钱包余额管理
```java
// 查询钱包余额
WalletBalanceDTO balanceDTO = new WalletBalanceDTO();
balanceDTO.setUserId(1L);
balanceDTO.setCurrencyType(CurrencyType.CNY);
Result<WalletBalanceVO> balanceResult = walletService.getBalance(balanceDTO);

// 冻结余额
WalletFreezeDTO freezeDTO = new WalletFreezeDTO();
freezeDTO.setUserId(1L);
freezeDTO.setAmount(new BigDecimal("100.00"));
freezeDTO.setBusinessType(BusinessType.ORDER_PAYMENT);
freezeDTO.setBusinessId("ORDER_123456");
Result<Void> freezeResult = walletService.freezeBalance(freezeDTO);

// 解冻余额
WalletUnfreezeDTO unfreezeDTO = new WalletUnfreezeDTO();
unfreezeDTO.setUserId(1L);
unfreezeDTO.setAmount(new BigDecimal("100.00"));
unfreezeDTO.setBusinessId("ORDER_123456");
Result<Void> unfreezeResult = walletService.unfreezeBalance(unfreezeDTO);
```

### 充值服务
```java
// 发起充值
WalletRechargeDTO rechargeDTO = new WalletRechargeDTO();
rechargeDTO.setUserId(1L);
rechargeDTO.setAmount(new BigDecimal("100.00"));
rechargeDTO.setPaymentMethod(PaymentMethod.ALIPAY);
rechargeDTO.setClientIp("192.168.1.1");
Result<WalletRechargeVO> rechargeResult = walletService.recharge(rechargeDTO);

// 充值回调处理
WalletRechargeCallbackDTO callbackDTO = new WalletRechargeCallbackDTO();
callbackDTO.setRechargeId(1L);
callbackDTO.setPaymentStatus(PaymentStatus.SUCCESS);
callbackDTO.setThirdPartyTradeNo("2024010112345678");
Result<Void> callbackResult = walletService.handleRechargeCallback(callbackDTO);
```

### 消费扣款
```java
// 订单支付
WalletPaymentDTO paymentDTO = new WalletPaymentDTO();
paymentDTO.setUserId(1L);
paymentDTO.setOrderId(1L);
paymentDTO.setAmount(new BigDecimal("50.00"));
paymentDTO.setPaymentPassword("123456");
Result<WalletPaymentVO> paymentResult = walletService.payment(paymentDTO);

// 支付退款
WalletRefundDTO refundDTO = new WalletRefundDTO();
refundDTO.setUserId(1L);
refundDTO.setOrderId(1L);
refundDTO.setRefundAmount(new BigDecimal("30.00"));
refundDTO.setRefundReason("商品质量问题");
Result<WalletRefundVO> refundResult = walletService.refund(refundDTO);
```

### 转账交易
```java
// 用户转账
WalletTransferDTO transferDTO = new WalletTransferDTO();
transferDTO.setFromUserId(1L);
transferDTO.setToUserId(2L);
transferDTO.setAmount(new BigDecimal("20.00"));
transferDTO.setTransferReason("朋友代付");
transferDTO.setPaymentPassword("123456");
Result<WalletTransferVO> transferResult = walletService.transfer(transferDTO);

// 查询转账记录
WalletTransferQueryDTO queryDTO = new WalletTransferQueryDTO();
queryDTO.setUserId(1L);
queryDTO.setStartTime(LocalDateTime.now().minusDays(30));
queryDTO.setEndTime(LocalDateTime.now());
Result<List<WalletTransferVO>> transferHistory = walletService.getTransferHistory(queryDTO);
```

### 积分管理
```java
// 获得积分
PointsEarnDTO earnDTO = new PointsEarnDTO();
earnDTO.setUserId(1L);
earnDTO.setPoints(100);
earnDTO.setEarnType(PointsEarnType.ORDER_CONSUMPTION);
earnDTO.setBusinessId("ORDER_123456");
Result<Void> earnResult = pointsService.earnPoints(earnDTO);

// 消费积分
PointsConsumeDTO consumeDTO = new PointsConsumeDTO();
consumeDTO.setUserId(1L);
consumeDTO.setPoints(50);
consumeDTO.setConsumeType(PointsConsumeType.ORDER_DEDUCTION);
consumeDTO.setBusinessId("ORDER_789012");
Result<Void> consumeResult = pointsService.consumePoints(consumeDTO);

// 积分兑换
PointsExchangeDTO exchangeDTO = new PointsExchangeDTO();
exchangeDTO.setUserId(1L);
exchangeDTO.setPoints(1000);
exchangeDTO.setExchangeType(PointsExchangeType.COUPON);
exchangeDTO.setTargetId(1L);
Result<PointsExchangeVO> exchangeResult = pointsService.exchangePoints(exchangeDTO);
```

### 优惠券管理
```java
// 发放优惠券
CouponIssueDTO issueDTO = new CouponIssueDTO();
issueDTO.setUserId(1L);
issueDTO.setCouponTemplateId(1L);
issueDTO.setIssueType(CouponIssueType.SYSTEM_GIFT);
Result<CouponVO> issueResult = couponService.issueCoupon(issueDTO);

// 使用优惠券
CouponUseDTO useDTO = new CouponUseDTO();
useDTO.setUserId(1L);
useDTO.setCouponId(1L);
useDTO.setOrderId(1L);
useDTO.setOrderAmount(new BigDecimal("100.00"));
Result<CouponUseVO> useResult = couponService.useCoupon(useDTO);

// 查询用户优惠券
CouponQueryDTO queryDTO = new CouponQueryDTO();
queryDTO.setUserId(1L);
queryDTO.setCouponStatus(CouponStatus.AVAILABLE);
Result<List<CouponVO>> coupons = couponService.getUserCoupons(queryDTO);
```

## API 接口

### 钱包管理接口
- `GET /api/wallet/balance/{userId}` - 获取钱包余额
- `POST /api/wallet/freeze` - 冻结余额
- `POST /api/wallet/unfreeze` - 解冻余额
- `GET /api/wallet/transaction/history` - 获取交易历史
- `POST /api/wallet/password/set` - 设置支付密码
- `POST /api/wallet/password/verify` - 验证支付密码

### 充值服务接口
- `POST /api/wallet/recharge` - 发起充值
- `POST /api/wallet/recharge/callback` - 充值回调
- `GET /api/wallet/recharge/{id}` - 获取充值详情
- `GET /api/wallet/recharge/history` - 获取充值历史
- `POST /api/wallet/recharge/cancel` - 取消充值

### 支付服务接口
- `POST /api/wallet/payment` - 钱包支付
- `POST /api/wallet/payment/verify` - 验证支付
- `POST /api/wallet/refund` - 申请退款
- `GET /api/wallet/payment/{id}` - 获取支付详情
- `GET /api/wallet/payment/history` - 获取支付历史

### 转账服务接口
- `POST /api/wallet/transfer` - 发起转账
- `GET /api/wallet/transfer/{id}` - 获取转账详情
- `GET /api/wallet/transfer/history` - 获取转账历史
- `POST /api/wallet/transfer/cancel` - 取消转账

### 积分管理接口
- `GET /api/points/balance/{userId}` - 获取积分余额
- `POST /api/points/earn` - 获得积分
- `POST /api/points/consume` - 消费积分
- `POST /api/points/exchange` - 积分兑换
- `GET /api/points/history` - 获取积分历史
- `GET /api/points/rules` - 获取积分规则

### 优惠券管理接口
- `POST /api/coupon/issue` - 发放优惠券
- `POST /api/coupon/use` - 使用优惠券
- `GET /api/coupon/user/{userId}` - 获取用户优惠券
- `GET /api/coupon/{id}` - 获取优惠券详情
- `POST /api/coupon/transfer` - 转赠优惠券
- `GET /api/coupon/templates` - 获取优惠券模板

## 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gxz_wallet?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
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
    database: 9
```

### 钱包配置
```yaml
wallet:
  balance:
    max-amount: 50000.00  # 钱包最大余额
    min-recharge: 1.00    # 最小充值金额
    max-recharge: 5000.00 # 最大充值金额
  transfer:
    daily-limit: 10000.00 # 日转账限额
    single-limit: 2000.00 # 单笔转账限额
    fee-rate: 0.001       # 转账手续费比例
  security:
    password-retry-limit: 5  # 支付密码重试次数
    lock-duration: 30        # 锁定时长(分钟)
    require-real-name: true  # 是否需要实名认证
  points:
    earn-rate: 0.01       # 积分获得比例
    expire-days: 365      # 积分有效期(天)
    exchange-rate: 100    # 积分兑换比例(100积分=1元)
```

## 启动方式

### 开发环境
```bash
# 启动依赖服务
docker-compose up -d mysql redis rocketmq

# 启动钱包服务
cd wallet-provider
mvn spring-boot:run
```

### 生产环境
```bash
# 构建项目
mvn clean package -DskipTests

# 启动服务
java -jar wallet-provider/target/wallet-provider-1.0.0.jar
```

## 监控端点

- **健康检查**: `GET /actuator/health`
- **服务信息**: `GET /actuator/info`
- **指标监控**: `GET /actuator/metrics`
- **日志级别**: `GET /actuator/loggers`

## 数据库设计

### 钱包账户表 (t_wallet_account)
```sql
CREATE TABLE t_wallet_account (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    currency_type TINYINT NOT NULL DEFAULT 1,
    available_balance DECIMAL(15,2) DEFAULT 0.00,
    frozen_balance DECIMAL(15,2) DEFAULT 0.00,
    total_balance DECIMAL(15,2) DEFAULT 0.00,
    total_recharge DECIMAL(15,2) DEFAULT 0.00,
    total_consume DECIMAL(15,2) DEFAULT 0.00,
    account_status TINYINT DEFAULT 1,
    payment_password VARCHAR(128),
    password_error_count INT DEFAULT 0,
    password_lock_time DATETIME,
    real_name_status TINYINT DEFAULT 0,
    version INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_currency (user_id, currency_type)
);
```

### 钱包交易记录表 (t_wallet_transaction)
```sql
CREATE TABLE t_wallet_transaction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transaction_no VARCHAR(32) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    currency_type TINYINT NOT NULL,
    transaction_type TINYINT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    balance_before DECIMAL(15,2) NOT NULL,
    balance_after DECIMAL(15,2) NOT NULL,
    business_type TINYINT NOT NULL,
    business_id VARCHAR(64),
    business_no VARCHAR(64),
    transaction_status TINYINT DEFAULT 1,
    remark VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_business (business_type, business_id),
    INDEX idx_create_time (create_time)
);
```

### 充值记录表 (t_wallet_recharge)
```sql
CREATE TABLE t_wallet_recharge (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    recharge_no VARCHAR(32) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    actual_amount DECIMAL(12,2),
    bonus_amount DECIMAL(12,2) DEFAULT 0.00,
    payment_method TINYINT NOT NULL,
    payment_channel VARCHAR(50),
    third_party_trade_no VARCHAR(100),
    recharge_status TINYINT DEFAULT 0,
    callback_time DATETIME,
    client_ip VARCHAR(50),
    remark VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_status (recharge_status),
    INDEX idx_create_time (create_time)
);
```

### 转账记录表 (t_wallet_transfer)
```sql
CREATE TABLE t_wallet_transfer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transfer_no VARCHAR(32) UNIQUE NOT NULL,
    from_user_id BIGINT NOT NULL,
    to_user_id BIGINT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    fee DECIMAL(12,2) DEFAULT 0.00,
    actual_amount DECIMAL(12,2) NOT NULL,
    transfer_type TINYINT NOT NULL,
    transfer_status TINYINT DEFAULT 0,
    transfer_reason VARCHAR(200),
    remark VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_from_user (from_user_id),
    INDEX idx_to_user (to_user_id),
    INDEX idx_status (transfer_status),
    INDEX idx_create_time (create_time)
);
```

### 积分账户表 (t_points_account)
```sql
CREATE TABLE t_points_account (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    available_points BIGINT DEFAULT 0,
    frozen_points BIGINT DEFAULT 0,
    total_points BIGINT DEFAULT 0,
    total_earned BIGINT DEFAULT 0,
    total_consumed BIGINT DEFAULT 0,
    total_expired BIGINT DEFAULT 0,
    account_status TINYINT DEFAULT 1,
    version INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_id (user_id)
);
```

### 积分交易记录表 (t_points_transaction)
```sql
CREATE TABLE t_points_transaction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transaction_no VARCHAR(32) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    transaction_type TINYINT NOT NULL,
    points BIGINT NOT NULL,
    balance_before BIGINT NOT NULL,
    balance_after BIGINT NOT NULL,
    business_type TINYINT NOT NULL,
    business_id VARCHAR(64),
    business_no VARCHAR(64),
    expire_time DATETIME,
    transaction_status TINYINT DEFAULT 1,
    remark VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_business (business_type, business_id),
    INDEX idx_expire_time (expire_time),
    INDEX idx_create_time (create_time)
);
```

### 优惠券模板表 (t_coupon_template)
```sql
CREATE TABLE t_coupon_template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_name VARCHAR(100) NOT NULL,
    coupon_type TINYINT NOT NULL,
    discount_type TINYINT NOT NULL,
    discount_value DECIMAL(10,2) NOT NULL,
    min_order_amount DECIMAL(10,2) DEFAULT 0.00,
    max_discount_amount DECIMAL(10,2),
    total_quantity INT DEFAULT 0,
    issued_quantity INT DEFAULT 0,
    per_user_limit INT DEFAULT 1,
    valid_days INT NOT NULL,
    start_time DATETIME,
    end_time DATETIME,
    template_status TINYINT DEFAULT 1,
    description VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 用户优惠券表 (t_user_coupon)
```sql
CREATE TABLE t_user_coupon (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    coupon_code VARCHAR(32) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    template_id BIGINT NOT NULL,
    coupon_name VARCHAR(100) NOT NULL,
    coupon_type TINYINT NOT NULL,
    discount_type TINYINT NOT NULL,
    discount_value DECIMAL(10,2) NOT NULL,
    min_order_amount DECIMAL(10,2) DEFAULT 0.00,
    max_discount_amount DECIMAL(10,2),
    coupon_status TINYINT DEFAULT 1,
    issue_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    use_time DATETIME,
    order_id BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_template_id (template_id),
    INDEX idx_status (coupon_status),
    INDEX idx_end_time (end_time)
);
```

## 业务流程

### 钱包充值流程
1. **发起充值**: 用户选择充值金额和支付方式
2. **创建充值单**: 系统创建充值记录
3. **调用支付**: 调用第三方支付接口
4. **支付回调**: 接收支付结果回调
5. **余额入账**: 充值成功后增加钱包余额
6. **发送通知**: 发送充值成功通知

### 钱包支付流程
1. **验证支付密码**: 验证用户支付密码
2. **检查余额**: 检查钱包余额是否充足
3. **冻结金额**: 冻结订单金额
4. **创建支付单**: 创建支付交易记录
5. **扣减余额**: 扣减钱包可用余额
6. **支付完成**: 标记支付完成状态

### 积分获得流程
1. **触发事件**: 用户消费等行为触发积分获得
2. **计算积分**: 根据规则计算应得积分
3. **积分入账**: 增加用户积分余额
4. **设置过期**: 设置积分过期时间
5. **记录流水**: 记录积分交易流水
6. **发送通知**: 发送积分获得通知

### 优惠券使用流程
1. **选择优惠券**: 用户选择可用优惠券
2. **验证有效性**: 验证优惠券是否有效
3. **计算优惠**: 计算优惠金额
4. **锁定优惠券**: 锁定优惠券防止重复使用
5. **订单抵扣**: 在订单中应用优惠
6. **标记已使用**: 标记优惠券为已使用状态

## 核心服务实现

### 钱包余额服务
```java
@Service
public class WalletBalanceService {
    
    @Transactional
    public void increaseBalance(Long userId, BigDecimal amount, 
                               BusinessType businessType, String businessId) {
        // 获取钱包账户
        WalletAccount account = getWalletAccount(userId);
        
        // 乐观锁更新余额
        BigDecimal newAvailableBalance = account.getAvailableBalance().add(amount);
        BigDecimal newTotalBalance = account.getTotalBalance().add(amount);
        
        boolean updated = walletAccountMapper.updateBalance(
            account.getId(),
            newAvailableBalance,
            newTotalBalance,
            account.getVersion()
        );
        
        if (!updated) {
            throw new BusinessException("余额更新失败，请重试");
        }
        
        // 记录交易流水
        recordTransaction(userId, TransactionType.INCOME, amount, 
                         account.getAvailableBalance(), newAvailableBalance,
                         businessType, businessId);
    }
    
    @Transactional
    public void decreaseBalance(Long userId, BigDecimal amount, 
                               BusinessType businessType, String businessId) {
        // 获取钱包账户
        WalletAccount account = getWalletAccount(userId);
        
        // 检查余额是否充足
        if (account.getAvailableBalance().compareTo(amount) < 0) {
            throw new BusinessException("余额不足");
        }
        
        // 乐观锁更新余额
        BigDecimal newAvailableBalance = account.getAvailableBalance().subtract(amount);
        BigDecimal newTotalBalance = account.getTotalBalance().subtract(amount);
        
        boolean updated = walletAccountMapper.updateBalance(
            account.getId(),
            newAvailableBalance,
            newTotalBalance,
            account.getVersion()
        );
        
        if (!updated) {
            throw new BusinessException("余额更新失败，请重试");
        }
        
        // 记录交易流水
        recordTransaction(userId, TransactionType.EXPENSE, amount, 
                         account.getAvailableBalance(), newAvailableBalance,
                         businessType, businessId);
    }
    
    @Transactional
    public void freezeBalance(Long userId, BigDecimal amount, 
                             BusinessType businessType, String businessId) {
        // 获取钱包账户
        WalletAccount account = getWalletAccount(userId);
        
        // 检查可用余额是否充足
        if (account.getAvailableBalance().compareTo(amount) < 0) {
            throw new BusinessException("可用余额不足");
        }
        
        // 乐观锁更新余额
        BigDecimal newAvailableBalance = account.getAvailableBalance().subtract(amount);
        BigDecimal newFrozenBalance = account.getFrozenBalance().add(amount);
        
        boolean updated = walletAccountMapper.updateFreezeBalance(
            account.getId(),
            newAvailableBalance,
            newFrozenBalance,
            account.getVersion()
        );
        
        if (!updated) {
            throw new BusinessException("余额冻结失败，请重试");
        }
        
        // 记录交易流水
        recordTransaction(userId, TransactionType.FREEZE, amount, 
                         account.getAvailableBalance(), newAvailableBalance,
                         businessType, businessId);
    }
}
```

### 积分服务实现
```java
@Service
public class PointsService {
    
    @Transactional
    public void earnPoints(Long userId, Long points, 
                          PointsEarnType earnType, String businessId) {
        // 获取积分账户
        PointsAccount account = getPointsAccount(userId);
        
        // 计算过期时间
        LocalDateTime expireTime = LocalDateTime.now().plusDays(pointsConfig.getExpireDays());
        
        // 乐观锁更新积分
        Long newAvailablePoints = account.getAvailablePoints() + points;
        Long newTotalPoints = account.getTotalPoints() + points;
        Long newTotalEarned = account.getTotalEarned() + points;
        
        boolean updated = pointsAccountMapper.updatePoints(
            account.getId(),
            newAvailablePoints,
            newTotalPoints,
            newTotalEarned,
            account.getVersion()
        );
        
        if (!updated) {
            throw new BusinessException("积分更新失败，请重试");
        }
        
        // 记录积分流水
        recordPointsTransaction(userId, PointsTransactionType.EARN, points,
                               account.getAvailablePoints(), newAvailablePoints,
                               earnType.getBusinessType(), businessId, expireTime);
    }
    
    @Transactional
    public void consumePoints(Long userId, Long points, 
                             PointsConsumeType consumeType, String businessId) {
        // 获取积分账户
        PointsAccount account = getPointsAccount(userId);
        
        // 检查积分是否充足
        if (account.getAvailablePoints() < points) {
            throw new BusinessException("积分不足");
        }
        
        // 按照先进先出原则消费积分
        List<PointsTransaction> availablePoints = getAvailablePointsOrderByExpire(userId);
        Long remainingPoints = points;
        
        for (PointsTransaction transaction : availablePoints) {
            if (remainingPoints <= 0) {
                break;
            }
            
            Long consumablePoints = Math.min(remainingPoints, transaction.getPoints());
            
            // 更新积分交易记录
            pointsTransactionMapper.updateConsumedPoints(
                transaction.getId(), 
                transaction.getConsumedPoints() + consumablePoints
            );
            
            remainingPoints -= consumablePoints;
        }
        
        // 乐观锁更新积分账户
        Long newAvailablePoints = account.getAvailablePoints() - points;
        Long newTotalConsumed = account.getTotalConsumed() + points;
        
        boolean updated = pointsAccountMapper.updateConsumePoints(
            account.getId(),
            newAvailablePoints,
            newTotalConsumed,
            account.getVersion()
        );
        
        if (!updated) {
            throw new BusinessException("积分消费失败，请重试");
        }
        
        // 记录积分消费流水
        recordPointsTransaction(userId, PointsTransactionType.CONSUME, points,
                               account.getAvailablePoints(), newAvailablePoints,
                               consumeType.getBusinessType(), businessId, null);
    }
}
```

### 优惠券服务实现
```java
@Service
public class CouponService {
    
    @Transactional
    public CouponVO issueCoupon(Long userId, Long templateId, CouponIssueType issueType) {
        // 获取优惠券模板
        CouponTemplate template = couponTemplateMapper.selectById(templateId);
        if (template == null) {
            throw new BusinessException("优惠券模板不存在");
        }
        
        // 检查发放限制
        checkIssueLimit(userId, template);
        
        // 生成优惠券码
        String couponCode = generateCouponCode();
        
        // 计算有效期
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusDays(template.getValidDays());
        
        // 创建用户优惠券
        UserCoupon userCoupon = UserCoupon.builder()
            .couponCode(couponCode)
            .userId(userId)
            .templateId(templateId)
            .couponName(template.getTemplateName())
            .couponType(template.getCouponType())
            .discountType(template.getDiscountType())
            .discountValue(template.getDiscountValue())
            .minOrderAmount(template.getMinOrderAmount())
            .maxDiscountAmount(template.getMaxDiscountAmount())
            .couponStatus(CouponStatus.AVAILABLE)
            .startTime(startTime)
            .endTime(endTime)
            .build();
        
        userCouponMapper.insert(userCoupon);
        
        // 更新模板已发放数量
        couponTemplateMapper.increaseIssuedQuantity(templateId);
        
        return convertToVO(userCoupon);
    }
    
    @Transactional
    public CouponUseVO useCoupon(Long userId, Long couponId, Long orderId, BigDecimal orderAmount) {
        // 获取用户优惠券
        UserCoupon coupon = userCouponMapper.selectByIdAndUserId(couponId, userId);
        if (coupon == null) {
            throw new BusinessException("优惠券不存在");
        }
        
        // 验证优惠券状态
        validateCouponStatus(coupon, orderAmount);
        
        // 计算优惠金额
        BigDecimal discountAmount = calculateDiscountAmount(coupon, orderAmount);
        
        // 更新优惠券状态
        boolean updated = userCouponMapper.updateCouponUsed(
            couponId, 
            CouponStatus.USED, 
            LocalDateTime.now(), 
            orderId
        );
        
        if (!updated) {
            throw new BusinessException("优惠券使用失败");
        }
        
        return CouponUseVO.builder()
            .couponId(couponId)
            .couponCode(coupon.getCouponCode())
            .discountAmount(discountAmount)
            .build();
    }
    
    private BigDecimal calculateDiscountAmount(UserCoupon coupon, BigDecimal orderAmount) {
        BigDecimal discountAmount = BigDecimal.ZERO;
        
        if (coupon.getDiscountType() == DiscountType.FIXED_AMOUNT) {
            // 固定金额优惠
            discountAmount = coupon.getDiscountValue();
        } else if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
            // 百分比优惠
            discountAmount = orderAmount.multiply(coupon.getDiscountValue())
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            
            // 检查最大优惠金额限制
            if (coupon.getMaxDiscountAmount() != null && 
                discountAmount.compareTo(coupon.getMaxDiscountAmount()) > 0) {
                discountAmount = coupon.getMaxDiscountAmount();
            }
        }
        
        // 优惠金额不能超过订单金额
        if (discountAmount.compareTo(orderAmount) > 0) {
            discountAmount = orderAmount;
        }
        
        return discountAmount;
    }
}
```

## 安全机制

### 支付密码安全
```java
@Service
public class PaymentSecurityService {
    
    public boolean verifyPaymentPassword(Long userId, String password) {
        WalletAccount account = getWalletAccount(userId);
        
        // 检查账户是否被锁定
        if (isAccountLocked(account)) {
            throw new BusinessException("账户已被锁定，请稍后再试");
        }
        
        // 验证支付密码
        boolean isValid = passwordEncoder.matches(password, account.getPaymentPassword());
        
        if (!isValid) {
            // 增加错误次数
            increasePasswordErrorCount(userId);
            
            // 检查是否需要锁定账户
            if (account.getPasswordErrorCount() + 1 >= securityConfig.getPasswordRetryLimit()) {
                lockAccount(userId);
                throw new BusinessException("密码错误次数过多，账户已被锁定");
            }
            
            throw new BusinessException("支付密码错误");
        }
        
        // 重置错误次数
        resetPasswordErrorCount(userId);
        
        return true;
    }
    
    private void lockAccount(Long userId) {
        LocalDateTime lockTime = LocalDateTime.now().plusMinutes(securityConfig.getLockDuration());
        walletAccountMapper.lockAccount(userId, lockTime);
    }
    
    private boolean isAccountLocked(WalletAccount account) {
        return account.getPasswordLockTime() != null && 
               account.getPasswordLockTime().isAfter(LocalDateTime.now());
    }
}
```

### 风控检查
```java
@Service
public class WalletRiskService {
    
    public void checkTransferRisk(Long fromUserId, Long toUserId, BigDecimal amount) {
        // 检查单笔转账限额
        if (amount.compareTo(riskConfig.getSingleTransferLimit()) > 0) {
            throw new BusinessException("单笔转账金额超过限额");
        }
        
        // 检查日转账限额
        BigDecimal dailyTransferAmount = getDailyTransferAmount(fromUserId);
        if (dailyTransferAmount.add(amount).compareTo(riskConfig.getDailyTransferLimit()) > 0) {
            throw new BusinessException("日转账金额超过限额");
        }
        
        // 检查转账频率
        if (isFrequentTransfer(fromUserId)) {
            throw new BusinessException("转账过于频繁，请稍后再试");
        }
        
        // 检查可疑账户
        if (isSuspiciousAccount(fromUserId) || isSuspiciousAccount(toUserId)) {
            throw new BusinessException("账户存在风险，转账被拒绝");
        }
    }
    
    private boolean isFrequentTransfer(Long userId) {
        String key = "transfer:count:" + userId;
        String count = redisTemplate.opsForValue().get(key);
        
        if (count == null) {
            redisTemplate.opsForValue().set(key, "1", Duration.ofMinutes(10));
            return false;
        }
        
        int currentCount = Integer.parseInt(count);
        if (currentCount >= riskConfig.getTransferFrequencyLimit()) {
            return true;
        }
        
        redisTemplate.opsForValue().increment(key);
        return false;
    }
}
```

## 性能优化

### 钱包优化
- 余额查询缓存
- 交易记录分表
- 批量交易处理
- 异步通知发送

### 积分优化
- 积分计算缓存
- 过期积分定时清理
- 积分兑换预计算
- 积分统计异步更新

### 优惠券优化
- 优惠券模板缓存
- 用户优惠券索引
- 优惠券使用预检查
- 过期优惠券定时清理

## 安全考虑

- 🔒 支付密码加密存储
- 🔒 交易金额精度控制
- 🔒 并发操作乐观锁
- 🔒 敏感操作审计日志
- 🔒 异常交易实时监控

## 故障排查

### 常见问题
1. **余额不一致**: 检查交易流水和账户余额
2. **支付失败**: 检查支付密码和账户状态
3. **积分异常**: 检查积分规则和过期时间
4. **优惠券无效**: 检查优惠券状态和使用条件

### 日志查看
```bash
# 查看应用日志
tail -f logs/wallet-provider.log

# 查看钱包交易日志
grep "WALLET_TRANSACTION" logs/wallet-provider.log

# 查看积分日志
grep "POINTS" logs/wallet-provider.log

# 查看优惠券日志
grep "COUPON" logs/wallet-provider.log

# 查看错误日志
grep "ERROR" logs/wallet-provider.log
```

## 开发指南

### 添加新的交易类型
1. 定义交易类型枚举
2. 实现交易处理逻辑
3. 添加交易验证规则
4. 更新交易流水记录
5. 添加单元测试

### 扩展积分规则
1. 定义积分获得规则
2. 实现积分计算逻辑
3. 配置积分有效期
4. 添加积分监控
5. 测试积分功能

## 版本信息

- **当前版本**: 1.0.0
- **最低 JDK 版本**: 17
- **Spring Boot 版本**: 3.x
- **Spring Cloud 版本**: 2023.x

---

**维护团队**: 共享臻选开发团队  
**创建时间**: 2024年  
**最后更新**: 2024年