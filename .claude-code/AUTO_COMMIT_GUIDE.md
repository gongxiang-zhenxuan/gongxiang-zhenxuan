# Claude Code 自动提交系统使用指南

## 🎯 概述

自动提交系统让您在完成需求后能够自动生成合适的提交信息并提交代码，大大提升开发效率。

## 🚀 快速开始

### 1. 最简单的使用方式

```bash
# 需求完成后，一键提交
/commit-done "完成用户登录功能"

# 修复bug后，一键提交
/commit-fix "修复支付金额计算错误"

# 新功能完成后，一键提交
/commit-feat "添加订单状态推送通知"
```

### 2. 智能自动提交

```bash
# 让系统自动分析变更并生成提交信息
/auto-commit

# 或者提供简单描述，系统会智能扩展
/auto-commit "用户模块优化"
```

### 3. 开发工作流

```bash
# 开始新功能开发
/start-feature "订单管理系统"

# 查看项目状态
/project-status

# 团队同步
/team-sync
```

## 🎨 提交类型自动识别

系统会根据您的文件变更自动识别提交类型：

| 文件变化 | 自动识别类型 | 生成的提交前缀 |
|---------|-------------|---------------|
| 新增 `.java` 文件 | feat | `feat(service): 新增功能` |
| 修改 `.java` 文件 | fix | `fix(service): 修复问题` |
| 测试文件变化 | test | `test(service): 测试相关` |
| `.md` 文件变化 | docs | `docs: 文档更新` |
| `pom.xml` 变化 | config | `config: 配置更新` |

## 🏷️ 需求完成标记

在代码中使用特殊标记来自动触发提交：

### 标记格式

```java
// 开发时
// TODO: 实现用户登录接口

// 完成时
// DONE: 实现用户登录接口
```

### 支持的标记模式

```java
// 基础标记
TODO -> DONE
FIXME -> FIXED

// 注解式标记
@TODO 添加支付验证 -> @DONE 添加支付验证

// 详细标记
/**
 * TODO: 实现订单状态同步
 * 1. 连接支付回调接口
 * 2. 更新订单状态
 * 3. 发送状态通知
 */

/**
 * DONE: 实现订单状态同步
 * ✅ 连接支付回调接口
 * ✅ 更新订单状态
 * ✅ 发送状态通知
 */
```

## 🔄 开发工作流

### 完整的功能开发流程

```bash
# 1. 开始功能开发
/start-feature "用户个人中心"
# ✅ 自动创建 feature/用户个人中心 分支
# ✅ 启动需求完成监控

# 2. 开发过程中...
# 在代码中添加 TODO 标记
// TODO: 实现个人信息展示
// TODO: 实现头像上传功能

# 3. 完成功能时，将 TODO 改为 DONE
// DONE: 实现个人信息展示
// DONE: 实现头像上传功能

# 4. 系统自动检测并提示提交
# 🎯 检测到需求完成标记，是否自动提交? (y/N): y

# 5. 完成开发后
/commit-done "用户个人中心功能"
# ✅ 自动生成详细提交信息
# ✅ 执行提交前检查
# ✅ 提交代码
```

## 📋 自动生成的提交信息示例

### 新增功能提交

```
feat(user): 实现用户个人中心功能

新增文件:
- gxz-user/src/main/java/com/gongxiang/zhenxuan/user/controller/ProfileController.java
- gxz-user/src/main/java/com/gongxiang/zhenxuan/user/service/ProfileService.java
- gxz-user/src/main/resources/templates/profile.html

修改文件:
- gxz-user/src/main/java/com/gongxiang/zhenxuan/user/entity/User.java
- gxz-user/src/main/resources/application.yml

变更统计:
 5 files changed, 234 insertions(+), 12 deletions(-)
```

### 修复问题提交

```
fix(payment): 修复支付金额计算精度问题

修改文件:
- gxz-payment/src/main/java/com/gongxiang/zhenxuan/payment/service/PaymentCalculator.java
- gxz-payment/src/test/java/com/gongxiang/zhenxuan/payment/PaymentCalculatorTest.java

问题描述:
- 修复浮点数计算精度丢失
- 使用 BigDecimal 进行金额计算
- 添加相关测试用例

变更统计:
 2 files changed, 45 insertions(+), 18 deletions(-)
```

