#!/bin/bash

# Git Hooks 安装脚本
# 为项目安装 Git hooks 来自动触发配置同步

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$(dirname "$SCRIPT_DIR")")"
GIT_HOOKS_DIR="$PROJECT_ROOT/.git/hooks"

echo -e "${BLUE}🪝 安装 Claude Code Git Hooks${NC}"
echo "=================================="

# 检查是否在 Git 仓库中
if [ ! -d "$PROJECT_ROOT/.git" ]; then
    echo -e "${RED}❌ 错误: 当前目录不是 Git 仓库${NC}"
    exit 1
fi

# 创建 post-merge hook
create_post_merge_hook() {
    local hook_file="$GIT_HOOKS_DIR/post-merge"
    
    echo -e "${BLUE}📝 创建 post-merge hook...${NC}"
    
    cat > "$hook_file" << 'EOF'
#!/bin/bash

# Claude Code 配置自动同步 - post-merge hook
# 在 git pull/merge 后自动检查配置同步

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
CLAUDE_CODE_DIR="$PROJECT_ROOT/.claude-code"

# 检查是否存在 Claude Code 配置
if [ ! -d "$CLAUDE_CODE_DIR" ]; then
    exit 0
fi

echo "🔄 检查 Claude Code 配置同步..."

# 检查配置监控脚本是否存在
if [ -x "$CLAUDE_CODE_DIR/scripts/config-monitor.sh" ]; then
    # 执行单次检查
    "$CLAUDE_CODE_DIR/scripts/config-monitor.sh" --once
else
    echo "⚠️  Claude Code 配置监控脚本不存在，跳过自动同步"
fi

EOF
    
    chmod +x "$hook_file"
    echo -e "${GREEN}✅ post-merge hook 已创建${NC}"
}

# 创建 post-checkout hook
create_post_checkout_hook() {
    local hook_file="$GIT_HOOKS_DIR/post-checkout"
    
    echo -e "${BLUE}📝 创建 post-checkout hook...${NC}"
    
    cat > "$hook_file" << 'EOF'
#!/bin/bash

# Claude Code 配置自动同步 - post-checkout hook
# 在切换分支后自动检查配置同步

# 参数: $1 = previous HEAD, $2 = new HEAD, $3 = flag (1 for branch checkout, 0 for file checkout)

# 只在分支切换时执行
if [ "$3" = "1" ]; then
    SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
    PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
    CLAUDE_CODE_DIR="$PROJECT_ROOT/.claude-code"
    
    # 检查是否存在 Claude Code 配置
    if [ ! -d "$CLAUDE_CODE_DIR" ]; then
        exit 0
    fi
    
    echo "🔄 分支切换，检查 Claude Code 配置..."
    
    # 检查配置监控脚本是否存在
    if [ -x "$CLAUDE_CODE_DIR/scripts/config-monitor.sh" ]; then
        # 执行单次检查
        "$CLAUDE_CODE_DIR/scripts/config-monitor.sh" --once
    fi
fi

EOF
    
    chmod +x "$hook_file"
    echo -e "${GREEN}✅ post-checkout hook 已创建${NC}"
}

# 创建 pre-commit hook
create_pre_commit_hook() {
    local hook_file="$GIT_HOOKS_DIR/pre-commit"
    
    echo -e "${BLUE}📝 创建 pre-commit hook...${NC}"
    
    cat > "$hook_file" << 'EOF'
#!/bin/bash

# Claude Code 配置验证 - pre-commit hook
# 在提交前验证 Claude Code 配置

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
CLAUDE_CODE_DIR="$PROJECT_ROOT/.claude-code"

# 检查是否存在 Claude Code 配置
if [ ! -d "$CLAUDE_CODE_DIR" ]; then
    exit 0
fi

# 检查是否有 Claude Code 配置文件的修改
if git diff --cached --name-only | grep -q "^\.claude-code/"; then
    echo "🔍 检测到 Claude Code 配置变更，执行验证..."
    
    # 验证配置文件
    if [ -x "$CLAUDE_CODE_DIR/scripts/validate-config.sh" ]; then
        if ! "$CLAUDE_CODE_DIR/scripts/validate-config.sh"; then
            echo "❌ Claude Code 配置验证失败，请修复后再提交"
            exit 1
        fi
        echo "✅ Claude Code 配置验证通过"
    fi
fi

EOF
    
    chmod +x "$hook_file"
    echo -e "${GREEN}✅ pre-commit hook 已创建${NC}"
}

