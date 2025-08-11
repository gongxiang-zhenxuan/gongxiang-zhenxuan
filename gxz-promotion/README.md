# GXZ Promotion - 共享臻选营销促销服务

## 概述

`gxz-promotion` 是共享臻选微服务架构中的营销促销服务，负责处理优惠券管理、促销活动、满减优惠、积分系统、会员权益等核心营销相关功能。

## 主要功能

### 🎫 优惠券管理
- **券模板管理**: 优惠券模板创建和配置
- **券发放**: 优惠券批量发放和定向投放
- **券使用**: 优惠券核销和使用限制
- **券回收**: 过期券回收和统计分析

### 🎉 促销活动
- **秒杀活动**: 限时秒杀和库存管理
- **团购活动**: 拼团购买和成团机制
- **满减优惠**: 满额减免和阶梯优惠
- **买赠活动**: 买N送M和赠品管理

### 🏆 积分系统
- **积分获取**: 购物、签到、分享等积分获取
- **积分消费**: 积分抵扣和积分商城
- **积分等级**: 会员等级和积分权益
- **积分转换**: 积分与优惠券互换

### 👑 会员权益
- **会员等级**: 多层级会员体系
- **专享价格**: 会员专享价格和折扣
- **生日特权**: 生日优惠和专属活动
- **成长体系**: 成长值和升级机制

## 技术栈

- **Java**: 17+
- **Spring Boot**: 3.x
- **Spring Cloud**: 2023.x
- **MyBatis Plus**: ORM 框架
- **MySQL**: 主数据库
- **Redis**: 缓存和分布式锁
- **RocketMQ**: 消息队列
- **XXL-Job**: 定时任务
- **Redisson**: 分布式锁
- **Nacos**: 服务注册发现

## 模块结构

```
gxz-promotion/
├── promotion-api/               # 促销服务 API 模块
│   ├── src/main/java/
│   │   └── com/gongxiang/zhenxuan/promotion/api/
│   │       ├── dto/            # 数据传输对象
│   │       ├── vo/             # 视图对象
│   │       ├── enums/          # 枚举定义
│   │       └── feign/          # Feign 客户端接口
│   └── pom.xml
├── promotion-provider/          # 促销服务提供者模块
│   ├── src/main/java/
│   │   └── com/gongxiang/zhenxuan/promotion/
│   │       ├── controller/     # 控制器层
│   │       ├── service/        # 服务层
│   │       ├── mapper/         # 数据访问层
│   │       ├── entity/         # 实体类
│   │       ├── config/         # 配置类
│   │       ├── strategy/       # 促销策略
│   │       ├── engine/         # 规则引擎
│   │       ├── job/            # 定时任务
│   │       └── PromotionApplication.java
│   ├── src/main/resources/
│   │   ├── application.yml     # 配置文件
│   │   └── mapper/            # MyBatis 映射文件
│   └── pom.xml
└── pom.xml
```

## 核心功能模块

### 优惠券管理
```java
// 创建优惠券模板
CouponTemplateDTO templateDTO = new CouponTemplateDTO();
templateDTO.setCouponName("新用户专享券");
templateDTO.setCouponType(CouponType.DISCOUNT);
templateDTO.setDiscountAmount(new BigDecimal("10.00"));
templateDTO.setMinAmount(new BigDecimal("100.00"));
Result<CouponTemplateVO> result = couponService.createTemplate(templateDTO);

// 发放优惠券
CouponIssueDTO issueDTO = new CouponIssueDTO();
issueDTO.setTemplateId(1L);
issueDTO.setUserId(1L);
issueDTO.setIssueType(IssueType.MANUAL);
Result<CouponVO> issueResult = couponService.issueCoupon(issueDTO);
```

### 促销活动
```java
// 创建秒杀活动
SeckillActivityDTO seckillDTO = new SeckillActivityDTO();
seckillDTO.setActivityName("限时秒杀");
seckillDTO.setGoodsId(1L);
seckillDTO.setSeckillPrice(new BigDecimal("99.00"));
seckillDTO.setSeckillStock(100);
seckillDTO.setStartTime(LocalDateTime.now().plusHours(1));
Result<SeckillActivityVO> seckillResult = activityService.createSeckill(seckillDTO);

// 参与秒杀
SeckillOrderDTO orderDTO = new SeckillOrderDTO();
orderDTO.setActivityId(1L);
orderDTO.setUserId(1L);
orderDTO.setQuantity(1);
Result<SeckillOrderVO> orderResult = seckillService.participateSeckill(orderDTO);
```

