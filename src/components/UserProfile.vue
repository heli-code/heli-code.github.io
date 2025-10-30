<template>
  <div class="user-profile">
    <!-- 用户信息头部 -->
    <div class="profile-header">
      <div class="avatar-section">
        <div class="avatar-container">
          <img 
            v-if="userInfo.avatar" 
            :src="userInfo.avatar" 
            alt="用户头像" 
            class="avatar-image"
          >
          <div v-else class="avatar-placeholder">
            {{ userInfo.username?.charAt(0).toUpperCase() || '👤' }}
          </div>
          <button class="avatar-edit-btn" @click="editAvatar">📷</button>
        </div>
        <h2 class="username">{{ userInfo.username }}</h2>
        <p class="user-bio">{{ userInfo.bio || '这个人很懒，什么都没有写～' }}</p>
      </div>
      
      <div class="stats-section">
        <div class="stat-item">
          <span class="stat-number">{{ userStats.followers || 0 }}</span>
          <span class="stat-label">粉丝</span>
        </div>
        <div class="stat-item">
          <span class="stat-number">{{ userStats.following || 0 }}</span>
          <span class="stat-label">关注</span>
        </div>
        <div class="stat-item">
          <span class="stat-number">{{ userStats.playlists || 0 }}</span>
          <span class="stat-label">歌单</span>
        </div>
        <div class="stat-item">
          <span class="stat-number">{{ userStats.likes || 0 }}</span>
          <span class="stat-label">获赞</span>
        </div>
      </div>
    </div>
    
    <!-- 编辑资料按钮 -->
    <div class="profile-actions">
      <button class="edit-profile-btn" @click="editProfile">
        ✏️ 编辑资料
      </button>
      <button class="settings-btn" @click="openSettings">
        ⚙️ 设置
      </button>
    </div>
    
    <!-- 用户内容区域 -->
    <div class="profile-content">
      <div class="content-tabs">
        <button 
          v-for="tab in tabs" 
          :key="tab.id"
          :class="['tab-btn', { active: activeTab === tab.id }]"
          @click="activeTab = tab.id"
        >
          {{ tab.name }}
        </button>
      </div>
      
      <!-- 歌单管理 -->
      <div v-if="activeTab === 'playlists'" class="tab-content">
        <div class="playlists-header">
          <h3>我的歌单</h3>
          <button class="create-playlist-btn" @click="createPlaylist">
            ➕ 创建歌单
          </button>
        </div>
        
        <div class="playlists-grid">
          <div 
            v-for="playlist in playlists" 
            :key="playlist.id"
            class="playlist-card"
            @click="openPlaylist(playlist.id)"
          >
            <div class="playlist-cover">
              <img 
                v-if="playlist.cover" 
                :src="playlist.cover" 
                alt="歌单封面"
              >
              <div v-else class="playlist-cover-placeholder">
                🎵
              </div>
              <div class="playlist-overlay">
                <button class="play-btn" @click.stop="playPlaylist(playlist)">▶</button>
                <span class="song-count">{{ playlist.songCount }}首</span>
              </div>
            </div>
            <div class="playlist-info">
              <h4 class="playlist-title">{{ playlist.title }}</h4>
              <p class="playlist-desc">{{ playlist.description }}</p>
              <div class="playlist-stats">
                <span class="play-count">播放 {{ playlist.playCount }}</span>
                <span class="like-count">喜欢 {{ playlist.likeCount }}</span>
              </div>
            </div>
            <div class="playlist-actions">
              <button class="action-btn" @click.stop="editPlaylist(playlist)">编辑</button>
              <button class="action-btn delete-btn" @click.stop="deletePlaylist(playlist.id)">删除</button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 收藏内容 -->
      <div v-if="activeTab === 'favorites'" class="tab-content">
        <h3>我的收藏</h3>
        <div class="favorites-grid">
          <div class="favorite-item">
            <span class="favorite-icon">❤️</span>
            <span class="favorite-count">{{ userStats.likedSongs || 0 }} 首歌曲</span>
          </div>
          <div class="favorite-item">
            <span class="favorite-icon">🎵</span>
            <span class="favorite-count">{{ userStats.likedPlaylists || 0 }} 个歌单</span>
          </div>
          <div class="favorite-item">
            <span class="favorite-icon">👤</span>
            <span class="favorite-count">{{ userStats.following || 0 }} 位用户</span>
          </div>
        </div>
      </div>
      
      <!-- 听歌记录 -->
      <div v-if="activeTab === 'history'" class="tab-content">
        <h3>听歌记录</h3>
        <div class="history-list">
          <div 
            v-for="record in listeningHistory" 
            :key="record.id"
            class="history-item"
          >
            <div class="song-info">
              <span class="song-title">{{ record.songTitle }}</span>
              <span class="song-artist">{{ record.artist }}</span>
            </div>
            <div class="history-time">
              {{ formatTime(record.listenTime) }}
            </div>
            <button class="play-again-btn" @click="playSong(record.songId)">▶</button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 编辑资料模态框 -->
    <div v-if="showEditModal" class="modal-overlay">
      <div class="modal-content">
        <h3>编辑资料</h3>
        <form @submit.prevent="saveProfile">
          <div class="form-group">
            <label>用户名</label>
            <input 
              v-model="editForm.username" 
              type="text" 
              maxlength="20"
              placeholder="请输入用户名"
            >
          </div>
          <div class="form-group">
            <label>个人简介</label>
            <textarea 
              v-model="editForm.bio" 
              maxlength="200"
              placeholder="介绍一下自己吧～"
              rows="3"
            ></textarea>
          </div>
          <div class="form-group">
            <label>邮箱</label>
            <input 
              v-model="editForm.email" 
              type="email" 
              placeholder="请输入邮箱"
            >
          </div>
          <div class="modal-actions">
            <button type="button" class="cancel-btn" @click="cancelEdit">取消</button>
            <button type="submit" class="save-btn">保存</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'

