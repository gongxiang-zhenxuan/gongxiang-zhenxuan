# Claude Code 团队配置

这个目录包含了贡享臻选项目的 Claude Code 配置文件，旨在为团队提供统一的开发体验。

## 📁 配置文件说明

| 文件 | 说明 |
|------|------|
| `config.yml` | 主配置文件，包含项目信息和微服务配置 |
| `mcp.json` | MCP (Model Context Protocol) 服务器配置 |
| `env.yml` | 环境变量配置（开发、测试、生产） |
| `commands.yml` | 自定义命令配置，包含项目特定的操作命令 |
| `security.yml` | 安全配置和 YOLO 模式设置 |
| `tdd.yml` | 测试驱动开发配置 |
| `setup.sh` | 一键配置脚本 |

## 🚀 快速开始

### 1. 首次设置

```bash
# 执行一键配置脚本
chmod +x .claude-code/setup.sh
./.claude-code/setup.sh
```

### 2. 验证配置

```bash
# 检查 Claude Code 版本
claude-code --version

# 查看可用的自定义命令
/commands list

# 测试 Git 集成
/git status
```

### 3. 常用命令

```bash
# 构建所有服务
/build-all

# 启动网关
/start-gateway

# 查看网关日志
/gateway-logs

# 运行测试
/test-all

# 检查代码质量
/code-quality
```

## 🧠 AI 增强工具

我们的 MCP 配置包含了多个强大的 AI 工具：

### 基础工具
- **MySQL**: 数据库操作和查询
- **Git**: 版本控制管理
- **Maven**: 项目构建工具
- **Spring Boot**: 微服务框架集成

### AI 增强工具
- **Sequential Thinking**: 复杂问题的逐步分析和推理
- **Puppeteer**: 浏览器自动化测试和网页操作
- **Context7**: 获取最新的库文档和API文档
- **PromptX**: AI 专业角色能力启动器

## ⚙️ 配置个性化

### 本地环境变量

如果需要个性化的环境变量，创建 `mcp-env.local.sh`：

```bash
#!/bin/bash
# 个人环境变量配置（不会被提交到 Git）

export MYSQL_PASSWORD="your_actual_password"
export REDIS_PASSWORD="your_redis_password"

# Puppeteer 配置（如果 Chrome 路径不同）
export PUPPETEER_EXECUTABLE_PATH="/your/chrome/path"

# 加载基础配置
source .claude-code/mcp-env.sh
```

### 个人配置文件

如果需要个人特定的配置，可以创建：
- `personal-config.yml` - 个人配置
- `user-preferences.yml` - 用户偏好设置

这些文件会被 `.gitignore` 忽略，不会影响团队配置。

## 🔧 故障排除

### 常见问题

1. **Claude Code 命令不识别**
   ```bash
   # 重新安装 Claude Code
   brew reinstall anthropic/tap/claude-code
   ```

2. **MCP 连接失败**
   ```bash
   # 检查 Node.js 和 npm
   node --version
   npm --version
   
   # 重新安装基础 MCP 包
   npm install -g @anthropic/mcp-mysql @anthropic/mcp-git @anthropic/mcp-filesystem @anthropic/mcp-maven @anthropic/mcp-spring-boot
   
   # 重新安装高级 MCP 包
   npm install -g @anthropic/mcp-sequential-thinking @anthropic/mcp-puppeteer @anthropic/mcp-context7 @anthropic/mcp-promptx
   ```

3. **环境变量问题**
   ```bash
   # 重新加载环境变量
   source .claude-code/mcp-env.sh
   ```

### 获取帮助

- 查看主配置指南：`../Claude-Code-最佳实践指南.md`
- 联系团队技术负责人
- 查看 Claude Code 官方文档

## 📝 团队约定

### 配置修改流程

1. 在开发分支修改配置文件
2. 测试配置是否正常工作
3. 提交 PR 并请求代码审查
4. 合并后通知团队成员更新配置

### 敏感信息处理

- ❌ 不要在配置文件中硬编码密码
- ✅ 使用环境变量占位符
- ✅ 敏感信息放在本地环境变量中

### 版本控制

- ✅ 提交团队共享配置文件
- ❌ 不提交个人配置和日志文件
- ✅ 定期更新 `.gitignore`

## 🤖 自动化配置管理

### 配置自动同步
项目结构或配置发生变化时，Claude Code 配置可以自动更新：

```bash
# 自动检测并同步配置
./.claude-code/scripts/auto-sync-config.sh

# 持续监控模式
./.claude-code/scripts/config-monitor.sh --continuous 60

# 安装 Git hooks 自动触发
./.claude-code/scripts/install-git-hooks.sh --install
```

### 监控的变化
- ✅ 新增/删除微服务目录
- ✅ 端口配置变更
- ✅ 依赖文件变化（pom.xml）
- ✅ 应用配置文件变化

详细说明请查看：`CONFIG_MANAGEMENT.md`

## 🔄 更新日志

### v1.1.0 (2024-01-XX)
- 添加自动化配置管理系统
- 支持项目结构变化自动同步
- 集成 Git hooks 自动触发机制
- 添加配置监控和报告功能

### v1.0.0 (2024-01-XX)
- 初始团队配置版本
- 包含基础微服务配置
- 添加常用自定义命令
- 配置 MCP 集成

---

如有问题或建议，请联系团队技术负责人。