### 积分系统
```java
// 积分获取
PointsEarnDTO earnDTO = new PointsEarnDTO();
earnDTO.setUserId(1L);
earnDTO.setPointsType(PointsType.PURCHASE);
earnDTO.setPoints(100);
earnDTO.setOrderId(1L);
Result<Void> earnResult = pointsService.earnPoints(earnDTO);

// 积分消费
PointsConsumeDTO consumeDTO = new PointsConsumeDTO();
consumeDTO.setUserId(1L);
consumeDTO.setPoints(50);
consumeDTO.setOrderId(1L);
Result<Void> consumeResult = pointsService.consumePoints(consumeDTO);
```

## API 接口

### 优惠券管理接口
- `POST /api/coupon/template/create` - 创建优惠券模板
- `GET /api/coupon/template/{id}` - 获取券模板详情
- `PUT /api/coupon/template/{id}` - 更新券模板
- `POST /api/coupon/issue` - 发放优惠券
- `GET /api/coupon/user/{userId}` - 获取用户优惠券
- `POST /api/coupon/use` - 使用优惠券

### 促销活动接口
- `POST /api/activity/seckill/create` - 创建秒杀活动
- `GET /api/activity/seckill/{id}` - 获取秒杀详情
- `POST /api/activity/seckill/participate` - 参与秒杀
- `POST /api/activity/group/create` - 创建团购活动
- `POST /api/activity/group/join` - 参与团购
- `GET /api/activity/list` - 获取活动列表

### 积分系统接口
- `POST /api/points/earn` - 积分获取
- `POST /api/points/consume` - 积分消费
- `GET /api/points/balance/{userId}` - 查询积分余额
- `GET /api/points/history/{userId}` - 积分历史记录
- `POST /api/points/exchange` - 积分兑换

### 会员权益接口
- `GET /api/member/level/{userId}` - 获取会员等级
- `POST /api/member/upgrade` - 会员升级
- `GET /api/member/benefits/{userId}` - 获取会员权益
- `POST /api/member/birthday` - 生日特权激活

## 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gxz_promotion?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
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
    database: 5
```

### 促销配置
```yaml
promotion:
  coupon:
    max-per-user: 10  # 每用户最大持券数
    expire-days: 30   # 默认过期天数
  seckill:
    max-per-user: 1   # 每用户最大参与次数
    timeout-seconds: 300  # 支付超时时间
  points:
    exchange-rate: 100  # 积分兑换比例
    max-daily-earn: 1000  # 每日最大获取积分
```

## 启动方式

### 开发环境
```bash
# 启动依赖服务
docker-compose up -d mysql redis rocketmq

# 启动促销服务
cd promotion-provider
mvn spring-boot:run
```

### 生产环境
```bash
# 构建项目
mvn clean package -DskipTests

# 启动服务
java -jar promotion-provider/target/promotion-provider-1.0.0.jar
```

## 监控端点

- **健康检查**: `GET /actuator/health`
- **服务信息**: `GET /actuator/info`
- **指标监控**: `GET /actuator/metrics`
- **日志级别**: `GET /actuator/loggers`

## 数据库设计

### 优惠券模板表 (t_coupon_template)
```sql
CREATE TABLE t_coupon_template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_code VARCHAR(32) UNIQUE NOT NULL,
    coupon_name VARCHAR(100) NOT NULL,
    coupon_type TINYINT NOT NULL,
    discount_amount DECIMAL(10,2),
    discount_rate DECIMAL(5,4),
    min_amount DECIMAL(10,2),
    max_discount DECIMAL(10,2),
    total_count INT NOT NULL,
    issued_count INT DEFAULT 0,
    used_count INT DEFAULT 0,
    per_user_limit INT DEFAULT 1,
    valid_days INT NOT NULL,
    start_time DATETIME,
    end_time DATETIME,
    status TINYINT DEFAULT 1,
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
    template_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    status TINYINT DEFAULT 0,
    issue_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    use_time DATETIME,
    expire_time DATETIME NOT NULL,
    order_id BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 促销活动表 (t_promotion_activity)
