# GXZ Admin - 共享臻选管理后台服务

## 概述

`gxz-admin` 是共享臻选微服务架构中的管理后台服务，为平台运营人员、商户管理员、系统管理员提供统一的管理界面和功能，包括用户管理、商户管理、订单管理、数据统计、系统配置等核心管理功能。

## 主要功能

### 👥 用户管理
- **用户列表**: 查看所有注册用户信息
- **用户详情**: 查看用户详细信息和行为数据
- **用户状态**: 启用/禁用用户账户
- **用户标签**: 用户分类和标签管理
- **实名认证**: 用户实名认证审核

### 🏪 商户管理
- **商户入驻**: 商户入驻申请审核
- **商户信息**: 商户基本信息管理
- **资质审核**: 商户资质文件审核
- **店铺管理**: 商户店铺信息管理
- **商户结算**: 商户结算数据查看

### 📦 商品管理
- **商品审核**: 商户上架商品审核
- **分类管理**: 商品分类体系管理
- **品牌管理**: 商品品牌信息管理
- **库存监控**: 商品库存预警监控
- **价格监控**: 商品价格变动监控

### 📋 订单管理
- **订单列表**: 查看所有订单信息
- **订单详情**: 订单详细信息查看
- **订单状态**: 订单状态流转管理
- **退款处理**: 订单退款审核处理
- **异常订单**: 异常订单处理和跟踪

### 🚚 配送管理
- **配送员管理**: 配送员信息和状态管理
- **配送区域**: 配送区域和范围设置
- **配送监控**: 实时配送状态监控
- **配送统计**: 配送效率和质量统计

### 💰 财务管理
- **交易流水**: 平台交易流水查看
- **结算管理**: 商户结算数据管理
- **提现审核**: 商户提现申请审核
- **财务报表**: 各类财务报表生成
- **对账管理**: 财务对账结果查看

### 🎯 营销管理
- **优惠券管理**: 优惠券模板和发放管理
- **促销活动**: 促销活动创建和管理
- **积分规则**: 积分获得和消费规则
- **会员管理**: 会员等级和权益管理

### 📊 数据统计
- **运营数据**: 平台核心运营指标
- **用户分析**: 用户行为和画像分析
- **商户分析**: 商户经营数据分析
- **商品分析**: 商品销售数据分析
- **财务分析**: 财务收入和成本分析

### ⚙️ 系统管理
- **管理员管理**: 后台管理员账户管理
- **角色权限**: 角色和权限配置管理
- **系统配置**: 系统参数和配置管理
- **操作日志**: 管理员操作日志查看
- **系统监控**: 系统运行状态监控

## 技术栈

- **Java**: 17+
- **Spring Boot**: 3.x
- **Spring Cloud**: 2023.x
- **Spring Security**: 安全框架
- **MyBatis Plus**: ORM 框架
- **MySQL**: 主数据库
- **Redis**: 缓存和会话存储
- **Vue.js**: 前端框架
- **Element Plus**: UI 组件库
- **ECharts**: 数据可视化
- **Nacos**: 服务注册发现

## 模块结构

```
gxz-admin/
├── admin-api/                  # 管理后台 API 模块
│   ├── src/main/java/
│   │   └── com/gongxiang/zhenxuan/admin/api/
│   │       ├── dto/            # 数据传输对象
│   │       ├── vo/             # 视图对象
│   │       ├── enums/          # 枚举定义
│   │       └── feign/          # Feign 客户端接口
│   └── pom.xml
├── admin-provider/             # 管理后台服务提供者模块
│   ├── src/main/java/
│   │   └── com/gongxiang/zhenxuan/admin/
│   │       ├── controller/     # 控制器层
│   │       ├── service/        # 服务层
│   │       ├── mapper/         # 数据访问层
│   │       ├── entity/         # 实体类
│   │       ├── config/         # 配置类
│   │       ├── security/       # 安全配置
│   │       ├── statistics/     # 统计分析
│   │       ├── report/         # 报表生成
│   │       ├── audit/          # 审核流程
│   │       └── AdminApplication.java
│   ├── src/main/resources/
│   │   ├── application.yml     # 配置文件
│   │   └── mapper/            # MyBatis 映射文件
│   └── pom.xml
├── admin-web/                  # 管理后台前端模块
│   ├── src/
│   │   ├── api/               # API 接口
│   │   ├── components/        # 公共组件
│   │   ├── views/             # 页面视图
│   │   ├── router/            # 路由配置
│   │   ├── store/             # 状态管理
│   │   ├── utils/             # 工具函数
│   │   └── main.js            # 入口文件
│   ├── public/
│   ├── package.json
│   └── vite.config.js
└── pom.xml
```

