<template>
  <div class="profile-view">
    <!-- 顶部导航栏 -->
    <header class="app-header">
      <div class="header-left">
        <BackButton />
        <h1 class="logo">个人资料</h1>
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
        <button class="save-btn" @click="saveAllChanges" :disabled="!hasChanges">
          {{ hasChanges ? '保存更改' : '已保存' }}
        </button>
      </div>
    </header>

    <!-- 主要内容区域 -->
    <main class="profile-content">
      <!-- 用户信息卡片 -->
      <section class="user-card glass fade-in">
        <div class="user-header">
          <div class="avatar-section">
            <div class="avatar-container" @click="editAvatar">
              <div class="avatar">
                <img v-if="userInfo.avatar && userInfo.avatar.startsWith('data:image')" 
                     :src="userInfo.avatar" 
                     alt="用户头像" 
                     class="avatar-image">
                <span v-else class="avatar-placeholder">{{ userInfo.avatar || '👤' }}</span>
              </div>
              <div class="avatar-overlay">
                <span class="edit-icon">📷</span>
              </div>
            </div>
            <button class="change-avatar-btn" @click="editAvatar">更换头像</button>
          </div>
          
          <div class="user-main-info">
            <h2 class="username">{{ userInfo.displayName || '未设置昵称' }}</h2>
            <p class="user-id">@{{ userInfo.username }}</p>
            <p class="user-motto">{{ profileForm.motto || '这个人很懒，什么都没有写...' }}</p>
            
            <div class="user-stats">
              <div class="stat-item">
                <span class="stat-number">{{ userStats.playlists || 0 }}</span>
                <span class="stat-label">歌单</span>
              </div>
              <div class="stat-item">
                <span class="stat-number">{{ userStats.followers || 0 }}</span>
                <span class="stat-label">粉丝</span>
              </div>
              <div class="stat-item">
                <span class="stat-number">{{ userStats.following || 0 }}</span>
                <span class="stat-label">关注</span>
              </div>
              <div class="stat-item">
                <span class="stat-number">{{ userStats.listenTime || '0h' }}</span>
                <span class="stat-label">听歌时长</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 标签页导航 -->
      <nav class="tab-navigation glass fade-in">
        <button 
          v-for="tab in tabs" 
          :key="tab.id"
          class="tab-button" 
          :class="{ active: activeTab === tab.id }"
          @click="activeTab = tab.id"
        >
          <span class="tab-icon">{{ tab.icon }}</span>
          {{ tab.label }}
        </button>
      </nav>

      <!-- 标签页内容 -->
      <div class="tab-content">
        <!-- 基本信息 -->
        <section v-if="activeTab === 'basic'" class="tab-panel glass fade-in">
          <div class="panel-header">
            <h3>基本信息</h3>
            <p>完善您的个人资料信息</p>
          </div>
          
          <div class="form-grid two-columns">
            <!-- 第一排：基础信息 -->
            <div class="form-group">
              <label class="form-label">昵称</label>
              <input 
                type="text" 
                v-model="profileForm.nickname"
                placeholder="请输入昵称"
                class="input"
                maxlength="20"
              >
              <div class="form-hint">昵称将显示给其他用户</div>
            </div>
            
            <div class="form-group">
              <label class="form-label">个性签名</label>
              <input 
                type="text" 
                v-model="profileForm.motto"
                placeholder="一句话描述自己"
                class="input"
                maxlength="50"
              >
              <div class="form-hint">{{ profileForm.motto?.length || 0 }}/50</div>
            </div>
            
            <div class="form-group">
              <label class="form-label">性别</label>
              <div class="radio-group">
                <label class="radio-option">
                  <input type="radio" v-model="profileForm.gender" value="male">
                  <span class="radio-label">男</span>
                </label>
                <label class="radio-option">
                  <input type="radio" v-model="profileForm.gender" value="female">
                  <span class="radio-label">女</span>
                </label>
                <label class="radio-option">
                  <input type="radio" v-model="profileForm.gender" value="other">
                  <span class="radio-label">其他</span>
                </label>
              </div>
            </div>
            
            <div class="form-group">
              <label class="form-label">生日</label>
              <input 
                type="date" 
                v-model="profileForm.birthday"
                class="input"
              >
            </div>
            
            <!-- 第二排：详细信息 -->
            <div class="form-group">
              <label class="form-label">职业</label>
              <input 
                type="text" 
                v-model="profileForm.occupation"
                placeholder="请输入您的职业"
                class="input"
                maxlength="30"
              >
            </div>
            
            <div class="form-group">
              <label class="form-label">学历</label>
              <select v-model="profileForm.education" class="input">
                <option value="">请选择学历</option>
                <option value="high_school">高中</option>
                <option value="college">大专</option>
                <option value="bachelor">本科</option>
                <option value="master">硕士</option>
                <option value="doctor">博士</option>
                <option value="other">其他</option>
              </select>
            </div>
            
            <div class="form-group">
              <label class="form-label">所在地</label>
              <input 
                type="text" 
                v-model="profileForm.location"
                placeholder="请输入所在城市"
                class="input"
              >
            </div>
            
            <div class="form-group">
              <label class="form-label">手机号</label>
              <input 
                type="tel" 
                v-model="profileForm.phone"
                placeholder="请输入手机号码"
                class="input"
                maxlength="11"
              >
              <div class="form-hint">用于账号安全验证</div>
            </div>
            
            <!-- 全宽字段 -->
            <div class="form-group full-width">
              <label class="form-label">个人简介</label>
              <textarea 
                v-model="profileForm.bio"
                placeholder="介绍一下自己..."
                class="input textarea"
                maxlength="200"
                rows="3"
              ></textarea>
              <div class="form-hint">{{ profileForm.bio?.length || 0 }}/200</div>
            </div>
            
            <div class="form-group full-width">
              <label class="form-label">兴趣爱好</label>
              <div class="interests-grid">
                <label class="interest-option" v-for="interest in interestOptions" :key="interest.value">
                  <input type="checkbox" :value="interest.value" v-model="profileForm.interests">
                  <span class="interest-label">{{ interest.label }}</span>
                </label>
              </div>
              <div class="form-hint">最多选择5个兴趣爱好</div>
            </div>
          </div>
        </section>

        <!-- 音乐偏好 -->
        <section v-if="activeTab === 'music'" class="tab-panel glass fade-in">
          <div class="panel-header">
            <h3>音乐偏好</h3>
            <p>个性化您的音乐体验</p>
          </div>
          
          <div class="preference-grid">
            <div class="preference-item">
              <div class="preference-info">
                <h4>最喜欢的音乐类型</h4>
                <p>选择您最常听的音乐风格</p>
              </div>
              <select v-model="profileForm.favoriteGenre" class="input">
                <option value="">请选择音乐类型</option>
                <option value="pop">流行音乐</option>
                <option value="rock">摇滚</option>
                <option value="hiphop">嘻哈</option>
                <option value="electronic">电子音乐</option>
                <option value="classical">古典音乐</option>
                <option value="jazz">爵士</option>
                <option value="folk">民谣</option>
                <option value="country">乡村音乐</option>
                <option value="rnb">R&B</option>
                <option value="other">其他</option>
              </select>
            </div>
            
            <div class="preference-item">
              <div class="preference-info">
                <h4>听歌时间</h4>
                <p>您通常在什么时间听歌</p>
              </div>
              <select v-model="profileForm.listeningTime" class="input">
                <option value="">请选择听歌时间</option>
                <option value="morning">早晨</option>
                <option value="afternoon">下午</option>
                <option value="evening">晚上</option>
                <option value="night">深夜</option>
                <option value="all_day">全天</option>
              </select>
            </div>
            
            <div class="preference-item full-width">
              <div class="preference-info">
                <h4>听歌场景</h4>
                <p>选择您听歌的常见场景</p>
              </div>
              <div class="scene-checkboxes">
                <label class="scene-option" v-for="scene in sceneOptions" :key="scene.value">
                  <input type="checkbox" :value="scene.value" v-model="profileForm.listeningScenes">
                  <span class="scene-label">{{ scene.label }}</span>
                </label>
              </div>
            </div>
            
            <div class="preference-item">
              <div class="preference-info">
                <h4>音乐品质</h4>
                <p>选择您偏好的音频质量</p>
              </div>
              <select v-model="preferences.audioQuality" class="input">
                <option value="standard">标准 (128kbps)</option>
                <option value="high">高 (192kbps)</option>
                <option value="lossless">无损 (320kbps)</option>
              </select>
            </div>
            
            <div class="preference-item">
              <div class="preference-info">
                <h4>自动播放</h4>
                <p>歌曲结束后自动播放下一首</p>
              </div>
              <label class="switch">
                <input type="checkbox" v-model="preferences.autoPlay">
                <span class="slider"></span>
              </label>
            </div>
            
            <div class="preference-item">
              <div class="preference-info">
                <h4>深色模式</h4>
                <p>启用深色主题</p>
              </div>
              <label class="switch">
                <input type="checkbox" v-model="preferences.darkMode">
                <span class="slider"></span>
              </label>
            </div>
            
            <div class="preference-item">
              <div class="preference-info">
                <h4>显示歌词</h4>
                <p>播放时显示歌词</p>
              </div>
              <label class="switch">
                <input type="checkbox" v-model="preferences.showLyrics">
                <span class="slider"></span>
              </label>
            </div>
          </div>
        </section>

        <!-- 账号安全 -->
        <section v-if="activeTab === 'security'" class="tab-panel glass fade-in">
          <div class="panel-header">
            <h3>账号安全</h3>
            <p>管理您的账号安全设置</p>
          </div>
          
          <div class="security-grid">
            <div class="security-item">
              <div class="security-info">
                <h4>修改密码</h4>
                <p>定期更换密码保护账号安全</p>
              </div>
              <button class="btn">修改密码</button>
            </div>
            
            <div class="security-item">
              <div class="security-info">
                <h4>手机绑定</h4>
                <p>已绑定手机：{{ userInfo.phone || '未绑定' }}</p>
              </div>
              <button class="btn">{{ userInfo.phone ? '更换手机' : '绑定手机' }}</button>
            </div>
            
            <div class="security-item">
              <div class="security-info">
                <h4>邮箱绑定</h4>
                <p>已绑定邮箱：{{ userInfo.email || '未绑定' }}</p>
              </div>
              <button class="btn">{{ userInfo.email ? '更换邮箱' : '绑定邮箱' }}</button>
            </div>
            
            <div class="security-item danger">
              <div class="security-info">
                <h4>账号注销</h4>
                <p>永久删除账号及所有数据</p>
              </div>
              <button class="btn danger">注销账号</button>
            </div>
          </div>
        </section>

        <!-- 音乐统计 -->
        <section v-if="activeTab === 'stats'" class="tab-panel glass fade-in">
          <div class="panel-header">
            <h3>音乐统计</h3>
            <p>您的音乐收听数据分析</p>
          </div>
          
          <div class="stats-grid">
            <div class="stat-card">
              <div class="stat-icon">🎵</div>
              <div class="stat-content">
                <h4>总听歌时长</h4>
                <p class="stat-value">{{ userStats.totalListenTime || '0小时' }}</p>
                <p class="stat-desc">累计听歌时间</p>
              </div>
            </div>
            
            <div class="stat-card">
              <div class="stat-icon">❤️</div>
              <div class="stat-content">
                <h4>收藏歌曲</h4>
                <p class="stat-value">{{ userStats.likedSongs || 0 }}首</p>
                <p class="stat-desc">您喜欢的歌曲数量</p>
              </div>
            </div>
            
            <div class="stat-card">
              <div class="stat-icon">📊</div>
              <div class="stat-content">
                <h4>活跃天数</h4>
                <p class="stat-value">{{ userStats.activeDays || 0 }}天</p>
                <p class="stat-desc">本月活跃天数</p>
              </div>
            </div>
            
            <div class="stat-card">
              <div class="stat-icon">🎧</div>
              <div class="stat-content">
                <h4>最近收听</h4>
                <p class="stat-value">{{ userStats.recentPlays || 0 }}首</p>
                <p class="stat-desc">最近7天播放</p>
              </div>
            </div>
          </div>
          
          <div class="chart-section">
            <h4>听歌趋势</h4>
            <div class="chart-placeholder">
              <p>听歌趋势图表将在这里显示</p>
              <p class="chart-hint">（图表功能开发中）</p>
            </div>
          </div>
        </section>
      </div>
    </main>

    <!-- 头像编辑模态框 -->
    <div v-if="showAvatarModal" class="modal-overlay" @click="closeAvatarModal">
      <div class="modal-content glass" @click.stop>
        <div class="modal-header">
          <h3>选择头像</h3>
          <button class="close-button" @click="closeAvatarModal">×</button>
        </div>
        
        <div class="modal-body">
          <div class="avatar-options">
            <h4>默认头像</h4>
            <div class="avatar-grid">
              <div 
                v-for="emoji in defaultAvatars" 
                :key="emoji"
                class="avatar-option"
                :class="{ selected: selectedAvatar === emoji }"
                @click="selectAvatar(emoji)"
              >
                {{ emoji }}
              </div>
            </div>
            
            <h4>自定义头像</h4>
            <div class="custom-avatar">
              <div class="avatar-preview">
                <img v-if="customAvatar" :src="customAvatar" alt="预览" class="preview-image">
                <span v-else class="preview-placeholder">📷</span>
              </div>
              <div class="upload-section">
                <input 
                  type="file" 
                  ref="fileInput"
                  accept="image/*" 
                  @change="handleFileUpload"
                  class="file-input"
                >
                <button class="btn" @click="triggerFileInput">选择图片</button>
                <p class="upload-hint">支持 JPG、PNG 格式，最大 2MB</p>
              </div>
            </div>
          </div>
        </div>
        
        <div class="modal-footer">
          <button class="btn secondary" @click="closeAvatarModal">取消</button>
          <button class="btn primary" @click="saveAvatar" :disabled="!selectedAvatar && !customAvatar">
            保存头像
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import BackButton from '@/components/BackButton.vue'