```sql
CREATE TABLE t_promotion_activity (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    activity_code VARCHAR(32) UNIQUE NOT NULL,
    activity_name VARCHAR(100) NOT NULL,
    activity_type TINYINT NOT NULL,
    activity_rule TEXT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    status TINYINT DEFAULT 0,
    participate_count INT DEFAULT 0,
    success_count INT DEFAULT 0,
    description VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 用户积分表 (t_user_points)
```sql
CREATE TABLE t_user_points (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    total_points INT DEFAULT 0,
    available_points INT DEFAULT 0,
    frozen_points INT DEFAULT 0,
    used_points INT DEFAULT 0,
    expire_points INT DEFAULT 0,
    level TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_id (user_id)
);
```

### 积分记录表 (t_points_record)
```sql
CREATE TABLE t_points_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    points_type TINYINT NOT NULL,
    operation_type TINYINT NOT NULL,
    points INT NOT NULL,
    balance_points INT NOT NULL,
    source_type TINYINT NOT NULL,
    source_id BIGINT,
    description VARCHAR(255),
    expire_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

## 业务流程

### 优惠券发放流程
1. **模板创建**: 创建优惠券模板和规则
2. **发放策略**: 设置发放条件和数量
3. **券发放**: 批量或定向发放优惠券
4. **券使用**: 用户下单时使用优惠券
5. **券核销**: 订单支付后核销优惠券

### 秒杀活动流程
1. **活动创建**: 创建秒杀活动和商品
2. **库存预热**: 将库存加载到 Redis
3. **活动开始**: 用户参与秒杀抢购
4. **库存扣减**: 原子性扣减库存数量
5. **订单创建**: 创建秒杀订单
6. **支付确认**: 支付成功确认购买

### 积分获取流程
1. **行为触发**: 用户行为触发积分获取
2. **规则匹配**: 匹配积分获取规则
3. **积分计算**: 计算应获得积分数量
4. **积分发放**: 增加用户积分余额
5. **等级更新**: 更新用户会员等级

## 促销策略引擎

### 规则引擎设计
```java
public interface PromotionRule {
    boolean match(PromotionContext context);
    PromotionResult calculate(PromotionContext context);
    int getPriority();
}
```

### 满减策略
```java
@Component
public class FullReductionRule implements PromotionRule {
    @Override
    public boolean match(PromotionContext context) {
        return context.getTotalAmount().compareTo(getMinAmount()) >= 0;
    }
    
    @Override
    public PromotionResult calculate(PromotionContext context) {
        // 满减计算逻辑
    }
}
```

## 性能优化

### 秒杀优化
- Redis 预扣库存
- 异步订单处理
- 限流和防刷
- 缓存预热

### 优惠券优化
- 券模板缓存
- 批量发放优化
- 过期券清理
- 使用状态缓存

### 积分优化
- 积分余额缓存
- 批量积分操作
- 异步积分计算
- 分级存储策略

## 安全考虑

- 🔒 优惠券防刷和防薅羊毛
- 🔒 秒杀活动防机器人
- 🔒 积分操作防篡改
- 🔒 促销规则权限控制
- 🔒 敏感操作审计日志

## 故障排查

### 常见问题
1. **优惠券发放失败**: 检查模板配置和库存
2. **秒杀库存异常**: 检查 Redis 连接和数据一致性
3. **积分计算错误**: 检查规则配置和计算逻辑
4. **促销规则不生效**: 检查规则优先级和匹配条件

### 日志查看
```bash
# 查看应用日志
tail -f logs/promotion-provider.log

# 查看优惠券日志
grep "COUPON" logs/promotion-provider.log

# 查看秒杀日志
grep "SECKILL" logs/promotion-provider.log

# 查看积分日志
grep "POINTS" logs/promotion-provider.log

# 查看错误日志
grep "ERROR" logs/promotion-provider.log
```

## 开发指南

### 添加新的促销类型
1. 定义促销规则接口
2. 实现具体促销策略
3. 配置规则优先级
4. 添加数据库表结构
5. 编写单元测试

### 优化促销性能
1. 识别性能瓶颈
2. 添加缓存策略
3. 优化数据库查询
4. 使用异步处理
5. 监控性能指标

## 版本信息

- **当前版本**: 1.0.0
- **最低 JDK 版本**: 17
- **Spring Boot 版本**: 3.x
- **Spring Cloud 版本**: 2023.x

---

**维护团队**: 共享臻选开发团队  
**创建时间**: 2024年  
**最后更新**: 2024年