## 核心功能模块

### 用户管理
```java
// 查询用户列表
UserQueryDTO queryDTO = new UserQueryDTO();
queryDTO.setKeyword("张三");
queryDTO.setUserStatus(UserStatus.ACTIVE);
queryDTO.setPageNum(1);
queryDTO.setPageSize(20);
Result<PageVO<UserVO>> userList = userService.getUserList(queryDTO);

// 禁用用户
UserStatusDTO statusDTO = new UserStatusDTO();
statusDTO.setUserId(1L);
statusDTO.setUserStatus(UserStatus.DISABLED);
statusDTO.setReason("违规操作");
Result<Void> result = userService.updateUserStatus(statusDTO);

// 用户实名认证审核
UserRealNameAuditDTO auditDTO = new UserRealNameAuditDTO();
auditDTO.setUserId(1L);
auditDTO.setAuditStatus(AuditStatus.APPROVED);
auditDTO.setAuditRemark("审核通过");
Result<Void> auditResult = userService.auditRealName(auditDTO);
```

### 商户管理
```java
// 商户入驻审核
MerchantAuditDTO auditDTO = new MerchantAuditDTO();
auditDTO.setMerchantId(1L);
auditDTO.setAuditStatus(AuditStatus.APPROVED);
auditDTO.setAuditRemark("资质齐全，审核通过");
Result<Void> auditResult = merchantService.auditMerchant(auditDTO);

// 查询商户列表
MerchantQueryDTO queryDTO = new MerchantQueryDTO();
queryDTO.setMerchantName("测试商户");
queryDTO.setMerchantStatus(MerchantStatus.ACTIVE);
queryDTO.setPageNum(1);
queryDTO.setPageSize(20);
Result<PageVO<MerchantVO>> merchantList = merchantService.getMerchantList(queryDTO);

// 商户资质审核
MerchantQualificationAuditDTO qualificationDTO = new MerchantQualificationAuditDTO();
qualificationDTO.setMerchantId(1L);
qualificationDTO.setQualificationType(QualificationType.BUSINESS_LICENSE);
qualificationDTO.setAuditStatus(AuditStatus.APPROVED);
Result<Void> qualificationResult = merchantService.auditQualification(qualificationDTO);
```

### 订单管理
```java
// 查询订单列表
OrderQueryDTO queryDTO = new OrderQueryDTO();
queryDTO.setOrderNo("ORDER_123456");
queryDTO.setOrderStatus(OrderStatus.PAID);
queryDTO.setStartTime(LocalDateTime.now().minusDays(7));
queryDTO.setEndTime(LocalDateTime.now());
queryDTO.setPageNum(1);
queryDTO.setPageSize(20);
Result<PageVO<OrderVO>> orderList = orderService.getOrderList(queryDTO);

// 处理退款申请
RefundProcessDTO processDTO = new RefundProcessDTO();
processDTO.setRefundId(1L);
processDTO.setProcessStatus(RefundStatus.APPROVED);
processDTO.setProcessRemark("同意退款");
Result<Void> processResult = orderService.processRefund(processDTO);

// 异常订单处理
OrderExceptionDTO exceptionDTO = new OrderExceptionDTO();
exceptionDTO.setOrderId(1L);
exceptionDTO.setExceptionType(OrderExceptionType.PAYMENT_TIMEOUT);
exceptionDTO.setHandleAction(HandleAction.CANCEL_ORDER);
Result<Void> exceptionResult = orderService.handleOrderException(exceptionDTO);
```

