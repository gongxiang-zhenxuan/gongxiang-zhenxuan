#!/bin/bash

# Claude Code 提交助手
# 提供快捷的提交操作命令

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
WORKFLOW_SCRIPT="$SCRIPT_DIR/commit-workflow.sh"

# 颜色定义
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

# 确保工作流脚本可执行
if [ ! -x "$WORKFLOW_SCRIPT" ]; then
    chmod +x "$WORKFLOW_SCRIPT"
fi

echo -e "${BLUE}⚡ Claude Code 提交助手${NC}"

case "${1:-}" in
    "done"|"完成")
        echo -e "${GREEN}🎯 标记需求完成并提交${NC}"
        "$WORKFLOW_SCRIPT" --quick "完成需求: ${2:-开发任务}"
        ;;
    "fix"|"修复")
        echo -e "${YELLOW}🔧 修复问题并提交${NC}"
        "$WORKFLOW_SCRIPT" --quick "修复: ${2:-bug修复}"
        ;;
    "feat"|"功能")
        echo -e "${BLUE}🚀 新功能完成并提交${NC}"
        "$WORKFLOW_SCRIPT" --quick "新增功能: ${2:-功能开发}"
        ;;
    "test"|"测试")
        echo -e "${GREEN}🧪 测试完成并提交${NC}"
        "$WORKFLOW_SCRIPT" --quick "测试: ${2:-测试用例}"
        ;;
    "docs"|"文档")
        echo -e "${BLUE}📚 文档更新并提交${NC}"
        "$WORKFLOW_SCRIPT" --quick "文档: ${2:-文档更新}"
        ;;
    "sync"|"同步")
        echo -e "${BLUE}🔄 团队同步${NC}"
        "$WORKFLOW_SCRIPT" --sync
        ;;
    "status"|"状态")
        echo -e "${BLUE}📊 查看状态${NC}"
        "$WORKFLOW_SCRIPT" --status
        ;;
    "start"|"开始")
        echo -e "${BLUE}🚀 开始功能开发${NC}"
        "$WORKFLOW_SCRIPT" --feature "$2"
        ;;
    *)
        echo -e "${YELLOW}💡 Claude Code 快捷提交命令:${NC}"
        echo ""
        echo "  done [描述]     - 标记完成并提交"
        echo "  fix [描述]      - 修复问题并提交"
        echo "  feat [描述]     - 新功能并提交"
        echo "  test [描述]     - 测试相关提交"
        echo "  docs [描述]     - 文档更新提交"
        echo "  sync           - 团队同步"
        echo "  status         - 查看状态"
        echo "  start [功能名]  - 开始功能开发"
        echo ""
        echo "示例:"
        echo "  $0 done \"用户登录功能\""
        echo "  $0 fix \"支付金额计算错误\""
        echo "  $0 start \"订单管理\""
        ;;
esac