const router = useRouter()

// 标签页配置
const tabs = [
  { id: 'basic', label: '基本信息', icon: '👤' },
  { id: 'music', label: '音乐偏好', icon: '🎵' },
  { id: 'stats', label: '音乐统计', icon: '📊' },
  { id: 'security', label: '账号安全', icon: '🔒' }
]

const activeTab = ref('basic')

// 用户信息
const userInfo = reactive({
  username: 'musiclover',
  displayName: '音乐爱好者',
  avatar: '👤',
  phone: '138****8888',
  email: 'user@example.com'
})

// 用户统计数据
const userStats = reactive({
  playlists: 12,
  followers: 156,
  following: 89,
  listenTime: '128h',
  totalListenTime: '256小时',
  likedSongs: 45,
  activeDays: 28,
  recentPlays: 67
})

// 表单数据
const profileForm = reactive({
  nickname: '音乐爱好者',
  bio: '热爱音乐，享受生活。喜欢在深夜听一些轻音乐，放松心情。',
  gender: 'male',
  birthday: '1990-01-01',
  location: '北京',
  occupation: '设计师',
  education: 'bachelor',
  interests: ['music', 'reading', 'travel'],
  favoriteGenre: 'pop',
  listeningTime: 'evening',
  listeningScenes: ['work', 'relax'],
  motto: '音乐是生活的灵魂',
  phone: '13888888888'
})

