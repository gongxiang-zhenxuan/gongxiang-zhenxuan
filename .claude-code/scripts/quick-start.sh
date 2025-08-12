#!/bin/bash

# Claude Code 快速启动脚本
# 为新加入团队的成员提供快速配置体验

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m'

clear

echo -e "${PURPLE}"
cat << "EOF"
   ____                       __  __   ____          _      
  / ___| ___  _ __   __ __    / / / /  |  _ \   ___  | |     
 | |  _ / __|| '_ \  \ \ /   / / / /   | | | | / _ \ | |     
 | |_| |\__ \| | | |  \ V \  \ \ \ \   | |_| || |_||| |     
  \____||___/|_| |_|   \_/    \_\ \_\  |____/  \___/|_|     
                                                            
     贡享臻选 Claude Code 快速启动指南
EOF
echo -e "${NC}"

echo -e "${BLUE}🚀 欢迎使用贡享臻选项目的 Claude Code 配置！${NC}"
echo ""

# 步骤 1: 环境检查
echo -e "${YELLOW}步骤 1/5: 环境检查${NC}"
echo "==============================="

check_tool() {
    local tool="$1"
    local install_hint="$2"
    
    if command -v "$tool" &> /dev/null; then
        echo -e "✅ $tool: ${GREEN}已安装${NC}"
        return 0
    else
        echo -e "❌ $tool: ${RED}未安装${NC} - $install_hint"
        return 1
    fi
}

MISSING_TOOLS=0

check_tool "claude-code" "brew install anthropic/tap/claude-code" || ((MISSING_TOOLS++))
check_tool "java" "安装 JDK 8" || ((MISSING_TOOLS++))
check_tool "mvn" "安装 Maven" || ((MISSING_TOOLS++))
check_tool "git" "安装 Git" || ((MISSING_TOOLS++))
check_tool "node" "安装 Node.js (可选，用于 MCP)" || true  # Node.js 是可选的

if [ $MISSING_TOOLS -gt 0 ]; then
    echo -e "\n${RED}⚠️  请先安装缺失的工具，然后重新运行此脚本${NC}"
    exit 1
fi

echo -e "\n${GREEN}✅ 环境检查通过！${NC}"

# 步骤 2: 配置加载
echo -e "\n${YELLOW}步骤 2/5: 加载配置${NC}"
echo "==============================="

if [ -f ".claude-code/mcp-env.sh" ]; then
    echo "🔧 加载环境变量..."
    source .claude-code/mcp-env.sh
    echo -e "${GREEN}✅ 环境变量加载完成${NC}"
else
    echo -e "${RED}❌ 环境变量脚本不存在${NC}"
    exit 1
fi

# 步骤 3: 验证配置
echo -e "\n${YELLOW}步骤 3/5: 验证配置${NC}"
echo "==============================="

if [ -x ".claude-code/scripts/validate-config.sh" ]; then
    echo "🔍 运行配置验证..."
    if ./.claude-code/scripts/validate-config.sh; then
        echo -e "${GREEN}✅ 配置验证通过${NC}"
    else
        echo -e "${RED}❌ 配置验证失败，请检查配置文件${NC}"
        exit 1
    fi
else
    echo -e "${YELLOW}⚠️  验证脚本不存在，跳过验证${NC}"
fi

# 步骤 4: 快速测试
echo -e "\n${YELLOW}步骤 4/5: 快速功能测试${NC}"
echo "==============================="

echo "📋 测试自定义命令..."
if claude-code --help &> /dev/null; then
    echo -e "✅ Claude Code CLI: ${GREEN}工作正常${NC}"
else
    echo -e "❌ Claude Code CLI: ${RED}无法使用${NC}"
    exit 1
fi

echo "🏗️  测试项目构建..."
if mvn compile -q &> /dev/null; then
    echo -e "✅ Maven 构建: ${GREEN}成功${NC}"
else
    echo -e "❌ Maven 构建: ${RED}失败${NC}"
    echo -e "${YELLOW}提示: 可能需要先安装依赖或修复编译错误${NC}"
fi

# 步骤 5: 使用指南
echo -e "\n${YELLOW}步骤 5/5: 使用指南${NC}"
echo "==============================="

echo -e "\n${GREEN}🎉 配置完成！您现在可以开始使用 Claude Code 了${NC}"

echo -e "\n${BLUE}💡 常用命令:${NC}"
echo "  /commands list      # 查看所有可用命令"
echo "  /build-all          # 构建所有微服务"
echo "  /start-gateway      # 启动网关服务"
echo "  /test-all           # 运行所有测试"
echo "  /code-quality       # 代码质量检查"

echo -e "\n${BLUE}🔧 服务管理:${NC}"
echo "  /start-gateway      # 启动网关"
echo "  /gateway-logs       # 查看网关日志"
echo "  /build-service      # 构建指定服务"
echo "  /test-service       # 测试指定服务"

echo -e "\n${BLUE}📊 开发辅助:${NC}"
echo "  /generate-crud      # 生成 CRUD 代码"
echo "  /git-status-all     # 查看所有服务 Git 状态"
echo "  /clean-all          # 清理构建产物"
echo "  /switch-env         # 切换环境"

echo -e "\n${BLUE}📚 学习资源:${NC}"
echo "  📖 完整指南: Claude-Code-最佳实践指南.md"
echo "  📁 配置文件: .claude-code/ 目录"
echo "  🔧 团队配置: .claude-code/README.md"

echo -e "\n${PURPLE}🤝 团队协作:${NC}"
echo "  • 所有配置都在项目中，团队成员自动同步"
echo "  • 有问题可以运行: ./.claude-code/scripts/validate-config.sh"
echo "  • 配置更新时运行: ./.claude-code/scripts/team-sync.sh"

echo -e "\n${GREEN}✨ 现在您可以开始高效开发了！${NC}"
echo ""

# 交互式演示
read -p "是否要查看可用命令列表？(y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo -e "\n${BLUE}📋 可用的自定义命令:${NC}"
    if [ -f ".claude-code/commands.yml" ] && command -v python3 &> /dev/null; then
        python3 -c "
import yaml
with open('.claude-code/commands.yml', 'r', encoding='utf-8') as f:
    commands = yaml.safe_load(f)
    for name, config in commands['commands'].items():
        print(f'  /{name:<20} - {config.get(\"description\", \"无描述\")}')
" 2>/dev/null || echo "命令列表加载失败"
    else
        echo "  命令配置文件不存在或 Python3 未安装"
    fi
fi

echo -e "\n${GREEN}🎊 祝您开发愉快！${NC}"
