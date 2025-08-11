#!/bin/bash

# 网关服务启动脚本
# Author: gongxiang-zhenxuan
# Version: 1.0.0

# 设置环境变量
export JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"
export SPRING_PROFILES_ACTIVE="dev"

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
APP_NAME="gxz-gateway"
JAR_FILE="target/${APP_NAME}-1.0.0-SNAPSHOT.jar"
PID_FILE="${SCRIPT_DIR}/${APP_NAME}.pid"
LOG_FILE="${SCRIPT_DIR}/logs/${APP_NAME}.log"

# 创建日志目录
mkdir -p "${SCRIPT_DIR}/logs"

# 检查Java环境
if [ -z "$JAVA_HOME" ]; then
    JAVA_CMD="java"
else
    JAVA_CMD="$JAVA_HOME/bin/java"
fi

# 检查Java版本
JAVA_VERSION=$($JAVA_CMD -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F '.' '{print $1}')
if [ "$JAVA_VERSION" -lt 8 ]; then
    echo "Error: Java 8 or higher is required"
    exit 1
fi

# 启动函数
start() {
    if [ -f "$PID_FILE" ]; then
        PID=$(cat "$PID_FILE")
        if ps -p "$PID" > /dev/null 2>&1; then
            echo "$APP_NAME is already running (PID: $PID)"
            return 1
        else
            rm -f "$PID_FILE"
        fi
    fi
    
    echo "Starting $APP_NAME..."
    
    # 检查JAR文件是否存在
    if [ ! -f "$JAR_FILE" ]; then
        echo "Error: JAR file not found: $JAR_FILE"
        echo "Please run 'mvn clean package' first"
        exit 1
    fi
    
    # 启动应用
    nohup $JAVA_CMD $JAVA_OPTS -jar "$JAR_FILE" > "$LOG_FILE" 2>&1 &
    PID=$!
    echo $PID > "$PID_FILE"
    
    # 等待启动
    sleep 3
    if ps -p "$PID" > /dev/null 2>&1; then
        echo "$APP_NAME started successfully (PID: $PID)"
        echo "Log file: $LOG_FILE"
    else
        echo "Failed to start $APP_NAME"
        rm -f "$PID_FILE"
        exit 1
    fi
}

# 停止函数
stop() {
    if [ ! -f "$PID_FILE" ]; then
        echo "$APP_NAME is not running"
        return 1
    fi
    
    PID=$(cat "$PID_FILE")
    if ! ps -p "$PID" > /dev/null 2>&1; then
        echo "$APP_NAME is not running"
        rm -f "$PID_FILE"
        return 1
    fi
    
    echo "Stopping $APP_NAME (PID: $PID)..."
    kill "$PID"
    
    # 等待进程结束
    for i in {1..30}; do
        if ! ps -p "$PID" > /dev/null 2>&1; then
            echo "$APP_NAME stopped successfully"
            rm -f "$PID_FILE"
            return 0
        fi
        sleep 1
    done
    
    # 强制杀死进程
    echo "Force killing $APP_NAME..."
    kill -9 "$PID"
    rm -f "$PID_FILE"
    echo "$APP_NAME stopped"
}

# 重启函数
restart() {
    stop
    sleep 2
    start
}

# 状态检查函数
status() {
    if [ ! -f "$PID_FILE" ]; then
        echo "$APP_NAME is not running"
        return 1
    fi
    
    PID=$(cat "$PID_FILE")
    if ps -p "$PID" > /dev/null 2>&1; then
        echo "$APP_NAME is running (PID: $PID)"
        return 0
    else
        echo "$APP_NAME is not running"
        rm -f "$PID_FILE"
        return 1
    fi
}

# 查看日志函数
logs() {
    if [ -f "$LOG_FILE" ]; then
        tail -f "$LOG_FILE"
    else
        echo "Log file not found: $LOG_FILE"
    fi
}

# 主逻辑
case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        restart
        ;;
    status)
        status
        ;;
    logs)
        logs
        ;;
    *)
        echo "Usage: $0 {start|stop|restart|status|logs}"
        exit 1
        ;;
esac

exit $?