# GXZ Order - 共享臻选订单服务

## 概述

`gxz-order` 是共享臻选微服务架构中的订单管理服务，负责处理订单创建、订单状态管理、订单支付、订单履约、售后服务等核心订单相关功能。

## 主要功能

### 📋 订单管理
- **订单创建**: 购物车结算和订单生成
- **订单查询**: 订单详情和订单列表查询
- **订单状态**: 订单状态流转和状态管理
- **订单修改**: 订单信息修改和取消

### 💳 支付管理
- **支付集成**: 多种支付方式集成
- **支付回调**: 支付结果处理和通知
- **退款处理**: 订单退款和退款状态跟踪
- **支付安全**: 支付安全验证和风控

### 🚚 物流管理
- **物流跟踪**: 订单物流信息跟踪
- **配送管理**: 配送方式和配送时间管理
- **签收确认**: 订单签收和确认收货
- **物流异常**: 物流异常处理和客服介入

### 🔄 售后服务
- **退货退款**: 退货申请和退款处理
- **换货服务**: 商品换货和重新发货
- **投诉处理**: 订单投诉和纠纷处理
- **评价管理**: 订单评价和商品评分

## 技术栈

- **Java**: 17+
- **Spring Boot**: 3.x
- **Spring Cloud**: 2023.x
- **MyBatis Plus**: ORM 框架
- **MySQL**: 主数据库
- **Redis**: 缓存和分布式锁
- **RocketMQ**: 消息队列
- **Seata**: 分布式事务
- **XXL-Job**: 定时任务
- **Nacos**: 服务注册发现

## 模块结构

```
gxz-order/
├── order-api/                   # 订单服务 API 模块
│   ├── src/main/java/
│   │   └── com/gongxiang/zhenxuan/order/api/
│   │       ├── dto/            # 数据传输对象
│   │       ├── vo/             # 视图对象
│   │       ├── enums/          # 枚举定义
│   │       └── feign/          # Feign 客户端接口
│   └── pom.xml
├── order-provider/              # 订单服务提供者模块
│   ├── src/main/java/
│   │   └── com/gongxiang/zhenxuan/order/
│   │       ├── controller/     # 控制器层
│   │       ├── service/        # 服务层
│   │       ├── mapper/         # 数据访问层
│   │       ├── entity/         # 实体类
│   │       ├── config/         # 配置类
│   │       ├── statemachine/   # 状态机
│   │       ├── job/            # 定时任务
│   │       └── OrderApplication.java
│   ├── src/main/resources/
│   │   ├── application.yml     # 配置文件
│   │   └── mapper/            # MyBatis 映射文件
│   └── pom.xml
└── pom.xml
```

## 核心功能模块

### 订单创建
```java
// 创建订单
OrderCreateDTO createDTO = new OrderCreateDTO();
createDTO.setUserId(1L);
createDTO.setMerchantId(1L);
createDTO.setItems(orderItems);
createDTO.setDeliveryAddress(address);
Result<OrderVO> result = orderService.createOrder(createDTO);

// 订单支付
OrderPayDTO payDTO = new OrderPayDTO();
payDTO.setOrderId(1L);
payDTO.setPayType(PayType.WECHAT);
payDTO.setPayAmount(new BigDecimal("99.00"));
Result<PayVO> payResult = orderService.payOrder(payDTO);
```

### 订单状态管理
```java
// 更新订单状态
OrderStatusUpdateDTO statusDTO = new OrderStatusUpdateDTO();
statusDTO.setOrderId(1L);
statusDTO.setStatus(OrderStatus.SHIPPED);
statusDTO.setRemark("商品已发货");
Result<Void> statusResult = orderService.updateOrderStatus(statusDTO);

// 取消订单
OrderCancelDTO cancelDTO = new OrderCancelDTO();
cancelDTO.setOrderId(1L);
cancelDTO.setCancelReason("用户主动取消");
Result<Void> cancelResult = orderService.cancelOrder(cancelDTO);
```