// 偏好设置
const preferences = reactive({
  audioQuality: 'high',
  autoPlay: true,
  darkMode: false,
  showLyrics: true
})

// 兴趣爱好选项
const interestOptions = [
  { value: 'music', label: '音乐' },
  { value: 'reading', label: '阅读' },
  { value: 'travel', label: '旅行' },
  { value: 'sports', label: '运动' },
  { value: 'photography', label: '摄影' },
  { value: 'cooking', label: '烹饪' },
  { value: 'gaming', label: '游戏' },
  { value: 'movies', label: '电影' },
  { value: 'art', label: '艺术' },
  { value: 'technology', label: '科技' },
  { value: 'fashion', label: '时尚' },
  { value: 'food', label: '美食' }
]

// 听歌场景选项
const sceneOptions = [
  { value: 'work', label: '工作学习' },
  { value: 'commute', label: '通勤路上' },
  { value: 'exercise', label: '运动健身' },
  { value: 'relax', label: '休闲放松' },
  { value: 'sleep', label: '睡前助眠' },
  { value: 'party', label: '聚会派对' }
]

// 计算是否有更改
const hasChanges = computed(() => {
  return profileForm.nickname !== userInfo.displayName || 
         profileForm.bio !== '热爱音乐，享受生活' ||
         preferences.audioQuality !== 'high'
})

