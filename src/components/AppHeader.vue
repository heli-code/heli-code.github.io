<template>
  <header class="app-header">
    <!-- Logo和品牌 -->
    <div class="brand">
      <router-link to="/" class="logo">
        <span class="logo-icon">🎵</span>
        <span class="logo-text">MusicFlow</span>
      </router-link>
    </div>

    <!-- 主导航 -->
    <nav class="main-nav">
      <router-link 
        v-for="navItem in navItems" 
        :key="navItem.name"
        :to="{ name: navItem.name }" 
        class="nav-item"
        :class="{ active: $route.name === navItem.name }"
      >
        <span class="nav-icon">{{ navItem.icon }}</span>
        <span class="nav-text">{{ navItem.text }}</span>
      </router-link>
    </nav>

    <!-- 搜索框 -->
    <div class="search-section">
      <div class="search-box">
        <input 
          type="text" 
          v-model="searchQuery" 
          placeholder="搜索歌曲、歌手、专辑..."
          class="search-input"
          @keyup.enter="performSearch"
        />
        <button class="search-btn" @click="performSearch">
          <span>🔍</span>
        </button>
      </div>
    </div>

    <!-- 用户操作 -->
    <div class="user-section">
      <!-- 未登录状态 -->
      <div v-if="!isAuthenticated" class="auth-buttons">
        <router-link to="/auth" class="auth-btn login-btn">登录</router-link>
        <router-link to="/auth" class="auth-btn register-btn">注册</router-link>
      </div>

      <!-- 已登录状态 -->
      <div v-else class="user-menu">
        <!-- 通知 -->
        <button class="icon-btn" @click="toggleNotifications">
          <span class="icon">🔔</span>
          <span v-if="unreadCount > 0" class="badge">{{ unreadCount }}</span>
        </button>

        <!-- 消息 -->
        <button class="icon-btn" @click="toggleMessages">
          <span class="icon">💬</span>
        </button>

        <!-- 用户头像和下拉菜单 -->
        <div class="user-dropdown">
          <button class="user-avatar" @click="toggleDropdown">
            <img :src="userInfo.avatar" :alt="userInfo.displayName" />
            <span class="user-name">{{ userInfo.displayName }}</span>
          </button>
          
          <div v-if="isDropdownOpen" class="dropdown-menu">
            <router-link to="/profile" class="dropdown-item">
              <span class="icon">👤</span>
              个人资料
            </router-link>
            <router-link to="/library" class="dropdown-item">
              <span class="icon">📚</span>
              我的音乐
            </router-link>
            <div class="dropdown-divider"></div>
            <button class="dropdown-item" @click="logout">
              <span class="icon">🚪</span>
              退出登录
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 移动端菜单按钮 -->
    <button class="mobile-menu-btn" @click="toggleMobileMenu">
      <span>☰</span>
    </button>
  </header>

  <!-- 移动端菜单 -->
  <div v-if="isMobileMenuOpen" class="mobile-menu-overlay" @click="closeMobileMenu">
    <div class="mobile-menu" @click.stop>
      <div class="mobile-nav">
        <router-link 
          v-for="navItem in navItems" 
          :key="navItem.name"
          :to="{ name: navItem.name }" 
          class="mobile-nav-item"
          :class="{ active: $route.name === navItem.name }"
          @click="closeMobileMenu"
        >
          <span class="nav-icon">{{ navItem.icon }}</span>
          <span class="nav-text">{{ navItem.text }}</span>
        </router-link>
      </div>

      <div v-if="isAuthenticated" class="mobile-user-info">
        <div class="user-avatar">
          <img :src="userInfo.avatar" :alt="userInfo.displayName" />
        </div>
        <div class="user-details">
          <div class="user-name">{{ userInfo.displayName }}</div>
          <div class="user-stats">
            <span>歌单 {{ userStats.playlists }}</span>
            <span>粉丝 {{ userStats.followers }}</span>
          </div>
        </div>
      </div>

      <div class="mobile-actions">
        <button v-if="isAuthenticated" class="mobile-action-btn" @click="logout">
          <span class="icon">🚪</span>
          退出登录
        </button>
        <div v-else class="mobile-auth-buttons">
          <router-link to="/auth" class="mobile-auth-btn" @click="closeMobileMenu">
            登录
          </router-link>
          <router-link to="/auth" class="mobile-auth-btn" @click="closeMobileMenu">
            注册
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

