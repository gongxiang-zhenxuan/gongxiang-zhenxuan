# 贡享臻选团队 Claude Code 使用指南

## 🎯 配置概览

我们已经为贡享臻选项目创建了完整的 Claude Code 团队配置，所有配置都位于项目根目录的 `.claude-code/` 文件夹中。

### 📁 配置文件结构

```
.claude-code/
├── 📋 配置文件
│   ├── config.yml              # 主配置文件
│   ├── mcp.json               # MCP 服务器配置
│   ├── env.yml                # 环境变量配置
│   ├── commands.yml           # 自定义命令配置
│   ├── security.yml           # 安全配置
│   ├── tdd.yml                # TDD 配置
│   └── yolo-monitor.yml       # YOLO 监控配置
├── 🛠️ 脚本工具
│   ├── setup.sh               # 一键配置脚本
│   ├── mcp-env.sh            # 环境变量加载脚本
│   └── scripts/
│       ├── validate-config.sh # 配置验证脚本
│       ├── team-sync.sh       # 团队同步脚本
│       └── quick-start.sh     # 快速启动脚本
├── 📝 代码模板
│   └── templates/
│       ├── controller-template.java
│       ├── service-template.java
│       └── test-template.java
├── 📊 日志目录
│   └── logs/                  # Claude Code 日志
└── 📚 文档
    ├── README.md              # 配置说明
    └── TEAM_GUIDE.md          # 团队使用指南
```

## 🚀 快速开始

### 1. 首次使用（项目负责人）

```bash
# 执行一键配置脚本
./.claude-code/setup.sh

# 提交配置到 Git
git add .claude-code/
git commit -m "feat: 添加 Claude Code 团队配置"
git push origin main
```

### 2. 团队成员加入

```bash
# 拉取最新代码
git pull origin main

# 运行快速启动脚本
./.claude-code/scripts/quick-start.sh

# 或者手动设置
source .claude-code/mcp-env.sh
```

### 3. 日常使用

```bash
# 查看可用命令
/commands list

# 常用开发命令
/build-all          # 构建所有服务
/start-gateway      # 启动网关
/test-all           # 运行测试
/code-quality       # 代码质量检查
```

## 💼 团队协作工作流

### 配置更新流程

#### 🤖 自动化配置管理（推荐）

我们的配置现在支持自动同步！当项目结构发生变化时：

1. **自动检测**：系统自动检测项目变化
2. **自动同步**：配置文件自动更新
3. **自动验证**：验证配置正确性
4. **自动通知**：生成变更报告

```bash
# 一键启用自动化
./.claude-code/scripts/install-git-hooks.sh --install
./.claude-code/scripts/config-monitor.sh --setup-auto
```

#### 📋 手动配置管理

1. **修改配置**：在开发分支修改 `.claude-code/` 中的配置文件
2. **验证配置**：运行 `./.claude-code/scripts/validate-config.sh`
3. **提交审查**：创建 PR 并请求代码审查
4. **团队通知**：合并后通知团队成员运行同步脚本
5. **同步配置**：团队成员运行 `./.claude-code/scripts/team-sync.sh`

### 角色权限

- **项目负责人**：可以修改所有配置文件
- **高级开发者**：可以修改命令和模板配置
- **开发者**：建议通过 PR 方式提出配置修改

## 🔧 自定义命令速查

### 服务管理
- `/start-gateway` - 启动网关服务
- `/stop-gateway` - 停止网关服务
- `/gateway-logs` - 查看网关日志
- `/build-service --service gxz-user` - 构建指定服务

### 构建与测试
- `/build-all` - 构建所有服务
- `/test-all` - 运行所有测试
- `/code-quality` - 代码质量检查
- `/clean-all` - 清理构建产物

### 开发辅助
- `/generate-crud` - 生成 CRUD 代码
- `/git-status-all` - 查看所有服务状态
- `/switch-env` - 切换环境
- `/find-service-by-port` - 根据端口查找服务

## 🛡️ 安全最佳实践

### 敏感信息管理

