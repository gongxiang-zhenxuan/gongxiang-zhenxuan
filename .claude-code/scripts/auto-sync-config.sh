#!/bin/bash

# Claude Code 配置自动同步脚本
# 检测项目结构变化并自动更新配置文件

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

echo -e "${BLUE}🔄 Claude Code 配置自动同步${NC}"
echo "=================================="

# 检测项目变化
detect_changes() {
    local changes_detected=false
    
    echo -e "\n${YELLOW}🔍 检测项目结构变化...${NC}"
    
    # 1. 检测新的微服务
    echo "检查微服务目录..."
    local current_services=($(find "$PROJECT_ROOT" -maxdepth 1 -name "gxz-*" -type d | sort))
    local config_services=($(python3 -c "
import yaml
with open('$CONFIG_DIR/config.yml', 'r', encoding='utf-8') as f:
    config = yaml.safe_load(f)
    services = config.get('services', {})
    for service in services:
        if service != 'gateway':
            print('$PROJECT_ROOT/gxz-' + service)
" 2>/dev/null || echo))
    
    # 检查新增的服务
    for service_path in "${current_services[@]}"; do
        if [[ ! " ${config_services[@]} " =~ " ${service_path} " ]]; then
            local service_name=$(basename "$service_path")
            echo -e "${GREEN}  + 发现新服务: $service_name${NC}"
            add_service_to_config "$service_name"
            changes_detected=true
        fi
    done
    
    # 检查删除的服务
    for config_service in "${config_services[@]}"; do
        if [[ ! " ${current_services[@]} " =~ " ${config_service} " ]]; then
            local service_name=$(basename "$config_service")
            echo -e "${RED}  - 服务已删除: $service_name${NC}"
            remove_service_from_config "$service_name"
            changes_detected=true
        fi
    done
    
    # 2. 检测 pom.xml 变化
    echo "检查依赖变化..."
    if check_dependency_changes; then
        echo -e "${YELLOW}  ~ 依赖发生变化${NC}"
        changes_detected=true
    fi
    
    # 3. 检测端口配置变化
    echo "检查端口配置..."
    if check_port_changes; then
        echo -e "${YELLOW}  ~ 端口配置发生变化${NC}"
        changes_detected=true
    fi
    
    if [ "$changes_detected" = false ]; then
        echo -e "${GREEN}✅ 未检测到需要同步的变化${NC}"
    fi
    
    return $($changes_detected && echo 0 || echo 1)
}

# 添加新服务到配置
add_service_to_config() {
    local service_name="$1"
    local service_key="${service_name#gxz-}"  # 移除 gxz- 前缀
    
    echo -e "${BLUE}📝 添加服务 $service_name 到配置...${NC}"
    
    # 自动分配端口号
    local next_port=$(get_next_available_port)
    
    # 更新 config.yml
    python3 << EOF
import yaml
import sys

config_file = '$CONFIG_DIR/config.yml'
try:
    with open(config_file, 'r', encoding='utf-8') as f:
        config = yaml.safe_load(f)
    
    if 'services' not in config:
        config['services'] = {}
    
    config['services']['$service_key'] = {
        'name': '$service_name',
        'port': $next_port,
        'path': './$service_name'
    }
    
    with open(config_file, 'w', encoding='utf-8') as f:
        yaml.dump(config, f, default_flow_style=False, allow_unicode=True, indent=2)
    
    print(f"✅ 已添加服务 $service_name (端口: $next_port)")
except Exception as e:
    print(f"❌ 更新配置失败: {e}")
    sys.exit(1)
EOF

    # 更新自定义命令
    add_service_commands "$service_key" "$service_name"
}

# 从配置中移除服务
remove_service_from_config() {
    local service_name="$1"
    local service_key="${service_name#gxz-}"
    
    echo -e "${BLUE}🗑️  从配置中移除服务 $service_name...${NC}"
    
    python3 << EOF
import yaml

config_file = '$CONFIG_DIR/config.yml'
try:
    with open(config_file, 'r', encoding='utf-8') as f:
        config = yaml.safe_load(f)
    
    if 'services' in config and '$service_key' in config['services']:
        del config['services']['$service_key']
    
    with open(config_file, 'w', encoding='utf-8') as f:
        yaml.dump(config, f, default_flow_style=False, allow_unicode=True, indent=2)
    
    print(f"✅ 已移除服务 $service_name")
except Exception as e:
    print(f"❌ 更新配置失败: {e}")
EOF

    # 移除相关命令
    remove_service_commands "$service_key"
}

# 获取下一个可用端口
get_next_available_port() {
    local used_ports=($(python3 -c "
import yaml
with open('$CONFIG_DIR/config.yml', 'r', encoding='utf-8') as f:
    config = yaml.safe_load(f)
    services = config.get('services', {})
    for service in services.values():
        if 'port' in service:
            print(service['port'])
" | sort -n))
    
    local next_port=8088
    for port in "${used_ports[@]}"; do
        if [ "$port" -ge "$next_port" ]; then
            next_port=$((port + 1))
        fi
    done
    
    echo $next_port
}

# 检查依赖变化
check_dependency_changes() {
    if [ -f "$PROJECT_ROOT/pom.xml" ]; then
        # 检查是否有新的 Spring Boot 或其他重要依赖
        local new_deps=$(mvn dependency:list -q 2>/dev/null | grep -E "(spring-boot|mysql|redis|nacos)" | wc -l || echo "0")
        local config_timestamp=$(stat -f "%m" "$CONFIG_DIR/config.yml" 2>/dev/null || echo "0")
        local pom_timestamp=$(stat -f "%m" "$PROJECT_ROOT/pom.xml" 2>/dev/null || echo "0")
        
        if [ "$pom_timestamp" -gt "$config_timestamp" ]; then
            return 0  # 有变化
        fi
    fi
    return 1  # 无变化
}

# 检查端口配置变化
check_port_changes() {
    # 检查 application.yml 文件中的端口配置
    for service_dir in "$PROJECT_ROOT"/gxz-*; do
        if [ -d "$service_dir" ]; then
            local app_yml="$service_dir/src/main/resources/application.yml"
            if [ -f "$app_yml" ]; then
                local actual_port=$(grep -E "^\s*port:" "$app_yml" | head -1 | sed 's/.*port:\s*//' | tr -d ' ')
                if [ -n "$actual_port" ]; then
                    local service_name=$(basename "$service_dir")
                    local service_key="${service_name#gxz-}"
                    local config_port=$(python3 -c "
import yaml
with open('$CONFIG_DIR/config.yml', 'r') as f:
    config = yaml.safe_load(f)
    services = config.get('services', {})
    if '$service_key' in services and 'port' in services['$service_key']:
        print(services['$service_key']['port'])
" 2>/dev/null || echo "")
                    
                    if [ "$actual_port" != "$config_port" ]; then
                        echo -e "${YELLOW}  端口不匹配: $service_name (实际: $actual_port, 配置: $config_port)${NC}"
                        update_service_port "$service_key" "$actual_port"
                        return 0
                    fi
                fi
            fi
        fi
    done
    return 1
}

# 更新服务端口
update_service_port() {
    local service_key="$1"
    local new_port="$2"
    
    python3 << EOF
import yaml

config_file = '$CONFIG_DIR/config.yml'
with open(config_file, 'r', encoding='utf-8') as f:
    config = yaml.safe_load(f)

if 'services' in config and '$service_key' in config['services']:
    config['services']['$service_key']['port'] = int('$new_port')

with open(config_file, 'w', encoding='utf-8') as f:
    yaml.dump(config, f, default_flow_style=False, allow_unicode=True, indent=2)

print(f"✅ 已更新 $service_key 端口为 $new_port")
EOF
}

# 添加服务相关命令
add_service_commands() {
    local service_key="$1"
    local service_name="$2"
    
    # 这里可以添加自动生成服务特定命令的逻辑
    echo -e "${BLUE}📝 为 $service_name 生成相关命令...${NC}"
    # TODO: 实现命令自动生成
}

# 移除服务相关命令
remove_service_commands() {
    local service_key="$1"
    
    # 这里可以添加移除服务特定命令的逻辑
    echo -e "${BLUE}🗑️  移除 $service_key 相关命令...${NC}"
    # TODO: 实现命令自动移除
}

# 生成配置变更报告
generate_change_report() {
    echo -e "\n${PURPLE}📊 配置变更报告${NC}"
    echo "=================================="
    
    local report_file="$CONFIG_DIR/logs/config-changes-$(date +%Y%m%d-%H%M%S).log"
    mkdir -p "$(dirname "$report_file")"
    
    {
        echo "配置同步时间: $(date)"
        echo "项目路径: $PROJECT_ROOT"
        echo ""
        echo "检测到的变化:"
        # 这里记录具体的变化
    } > "$report_file"
    
    echo "变更报告已保存到: $report_file"
}

# 备份当前配置
backup_config() {
    local backup_dir="$CONFIG_DIR/backups/$(date +%Y%m%d-%H%M%S)"
    mkdir -p "$backup_dir"
    
    cp "$CONFIG_DIR"/*.yml "$backup_dir/" 2>/dev/null || true
    cp "$CONFIG_DIR"/*.json "$backup_dir/" 2>/dev/null || true
    
    echo -e "${GREEN}📦 配置已备份到: $backup_dir${NC}"
}

# 验证更新后的配置
validate_updated_config() {
    echo -e "\n${YELLOW}🔍 验证更新后的配置...${NC}"
    
    if [ -x "$CONFIG_DIR/scripts/validate-config.sh" ]; then
        "$CONFIG_DIR/scripts/validate-config.sh"
    else
        echo -e "${YELLOW}⚠️  验证脚本不存在，跳过验证${NC}"
    fi
}

# 主执行流程
main() {
    # 检查必要的依赖
    if ! command -v python3 &> /dev/null; then
        echo -e "${RED}❌ 需要安装 Python3${NC}"
        exit 1
    fi
    
    if ! python3 -c "import yaml" 2>/dev/null; then
        echo -e "${RED}❌ 需要安装 PyYAML: pip3 install PyYAML${NC}"
        exit 1
    fi
    
    # 备份当前配置
    backup_config
    
    # 检测并应用变化
    if detect_changes; then
        echo -e "\n${GREEN}🎉 配置同步完成！${NC}"
        
        # 生成变更报告
        generate_change_report
        
        # 验证配置
        validate_updated_config
        
        echo -e "\n${BLUE}💡 建议操作:${NC}"
        echo "1. 检查更新后的配置文件"
        echo "2. 运行 git diff 查看变更"
        echo "3. 测试新配置是否正常工作"
        echo "4. 提交配置变更到 Git"
    else
        echo -e "\n${GREEN}✅ 配置已是最新状态${NC}"
    fi
}

# 执行主函数
main "$@"
