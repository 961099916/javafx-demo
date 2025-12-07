const fs = require('fs');
const path = require('path');

// 定义源目录和目标目录
const sourceDir = path.resolve(__dirname, 'dist');
const targetDir = path.resolve(__dirname, '../application/src/main/resources/web');

// 创建目标目录的函数
function createDir(dir) {
  if (!fs.existsSync(dir)) {
    fs.mkdirSync(dir, { recursive: true });
  }
}

// 复制文件的函数
function copyFile(src, dest) {
  fs.copyFileSync(src, dest);
}

// 递归复制目录的函数
function copyDir(src, dest) {
  createDir(dest);
  
  const entries = fs.readdirSync(src, { withFileTypes: true });
  
  for (const entry of entries) {
    const srcPath = path.join(src, entry.name);
    const destPath = path.join(dest, entry.name);
    
    if (entry.isDirectory()) {
      copyDir(srcPath, destPath);
    } else {
      copyFile(srcPath, destPath);
    }
  }
}

// 执行复制操作
try {
  console.log('Copying frontend build to Java resources...');
  copyDir(sourceDir, targetDir);
  console.log('Frontend build copied to Java resources successfully!');
} catch (error) {
  console.error('Error copying frontend build:', error);
  process.exit(1);
}