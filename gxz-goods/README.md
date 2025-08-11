# GXZ Goods - 共享臻选商品服务

## 概述

`gxz-goods` 是共享臻选微服务架构中的商品管理服务，负责处理商品信息管理、分类管理、库存管理、价格管理、商品搜索等核心商品相关功能。

## 主要功能

### 📦 商品管理
- **商品信息**: 商品基本信息、详情、规格参数管理
- **商品分类**: 多级分类体系和分类属性管理
- **商品品牌**: 品牌信息和品牌授权管理
- **商品标签**: 商品标签和特色标识管理

### 📊 库存管理
- **库存监控**: 实时库存数量监控和预警
- **库存调整**: 入库、出库、调拨等库存操作
- **安全库存**: 安全库存设置和自动补货
- **库存盘点**: 定期库存盘点和差异处理

### 💰 价格管理
- **价格策略**: 多种价格策略和动态定价
- **促销价格**: 促销活动价格和优惠券支持
- **会员价格**: 不同会员等级的专享价格
- **批发价格**: 批量采购的阶梯价格

### 🔍 搜索服务
- **全文搜索**: 基于 Elasticsearch 的全文搜索
- **智能推荐**: 基于用户行为的商品推荐
- **搜索优化**: 搜索结果排序和相关性优化
- **搜索统计**: 搜索关键词统计和热词分析

## 技术栈

- **Java**: 17+
- **Spring Boot**: 3.x
- **Spring Cloud**: 2023.x
- **MyBatis Plus**: ORM 框架
- **MySQL**: 主数据库
- **Redis**: 缓存存储
- **Elasticsearch**: 搜索引擎
- **RocketMQ**: 消息队列
- **MinIO**: 对象存储
- **Nacos**: 服务注册发现

## 模块结构

```
gxz-goods/
├── goods-api/                   # 商品服务 API 模块
│   ├── src/main/java/
│   │   └── com/gongxiang/zhenxuan/goods/api/
│   │       ├── dto/            # 数据传输对象
│   │       ├── vo/             # 视图对象
│   │       ├── enums/          # 枚举定义
│   │       └── feign/          # Feign 客户端接口
│   └── pom.xml
├── goods-provider/              # 商品服务提供者模块
│   ├── src/main/java/
│   │   └── com/gongxiang/zhenxuan/goods/
│   │       ├── controller/     # 控制器层
│   │       ├── service/        # 服务层
│   │       ├── mapper/         # 数据访问层
│   │       ├── entity/         # 实体类
│   │       ├── config/         # 配置类
│   │       ├── search/         # 搜索相关
│   │       └── GoodsApplication.java
│   ├── src/main/resources/
│   │   ├── application.yml     # 配置文件
│   │   └── mapper/            # MyBatis 映射文件
│   └── pom.xml
└── pom.xml
```

## 核心功能模块

### 商品管理
```java
// 创建商品
GoodsCreateDTO createDTO = new GoodsCreateDTO();
createDTO.setGoodsName("iPhone 15 Pro");
createDTO.setCategoryId(1L);
createDTO.setBrandId(1L);
createDTO.setPrice(new BigDecimal("7999.00"));
Result<GoodsVO> result = goodsService.createGoods(createDTO);

// 更新商品
GoodsUpdateDTO updateDTO = new GoodsUpdateDTO();
updateDTO.setGoodsId(1L);
updateDTO.setGoodsName("iPhone 15 Pro Max");
Result<Void> updateResult = goodsService.updateGoods(updateDTO);
```

### 库存管理
```java
// 库存调整
StockAdjustDTO adjustDTO = new StockAdjustDTO();
adjustDTO.setGoodsId(1L);
adjustDTO.setSkuId(1L);
adjustDTO.setAdjustType(StockAdjustType.IN);
adjustDTO.setQuantity(100);
Result<Void> adjustResult = stockService.adjustStock(adjustDTO);

// 库存查询
Result<StockVO> stockResult = stockService.getStock(1L, 1L);
```