// 响应式数据
const searchQuery = ref('')
const isDropdownOpen = ref(false)
const isMobileMenuOpen = ref(false)
const unreadCount = ref(3) // 模拟未读消息数量

// 导航项配置
const navItems = [
  { name: 'Home', text: '发现', icon: '🔍' },
  { name: 'Library', text: '我的音乐', icon: '📚' },
  { name: 'Social', text: '音乐社交', icon: '👥' },
  { name: 'AI', text: 'AI助手', icon: '🤖' }
]

// 计算属性
const isAuthenticated = computed(() => userStore.isAuthenticated)
const userInfo = computed(() => userStore.userInfo)
const userStats = computed(() => userStore.userStats)

// 方法
const performSearch = () => {
  if (searchQuery.value.trim()) {
    router.push({ 
      name: 'Search', 
      query: { q: searchQuery.value.trim() } 
    })
    searchQuery.value = ''
  }
}

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value
}

const toggleMobileMenu = () => {
  isMobileMenuOpen.value = !isMobileMenuOpen.value
}

const closeMobileMenu = () => {
  isMobileMenuOpen.value = false
}

const toggleNotifications = () => {
  // 实现通知功能
  console.log('打开通知')
}

const toggleMessages = () => {
  // 实现消息功能
  console.log('打开消息')
}

const logout = async () => {
  userStore.logout()
  isDropdownOpen.value = false
  isMobileMenuOpen.value = false
  router.push('/')
}

// 点击外部关闭下拉菜单
const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as HTMLElement
  if (!target.closest('.user-dropdown')) {
    isDropdownOpen.value = false
  }
}

