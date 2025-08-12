#!/bin/bash

# 团队配置同步脚本
# 帮助团队成员同步最新的 Claude Code 配置

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}🔄 Claude Code 团队配置同步${NC}"
echo "================================"

# 检查是否在 Git 仓库中
if ! git rev-parse --git-dir > /dev/null 2>&1; then
    echo -e "${RED}❌ 错误：当前目录不是 Git 仓库${NC}"
    exit 1
fi

# 检查是否有未提交的更改
if [ -n "$(git status --porcelain .claude-code/)" ]; then
    echo -e "${YELLOW}⚠️  检测到 .claude-code/ 目录有未提交的更改：${NC}"
    git status --porcelain .claude-code/
    echo ""
    read -p "是否要备份当前配置并继续同步？(y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        echo -e "${YELLOW}❌ 同步已取消${NC}"
        exit 1
    fi
    
    # 备份当前配置
    BACKUP_DIR=".claude-code-backup-$(date +%Y%m%d-%H%M%S)"
    echo -e "${BLUE}📦 备份当前配置到 $BACKUP_DIR${NC}"
    cp -r .claude-code "$BACKUP_DIR"
    echo -e "${GREEN}✅ 配置已备份${NC}"
fi

# 拉取最新更改
echo -e "${BLUE}📥 拉取最新配置...${NC}"
if git pull origin main; then
    echo -e "${GREEN}✅ 配置更新成功${NC}"
else
    echo -e "${RED}❌ 配置更新失败${NC}"
    exit 1
fi

# 验证配置
echo -e "\n${BLUE}🔍 验证配置...${NC}"
if [ -x ".claude-code/scripts/validate-config.sh" ]; then
    ./.claude-code/scripts/validate-config.sh
else
    echo -e "${YELLOW}⚠️  验证脚本不存在，跳过验证${NC}"
fi

# 重新加载环境变量
echo -e "\n${BLUE}🔧 重新加载环境变量...${NC}"
if [ -f ".claude-code/mcp-env.sh" ]; then
    source .claude-code/mcp-env.sh
else
    echo -e "${YELLOW}⚠️  环境变量脚本不存在${NC}"
fi

# 检查并安装新的依赖
echo -e "\n${BLUE}📦 检查依赖...${NC}"
if command -v npm &> /dev/null; then
    echo "检查 MCP 包更新..."
    npm list -g @anthropic/mcp-mysql @anthropic/mcp-git @anthropic/mcp-filesystem @anthropic/mcp-maven @anthropic/mcp-spring-boot 2>/dev/null || {
        echo "安装缺失的 MCP 包..."
        npm install -g @anthropic/mcp-mysql @anthropic/mcp-git @anthropic/mcp-filesystem @anthropic/mcp-maven @anthropic/mcp-spring-boot 2>/dev/null || echo "部分包安装失败"
    }
else
    echo -e "${YELLOW}⚠️  npm 不可用，跳过依赖检查${NC}"
fi

# 显示更新摘要
echo -e "\n${BLUE}📋 更新摘要${NC}"
echo "================================"

# 检查配置文件的最后修改时间
echo "最近更新的配置文件："
find .claude-code/ -name "*.yml" -o -name "*.json" -o -name "*.sh" | head -10 | while read file; do
    if [ -f "$file" ]; then
        echo "  📄 $file ($(stat -f "%Sm" -t "%Y-%m-%d %H:%M" "$file" 2>/dev/null || date))"
    fi
done

# 显示可用命令
echo -e "\n${GREEN}🎉 同步完成！${NC}"
echo -e "\n${BLUE}💡 可用的快速命令：${NC}"
echo "  /commands list      # 查看所有自定义命令"
echo "  /build-all          # 构建所有服务"
echo "  /start-gateway      # 启动网关"
echo "  /test-all           # 运行所有测试"
echo ""
echo "如有问题，请联系团队技术负责人或查看配置文档。"
