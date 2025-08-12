#!/bin/bash

# Claude Code 团队配置一键设置脚本
# 适用于贡享臻选项目团队成员

set -e  # 遇到错误时退出

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查命令是否存在
check_command() {
    if ! command -v $1 &> /dev/null; then
        log_error "$1 命令未找到，请先安装"
        return 1
    fi
    return 0
}

# 主函数
main() {
    log_info "🚀 开始设置 Claude Code 团队配置..."
    
    # 1. 检查必要工具
    log_info "📋 检查必要工具..."
    
    if ! check_command "claude-code"; then
        log_error "Claude Code CLI 未安装，请先安装：brew install anthropic/tap/claude-code"
        exit 1
    fi
    
    if ! check_command "node"; then
        log_warning "Node.js 未安装，部分 MCP 功能可能无法使用"
    fi
    
    if ! check_command "git"; then
        log_error "Git 未安装，请先安装 Git"
        exit 1
    fi
    
    log_success "必要工具检查完成"
    
    # 2. 创建配置目录结构
    log_info "📁 创建配置目录结构..."
    
    mkdir -p .claude-code/{config,templates,scripts,logs}
    
    # 3. 检查配置文件
    log_info "⚙️ 检查配置文件..."
    
    config_files=(
        "config.yml"
        "mcp.json"
        "env.yml"
        "commands.yml"
        "security.yml"
        "tdd.yml"
        "yolo-monitor.yml"
    )
    
    for file in "${config_files[@]}"; do
        if [[ ! -f ".claude-code/$file" ]]; then
            log_warning "配置文件 $file 不存在，请从团队指南中复制配置内容"
        else
            log_success "配置文件 $file 存在"
        fi
    done
    
    # 4. 设置环境变量
    log_info "🔧 设置环境变量..."
    
    if [[ -f ".claude-code/mcp-env.sh" ]]; then
        source .claude-code/mcp-env.sh
        log_success "MCP 环境变量已加载"
    else
        log_warning "mcp-env.sh 文件不存在"
    fi
    
    # 5. 验证 Claude Code 配置
    log_info "✅ 验证 Claude Code 配置..."
    
    if claude-code --version &> /dev/null; then
        log_success "Claude Code CLI 工作正常"
    else
        log_error "Claude Code CLI 配置有问题"
        exit 1
    fi
    
    # 6. 安装 MCP 依赖
    log_info "📦 安装 MCP 依赖..."
    
    if command -v npm &> /dev/null; then
        log_info "安装基础 MCP 服务器包..."
        npm install -g @anthropic/mcp-mysql @anthropic/mcp-git @anthropic/mcp-filesystem @anthropic/mcp-maven @anthropic/mcp-spring-boot 2>/dev/null || log_warning "部分基础 MCP 包安装失败"
        
        log_info "安装高级 MCP 服务器包..."
        npm install -g @anthropic/mcp-sequential-thinking @anthropic/mcp-puppeteer @anthropic/mcp-context7 @anthropic/mcp-promptx 2>/dev/null || log_warning "部分高级 MCP 包安装失败"
        
        log_success "MCP 依赖安装完成"
    else
        log_warning "npm 未安装，跳过 MCP 依赖安装"
    fi
    
    # 7. 创建 .gitignore
    log_info "📝 创建 .gitignore..."
    
    if [[ ! -f ".claude-code/.gitignore" ]]; then
        cat > .claude-code/.gitignore << 'EOF'
# 忽略敏感信息
mcp-env.local.sh
*.log
logs/
*.tmp

# 忽略个人配置
personal-config.yml
user-preferences.yml

# 保留团队配置
!config.yml
!mcp.json
!env.yml
!commands.yml
!security.yml
!tdd.yml
!yolo-monitor.yml
!templates/
!scripts/
EOF
        log_success "创建 .gitignore 文件"
    else
        log_info ".gitignore 文件已存在"
    fi
    
    # 8. 设置权限
    log_info "🔐 设置文件权限..."
    
    chmod +x .claude-code/*.sh 2>/dev/null || true
    
    # 9. 验证设置
    log_info "🧪 验证设置..."
    
    # 检查项目根目录
    if [[ -f "pom.xml" ]] && [[ -d "gxz-gateway" ]]; then
        log_success "项目根目录验证通过"
    else
        log_warning "请确认在贡享臻选项目根目录执行此脚本"
    fi
    
    # 10. 完成设置
    log_success "🎉 Claude Code 团队配置设置完成！"
    
    echo ""
    echo "📚 下一步："
    echo "1. 确认所有配置文件内容正确"
    echo "2. 测试基本功能：claude-code --help"
    echo "3. 查看自定义命令：/commands list"
    echo "4. 开始使用 Claude Code 进行开发"
    echo ""
    echo "📖 详细使用方法请参考：Claude-Code-最佳实践指南.md"
}

# 执行主函数
main "$@"
