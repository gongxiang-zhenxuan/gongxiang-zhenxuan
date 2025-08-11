# GXZ Delivery - 共享臻选配送履约服务

## 概述

`gxz-delivery` 是共享臻选微服务架构中的配送履约服务，负责处理订单配送、物流跟踪、配送员管理、路径规划、配送费计算等核心配送相关功能。

## 主要功能

### 🚚 配送管理
- **订单分配**: 智能订单分配和配送员匹配
- **路径规划**: 最优配送路径规划和导航
- **实时跟踪**: 配送过程实时位置跟踪
- **状态更新**: 配送状态实时更新和通知

### 👨‍💼 配送员管理
- **配送员注册**: 配送员入驻和资质审核
- **在线状态**: 配送员在线状态管理
- **工作区域**: 配送范围和服务区域设置
- **绩效考核**: 配送效率和服务质量评估

### 📍 物流跟踪
- **轨迹记录**: 配送轨迹实时记录
- **位置共享**: 用户实时查看配送位置
- **异常处理**: 配送异常和延迟处理
- **签收确认**: 电子签收和拍照确认

### 💰 费用计算
- **配送费计算**: 基于距离和时间的费用计算
- **动态定价**: 根据供需关系动态调整价格
- **优惠策略**: 配送费优惠和免配送费活动
- **结算管理**: 配送员费用结算和分成

## 技术栈

- **Java**: 17+
- **Spring Boot**: 3.x
- **Spring Cloud**: 2023.x
- **MyBatis Plus**: ORM 框架
- **MySQL**: 主数据库
- **Redis**: 缓存和位置存储
- **MongoDB**: 轨迹数据存储
- **RocketMQ**: 消息队列
- **高德地图 API**: 地图服务
- **WebSocket**: 实时通信
- **Nacos**: 服务注册发现

## 模块结构

```
gxz-delivery/
├── delivery-api/                # 配送服务 API 模块
│   ├── src/main/java/
│   │   └── com/gongxiang/zhenxuan/delivery/api/
│   │       ├── dto/            # 数据传输对象
│   │       ├── vo/             # 视图对象
│   │       ├── enums/          # 枚举定义
│   │       └── feign/          # Feign 客户端接口
│   └── pom.xml
├── delivery-provider/           # 配送服务提供者模块
│   ├── src/main/java/
│   │   └── com/gongxiang/zhenxuan/delivery/
│   │       ├── controller/     # 控制器层
│   │       ├── service/        # 服务层
│   │       ├── mapper/         # 数据访问层
│   │       ├── entity/         # 实体类
│   │       ├── config/         # 配置类
│   │       ├── algorithm/      # 路径规划算法
│   │       ├── websocket/      # WebSocket 处理
│   │       ├── job/            # 定时任务
│   │       └── DeliveryApplication.java
│   ├── src/main/resources/
│   │   ├── application.yml     # 配置文件
│   │   └── mapper/            # MyBatis 映射文件
│   └── pom.xml
└── pom.xml
```

## 核心功能模块

### 订单分配
```java
// 创建配送订单
DeliveryOrderDTO orderDTO = new DeliveryOrderDTO();
orderDTO.setOrderId(1L);
orderDTO.setPickupAddress("商家地址");
orderDTO.setDeliveryAddress("用户地址");
orderDTO.setPickupLat(new BigDecimal("39.908823"));
orderDTO.setPickupLng(new BigDecimal("116.397470"));
orderDTO.setDeliveryLat(new BigDecimal("39.918823"));
orderDTO.setDeliveryLng(new BigDecimal("116.407470"));
Result<DeliveryOrderVO> result = deliveryService.createOrder(orderDTO);

// 分配配送员
DeliveryAssignDTO assignDTO = new DeliveryAssignDTO();
assignDTO.setDeliveryOrderId(1L);
assignDTO.setDeliverymanId(1L);
Result<Void> assignResult = deliveryService.assignDeliveryman(assignDTO);
```

