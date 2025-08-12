#!/bin/bash

# Claude Code 自动提交脚本
# 需求完成后自动生成提交信息并提交代码

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m'

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$(dirname "$SCRIPT_DIR")")"
CONFIG_DIR="$PROJECT_ROOT/.claude-code"

echo -e "${BLUE}🤖 Claude Code 自动提交系统${NC}"
echo "=================================="

# 检测变更类型
detect_change_type() {
    local changes=$(git status --porcelain)
    local change_types=()
    
    if echo "$changes" | grep -q "^A.*\.java$"; then
        change_types+=("feat")
    fi
    
    if echo "$changes" | grep -q "^M.*\.java$"; then
        change_types+=("fix")
    fi
    
    if echo "$changes" | grep -q "^M.*test.*\.java$"; then
        change_types+=("test")
    fi
    
    if echo "$changes" | grep -q "^M.*\.md$\|^A.*\.md$"; then
        change_types+=("docs")
    fi
    
    if echo "$changes" | grep -q "^M.*pom\.xml$\|^M.*\.yml$\|^M.*\.properties$"; then
        change_types+=("config")
    fi
    
    if echo "$changes" | grep -q "^D"; then
        change_types+=("remove")
    fi
    
    # 默认类型
    if [ ${#change_types[@]} -eq 0 ]; then
        change_types+=("update")
    fi
    
    # 返回主要类型
    echo "${change_types[0]}"
}

# 分析变更文件
analyze_changes() {
    local changes=$(git status --porcelain)
    local modified_services=()
    local commit_scope=""
    local commit_description=""
    
    # 检测涉及的服务
    while IFS= read -r line; do
        if [[ $line =~ ^[AM][[:space:]]+gxz-([^/]+)/ ]]; then
            local service="${BASH_REMATCH[1]}"
            if [[ ! " ${modified_services[@]} " =~ " ${service} " ]]; then
                modified_services+=("$service")
            fi
        fi
    done <<< "$changes"
    
    # 确定提交范围
    if [ ${#modified_services[@]} -eq 1 ]; then
        commit_scope="${modified_services[0]}"
    elif [ ${#modified_services[@]} -gt 1 ] && [ ${#modified_services[@]} -le 3 ]; then
        commit_scope=$(IFS=','; echo "${modified_services[*]}")
    elif [ ${#modified_services[@]} -gt 3 ]; then
        commit_scope="multiple"
    else
        # 检查是否是根目录文件
        if echo "$changes" | grep -q "^[AM][[:space:]]+pom\.xml$"; then
            commit_scope="root"
        elif echo "$changes" | grep -q "^[AM][[:space:]]+\.claude-code/"; then
            commit_scope="config"
        else
            commit_scope="project"
        fi
    fi
    
    echo "$commit_scope"
}

# 生成智能提交消息
generate_commit_message() {
    local change_type="$1"
    local scope="$2"
    local user_description="$3"
    
    local commit_message=""
    local detailed_changes=""
    
    # 获取变更的详细信息
    local changes=$(git status --porcelain)
    local added_files=()
    local modified_files=()
    local deleted_files=()
    
    while IFS= read -r line; do
        local status="${line:0:2}"
        local file="${line:3}"
        
        case "$status" in
            "A "*)
                added_files+=("$file")
                ;;
            "M "*)
                modified_files+=("$file")
                ;;
            "D "*)
                deleted_files+=("$file")
                ;;
            "AM")
                added_files+=("$file")
                ;;
        esac
    done <<< "$changes"
    
    # 构建提交消息主题
    case "$change_type" in
        "feat")
            if [ -n "$user_description" ]; then
                commit_message="${change_type}(${scope}): ${user_description}"
            else
                commit_message="${change_type}(${scope}): 新增功能实现"
            fi
            ;;
        "fix")
            if [ -n "$user_description" ]; then
                commit_message="${change_type}(${scope}): ${user_description}"
            else
                commit_message="${change_type}(${scope}): 修复bug和问题"
            fi
            ;;
        "test")
            commit_message="${change_type}(${scope}): 添加和完善测试用例"
            ;;
        "docs")
            commit_message="${change_type}(${scope}): 更新文档"
            ;;
        "config")
            commit_message="${change_type}(${scope}): 配置文件更新"
            ;;
        "remove")
            commit_message="${change_type}(${scope}): 删除文件和代码"
            ;;
        *)
            if [ -n "$user_description" ]; then
                commit_message="${change_type}(${scope}): ${user_description}"
            else
                commit_message="${change_type}(${scope}): 代码更新"
            fi
            ;;
    esac
    
    # 构建详细描述
    if [ ${#added_files[@]} -gt 0 ]; then
        detailed_changes+="\n\n新增文件:"
        for file in "${added_files[@]:0:5}"; do  # 最多显示5个
            detailed_changes+="\n- $file"
        done
        if [ ${#added_files[@]} -gt 5 ]; then
            detailed_changes+="\n- ... 和其他 $((${#added_files[@]} - 5)) 个文件"
        fi
    fi
    
    if [ ${#modified_files[@]} -gt 0 ]; then
        detailed_changes+="\n\n修改文件:"
        for file in "${modified_files[@]:0:5}"; do
            detailed_changes+="\n- $file"
        done
        if [ ${#modified_files[@]} -gt 5 ]; then
            detailed_changes+="\n- ... 和其他 $((${#modified_files[@]} - 5)) 个文件"
        fi
    fi
    
    if [ ${#deleted_files[@]} -gt 0 ]; then
        detailed_changes+="\n\n删除文件:"
        for file in "${deleted_files[@]}"; do
            detailed_changes+="\n- $file"
        done
    fi
    
    # 添加统计信息
    local stats=$(git diff --cached --stat 2>/dev/null || git diff --stat)
    if [ -n "$stats" ]; then
        detailed_changes+="\n\n变更统计:\n$stats"
    fi
    
    echo -e "${commit_message}${detailed_changes}"
}

# 执行提交前检查
pre_commit_checks() {
    echo -e "${YELLOW}🔍 执行提交前检查...${NC}"
    
    # 检查是否有待提交的变更
    if ! git diff --cached --quiet 2>/dev/null && ! git diff --quiet; then
        # 有变更，继续检查
        :
    elif git diff --cached --quiet 2>/dev/null; then
        echo -e "${YELLOW}⚠️  没有待提交的变更${NC}"
        return 1
    fi
    
    # 检查 Java 编译
    if find . -name "*.java" -newer .git/COMMIT_EDITMSG 2>/dev/null | grep -q .; then
        echo "📋 检查 Java 代码编译..."
        if command -v mvn &> /dev/null; then
            if ! mvn compile -q &>/dev/null; then
                echo -e "${RED}❌ Java 代码编译失败，请修复后再提交${NC}"
                return 1
            fi
            echo -e "${GREEN}✅ Java 代码编译通过${NC}"
        fi
    fi
    
    # 检查 Claude Code 配置
    if git diff --cached --name-only | grep -q "^\.claude-code/"; then
        echo "📋 检查 Claude Code 配置..."
        if [ -x "$CONFIG_DIR/scripts/validate-config.sh" ]; then
            # 允许配置验证有警告但继续执行
            if "$CONFIG_DIR/scripts/validate-config.sh" 2>/dev/null || [ $? -eq 1 ]; then
                echo -e "${GREEN}✅ Claude Code 配置验证通过${NC}"
            else
                echo -e "${RED}❌ Claude Code 配置验证失败${NC}"
                return 1
            fi
        fi
    fi
    
    # 检查大文件
    local large_files=$(git diff --cached --name-only | xargs -I {} find {} -size +10M 2>/dev/null || true)
    if [ -n "$large_files" ]; then
        echo -e "${YELLOW}⚠️  检测到大文件 (>10MB):${NC}"
        echo "$large_files"
        read -p "是否继续提交? (y/N): " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            return 1
        fi
    fi
    
    echo -e "${GREEN}✅ 提交前检查通过${NC}"
    return 0
}

# 自动提交功能
auto_commit() {
    local description="$1"
    local auto_mode="$2"
    
    # 检查是否有变更
    if git diff --quiet && git diff --cached --quiet; then
        echo -e "${YELLOW}💡 没有检测到文件变更${NC}"
        return 0
    fi
    
    # 添加所有变更到暂存区
    echo -e "${BLUE}📋 添加文件到暂存区...${NC}"
    git add .
    
    # 执行提交前检查
    if ! pre_commit_checks; then
        echo -e "${RED}❌ 提交前检查失败，取消提交${NC}"
        return 1
    fi
    
    # 分析变更
    local change_type=$(detect_change_type)
    local scope=$(analyze_changes)
    
    echo -e "${BLUE}📊 变更分析:${NC}"
    echo "  类型: $change_type"
    echo "  范围: $scope"
    
    # 生成提交消息
    local commit_message=$(generate_commit_message "$change_type" "$scope" "$description")
    
    echo -e "\n${PURPLE}📝 生成的提交消息:${NC}"
    echo -e "${YELLOW}$commit_message${NC}"
    
    # 确认提交
    if [ "$auto_mode" != "silent" ]; then
        echo ""
        read -p "是否使用此提交消息? (Y/n): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Nn]$ ]]; then
            echo "请输入自定义提交消息:"
            read -r custom_message
            if [ -n "$custom_message" ]; then
                commit_message="$custom_message"
            fi
        fi
    fi
    
    # 执行提交
    echo -e "${BLUE}💾 执行提交...${NC}"
    if git commit -m "$commit_message"; then
        echo -e "${GREEN}✅ 提交成功!${NC}"
        
        # 显示提交信息
        echo -e "\n${BLUE}📋 提交详情:${NC}"
        git log -1 --stat
        
        # 询问是否推送
        if [ "$auto_mode" != "silent" ]; then
            echo ""
            read -p "是否推送到远程仓库? (y/N): " -n 1 -r
            echo
            if [[ $REPLY =~ ^[Yy]$ ]]; then
                push_to_remote
            fi
        fi
        
        return 0
    else
        echo -e "${RED}❌ 提交失败${NC}"
        return 1
    fi
}

# 推送到远程仓库
push_to_remote() {
    local current_branch=$(git branch --show-current)
    
    echo -e "${BLUE}🚀 推送到远程仓库...${NC}"
    
    if git push origin "$current_branch"; then
        echo -e "${GREEN}✅ 推送成功!${NC}"
        
        # 显示远程状态
        echo -e "${BLUE}🌐 远程状态:${NC}"
        git log origin/"$current_branch"..HEAD --oneline 2>/dev/null || echo "已与远程同步"
    else
        echo -e "${RED}❌ 推送失败${NC}"
        echo -e "${YELLOW}💡 提示: 可能需要先拉取远程变更: git pull${NC}"
    fi
}

# 监控模式 - 检测需求完成标记
monitor_completion() {
    local interval="${1:-30}"  # 默认30秒检查一次
    
    echo -e "${BLUE}👁️  启动需求完成监控 (检查间隔: ${interval}秒)${NC}"
    echo "监控以下完成标记:"
    echo "  • TODO -> DONE"
    echo "  • @TODO -> @DONE"
    echo "  • FIXME -> FIXED"
    echo "  • // TODO: xxx -> // DONE: xxx"
    echo ""
    echo "按 Ctrl+C 停止监控"
    
    local last_check_time=$(date +%s)
    
    while true; do
        # 检查最近修改的文件中是否有完成标记
        local recent_files=$(find . -name "*.java" -newermt "@$last_check_time" 2>/dev/null | grep -v target/ | head -20)
        
        if [ -n "$recent_files" ]; then
            local completion_found=false
            
            for file in $recent_files; do
                # 检查完成标记
                if grep -q -E "(TODO.*->.*DONE|@TODO.*->.*@DONE|FIXME.*->.*FIXED|//\s*TODO:.*->\s*//\s*DONE:)" "$file" 2>/dev/null; then
                    echo -e "${GREEN}🎯 检测到需求完成标记: $file${NC}"
                    completion_found=true
                fi
                
                # 检查新增的 DONE 标记
                if git diff HEAD~1 -- "$file" 2>/dev/null | grep -q "^+.*DONE"; then
                    echo -e "${GREEN}🎯 检测到新增 DONE 标记: $file${NC}"
                    completion_found=true
                fi
            done
            
            if [ "$completion_found" = true ]; then
                echo -e "${YELLOW}💡 检测到需求完成，是否自动提交? (y/N): ${NC}"
                read -t 10 -n 1 -r
                echo
                if [[ $REPLY =~ ^[Yy]$ ]]; then
                    auto_commit "完成需求开发" "auto"
                fi
            fi
        fi
        
        last_check_time=$(date +%s)
        sleep "$interval"
    done
}

# 显示帮助信息
show_help() {
    cat << EOF
Claude Code 自动提交系统

用法: $0 [选项] [描述]

选项:
  --commit, -c [描述]     立即执行自动提交
  --monitor, -m [间隔]    监控需求完成标记 (默认30秒间隔)
  --silent, -s [描述]     静默模式自动提交 (无交互确认)
  --push, -p             提交后自动推送到远程
  --check                 只执行提交前检查，不提交
  --help, -h             显示此帮助信息

示例:
  $0 --commit "完成用户登录功能"     # 手动触发提交
  $0 --monitor 60                  # 每60秒监控需求完成
  $0 --silent "自动同步配置"        # 静默提交
  $0 --push --commit "修复支付bug"  # 提交并推送

自动检测的提交类型:
  feat     - 新增功能 (新增 .java 文件)
  fix      - 修复问题 (修改现有 .java 文件)
  test     - 测试相关 (test/ 目录下文件)
  docs     - 文档更新 (.md 文件)
  config   - 配置变更 (pom.xml, .yml, .properties)

需求完成标记:
  在代码注释中使用以下格式标记需求完成:
  • TODO -> DONE
  • @TODO -> @DONE  
  • FIXME -> FIXED
  • // TODO: 实现用户登录 -> // DONE: 实现用户登录

EOF
}

# 主函数
main() {
    local auto_push=false
    local description=""
    local mode=""
    local interval=30
    
    # 解析参数
    while [[ $# -gt 0 ]]; do
        case $1 in
            --commit|-c)
                mode="commit"
                if [[ $# -gt 1 && ! $2 =~ ^-- ]]; then
                    description="$2"
                    shift
                fi
                shift
                ;;
            --monitor|-m)
                mode="monitor"
                if [[ $# -gt 1 && $2 =~ ^[0-9]+$ ]]; then
                    interval="$2"
                    shift
                fi
                shift
                ;;
            --silent|-s)
                mode="silent"
                if [[ $# -gt 1 && ! $2 =~ ^-- ]]; then
                    description="$2"
                    shift
                fi
                shift
                ;;
            --push|-p)
                auto_push=true
                shift
                ;;
            --check)
                mode="check"
                shift
                ;;
            --help|-h)
                show_help
                exit 0
                ;;
            *)
                if [ -z "$description" ]; then
                    description="$1"
                fi
                shift
                ;;
        esac
    done
    
    # 检查是否在 Git 仓库中
    if [ ! -d "$PROJECT_ROOT/.git" ]; then
        echo -e "${RED}❌ 错误: 当前目录不是 Git 仓库${NC}"
        exit 1
    fi
    
    # 执行对应操作
    case "$mode" in
        "commit")
            auto_commit "$description" "interactive"
            if [ $? -eq 0 ] && [ "$auto_push" = true ]; then
                push_to_remote
            fi
            ;;
        "silent")
            auto_commit "$description" "silent"
            if [ $? -eq 0 ] && [ "$auto_push" = true ]; then
                push_to_remote
            fi
            ;;
        "monitor")
            monitor_completion "$interval"
            ;;
        "check")
            git add .
            pre_commit_checks
            ;;
        *)
            show_help
            ;;
    esac
}

# 捕获 Ctrl+C 信号
trap 'echo -e "\n${YELLOW}⏹️  自动提交监控已停止${NC}"; exit 0' INT

# 执行主函数
main "$@"
