import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  base: './', // 设置为相对路径
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  build: {
    outDir: 'dist',
    assetsDir: 'assets',
    // 为JavaFX WebView优化构建配置
    target: ['es2015'], // 使用较旧的ES版本以提高兼容性
    rollupOptions: {
      output: {
        entryFileNames: 'assets/index.js',
        chunkFileNames: 'assets/[name].js',
        assetFileNames: 'assets/[name].[ext]',
        // 禁用代码分割以减少模块化问题
        manualChunks: undefined,
        // 使用UMD格式以提高JavaFX WebView兼容性
        format: 'umd',
        // 确保全局变量名
        name: 'VueApp',
      }
    },
    // 禁用模块预加载以提高兼容性
    polyfillModulePreload: false,
    // 禁用模块预加载
    modulePreload: false,
  },
  // 优化依赖构建以提高兼容性
  optimizeDeps: {
    esbuildOptions: {
      target: 'es2015',
    }
  }
})