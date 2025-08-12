#!/bin/bash

# Claude Code 提交工作流脚本
# 集成开发工作流，提供便捷的提交操作

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m'

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$(dirname "$SCRIPT_DIR")")"
CONFIG_DIR="$PROJECT_ROOT/.claude-code"
AUTO_COMMIT_SCRIPT="$SCRIPT_DIR/auto-commit.sh"

echo -e "${CYAN}🔄 Claude Code 提交工作流${NC}"
echo "=================================="

# 加载配置
load_config() {
    if [ -f "$CONFIG_DIR/auto-commit-config.yml" ]; then
        # 这里可以添加配置解析逻辑
        return 0
    fi
    return 1
}

# 显示当前状态
show_status() {
    echo -e "${BLUE}📊 当前项目状态${NC}"
    echo "=================================="
    
    # Git 状态
    echo -e "${YELLOW}📋 Git 状态:${NC}"
    git status --short | head -10
    if [ $(git status --porcelain | wc -l) -gt 10 ]; then
        echo "... 和其他 $(($(git status --porcelain | wc -l) - 10)) 个文件"
    fi
    
    # 分支信息
    echo -e "\n${YELLOW}🌿 分支信息:${NC}"
    echo "当前分支: $(git branch --show-current)"
    
    # 最近提交
    echo -e "\n${YELLOW}📝 最近提交:${NC}"
    git log --oneline -3
    
    # 待推送提交
    local unpushed=$(git log origin/$(git branch --show-current)..HEAD --oneline 2>/dev/null | wc -l || echo "0")
    if [ "$unpushed" -gt 0 ]; then
        echo -e "\n${YELLOW}🚀 待推送提交: ${unpushed} 个${NC}"
    fi
}

# 需求开发工作流
feature_workflow() {
    local feature_name="$1"
    
    if [ -z "$feature_name" ]; then
        echo "请输入需求/功能名称:"
        read -r feature_name
    fi
    
    echo -e "${BLUE}🚀 开始需求开发工作流: $feature_name${NC}"
    
    # 1. 创建功能分支 (如果不存在)
    local current_branch=$(git branch --show-current)
    local feature_branch="feature/${feature_name//[^a-zA-Z0-9]/-}"
    
    if [ "$current_branch" != "$feature_branch" ]; then
        echo -e "${YELLOW}🌿 切换到功能分支: $feature_branch${NC}"
        
        if git show-ref --verify --quiet refs/heads/"$feature_branch"; then
            git checkout "$feature_branch"
        else
            git checkout -b "$feature_branch"
            echo -e "${GREEN}✅ 创建新功能分支: $feature_branch${NC}"
        fi
    fi
    
    # 2. 启动自动提交监控
    echo -e "${BLUE}👁️  启动需求完成监控...${NC}"
    echo "在代码中使用以下标记来标识需求完成:"
    echo "  • // TODO: 实现用户登录 -> // DONE: 实现用户登录"
    echo "  • @TODO 添加支付接口 -> @DONE 添加支付接口"
    echo ""
    echo "监控将在后台运行，检测到完成标记时会提示自动提交"
    
    # 启动监控 (后台运行)
    nohup "$AUTO_COMMIT_SCRIPT" --monitor 30 > "$CONFIG_DIR/logs/monitor-$feature_name.log" 2>&1 &
    local monitor_pid=$!
    echo "$monitor_pid" > "$CONFIG_DIR/.monitor_pid"
    
    echo -e "${GREEN}✅ 需求监控已启动 (PID: $monitor_pid)${NC}"
    echo -e "${YELLOW}💡 提示: 使用 '$0 --stop-monitor' 停止监控${NC}"
}

# 停止监控
stop_monitor() {
    if [ -f "$CONFIG_DIR/.monitor_pid" ]; then
        local pid=$(cat "$CONFIG_DIR/.monitor_pid")
        if ps -p "$pid" > /dev/null 2>&1; then
            kill "$pid"
            echo -e "${GREEN}✅ 需求监控已停止 (PID: $pid)${NC}"
        else
            echo -e "${YELLOW}⚠️  监控进程不存在${NC}"
        fi
        rm -f "$CONFIG_DIR/.monitor_pid"
    else
        echo -e "${YELLOW}⚠️  没有运行中的监控进程${NC}"
    fi
}

# 快速提交工作流
quick_commit() {
    local commit_msg="$1"
    
    echo -e "${BLUE}⚡ 快速提交工作流${NC}"
    
    # 显示变更概览
    echo -e "${YELLOW}📋 变更概览:${NC}"
    git status --short
    
    if [ -z "$commit_msg" ]; then
        echo ""
        echo "请输入提交描述 (可选，留空将自动生成):"
        read -r commit_msg
    fi
    
    # 执行自动提交
    "$AUTO_COMMIT_SCRIPT" --commit "$commit_msg"
}