// 保存所有更改
const saveAllChanges = () => {
  userInfo.displayName = profileForm.nickname
  // 模拟保存到本地存储
  localStorage.setItem('musicflow_profile', JSON.stringify(profileForm))
  localStorage.setItem('musicflow_preferences', JSON.stringify(preferences))
  
  // 显示保存成功提示
  showSaveSuccess('保存成功！')
}

// 显示保存成功提示
const showSaveSuccess = (message: string) => {
  const saveBtn = document.querySelector('.save-btn') as HTMLElement
  if (saveBtn) {
    const originalText = saveBtn.textContent
    const originalBg = saveBtn.style.background
    
    saveBtn.textContent = '✓ ' + message
    saveBtn.style.background = 'linear-gradient(135deg, #4CAF50, #45a049)'
    
    setTimeout(() => {
      saveBtn.textContent = originalText
      saveBtn.style.background = originalBg
    }, 2000)
  }
}

// 头像相关功能
const showAvatarModal = ref(false)
const selectedAvatar = ref('')
const customAvatar = ref('')
const fileInput = ref<HTMLInputElement>()

const defaultAvatars = ['👤', '👨', '👩', '🧑', '👦', '👧', '👨‍💼', '👩‍💼', '👨‍🎓', '👩‍🎓', '👨‍🍳', '👩‍🍳']