// 用户信息接口
interface UserInfo {
  id: number
  username: string
  email: string
  avatar?: string
  bio?: string
  createdAt: string
}

interface UserStats {
  followers: number
  following: number
  playlists: number
  likes: number
  likedSongs: number
  likedPlaylists: number
}

interface Playlist {
  id: number
  title: string
  description: string
  cover?: string
  songCount: number
  playCount: number
  likeCount: number
  createdAt: string
}

interface ListeningRecord {
  id: number
  songId: number
  songTitle: string
  artist: string
  listenTime: string
}

// 响应式数据
const activeTab = ref('playlists')
const showEditModal = ref(false)

const userInfo = reactive<UserInfo>({
  id: 1,
  username: '音乐爱好者',
  email: 'user@example.com',
  bio: '热爱音乐，享受生活',
  createdAt: '2024-01-01'
})

const userStats = reactive<UserStats>({
  followers: 123,
  following: 45,
  playlists: 8,
  likes: 567,
  likedSongs: 89,
  likedPlaylists: 12
})

const playlists = ref<Playlist[]>([
  {
    id: 1,
    title: '我的最爱',
    description: '收藏的经典歌曲',
    songCount: 25,
    playCount: 156,
    likeCount: 23,
    createdAt: '2024-01-15'
  },
  {
    id: 2,
    title: '工作专注',
    description: '适合工作学习的音乐',
    songCount: 18,
    playCount: 89,
    likeCount: 15,
    createdAt: '2024-02-01'
  }
])

const listeningHistory = ref<ListeningRecord[]>([
  {
    id: 1,
    songId: 101,
    songTitle: '示例歌曲1',
    artist: '艺术家A',
    listenTime: new Date().toISOString()
  },
  {
    id: 2,
    songId: 102,
    songTitle: '示例歌曲2',
    artist: '艺术家B',
    listenTime: new Date(Date.now() - 3600000).toISOString()
  }
])

const editForm = reactive({
  username: '',
  bio: '',
  email: ''
})

const tabs = [
  { id: 'playlists', name: '歌单' },
  { id: 'favorites', name: '收藏' },
  { id: 'history', name: '记录' }
]

// 方法
const editProfile = () => {
  // 填充编辑表单
  editForm.username = userInfo.username
  editForm.bio = userInfo.bio || ''
  editForm.email = userInfo.email
  showEditModal.value = true
}

const saveProfile = async () => {
  try {
    // 调用API更新用户信息
    console.log('更新用户信息:', editForm)
    
    // 更新本地数据
    userInfo.username = editForm.username
    userInfo.bio = editForm.bio
    userInfo.email = editForm.email
    
    showEditModal.value = false
  } catch (error) {
    console.error('保存资料失败:', error)
  }
}

const cancelEdit = () => {
  showEditModal.value = false
}

const editAvatar = () => {
  // 实现头像上传逻辑
  console.log('编辑头像')
}

const openSettings = () => {
  console.log('打开设置')
}

const createPlaylist = () => {
  console.log('创建歌单')
}

const openPlaylist = (playlistId: number) => {
  console.log('打开歌单:', playlistId)
}

const playPlaylist = (playlist: Playlist) => {
  console.log('播放歌单:', playlist.title)
}

const editPlaylist = (playlist: Playlist) => {
  console.log('编辑歌单:', playlist.title)
}

const deletePlaylist = (playlistId: number) => {
  if (confirm('确定要删除这个歌单吗？')) {
    console.log('删除歌单:', playlistId)
  }
}

