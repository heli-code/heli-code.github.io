<template>
  <div class="home-view">
    <!-- 悬浮AI助手 -->
    <FloatingAI />
    
    <!-- 顶部导航栏 -->
    <header class="app-header">
      <div class="header-left">
        <h1 class="logo">MusicFlow</h1>
      </div>
      <div class="header-center">
        <nav class="main-nav">
          <RouterLink to="/" class="nav-item">首页</RouterLink>
          <RouterLink to="/library" class="nav-item">我的音乐</RouterLink>
          <RouterLink to="/social" class="nav-item">社交</RouterLink>
        </nav>
      </div>
      <div class="header-right">
        <RouterLink to="/search" class="search-btn">搜索</RouterLink>
        <div class="user-menu">
          <div class="user-avatar" @click="handleAvatarClick">
            <img v-if="userInfo.avatar && userInfo.avatar.startsWith('data:image')" :src="userInfo.avatar" alt="用户头像" class="avatar-image">
            <span v-else class="avatar-icon">{{ userInfo.avatar }}</span>
          </div>
          <div class="user-dropdown" v-if="showUserMenu">
            <div class="dropdown-content">
              <div class="user-info">
                <div class="user-avatar-large">
                  <img v-if="userInfo.avatar && userInfo.avatar.startsWith('data:image')" :src="userInfo.avatar" alt="用户头像" class="avatar-image">
                  <span v-else class="avatar-icon">{{ userInfo.avatar }}</span>
                </div>
                <div class="user-details">
                  <h4>{{ userInfo.displayName }}</h4>
                  <p v-if="userInfo.isLoggedIn">{{ userInfo.loginType === 'email' ? '邮箱用户' : '手机用户' }}</p>
                  <p v-else>点击登录享受完整功能</p>
                </div>
              </div>
              <div class="dropdown-divider"></div>
              <div class="menu-items">
                <div v-if="userInfo.isLoggedIn" class="menu-item" @click="goToProfile">
                  <span class="menu-icon">👤</span>
                  <span>个人资料</span>
                </div>
                <div v-if="userInfo.isLoggedIn" class="menu-item" @click="goToSettings">
                  <span class="menu-icon">⚙️</span>
                  <span>设置</span>
                </div>
                <div v-if="!userInfo.isLoggedIn" class="menu-item" @click="goToLogin">
                  <span class="menu-icon">🔐</span>
                  <span>登录/注册</span>
                </div>
                <div v-if="userInfo.isLoggedIn" class="menu-item" @click="handleLogout">
                  <span class="menu-icon">🚪</span>
                  <span>退出登录</span>
                </div>
                <div class="dropdown-divider"></div>
                <div class="menu-item" @click="goToHelp">
                  <span class="menu-icon">❓</span>
                  <span>帮助中心</span>
                </div>
                <div class="menu-item" @click="goToFeedback">
                  <span class="menu-icon">💬</span>
                  <span>意见反馈</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </header>

    <!-- 主要内容区域 -->
    <main class="main-content">
      <!-- 欢迎横幅 -->
      <section class="welcome-banner">
        <div class="banner-content">
          <h2>欢迎来到 MusicFlow</h2>
          <p>AI智能音乐平台，发现属于你的音乐世界</p>
          <div class="banner-stats">
            <div class="stat-item">
              <span class="stat-number">10万+</span>
              <span class="stat-label">首歌曲</span>
            </div>
            <div class="stat-item">
              <span class="stat-number">5万+</span>
              <span class="stat-label">个歌单</span>
            </div>
            <div class="stat-item">
              <span class="stat-number">1万+</span>
              <span class="stat-label">活跃用户</span>
            </div>
          </div>
        </div>
      </section>

      <!-- 内容速览导航 -->
      <section class="content-overview">
        <h3>🎯 首页内容速览</h3>
        <div class="overview-grid">
          <div class="overview-item" v-for="item in contentOverview" :key="item.id" @click="scrollToSection(item.id)">
            <div class="overview-icon">{{ item.icon }}</div>
            <div class="overview-info">
              <h4>{{ item.title }}</h4>
              <p>{{ item.description }}</p>
            </div>
          </div>
        </div>
      </section>

      <!-- 个性化推荐区域 -->
      <section id="personal-recommendation" class="recommendation-section">
        <div class="section-header">
          <h2>🎵 为你推荐</h2>
          <span class="section-subtitle">基于你的听歌习惯智能推荐</span>
        </div>
        <div class="recommendation-grid">
          <div class="recommendation-card" v-for="i in 6" :key="i">
            <div class="card-cover"></div>
            <div class="card-info">
              <h3>推荐歌单 {{ i }}</h3>
              <p>AI智能推荐 · {{ ['流行', '电子', '摇滚', '民谣', '古典', '爵士'][i-1] }}</p>
              <div class="card-stats">
                <span>🎵 {{ i * 15 }} 首歌曲</span>
                <span>❤️ {{ i * 1000 }} 喜欢</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 热门歌单 -->
      <section id="hot-playlists" class="hot-playlists">
        <div class="section-header">
          <h2>🔥 热门歌单</h2>
          <span class="section-subtitle">大家都在听的精选歌单</span>
        </div>
        <div class="playlist-grid">
          <div class="playlist-card" v-for="i in 4" :key="i">
            <div class="playlist-cover"></div>
            <div class="playlist-info">
              <h3>热门歌单 {{ i }}</h3>
              <p>播放量：{{ (i * 10000).toLocaleString() }}</p>
              <div class="playlist-tags">
                <span class="tag">{{ ['流行', '电子', '摇滚', '民谣'][i-1] }}</span>
                <span class="tag">热门</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- AI智能推荐 -->
      <section id="ai-recommendation" class="ai-recommendation">
        <div class="section-header">
          <h2>🤖 AI智能推荐</h2>
          <span class="section-subtitle">基于情绪和场景的个性化推荐</span>
        </div>
        <div class="ai-grid">
          <div class="ai-card" v-for="i in 8" :key="i">
            <div class="ai-cover"></div>
            <div class="ai-info">
              <h3>AI推荐歌曲 {{ i }}</h3>
              <p>基于你的听歌习惯</p>
              <div class="mood-tags">
                <span class="mood-tag">{{ moods[i-1] }}</span>
                <span class="mood-tag">智能推荐</span>
              </div>
              <div class="song-actions">
                <button class="play-btn">播放</button>
                <button class="like-btn">收藏</button>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 情绪歌单 -->
      <section id="mood-playlists" class="mood-playlists">
        <div class="section-header">
          <h2>😊 情绪歌单</h2>
          <span class="section-subtitle">根据心情选择适合的音乐</span>
        </div>
        <div class="mood-grid">
          <div class="mood-card" v-for="mood in moods" :key="mood">
            <div class="mood-icon">{{ getMoodEmoji(mood) }}</div>
            <h3>{{ mood }}</h3>
            <p>专属歌单</p>
            <button class="explore-btn">探索</button>
          </div>
        </div>
      </section>

      <!-- 老歌怀旧 -->
      <section id="nostalgic-songs" class="nostalgic-songs">
        <div class="section-header">
          <h2>📻 老歌怀旧</h2>
          <span class="section-subtitle">经典老歌，重温美好时光</span>
        </div>
        <div class="nostalgic-grid">
          <div class="nostalgic-card" v-for="i in 6" :key="i">
            <div class="nostalgic-cover"></div>
            <div class="nostalgic-info">
              <h3>{{ nostalgicSongs[i-1].title }}</h3>
              <p>{{ nostalgicSongs[i-1].artist }}</p>
              <div class="nostalgic-details">
                <span class="year">{{ nostalgicSongs[i-1].year}}年代</span>
                <span class="genre">{{ nostalgicSongs[i-1].genre}}</span>
              </div>
              <div class="nostalgic-actions">
                <button class="play-btn">播放</button>
                <button class="share-btn">分享</button>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 新歌速递 -->
      <section id="new-releases" class="new-releases">
        <div class="section-header">
          <h2>🎶 新歌速递</h2>
          <span class="section-subtitle">最新上线的热门歌曲</span>
        </div>
        <div class="new-songs-list">
          <div class="song-item" v-for="i in 5" :key="i">
            <div class="song-number">{{ i }}</div>
            <div class="song-cover"></div>
            <div class="song-info">
              <h3>新歌 {{ i }}</h3>
              <p>歌手名称</p>
            </div>
            <div class="song-duration">3:45</div>
            <div class="song-actions">
              <button class="action-btn">播放</button>
              <button class="action-btn">收藏</button>
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
import { RouterLink, useRouter } from 'vue-router'
import FloatingAI from '@/components/FloatingAI.vue'
import { ref, onMounted, onUnmounted } from 'vue'