### 实时跟踪
```java
// 更新配送位置
LocationUpdateDTO locationDTO = new LocationUpdateDTO();
locationDTO.setDeliveryOrderId(1L);
locationDTO.setDeliverymanId(1L);
locationDTO.setLatitude(new BigDecimal("39.908823"));
locationDTO.setLongitude(new BigDecimal("116.397470"));
locationDTO.setUpdateTime(LocalDateTime.now());
Result<Void> updateResult = trackingService.updateLocation(locationDTO);

// 获取配送轨迹
TrackingQueryDTO queryDTO = new TrackingQueryDTO();
queryDTO.setDeliveryOrderId(1L);
Result<List<TrackingVO>> trackingResult = trackingService.getTrackingInfo(queryDTO);
```

### 配送员管理
```java
// 配送员注册
DeliverymanRegisterDTO registerDTO = new DeliverymanRegisterDTO();
registerDTO.setName("张三");
registerDTO.setPhone("13800138000");
registerDTO.setIdCard("110101199001011234");
registerDTO.setVehicleType(VehicleType.ELECTRIC_BIKE);
registerDTO.setServiceArea("朝阳区");
Result<DeliverymanVO> registerResult = deliverymanService.register(registerDTO);

// 更新在线状态
OnlineStatusDTO statusDTO = new OnlineStatusDTO();
statusDTO.setDeliverymanId(1L);
statusDTO.setOnlineStatus(OnlineStatus.ONLINE);
statusDTO.setLatitude(new BigDecimal("39.908823"));
statusDTO.setLongitude(new BigDecimal("116.397470"));
Result<Void> statusResult = deliverymanService.updateOnlineStatus(statusDTO);
```

## API 接口

### 配送订单接口
- `POST /api/delivery/order/create` - 创建配送订单
- `GET /api/delivery/order/{id}` - 获取配送订单详情
- `PUT /api/delivery/order/{id}/assign` - 分配配送员
- `PUT /api/delivery/order/{id}/status` - 更新配送状态
- `POST /api/delivery/order/{id}/cancel` - 取消配送订单
- `GET /api/delivery/order/list` - 获取配送订单列表

### 配送员管理接口
- `POST /api/deliveryman/register` - 配送员注册
- `GET /api/deliveryman/{id}` - 获取配送员信息
- `PUT /api/deliveryman/{id}` - 更新配送员信息
- `PUT /api/deliveryman/{id}/status` - 更新在线状态
- `GET /api/deliveryman/nearby` - 获取附近配送员
- `GET /api/deliveryman/orders` - 获取配送员订单

### 实时跟踪接口
- `POST /api/tracking/location/update` - 更新位置信息
- `GET /api/tracking/{orderId}` - 获取配送轨迹
- `GET /api/tracking/{orderId}/current` - 获取当前位置
- `POST /api/tracking/{orderId}/photo` - 上传配送照片
- `POST /api/tracking/{orderId}/sign` - 电子签收

### 费用计算接口
- `POST /api/delivery/fee/calculate` - 计算配送费
- `GET /api/delivery/fee/rules` - 获取计费规则
- `POST /api/delivery/settlement/create` - 创建结算单
- `GET /api/delivery/settlement/{id}` - 获取结算详情

## 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gxz_delivery?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
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
    database: 6
```

### MongoDB 配置
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://${MONGO_HOST:localhost}:${MONGO_PORT:27017}/gxz_delivery
```

### 地图服务配置
```yaml
map:
  amap:
    key: ${AMAP_KEY:your_amap_key}
    secret: ${AMAP_SECRET:your_amap_secret}
  baidu:
    key: ${BAIDU_KEY:your_baidu_key}
```

### 配送配置
```yaml
delivery:
  assignment:
    radius: 5000  # 分配半径(米)
    max-orders: 5  # 配送员最大接单数
  tracking:
    interval: 30  # 位置更新间隔(秒)
    timeout: 3600  # 配送超时时间(秒)
  fee:
    base-fee: 5.00  # 基础配送费
    per-km-fee: 2.00  # 每公里费用
    min-fee: 3.00  # 最低配送费
    max-fee: 20.00  # 最高配送费
```

## 启动方式

### 开发环境
```bash
# 启动依赖服务
docker-compose up -d mysql redis mongodb rocketmq

# 启动配送服务
cd delivery-provider
mvn spring-boot:run
```

### 生产环境
```bash
# 构建项目
mvn clean package -DskipTests

# 启动服务
java -jar delivery-provider/target/delivery-provider-1.0.0.jar
```

## 监控端点

- **健康检查**: `GET /actuator/health`
- **服务信息**: `GET /actuator/info`
- **指标监控**: `GET /actuator/metrics`
- **日志级别**: `GET /actuator/loggers`