### 售后处理
```java
// 申请退款
RefundApplyDTO refundDTO = new RefundApplyDTO();
refundDTO.setOrderId(1L);
refundDTO.setRefundAmount(new BigDecimal("99.00"));
refundDTO.setRefundReason("商品质量问题");
Result<RefundVO> refundResult = afterSaleService.applyRefund(refundDTO);
```

## API 接口

### 订单管理接口
- `POST /api/order/create` - 创建订单
- `GET /api/order/{id}` - 获取订单详情
- `GET /api/order/list` - 获取订单列表
- `PUT /api/order/{id}/cancel` - 取消订单
- `PUT /api/order/{id}/status` - 更新订单状态
- `POST /api/order/{id}/confirm` - 确认收货

### 支付管理接口
- `POST /api/payment/pay` - 发起支付
- `POST /api/payment/callback` - 支付回调
- `GET /api/payment/{orderId}/status` - 查询支付状态
- `POST /api/payment/refund` - 申请退款
- `GET /api/payment/methods` - 获取支付方式

### 物流管理接口
- `GET /api/logistics/{orderId}` - 获取物流信息
- `POST /api/logistics/ship` - 订单发货
- `PUT /api/logistics/{orderId}/update` - 更新物流信息
- `POST /api/logistics/receive` - 确认收货

### 售后服务接口
- `POST /api/aftersale/refund/apply` - 申请退款
- `POST /api/aftersale/return/apply` - 申请退货
- `GET /api/aftersale/{id}` - 获取售后详情
- `PUT /api/aftersale/{id}/process` - 处理售后申请
- `POST /api/aftersale/evaluate` - 订单评价

## 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gxz_order?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
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
    database: 4
```

### 消息队列配置
```yaml
rocketmq:
  name-server: ${ROCKETMQ_NAME_SERVER:localhost:9876}
  producer:
    group: order-producer-group
  consumer:
    group: order-consumer-group
```

### 分布式事务配置
```yaml
seata:
  registry:
    type: nacos
    nacos:
      server-addr: ${NACOS_SERVER:localhost:8848}
      group: SEATA_GROUP
  config:
    type: nacos
    nacos:
      server-addr: ${NACOS_SERVER:localhost:8848}
      group: SEATA_GROUP
```

## 启动方式

### 开发环境
```bash
# 启动依赖服务
docker-compose up -d mysql redis rocketmq seata

# 启动订单服务
cd order-provider
mvn spring-boot:run
```

### 生产环境
```bash
# 构建项目
mvn clean package -DskipTests

# 启动服务
java -jar order-provider/target/order-provider-1.0.0.jar
```

## 监控端点

- **健康检查**: `GET /actuator/health`
- **服务信息**: `GET /actuator/info`
- **指标监控**: `GET /actuator/metrics`
- **日志级别**: `GET /actuator/loggers`

## 数据库设计

### 订单主表 (t_order)
```sql
CREATE TABLE t_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(32) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    merchant_id BIGINT NOT NULL,
    shop_id BIGINT NOT NULL,
    order_type TINYINT DEFAULT 1,
    order_status TINYINT NOT NULL,
    pay_status TINYINT DEFAULT 0,
    delivery_status TINYINT DEFAULT 0,
    total_amount DECIMAL(10,2) NOT NULL,
    pay_amount DECIMAL(10,2) NOT NULL,
    discount_amount DECIMAL(10,2) DEFAULT 0,
    freight_amount DECIMAL(10,2) DEFAULT 0,
    currency VARCHAR(3) DEFAULT 'CNY',
    remark VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    pay_time DATETIME,
    ship_time DATETIME,
    receive_time DATETIME,
    finish_time DATETIME
);
```

### 订单明细表 (t_order_item)
```sql
CREATE TABLE t_order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    goods_id BIGINT NOT NULL,
    sku_id BIGINT NOT NULL,
    goods_name VARCHAR(255) NOT NULL,
    sku_name VARCHAR(255),
    goods_image VARCHAR(255),
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 订单配送表 (t_order_delivery)
```sql
CREATE TABLE t_order_delivery (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    delivery_type TINYINT NOT NULL,
    receiver_name VARCHAR(50) NOT NULL,
    receiver_phone VARCHAR(20) NOT NULL,
    receiver_province VARCHAR(20),
    receiver_city VARCHAR(20),
    receiver_district VARCHAR(20),
    receiver_address VARCHAR(255) NOT NULL,
    logistics_company VARCHAR(100),
    logistics_no VARCHAR(100),
    delivery_time DATETIME,
    receive_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 订单支付表 (t_order_payment)
```sql
CREATE TABLE t_order_payment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    payment_no VARCHAR(32) UNIQUE NOT NULL,
    pay_type TINYINT NOT NULL,
    pay_channel VARCHAR(50),
    pay_amount DECIMAL(10,2) NOT NULL,
    pay_status TINYINT DEFAULT 0,
    third_party_no VARCHAR(100),
    pay_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## 业务流程