const router = useRouter()

// 用户状态
const userInfo = ref({
  isLoggedIn: false,
  username: '',
  displayName: '访客用户',
  avatar: '👤',
  loginType: ''
})

// 用户菜单状态
const showUserMenu = ref(false)

// 用户菜单控制
const toggleUserMenu = () => {
  showUserMenu.value = !showUserMenu.value
}

// 关闭用户菜单
const closeUserMenu = () => {
  showUserMenu.value = false
}

// 菜单项点击处理
const goToProfile = () => {
  router.push('/profile')
  closeUserMenu()
}

const goToSettings = () => {
  console.log('前往设置')
  closeUserMenu()
}

const goToLogin = () => {
  router.push('/auth')
  closeUserMenu()
}

const goToHelp = () => {
  console.log('前往帮助中心')
  closeUserMenu()
}

const goToFeedback = () => {
  console.log('前往意见反馈')
  closeUserMenu()
}

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

// 点击头像处理
const handleAvatarClick = () => {
  if (userInfo.value.isLoggedIn) {
    // 已登录：显示用户菜单
    toggleUserMenu()
  } else {
    // 未登录：跳转到登录注册页面
    router.push('/auth')
  }
}

// 退出登录
const handleLogout = () => {
  // 清除用户信息
  localStorage.removeItem('musicflow_user')
  userInfo.value = {
    isLoggedIn: false,
    username: '',
    displayName: '访客用户',
    avatar: '👤',
    loginType: ''
  }
  closeUserMenu()
  // 可以添加退出登录的API调用
  console.log('用户已退出登录')
}

