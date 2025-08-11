# GXZ Settlement - 共享臻选结算清分服务

## 概述

`gxz-settlement` 是共享臻选微服务架构中的结算清分服务，负责处理平台资金结算、商户分账、佣金计算、提现管理、对账服务、财务报表等核心结算相关功能。

## 主要功能

### 💰 资金结算
- **订单结算**: 订单完成后的资金结算处理
- **分账管理**: 平台、商户、配送员等多方分账
- **佣金计算**: 基于规则的佣金自动计算
- **费用扣除**: 平台服务费、技术服务费扣除

### 🏦 提现管理
- **提现申请**: 商户和配送员提现申请
- **提现审核**: 提现申请审核流程
- **批量打款**: 银行批量转账处理
- **提现记录**: 提现历史和状态跟踪

### 📊 对账服务
- **日对账**: 每日资金流水对账
- **月对账**: 月度结算对账报告
- **异常处理**: 对账差异处理和调账
- **报表生成**: 财务报表自动生成

### 🔍 风控管理
- **异常监控**: 异常交易和资金流向监控
- **风险评估**: 商户和用户风险评估
- **冻结管理**: 可疑账户资金冻结
- **合规检查**: 反洗钱和合规性检查

## 技术栈

- **Java**: 8
- **Spring Boot**: 2.7.x
- **Spring Cloud**: 2021.x
- **MyBatis Plus**: ORM 框架
- **MySQL**: 主数据库
- **Redis**: 缓存和分布式锁
- **RocketMQ**: 消息队列
- **XXL-Job**: 定时任务
- **Redisson**: 分布式锁
- **Apache POI**: Excel报表生成
- **Nacos**: 服务注册发现

## 模块结构

```
gxz-settlement/
├── settlement-api/              # 结算服务 API 模块
│   ├── src/main/java/
│   │   └── com/gongxiang/zhenxuan/settlement/api/
│   │       ├── dto/            # 数据传输对象
│   │       ├── vo/             # 视图对象
│   │       ├── enums/          # 枚举定义
│   │       └── feign/          # Feign 客户端接口
│   └── pom.xml
├── settlement-provider/         # 结算服务提供者模块
│   ├── src/main/java/
│   │   └── com/gongxiang/zhenxuan/settlement/
│   │       ├── controller/     # 控制器层
│   │       ├── service/        # 服务层
│   │       ├── mapper/         # 数据访问层
│   │       ├── entity/         # 实体类
│   │       ├── config/         # 配置类
│   │       ├── calculator/     # 计算引擎
│   │       ├── reconcile/      # 对账服务
│   │       ├── report/         # 报表生成
│   │       ├── job/            # 定时任务
│   │       └── SettlementApplication.java
│   ├── src/main/resources/
│   │   ├── application.yml     # 配置文件
│   │   └── mapper/            # MyBatis 映射文件
│   └── pom.xml
└── pom.xml
```

## 核心功能模块

### 订单结算
```java
// 创建结算单
SettlementCreateDTO settlementDTO = new SettlementCreateDTO();
settlementDTO.setOrderId(1L);
settlementDTO.setMerchantId(1L);
settlementDTO.setOrderAmount(new BigDecimal("100.00"));
settlementDTO.setPlatformFeeRate(new BigDecimal("0.05"));
settlementDTO.setDeliveryFee(new BigDecimal("8.00"));
Result<SettlementVO> result = settlementService.createSettlement(settlementDTO);

// 执行结算
SettlementExecuteDTO executeDTO = new SettlementExecuteDTO();
executeDTO.setSettlementId(1L);
executeDTO.setExecuteTime(LocalDateTime.now());
Result<Void> executeResult = settlementService.executeSettlement(executeDTO);
```

### 提现管理
```java
// 申请提现
WithdrawApplyDTO withdrawDTO = new WithdrawApplyDTO();
withdrawDTO.setMerchantId(1L);
withdrawDTO.setAmount(new BigDecimal("1000.00"));
withdrawDTO.setBankAccount("6222021234567890");
withdrawDTO.setBankName("中国工商银行");
withdrawDTO.setAccountName("张三");
Result<WithdrawVO> withdrawResult = withdrawService.applyWithdraw(withdrawDTO);

// 审核提现
WithdrawAuditDTO auditDTO = new WithdrawAuditDTO();
auditDTO.setWithdrawId(1L);
auditDTO.setAuditStatus(AuditStatus.APPROVED);
auditDTO.setAuditRemark("审核通过");
Result<Void> auditResult = withdrawService.auditWithdraw(auditDTO);
```

