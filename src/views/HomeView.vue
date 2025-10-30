<template>
  <div class="home-view">
    <!-- 顶部导航栏 -->
    <header class="app-header">
      <div class="header-left">
        <BackButton />
        <h1 class="logo">MusicFlow</h1>
      </div>
      <div class="header-center">
        <nav class="main-nav">
          <RouterLink to="/" class="nav-item">发现</RouterLink>
          <RouterLink to="/discover" class="nav-item">发现音乐</RouterLink>
          <RouterLink to="/library" class="nav-item">我的音乐</RouterLink>
          <RouterLink to="/social" class="nav-item">社交</RouterLink>
          <RouterLink to="/ai" class="nav-item">AI助手</RouterLink>
        </nav>
      </div>
      <div class="header-right">
        <RouterLink to="/search" class="search-btn">搜索</RouterLink>
        <button class="theme-toggle-btn" @click="toggleTheme">
          <span class="theme-icon">{{ isDarkMode ? '☀️' : '🌙' }}</span>
        </button>
        <div class="user-avatar" @click="goToProfile">
          <img v-if="userInfo.avatar && userInfo.avatar.startsWith('data:image')" :src="userInfo.avatar" alt="用户头像" class="avatar-image">
          <span v-else class="avatar-icon">{{ userInfo.avatar || '👤' }}</span>
        </div>
      </div>
    </header>

    <!-- 主要内容区域 -->
    <main class="main-content">
      <!-- 个性化推荐区域 -->
      <section class="recommendation-section">
        <h2>为你推荐</h2>
        <div class="recommendation-grid">
          <div class="recommendation-card" v-for="i in 6" :key="i">
            <div class="card-cover"></div>
            <div class="card-info">
              <h3>推荐歌单 {{ i }}</h3>
              <p>AI智能推荐</p>
            </div> 
          </div>
        </div>
      </section>

      <!-- 热门歌单 -->
      <section class="hot-playlists">
        <h2>热门歌单</h2>
        <div class="playlist-grid">
          <div class="playlist-card" v-for="i in 4" :key="i">
            <div class="playlist-cover"></div>
            <div class="playlist-info">
              <h3>热门歌单 {{ i }}</h3>
              <p>播放量：{{ (i * 10000).toLocaleString() }}</p>
            </div>
          </div>
        </div>
      </section>
    </main>

    <!-- 底部播放器 -->
    <footer class="player-footer">
      <div class="player-controls">
        <button class="control-btn">上一首</button>
        <button class="control-btn play-btn">播放</button>
        <button class="control-btn">下一首</button>
      </div>
      <div class="player-progress">
        <div class="progress-bar">
          <div class="progress-fill"></div>
        </div>
      </div>
      <div class="player-info">
        <span>当前播放：暂无音乐</span>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import BackButton from '@/components/BackButton.vue'

const router = useRouter()

// 主题模式
const isDarkMode = ref(true)

// 用户信息
const userInfo = ref({
  isLoggedIn: false,
  username: '',
  displayName: '访客用户',
  avatar: '👤',
  loginType: ''
})

// 检查用户登录状态
const checkLoginStatus = () => {
  const savedUser = localStorage.getItem('musicflow_user')
  if (savedUser) {
    userInfo.value = JSON.parse(savedUser)
  }
}

// 处理用户状态变化
const handleUserStatusChange = (event: CustomEvent) => {
  userInfo.value = event.detail
}

// 处理头像变化
const handleAvatarChange = (event: CustomEvent) => {
  const newAvatar = event.detail
  userInfo.value.avatar = newAvatar
  
  // 更新本地存储的用户信息
  const savedUser = localStorage.getItem('musicflow_user')
  if (savedUser) {
    const userData = JSON.parse(savedUser)
    userData.avatar = newAvatar
    localStorage.setItem('musicflow_user', JSON.stringify(userData))
  }
}

// 处理主题变化
const handleThemeChange = (event: CustomEvent) => {
  isDarkMode.value = event.detail.isDarkMode
}

// 前往个人资料页面
const goToProfile = () => {
  router.push('/profile')
}