// 组件挂载时检查登录状态
onMounted(() => {
  checkLoginStatus()
  window.addEventListener('userStatusChanged', handleUserStatusChange as EventListener)
  window.addEventListener('avatarChanged', handleAvatarChange as EventListener)
})

// 组件卸载时清理事件监听
onUnmounted(() => {
  window.removeEventListener('userStatusChanged', handleUserStatusChange as EventListener)
  window.removeEventListener('avatarChanged', handleAvatarChange as EventListener)
})

const moods = ['快乐', '放松', '专注', '运动', '伤感', '浪漫', '派对', '睡眠']

// 内容速览导航数据
const contentOverview = [
  { id: 'personal-recommendation', icon: '🎵', title: '为你推荐', description: '个性化音乐推荐' },
  { id: 'hot-playlists', icon: '🔥', title: '热门歌单', description: '大家都在听的精选' },
  { id: 'ai-recommendation', icon: '🤖', title: 'AI智能推荐', description: '基于情绪的个性化推荐' },
  { id: 'mood-playlists', icon: '😊', title: '情绪歌单', description: '根据心情选择音乐' },
  { id: 'nostalgic-songs', icon: '📻', title: '老歌怀旧', description: '经典老歌重温时光' },
  { id: 'new-releases', icon: '🎶', title: '新歌速递', description: '最新热门歌曲' }
]