### 订单创建流程
1. **购物车结算**: 用户选择商品进行结算
2. **库存检查**: 检查商品库存是否充足
3. **价格计算**: 计算订单总价和优惠
4. **订单生成**: 生成订单并锁定库存
5. **支付引导**: 引导用户进行支付

### 订单支付流程
1. **发起支付**: 用户选择支付方式
2. **调用支付**: 调用第三方支付接口
3. **支付回调**: 接收支付结果通知
4. **状态更新**: 更新订单支付状态
5. **库存扣减**: 扣减商品库存

### 订单履约流程
1. **订单确认**: 商户确认订单
2. **商品备货**: 准备发货商品
3. **订单发货**: 生成物流单并发货
4. **物流跟踪**: 跟踪物流状态
5. **确认收货**: 用户确认收货完成

### 售后处理流程
1. **售后申请**: 用户提交售后申请
2. **申请审核**: 商户审核售后申请
3. **退货处理**: 用户退货商品
4. **退款处理**: 处理退款到账
5. **售后完成**: 售后流程结束

## 状态机设计

### 订单状态流转
```
待支付 → 已支付 → 已发货 → 已收货 → 已完成
   ↓        ↓        ↓        ↓
已取消   已取消   已取消   售后中
```

### 支付状态流转
```
待支付 → 支付中 → 支付成功
   ↓        ↓
已取消   支付失败
```

## 开发指南

### 添加新的订单功能
1. 在 `order-api` 模块定义接口和 DTO
2. 在 `order-provider` 模块实现业务逻辑
3. 添加相应的数据库表和映射
4. 配置状态机流转规则
5. 编写单元测试和集成测试

### 集成新的支付方式
1. 实现支付接口适配器
2. 配置支付参数和回调
3. 处理支付结果和异常
4. 添加支付方式配置

## 性能优化

- ⚡ 订单数据分库分表
- ⚡ 热点订单数据缓存
- ⚡ 异步处理订单状态
- ⚡ 批量处理订单操作
- ⚡ 读写分离优化查询

## 安全考虑

- 🔒 订单数据权限控制
- 🔒 支付信息加密传输
- 🔒 订单状态防篡改
- 🔒 重复支付防护
- 🔒 订单操作审计日志

## 故障排查

### 常见问题
1. **订单创建失败**: 检查库存和价格计算逻辑
2. **支付回调异常**: 检查支付配置和网络连接
3. **订单状态异常**: 检查状态机配置和消息队列
4. **分布式事务失败**: 检查 Seata 配置和服务状态

### 日志查看
```bash
# 查看应用日志
tail -f logs/order-provider.log

# 查看订单创建日志
grep "CREATE_ORDER" logs/order-provider.log

# 查看支付日志
grep "PAYMENT" logs/order-provider.log

# 查看错误日志
grep "ERROR" logs/order-provider.log
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