const playSong = (songId: number) => {
  console.log('播放歌曲:', songId)
}

const formatTime = (timeString: string) => {
  const time = new Date(timeString)
  const now = new Date()
  const diff = now.getTime() - time.getTime()
  
  if (diff < 60000) {
    return '刚刚'
  } else if (diff < 3600000) {
    return `${Math.floor(diff / 60000)}分钟前`
  } else if (diff < 86400000) {
    return `${Math.floor(diff / 3600000)}小时前`
  } else {
    return time.toLocaleDateString('zh-CN')
  }
}

// 生命周期
onMounted(() => {
  // 加载用户数据
  loadUserData()
})

const loadUserData = async () => {
  try {
    // 模拟API调用
    console.log('加载用户数据')
  } catch (error) {
    console.error('加载用户数据失败:', error)
  }
}

// 暴露方法给父组件
defineExpose({
  refreshData: loadUserData
})
</script>

<style scoped>
.user-profile {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.profile-header {
  display: flex;
  gap: 40px;
  align-items: center;
  margin-bottom: 30px;
}

.avatar-section {
  text-align: center;
}

.avatar-container {
  position: relative;
  display: inline-block;
  margin-bottom: 15px;
}

.avatar-image {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
}

.avatar-placeholder {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  color: white;
}

.avatar-edit-btn {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: #667eea;
  border: none;
  color: white;
  cursor: pointer;
}

.username {
  margin: 0 0 10px 0;
  font-size: 24px;
  font-weight: 600;
}

.user-bio {
  margin: 0;
  color: #666;
  max-width: 300px;
}

.stats-section {
  display: flex;
  gap: 30px;
}

.stat-item {
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 24px;
  font-weight: 600;
  color: #667eea;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.profile-actions {
  display: flex;
  gap: 15px;
  margin-bottom: 30px;
}

.edit-profile-btn, .settings-btn {
  padding: 10px 20px;
  border: 1px solid #667eea;
  border-radius: 6px;
  background: white;
  color: #667eea;
  cursor: pointer;
  transition: all 0.3s;
}

.edit-profile-btn:hover, .settings-btn:hover {
  background: #667eea;
  color: white;
}

.content-tabs {
  display: flex;
  border-bottom: 1px solid #e0e0e0;
  margin-bottom: 20px;
}

.tab-btn {
  padding: 12px 24px;
  border: none;
  background: none;
  cursor: pointer;
  border-bottom: 2px solid transparent;
  transition: all 0.3s;
}

.tab-btn.active {
  border-bottom-color: #667eea;
  color: #667eea;
}

.tab-content {
  min-height: 400px;
}

.playlists-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.create-playlist-btn {
  padding: 8px 16px;
  border: 1px solid #667eea;
  border-radius: 6px;
  background: #667eea;
  color: white;
  cursor: pointer;
}

.playlists-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.playlist-card {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 15px;
  cursor: pointer;
  transition: all 0.3s;
}

.playlist-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.playlist-cover {
  position: relative;
  width: 100%;
  height: 120px;
  border-radius: 6px;
  overflow: hidden;
  margin-bottom: 10px;
}

.playlist-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.playlist-cover-placeholder {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  color: white;
}

.playlist-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.playlist-card:hover .playlist-overlay {
  opacity: 1;
}

.play-btn {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: white;
  border: none;
  color: #667eea;
  font-size: 16px;
  cursor: pointer;
}

.song-count {
  position: absolute;
  bottom: 10px;
  right: 10px;
  color: white;
  font-size: 12px;
}

.playlist-info h4 {
  margin: 0 0 5px 0;
}

.playlist-desc {
  margin: 0 0 10px 0;
  color: #666;
  font-size: 14px;
}

.playlist-stats {
  display: flex;
  gap: 15px;
  font-size: 12px;
  color: #999;
}

.playlist-actions {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.action-btn {
  padding: 4px 8px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  background: white;
  cursor: pointer;
  font-size: 12px;
}

.delete-btn {
  color: #ff4757;
  border-color: #ff4757;
}

/* 模态框样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 30px;
  border-radius: 12px;
  max-width: 500px;
  width: 90%;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 600;
}

.form-group input, .form-group textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  font-size: 14px;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
}

.cancel-btn, .save-btn {
  padding: 10px 20px;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  cursor: pointer;
}

.save-btn {
  background: #667eea;
  color: white;
  border-color: #667eea;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .profile-header {
    flex-direction: column;
    text-align: center;
    gap: 20px;
  }
  
  .stats-section {
    gap: 20px;
  }
  
  .playlists-grid {
    grid-template-columns: 1fr;
  }
}
</style>