// 老歌怀旧数据
const nostalgicSongs = [
  { title: '月亮代表我的心', artist: '邓丽君', year: '70', genre: '流行' },
  { title: '甜蜜蜜', artist: '邓丽君', year: '70', genre: '流行' },
  { title: '海阔天空', artist: 'Beyond', year: '90', genre: '摇滚' },
  { title: '吻别', artist: '张学友', year: '90', genre: '流行' },
  { title: '朋友', artist: '周华健', year: '90', genre: '流行' },
  { title: '童年', artist: '罗大佑', year: '80', genre: '民谣' }
]

const getMoodEmoji = (mood: string) => {
  const emojiMap: Record<string, string> = {
    '快乐': '😊',
    '放松': '😌',
    '专注': '🧠',
    '运动': '🏃',
    '伤感': '😢',
    '浪漫': '💕',
    '派对': '🎉',
    '睡眠': '😴'
  }
  return emojiMap[mood] || '🎵'
}

// 滚动到指定区域
const scrollToSection = (sectionId: string) => {
  const element = document.getElementById(sectionId)
  if (element) {
    element.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }
}
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
  }
  
  .header-right {
    display: flex;
    align-items: center;
    gap: 1rem;
    
    .search-btn {
      color: white;
      text-decoration: none;
      padding: 0.5rem 1rem;
      border: 1px solid rgba(255, 255, 255, 0.3);
      border-radius: 20px;
      transition: all 0.3s ease;
      
      &:hover {
        background: rgba(255, 255, 255, 0.1);
      }
    }
    
    .user-menu {
      position: relative;
      cursor: pointer;
      
      .user-avatar {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        background: rgba(255, 255, 255, 0.2);
        display: flex;
        align-items: center;
        justify-content: center;
        transition: all 0.3s ease;
        overflow: hidden;
        
        .avatar-icon {
          font-size: 1.2rem;
        }
        
        .avatar-image {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
        
        &:hover {
          background: rgba(255, 255, 255, 0.3);
          transform: scale(1.05);
        }
      }
      
      .user-dropdown {
        position: absolute;
        top: 100%;
        right: 0;
        margin-top: 0.5rem;
        min-width: 250px;
        background: rgba(26, 26, 46, 0.95);
        backdrop-filter: blur(10px);
        border: 1px solid rgba(255, 255, 255, 0.1);
        border-radius: 12px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
        z-index: 1000;
        
        .dropdown-content {
          padding: 1rem;
          
          .user-info {
            display: flex;
            align-items: center;
            gap: 1rem;
            margin-bottom: 1rem;
            
            .user-avatar-large {
              width: 50px;
              height: 50px;
              border-radius: 50%;
              background: rgba(76, 201, 240, 0.2);
              display: flex;
              align-items: center;
              justify-content: center;
              font-size: 1.5rem;
              overflow: hidden;
              
              .avatar-image {
                width: 100%;
                height: 100%;
                object-fit: cover;
              }
            }
            
            .user-details {
              flex: 1;
              
              h4 {
                margin: 0 0 0.3rem 0;
                font-size: 1rem;
                color: white;
              }
              
              p {
                margin: 0;
                font-size: 0.8rem;
                color: rgba(255, 255, 255, 0.7);
              }
            }
          }
          
          .dropdown-divider {
            height: 1px;
            background: rgba(255, 255, 255, 0.1);
            margin: 0.5rem 0;
          }
          
          .menu-items {
            .menu-item {
              display: flex;
              align-items: center;
              gap: 0.8rem;
              padding: 0.8rem 0.5rem;
              border-radius: 8px;
              cursor: pointer;
              transition: all 0.3s ease;
              
              &:hover {
                background: rgba(255, 255, 255, 0.1);
              }
              
              .menu-icon {
                font-size: 1.1rem;
                width: 20px;
                text-align: center;
              }
              
              span:last-child {
                font-size: 0.9rem;
                color: white;
              }
            }
          }
        }
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
  }
}

.welcome-banner {
  background: linear-gradient(135deg, #4cc9f0 0%, #f72585 100%);
  border-radius: 20px;
  padding: 3rem 2rem;
  margin-bottom: 3rem;
  text-align: center;
  
  h2 {
    font-size: 2.5rem;
    margin-bottom: 1rem;
    font-weight: bold;
  }
  
  p {
    font-size: 1.2rem;
    margin-bottom: 2rem;
    opacity: 0.9;
  }
  
  .banner-stats {
    display: flex;
    justify-content: center;
    gap: 3rem;
    
    .stat-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      
      .stat-number {
        font-size: 2rem;
        font-weight: bold;
        margin-bottom: 0.5rem;
      }
      
      .stat-label {
        font-size: 1rem;
        opacity: 0.8;
      }
    }
  }
}

.content-overview {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 15px;
  padding: 2rem;
  margin-bottom: 3rem;
  
  h3 {
    font-size: 1.5rem;
    margin-bottom: 1.5rem;
    color: #4cc9f0;
    text-align: center;
  }
}

.overview-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
}

.overview-item {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.1);
  padding: 1rem;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  
  &:hover {
    background: rgba(76, 201, 240, 0.2);
    transform: translateY(-2px);
  }
  
  .overview-icon {
    font-size: 2rem;
    margin-right: 1rem;
  }
  
  .overview-info {
    h4 {
      margin: 0 0 0.3rem 0;
      font-size: 1rem;
      color: white;
    }
    
    p {
      margin: 0;
      font-size: 0.8rem;
      color: rgba(255, 255, 255, 0.7);
    }
  }
}

