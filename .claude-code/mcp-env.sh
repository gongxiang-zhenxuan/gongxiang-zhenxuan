#!/bin/bash

# MCP 环境变量配置脚本 - 贡享臻选项目
# 使用方法: source .claude-code/mcp-env.sh

# 颜色定义
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}🔧 正在加载 Claude Code MCP 环境变量...${NC}"

# 数据库配置
export MYSQL_HOST="${MYSQL_HOST:-localhost}"
export MYSQL_PORT="${MYSQL_PORT:-3306}"
export MYSQL_DATABASE="${MYSQL_DATABASE:-gongxiang_zhenxuan}"
export MYSQL_USERNAME="${MYSQL_USERNAME:-root}"

# 如果没有设置密码，提示用户
if [ -z "$MYSQL_PASSWORD" ]; then
    echo -e "${YELLOW}⚠️  MYSQL_PASSWORD 环境变量未设置${NC}"
    echo -e "${YELLOW}   请在 ~/.bashrc 或 ~/.zshrc 中设置：${NC}"
    echo -e "${YELLOW}   export MYSQL_PASSWORD='your_password'${NC}"
fi

# Redis 配置
export REDIS_HOST="${REDIS_HOST:-localhost}"
export REDIS_PORT="${REDIS_PORT:-6379}"

# Nacos 配置
export NACOS_SERVER_ADDR="${NACOS_SERVER_ADDR:-localhost:8848}"
export NACOS_NAMESPACE="${NACOS_NAMESPACE:-gxz-dev}"

# 项目配置
export PROJECT_ROOT="$(pwd)"
export CLAUDE_CODE_CONFIG_PATH="$(pwd)/.claude-code"

# Claude Code 特定配置
export CLAUDE_CODE_PROJECT_NAME="gongxiang-zhenxuan"
export CLAUDE_CODE_PROJECT_TYPE="java-microservices"

# PromptX 配置
export PROMPTX_WORKSPACE="$PROJECT_ROOT"
export PROMPTX_PROJECT_TYPE="java-microservices"

# Puppeteer 配置（macOS Chrome 路径）
export PUPPETEER_EXECUTABLE_PATH="/Applications/Google Chrome.app/Contents/MacOS/Google Chrome"

# 开发环境配置
export SPRING_PROFILES_ACTIVE="${SPRING_PROFILES_ACTIVE:-dev}"
export LOG_LEVEL="${LOG_LEVEL:-DEBUG}"

# 构建相关环境变量
export MAVEN_OPTS="-Xmx2048m -XX:ReservedCodeCacheSize=512m"
export JAVA_HOME="${JAVA_HOME:-$(/usr/libexec/java_home -v 1.8)}"

echo -e "${GREEN}✅ MCP 环境变量已加载${NC}"
echo ""
echo -e "${BLUE}📁 项目根目录:${NC} $PROJECT_ROOT"
echo -e "${BLUE}⚙️  配置目录:${NC} $CLAUDE_CODE_CONFIG_PATH"
echo -e "${BLUE}🗄️  数据库:${NC} $MYSQL_USERNAME@$MYSQL_HOST:$MYSQL_PORT/$MYSQL_DATABASE"
echo -e "${BLUE}📮 Redis:${NC} $REDIS_HOST:$REDIS_PORT"
echo -e "${BLUE}🔍 Nacos:${NC} $NACOS_SERVER_ADDR (namespace: $NACOS_NAMESPACE)"
echo -e "${BLUE}🌍 环境:${NC} $SPRING_PROFILES_ACTIVE"
echo ""

# 检查关键工具
echo -e "${BLUE}🛠️  工具检查:${NC}"

if command -v claude-code &> /dev/null; then
    echo -e "${GREEN}  ✅ Claude Code CLI: $(claude-code --version 2>/dev/null || echo 'installed')${NC}"
else
    echo -e "${YELLOW}  ⚠️  Claude Code CLI: 未安装${NC}"
fi

if command -v mvn &> /dev/null; then
    echo -e "${GREEN}  ✅ Maven: $(mvn --version | head -1 | cut -d' ' -f3)${NC}"
else
    echo -e "${YELLOW}  ⚠️  Maven: 未安装${NC}"
fi

if command -v java &> /dev/null; then
    echo -e "${GREEN}  ✅ Java: $(java -version 2>&1 | head -1 | cut -d'"' -f2)${NC}"
else
    echo -e "${YELLOW}  ⚠️  Java: 未安装${NC}"
fi

if command -v node &> /dev/null; then
    echo -e "${GREEN}  ✅ Node.js: $(node --version)${NC}"
else
    echo -e "${YELLOW}  ⚠️  Node.js: 未安装 (MCP 功能可能受限)${NC}"
fi

if command -v git &> /dev/null; then
    echo -e "${GREEN}  ✅ Git: $(git --version | cut -d' ' -f3)${NC}"
else
    echo -e "${YELLOW}  ⚠️  Git: 未安装${NC}"
fi

echo ""

# 检查数据库连接
echo -e "${BLUE}🔗 连接测试:${NC}"

# 测试 MySQL 连接（如果设置了密码）
if [ ! -z "$MYSQL_PASSWORD" ]; then
    if mysql -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u "$MYSQL_USERNAME" -p"$MYSQL_PASSWORD" -e "SELECT 1;" &>/dev/null; then
        echo -e "${GREEN}  ✅ MySQL 连接正常${NC}"
    else
        echo -e "${YELLOW}  ⚠️  MySQL 连接失败${NC}"
    fi
else
    echo -e "${YELLOW}  ⚠️  MySQL 密码未设置，跳过连接测试${NC}"
fi

# 测试 Redis 连接
if command -v redis-cli &> /dev/null; then
    if redis-cli -h "$REDIS_HOST" -p "$REDIS_PORT" ping &>/dev/null; then
        echo -e "${GREEN}  ✅ Redis 连接正常${NC}"
    else
        echo -e "${YELLOW}  ⚠️  Redis 连接失败${NC}"
    fi
else
    echo -e "${YELLOW}  ⚠️  redis-cli 未安装，跳过 Redis 连接测试${NC}"
fi

echo ""
echo -e "${GREEN}🎉 环境配置完成！现在可以使用 Claude Code 进行开发了${NC}"

# 提供快速命令提示
echo ""
echo -e "${BLUE}💡 快速开始命令:${NC}"
echo "   /build-all          # 构建所有服务"
echo "   /start-gateway      # 启动网关"
echo "   /test-all           # 运行所有测试"
echo "   /commands list      # 查看所有可用命令"
echo ""
echo -e "${BLUE}🧠 AI 增强工具:${NC}"
echo "   Sequential Thinking  # 复杂问题推理分析"
echo "   Puppeteer           # 浏览器自动化测试"
echo "   Context7            # 获取最新技术文档"
echo "   PromptX             # AI 专业角色能力"