### 数据统计
```java
// 获取运营概览数据
OperationOverviewDTO overviewDTO = new OperationOverviewDTO();
overviewDTO.setStartDate(LocalDate.now().minusDays(30));
overviewDTO.setEndDate(LocalDate.now());
Result<OperationOverviewVO> overview = statisticsService.getOperationOverview(overviewDTO);

// 获取用户增长趋势
UserGrowthDTO growthDTO = new UserGrowthDTO();
growthDTO.setStartDate(LocalDate.now().minusDays(30));
growthDTO.setEndDate(LocalDate.now());
growthDTO.setGranularity(TimeGranularity.DAILY);
Result<List<UserGrowthVO>> userGrowth = statisticsService.getUserGrowthTrend(growthDTO);

// 获取销售数据分析
SalesAnalysisDTO analysisDTO = new SalesAnalysisDTO();
analysisDTO.setStartDate(LocalDate.now().minusDays(30));
analysisDTO.setEndDate(LocalDate.now());
analysisDTO.setDimension(AnalysisDimension.CATEGORY);
Result<List<SalesAnalysisVO>> salesAnalysis = statisticsService.getSalesAnalysis(analysisDTO);
```

## API 接口

### 用户管理接口
- `GET /api/admin/user/list` - 获取用户列表
- `GET /api/admin/user/{id}` - 获取用户详情
- `PUT /api/admin/user/{id}/status` - 更新用户状态
- `POST /api/admin/user/realname/audit` - 实名认证审核
- `GET /api/admin/user/statistics` - 用户统计数据

### 商户管理接口
- `GET /api/admin/merchant/list` - 获取商户列表
- `GET /api/admin/merchant/{id}` - 获取商户详情
- `POST /api/admin/merchant/audit` - 商户入驻审核
- `POST /api/admin/merchant/qualification/audit` - 资质审核
- `PUT /api/admin/merchant/{id}/status` - 更新商户状态

### 商品管理接口
- `GET /api/admin/goods/list` - 获取商品列表
- `GET /api/admin/goods/{id}` - 获取商品详情
- `POST /api/admin/goods/audit` - 商品审核
- `GET /api/admin/goods/category/list` - 获取分类列表
- `POST /api/admin/goods/category` - 创建商品分类
- `PUT /api/admin/goods/category/{id}` - 更新商品分类

### 订单管理接口
- `GET /api/admin/order/list` - 获取订单列表
- `GET /api/admin/order/{id}` - 获取订单详情
- `POST /api/admin/order/refund/process` - 处理退款申请
- `POST /api/admin/order/exception/handle` - 处理异常订单
- `GET /api/admin/order/statistics` - 订单统计数据

### 财务管理接口
- `GET /api/admin/finance/transaction/list` - 获取交易流水
- `GET /api/admin/finance/settlement/list` - 获取结算列表
- `POST /api/admin/finance/withdraw/audit` - 提现审核
- `GET /api/admin/finance/report/revenue` - 收入报表
- `GET /api/admin/finance/reconcile/list` - 对账结果列表

### 营销管理接口
- `GET /api/admin/promotion/coupon/template/list` - 获取优惠券模板
- `POST /api/admin/promotion/coupon/template` - 创建优惠券模板
- `POST /api/admin/promotion/coupon/issue` - 发放优惠券
- `GET /api/admin/promotion/activity/list` - 获取促销活动
- `POST /api/admin/promotion/activity` - 创建促销活动

### 数据统计接口
- `GET /api/admin/statistics/overview` - 运营概览
- `GET /api/admin/statistics/user/growth` - 用户增长趋势
- `GET /api/admin/statistics/sales/analysis` - 销售数据分析
- `GET /api/admin/statistics/merchant/ranking` - 商户排行榜
- `GET /api/admin/statistics/goods/ranking` - 商品排行榜