// 切换主题
const toggleTheme = () => {
  isDarkMode.value = !isDarkMode.value
  
  // 更新根元素的主题类
  const root = document.documentElement
  if (isDarkMode.value) {
    root.classList.add('dark-theme')
    root.classList.remove('light-theme')
  } else {
    root.classList.add('light-theme')
    root.classList.remove('dark-theme')
  }
  
  // 保存主题偏好到本地存储
  localStorage.setItem('musicflow_theme', isDarkMode.value ? 'dark' : 'light')
  
  // 触发主题变化事件，让其他组件也能响应
  window.dispatchEvent(new CustomEvent('themeChanged', { 
    detail: { isDarkMode: isDarkMode.value }
  }))
}

// 初始化主题
const initTheme = () => {
  const savedTheme = localStorage.getItem('musicflow_theme')
  const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
  
  // 优先使用保存的主题，其次使用系统偏好
  if (savedTheme) {
    isDarkMode.value = savedTheme === 'dark'
  } else {
    isDarkMode.value = prefersDark
  }
  
  // 应用主题
  const root = document.documentElement
  if (isDarkMode.value) {
    root.classList.add('dark-theme')
    root.classList.remove('light-theme')
  } else {
    root.classList.add('light-theme')
    root.classList.remove('dark-theme')
  }
}

// 组件挂载时检查登录状态
onMounted(() => {
  checkLoginStatus()
  initTheme()
  window.addEventListener('userStatusChanged', handleUserStatusChange as EventListener)
  window.addEventListener('avatarChanged', handleAvatarChange as EventListener)
  window.addEventListener('themeChanged', handleThemeChange as EventListener)
})

// 组件卸载时清理事件监听
onUnmounted(() => {
  window.removeEventListener('userStatusChanged', handleUserStatusChange as EventListener)
  window.removeEventListener('avatarChanged', handleAvatarChange as EventListener)
  window.removeEventListener('themeChanged', handleThemeChange as EventListener)
})
</script>

<style scoped lang="scss">
.home-view {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%);
  color: white;
}

.app-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 2rem;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  
  .logo {
    font-size: 1.5rem;
    font-weight: bold;
    color: #4cc9f0;
  }
  
  .header-center {
    display: flex;
    align-items: center;
    gap: 3rem;
    
    .main-nav {
      display: flex;
      gap: 2rem;
      
      .nav-item {
        color: white;
        text-decoration: none;
        padding: 0.5rem 1rem;
        border-radius: 20px;
        transition: all 0.3s ease;
        
        &:hover, &.router-link-active {
          background: rgba(76, 201, 240, 0.2);
          color: #4cc9f0;
        }
      }
    }
    
    .search-btn {
      color: white;
      text-decoration: none;
      padding: 0.5rem 1rem;
      border: 1px solid rgba(255, 255, 255, 0.3);
      border-radius: 20px;
      transition: all 0.3s ease;
      
      &:hover {
        background: rgba(76, 201, 240, 0.2);
        border-color: #4cc9f0;
      }
    }
  }
  
  .header-right {
    display: flex;
    align-items: center;
    gap: 1rem;
    
    .theme-toggle-btn {
      background: rgba(255, 255, 255, 0.1);
      border: 1px solid rgba(255, 255, 255, 0.3);
      border-radius: 20px;
      padding: 0.5rem;
      cursor: pointer;
      transition: all 0.3s ease;
      display: flex;
      align-items: center;
      justify-content: center;
      width: 40px;
      height: 40px;
      
      &:hover {
        background: rgba(76, 201, 240, 0.2);
        border-color: #4cc9f0;
      }
      
      .theme-icon {
        font-size: 1.2rem;
      }
    }
    
    .user-avatar {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      background: rgba(255, 255, 255, 0.2);
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      transition: all 0.3s ease;
      
      &:hover {
        background: rgba(76, 201, 240, 0.2);
      }
      
      .avatar-image {
        width: 100%;
        height: 100%;
        border-radius: 50%;
        object-fit: cover;
      }
      
      .avatar-icon {
        font-size: 1.2rem;
      }
    }
  }
}