const editAvatar = () => {
  showAvatarModal.value = true
  selectedAvatar.value = userInfo.avatar
  customAvatar.value = ''
}

const closeAvatarModal = () => {
  showAvatarModal.value = false
}

const selectAvatar = (emoji: string) => {
  selectedAvatar.value = emoji
  customAvatar.value = ''
}

const triggerFileInput = () => {
  fileInput.value?.click()
}

const handleFileUpload = (event: Event) => {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  
  if (file) {
    if (file.size > 2 * 1024 * 1024) {
      alert('文件大小不能超过 2MB')
      return
    }
    
    const reader = new FileReader()
    reader.onload = (e) => {
      customAvatar.value = e.target?.result as string
      selectedAvatar.value = ''
    }
    reader.readAsDataURL(file)
  }
}

const saveAvatar = () => {
  if (selectedAvatar.value) {
    userInfo.avatar = selectedAvatar.value
  } else if (customAvatar.value) {
    userInfo.avatar = customAvatar.value
  }
  
  localStorage.setItem('musicflow_avatar', userInfo.avatar)
  closeAvatarModal()
  showSaveSuccess('头像保存成功！')
}

// 初始化数据
onMounted(() => {
  const savedProfile = localStorage.getItem('musicflow_profile')
  const savedPreferences = localStorage.getItem('musicflow_preferences')
  const savedAvatar = localStorage.getItem('musicflow_avatar')
  
  if (savedProfile) {
    Object.assign(profileForm, JSON.parse(savedProfile))
  }
  
  if (savedPreferences) {
    Object.assign(preferences, JSON.parse(savedPreferences))
  }
  
  if (savedAvatar) {
    userInfo.avatar = savedAvatar
  }
})

// 监听表单变化自动保存
watch([profileForm, preferences], () => {
  // 可以添加防抖保存逻辑
}, { deep: true })
</script>

<style scoped>
.profile-view {
  min-height: 100vh;
  background: #0f1419;
  color: white;
  display: flex;
  flex-direction: column;
}