# 备份现有的 hooks
backup_existing_hooks() {
    echo -e "${BLUE}📦 备份现有 Git hooks...${NC}"
    
    local backup_dir="$GIT_HOOKS_DIR/backup-$(date +%Y%m%d-%H%M%S)"
    mkdir -p "$backup_dir"
    
    for hook in post-merge post-checkout pre-commit; do
        if [ -f "$GIT_HOOKS_DIR/$hook" ]; then
            cp "$GIT_HOOKS_DIR/$hook" "$backup_dir/"
            echo "  📄 备份 $hook"
        fi
    done
    
    if [ "$(ls -A "$backup_dir" 2>/dev/null)" ]; then
        echo -e "${GREEN}✅ hooks 已备份到: $backup_dir${NC}"
    else
        rmdir "$backup_dir"
        echo -e "${YELLOW}ℹ️  没有现有的 hooks 需要备份${NC}"
    fi
}

# 卸载 hooks
uninstall_hooks() {
    echo -e "${BLUE}🗑️  卸载 Claude Code Git hooks...${NC}"
    
    for hook in post-merge post-checkout pre-commit; do
        local hook_file="$GIT_HOOKS_DIR/$hook"
        if [ -f "$hook_file" ] && grep -q "Claude Code" "$hook_file"; then
            rm "$hook_file"
            echo -e "${GREEN}✅ 已移除 $hook hook${NC}"
        fi
    done
}

# 显示 hooks 状态
show_hooks_status() {
    echo -e "${BLUE}📊 Git Hooks 状态${NC}"
    echo "=================================="
    
    for hook in post-merge post-checkout pre-commit; do
        local hook_file="$GIT_HOOKS_DIR/$hook"
        printf "%-15s" "$hook:"
        
        if [ -f "$hook_file" ]; then
            if [ -x "$hook_file" ]; then
                if grep -q "Claude Code" "$hook_file"; then
                    echo -e "${GREEN}✅ 已安装 (Claude Code)${NC}"
                else
                    echo -e "${YELLOW}⚠️  存在但非 Claude Code${NC}"
                fi
            else
                echo -e "${RED}❌ 存在但不可执行${NC}"
            fi
        else
            echo -e "${YELLOW}➖ 未安装${NC}"
        fi
    done
}

# 显示帮助信息
show_help() {
    cat << EOF
Claude Code Git Hooks 管理工具

用法: $0 [选项]

选项:
  --install, -i           安装 Git hooks
  --uninstall, -u         卸载 Git hooks
  --status, -s            显示 hooks 状态
  --help, -h              显示此帮助信息

安装的 Hooks:
  post-merge             在 git pull/merge 后自动检查配置同步
  post-checkout          在切换分支后自动检查配置同步
  pre-commit             在提交前验证 Claude Code 配置

示例:
  $0 --install            # 安装所有 hooks
  $0 --status             # 查看 hooks 状态
  $0 --uninstall          # 卸载 Claude Code hooks

EOF
}

# 主函数
main() {
    case "${1:-}" in
        --install|-i)
            backup_existing_hooks
            create_post_merge_hook
            create_post_checkout_hook
            create_pre_commit_hook
            echo -e "\n${GREEN}🎉 Git hooks 安装完成！${NC}"
            echo -e "${BLUE}💡 现在以下操作会自动触发配置检查:${NC}"
            echo "  • git pull"
            echo "  • git merge"
            echo "  • git checkout <branch>"
            echo "  • git commit (验证配置)"
            ;;
        --uninstall|-u)
            uninstall_hooks
            echo -e "${GREEN}✅ Git hooks 已卸载${NC}"
            ;;
        --status|-s)
            show_hooks_status
            ;;
        --help|-h|"")
            show_help
            ;;
        *)
            echo -e "${RED}❌ 未知选项: $1${NC}"
            echo "使用 --help 查看帮助信息"
            exit 1
            ;;
    esac
}

# 执行主函数
main "$@"
