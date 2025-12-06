@echo off
REM JavaFX应用程序Windows安装包构建脚本

echo 开始构建JavaFX应用程序Windows安装包...

REM 项目基本信息
set APP_NAME=JavaFXApp
set APP_VERSION=1.0
set MAIN_CLASS=top.jiuxialb.javafx.SpringBootJavaFXApplication
set MAIN_JAR=javafx-1.0.jar

REM 创建输出目录
if not exist "target\installers" mkdir target\installers

REM 清理之前的构建
call mvn clean

REM 构建项目
echo 正在构建项目...
call mvn package

if %errorlevel% neq 0 (
    echo 项目构建失败，退出。
    exit /b 1
)

echo 项目构建成功!

REM 检查jpackage工具是否存在
where jpackage >nul 2>&1
if %errorlevel% neq 0 (
    echo jpackage工具未找到，请确保已安装JDK 16+
    exit /b 1
)

REM 构建Windows安装包
echo 正在构建Windows安装包...
jpackage ^
    --input target/ ^
    --name "%APP_NAME%" ^
    --app-version "%APP_VERSION%" ^
    --main-class "%MAIN_CLASS%" ^
    --main-jar "%MAIN_JAR%" ^
    --type exe ^
    --dest target/installers/windows/ ^
    --icon src/main/resources/icons/app.ico

if %errorlevel% equ 0 (
    echo Windows安装包构建成功: target/installers/windows/
) else (
    echo Windows安装包构建失败
    echo 请确保已安装JDK 16+并且jpackage工具可用
)

echo 安装包构建完成!