.section-header {
  margin-bottom: 1.5rem;
  
  h2 {
    font-size: 1.8rem;
    margin-bottom: 0.5rem;
    color: #4cc9f0;
  }
  
  .section-subtitle {
    font-size: 1rem;
    color: rgba(255, 255, 255, 0.7);
  }
}

.recommendation-grid, .playlist-grid, .ai-grid, .mood-grid, .nostalgic-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 1.5rem;
}

.recommendation-card, .playlist-card, .ai-card, .nostalgic-card {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 1rem;
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-5px);
    background: rgba(255, 255, 255, 0.15);
  }
  
  .card-cover, .playlist-cover, .ai-cover, .nostalgic-cover {
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
    margin: 0.3rem 0;
    font-size: 0.9rem;
    color: rgba(255, 255, 255, 0.7);
  }
}

.card-stats, .playlist-tags {
  display: flex;
  gap: 1rem;
  margin-top: 0.5rem;
  font-size: 0.8rem;
  color: rgba(255, 255, 255, 0.6);
}

.tag {
  background: rgba(76, 201, 240, 0.2);
  color: #4cc9f0;
  padding: 0.2rem 0.5rem;
  border-radius: 10px;
  font-size: 0.7rem;
}

.mood-tags {
  display: flex;
  gap: 0.5rem;
  margin: 0.5rem 0;
}

.mood-tag {
  background: rgba(247, 37, 133, 0.2);
  color: #f72585;
  padding: 0.2rem 0.5rem;
  border-radius: 10px;
  font-size: 0.7rem;
}

.song-actions {
  display: flex;
  gap: 0.5rem;
  margin-top: 0.5rem;
  
  .play-btn, .like-btn {
    background: rgba(76, 201, 240, 0.3);
    border: none;
    color: white;
    padding: 0.3rem 0.8rem;
    border-radius: 15px;
    cursor: pointer;
    font-size: 0.8rem;
    transition: background 0.3s ease;
    
    &:hover {
      background: rgba(76, 201, 240, 0.5);
    }
  }
}