/* 顶部导航栏 */
.app-header {
  background: rgba(15, 20, 25, 0.95);
  backdrop-filter: blur(10px);
  padding: 1rem 2rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.logo {
  font-size: 1.5rem;
  font-weight: 600;
  background: linear-gradient(45deg, #4cc9f0, #f72585);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.header-center {
  flex: 1;
  display: flex;
  justify-content: center;
}

.main-nav {
  display: flex;
  gap: 2rem;
}

.nav-item {
  color: rgba(255, 255, 255, 0.7);
  text-decoration: none;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.nav-item:hover,
.nav-item.router-link-active {
  color: white;
  background: rgba(76, 201, 240, 0.2);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.save-btn {
  background: #4cc9f0;
  color: white;
  border: none;
  padding: 0.6rem 1.5rem;
  border-radius: 20px;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.3s ease;
  min-width: 100px;
}

.save-btn:hover:not(:disabled) {
  background: #3ab0d9;
}

.save-btn:disabled {
  background: rgba(255, 255, 255, 0.2);
  cursor: not-allowed;
}

/* 主要内容区域 */
.profile-content {
  flex: 1;
  padding: 2rem;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

/* 用户信息卡片 */
.user-card {
  border-radius: 16px;
  padding: 2rem;
}

.user-header {
  display: flex;
  align-items: flex-start;
  gap: 2rem;
}

.avatar-section {
  text-align: center;
}

.avatar-container {
  position: relative;
  display: inline-block;
  cursor: pointer;
  margin-bottom: 1rem;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: linear-gradient(135deg, #4cc9f0, #f72585);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  border: 4px solid rgba(255, 255, 255, 0.2);
}

.avatar-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  font-size: 3rem;
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.avatar-container:hover .avatar-overlay {
  opacity: 1;
}

.edit-icon {
  font-size: 1.5rem;
  color: white;
}

.change-avatar-btn {
  background: rgba(76, 201, 240, 0.2);
  color: #4cc9f0;
  border: 1px solid #4cc9f0;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.change-avatar-btn:hover {
  background: #4cc9f0;
  color: white;
}

.user-main-info {
  flex: 1;
}

.username {
  margin: 0 0 0.5rem 0;
  font-size: 2rem;
  font-weight: 600;
}

.user-id {
  margin: 0 0 0.5rem 0;
  color: rgba(255, 255, 255, 0.7);
  font-size: 1rem;
}

.user-motto {
  margin: 0 0 1.5rem 0;
  color: rgba(255, 255, 255, 0.8);
  font-style: italic;
}

.user-stats {
  display: flex;
  gap: 2rem;
}

.stat-item {
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 1.5rem;
  font-weight: 600;
  color: #4cc9f0;
}

.stat-label {
  font-size: 0.9rem;
  color: rgba(255, 255, 255, 0.7);
}

/* 标签页导航 */
.tab-navigation {
  border-radius: 12px;
  padding: 0.5rem;
  display: flex;
  gap: 0.5rem;
}

.tab-button {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  background: none;
  border: none;
  padding: 1rem;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 1rem;
  color: rgba(255, 255, 255, 0.7);
}

.tab-button:hover {
  background: rgba(76, 201, 240, 0.1);
}

.tab-button.active {
  background: rgba(76, 201, 240, 0.2);
  color: #4cc9f0;
}

.tab-icon {
  font-size: 1.2rem;
}

/* 标签页内容 */
.tab-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  overflow-y: auto;
  max-height: calc(100vh - 200px);
  scrollbar-width: thin;
  scrollbar-color: rgba(76, 201, 240, 0.5) rgba(255, 255, 255, 0.1);
}

.tab-content::-webkit-scrollbar {
  width: 6px;
}

.tab-content::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 3px;
}

.tab-content::-webkit-scrollbar-thumb {
  background: rgba(76, 201, 240, 0.5);
  border-radius: 3px;
}

.tab-content::-webkit-scrollbar-thumb:hover {
  background: rgba(76, 201, 240, 0.7);
}

.tab-panel {
  border-radius: 16px;
  padding: 2rem;
  min-height: fit-content;
}

.panel-header {
  margin-bottom: 2rem;
}

.panel-header h3 {
  margin: 0 0 0.5rem 0;
  font-size: 1.5rem;
  color: white;
}

.panel-header p {
  margin: 0;
  color: rgba(255, 255, 255, 0.7);
}

/* 表单网格 */
.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
}

.form-grid.two-columns {
  grid-template-columns: repeat(2, 1fr);
  gap: 1.2rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
  padding: 1.2rem;
  background: rgba(255, 255, 255, 0.02);
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.08);
  transition: all 0.3s ease;
  min-height: 120px;
}

.form-group:hover {
  background: rgba(255, 255, 255, 0.04);
  border-color: rgba(76, 201, 240, 0.4);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

.form-group.full-width {
  min-height: auto;
  padding: 1.5rem;
}

.form-group.full-width {
  grid-column: 1 / -1;
}

.form-label {
  font-weight: 500;
  color: white;
  margin-bottom: 0.3rem;
}

/* 输入框和选择框样式 */
.input {
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 8px;
  padding: 0.8rem 1rem;
  color: white;
  font-size: 1rem;
  transition: all 0.3s ease;
  outline: none;
}

.input:focus {
  border-color: #4cc9f0;
  box-shadow: 0 0 0 2px rgba(76, 201, 240, 0.2);
  background: rgba(255, 255, 255, 0.12);
}

.input::placeholder {
  color: rgba(255, 255, 255, 0.5);
}

/* 选择框选项样式 */
select.input {
  appearance: none;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='white'%3E%3Cpath d='M7 10l5 5 5-5z'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 1rem center;
  background-size: 16px;
  padding-right: 3rem;
}

select.input option {
  background: #1a1f2e;
  color: white;
  padding: 0.5rem;
}

/* 文本域样式 */
.textarea {
  resize: vertical;
  min-height: 100px;
  font-family: inherit;
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 8px;
  padding: 0.8rem 1rem;
  color: white;
  font-size: 1rem;
  transition: all 0.3s ease;
  outline: none;
}

.textarea:focus {
  border-color: #4cc9f0;
  box-shadow: 0 0 0 2px rgba(76, 201, 240, 0.2);
  background: rgba(255, 255, 255, 0.12);
}

.textarea::placeholder {
  color: rgba(255, 255, 255, 0.5);
}

.form-hint {
  font-size: 0.85rem;
  color: rgba(255, 255, 255, 0.5);
}

.radio-group {
  display: flex;
  gap: 1rem;
}

.radio-option {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
}

.radio-option input[type="radio"] {
  appearance: none;
  width: 18px;
  height: 18px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  background: transparent;
  position: relative;
  cursor: pointer;
  transition: all 0.3s ease;
}

.radio-option input[type="radio"]:checked {
  border-color: #4cc9f0;
  background: #4cc9f0;
}

.radio-option input[type="radio"]:checked::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 8px;
  height: 8px;
  background: white;
  border-radius: 50%;
}

.radio-label {
  color: white;
  font-size: 0.95rem;
}

/* 兴趣爱好网格 */
.interests-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 0.8rem;
  margin-bottom: 0.5rem;
}

.interest-option {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.6rem 1rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.interest-option:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: #4cc9f0;
}

.interest-option input[type="checkbox"] {
  appearance: none;
  width: 18px;
  height: 18px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 4px;
  background: transparent;
  position: relative;
  cursor: pointer;
  transition: all 0.3s ease;
}

.interest-option input[type="checkbox"]:checked {
  border-color: #4cc9f0;
  background: #4cc9f0;
}

.interest-option input[type="checkbox"]:checked::after {
  content: '✓';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: white;
  font-size: 12px;
  font-weight: bold;
}

.interest-option input[type="checkbox"]:checked + .interest-label {
  color: #4cc9f0;
  font-weight: 500;
}

.interest-label {
  color: rgba(255, 255, 255, 0.8);
  font-size: 0.9rem;
}

/* 偏好设置网格 */
.preference-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
}

