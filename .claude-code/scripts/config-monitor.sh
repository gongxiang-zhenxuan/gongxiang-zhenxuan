#!/bin/bash

# Claude Code 配置监控脚本
# 监控项目变化并在需要时自动触发配置同步

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$(dirname "$SCRIPT_DIR")")"
CONFIG_DIR="$PROJECT_ROOT/.claude-code"
MONITOR_FILE="$CONFIG_DIR/logs/monitor.log"

# 创建监控日志目录
mkdir -p "$(dirname "$MONITOR_FILE")"

# 记录监控日志
log_monitor() {
    echo "$(date '+%Y-%m-%d %H:%M:%S') - $1" >> "$MONITOR_FILE"
}

# 获取项目文件的校验和
get_project_checksum() {
    # 监控关键文件和目录
    local files_to_monitor=(
        "$PROJECT_ROOT/pom.xml"
        "$PROJECT_ROOT"/gxz-*/pom.xml
        "$PROJECT_ROOT"/gxz-*/src/main/resources/application*.yml
        "$PROJECT_ROOT"/gxz-*/src/main/resources/application*.properties
    )
    
    local checksum=""
    for file_pattern in "${files_to_monitor[@]}"; do
        if ls $file_pattern 1> /dev/null 2>&1; then
            for file in $file_pattern; do
                if [ -f "$file" ]; then
                    checksum="$checksum$(stat -f "%m" "$file" 2>/dev/null || echo "0")"
                fi
            done
        fi
    done
    
    # 也监控目录结构变化
    local dir_structure=$(find "$PROJECT_ROOT" -maxdepth 1 -name "gxz-*" -type d | sort | tr '\n' '|')
    checksum="$checksum$dir_structure"
    
    echo "$checksum" | shasum -a 256 | cut -d' ' -f1
}

# 检查是否需要同步
needs_sync() {
    local current_checksum=$(get_project_checksum)
    local stored_checksum=""
    
    if [ -f "$CONFIG_DIR/.last_sync_checksum" ]; then
        stored_checksum=$(cat "$CONFIG_DIR/.last_sync_checksum")
    fi
    
    if [ "$current_checksum" != "$stored_checksum" ]; then
        echo "$current_checksum" > "$CONFIG_DIR/.last_sync_checksum"
        return 0  # 需要同步
    fi
    
    return 1  # 不需要同步
}

# 执行配置同步
trigger_sync() {
    log_monitor "触发配置自动同步"
    echo -e "${YELLOW}🔄 检测到项目变化，触发配置同步...${NC}"
    
    if [ -x "$SCRIPT_DIR/auto-sync-config.sh" ]; then
        "$SCRIPT_DIR/auto-sync-config.sh"
        log_monitor "配置同步完成"
        return $?
    else
        echo -e "${RED}❌ 自动同步脚本不存在${NC}"
        log_monitor "错误: 自动同步脚本不存在"
        return 1
    fi
}

# 监控模式：持续监控
monitor_continuous() {
    local interval=${1:-60}  # 默认60秒检查一次
    
    echo -e "${BLUE}🔍 启动配置监控 (检查间隔: ${interval}秒)${NC}"
    echo "按 Ctrl+C 停止监控"
    
    log_monitor "启动持续监控模式，间隔: ${interval}秒"
    
    while true; do
        if needs_sync; then
            trigger_sync
        fi
        sleep "$interval"
    done
}

# 监控模式：单次检查
monitor_once() {
    echo -e "${BLUE}🔍 执行单次配置检查...${NC}"
    log_monitor "执行单次检查"
    
    if needs_sync; then
        trigger_sync
    else
        echo -e "${GREEN}✅ 配置无需更新${NC}"
        log_monitor "配置无需更新"
    fi
}

# 显示监控状态
show_status() {
    echo -e "${BLUE}📊 配置监控状态${NC}"
    echo "=================================="
    
    # 显示最后检查时间
    if [ -f "$CONFIG_DIR/.last_sync_checksum" ]; then
        local last_check=$(stat -f "%Sm" -t "%Y-%m-%d %H:%M:%S" "$CONFIG_DIR/.last_sync_checksum" 2>/dev/null || echo "未知")
        echo "最后检查时间: $last_check"
    else
        echo "最后检查时间: 从未检查"
    fi
    
    # 显示监控的文件
    echo ""
    echo "监控的关键文件:"
    echo "  • 根目录 pom.xml"
    echo "  • 各微服务 pom.xml"
    echo "  • 应用配置文件 (application*.yml/properties)"
    echo "  • 微服务目录结构"
    
    # 显示最近的监控日志
    if [ -f "$MONITOR_FILE" ]; then
        echo ""
        echo "最近的监控日志 (最近10条):"
        tail -10 "$MONITOR_FILE" | while read line; do
            echo "  $line"
        done
    fi
}

# 清理监控数据
cleanup_monitor() {
    echo -e "${YELLOW}🧹 清理监控数据...${NC}"
    
    rm -f "$CONFIG_DIR/.last_sync_checksum"
    rm -f "$MONITOR_FILE"
    
    echo -e "${GREEN}✅ 监控数据已清理${NC}"
    log_monitor "监控数据已清理"
}

# 设置自动监控（通过 cron）
setup_auto_monitor() {
    local interval=${1:-300}  # 默认5分钟
    
    echo -e "${BLUE}⚙️  设置自动监控 (每${interval}秒检查一次)...${NC}"
    
    # 创建监控守护脚本
    cat > "$CONFIG_DIR/scripts/monitor-daemon.sh" << EOF
#!/bin/bash
# Claude Code 配置监控守护进程
cd "$PROJECT_ROOT"
"$SCRIPT_DIR/config-monitor.sh" --continuous $interval
EOF
    
    chmod +x "$CONFIG_DIR/scripts/monitor-daemon.sh"
    
    echo -e "${GREEN}✅ 监控守护脚本已创建${NC}"
    echo -e "${YELLOW}💡 提示: 您可以使用以下命令启动后台监控:${NC}"
    echo "    nohup $CONFIG_DIR/scripts/monitor-daemon.sh > $CONFIG_DIR/logs/monitor-daemon.log 2>&1 &"
}

# 显示帮助信息
show_help() {
    cat << EOF
Claude Code 配置监控工具

用法: $0 [选项]

选项:
  --once, -o              执行单次检查
  --continuous, -c [间隔]  持续监控模式 (默认60秒间隔)
  --status, -s            显示监控状态
  --cleanup               清理监控数据
  --setup-auto [间隔]     设置自动监控 (默认300秒间隔)
  --help, -h              显示此帮助信息

示例:
  $0 --once                    # 单次检查
  $0 --continuous 30           # 每30秒检查一次
  $0 --status                  # 查看状态
  $0 --setup-auto 600          # 设置每10分钟自动检查

监控的变化:
  • 新增/删除微服务目录
  • pom.xml 文件修改
  • 应用配置文件变化
  • 端口配置变更

EOF
}

# 主函数
main() {
    case "${1:-}" in
        --once|-o)
            monitor_once
            ;;
        --continuous|-c)
            local interval=${2:-60}
            monitor_continuous "$interval"
            ;;
        --status|-s)
            show_status
            ;;
        --cleanup)
            cleanup_monitor
            ;;
        --setup-auto)
            local interval=${2:-300}
            setup_auto_monitor "$interval"
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
trap 'echo -e "\n${YELLOW}⏹️  监控已停止${NC}"; log_monitor "监控已停止"; exit 0' INT

# 执行主函数
main "$@"