.main-content {
  flex: 1;
  padding: 2rem;
  overflow-y: auto;
  
  section {
    margin-bottom: 3rem;
    
    h2 {
      margin-bottom: 1rem;
      font-size: 1.5rem;
      color: #4cc9f0;
    }
  }
}

.recommendation-grid, .playlist-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
}

.recommendation-card, .playlist-card {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 1rem;
  transition: transform 0.3s ease;
  
  &:hover {
    transform: translateY(-5px);
    background: rgba(255, 255, 255, 0.15);
  }
  
  .card-cover, .playlist-cover {
    width: 100%;
    height: 120px;
    background: linear-gradient(45deg, #4cc9f0, #f72585);
    border-radius: 8px;
    margin-bottom: 0.5rem;
  }
  
  h3 {
    margin: 0.5rem 0;
    font-size: 1rem;
  }
  
  p {
    margin: 0;
    font-size: 0.9rem;
    color: rgba(255, 255, 255, 0.7);
  }
}

.player-footer {
  background: rgba(0, 0, 0, 0.8);
  padding: 1rem 2rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  
  .player-controls {
    display: flex;
    gap: 1rem;
    
    .control-btn {
      background: rgba(255, 255, 255, 0.1);
      border: none;
      color: white;
      padding: 0.5rem 1rem;
      border-radius: 20px;
      cursor: pointer;
      transition: all 0.3s ease;
      
      &:hover {
        background: rgba(76, 201, 240, 0.3);
      }
      
      &.play-btn {
        background: #4cc9f0;
        padding: 0.5rem 1.5rem;
        
        &:hover {
          background: #3ab0d9;
        }
      }
    }
  }
  
  .progress-bar {
    width: 300px;
    height: 4px;
    background: rgba(255, 255, 255, 0.3);
    border-radius: 2px;
    overflow: hidden;
    
    .progress-fill {
      width: 30%;
      height: 100%;
      background: #4cc9f0;
      border-radius: 2px;
    }
  }
}

/* 全局主题样式 */
:global(.light-theme) .home-view {
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 50%, #90caf9 100%);
  color: #333;
}

:global(.light-theme) .app-header {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  
  .logo {
    color: #1976d2;
  }
  
  .main-nav .nav-item {
    color: #333;
    
    &:hover, &.router-link-active {
      background: rgba(25, 118, 210, 0.1);
      color: #1976d2;
    }
  }
  
  .search-btn {
    color: #333;
    border: 1px solid rgba(0, 0, 0, 0.2);
    
    &:hover {
      background: rgba(25, 118, 210, 0.1);
      border-color: #1976d2;
    }
  }
  
  .theme-toggle-btn {
    background: rgba(0, 0, 0, 0.05);
    border: 1px solid rgba(0, 0, 0, 0.2);
    
    &:hover {
      background: rgba(25, 118, 210, 0.1);
      border-color: #1976d2;
    }
  }
  
  .user-avatar {
    background: rgba(0, 0, 0, 0.1);
    
    &:hover {
      background: rgba(25, 118, 210, 0.2);
    }
  }
}

:global(.light-theme) .main-content {
  section h2 {
    color: #1976d2;
  }
}

:global(.light-theme) .recommendation-card,
:global(.light-theme) .playlist-card {
  background: rgba(255, 255, 255, 0.8);
  color: #333;
  
  &:hover {
    background: rgba(255, 255, 255, 0.9);
  }
  
  p {
    color: #666;
  }
}

:global(.light-theme) .player-footer {
  background: rgba(255, 255, 255, 0.9);
  
  .control-btn {
    background: rgba(0, 0, 0, 0.1);
    color: #333;
    
    &:hover {
      background: rgba(25, 118, 210, 0.2);
    }
    
    &.play-btn {
      background: #1976d2;
      color: white;
      
      &:hover {
        background: #1565c0;
      }
    }
  }
  
  .progress-bar {
    background: rgba(0, 0, 0, 0.2);
    
    .progress-fill {
      background: #1976d2;
    }
  }
}
</style>