.preference-item {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 1.5rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
}

.preference-item.full-width {
  grid-column: 1 / -1;
}

.preference-info h4 {
  margin: 0 0 0.3rem 0;
  color: white;
}

.preference-info p {
  margin: 0;
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.9rem;
}

/* 听歌场景复选框 */
.scene-checkboxes {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  gap: 0.8rem;
}

.scene-option {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 0.8rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.1);
  font-size: 0.85rem;
}

.scene-option:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: #4cc9f0;
}

.scene-option input[type="checkbox"] {
  appearance: none;
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 3px;
  background: transparent;
  position: relative;
  cursor: pointer;
  transition: all 0.3s ease;
}

.scene-option input[type="checkbox"]:checked {
  border-color: #4cc9f0;
  background: #4cc9f0;
}

.scene-option input[type="checkbox"]:checked::after {
  content: '✓';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: white;
  font-size: 10px;
  font-weight: bold;
}

.scene-option input[type="checkbox"]:checked + .scene-label {
  color: #4cc9f0;
  font-weight: 500;
}

.scene-label {
  color: rgba(255, 255, 255, 0.8);
}

/* 开关样式 */
.switch {
  position: relative;
  display: inline-block;
  width: 50px;
  height: 24px;
}

.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.2);
  transition: .4s;
  border-radius: 24px;
}

.slider:before {
  position: absolute;
  content: "";
  height: 16px;
  width: 16px;
  left: 4px;
  bottom: 4px;
  background-color: white;
  transition: .4s;
  border-radius: 50%;
}