# 发布工作流
release_workflow() {
    local version="$1"
    
    echo -e "${PURPLE}🚢 发布工作流${NC}"
    
    if [ -z "$version" ]; then
        echo "请输入版本号 (如: v1.2.0):"
        read -r version
    fi
    
    local current_branch=$(git branch --show-current)
    
    # 1. 确保在主分支
    if [ "$current_branch" != "main" ] && [ "$current_branch" != "master" ]; then
        echo -e "${YELLOW}⚠️  当前不在主分支，是否切换到 main? (y/N): ${NC}"
        read -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            git checkout main
            git pull origin main
        else
            echo -e "${RED}❌ 发布工作流取消${NC}"
            return 1
        fi
    fi
    
    # 2. 检查是否有未提交的变更
    if ! git diff --quiet || ! git diff --cached --quiet; then
        echo -e "${YELLOW}📋 检测到未提交的变更，是否提交? (y/N): ${NC}"
        read -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            quick_commit "准备发布 $version"
        else
            echo -e "${RED}❌ 请先提交或暂存变更${NC}"
            return 1
        fi
    fi
    
    # 3. 创建发布提交
    echo -e "${BLUE}📝 创建发布提交...${NC}"
    
    # 更新版本号 (如果有版本文件)
    if [ -f "pom.xml" ]; then
        echo "更新 Maven 版本号..."
        # 这里可以添加版本号更新逻辑
    fi
    
    # 提交版本变更
    git add .
    git commit -m "release: 发布版本 $version

版本发布说明:
- 完成功能开发和测试
- 更新版本号到 $version
- 准备生产部署

Release: $version" || echo "没有版本文件需要更新"
    
    # 4. 创建标签
    echo -e "${BLUE}🏷️  创建版本标签...${NC}"
    git tag -a "$version" -m "Release $version"
    
    # 5. 推送到远程
    echo -e "${BLUE}🚀 推送到远程仓库...${NC}"
    git push origin main
    git push origin "$version"
    
    echo -e "${GREEN}✅ 版本 $version 发布完成!${NC}"
}

# 团队同步工作流
team_sync() {
    echo -e "${CYAN}👥 团队同步工作流${NC}"
    
    local current_branch=$(git branch --show-current)
    
    # 1. 保存当前工作
    if ! git diff --quiet || ! git diff --cached --quiet; then
        echo -e "${YELLOW}📦 暂存当前工作...${NC}"
        git stash push -m "auto-stash before team sync"
        local stashed=true
    fi
    
    # 2. 拉取最新变更
    echo -e "${BLUE}📥 拉取最新变更...${NC}"
    git pull origin "$current_branch"
    
    # 3. 同步 Claude Code 配置
    echo -e "${BLUE}🔄 同步 Claude Code 配置...${NC}"
    if [ -x "$CONFIG_DIR/scripts/team-sync.sh" ]; then
        "$CONFIG_DIR/scripts/team-sync.sh"
    fi
    
    # 4. 恢复工作
    if [ "$stashed" = true ]; then
        echo -e "${YELLOW}📂 恢复工作状态...${NC}"
        git stash pop
    fi
    
    echo -e "${GREEN}✅ 团队同步完成!${NC}"
}

# 显示帮助信息
show_help() {
    cat << EOF
Claude Code 提交工作流工具

用法: $0 [选项] [参数]

工作流命令:
  --status, -s               显示项目当前状态
  --feature, -f [名称]       启动需求开发工作流
  --quick, -q [描述]         快速提交工作流
  --release, -r [版本]       发布工作流
  --sync                     团队同步工作流
  --stop-monitor             停止需求监控

直接提交命令:
  --commit [描述]            执行自动提交
  --silent [描述]            静默提交 (无交互)
  --push                     提交后自动推送

示例:
  $0 --status                        # 查看项目状态
  $0 --feature "用户登录功能"         # 开始功能开发
  $0 --quick "修复支付bug"            # 快速提交
  $0 --release "v1.2.0"              # 发布版本
  $0 --sync                          # 团队同步

需求开发工作流:
  1. 自动创建/切换功能分支
  2. 启动代码监控，检测完成标记
  3. 自动提示提交已完成的功能
  4. 支持多人协作和冲突解决

完成标记示例:
  // TODO: 实现用户注册接口 -> // DONE: 实现用户注册接口
  @TODO 添加订单状态检查 -> @DONE 添加订单状态检查
  FIXME: 修复支付金额计算 -> FIXED: 修复支付金额计算

EOF
}

# 主函数
main() {
    # 检查是否在 Git 仓库中
    if [ ! -d "$PROJECT_ROOT/.git" ]; then
        echo -e "${RED}❌ 错误: 当前目录不是 Git 仓库${NC}"
        exit 1
    fi
    
    # 确保自动提交脚本可执行
    if [ ! -x "$AUTO_COMMIT_SCRIPT" ]; then
        chmod +x "$AUTO_COMMIT_SCRIPT"
    fi
    
    case "${1:-}" in
        --status|-s)
            show_status
            ;;
        --feature|-f)
            feature_workflow "$2"
            ;;
        --quick|-q)
            quick_commit "$2"
            ;;
        --release|-r)
            release_workflow "$2"
            ;;
        --sync)
            team_sync
            ;;
        --stop-monitor)
            stop_monitor
            ;;
        --commit)
            "$AUTO_COMMIT_SCRIPT" --commit "$2"
            ;;
        --silent)
            "$AUTO_COMMIT_SCRIPT" --silent "$2"
            ;;
        --push)
            "$AUTO_COMMIT_SCRIPT" --commit "$2" --push
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

# 捕获 Ctrl+C 信号
trap 'echo -e "\n${YELLOW}⏹️  工作流已中断${NC}"; exit 0' INT

# 执行主函数
main "$@"
