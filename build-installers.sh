#!/bin/bash

# JavaFX应用程序多平台安装包构建脚本
# 支持Windows、Linux、Mac平台

echo "开始构建JavaFX应用程序安装包..."

# 项目基本信息
APP_NAME="JavaFXApp"
APP_VERSION="1.0"
MAIN_CLASS="org.springframework.boot.loader.launch.JarLauncher"
MAIN_JAR="javafx-1.0.jar"

# 创建输出目录
mkdir -p target/installers

# 清理之前的构建
mvn clean

# 构建项目
echo "正在构建项目..."
mvn package

if [ $? -ne 0 ]; then
    echo "项目构建失败，退出。"
    exit 1
fi

echo "项目构建成功!"

# 检查Java版本
JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | sed '/^1\./s///' | cut -d'.' -f1)
echo "当前Java版本: $JAVA_VERSION"

# 检查是否支持jpackage (需要Java 16+)
if [ "$JAVA_VERSION" -lt "16" ]; then
    echo "错误: jpackage工具需要Java 16或更高版本，当前版本为Java $JAVA_VERSION"
    echo "请安装JDK 16+并设置JAVA_HOME环境变量"
    echo "您可以从以下链接下载:"
    echo "  - Oracle JDK: https://www.oracle.com/java/technologies/downloads/"
    echo "  - OpenJDK: https://adoptium.net/"
    echo "  - Azul Zulu: https://www.azul.com/downloads/?package=jdk"
    exit 1
fi

# 检查jpackage工具是否存在
if ! command -v jpackage &> /dev/null
then
    echo "jpackage工具未找到，请确保已安装JDK 16+并且已正确配置PATH环境变量"
    exit 1
fi

# 构建Windows安装包
build_windows() {
    # 检查是否在Windows上运行
    if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" || "$OSTYPE" == "win32" ]]; then
        echo "正在构建Windows安装包..."
        jpackage \
            --input target/ \
            --name "$APP_NAME" \
            --app-version "$APP_VERSION" \
            --main-class "$MAIN_CLASS" \
            --main-jar "$MAIN_JAR" \
            --type exe \
            --dest target/installers/windows/
        
        if [ $? -eq 0 ]; then
            echo "Windows安装包构建成功: target/installers/windows/"
        else
            echo "Windows安装包构建失败"
        fi
    else
        echo "警告：无法在非Windows系统上构建Windows安装包"
        echo "请在Windows系统上运行此脚本以构建Windows安装包"
    fi
}

# 构建Linux安装包
build_linux() {
    # 检查是否在Linux上运行
    if [[ "$OSTYPE" == "linux-gnu"* ]]; then
        echo "正在构建Linux安装包..."
        jpackage \
            --input target/ \
            --name "$APP_NAME" \
            --app-version "$APP_VERSION" \
            --main-class "$MAIN_CLASS" \
            --main-jar "$MAIN_JAR" \
            --type deb \
            --dest target/installers/linux/
        
        if [ $? -eq 0 ]; then
            echo "Linux安装包构建成功: target/installers/linux/"
        else
            echo "Linux安装包构建失败"
        fi
    else
        echo "警告：无法在非Linux系统上构建Linux安装包"
        echo "请在Linux系统上运行此脚本以构建Linux安装包"
    fi
}

# 构建Mac安装包 (支持Apple Silicon)
build_mac() {
    # 检查是否在macOS上运行
    if [[ "$OSTYPE" == "darwin"* ]]; then
        echo "正在构建Mac安装包..."
        jpackage \
            --input target \
            --name "$APP_NAME" \
            --app-version "$APP_VERSION" \
            --main-class "$MAIN_CLASS" \
            --main-jar "$(basename target/*.jar)" \
            --type dmg \
            --dest target/installers/mac/ \
            --java-options "-Djava.awt.headless=true -Dprism.order=sw -Dprism.forceGPU=true -Dprism.allowhidpi=true" \
            --mac-package-identifier "top.jiuxialb.javafx" \
            --mac-package-name "JavaFXApp" \
            --verbose
        
        if [ $? -eq 0 ]; then
            echo "Mac安装包构建成功: target/installers/mac/"
        else
            echo "Mac安装包构建失败"
        fi
    else
        echo "警告：无法在非macOS系统上构建Mac安装包"
        echo "请在macOS系统上运行此脚本以构建Mac安装包"
    fi
}

# 根据参数构建指定平台的安装包
case "${1:-all}" in
    windows)
        build_windows
        ;;
    linux)
        build_linux
        ;;
    mac)
        build_mac
        ;;
    all)
        build_windows
        build_linux
        build_mac
        ;;
    *)
        echo "用法: $0 [windows|linux|mac|all]"
        echo "默认构建所有平台的安装包"
        exit 1
        ;;
esac

echo "安装包构建完成!"