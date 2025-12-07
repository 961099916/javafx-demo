# JavaFX + Vue.js 集成项目

本项目演示了如何将Vue.js前端应用集成到JavaFX桌面应用程序中。

## 项目结构

- `front/` - Vue.js前端项目 (Vue 3 + Vite)
- `application/` - JavaFX桌面应用程序 (Spring Boot + JavaFX)

## 功能特性

1. Vue.js前端使用Vue 3和推荐组件构建
2. 前端构建产物自动复制到JavaFX应用的资源目录中
3. JavaFX通过WebView组件加载和显示Vue.js应用
4. 使用Maven插件自动处理前端构建和资源复制

## 构建和运行

### 构建项目

```bash
# 进入application目录
cd application

# 清理并编译项目（会自动构建前端并复制资源）
mvn clean compile
```

### 运行应用

```bash
# 运行JavaFX应用
mvn javafx:run
```

## 开发流程

1. 在`front/`目录中开发Vue.js应用
2. 运行`npm run build-and-copy`构建前端并复制到JavaFX资源目录
3. 在JavaFX应用中通过Vue WebView查看效果

## 自定义配置

### 前端构建配置

前端构建配置位于`front/vite.config.ts`，包含：
- Vue 3支持
- Vue DevTools集成
- 构建优化配置

### 资源复制脚本

资源复制脚本`front/copy-to-java-resources.cjs`负责将构建产物复制到JavaFX资源目录。

### Maven集成

Maven配置位于`application/pom.xml`，使用frontend-maven-plugin自动处理：
1. Node.js和npm安装
2. 前端依赖安装
3. 前端构建和资源复制

## 使用说明

在JavaFX应用的首页中，点击"Vue视图"按钮即可打开集成的Vue.js应用。