❌ **不要做**：
- 在配置文件中硬编码密码
- 提交个人环境变量到 Git
- 在生产环境使用测试配置

✅ **要做**：
- 使用环境变量占位符
- 创建个人的 `mcp-env.local.sh`（已在 .gitignore 中）
- 使用 YOLO 安全模式

### YOLO 模式使用

```bash
# 启用中等安全级别的 YOLO 模式
/yolo enable --level moderate

# 查看 YOLO 操作历史
/yolo history

# 在需要时禁用
/yolo disable
```

## 🧠 AI 增强工具使用

### Sequential Thinking（序列化思考）
用于复杂问题的逐步分析和推理：
```bash
# 通过 Claude Code 使用 Sequential Thinking 工具
# 适用于架构设计、问题分析、解决方案规划
```

### Puppeteer（浏览器自动化）
用于网页测试、截图和数据抓取：
```bash
# 自动化测试微信小程序界面
# 截图验证页面效果
# 模拟用户操作流程
```

### Context7（文档获取）
获取最新的库文档和API文档：
```bash
# 获取 Spring Boot 最新文档
# 查询 MyBatis Plus 使用方法
# 获取微信小程序 API 文档
```

### PromptX（AI 专业角色）
AI 专业能力启动器，提供专业角色支持：
```bash
# 激活不同专业角色
# 获取专业建议和解决方案
# 代码优化和最佳实践建议
```

## 🧪 测试驱动开发

### TDD 工作流

```bash
# 创建测试
/create-test --class_name UserService --test_type unit

# 运行 TDD 循环
/tdd-cycle --feature "用户注册功能"

# 查看测试覆盖率
/test-coverage
```

### 测试命令

- `/test-service --service gxz-user` - 测试指定服务
- `/integration-test` - 运行集成测试
- `/watch-tests` - 监视测试变化

## 🔍 故障排除

### 常见问题

**问题 1：Claude Code 命令不识别**
```bash
# 解决方案
brew reinstall anthropic/tap/claude-code
source .claude-code/mcp-env.sh
```

**问题 2：MCP 连接失败**
```bash
# 检查依赖
npm list -g @anthropic/mcp-mysql
# 重新安装
npm install -g @anthropic/mcp-mysql @anthropic/mcp-git
```

**问题 3：配置验证失败**
```bash
# 运行验证脚本查看详情
./.claude-code/scripts/validate-config.sh
```

### 获取帮助

1. **运行诊断脚本**：`./.claude-code/scripts/validate-config.sh`
2. **查看配置文档**：`Claude-Code-最佳实践指南.md`
3. **联系技术负责人**：通过团队沟通渠道
4. **查看日志**：`.claude-code/logs/` 目录

## 📈 高级功能

### 代码生成

使用代码模板快速生成标准化代码：

```bash
# 生成 Controller
/generate-crud --entity Product --service gxz-goods --layer controller

# 生成完整 CRUD
/generate-crud --entity Order --service gxz-order
```

### 性能监控

```bash
# 分析构建性能
/build-all --analyze

# 查看服务运行状态
/find-service-by-port --port 8080
```

### 环境管理

```bash
# 切换到测试环境
/switch-env --env test

# 查看当前环境配置
echo $SPRING_PROFILES_ACTIVE
```

## 📋 配置验证清单

使用此清单确保配置正确：

- [ ] 所有配置文件存在且格式正确
- [ ] 环境变量正确加载
- [ ] Claude Code CLI 正常工作
- [ ] 自定义命令可执行
- [ ] MCP 服务器连接正常
- [ ] Git 集成工作正常
- [ ] 团队成员配置一致

## 🔄 持续改进

### 反馈机制

- 通过 Git Issues 提出配置改进建议
- 在团队会议中讨论配置优化
- 定期审查和更新配置文件

### 配置演进

- 根据项目发展调整服务配置
- 添加新的自定义命令
- 优化安全和性能配置

---

**记住**：这套配置是为团队服务的，如果有任何改进建议，请随时提出！

**支持**：如有问题，请联系项目技术负责人或查看详细文档 `Claude-Code-最佳实践指南.md`。
