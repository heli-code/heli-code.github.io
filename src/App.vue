<template>
  <div id="app">
    <!-- 应用头部 -->
    <AppHeader />
    
    <!-- 主内容区域 -->
    <main class="main-content">
      <RouterView />
    </main>
    
    <!-- 音频播放器 -->
    <AudioPlayer />
    
    <!-- 悬浮AI助手 -->
    <FloatingAI />
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import AppHeader from '@/components/AppHeader.vue'
import AudioPlayer from '@/components/AudioPlayer.vue'
import FloatingAI from '@/components/FloatingAI.vue'

const userStore = useUserStore()

// 应用初始化
onMounted(() => {
  // 初始化用户认证状态
  userStore.checkAuthStatus()
  
  // 初始化用户偏好设置
  userStore.initializeFromStorage()
  
  // 监听系统主题变化
  watchSystemTheme()
})

// 监听系统主题变化
const watchSystemTheme = () => {
  const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)')
  const handleThemeChange = (e: MediaQueryListEvent) => {
    document.documentElement.setAttribute('data-theme', e.matches ? 'dark' : 'light')
  }
  
  mediaQuery.addEventListener('change', handleThemeChange)
  
  // 初始设置
  document.documentElement.setAttribute('data-theme', 
    mediaQuery.matches ? 'dark' : 'light'
  )
}
</script>

<style scoped>
#app {
  height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.main-content {
  flex: 1;
  overflow-y: auto;
  padding: 0;
  position: relative;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main-content {
    padding-bottom: 80px; /* 为移动端播放器留出空间 */
  }
}
</style>

<style>
/* 全局样式 */
:root {
  --primary-color: #4cc9f0;
  --secondary-color: #f72585;
  --background-color: #0f1419;
  --surface-color: rgba(255, 255, 255, 0.1);
  --text-color: #ffffff;
  --text-secondary: rgba(255, 255, 255, 0.7);
  --border-color: rgba(255, 255, 255, 0.1);
  --shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

[data-theme="light"] {
  --background-color: #f5f5f5;
  --surface-color: rgba(255, 255, 255, 0.9);
  --text-color: #333333;
  --text-secondary: rgba(0, 0, 0, 0.7);
  --border-color: rgba(0, 0, 0, 0.1);
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  height: 100%;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background: var(--background-color);
  color: var(--text-color);
  overflow: hidden;
}

#app {
  height: 100vh;
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: rgba(76, 201, 240, 0.5);
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: rgba(76, 201, 240, 0.7);
}

/* 通用动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.slide-up-enter-active,
.slide-up-leave-active {
  transition: transform 0.3s ease, opacity 0.3s ease;
}

.slide-up-enter-from,
.slide-up-leave-to {
  transform: translateY(20px);
  opacity: 0;
}

/* 加载动画 */
@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-spinner {
  animation: spin 1s linear infinite;
}

/* 毛玻璃效果 */
.glass {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

/* 渐变文字 */
.gradient-text {
  background: linear-gradient(45deg, var(--primary-color), var(--secondary-color));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 响应式断点 */
@media (max-width: 768px) {
  .container {
    padding: 0 1rem;
  }
  
  .grid-2 {
    grid-template-columns: 1fr;
  }
  
  .grid-3 {
    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  }
}

@media (max-width: 480px) {
  .grid-3 {
    grid-template-columns: 1fr;
  }
}
</style>