## ⚙️ 配置选项

### 自动提交配置文件

编辑 `.claude-code/auto-commit-config.yml` 来自定义行为：

```yaml
auto_commit:
  # 提交模式
  mode: "interactive"  # interactive, silent, monitor
  
  # 自动推送设置
  auto_push:
    enabled: false
    branches: ["develop", "feature/*"]
    
  # 监控设置
  completion_detection:
    enabled: true
    monitor_interval: 30  # 秒
```

### 个性化设置

```bash
# 设置静默提交模式
./.claude-code/scripts/auto-commit.sh --silent "功能完成"

# 提交后自动推送
./.claude-code/scripts/auto-commit.sh --commit --push "发布功能"

# 只执行检查，不提交
./.claude-code/scripts/auto-commit.sh --check
```

## 🛡️ 安全检查

每次提交前系统会自动执行：

### ✅ 代码检查
- Java 代码编译验证
- Claude Code 配置验证
- 大文件检查（>10MB）

### ✅ 内容检查
- 敏感信息检测
- 提交信息格式验证
- 文件完整性检查

### ✅ 权限检查
- 分支保护检查
- 提交权限验证
- 团队规范符合性

## 🎯 高级功能

### 1. 批量提交

```bash
# 监控模式：自动检测多个完成标记
/monitor-completion 60  # 每60秒检查一次

# 后台运行监控
nohup ./.claude-code/scripts/auto-commit.sh --monitor 30 &
```

### 2. 发布工作流

```bash
# 版本发布
./.claude-code/scripts/commit-workflow.sh --release "v1.2.0"
# ✅ 自动创建发布分支
# ✅ 更新版本号
# ✅ 创建发布标签
# ✅ 推送到远程仓库
```

### 3. 团队协作

```bash
# 团队同步工作流
/team-sync
# ✅ 自动拉取最新代码
# ✅ 同步 Claude Code 配置
# ✅ 解决配置冲突
# ✅ 恢复工作状态
```

## 📊 监控和统计

### 查看提交历史

```bash
# 查看自动提交日志
cat .claude-code/logs/auto-commit.log

# 查看监控状态
tail -f .claude-code/logs/monitor-*.log
```

### 提交统计

```bash
# 查看项目状态
/project-status

# 输出示例：
# 📊 当前项目状态
# Git 状态: 3 个文件待提交
# 当前分支: feature/user-profile
# 最近提交: feat(user): 添加个人信息接口
# 待推送提交: 2 个
```

## 🚨 故障排除

### 常见问题

**问题 1**: 自动提交失败
```bash
# 检查权限
chmod +x .claude-code/scripts/*.sh

# 检查配置
./.claude-code/scripts/validate-config.sh
```

**问题 2**: 监控进程异常
```bash
# 停止监控
./.claude-code/scripts/commit-workflow.sh --stop-monitor

# 重新启动
/monitor-completion 30
```

**问题 3**: 提交信息格式问题
```bash
# 重新配置提交规范
vim .claude-code/auto-commit-config.yml
```

## 💡 最佳实践

### 开发习惯

1. **使用标记**: 在开发时添加 TODO 标记，完成时改为 DONE
2. **及时提交**: 完成小功能就提交，保持提交颗粒度合适
3. **描述清楚**: 提供有意义的功能描述
4. **测试先行**: 确保代码编译和基本测试通过

### 团队协作

1. **统一规范**: 团队使用相同的标记格式
2. **定期同步**: 使用 `/team-sync` 保持代码同步
3. **分支管理**: 使用功能分支进行开发
4. **代码审查**: 重要功能提交后进行代码审查

### 提交质量

1. **原子提交**: 每次提交只包含一个逻辑功能
2. **清晰描述**: 提交信息清楚说明变更内容
3. **测试验证**: 提交前确保功能正常
4. **文档同步**: 更新相关文档和注释

---

🎉 **现在您可以享受智能化的代码提交体验了！** 系统会自动处理繁琐的提交信息生成和格式化工作，让您专注于代码开发。