### 对账服务
```java
// 执行日对账
DailyReconcileDTO reconcileDTO = new DailyReconcileDTO();
reconcileDTO.setReconcileDate(LocalDate.now().minusDays(1));
reconcileDTO.setReconcileType(ReconcileType.DAILY);
Result<ReconcileResultVO> reconcileResult = reconcileService.executeDailyReconcile(reconcileDTO);

// 生成对账报告
ReconcileReportDTO reportDTO = new ReconcileReportDTO();
reportDTO.setStartDate(LocalDate.now().minusDays(30));
reportDTO.setEndDate(LocalDate.now());
reportDTO.setReportType(ReportType.MONTHLY);
Result<String> reportResult = reconcileService.generateReconcileReport(reportDTO);
```

## API 接口

### 结算管理接口
- `POST /api/settlement/create` - 创建结算单
- `GET /api/settlement/{id}` - 获取结算详情
- `POST /api/settlement/{id}/execute` - 执行结算
- `POST /api/settlement/{id}/cancel` - 取消结算
- `GET /api/settlement/list` - 获取结算列表
- `GET /api/settlement/merchant/{merchantId}` - 获取商户结算记录

### 提现管理接口
- `POST /api/withdraw/apply` - 申请提现
- `GET /api/withdraw/{id}` - 获取提现详情
- `PUT /api/withdraw/{id}/audit` - 审核提现
- `POST /api/withdraw/batch/transfer` - 批量转账
- `GET /api/withdraw/list` - 获取提现列表
- `GET /api/withdraw/merchant/{merchantId}` - 获取商户提现记录

### 对账服务接口
- `POST /api/reconcile/daily` - 执行日对账
- `POST /api/reconcile/monthly` - 执行月对账
- `GET /api/reconcile/{id}` - 获取对账结果
- `POST /api/reconcile/report/generate` - 生成对账报告
- `GET /api/reconcile/report/{id}` - 下载对账报告
- `GET /api/reconcile/exception/list` - 获取对账异常列表

### 财务报表接口
- `GET /api/report/revenue/daily` - 日收入报表
- `GET /api/report/revenue/monthly` - 月收入报表
- `GET /api/report/merchant/settlement` - 商户结算报表
- `GET /api/report/platform/commission` - 平台佣金报表
- `POST /api/report/export` - 导出财务报表

## 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gxz_settlement?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
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
    database: 8
```

### 结算配置
```yaml
settlement:
  platform:
    commission-rate: 0.05  # 平台佣金比例
    service-fee-rate: 0.02  # 技术服务费比例
    min-settlement-amount: 0.01  # 最小结算金额
  withdraw:
    min-amount: 10.00  # 最小提现金额
    max-amount: 50000.00  # 最大提现金额
    fee-rate: 0.001  # 提现手续费比例
    daily-limit: 100000.00  # 日提现限额
  reconcile:
    auto-reconcile: true  # 自动对账
    reconcile-time: "02:00:00"  # 对账时间
    tolerance-amount: 0.01  # 容差金额
```

## 启动方式

### 开发环境
```bash
# 启动依赖服务
docker-compose up -d mysql redis rocketmq

# 启动结算服务
cd settlement-provider
mvn spring-boot:run
```

### 生产环境
```bash
# 构建项目
mvn clean package -DskipTests

