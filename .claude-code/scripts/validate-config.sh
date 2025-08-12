#!/bin/bash

# Claude Code 配置验证脚本
# 用于验证团队配置是否正确

# set -e  # 临时注释掉，允许脚本继续执行

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$(dirname "$SCRIPT_DIR")")"

echo -e "${BLUE}🔍 Claude Code 配置验证${NC}"
echo "=============================="

# 验证计数器
PASSED=0
FAILED=0

# 验证函数
validate() {
    local test_name="$1"
    local test_command="$2"
    
    printf "%-50s" "$test_name"
    
    if eval "$test_command" &>/dev/null; then
        echo -e "${GREEN}✅ 通过${NC}"
        ((PASSED++))
    else
        echo -e "${RED}❌ 失败${NC}"
        ((FAILED++))
    fi
}

# 1. 验证配置文件存在性
echo -e "\n${YELLOW}📁 配置文件检查${NC}"
validate "config.yml 存在" "[ -f '$PROJECT_ROOT/.claude-code/config.yml' ]"
validate "mcp.json 存在" "[ -f '$PROJECT_ROOT/.claude-code/mcp.json' ]"
validate "env.yml 存在" "[ -f '$PROJECT_ROOT/.claude-code/env.yml' ]"
validate "commands.yml 存在" "[ -f '$PROJECT_ROOT/.claude-code/commands.yml' ]"
validate "security.yml 存在" "[ -f '$PROJECT_ROOT/.claude-code/security.yml' ]"
validate "tdd.yml 存在" "[ -f '$PROJECT_ROOT/.claude-code/tdd.yml' ]"

# 2. 验证配置文件格式
echo -e "\n${YELLOW}📋 配置文件格式检查${NC}"
if command -v python3 &> /dev/null; then
    validate "config.yml 格式正确" "python3 -c 'import yaml; yaml.safe_load(open(\"$PROJECT_ROOT/.claude-code/config.yml\"))'"
    validate "env.yml 格式正确" "python3 -c 'import yaml; yaml.safe_load(open(\"$PROJECT_ROOT/.claude-code/env.yml\"))'"
    validate "commands.yml 格式正确" "python3 -c 'import yaml; yaml.safe_load(open(\"$PROJECT_ROOT/.claude-code/commands.yml\"))'"
    validate "security.yml 格式正确" "python3 -c 'import yaml; yaml.safe_load(open(\"$PROJECT_ROOT/.claude-code/security.yml\"))'"
    validate "tdd.yml 格式正确" "python3 -c 'import yaml; yaml.safe_load(open(\"$PROJECT_ROOT/.claude-code/tdd.yml\"))'"
else
    echo -e "${YELLOW}⚠️  Python3 未安装，跳过 YAML 格式验证${NC}"
fi

if command -v node &> /dev/null; then
    validate "mcp.json 格式正确" "node -e 'JSON.parse(require(\"fs\").readFileSync(\"$PROJECT_ROOT/.claude-code/mcp.json\"))'"
else
    echo -e "${YELLOW}⚠️  Node.js 未安装，跳过 JSON 格式验证${NC}"
fi

# 3. 验证目录结构
echo -e "\n${YELLOW}📁 目录结构检查${NC}"
validate "templates 目录存在" "[ -d '$PROJECT_ROOT/.claude-code/templates' ]"
validate "scripts 目录存在" "[ -d '$PROJECT_ROOT/.claude-code/scripts' ]"
validate "logs 目录存在" "[ -d '$PROJECT_ROOT/.claude-code/logs' ]"

# 4. 验证脚本权限
echo -e "\n${YELLOW}🔐 脚本权限检查${NC}"
validate "setup.sh 可执行" "[ -x '$PROJECT_ROOT/.claude-code/setup.sh' ]"
validate "mcp-env.sh 可执行" "[ -x '$PROJECT_ROOT/.claude-code/mcp-env.sh' ]"

# 5. 验证环境工具
echo -e "\n${YELLOW}🛠️  环境工具检查${NC}"
validate "Claude Code CLI 可用" "command -v claude-code"
validate "Maven 可用" "command -v mvn"
validate "Java 可用" "command -v java"
validate "Git 可用" "command -v git"

# 6. 验证项目结构
echo -e "\n${YELLOW}🏗️  项目结构检查${NC}"
validate "项目根目录正确" "[ -f '$PROJECT_ROOT/pom.xml' ]"
validate "网关服务存在" "[ -d '$PROJECT_ROOT/gxz-gateway' ]"
validate "用户服务存在" "[ -d '$PROJECT_ROOT/gxz-user' ]"
validate "订单服务存在" "[ -d '$PROJECT_ROOT/gxz-order' ]"

# 7. 验证配置内容
echo -e "\n${YELLOW}⚙️  配置内容检查${NC}"
if command -v python3 &> /dev/null; then
    validate "项目名称配置正确" "python3 -c '
import yaml
config = yaml.safe_load(open(\"$PROJECT_ROOT/.claude-code/config.yml\"))
assert config[\"project\"][\"name\"] == \"贡享臻选 (GongXiang ZhenXuan)\"
'"
    
    validate "服务配置完整" "python3 -c '
import yaml
config = yaml.safe_load(open(\"$PROJECT_ROOT/.claude-code/config.yml\"))
services = config[\"services\"]
required_services = [\"gateway\", \"user\", \"merchant\", \"order\", \"payment\", \"delivery\", \"goods\", \"admin\"]
for service in required_services:
    assert service in services
'"
fi

# 8. 生成报告
echo -e "\n${BLUE}📊 验证报告${NC}"
echo "=============================="
echo -e "✅ 通过: ${GREEN}$PASSED${NC}"
echo -e "❌ 失败: ${RED}$FAILED${NC}"
echo -e "📊 总计: $((PASSED + FAILED))"

if [ $FAILED -eq 0 ]; then
    echo -e "\n${GREEN}🎉 所有验证都通过了！Claude Code 配置正确。${NC}"
    exit 0
else
    echo -e "\n${RED}⚠️  有 $FAILED 项验证失败，请检查配置。${NC}"
    exit 1
fi