input:checked + .slider {
  background-color: #4cc9f0;
}

input:checked + .slider:before {
  transform: translateX(26px);
}

/* 安全设置网格 */
.security-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1rem;
}

.security-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1.5rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  border-left: 4px solid #4cc9f0;
}

.security-item.danger {
  border-left-color: #f72585;
}

.security-info h4 {
  margin: 0 0 0.3rem 0;
  color: white;
}

.security-info p {
  margin: 0;
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.9rem;
}

/* 统计卡片网格 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1.5rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
}

.stat-icon {
  font-size: 2rem;
}

.stat-content h4 {
  margin: 0 0 0.5rem 0;
  color: white;
  font-size: 1rem;
}

.stat-value {
  margin: 0 0 0.3rem 0;
  font-size: 1.5rem;
  font-weight: 600;
  color: #4cc9f0;
}

.stat-desc {
  margin: 0;
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.9rem;
}

/* 图表区域 */
.chart-section {
  padding: 1.5rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
}

.chart-section h4 {
  margin: 0 0 1rem 0;
  color: white;
}

.chart-placeholder {
  text-align: center;
  padding: 3rem 2rem;
  background: rgba(255, 255, 255, 0.02);
  border-radius: 8px;
  border: 2px dashed rgba(255, 255, 255, 0.1);
}

.chart-hint {
  color: rgba(255, 255, 255, 0.5);
  font-size: 0.9rem;
  margin-top: 0.5rem;
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
  border-radius: 16px;
  width: 90%;
  max-width: 500px;
  max-height: 80vh;
  overflow: hidden;
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1.5rem 2rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.modal-header h3 {
  margin: 0;
  color: white;
}

.close-button {
  background: none;
  border: none;
  font-size: 2rem;
  cursor: pointer;
  color: rgba(255, 255, 255, 0.7);
  line-height: 1;
}

.close-button:hover {
  color: white;
}

.modal-body {
  padding: 2rem;
  max-height: 60vh;
  overflow-y: auto;
}

.avatar-options h4 {
  margin: 0 0 1rem 0;
  color: white;
}

.avatar-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 0.5rem;
  margin-bottom: 2rem;
}

.avatar-option {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.1);
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 1.5rem;
}

.avatar-option:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: scale(1.1);
}

.avatar-option.selected {
  background: #4cc9f0;
  color: white;
  transform: scale(1.1);
}

.custom-avatar {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.avatar-preview {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  border: 2px dashed rgba(255, 255, 255, 0.2);
}

.preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.preview-placeholder {
  font-size: 2rem;
  color: rgba(255, 255, 255, 0.5);
}

.upload-section {
  flex: 1;
}

.file-input {
  display: none;
}

.upload-hint {
  margin: 0;
  color: rgba(255, 255, 255, 0.5);
  font-size: 0.85rem;
}

.modal-footer {
  display: flex;
  gap: 1rem;
  padding: 1.5rem 2rem;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.btn.secondary {
  background: rgba(255, 255, 255, 0.1);
  color: rgba(255, 255, 255, 0.8);
}

.btn.secondary:hover {
  background: rgba(255, 255, 255, 0.2);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .app-header {
    padding: 1rem;
    flex-direction: column;
    gap: 1rem;
  }
  
  .main-nav {
    gap: 1rem;
  }
  
  .profile-content {
    padding: 1rem;
  }
  
  .user-header {
    flex-direction: column;
    text-align: center;
    gap: 1.5rem;
  }
  
  .user-stats {
    justify-content: center;
  }
  
  .tab-navigation {
    flex-direction: column;
  }
  
  .form-grid,
  .form-grid.two-columns,
  .preference-grid,
  .security-grid,
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .interests-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .scene-checkboxes {
    grid-template-columns: 1fr;
  }
  
  .avatar-grid {
    grid-template-columns: repeat(4, 1fr);
  }
  
  .custom-avatar {
    flex-direction: column;
    text-align: center;
  }
  
  .modal-footer {
    flex-direction: column;
  }
}

/* 中等屏幕响应式 */
@media (max-width: 1024px) and (min-width: 769px) {
  .form-grid.two-columns {
    grid-template-columns: 1fr;
  }
}

/* 动画效果 */
.fade-in {
  animation: fadeIn 0.5s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>