# 启动服务
java -jar settlement-provider/target/settlement-provider-1.0.0.jar
```

## 监控端点

- **健康检查**: `GET /actuator/health`
- **服务信息**: `GET /actuator/info`
- **指标监控**: `GET /actuator/metrics`
- **日志级别**: `GET /actuator/loggers`

## 数据库设计

### 结算单表 (t_settlement)
```sql
CREATE TABLE t_settlement (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    settlement_code VARCHAR(32) UNIQUE NOT NULL,
    order_id BIGINT NOT NULL,
    merchant_id BIGINT NOT NULL,
    order_amount DECIMAL(12,2) NOT NULL,
    platform_fee DECIMAL(12,2) NOT NULL,
    service_fee DECIMAL(12,2) NOT NULL,
    delivery_fee DECIMAL(12,2),
    promotion_discount DECIMAL(12,2),
    merchant_amount DECIMAL(12,2) NOT NULL,
    deliveryman_amount DECIMAL(12,2),
    platform_amount DECIMAL(12,2) NOT NULL,
    settlement_status TINYINT DEFAULT 0,
    settlement_time DATETIME,
    remark VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 提现记录表 (t_withdraw_record)
```sql
CREATE TABLE t_withdraw_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    withdraw_code VARCHAR(32) UNIQUE NOT NULL,
    merchant_id BIGINT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    fee DECIMAL(12,2) DEFAULT 0,
    actual_amount DECIMAL(12,2) NOT NULL,
    bank_account VARCHAR(50) NOT NULL,
    bank_name VARCHAR(100) NOT NULL,
    account_name VARCHAR(50) NOT NULL,
    withdraw_status TINYINT DEFAULT 0,
    audit_status TINYINT DEFAULT 0,
    audit_time DATETIME,
    audit_remark VARCHAR(500),
    transfer_time DATETIME,
    transfer_serial VARCHAR(100),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 对账记录表 (t_reconcile_record)
```sql
CREATE TABLE t_reconcile_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    reconcile_code VARCHAR(32) UNIQUE NOT NULL,
    reconcile_date DATE NOT NULL,
    reconcile_type TINYINT NOT NULL,
    total_order_count INT DEFAULT 0,
    total_order_amount DECIMAL(15,2) DEFAULT 0,
    total_settlement_amount DECIMAL(15,2) DEFAULT 0,
    total_platform_amount DECIMAL(15,2) DEFAULT 0,
    total_merchant_amount DECIMAL(15,2) DEFAULT 0,
    exception_count INT DEFAULT 0,
    exception_amount DECIMAL(15,2) DEFAULT 0,
    reconcile_status TINYINT DEFAULT 0,
    start_time DATETIME,
    end_time DATETIME,
    report_url VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 账户余额表 (t_account_balance)
```sql
CREATE TABLE t_account_balance (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_id BIGINT NOT NULL,
    account_type TINYINT NOT NULL,
    available_balance DECIMAL(15,2) DEFAULT 0,
    frozen_balance DECIMAL(15,2) DEFAULT 0,
    total_balance DECIMAL(15,2) DEFAULT 0,
    total_income DECIMAL(15,2) DEFAULT 0,
    total_withdraw DECIMAL(15,2) DEFAULT 0,
    version INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_account (account_id, account_type)
);
```

### 资金流水表 (t_fund_flow)
```sql
CREATE TABLE t_fund_flow (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    flow_code VARCHAR(32) UNIQUE NOT NULL,
    account_id BIGINT NOT NULL,
    account_type TINYINT NOT NULL,
    flow_type TINYINT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    balance_before DECIMAL(15,2) NOT NULL,
    balance_after DECIMAL(15,2) NOT NULL,
    business_type TINYINT NOT NULL,
    business_id BIGINT,
    business_code VARCHAR(32),
    description VARCHAR(200),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

## 业务流程

### 订单结算流程
1. **订单完成**: 订单状态变更为已完成
2. **创建结算单**: 系统自动创建结算单
3. **费用计算**: 计算各方应得金额
4. **资金分账**: 执行资金分账操作
5. **更新余额**: 更新各账户余额
6. **结算完成**: 标记结算单完成

### 提现处理流程
1. **提现申请**: 商户提交提现申请
2. **风控检查**: 系统风控检查
3. **人工审核**: 财务人员审核
4. **批量打款**: 银行批量转账
5. **状态更新**: 更新提现状态
6. **通知商户**: 发送提现结果通知

### 对账处理流程
1. **数据收集**: 收集交易和结算数据
2. **数据比对**: 比对各系统数据
3. **差异识别**: 识别对账差异
4. **异常处理**: 处理对账异常
5. **报告生成**: 生成对账报告
6. **结果确认**: 确认对账结果

## 结算计算引擎

### 佣金计算器
```java
@Component
public class CommissionCalculator {
    
    public SettlementResult calculateSettlement(SettlementRequest request) {
        BigDecimal orderAmount = request.getOrderAmount();
        BigDecimal platformFeeRate = request.getPlatformFeeRate();
        BigDecimal serviceFeeRate = request.getServiceFeeRate();
        
        // 计算平台佣金
        BigDecimal platformFee = orderAmount.multiply(platformFeeRate)
            .setScale(2, RoundingMode.HALF_UP);
        
        // 计算技术服务费
        BigDecimal serviceFee = orderAmount.multiply(serviceFeeRate)
            .setScale(2, RoundingMode.HALF_UP);
        
        // 计算商户应得金额
        BigDecimal merchantAmount = orderAmount
            .subtract(platformFee)
            .subtract(serviceFee)
            .subtract(request.getDeliveryFee())
            .add(request.getPromotionDiscount());
        
        return SettlementResult.builder()
            .orderAmount(orderAmount)
            .platformFee(platformFee)
            .serviceFee(serviceFee)
            .merchantAmount(merchantAmount)
            .deliverymanAmount(request.getDeliveryFee())
            .platformAmount(platformFee.add(serviceFee))
            .build();
    }
}
```

### 分账处理器
```java
@Service
public class AccountSplitService {
    
    @Transactional
    public void executeAccountSplit(SettlementVO settlement) {
        // 扣减平台待结算金额
        accountService.decreaseBalance(
            PLATFORM_ACCOUNT_ID, 
            AccountType.PLATFORM, 
            settlement.getOrderAmount()
        );
        
        // 增加商户余额
        accountService.increaseBalance(
            settlement.getMerchantId(), 
            AccountType.MERCHANT, 
            settlement.getMerchantAmount()
        );
        
        // 增加配送员余额
        if (settlement.getDeliverymanAmount().compareTo(BigDecimal.ZERO) > 0) {
            accountService.increaseBalance(
                settlement.getDeliverymanId(), 
                AccountType.DELIVERYMAN, 
                settlement.getDeliverymanAmount()
            );
        }
        
        // 增加平台收入
        accountService.increaseBalance(
            PLATFORM_ACCOUNT_ID, 
            AccountType.PLATFORM, 
            settlement.getPlatformAmount()
        );
        
        // 记录资金流水
        recordFundFlow(settlement);
    }
}
```

## 对账服务实现

### 日对账服务
```java
@Service
public class DailyReconcileService {
    
    @Scheduled(cron = "0 0 2 * * ?")
    public void executeDailyReconcile() {
        LocalDate reconcileDate = LocalDate.now().minusDays(1);
        
        try {
            // 获取订单数据
            List<OrderSettlementData> orderData = getOrderData(reconcileDate);
            
            // 获取结算数据
            List<SettlementData> settlementData = getSettlementData(reconcileDate);
            
            // 获取资金流水数据
            List<FundFlowData> fundFlowData = getFundFlowData(reconcileDate);
            
            // 执行对账
            ReconcileResult result = reconcile(orderData, settlementData, fundFlowData);
            
            // 保存对账结果
            saveReconcileResult(reconcileDate, result);
            
            // 处理对账异常
            if (result.hasException()) {
                handleReconcileException(result.getExceptions());
            }
            
            // 生成对账报告
            generateReconcileReport(reconcileDate, result);
            
        } catch (Exception e) {
            log.error("日对账执行失败: {}", reconcileDate, e);
            // 发送告警通知
            alertService.sendReconcileFailAlert(reconcileDate, e.getMessage());
        }
    }
    
    private ReconcileResult reconcile(List<OrderSettlementData> orderData,
                                     List<SettlementData> settlementData,
                                     List<FundFlowData> fundFlowData) {
        ReconcileResult result = new ReconcileResult();
        
        // 订单金额汇总
        BigDecimal totalOrderAmount = orderData.stream()
            .map(OrderSettlementData::getOrderAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 结算金额汇总
        BigDecimal totalSettlementAmount = settlementData.stream()
            .map(SettlementData::getSettlementAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 资金流水汇总
        BigDecimal totalFundFlowAmount = fundFlowData.stream()
            .map(FundFlowData::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 检查金额是否一致
        if (totalOrderAmount.compareTo(totalSettlementAmount) != 0) {
            result.addException(new ReconcileException(
                "订单金额与结算金额不一致", 
                totalOrderAmount, 
                totalSettlementAmount
            ));
        }
        
        if (totalSettlementAmount.compareTo(totalFundFlowAmount) != 0) {
            result.addException(new ReconcileException(
                "结算金额与资金流水不一致", 
                totalSettlementAmount, 
                totalFundFlowAmount
            ));
        }
        
        result.setTotalOrderAmount(totalOrderAmount);
        result.setTotalSettlementAmount(totalSettlementAmount);
        result.setTotalFundFlowAmount(totalFundFlowAmount);
        
        return result;
    }
}
```

## 财务报表生成

### 报表生成服务
```java
@Service
public class FinancialReportService {
    
    public String generateMonthlyReport(LocalDate month) {
        try {
            // 创建工作簿
            Workbook workbook = new XSSFWorkbook();
            
            // 创建收入汇总表
            createRevenueSummarySheet(workbook, month);
            
            // 创建商户结算表
            createMerchantSettlementSheet(workbook, month);
            
            // 创建平台佣金表
            createPlatformCommissionSheet(workbook, month);
            
            // 创建提现统计表
            createWithdrawStatisticsSheet(workbook, month);
            
            // 保存文件
            String fileName = String.format("财务报表_%s.xlsx", 
                month.format(DateTimeFormatter.ofPattern("yyyy-MM")));
            String filePath = saveWorkbook(workbook, fileName);
            
            workbook.close();
            
            return filePath;
        } catch (Exception e) {
            log.error("生成月度财务报表失败: {}", month, e);
            throw new BusinessException("报表生成失败");
        }
    }
    
    private void createRevenueSummarySheet(Workbook workbook, LocalDate month) {
        Sheet sheet = workbook.createSheet("收入汇总");
        
        // 创建标题行
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("日期");
        headerRow.createCell(1).setCellValue("订单数量");
        headerRow.createCell(2).setCellValue("订单金额");
        headerRow.createCell(3).setCellValue("平台佣金");
        headerRow.createCell(4).setCellValue("技术服务费");
        headerRow.createCell(5).setCellValue("商户结算金额");
        
        // 获取数据并填充
        List<DailyRevenueData> revenueData = getMonthlyRevenueData(month);
        for (int i = 0; i < revenueData.size(); i++) {
            Row dataRow = sheet.createRow(i + 1);
            DailyRevenueData data = revenueData.get(i);
            
            dataRow.createCell(0).setCellValue(data.getDate().toString());
            dataRow.createCell(1).setCellValue(data.getOrderCount());
            dataRow.createCell(2).setCellValue(data.getOrderAmount().doubleValue());
            dataRow.createCell(3).setCellValue(data.getPlatformCommission().doubleValue());
            dataRow.createCell(4).setCellValue(data.getServiceFee().doubleValue());
            dataRow.createCell(5).setCellValue(data.getMerchantAmount().doubleValue());
        }
        
        // 自动调整列宽
        for (int i = 0; i < 6; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
```

## 风控管理

### 风险监控服务
```java
@Service
public class RiskMonitorService {
    
    @EventListener
    public void handleSettlementEvent(SettlementEvent event) {
        SettlementVO settlement = event.getSettlement();
        
        // 检查异常金额
        if (isAbnormalAmount(settlement)) {
            createRiskAlert(RiskType.ABNORMAL_AMOUNT, settlement);
        }
        
        // 检查频繁操作
        if (isFrequentOperation(settlement.getMerchantId())) {
            createRiskAlert(RiskType.FREQUENT_OPERATION, settlement);
        }
        
        // 检查可疑账户
        if (isSuspiciousAccount(settlement.getMerchantId())) {
            freezeAccount(settlement.getMerchantId());
            createRiskAlert(RiskType.SUSPICIOUS_ACCOUNT, settlement);
        }
    }
    
    private boolean isAbnormalAmount(SettlementVO settlement) {
        // 检查是否超过正常金额范围
        BigDecimal threshold = new BigDecimal("10000.00");
        return settlement.getOrderAmount().compareTo(threshold) > 0;
    }
    
    private boolean isFrequentOperation(Long merchantId) {
        // 检查是否频繁操作
        String key = "settlement:count:" + merchantId;
        String count = redisTemplate.opsForValue().get(key);
        
        if (count == null) {
            redisTemplate.opsForValue().set(key, "1", Duration.ofHours(1));
            return false;
        }
        
        int currentCount = Integer.parseInt(count);
        if (currentCount >= 100) {
            return true;
        }
        
        redisTemplate.opsForValue().increment(key);
        return false;
    }
}
```

## 性能优化

### 结算优化
- 批量结算处理
- 异步结算执行
- 结算结果缓存
- 数据库连接池优化

### 对账优化
- 分片对账处理
- 并行数据比对
- 增量对账策略
- 历史数据归档

### 报表优化
- 报表数据预计算
- 报表结果缓存
- 异步报表生成
- 分页数据查询

## 安全考虑

- 🔒 资金操作权限控制
- 🔒 敏感数据加密存储
- 🔒 操作日志完整记录
- 🔒 异常交易实时监控
- 🔒 数据备份和恢复

## 故障排查

### 常见问题
1. **结算失败**: 检查账户余额和业务规则
2. **对账异常**: 检查数据一致性和时间范围
3. **提现延迟**: 检查审核流程和银行接口
4. **报表错误**: 检查数据源和计算逻辑

### 日志查看
```bash
# 查看应用日志
tail -f logs/settlement-provider.log

# 查看结算日志
grep "SETTLEMENT" logs/settlement-provider.log

# 查看对账日志
grep "RECONCILE" logs/settlement-provider.log

# 查看提现日志
grep "WITHDRAW" logs/settlement-provider.log

# 查看错误日志
grep "ERROR" logs/settlement-provider.log
```

## 开发指南

### 添加新的结算规则
1. 定义结算规则接口
2. 实现具体规则逻辑
3. 配置规则优先级
4. 添加规则测试用例
5. 更新文档说明

### 扩展对账功能
1. 识别对账需求
2. 设计对账算法
3. 实现对账逻辑
4. 添加异常处理
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