### 商品搜索
```java
// 搜索商品
GoodsSearchDTO searchDTO = new GoodsSearchDTO();
searchDTO.setKeyword("手机");
searchDTO.setCategoryId(1L);
searchDTO.setMinPrice(new BigDecimal("1000"));
searchDTO.setMaxPrice(new BigDecimal("10000"));
Result<PageResult<GoodsVO>> searchResult = searchService.searchGoods(searchDTO);
```

## API 接口

### 商品管理接口
- `POST /api/goods/create` - 创建商品
- `GET /api/goods/{id}` - 获取商品详情
- `PUT /api/goods/{id}` - 更新商品信息
- `DELETE /api/goods/{id}` - 删除商品
- `GET /api/goods/list` - 获取商品列表
- `PUT /api/goods/{id}/status` - 更新商品状态

### 分类管理接口
- `POST /api/category/create` - 创建分类
- `GET /api/category/{id}` - 获取分类信息
- `PUT /api/category/{id}` - 更新分类信息
- `DELETE /api/category/{id}` - 删除分类
- `GET /api/category/tree` - 获取分类树

### 库存管理接口
- `GET /api/stock/{goodsId}/{skuId}` - 获取库存信息
- `POST /api/stock/adjust` - 库存调整
- `GET /api/stock/list` - 获取库存列表
- `POST /api/stock/check` - 库存盘点
- `GET /api/stock/warning` - 库存预警

### 搜索接口
- `GET /api/search/goods` - 商品搜索
- `GET /api/search/suggest` - 搜索建议
- `GET /api/search/hot` - 热门搜索
- `POST /api/search/index` - 重建搜索索引

### 价格管理接口
- `GET /api/price/{goodsId}` - 获取商品价格
- `PUT /api/price/{goodsId}` - 更新商品价格
- `GET /api/price/history/{goodsId}` - 价格历史
- `POST /api/price/batch` - 批量更新价格

## 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gxz_goods?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
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
    database: 3