### 系统管理接口
- `GET /api/admin/system/admin/list` - 获取管理员列表
- `POST /api/admin/system/admin` - 创建管理员
- `GET /api/admin/system/role/list` - 获取角色列表
- `POST /api/admin/system/role` - 创建角色
- `GET /api/admin/system/permission/list` - 获取权限列表
- `GET /api/admin/system/config/list` - 获取系统配置
- `PUT /api/admin/system/config/{key}` - 更新系统配置
- `GET /api/admin/system/log/operation` - 获取操作日志

## 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gxz_admin?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
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
    database: 10
```

### 安全配置
```yaml
security:
  jwt:
    secret: ${JWT_SECRET:gxz-admin-secret-key}
    expiration: 86400  # 24小时
  session:
    timeout: 1800      # 30分钟
  password:
    min-length: 8
    require-special-char: true
    require-number: true
    require-uppercase: true
```

### 管理后台配置
```yaml
admin:
  upload:
    max-file-size: 10MB
    allowed-types: jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx
    upload-path: /data/uploads/
  audit:
    auto-audit: false
    audit-timeout: 72  # 72小时
  statistics:
    cache-duration: 300  # 5分钟
    max-query-days: 90   # 最大查询90天
  export:
    max-records: 10000   # 最大导出记录数
    temp-dir: /tmp/export/
```

## 启动方式

### 后端服务启动
```bash
# 启动依赖服务
docker-compose up -d mysql redis nacos

# 启动管理后台服务
cd admin-provider
mvn spring-boot:run
```

### 前端服务启动
```bash
# 安装依赖
cd admin-web
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build
```

### 生产环境部署
```bash
# 构建后端服务
mvn clean package -DskipTests

# 构建前端资源
cd admin-web
npm run build

# 启动后端服务
java -jar admin-provider/target/admin-provider-1.0.0.jar