.mood-card {
  background: rgba(255, 255, 255, 0.1);
  padding: 1.5rem;
  border-radius: 12px;
  text-align: center;
  transition: all 0.3s ease;
  
  &:hover {
    background: rgba(255, 255, 255, 0.15);
    transform: scale(1.05);
  }
  
  .mood-icon {
    font-size: 2rem;
    margin-bottom: 1rem;
  }
  
  h3 {
    margin: 0 0 0.5rem 0;
    font-size: 1.2rem;
  }
  
  p {
    margin: 0 0 1rem 0;
    font-size: 0.9rem;
    color: rgba(255, 255, 255, 0.7);
  }
  
  .explore-btn {
    background: rgba(76, 201, 240, 0.3);
    border: none;
    color: white;
    padding: 0.5rem 1rem;
    border-radius: 15px;
    cursor: pointer;
    transition: background 0.3s ease;
    
    &:hover {
      background: rgba(76, 201, 240, 0.5);
    }
  }
}

.nostalgic-details {
  display: flex;
  gap: 1rem;
  margin: 0.5rem 0;
  font-size: 0.8rem;
  
  .year {
    background: rgba(247, 181, 0, 0.2);
    color: #f7b500;
    padding: 0.2rem 0.5rem;
    border-radius: 10px;
  }
  
  .genre {
    background: rgba(76, 201, 240, 0.2);
    color: #4cc9f0;
    padding: 0.2rem 0.5rem;
    border-radius: 10px;
  }
}

.nostalgic-actions {
  display: flex;
  gap: 0.5rem;
  margin-top: 0.5rem;
  
  .play-btn, .share-btn {
    background: rgba(247, 181, 0, 0.3);
    border: none;
    color: white;
    padding: 0.3rem 0.8rem;
    border-radius: 15px;
    cursor: pointer;
    font-size: 0.8rem;
    transition: background 0.3s ease;
    
    &:hover {
      background: rgba(247, 181, 0, 0.5);
    }
  }
  
  .share-btn {
    background: rgba(76, 201, 240, 0.3);
    
    &:hover {
      background: rgba(76, 201, 240, 0.5);
    }
  }
}

.new-songs-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.song-item {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.1);
  padding: 1rem;
  border-radius: 12px;
  transition: background 0.3s ease;
  
  &:hover {
    background: rgba(255, 255, 255, 0.15);
  }
  
  .song-number {
    width: 30px;
    text-align: center;
    margin-right: 1rem;
    color: rgba(255, 255, 255, 0.7);
  }
  
  .song-cover {
    width: 50px;
    height: 50px;
    background: linear-gradient(45deg, #4cc9f0, #f72585);
    border-radius: 8px;
    margin-right: 1rem;
  }
  
  .song-info {
    flex: 1;
    
    h3 {
      margin: 0 0 0.5rem 0;
      font-size: 1rem;
    }
    
    p {
      margin: 0;
      font-size: 0.9rem;
      color: rgba(255, 255, 255, 0.7);
    }
  }
  
  .song-duration {
    margin: 0 1rem;
    color: rgba(255, 255, 255, 0.7);
  }
  
  .song-actions {
    display: flex;
    gap: 0.5rem;
    
    .action-btn {
      background: rgba(255, 255, 255, 0.1);
      border: none;
      color: white;
      padding: 0.5rem 1rem;
      border-radius: 15px;
      cursor: pointer;
      font-size: 0.8rem;
      transition: background 0.3s ease;
      
      &:hover {
        background: rgba(76, 201, 240, 0.3);
      }
    }
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

@media (max-width: 768px) {
  .app-header {
    padding: 1rem !important;
    flex-direction: column;
    gap: 1rem;
    
    .main-nav {
      gap: 1rem !important;
      
      .nav-item {
        padding: 0.5rem !important;
        font-size: 0.9rem;
      }
    }
  }
  
  .main-content {
    padding: 1rem !important;
  }
  
  .recommendation-grid, .playlist-grid, .ai-grid, .mood-grid {
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)) !important;
    gap: 1rem !important;
  }
  
  .welcome-banner {
    padding: 2rem 1rem;
    
    h2 {
      font-size: 2rem;
    }
    
    .banner-stats {
      gap: 1.5rem;
      
      .stat-item .stat-number {
        font-size: 1.5rem;
      }
    }
  }
}
</style>