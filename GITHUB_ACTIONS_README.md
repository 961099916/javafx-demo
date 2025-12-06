# GitHub Actions 自动化构建说明

## 工作流概述

本项目配置了GitHub Actions自动化工作流，用于构建多平台安装包并自动发布到GitHub Releases。

### 触发条件

1. 推送到`main`或`master`分支
2. 创建以`v`开头的标签（如`v1.0.0`）
3. 提交PR到`main`或`master`分支

## 工作流步骤

### 1. 构建和测试 (Build and Test)
- 在Ubuntu上运行
- 检出代码
- 设置JDK 21环境
- 编译项目
- 运行测试

### 2. 多平台安装包构建
并行运行三个作业，分别在对应的平台上构建安装包：

#### Windows安装包构建
- 在Windows最新版上运行
- 使用`-Pwindows` Maven Profile构建
- 生成`.exe`安装包
- 上传为artifact

#### Linux安装包构建
- 在Ubuntu最新版上运行
- 使用`-Plinux` Maven Profile构建
- 生成`.deb`安装包
- 上传为artifact

#### macOS Intel安装包构建
- 在macOS Intel版上运行
- 使用`-Pmac-intel` Maven Profile构建
- 生成`.dmg`安装包
- 上传为artifact

#### macOS ARM安装包构建
- 在macOS ARM版( Apple Silicon)上运行
- 使用`-Pmac-arm` Maven Profile构建
- 生成`.dmg`安装包
- 上传为artifact

### 3. 创建GitHub Release
仅在推送标签时触发：
- 创建GitHub Release
- 下载所有平台的安装包
- 上传到Release中

## 如何使用

### 1. 创建新版本发布
```bash
# 创建并推送新标签
git tag v1.0.0
git push origin v1.0.0
```

### 2. 手动触发工作流
1. 访问GitHub仓库的Actions页面
2. 选择"Build and Release Multi-platform Installers"工作流
3. 点击"Run workflow"按钮

## 本地构建测试

### 构建特定平台安装包
```bash
# 构建Windows安装包
mvn clean package -Pwindows

# 构建Linux安装包
mvn clean package -Plinux

# 构建macOS Intel安装包
mvn clean package -Pmac-intel

# 构建macOS ARM安装包
mvn clean package -Pmac-arm
```

## 配置说明

### Maven Profiles
项目中配置了四个Maven Profile：
- `windows`: Windows平台配置
- `linux`: Linux平台配置
- `mac-intel`: macOS Intel平台配置
- `mac-arm`: macOS ARM平台配置

每个Profile都会自动激活对应的平台，并配置相应的jpackage参数。

### 环境变量
- `APP_NAME`: 应用程序名称（默认: JavaFXApp）

## 故障排除

### 构建失败
1. 检查日志中的错误信息
2. 确认代码能否在本地正常编译
3. 检查GitHub Actions配额限制

### 安装包未生成
1. 确认是否在正确的平台上运行
2. 检查jpackage命令是否正确执行
3. 查看构建日志中的详细信息

## 注意事项

1. jpackage工具只能在对应的平台上生成相应类型的安装包
2. 发布仅在推送标签时触发
3. 构建产物会保留7天用于调试