# Claude Code 最佳实践指南 - 从入门到精通

## 📋 目录

1. [Claude Code 简介](#claude-code-简介)
2. [环境准备与安装](#环境准备与安装)
3. [基础配置](#基础配置)
4. [MCP (Model Context Protocol) 集成](#mcp-model-context-protocol-集成)
5. [Git 集成最佳实践](#git-集成最佳实践)
6. [图像传递与多媒体处理](#图像传递与多媒体处理)
7. [自定义命令创建](#自定义命令创建)
8. [安全 YOLO 模式配置](#安全-yolo-模式配置)
9. [测试驱动开发 (TDD)](#测试驱动开发-tdd)
10. [高级功能与工具](#高级功能与工具)
11. [性能优化与成本控制](#性能优化与成本控制)
12. [故障排除与调试](#故障排除与调试)
13. [团队协作最佳实践](#团队协作最佳实践)

---

## Claude Code 简介

Claude Code 是 Anthropic 开发的智能编程工具，能够通过自然语言指令帮助开发者高效编写代码。它特别适合像贡享臻选这样的复杂微服务项目。

### 核心优势
- **跨文件编辑**: 能够在整个代码库中进行智能编辑
- **架构理解**: 深度理解微服务架构和业务逻辑
- **自动化测试**: 执行测试、修复代码、质量检查
- **Git 集成**: 搜索历史、解决冲突、创建 PR

---

## 环境准备与安装

### 系统要求
- **操作系统**: macOS, Linux, Windows (WSL2)
- **终端**: 支持 ANSI 颜色的现代终端
- **网络**: 稳定的互联网连接
- **内存**: 至少 4GB RAM

### 安装步骤

#### 1. 获取访问权限
```bash
# 检查 Claude Code 访问权限
# 访问 https://claude.ai/code 并申请 beta 访问权限
```

#### 2. 安装 Claude Code CLI
```bash
# 对于 macOS (使用 Homebrew)
brew install anthropic/tap/claude-code

# 对于 Linux/WSL2
curl -fsSL https://releases.anthropic.com/claude-code/install.sh | sh

# 验证安装
claude-code --version
```

#### 3. 身份认证
```bash
# 登录您的 Claude 账户
claude-code auth login

# 验证认证状态
claude-code auth status
```

---

## 基础配置

### 1. 项目初始化

为您的贡享臻选项目创建 Claude Code 配置：

```bash
# 进入项目根目录
cd /Users/chengzheng/workspace/waibao/siam-cloud/gongxiang-zhenxuan

# 初始化 Claude Code 配置
claude-code init

# 这将创建 .claude-code/ 目录和配置文件
```

### 2. 项目级配置文件

创建完整的项目级配置结构：

```bash
# 创建 Claude Code 配置目录
mkdir -p .claude-code/{config,templates,scripts,logs}

# 创建配置文件
touch .claude-code/config.yml
touch .claude-code/mcp.json
touch .claude-code/env.yml
touch .claude-code/commands.yml
touch .claude-code/security.yml
touch .claude-code/tdd.yml
```

#### 主配置文件 `.claude-code/config.yml`

```yaml
# Claude Code 项目配置文件
project:
  name: "贡享臻选 (GongXiang ZhenXuan)"
  type: "java-microservices"
  framework: "spring-boot"
  version: "1.0.0"
  description: "食品外卖和电商平台微服务架构"
  
# 构建工具配置
build:
  tool: "maven"
  java_version: "8"
  spring_boot_version: "2.7.0"
  main_command: "mvn clean package"
  test_command: "mvn test"
  
# 微服务配置
services:
  gateway:
    name: "gxz-gateway"
    port: 8080
    path: "./gxz-gateway"
    start_script: "./start.sh start"
    
  user:
    name: "gxz-user"
    port: 8081
    path: "./gxz-user"
    
  merchant:
    name: "gxz-merchant"
    port: 8082
    path: "./gxz-merchant"
    
  order:
    name: "gxz-order"
    port: 8083
    path: "./gxz-order"
    
  payment:
    name: "gxz-payment"
    port: 8084
    path: "./gxz-payment"
    
  delivery:
    name: "gxz-delivery"
    port: 8085
    path: "./gxz-delivery"
    
  goods:
    name: "gxz-goods"
    port: 8086
    path: "./gxz-goods"
    
  admin:
    name: "gxz-admin"
    port: 8087
    path: "./gxz-admin"
  
# 服务发现配置
discovery:
  type: "nacos"
  url: "localhost:8848"
  namespace: "gxz-dev"
    
# 数据库配置
database:
  primary: "mysql"
  cache: "redis"
  host: "localhost"
  port: 3306
  database: "gongxiang_zhenxuan"
  
# 忽略文件和目录
ignore:
  - "target/"
  - "*.log"
  - ".idea/"
  - "node_modules/"
  - "*.class"
  - ".DS_Store"
  - "*.iml"
  
# 代码生成配置
code_generation:
  base_package: "com.gongxiang.zhenxuan"
  author: "gongxiang-zhenxuan"
  
# 文档配置
documentation:
  api_docs_path: "./docs/api"
  architecture_docs_path: "./docs/architecture"
  auto_generate: true
```

#### 环境变量配置 `.claude-code/env.yml`

```yaml
# 项目环境变量配置
environments:
  # 开发环境
  development:
    SPRING_PROFILES_ACTIVE: "dev"
    NACOS_SERVER_ADDR: "localhost:8848"
    MYSQL_HOST: "localhost"
    MYSQL_PORT: "3306"
    MYSQL_DATABASE: "gongxiang_zhenxuan"
    REDIS_HOST: "localhost"
    REDIS_PORT: "6379"
    LOG_LEVEL: "DEBUG"
    
  # 测试环境
  test:
    SPRING_PROFILES_ACTIVE: "test"
    NACOS_SERVER_ADDR: "test-nacos:8848"
    MYSQL_HOST: "test-mysql"
    MYSQL_PORT: "3306"
    MYSQL_DATABASE: "gongxiang_zhenxuan_test"
    REDIS_HOST: "test-redis"
    REDIS_PORT: "6379"
    LOG_LEVEL: "INFO"
    
  # 生产环境（敏感信息用占位符）
  production:
    SPRING_PROFILES_ACTIVE: "prod"
    NACOS_SERVER_ADDR: "${PROD_NACOS_ADDR}"
    MYSQL_HOST: "${PROD_MYSQL_HOST}"
    MYSQL_PORT: "3306"
    MYSQL_DATABASE: "gongxiang_zhenxuan"
    REDIS_HOST: "${PROD_REDIS_HOST}"
    REDIS_PORT: "6379"
    LOG_LEVEL: "WARN"
    
# 当前使用的环境
current_environment: "development"
```

---

## MCP (Model Context Protocol) 集成

MCP 允许 Claude Code 与外部工具和服务进行深度集成。

### 1. 安装 MCP 服务器

```bash
# 安装 MCP 开发工具
npm install -g @anthropic/mcp-dev

# 验证安装
mcp-dev --version
```

### 2. 数据库 MCP 集成

为您的 MySQL 数据库创建 MCP 配置：

```json
{
  "mcpServers": {
    "mysql": {
      "command": "npx",
      "args": [
        "@anthropic/mcp-mysql",
        "--host", "localhost",
        "--port", "3306",
        "--database", "gongxiang_zhenxuan",
        "--username", "${MYSQL_USERNAME}",
        "--password", "${MYSQL_PASSWORD}"
      ],
      "env": {
        "MYSQL_USERNAME": "root",
        "MYSQL_PASSWORD": "your_password"
      }
    }
  }
}
```

### 3. Git MCP 集成

```json
{
  "mcpServers": {
    "git": {
      "command": "npx",
      "args": ["@anthropic/mcp-git"],
      "cwd": "/Users/chengzheng/workspace/waibao/siam-cloud/gongxiang-zhenxuan"
    }
  }
}
```

### 4. 文件系统 MCP 集成

```json
{
  "mcpServers": {
    "filesystem": {
      "command": "npx",
      "args": [
        "@anthropic/mcp-filesystem",
        "/Users/chengzheng/workspace/waibao/siam-cloud/gongxiang-zhenxuan"
      ]
    }
  }
}
```

### 5. 项目级 MCP 配置

创建 `.claude-code/mcp.json`：

```json
{
  "mcpServers": {
    "mysql": {
      "command": "npx",
      "args": [
        "@anthropic/mcp-mysql",
        "--host", "${MYSQL_HOST:-localhost}",
        "--port", "${MYSQL_PORT:-3306}",
        "--database", "${MYSQL_DATABASE:-gongxiang_zhenxuan}",
        "--username", "${MYSQL_USERNAME:-root}",
        "--password", "${MYSQL_PASSWORD}"
      ],
      "env": {
        "MYSQL_HOST": "localhost",
        "MYSQL_PORT": "3306",
        "MYSQL_DATABASE": "gongxiang_zhenxuan",
        "MYSQL_USERNAME": "root",
        "MYSQL_PASSWORD": "your_password"
      },
      "description": "MySQL 数据库连接，用于查询和分析数据库结构"
    },
    "git": {
      "command": "npx",
      "args": ["@anthropic/mcp-git"],
      "cwd": ".",
      "description": "Git 版本控制集成，用于管理代码版本和历史"
    },
    "filesystem": {
      "command": "npx",
      "args": ["@anthropic/mcp-filesystem", "."],
      "description": "文件系统访问，用于读取和修改项目文件"
    },
    "maven": {
      "command": "npx",
      "args": ["@anthropic/mcp-maven"],
      "cwd": ".",
      "description": "Maven 构建工具集成，用于项目构建和依赖管理"
    },
    "spring-boot": {
      "command": "npx",
      "args": ["@anthropic/mcp-spring-boot"],
      "cwd": ".",
      "description": "Spring Boot 框架集成，用于微服务开发"
    },
    "nacos": {
      "command": "npx",
      "args": ["@anthropic/mcp-nacos"],
      "env": {
        "NACOS_SERVER_ADDR": "${NACOS_SERVER_ADDR:-localhost:8848}",
        "NACOS_NAMESPACE": "${NACOS_NAMESPACE:-gxz-dev}"
      },
      "description": "Nacos 服务发现和配置中心集成"
    },
    "redis": {
      "command": "npx",
      "args": ["@anthropic/mcp-redis"],
      "env": {
        "REDIS_HOST": "${REDIS_HOST:-localhost}",
        "REDIS_PORT": "${REDIS_PORT:-6379}",
        "REDIS_PASSWORD": "${REDIS_PASSWORD}"
      },
      "description": "Redis 缓存系统集成"
    }
  },
  "globalSettings": {
    "timeout": 30000,
    "retries": 3,
    "logLevel": "info"
  }
}
```

### 6. MCP 环境变量配置

创建 `.claude-code/mcp-env.sh` 脚本来管理 MCP 环境变量：

```bash
#!/bin/bash

# MCP 环境变量配置脚本
# 使用方法: source .claude-code/mcp-env.sh

# 数据库配置
export MYSQL_HOST="${MYSQL_HOST:-localhost}"
export MYSQL_PORT="${MYSQL_PORT:-3306}"
export MYSQL_DATABASE="${MYSQL_DATABASE:-gongxiang_zhenxuan}"
export MYSQL_USERNAME="${MYSQL_USERNAME:-root}"

# Redis 配置
export REDIS_HOST="${REDIS_HOST:-localhost}"
export REDIS_PORT="${REDIS_PORT:-6379}"

# Nacos 配置
export NACOS_SERVER_ADDR="${NACOS_SERVER_ADDR:-localhost:8848}"
export NACOS_NAMESPACE="${NACOS_NAMESPACE:-gxz-dev}"

# 项目配置
export PROJECT_ROOT="$(pwd)"
export CLAUDE_CODE_CONFIG_PATH="$(pwd)/.claude-code"

echo "✅ MCP 环境变量已加载"
echo "📁 项目根目录: $PROJECT_ROOT"
echo "⚙️  配置目录: $CLAUDE_CODE_CONFIG_PATH"
```

---

## Git 集成最佳实践

### 1. Git 配置

确保 Git 配置正确：

```bash
# 检查 Git 配置
git config --list

# 设置用户信息
git config user.name "Your Name"
git config user.email "your.email@example.com"

# 设置默认编辑器
git config core.editor "code --wait"
```

### 2. Claude Code Git 命令

#### 基础 Git 操作
```bash
# 查看项目状态
/git status

# 查看提交历史
/git log --oneline -10

# 查看特定文件的历史
/git log --follow gxz-user/user-provider/src/main/java/com/example/UserController.java

# 查看分支信息
/git branch -a
```

#### 高级 Git 操作
```bash
# 搜索提交历史中的特定内容
/git search "支付" --author="张三" --since="2024-01-01"

# 查看两个分支的差异
/git diff main..feature/payment-integration

# 查看特定提交的详细信息
/git show abc1234

# 查找引入 bug 的提交
/git bisect start HEAD v1.0.0
```

### 3. 自动化提交信息

创建 `.claude-code/git-templates.yml`：

```yaml
commit_templates:
  feature: |
    feat({scope}): {summary}
    
    {description}
    
    Closes #{issue}
  
  bugfix: |
    fix({scope}): {summary}
    
    {description}
    
    Fixes #{issue}
  
  docs: |
    docs({scope}): {summary}
    
    {description}
  
  refactor: |
    refactor({scope}): {summary}
    
    {description}

# 微服务范围映射
scopes:
  - user: "用户服务"
  - merchant: "商户服务"
  - order: "订单服务"
  - payment: "支付服务"
  - delivery: "配送服务"
  - goods: "商品服务"
  - gateway: "网关服务"
  - admin: "管理后台"
```

### 4. 自动合并冲突解决

```bash
# 让 Claude Code 自动解决合并冲突
/git merge-conflicts resolve --auto

# 交互式解决冲突
/git merge-conflicts resolve --interactive

# 预览合并结果
/git merge-conflicts preview main feature/new-payment
```

---

## 图像传递与多媒体处理

### 1. 图像上传和分析

Claude Code 支持多种图像格式的处理：

#### 支持的图像格式
- PNG
- JPEG/JPG
- GIF
- WebP
- SVG
- BMP

#### 图像上传方法

**方法 1: 拖拽上传**
```bash
# 直接将图像文件拖拽到 Claude Code 终端
# Claude Code 会自动识别和分析图像内容
```

**方法 2: 文件路径上传**
```bash
# 上传本地图像文件
/image upload /path/to/your/image.png

# 上传多个图像
/image upload /path/to/image1.png /path/to/image2.jpg

# 上传目录中的所有图像
/image upload /path/to/images/
```

**方法 3: URL 上传**
```bash
# 从 URL 加载图像
/image load https://example.com/screenshot.png
```

### 2. 图像分析功能

#### UI 界面分析
```bash
# 分析 UI 截图并生成对应的代码
/analyze ui screenshot.png --framework spring-boot --output controller

# 分析移动端 UI
/analyze ui mobile-screen.png --platform miniprogram --output component
```

#### 架构图分析
```bash
# 分析系统架构图
/analyze architecture system-diagram.png --output documentation

# 生成对应的代码结构
/analyze architecture microservices.png --output maven-modules
```

#### 错误截图分析
```bash
# 分析错误截图并提供解决方案
/debug screenshot error-page.png

# 分析日志截图
/debug logs log-screenshot.png --format spring-boot
```

### 3. 为贡享臻选项目定制的图像处理

#### 小程序界面分析
```bash
# 分析微信小程序界面截图
/analyze miniprogram ui-design.png --generate pages

# 生成对应的 WXML 和 WXSS 代码
/analyze miniprogram checkout-page.png --output wxml,wxss,js
```

#### 数据库设计图分析
```bash
# 分析 ER 图并生成对应的实体类
/analyze database er-diagram.png --output entity --package com.gongxiang.zhenxuan

# 生成 MyBatis Mapper
/analyze database schema.png --output mapper --framework mybatis-plus
```

---

## 自定义命令创建

### 1. 创建项目特定命令

在 `.claude-code/commands.yml` 中定义自定义命令：

```yaml
# 贡享臻选项目自定义命令
commands:
  # 服务管理命令
  start-gateway:
    description: "启动网关服务"
    command: "cd gxz-gateway && ./start.sh start"
    working_directory: "."
    
  stop-gateway:
    description: "停止网关服务"
    command: "cd gxz-gateway && ./start.sh stop"
    working_directory: "."
    
  restart-gateway:
    description: "重启网关服务"
    command: "cd gxz-gateway && ./start.sh restart"
    working_directory: "."
    
  # 构建命令
  build-all:
    description: "构建所有微服务"
    command: "mvn clean package -T 4"
    working_directory: "."
    
  build-service:
    description: "构建指定微服务"
    command: "cd {service} && mvn clean package"
    parameters:
      service:
        type: "choice"
        choices: ["gxz-user", "gxz-merchant", "gxz-order", "gxz-payment", "gxz-delivery", "gxz-goods", "gxz-gateway", "gxz-admin"]
        description: "选择要构建的服务"
    
  # 测试命令
  test-service:
    description: "运行指定服务的测试"
    command: "cd {service} && mvn test"
    parameters:
      service:
        type: "choice"
        choices: ["gxz-user", "gxz-merchant", "gxz-order", "gxz-payment", "gxz-delivery", "gxz-goods"]
        description: "选择要测试的服务"
        
  integration-test:
    description: "运行集成测试"
    command: "mvn verify -P integration-test"
    working_directory: "."
    
  # 数据库管理命令
  db-migrate:
    description: "执行数据库迁移"
    command: "mvn flyway:migrate -Dflyway.configFiles=db/flyway.conf"
    working_directory: "."
    
  db-seed:
    description: "导入测试数据"
    command: "mysql -u root -p gongxiang_zhenxuan < db/seed.sql"
    working_directory: "."
    
  # 代码质量检查
  code-quality:
    description: "运行代码质量检查"
    command: "mvn spotbugs:check checkstyle:check pmd:check"
    working_directory: "."
    
  # 部署命令
  deploy-dev:
    description: "部署到开发环境"
    command: "docker-compose -f docker-compose.dev.yml up -d"
    working_directory: "."
    
  # 日志查看
  logs-gateway:
    description: "查看网关日志"
    command: "cd gxz-gateway && ./start.sh logs"
    working_directory: "."
    
  logs-service:
    description: "查看指定服务日志"
    command: "docker logs -f gxz-{service}"
    parameters:
      service:
        type: "choice"
        choices: ["user", "merchant", "order", "payment", "delivery", "goods"]
        description: "选择要查看日志的服务"
```

### 2. 复杂命令示例

```yaml
# 高级自定义命令
commands:
  # 智能代码生成
  generate-crud:
    description: "为指定实体生成完整的 CRUD 代码"
    command: |
      echo "正在为 {entity} 生成 CRUD 代码..."
      mkdir -p {service}/{entity_lower}/src/main/java/com/gongxiang/zhenxuan/{service_lower}/{entity_lower}/{layer}
    parameters:
      entity:
        type: "string"
        description: "实体类名称（如：User, Order, Product）"
      service:
        type: "choice"
        choices: ["gxz-user", "gxz-merchant", "gxz-order", "gxz-goods"]
        description: "目标服务"
      layer:
        type: "choice"
        choices: ["controller", "service", "mapper", "entity"]
        description: "要生成的层"
        
  # 环境切换
  switch-env:
    description: "切换开发环境"
    command: |
      echo "切换到 {env} 环境"
      cp config/application-{env}.yml config/application.yml
      echo "SPRING_PROFILES_ACTIVE={env}" > .env
    parameters:
      env:
        type: "choice"
        choices: ["dev", "test", "staging", "prod"]
        description: "目标环境"
        
  # 性能测试
  performance-test:
    description: "运行性能测试"
    command: |
      echo "开始性能测试，目标: {endpoint}"
      ab -n {requests} -c {concurrency} http://localhost:8080{endpoint}
    parameters:
      endpoint:
        type: "string"
        description: "测试端点（如：/api/user/login）"
      requests:
        type: "number"
        default: 1000
        description: "总请求数"
      concurrency:
        type: "number"
        default: 10
        description: "并发数"
```

### 3. 使用自定义命令

```bash
# 列出所有自定义命令
/commands list

# 执行自定义命令
/start-gateway
/build-service --service gxz-user
/test-service --service gxz-order

# 生成 CRUD 代码
/generate-crud --entity Product --service gxz-goods --layer controller

# 切换环境
/switch-env --env test

# 运行性能测试
/performance-test --endpoint "/api/user/login" --requests 5000 --concurrency 50
```

---

## 安全 YOLO 模式配置

YOLO (You Only Live Once) 模式允许 Claude Code 在确保安全的前提下自动执行某些操作。

### 1. YOLO 模式基础配置

创建项目级安全配置 `.claude-code/security.yml`：

```yaml
# YOLO 模式安全配置
yolo_mode:
  enabled: true
  level: "moderate"  # safe, moderate, aggressive
  
  # 允许的自动操作
  allowed_operations:
    # 文件操作
    file_operations:
      - "create"
      - "edit"
      - "rename"
      - "copy"
    
    # 构建操作
    build_operations:
      - "compile"
      - "test"
      - "package"
    
    # Git 操作
    git_operations:
      - "add"
      - "commit"
      - "push"      # 仅在安全分支
      - "pull"
      - "merge"     # 仅在开发分支
    
    # 数据库操作
    database_operations:
      - "select"
      - "insert"    # 仅测试数据
      - "update"    # 仅开发环境
    
  # 禁止的操作
  forbidden_operations:
    - "delete_files"
    - "drop_tables"
    - "production_deploy"
    - "system_commands"
    
  # 安全分支
  safe_branches:
    - "develop"
    - "feature/*"
    - "hotfix/*"
    - "test/*"
    
  # 保护分支
  protected_branches:
    - "main"
    - "master"
    - "release/*"
    
  # 安全环境
  safe_environments:
    - "development"
    - "test"
    - "staging"
    
  # 生产环境保护
  protected_environments:
    - "production"
    - "prod"
```

### 2. 分层安全策略

#### Safe 级别
```yaml
yolo_mode:
  level: "safe"
  allowed_operations:
    file_operations:
      - "create"    # 仅在 src/test/ 目录
      - "edit"      # 仅非关键文件
    build_operations:
      - "test"      # 仅单元测试
    git_operations:
      - "add"
      - "commit"    # 仅在 feature 分支
```

#### Moderate 级别
```yaml
yolo_mode:
  level: "moderate"
  allowed_operations:
    file_operations:
      - "create"
      - "edit"
      - "rename"
    build_operations:
      - "compile"
      - "test"
      - "package"   # 仅开发环境
    git_operations:
      - "add"
      - "commit"
      - "push"      # 仅 feature 分支
      - "pull"
```

#### Aggressive 级别
```yaml
yolo_mode:
  level: "aggressive"
  allowed_operations:
    file_operations:
      - "create"
      - "edit"
      - "rename"
      - "copy"
      - "move"
    build_operations:
      - "compile"
      - "test"
      - "package"
      - "deploy"    # 仅开发环境
    git_operations:
      - "add"
      - "commit"
      - "push"
      - "pull"
      - "merge"     # 仅开发分支
    database_operations:
      - "select"
      - "insert"
      - "update"    # 仅开发数据库
```

### 3. 项目特定安全规则

```yaml
# 贡享臻选项目安全规则
project_security:
  # 关键文件保护
  protected_files:
    - "application-prod.yml"
    - "application-production.yml"
    - "*.properties"
    - "pom.xml"
    - "docker-compose.prod.yml"
    
  # 敏感目录保护
  protected_directories:
    - "config/prod/"
    - "scripts/deploy/"
    - "secrets/"
    - ".ssh/"
    
  # 微服务特定规则
  service_rules:
    gxz-payment:
      # 支付服务特殊保护
      protected_files:
        - "**/PaymentController.java"
        - "**/PaymentService.java"
        - "**/WechatPayConfig.java"
      forbidden_operations:
        - "auto_deploy"
        - "auto_test_production"
        
    gxz-user:
      # 用户服务规则
      allowed_operations:
        - "create_test_users"
        - "update_user_info"
      forbidden_operations:
        - "delete_users"
        - "access_sensitive_data"
        
  # 数据库安全
  database_rules:
    development:
      allowed_operations: ["select", "insert", "update", "delete"]
    test:
      allowed_operations: ["select", "insert", "update"]
    production:
      allowed_operations: ["select"]  # 只读
```

### 4. 使用 YOLO 模式

```bash
# 启用 YOLO 模式
/yolo enable --level moderate

# 禁用 YOLO 模式
/yolo disable

# 查看 YOLO 模式状态
/yolo status

# 临时调整 YOLO 级别
/yolo set-level safe

# 为特定操作启用 YOLO
/yolo allow git_push --branch feature/payment

# 查看 YOLO 操作历史
/yolo history

# 回滚 YOLO 操作
/yolo rollback --operation 12345
```

### 5. YOLO 模式监控

创建项目级 YOLO 监控配置 `.claude-code/yolo-monitor.yml`：

```yaml
# YOLO 操作监控
monitoring:
  # 操作日志
  logging:
    enabled: true
    file: ".claude-code/yolo.log"
    level: "info"
    
  # 通知设置
  notifications:
    # 高风险操作通知
    high_risk_operations:
      - "database_update"
      - "git_merge_to_main"
      - "production_deploy"
    
    # 通知方式
    methods:
      email:
        enabled: true
        recipients: ["dev-team@company.com"]
      slack:
        enabled: true
        webhook: "${SLACK_WEBHOOK_URL}"
        channel: "#claude-code-alerts"
        
  # 审计追踪
  audit:
    enabled: true
    retention_days: 30
    include_context: true
    
  # 自动回滚条件
  auto_rollback:
    enabled: true
    conditions:
      - "test_failure"
      - "build_failure"
      - "security_violation"
```

---

## 测试驱动开发 (TDD)

### 1. TDD 工作流配置

创建项目级 TDD 配置 `.claude-code/tdd.yml`：

```yaml
# TDD 配置
tdd:
  enabled: true
  
  # 测试框架配置
  frameworks:
    unit_testing:
      java: "junit5"
      spring: "spring-boot-test"
      mocking: "mockito"
      
    integration_testing:
      spring: "spring-boot-test"
      database: "testcontainers"
      web: "spring-boot-test-web"
      
  # 测试模板
  templates:
    unit_test: |
      package {package};
      
      import org.junit.jupiter.api.Test;
      import org.junit.jupiter.api.BeforeEach;
      import org.junit.jupiter.api.DisplayName;
      import org.mockito.Mock;
      import org.mockito.junit.jupiter.MockitoExtension;
      import org.junit.jupiter.api.extension.ExtendWith;
      
      import static org.junit.jupiter.api.Assertions.*;
      import static org.mockito.Mockito.*;
      
      @ExtendWith(MockitoExtension.class)
      @DisplayName("{class_name} 单元测试")
      class {class_name}Test {
      
          @Mock
          private {dependency} {dependency_name};
          
          private {class_name} {instance_name};
          
          @BeforeEach
          void setUp() {
              {instance_name} = new {class_name}({dependency_name});
          }
          
          @Test
          @DisplayName("应该{test_description}")
          void should{test_method}() {
              // Given (准备测试数据)
              
              // When (执行被测试方法)
              
              // Then (验证结果)
              
          }
      }
      
    integration_test: |
      package {package};
      
      import org.junit.jupiter.api.Test;
      import org.springframework.boot.test.context.SpringBootTest;
      import org.springframework.test.context.ActiveProfiles;
      import org.springframework.transaction.annotation.Transactional;
      import org.testcontainers.junit.jupiter.Testcontainers;
      
      @SpringBootTest
      @ActiveProfiles("test")
      @Testcontainers
      @Transactional
      class {class_name}IntegrationTest {
      
          @Test
          void should{test_method}() {
              // Given
              
              // When
              
              // Then
              
          }
      }
      
  # TDD 规则
  rules:
    # 测试优先
    test_first: true
    
    # 代码覆盖率要求
    coverage:
      minimum: 80
      target: 90
      
    # 测试命名规范
    naming_convention:
      test_class: "{ClassName}Test"
      test_method: "should{ExpectedBehavior}When{Condition}"
      
    # 自动化
    automation:
      run_tests_on_save: true
      generate_missing_tests: true
      update_tests_on_refactor: true
```

### 2. TDD 命令

创建 TDD 相关的自定义命令：

```yaml
# TDD 自定义命令
commands:
  # 创建测试
  create-test:
    description: "为指定类创建测试"
    command: |
      echo "为 {class_name} 创建测试..."
      # Claude Code 会自动生成测试代码
    parameters:
      class_name:
        type: "string"
        description: "要测试的类名"
      test_type:
        type: "choice"
        choices: ["unit", "integration", "e2e"]
        default: "unit"
        description: "测试类型"
        
  # 运行 TDD 循环
  tdd-cycle:
    description: "执行完整的 TDD 循环"
    command: |
      echo "开始 TDD 循环..."
      echo "1. Red: 编写失败的测试"
      echo "2. Green: 编写最简实现"
      echo "3. Refactor: 重构代码"
    parameters:
      feature:
        type: "string"
        description: "要开发的功能"
        
  # 测试覆盖率
  test-coverage:
    description: "生成测试覆盖率报告"
    command: "mvn jacoco:report"
    working_directory: "."
    
  # 运行特定测试
  run-test:
    description: "运行指定的测试"
    command: "mvn test -Dtest={test_class}"
    parameters:
      test_class:
        type: "string"
        description: "测试类名"
        
  # 监视测试
  watch-tests:
    description: "监视文件变化并自动运行测试"
    command: "mvn test -Dtest={test_pattern} -DforkCount=0"
    parameters:
      test_pattern:
        type: "string"
        default: "*Test"
        description: "测试文件模式"
```

### 3. 微服务特定 TDD 配置

#### 用户服务 TDD 示例

```yaml
# gxz-user 服务 TDD 配置
services:
  gxz-user:
    tdd:
      # 测试数据库配置
      test_database:
        type: "h2"
        url: "jdbc:h2:mem:testdb"
        
      # 测试场景
      test_scenarios:
        user_registration:
          description: "用户注册功能测试"
          tests:
            - "should_register_new_user_successfully"
            - "should_reject_duplicate_phone_number"
            - "should_validate_phone_number_format"
            - "should_encrypt_password"
            
        user_authentication:
          description: "用户认证功能测试"
          tests:
            - "should_authenticate_valid_credentials"
            - "should_reject_invalid_credentials"
            - "should_lock_account_after_failed_attempts"
            
      # Mock 配置
      mocks:
        external_services:
          - "SmsService"
          - "WechatApiService"
          - "RedisService"
```

#### 订单服务 TDD 示例

```yaml
# gxz-order 服务 TDD 配置
services:
  gxz-order:
    tdd:
      test_scenarios:
        order_creation:
          description: "订单创建功能测试"
          tests:
            - "should_create_order_with_valid_products"
            - "should_calculate_total_amount_correctly"
            - "should_apply_coupon_discount"
            - "should_check_product_inventory"
            
        order_status_management:
          description: "订单状态管理测试"
          tests:
            - "should_update_order_status"
            - "should_send_notification_on_status_change"
            - "should_handle_payment_callback"
            
      # 集成测试配置
      integration_tests:
        database_operations:
          - "order_persistence"
          - "order_query"
          - "order_update"
        external_services:
          - "payment_service_integration"
          - "inventory_service_integration"
          - "notification_service_integration"
```

### 4. 使用 TDD 命令

```bash
# 创建用户注册功能的测试
/create-test --class_name UserRegistrationService --test_type unit

# 开始 TDD 循环
/tdd-cycle --feature "用户手机号注册"

# 运行特定测试
/run-test --test_class UserRegistrationServiceTest

# 生成覆盖率报告
/test-coverage

# 监视测试（文件变化时自动运行）
/watch-tests --test_pattern "*UserTest"

# 为订单服务创建集成测试
/create-test --class_name OrderController --test_type integration
```

### 5. TDD 最佳实践

#### Red-Green-Refactor 循环示例

```bash
# 1. Red: 编写失败的测试
/tdd red --feature "用户登录" --description "应该验证用户凭据"

# Claude Code 生成失败的测试：
# @Test
# void shouldAuthenticateValidCredentials() {
#     // Given
#     String phone = "13812345678";
#     String password = "password123";
#     
#     // When
#     AuthResult result = userService.authenticate(phone, password);
#     
#     // Then
#     assertTrue(result.isSuccess());
#     assertNotNull(result.getToken());
# }

# 2. Green: 编写最简实现
/tdd green --make_test_pass

# Claude Code 生成最简实现让测试通过

# 3. Refactor: 重构代码
/tdd refactor --improve_design --maintain_tests
```

#### 测试数据管理

```yaml
# 测试数据配置
test_data:
  users:
    valid_user:
      phone: "13812345678"
      password: "password123"
      name: "测试用户"
    invalid_user:
      phone: "invalid_phone"
      password: "weak"
      
  orders:
    sample_order:
      user_id: 1
      total_amount: 59.90
      status: "PENDING"
      items:
        - product_id: 1
          quantity: 2
          price: 29.95
          
  products:
    sample_product:
      name: "麻辣烫"
      price: 29.95
      category: "热菜"
      merchant_id: 1
```

---

## 高级功能与工具

### 1. 代码生成器

#### 实体类生成器

```bash
# 从数据库表生成实体类
/generate entity --table users --package com.gongxiang.zhenxuan.user.entity

# 生成带验证注解的实体类
/generate entity --table orders --package com.gongxiang.zhenxuan.order.entity --validation

# 批量生成所有表的实体类
/generate entities --database gongxiang_zhenxuan --package com.gongxiang.zhenxuan
```

#### Controller 生成器

```bash
# 生成 RESTful Controller
/generate controller --entity User --package com.gongxiang.zhenxuan.user.controller

# 生成带 Swagger 文档的 Controller
/generate controller --entity Order --package com.gongxiang.zhenxuan.order.controller --swagger

# 生成微信小程序专用 Controller
/generate controller --entity Product --package com.gongxiang.zhenxuan.goods.controller --miniprogram
```

#### Service 层生成器

```bash
# 生成 Service 接口和实现
/generate service --entity User --package com.gongxiang.zhenxuan.user.service

# 生成带事务管理的 Service
/generate service --entity Order --package com.gongxiang.zhenxuan.order.service --transactional

# 生成带缓存的 Service
/generate service --entity Product --package com.gongxiang.zhenxuan.goods.service --cache
```

### 2. 微服务架构工具

#### 服务依赖分析

```bash
# 分析服务间依赖关系
/analyze dependencies --service gxz-order

# 生成服务依赖图
/analyze dependencies --all --output dependency-graph.png

# 检查循环依赖
/analyze circular-dependencies
```

#### API 文档生成

```bash
# 生成 OpenAPI 文档
/docs generate --service gxz-user --format openapi

# 生成微信小程序 API 文档
/docs generate --service gxz-order --format miniprogram

# 生成全局 API 文档
/docs generate --all --format confluence
```

#### 配置管理

```bash
# 同步配置到 Nacos
/config sync --environment dev --service gxz-user

# 验证配置完整性
/config validate --all

# 加密敏感配置
/config encrypt --key database.password --service gxz-payment
```

### 3. 数据库管理工具

#### 数据库迁移

```bash
# 生成迁移脚本
/db migration create --name add_user_avatar_column

# 执行迁移
/db migration run --environment dev

# 回滚迁移
/db migration rollback --version 001

# 查看迁移状态
/db migration status
```

#### 数据同步

```bash
# 从生产环境同步数据结构到开发环境
/db sync-schema --from prod --to dev --tables users,orders

# 同步测试数据
/db sync-data --from test --to dev --exclude users.password
```

### 4. 性能监控与优化

#### 性能分析

```bash
# 分析 SQL 查询性能
/performance analyze-sql --service gxz-order --duration 24h

# 生成性能报告
/performance report --service gxz-user --metrics cpu,memory,db

# 检查 N+1 查询问题
/performance check-n-plus-one --service gxz-goods
```

#### 缓存管理

```bash
# 分析缓存使用情况
/cache analyze --service gxz-product

# 清理过期缓存
/cache cleanup --pattern "user:*" --ttl-expired

# 预热缓存
/cache warmup --service gxz-goods --data products
```

### 5. 安全审计工具

#### 代码安全扫描

```bash
# 扫描安全漏洞
/security scan --service gxz-payment --level critical

# 检查依赖库漏洞
/security check-dependencies --all

# 生成安全报告
/security report --format pdf --output security-audit.pdf
```

#### 敏感信息检测

```bash
# 检测硬编码的敏感信息
/security detect-secrets --exclude test/

# 检查 SQL 注入风险
/security check-sql-injection --service gxz-user

# 验证输入验证
/security check-input-validation --all
```

---

## 性能优化与成本控制

### 1. Token 使用优化

#### 智能上下文管理

```yaml
# 上下文管理配置
context_management:
  # 自动优化上下文
  auto_optimize: true
  
  # 上下文大小限制
  max_context_size: 200000
  
  # 文件包含策略
  file_inclusion:
    # 优先包含的文件类型
    priority_types: [".java", ".yml", ".xml", ".sql"]
    
    # 排除的文件类型
    exclude_types: [".log", ".tmp", ".class", ".jar"]
    
    # 排除的目录
    exclude_directories: ["target/", ".git/", "node_modules/"]
    
  # 智能文件选择
  smart_selection:
    enabled: true
    
    # 基于文件修改时间
    prefer_recent_files: true
    
    # 基于文件相关性
    relevance_scoring: true
    
    # 基于文件大小
    size_optimization: true
```

#### 增量处理策略

```bash
# 只处理变更的文件
/optimize context --incremental --since "2024-01-01"

# 基于 Git 变更的智能上下文
/optimize context --git-diff --branch main

# 只包含相关文件
/optimize context --related-to OrderService
```

### 2. 成本监控

#### 成本追踪配置

```yaml
# 成本监控配置
cost_monitoring:
  enabled: true
  
  # 预算设置
  budgets:
    daily: 50.00    # 每日预算 (USD)
    weekly: 300.00  # 每周预算
    monthly: 1000.00 # 每月预算
    
  # 警告阈值
  alerts:
    warning_threshold: 0.8   # 80% 预算使用时警告
    critical_threshold: 0.95 # 95% 预算使用时严重警告
    
  # 成本分类
  categories:
    - code_generation
    - code_review
    - debugging
    - documentation
    - testing
    
  # 成本报告
  reporting:
    daily_summary: true
    weekly_report: true
    cost_breakdown: true
```

#### 成本优化策略

```bash
# 查看成本使用情况
/cost summary --period week

# 分析最耗费 token 的操作
/cost analyze --top 10

# 设置成本限制
/cost limit --daily 30 --weekly 150

# 优化建议
/cost optimize --suggest
```

### 3. 批量处理优化

#### 批量操作配置

```yaml
# 批量处理配置
batch_processing:
  # 并行处理
  parallel_execution:
    enabled: true
    max_threads: 4
    
  # 批次大小
  batch_sizes:
    file_processing: 10
    code_generation: 5
    test_creation: 8
    
  # 优先级队列
  priority_queue:
    high: ["critical_bugs", "security_fixes"]
    medium: ["new_features", "refactoring"]
    low: ["documentation", "cleanup"]
```

#### 批量操作示例

```bash
# 批量生成测试文件
/batch generate-tests --pattern "src/main/java/**/*Service.java" --parallel

# 批量代码审查
/batch code-review --files changed --since "last week"

# 批量重构
/batch refactor --pattern "*Controller.java" --rule "extract_method"
```

---

## 故障排除与调试

### 1. 常见问题诊断

#### 连接问题

```bash
# 检查网络连接
/debug connection --test-endpoint api.anthropic.com

# 验证认证状态
/debug auth --check-token

# 测试 MCP 连接
/debug mcp --server mysql --test-connection
```

#### 性能问题

```bash
# 分析响应时间
/debug performance --operation code_generation --samples 10

# 检查内存使用
/debug memory --show-usage

# 分析 token 使用效率
/debug tokens --analyze-efficiency
```

### 2. 日志分析

#### 日志配置

```yaml
# 日志配置
logging:
  level: "info"  # debug, info, warn, error
  
  # 日志文件
  files:
    main: ".claude-code/logs/claude-code.log"
    performance: ".claude-code/logs/performance.log"
    security: ".claude-code/logs/security.log"
    
  # 日志格式
  format: "{timestamp} [{level}] {operation} - {message}"
  
  # 日志轮转
  rotation:
    max_size: "100MB"
    max_files: 10
    compress: true
```

#### 日志分析命令

```bash
# 查看最近的错误
/logs errors --last 24h

# 分析性能日志
/logs performance --operation code_generation --stats

# 搜索特定操作的日志
/logs search --operation "git_operations" --level error

# 生成日志报告
/logs report --period week --format html
```

### 3. 调试工具

#### 详细输出模式

```bash
# 启用详细输出
/debug verbose --enable

# 跟踪操作执行
/debug trace --operation create_test --show-steps

# 显示内部状态
/debug state --show-context --show-config
```

#### 问题报告

```bash
# 生成问题报告
/debug report --issue "代码生成失败" --include-logs --include-config

# 收集诊断信息
/debug collect --all --output diagnostic-info.zip

# 发送反馈
/feedback submit --bug --description "详细问题描述" --attach diagnostic-info.zip
```

---

## 团队协作最佳实践

### 1. 团队配置

#### 团队设置

```yaml
# 团队配置
team:
  name: "贡享臻选开发团队"
  
  # 团队成员
  members:
    - name: "张三"
      role: "tech_lead"
      services: ["gxz-gateway", "gxz-common"]
      claude_id: "zhang.san@company.com"
      
    - name: "李四"
      role: "backend_developer"
      services: ["gxz-user", "gxz-merchant"]
      claude_id: "li.si@company.com"
      
    - name: "王五"
      role: "backend_developer"
      services: ["gxz-order", "gxz-payment"]
      claude_id: "wang.wu@company.com"
      
    - name: "赵六"
      role: "frontend_developer"
      services: ["gxz-admin"]
      claude_id: "zhao.liu@company.com"
      
  # 角色权限
  roles:
    tech_lead:
      permissions:
        - "all_services"
        - "deployment"
        - "configuration"
        - "security_settings"
        
    backend_developer:
      permissions:
        - "assigned_services"
        - "database_operations"
        - "unit_testing"
        
    frontend_developer:
      permissions:
        - "ui_components"
        - "frontend_testing"
        - "integration_testing"
```

### 2. 协作工作流

#### 代码审查流程

```yaml
# 代码审查配置
code_review:
  # 自动审查规则
  auto_review:
    enabled: true
    
    # 审查标准
    standards:
      - "code_style"
      - "security_best_practices"
      - "performance_optimization"
      - "test_coverage"
      
    # 审查级别
    levels:
      - "syntax_check"
      - "logic_review"
      - "architecture_review"
      
  # 人工审查流程
  manual_review:
    required_reviewers: 2
    
    # 审查检查清单
    checklist:
      - "业务逻辑正确性"
      - "异常处理完整性"
      - "数据库操作安全性"
      - "API 接口设计合理性"
      - "测试用例覆盖度"
      
  # 审查模板
  templates:
    bug_fix: |
      ## 问题描述
      {problem_description}
      
      ## 解决方案
      {solution_description}
      
      ## 测试验证
      {test_verification}
      
      ## 影响范围
      {impact_scope}
      
    feature: |
      ## 功能描述
      {feature_description}
      
      ## 实现方案
      {implementation_plan}
      
      ## 测试计划
      {test_plan}
      
      ## 性能影响
      {performance_impact}
```

#### 协作命令

```bash
# 请求代码审查
/review request --files changed --assignee "li.si@company.com"

# 进行代码审查
/review perform --pull-request 123 --checklist security,performance

# 查看团队活动
/team activity --period week

# 分配任务
/team assign --task "实现用户登录功能" --member "wang.wu@company.com" --service gxz-user

# 团队协作报告
/team report --type productivity --period month
```

### 3. 知识共享

#### 知识库配置

```yaml
# 团队知识库
knowledge_base:
  # 文档分类
  categories:
    - "architecture_decisions"
    - "coding_standards"
    - "deployment_procedures"
    - "troubleshooting_guides"
    - "api_documentation"
    
  # 自动文档生成
  auto_documentation:
    enabled: true
    
    # 文档类型
    types:
      - "api_docs"
      - "database_schema"
      - "service_dependencies"
      - "configuration_guide"
      
  # 知识共享规则
  sharing_rules:
    # 自动共享新功能文档
    auto_share_features: true
    
    # 问题解决方案共享
    share_solutions: true
    
    # 最佳实践收集
    collect_best_practices: true
```

#### 知识管理命令

```bash
# 创建知识文档
/knowledge create --title "微信支付集成指南" --category api_documentation

# 搜索知识库
/knowledge search --query "Redis 配置" --category troubleshooting_guides

# 分享解决方案
/knowledge share --solution "订单状态同步问题解决方案" --tags order,redis

# 生成团队知识报告
/knowledge report --team --period quarter
```

### 4. 代码标准化

#### 代码规范配置

```yaml
# 代码规范
coding_standards:
  # Java 规范
  java:
    # 命名规范
    naming:
      class: "PascalCase"
      method: "camelCase"
      variable: "camelCase"
      constant: "UPPER_SNAKE_CASE"
      package: "lowercase"
      
    # 代码风格
    style:
      line_length: 120
      indentation: 4
      brace_style: "same_line"
      
    # 注释规范
    comments:
      class_required: true
      method_required: true
      complex_logic_required: true
      
  # Spring Boot 特定规范
  spring_boot:
    # 控制器规范
    controller:
      naming_pattern: "*Controller"
      base_path: "/api/{version}/{module}"
      
    # 服务层规范
    service:
      naming_pattern: "*Service"
      interface_required: true
      
    # 实体类规范
    entity:
      naming_pattern: "*Entity"
      validation_required: true
      
  # 微服务规范
  microservices:
    # 服务命名
    service_naming: "gxz-{domain}"
    
    # API 版本控制
    api_versioning: "url_path"  # /api/v1/
    
    # 错误处理
    error_handling: "global_exception_handler"
```

#### 代码标准化命令

```bash
# 检查代码规范
/standards check --service gxz-user --rules naming,style,comments

# 自动格式化代码
/standards format --files changed --apply

# 生成规范报告
/standards report --team --violations-only

# 更新代码模板
/standards template --type controller --service gxz-order
```

---

## 总结

这份 Claude Code 最佳实践指南涵盖了从基础配置到高级功能的全方位内容。作为 Claude Code 的小白用户，建议您按以下顺序进行学习和实践：

### 🎯 团队协作实施步骤

#### 1. 项目负责人初始化配置

```bash
# 1. 初始化 Claude Code 配置
cd /Users/chengzheng/workspace/waibao/siam-cloud/gongxiang-zhenxuan
claude-code init

# 2. 创建完整的配置结构
mkdir -p .claude-code/{config,templates,scripts,logs}

# 3. 创建所有配置文件
touch .claude-code/config.yml
touch .claude-code/mcp.json
touch .claude-code/env.yml
touch .claude-code/commands.yml
touch .claude-code/security.yml
touch .claude-code/tdd.yml
touch .claude-code/yolo-monitor.yml

# 4. 复制本指南中的配置内容到对应文件

# 5. 提交配置到 Git 仓库
git add .claude-code/
git commit -m "feat: 添加 Claude Code 团队配置"
git push origin main
```

#### 2. 团队成员设置

```bash
# 1. 拉取最新代码
git pull origin main

# 2. 验证配置目录存在
ls -la .claude-code/

# 3. 安装 MCP 依赖
source .claude-code/mcp-env.sh

# 4. 验证 Claude Code 配置
claude-code --help

# 5. 测试基本功能
/commands list
```

#### 3. 配置文件说明

```
.claude-code/
├── config.yml          # 主配置文件（项目信息、服务配置）
├── mcp.json            # MCP 服务器配置
├── env.yml             # 环境变量配置
├── commands.yml        # 自定义命令配置
├── security.yml        # 安全和 YOLO 模式配置
├── tdd.yml             # TDD 工作流配置
├── yolo-monitor.yml    # YOLO 监控配置
├── mcp-env.sh          # MCP 环境变量脚本
├── templates/          # 代码模板目录
├── scripts/            # 自定义脚本目录
└── logs/              # Claude Code 日志目录
```

#### 4. 版本控制管理

创建 `.claude-code/.gitignore`：

```
# 忽略敏感信息
mcp-env.local.sh
*.log
logs/
*.tmp

# 忽略个人配置
personal-config.yml
user-preferences.yml

# 保留团队配置
!config.yml
!mcp.json
!env.yml
!commands.yml
!security.yml
!tdd.yml
!yolo-monitor.yml
!templates/
!scripts/
```

### 📋 学习路径建议

#### 第一周：基础配置
- [ ] 完成 Claude Code 安装
- [ ] 设置项目级配置文件
- [ ] 熟悉基本命令和工作流
- [ ] 配置 Git 集成

#### 第二周：MCP 集成
- [ ] 配置数据库 MCP 连接
- [ ] 设置文件系统和 Maven 集成
- [ ] 测试 MCP 功能
- [ ] 创建项目特定命令

#### 第三周：安全与测试
- [ ] 配置 YOLO 安全模式
- [ ] 设置 TDD 工作流
- [ ] 建立测试习惯
- [ ] 配置代码质量检查

#### 第四周：高级功能
- [ ] 探索代码生成功能
- [ ] 设置性能监控
- [ ] 优化成本控制
- [ ] 完善团队协作流程

### 🔧 配置验证清单

#### 环境验证
- [ ] Claude Code CLI 正常工作
- [ ] 项目配置文件加载成功
- [ ] MCP 服务器连接正常
- [ ] 环境变量配置正确

#### 功能验证
- [ ] 自定义命令可执行
- [ ] Git 集成工作正常
- [ ] 代码生成功能可用
- [ ] 安全模式配置生效

#### 团队验证
- [ ] 所有成员使用相同配置
- [ ] 配置文件已加入版本控制
- [ ] 团队协作流程建立
- [ ] 知识共享机制运行

### 💡 最佳实践总结

1. **配置标准化**：所有配置放在项目下，便于团队共享
2. **渐进式采用**：不要一次性启用所有功能，逐步添加
3. **定期备份**：配置文件加入版本控制，定期备份
4. **团队同步**：定期检查团队成员配置一致性
5. **持续优化**：根据使用反馈调整和优化配置

### 🚀 进阶建议

- **自动化脚本**：创建一键配置脚本
- **配置模板**：为不同角色创建配置模板
- **监控告警**：设置 Claude Code 使用监控
- **成本控制**：定期审查和优化成本使用

这套完整的项目级配置将确保团队所有成员使用统一的 Claude Code 标准，提升贡享臻选项目的开发效率和代码质量。