// 生命周期
onMounted(() => {
  document.addEventListener('click', handleClickOutside)
  
  // 检查认证状态
  userStore.checkAuthStatus()
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
.app-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 2rem;
  background: rgba(15, 20, 25, 0.9);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.brand .logo {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  text-decoration: none;
  color: white;
  font-size: 1.5rem;
  font-weight: bold;
}

.logo-icon {
  font-size: 1.8rem;
}

.logo-text {
  background: linear-gradient(45deg, #4cc9f0, #f72585);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.main-nav {
  display: flex;
  align-items: center;
  gap: 2rem;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  text-decoration: none;
  color: rgba(255, 255, 255, 0.7);
  border-radius: 20px;
  transition: all 0.3s ease;
  font-size: 0.9rem;
}

.nav-item:hover,
.nav-item.active {
  background: rgba(76, 201, 240, 0.2);
  color: white;
}

.search-section {
  flex: 1;
  max-width: 400px;
  margin: 0 2rem;
}

.search-box {
  display: flex;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 25px;
  overflow: hidden;
}

.search-input {
  flex: 1;
  background: none;
  border: none;
  color: white;
  padding: 0.8rem 1.5rem;
  font-size: 0.9rem;
  outline: none;
}

.search-input::placeholder {
  color: rgba(255, 255, 255, 0.5);
}

.search-btn {
  background: none;
  border: none;
  color: white;
  padding: 0.8rem 1.5rem;
  cursor: pointer;
  transition: background 0.3s ease;
}

.search-btn:hover {
  background: rgba(255, 255, 255, 0.1);
}

.user-section {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.auth-buttons {
  display: flex;
  gap: 0.5rem;
}

.auth-btn {
  padding: 0.5rem 1rem;
  border-radius: 20px;
  text-decoration: none;
  font-size: 0.9rem;
  transition: all 0.3s ease;
}

.login-btn {
  background: rgba(255, 255, 255, 0.1);
  color: white;
}

.login-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

.register-btn {
  background: #4cc9f0;
  color: white;
}

.register-btn:hover {
  background: #3ab0d9;
}

.user-menu {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.icon-btn {
  position: relative;
  background: none;
  border: none;
  color: white;
  padding: 0.5rem;
  border-radius: 50%;
  cursor: pointer;
  transition: background 0.3s ease;
}

.icon-btn:hover {
  background: rgba(255, 255, 255, 0.1);
}

.badge {
  position: absolute;
  top: -5px;
  right: -5px;
  background: #ff4757;
  color: white;
  border-radius: 10px;
  padding: 2px 6px;
  font-size: 0.7rem;
  min-width: 18px;
  text-align: center;
}

.user-dropdown {
  position: relative;
}

.user-avatar {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: none;
  border: none;
  color: white;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 25px;
  transition: background 0.3s ease;
}

.user-avatar:hover {
  background: rgba(255, 255, 255, 0.1);
}

.user-avatar img {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
}

.user-name {
  font-size: 0.9rem;
  font-weight: 500;
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  background: rgba(15, 20, 25, 0.95);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  padding: 0.5rem;
  min-width: 200px;
  margin-top: 0.5rem;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  width: 100%;
  padding: 0.8rem 1rem;
  background: none;
  border: none;
  color: white;
  text-decoration: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.3s ease;
  font-size: 0.9rem;
}

.dropdown-item:hover {
  background: rgba(255, 255, 255, 0.1);
}

.dropdown-divider {
  height: 1px;
  background: rgba(255, 255, 255, 0.1);
  margin: 0.5rem 0;
}

.mobile-menu-btn {
  display: none;
  background: none;
  border: none;
  color: white;
  font-size: 1.5rem;
  cursor: pointer;
  padding: 0.5rem;
}

.mobile-menu-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 1000;
}

.mobile-menu {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  width: 300px;
  background: rgba(15, 20, 25, 0.95);
  backdrop-filter: blur(20px);
  padding: 2rem;
  display: flex;
  flex-direction: column;
}

.mobile-nav {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 2rem;
}

.mobile-nav-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  text-decoration: none;
  color: rgba(255, 255, 255, 0.7);
  border-radius: 8px;
  transition: all 0.3s ease;
}

.mobile-nav-item:hover,
.mobile-nav-item.active {
  background: rgba(76, 201, 240, 0.2);
  color: white;
}

.mobile-user-info {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  margin-bottom: 2rem;
}

.mobile-user-info .user-avatar img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
}

.user-details .user-name {
  font-weight: 600;
  margin-bottom: 0.5rem;
}

.user-details .user-stats {
  display: flex;
  gap: 1rem;
  font-size: 0.8rem;
  color: rgba(255, 255, 255, 0.7);
}

.mobile-actions {
  margin-top: auto;
}

.mobile-action-btn {
  width: 100%;
  padding: 1rem;
  background: rgba(255, 255, 255, 0.1);
  border: none;
  color: white;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  justify-content: center;
  transition: background 0.3s ease;
}

.mobile-action-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

.mobile-auth-buttons {
  display: flex;
  gap: 1rem;
}

.mobile-auth-btn {
  flex: 1;
  padding: 1rem;
  background: rgba(255, 255, 255, 0.1);
  border: none;
  color: white;
  border-radius: 8px;
  text-decoration: none;
  text-align: center;
  transition: background 0.3s ease;
}

.mobile-auth-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

@media (max-width: 768px) {
  .app-header {
    padding: 1rem;
  }
  
  .main-nav,
  .search-section,
  .user-section {
    display: none;
  }
  
  .mobile-menu-btn {
    display: block;
  }
}
</style>