## 数据库设计

### 配送订单表 (t_delivery_order)
```sql
CREATE TABLE t_delivery_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    delivery_code VARCHAR(32) UNIQUE NOT NULL,
    deliveryman_id BIGINT,
    pickup_address VARCHAR(500) NOT NULL,
    pickup_contact VARCHAR(100),
    pickup_phone VARCHAR(20),
    pickup_lat DECIMAL(10,6),
    pickup_lng DECIMAL(10,6),
    delivery_address VARCHAR(500) NOT NULL,
    delivery_contact VARCHAR(100),
    delivery_phone VARCHAR(20),
    delivery_lat DECIMAL(10,6),
    delivery_lng DECIMAL(10,6),
    distance DECIMAL(8,2),
    delivery_fee DECIMAL(10,2),
    estimated_time INT,
    actual_time INT,
    status TINYINT DEFAULT 0,
    pickup_time DATETIME,
    delivery_time DATETIME,
    remark VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 配送员表 (t_deliveryman)
```sql
CREATE TABLE t_deliveryman (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    deliveryman_code VARCHAR(32) UNIQUE NOT NULL,
    name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    id_card VARCHAR(18) UNIQUE NOT NULL,
    avatar VARCHAR(255),
    vehicle_type TINYINT NOT NULL,
    vehicle_no VARCHAR(20),
    service_area VARCHAR(100),
    online_status TINYINT DEFAULT 0,
    current_lat DECIMAL(10,6),
    current_lng DECIMAL(10,6),
    rating DECIMAL(3,2) DEFAULT 5.00,
    order_count INT DEFAULT 0,
    complete_count INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    register_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_online_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 配送轨迹表 (MongoDB)
```javascript
// delivery_tracking collection
{
  _id: ObjectId,
  deliveryOrderId: Long,
  deliverymanId: Long,
  trackingPoints: [
    {
      latitude: Double,
      longitude: Double,
      timestamp: Date,
      speed: Double,
      direction: Double
    }
  ],
  photos: [
    {
      url: String,
      type: String, // pickup, delivery, exception
      timestamp: Date,
      location: {
        latitude: Double,
        longitude: Double
      }
    }
  ],
  createTime: Date,
  updateTime: Date
}
```

### 配送费规则表 (t_delivery_fee_rule)
```sql
CREATE TABLE t_delivery_fee_rule (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    rule_name VARCHAR(100) NOT NULL,
    rule_type TINYINT NOT NULL,
    area_code VARCHAR(20),
    base_fee DECIMAL(10,2) NOT NULL,
    per_km_fee DECIMAL(10,2),
    min_fee DECIMAL(10,2),
    max_fee DECIMAL(10,2),
    free_distance DECIMAL(8,2),
    time_factor DECIMAL(5,2) DEFAULT 1.00,
    weather_factor DECIMAL(5,2) DEFAULT 1.00,
    priority INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    start_time TIME,
    end_time TIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## 业务流程

### 配送订单流程
1. **订单创建**: 用户下单后创建配送订单
2. **配送员分配**: 系统自动或手动分配配送员
3. **接单确认**: 配送员确认接单
4. **前往取货**: 配送员前往商家取货
5. **取货确认**: 扫码或拍照确认取货
6. **配送中**: 前往用户地址配送
7. **送达确认**: 用户签收或拍照确认
8. **订单完成**: 配送订单完成

### 配送员管理流程
1. **注册申请**: 配送员提交注册申请
2. **资质审核**: 审核身份证、驾驶证等
3. **培训考试**: 完成配送培训和考试
4. **账号激活**: 审核通过激活账号
5. **上线接单**: 配送员上线开始接单
6. **绩效考核**: 定期评估配送绩效

### 实时跟踪流程
1. **位置上报**: 配送员定时上报位置
2. **轨迹记录**: 系统记录配送轨迹
3. **状态同步**: 实时同步配送状态
4. **异常处理**: 处理配送异常情况
5. **用户通知**: 通知用户配送进度

## 路径规划算法

### 最短路径算法
```java
public class RouteOptimizer {
    
    public RouteResult optimizeRoute(RouteRequest request) {
        // 使用 A* 算法计算最优路径
        List<Point> waypoints = request.getWaypoints();
        Point start = request.getStartPoint();
        Point end = request.getEndPoint();
        
        // 调用地图 API 获取路径
        RouteResult result = mapService.calculateRoute(start, end, waypoints);
        
        // 优化路径
        result = optimizePath(result);
        
        return result;
    }
    
    private RouteResult optimizePath(RouteResult original) {
        // 路径优化逻辑
        return original;
    }
}
```

### 配送员分配算法
```java
public class DeliverymanAssignmentAlgorithm {
    
    public DeliverymanVO findBestDeliveryman(AssignmentRequest request) {
        // 获取附近配送员
        List<DeliverymanVO> nearbyDeliverymen = 
            deliverymanService.findNearby(request.getPickupLocation(), 5000);
        
        // 过滤可用配送员
        List<DeliverymanVO> availableDeliverymen = nearbyDeliverymen.stream()
            .filter(d -> d.getOnlineStatus() == OnlineStatus.ONLINE)
            .filter(d -> d.getCurrentOrderCount() < d.getMaxOrderCount())
            .collect(Collectors.toList());
        
        // 计算最优配送员
        return availableDeliverymen.stream()
            .min(Comparator.comparing(this::calculateScore))
            .orElse(null);
    }
    
    private double calculateScore(DeliverymanVO deliveryman) {
        // 综合评分算法
        double distanceScore = calculateDistanceScore(deliveryman);
        double ratingScore = deliveryman.getRating().doubleValue();
        double efficiencyScore = calculateEfficiencyScore(deliveryman);
        
        return distanceScore * 0.4 + ratingScore * 0.3 + efficiencyScore * 0.3;
    }
}
```

## WebSocket 实时通信

### WebSocket 配置
```java
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new DeliveryTrackingHandler(), "/ws/delivery/tracking")
                .setAllowedOrigins("*");
    }
}
```

### 位置推送
```java
@Component
public class LocationPushService {
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    public void pushLocationUpdate(Long orderId, LocationUpdateDTO location) {
        // 推送给用户
        messagingTemplate.convertAndSend(
            "/topic/delivery/" + orderId + "/location", 
            location
        );
        
        // 推送给商家
        messagingTemplate.convertAndSend(
            "/topic/merchant/" + orderId + "/location", 
            location
        );
    }
}
```

## 性能优化

### 位置缓存优化
- Redis 存储实时位置
- 位置更新批量处理
- 轨迹数据分片存储
- 历史数据定期清理

### 分配算法优化
- 地理位置索引
- 配送员状态缓存
- 分配结果缓存
- 异步分配处理

### 路径规划优化
- 路径结果缓存
- 批量路径计算
- 地图数据预加载
- CDN 加速地图服务

## 安全考虑

- 🔒 配送员身份认证和授权
- 🔒 位置数据加密传输
- 🔒 敏感信息脱敏处理
- 🔒 配送照片水印和防篡改
- 🔒 异常行为监控和预警

## 故障排查

### 常见问题
1. **配送员分配失败**: 检查配送员状态和服务区域
2. **位置更新异常**: 检查 GPS 信号和网络连接
3. **路径规划错误**: 检查地图 API 配置和调用限制
4. **WebSocket 连接断开**: 检查网络稳定性和服务器负载

### 日志查看
```bash
# 查看应用日志
tail -f logs/delivery-provider.log

# 查看配送日志
grep "DELIVERY" logs/delivery-provider.log

# 查看位置更新日志
grep "LOCATION" logs/delivery-provider.log

# 查看分配日志
grep "ASSIGNMENT" logs/delivery-provider.log

# 查看错误日志
grep "ERROR" logs/delivery-provider.log
```

## 开发指南

### 添加新的配送类型
1. 定义配送类型枚举
2. 实现配送策略接口
3. 配置费用计算规则
4. 添加状态流转逻辑
5. 编写单元测试

### 集成新的地图服务
1. 实现地图服务接口
2. 配置 API 密钥
3. 适配数据格式
4. 添加降级策略
5. 性能测试验证

## 版本信息

- **当前版本**: 1.0.0
- **最低 JDK 版本**: 17
- **Spring Boot 版本**: 3.x
- **Spring Cloud 版本**: 2023.x

---

**维护团队**: 共享臻选开发团队  
**创建时间**: 2024年  
**最后更新**: 2024年