# 部署前端资源到 Nginx
cp -r admin-web/dist/* /usr/share/nginx/html/admin/
```

## 监控端点

- **健康检查**: `GET /actuator/health`
- **服务信息**: `GET /actuator/info`
- **指标监控**: `GET /actuator/metrics`
- **日志级别**: `GET /actuator/loggers`

## 数据库设计

### 管理员表 (t_admin_user)
```sql
CREATE TABLE t_admin_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(128) NOT NULL,
    real_name VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    avatar VARCHAR(500),
    user_status TINYINT DEFAULT 1,
    last_login_time DATETIME,
    last_login_ip VARCHAR(50),
    password_update_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 角色表 (t_admin_role)
```sql
CREATE TABLE t_admin_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL,
    role_code VARCHAR(50) UNIQUE NOT NULL,
    role_desc VARCHAR(200),
    role_status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 权限表 (t_admin_permission)
```sql
CREATE TABLE t_admin_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    permission_name VARCHAR(100) NOT NULL,
    permission_code VARCHAR(100) UNIQUE NOT NULL,
    permission_type TINYINT NOT NULL,
    parent_id BIGINT DEFAULT 0,
    path VARCHAR(200),
    component VARCHAR(200),
    icon VARCHAR(100),
    sort_order INT DEFAULT 0,
    permission_status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 用户角色关联表 (t_admin_user_role)
```sql
CREATE TABLE t_admin_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_role (user_id, role_id)
);
```

### 角色权限关联表 (t_admin_role_permission)
```sql
CREATE TABLE t_admin_role_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_role_permission (role_id, permission_id)
);
```

### 操作日志表 (t_admin_operation_log)
```sql
CREATE TABLE t_admin_operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    username VARCHAR(50) NOT NULL,
    operation_type VARCHAR(50) NOT NULL,
    operation_desc VARCHAR(500),
    request_method VARCHAR(10),
    request_url VARCHAR(500),
    request_params TEXT,
    response_result TEXT,
    client_ip VARCHAR(50),
    user_agent VARCHAR(500),
    execution_time BIGINT,
    operation_status TINYINT DEFAULT 1,
    error_message TEXT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_operation_type (operation_type),
    INDEX idx_create_time (create_time)
);
```

### 系统配置表 (t_admin_system_config)
```sql
CREATE TABLE t_admin_system_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_key VARCHAR(100) UNIQUE NOT NULL,
    config_value TEXT,
    config_desc VARCHAR(200),
    config_type VARCHAR(50) DEFAULT 'STRING',
    config_group VARCHAR(50) DEFAULT 'DEFAULT',
    is_system TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## 核心服务实现

### 权限验证服务
```java
@Service
public class AdminSecurityService {
    
    public boolean hasPermission(Long userId, String permissionCode) {
        // 获取用户角色
        List<AdminRole> roles = adminUserRoleMapper.getRolesByUserId(userId);
        
        // 获取角色权限
        List<String> permissions = new ArrayList<>();
        for (AdminRole role : roles) {
            List<String> rolePermissions = adminRolePermissionMapper
                .getPermissionCodesByRoleId(role.getId());
            permissions.addAll(rolePermissions);
        }
        
        // 检查是否有指定权限
        return permissions.contains(permissionCode) || permissions.contains("*");
    }
    
    @Cacheable(value = "admin:permissions", key = "#userId")
    public List<AdminPermission> getUserPermissions(Long userId) {
        // 获取用户所有权限
        return adminPermissionMapper.getPermissionsByUserId(userId);
    }
    
    public List<AdminPermission> buildPermissionTree(List<AdminPermission> permissions) {
        // 构建权限树结构
        Map<Long, AdminPermission> permissionMap = permissions.stream()
            .collect(Collectors.toMap(AdminPermission::getId, p -> p));
        
        List<AdminPermission> tree = new ArrayList<>();
        
        for (AdminPermission permission : permissions) {
            if (permission.getParentId() == 0) {
                tree.add(permission);
            } else {
                AdminPermission parent = permissionMap.get(permission.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(permission);
                }
            }
        }
        
        return tree;
    }
}
```

### 数据统计服务
```java
@Service
public class AdminStatisticsService {
    
    @Cacheable(value = "admin:statistics:overview", key = "#startDate + '_' + #endDate")
    public OperationOverviewVO getOperationOverview(LocalDate startDate, LocalDate endDate) {
        // 用户统计
        Long totalUsers = userMapper.countUsers();
        Long newUsers = userMapper.countUsersByDateRange(startDate, endDate);
        
        // 商户统计
        Long totalMerchants = merchantMapper.countMerchants();
        Long newMerchants = merchantMapper.countMerchantsByDateRange(startDate, endDate);
        
        // 订单统计
        Long totalOrders = orderMapper.countOrders();
        Long newOrders = orderMapper.countOrdersByDateRange(startDate, endDate);
        BigDecimal totalAmount = orderMapper.sumOrderAmountByDateRange(startDate, endDate);
        
        // 商品统计
        Long totalGoods = goodsMapper.countGoods();
        Long newGoods = goodsMapper.countGoodsByDateRange(startDate, endDate);
        
        return OperationOverviewVO.builder()
            .totalUsers(totalUsers)
            .newUsers(newUsers)
            .totalMerchants(totalMerchants)
            .newMerchants(newMerchants)
            .totalOrders(totalOrders)
            .newOrders(newOrders)
            .totalAmount(totalAmount)
            .totalGoods(totalGoods)
            .newGoods(newGoods)
            .build();
    }
    
    public List<UserGrowthVO> getUserGrowthTrend(LocalDate startDate, LocalDate endDate, 
                                                TimeGranularity granularity) {
        List<UserGrowthVO> result = new ArrayList<>();
        
        if (granularity == TimeGranularity.DAILY) {
            LocalDate current = startDate;
            while (!current.isAfter(endDate)) {
                Long dailyNewUsers = userMapper.countUsersByDate(current);
                Long totalUsers = userMapper.countUsersBeforeDate(current.plusDays(1));
                
                result.add(UserGrowthVO.builder()
                    .date(current)
                    .newUsers(dailyNewUsers)
                    .totalUsers(totalUsers)
                    .build());
                
                current = current.plusDays(1);
            }
        } else if (granularity == TimeGranularity.MONTHLY) {
            YearMonth current = YearMonth.from(startDate);
            YearMonth end = YearMonth.from(endDate);
            
            while (!current.isAfter(end)) {
                LocalDate monthStart = current.atDay(1);
                LocalDate monthEnd = current.atEndOfMonth();
                
                Long monthlyNewUsers = userMapper.countUsersByDateRange(monthStart, monthEnd);
                Long totalUsers = userMapper.countUsersBeforeDate(monthEnd.plusDays(1));
                
                result.add(UserGrowthVO.builder()
                    .date(monthStart)
                    .newUsers(monthlyNewUsers)
                    .totalUsers(totalUsers)
                    .build());
                
                current = current.plusMonths(1);
            }
        }
        
        return result;
    }
    
    public List<SalesAnalysisVO> getSalesAnalysis(LocalDate startDate, LocalDate endDate, 
                                                 AnalysisDimension dimension) {
        switch (dimension) {
            case CATEGORY:
                return orderMapper.getSalesAnalysisByCategory(startDate, endDate);
            case MERCHANT:
                return orderMapper.getSalesAnalysisByMerchant(startDate, endDate);
            case GOODS:
                return orderMapper.getSalesAnalysisByGoods(startDate, endDate);
            case REGION:
                return orderMapper.getSalesAnalysisByRegion(startDate, endDate);
            default:
                throw new BusinessException("不支持的分析维度");
        }
    }
}
```

### 审核流程服务
```java
@Service
public class AdminAuditService {
    
    @Transactional
    public void auditMerchant(Long merchantId, AuditStatus auditStatus, String auditRemark) {
        // 获取商户信息
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new BusinessException("商户不存在");
        }
        
        // 更新审核状态
        merchant.setAuditStatus(auditStatus);
        merchant.setAuditRemark(auditRemark);
        merchant.setAuditTime(LocalDateTime.now());
        
        if (auditStatus == AuditStatus.APPROVED) {
            merchant.setMerchantStatus(MerchantStatus.ACTIVE);
        } else if (auditStatus == AuditStatus.REJECTED) {
            merchant.setMerchantStatus(MerchantStatus.REJECTED);
        }
        
        merchantMapper.updateById(merchant);
        
        // 发送审核结果通知
        sendAuditNotification(merchant, auditStatus, auditRemark);
        
        // 记录审核日志
        recordAuditLog(AuditType.MERCHANT, merchantId, auditStatus, auditRemark);
    }
    
    @Transactional
    public void auditGoods(Long goodsId, AuditStatus auditStatus, String auditRemark) {
        // 获取商品信息
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null) {
            throw new BusinessException("商品不存在");
        }
        
        // 更新审核状态
        goods.setAuditStatus(auditStatus);
        goods.setAuditRemark(auditRemark);
        goods.setAuditTime(LocalDateTime.now());
        
        if (auditStatus == AuditStatus.APPROVED) {
            goods.setGoodsStatus(GoodsStatus.ON_SALE);
        } else if (auditStatus == AuditStatus.REJECTED) {
            goods.setGoodsStatus(GoodsStatus.AUDIT_FAILED);
        }
        
        goodsMapper.updateById(goods);
        
        // 发送审核结果通知
        sendGoodsAuditNotification(goods, auditStatus, auditRemark);
        
        // 记录审核日志
        recordAuditLog(AuditType.GOODS, goodsId, auditStatus, auditRemark);
    }
    
    private void sendAuditNotification(Merchant merchant, AuditStatus auditStatus, String auditRemark) {
        // 构建通知消息
        NotificationMessage message = NotificationMessage.builder()
            .userId(merchant.getUserId())
            .title("商户审核结果通知")
            .content(buildAuditNotificationContent(merchant.getMerchantName(), auditStatus, auditRemark))
            .messageType(MessageType.AUDIT_RESULT)
            .build();
        
        // 发送通知
        notificationService.sendNotification(message);
    }
    
    private void recordAuditLog(AuditType auditType, Long businessId, 
                               AuditStatus auditStatus, String auditRemark) {
        AdminAuditLog auditLog = AdminAuditLog.builder()
            .auditType(auditType)
            .businessId(businessId)
            .auditStatus(auditStatus)
            .auditRemark(auditRemark)
            .auditTime(LocalDateTime.now())
            .build();
        
        adminAuditLogMapper.insert(auditLog);
    }
}
```

### 操作日志服务
```java
@Service
public class AdminOperationLogService {
    
    @Async
    public void recordOperationLog(AdminOperationLog operationLog) {
        try {
            adminOperationLogMapper.insert(operationLog);
        } catch (Exception e) {
            log.error("记录操作日志失败", e);
        }
    }
    
    public PageVO<AdminOperationLogVO> getOperationLogs(AdminOperationLogQueryDTO queryDTO) {
        // 构建查询条件
        LambdaQueryWrapper<AdminOperationLog> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(queryDTO.getUsername())) {
            wrapper.like(AdminOperationLog::getUsername, queryDTO.getUsername());
        }
        
        if (StringUtils.hasText(queryDTO.getOperationType())) {
            wrapper.eq(AdminOperationLog::getOperationType, queryDTO.getOperationType());
        }
        
        if (queryDTO.getStartTime() != null) {
            wrapper.ge(AdminOperationLog::getCreateTime, queryDTO.getStartTime());
        }
        
        if (queryDTO.getEndTime() != null) {
            wrapper.le(AdminOperationLog::getCreateTime, queryDTO.getEndTime());
        }
        
        wrapper.orderByDesc(AdminOperationLog::getCreateTime);
        
        // 分页查询
        Page<AdminOperationLog> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        Page<AdminOperationLog> result = adminOperationLogMapper.selectPage(page, wrapper);
        
        // 转换为 VO
        List<AdminOperationLogVO> logVOs = result.getRecords().stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());
        
        return PageVO.<AdminOperationLogVO>builder()
            .records(logVOs)
            .total(result.getTotal())
            .pageNum(queryDTO.getPageNum())
            .pageSize(queryDTO.getPageSize())
            .build();
    }
}
```

## 前端实现

### 路由配置
```javascript
// router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/layout/index.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页', icon: 'dashboard' }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    meta: { title: '用户管理', icon: 'user' },
    children: [
      {
        path: 'list',
        name: 'UserList',
        component: () => import('@/views/user/list.vue'),
        meta: { title: '用户列表', permission: 'user:list' }
      },
      {
        path: 'detail/:id',
        name: 'UserDetail',
        component: () => import('@/views/user/detail.vue'),
        meta: { title: '用户详情', permission: 'user:detail' },
        hidden: true
      }
    ]
  },
  {
    path: '/merchant',
    component: Layout,
    meta: { title: '商户管理', icon: 'shop' },
    children: [
      {
        path: 'list',
        name: 'MerchantList',
        component: () => import('@/views/merchant/list.vue'),
        meta: { title: '商户列表', permission: 'merchant:list' }
      },
      {
        path: 'audit',
        name: 'MerchantAudit',
        component: () => import('@/views/merchant/audit.vue'),
        meta: { title: '商户审核', permission: 'merchant:audit' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
```

### 状态管理
```javascript
// store/modules/user.js
import { defineStore } from 'pinia'
import { login, logout, getUserInfo } from '@/api/auth'
import { getToken, setToken, removeToken } from '@/utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken(),
    userInfo: null,
    permissions: []
  }),
  
  getters: {
    hasPermission: (state) => (permission) => {
      return state.permissions.includes(permission) || state.permissions.includes('*')
    }
  },
  
  actions: {
    async login(loginForm) {
      try {
        const response = await login(loginForm)
        const { token } = response.data
        
        this.token = token
        setToken(token)
        
        return response
      } catch (error) {
        throw error
      }
    },
    
    async getUserInfo() {
      try {
        const response = await getUserInfo()
        const { userInfo, permissions } = response.data
        
        this.userInfo = userInfo
        this.permissions = permissions
        
        return response
      } catch (error) {
        throw error
      }
    },
    
    async logout() {
      try {
        await logout()
      } finally {
        this.token = null
        this.userInfo = null
        this.permissions = []
        removeToken()
      }
    }
  }
})
```

### 数据可视化组件
```vue
<!-- components/Charts/LineChart.vue -->
<template>
  <div ref="chartRef" :style="{ width: width, height: height }"></div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  data: {
    type: Array,
    required: true
  },
  width: {
    type: String,
    default: '100%'
  },
  height: {
    type: String,
    default: '400px'
  }
})

const chartRef = ref(null)
let chart = null

const initChart = () => {
  chart = echarts.init(chartRef.value)
  
  const option = {
    title: {
      text: '用户增长趋势'
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['新增用户', '累计用户']
    },
    xAxis: {
      type: 'category',
      data: props.data.map(item => item.date)
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '新增用户',
        type: 'line',
        data: props.data.map(item => item.newUsers)
      },
      {
        name: '累计用户',
        type: 'line',
        data: props.data.map(item => item.totalUsers)
      }
    ]
  }
  
  chart.setOption(option)
}

const resizeChart = () => {
  if (chart) {
    chart.resize()
  }
}

watch(() => props.data, () => {
  if (chart) {
    initChart()
  }
}, { deep: true })

onMounted(() => {
  initChart()
  window.addEventListener('resize', resizeChart)
})

onUnmounted(() => {
  if (chart) {
    chart.dispose()
  }
  window.removeEventListener('resize', resizeChart)
})
</script>
```

## 性能优化

### 后端优化
- 数据库查询优化
- Redis 缓存策略
- 分页查询优化
- 异步任务处理

### 前端优化
- 路由懒加载
- 组件按需加载
- 图片懒加载
- 虚拟滚动

### 数据优化
- 统计数据缓存
- 大数据量分页
- 导出功能优化
- 实时数据推送

## 安全考虑

- 🔒 JWT 令牌认证
- 🔒 RBAC 权限控制
- 🔒 操作日志记录
- 🔒 敏感数据脱敏
- 🔒 XSS 和 CSRF 防护

## 故障排查

### 常见问题
1. **登录失败**: 检查用户名密码和账户状态
2. **权限不足**: 检查用户角色和权限配置
3. **数据加载慢**: 检查数据库查询和缓存
4. **统计数据错误**: 检查统计逻辑和数据源

### 日志查看
```bash
# 查看应用日志
tail -f logs/admin-provider.log

# 查看登录日志
grep "LOGIN" logs/admin-provider.log

# 查看操作日志
grep "OPERATION" logs/admin-provider.log

# 查看错误日志
grep "ERROR" logs/admin-provider.log
```

## 开发指南

### 添加新的管理功能
1. 设计数据库表结构
2. 创建实体类和 Mapper
3. 实现 Service 业务逻辑
4. 创建 Controller 接口
5. 添加权限配置
6. 实现前端页面
7. 添加路由和菜单
8. 编写测试用例

### 扩展统计功能
1. 分析统计需求
2. 设计统计指标
3. 实现统计查询
4. 添加缓存策略
5. 创建图表组件
6. 优化查询性能

## 版本信息

- **当前版本**: 1.0.0
- **最低 JDK 版本**: 17
- **Spring Boot 版本**: 3.x
- **Spring Cloud 版本**: 2023.x
- **Vue.js 版本**: 3.x
- **Element Plus 版本**: 2.x

---

**维护团队**: 共享臻选开发团队  
**创建时间**: 2024年  
**最后更新**: 2024年