```

### Elasticsearch 配置
```yaml
spring:
  elasticsearch:
    uris: ${ES_URIS:http://localhost:9200}
    username: ${ES_USERNAME:}
    password: ${ES_PASSWORD:}
```

### MinIO 配置
```yaml
minio:
  endpoint: ${MINIO_ENDPOINT:http://localhost:9000}
  access-key: ${MINIO_ACCESS_KEY:minioadmin}
  secret-key: ${MINIO_SECRET_KEY:minioadmin}
  bucket-name: ${MINIO_BUCKET:goods-images}
```

## 启动方式

### 开发环境
```bash
# 启动依赖服务
docker-compose up -d mysql redis elasticsearch minio

# 启动商品服务
cd goods-provider
mvn spring-boot:run
```

### 生产环境
```bash
# 构建项目
mvn clean package -DskipTests

# 启动服务
java -jar goods-provider/target/goods-provider-1.0.0.jar
```

## 监控端点

- **健康检查**: `GET /actuator/health`
- **服务信息**: `GET /actuator/info`
- **指标监控**: `GET /actuator/metrics`
- **日志级别**: `GET /actuator/loggers`

## 数据库设计

### 商品表 (t_goods)
```sql
CREATE TABLE t_goods (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    goods_code VARCHAR(32) UNIQUE NOT NULL,
    goods_name VARCHAR(255) NOT NULL,
    goods_title VARCHAR(500),
    goods_subtitle VARCHAR(500),
    category_id BIGINT NOT NULL,
    brand_id BIGINT,
    merchant_id BIGINT NOT NULL,
    shop_id BIGINT NOT NULL,
    goods_type TINYINT DEFAULT 1,
    price DECIMAL(10,2) NOT NULL,
    market_price DECIMAL(10,2),
    cost_price DECIMAL(10,2),
    weight DECIMAL(8,3),
    volume DECIMAL(8,3),
    main_image VARCHAR(255),
    images TEXT,
    description LONGTEXT,
    specifications TEXT,
    status TINYINT DEFAULT 1,
    sort_order INT DEFAULT 0,
    sales_count INT DEFAULT 0,
    view_count INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 商品分类表 (t_goods_category)
```sql
CREATE TABLE t_goods_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100) NOT NULL,
    category_code VARCHAR(50) UNIQUE NOT NULL,
    parent_id BIGINT DEFAULT 0,
    level TINYINT DEFAULT 1,
    icon VARCHAR(255),
    image VARCHAR(255),
    description VARCHAR(500),
    sort_order INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 商品库存表 (t_goods_stock)
```sql
CREATE TABLE t_goods_stock (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    goods_id BIGINT NOT NULL,
    sku_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0,
    available_quantity INT NOT NULL DEFAULT 0,
    frozen_quantity INT NOT NULL DEFAULT 0,
    safety_stock INT DEFAULT 0,
    warning_stock INT DEFAULT 0,
    last_in_time DATETIME,
    last_out_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_goods_sku_warehouse (goods_id, sku_id, warehouse_id)
);
```

### SKU 表 (t_goods_sku)
```sql
CREATE TABLE t_goods_sku (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    goods_id BIGINT NOT NULL,
    sku_code VARCHAR(50) UNIQUE NOT NULL,
    sku_name VARCHAR(255),
    attributes TEXT,
    price DECIMAL(10,2) NOT NULL,
    market_price DECIMAL(10,2),
    cost_price DECIMAL(10,2),
    weight DECIMAL(8,3),
    volume DECIMAL(8,3),
    image VARCHAR(255),
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## 业务流程

### 商品发布流程
1. **商品创建**: 商户创建商品基本信息
2. **规格设置**: 设置商品规格和 SKU
3. **价格设置**: 设置商品价格策略
4. **库存设置**: 设置初始库存数量
5. **审核提交**: 提交商品审核
6. **审核通过**: 商品上架销售

### 库存管理流程
1. **入库**: 商品入库增加库存
2. **出库**: 订单确认减少库存
3. **调拨**: 仓库间库存调拨
4. **盘点**: 定期库存盘点
5. **预警**: 库存不足自动预警

## 开发指南

### 添加新的商品功能
1. 在 `goods-api` 模块定义接口和 DTO
2. 在 `goods-provider` 模块实现业务逻辑
3. 添加相应的数据库表和映射
4. 更新搜索索引结构
5. 编写单元测试和集成测试

### 搜索功能开发
1. 定义 Elasticsearch 索引结构
2. 实现数据同步机制
3. 开发搜索查询逻辑
4. 优化搜索性能和相关性

## 性能优化

- ⚡ 商品信息多级缓存
- ⚡ 热门商品数据预热
- ⚡ 搜索结果缓存优化
- ⚡ 数据库读写分离
- ⚡ 图片 CDN 加速
- ⚡ 异步处理库存更新

## 安全考虑

- 🔒 商品数据权限控制
- 🔒 价格信息加密传输
- 🔒 库存操作审计日志
- 🔒 图片上传安全检查
- 🔒 API 接口访问限制

## 故障排查

### 常见问题
1. **搜索功能异常**: 检查 Elasticsearch 服务和索引状态
2. **库存数据不一致**: 检查消息队列和事务处理
3. **图片加载失败**: 检查 MinIO 服务和网络连接
4. **价格显示异常**: 检查价格策略和缓存数据

### 日志查看
```bash
# 查看应用日志
tail -f logs/goods-provider.log

# 查看库存操作日志
grep "STOCK" logs/goods-provider.log

# 查看搜索日志
grep "SEARCH" logs/goods-provider.log

# 查看错误日志
grep "ERROR" logs/goods-provider.log
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