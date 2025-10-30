import { defineStore } from 'pinia'
import { ref, computed, watch } from 'vue'
import { authService } from '@/services/authService'
import type { LoginRequest, RegisterRequest, AuthResponse } from '@/services/authService'

export interface UserPreferences {
  audioQuality: 'standard' | 'high' | 'lossless'
  autoPlay: boolean
  darkMode: boolean
  showLyrics: boolean
}

export interface UserProfile {
  nickname: string
  bio: string
  gender: 'male' | 'female' | 'other'
  birthday: string
  location: string
  occupation: string
  education: string
  interests: string[]
  favoriteGenre: string
  listeningTime: string
  listeningScenes: string[]
  motto: string
}

export interface UserInfo {
  username: string
  displayName: string
  avatar: string
  phone: string
  email: string
}

export interface UserStats {
  playlists: number
  followers: number
  following: number
}

export const useUserStore = defineStore('user', () => {
  // 认证状态
  const isAuthenticated = ref(false)
  const isLoading = ref(false)
  const authError = ref<string | null>(null)
  
  // 用户基本信息
  const userInfo = ref<UserInfo>({
    username: 'musiclover',
    displayName: '音乐爱好者',
    avatar: '👤',
    phone: '138****8888',
    email: 'user@example.com'
  })

  // 用户统计数据
  const userStats = ref<UserStats>({
    playlists: 12,
    followers: 156,
    following: 89
  })

  // 用户个人资料
  const profile = ref<UserProfile>({
    nickname: '音乐爱好者',
    bio: '热爱音乐，享受生活',
    gender: 'male',
    birthday: '1990-01-01',
    location: '北京',
    occupation: '设计师',
    education: 'bachelor',
    interests: ['music', 'reading', 'travel'],
    favoriteGenre: 'pop',
    listeningTime: 'evening',
    listeningScenes: ['work', 'relax'],
    motto: '音乐是生活的灵魂'
  })

  // 用户偏好设置
  const preferences = ref<UserPreferences>({
    audioQuality: 'high',
    autoPlay: true,
    darkMode: false,
    showLyrics: true
  })

  // 计算属性：是否有未保存的更改
  const hasUnsavedChanges = ref(false)

  // 初始化数据（从本地存储加载）
  const initializeFromStorage = () => {
    const savedProfile = localStorage.getItem('musicflow_profile')
    const savedPreferences = localStorage.getItem('musicflow_preferences')
    const savedAvatar = localStorage.getItem('musicflow_avatar')
    const savedUserInfo = localStorage.getItem('musicflow_userinfo')

    if (savedProfile) {
      profile.value = { ...profile.value, ...JSON.parse(savedProfile) }
    }
    
    if (savedPreferences) {
      preferences.value = { ...preferences.value, ...JSON.parse(savedPreferences) }
    }
    
    if (savedAvatar) {
      userInfo.value.avatar = savedAvatar
    }
    
    if (savedUserInfo) {
      userInfo.value = { ...userInfo.value, ...JSON.parse(savedUserInfo) }
    }
  }

  // 保存所有设置到本地存储
  const saveAllSettings = () => {
    localStorage.setItem('musicflow_profile', JSON.stringify(profile.value))
    localStorage.setItem('musicflow_preferences', JSON.stringify(preferences.value))
    localStorage.setItem('musicflow_avatar', userInfo.value.avatar)
    localStorage.setItem('musicflow_userinfo', JSON.stringify(userInfo.value))
    
    hasUnsavedChanges.value = false
    
    // 触发全局设置更新事件
    window.dispatchEvent(new CustomEvent('userSettingsUpdated'))
  }

  // 更新用户信息
  const updateUserInfo = (newInfo: Partial<UserInfo>) => {
    userInfo.value = { ...userInfo.value, ...newInfo }
    hasUnsavedChanges.value = true
  }

  // 更新个人资料
  const updateProfile = (newProfile: Partial<UserProfile>) => {
    profile.value = { ...profile.value, ...newProfile }
    hasUnsavedChanges.value = true
  }

  // 更新偏好设置
  const updatePreferences = (newPreferences: Partial<UserPreferences>) => {
    preferences.value = { ...preferences.value, ...newPreferences }
    hasUnsavedChanges.value = true
  }

  // 更新头像
  const updateAvatar = (avatar: string) => {
    userInfo.value.avatar = avatar
    hasUnsavedChanges.value = true
  }

  // 获取深色模式状态
  const isDarkMode = computed(() => preferences.value.darkMode)

  // 获取音频质量设置
  const audioQuality = computed(() => preferences.value.audioQuality)

  // 获取自动播放设置
  const autoPlayEnabled = computed(() => preferences.value.autoPlay)

  // 获取歌词显示设置
  const showLyricsEnabled = computed(() => preferences.value.showLyrics)

  // 初始化
  initializeFromStorage()

  return {
    // 状态
    userInfo,
    userStats,
    profile,
    preferences,
    hasUnsavedChanges,
    isAuthenticated,
    isLoading,
    authError,
    
    // 计算属性
    isDarkMode,
    audioQuality,
    autoPlayEnabled,
    showLyricsEnabled,
    
    // 方法
    initializeFromStorage,
    saveAllSettings,
    updateUserInfo,
    updateProfile,
    updatePreferences,
    updateAvatar,
    clearError,
    refreshUserInfo,
    
    // 用户登录
    async login(credentials: LoginRequest) {
      isLoading.value = true
      authError.value = null
      
      try {
        const response = await authService.login(credentials)
        
        // 保存token和用户信息
        localStorage.setItem('token', response.token)
        localStorage.setItem('userInfo', JSON.stringify(response.user))
        
        // 更新store状态
        userInfo.value = {
          username: response.user.username,
          displayName: response.user.nickname || response.user.username,
          avatar: response.user.avatar || '👤',
          phone: '',
          email: response.user.email
        }
        
        isAuthenticated.value = true
        authError.value = null
        
        return { success: true, data: response }
      } catch (error: any) {
        console.error('登录失败:', error)
        authError.value = error.response?.data?.message || '登录失败，请检查用户名和密码'
        isAuthenticated.value = false
        return { success: false, error: authError.value }
      } finally {
        isLoading.value = false
      }
    },
    
    // 用户注册
    async register(userData: RegisterRequest) {
      isLoading.value = true
      authError.value = null
      
      try {
        const response = await authService.register(userData)
        
        // 保存token和用户信息
        localStorage.setItem('token', response.token)
        localStorage.setItem('userInfo', JSON.stringify(response.user))
        
        // 更新store状态
        userInfo.value = {
          username: response.user.username,
          displayName: response.user.nickname || response.user.username,
          avatar: response.user.avatar || '👤',
          phone: '',
          email: response.user.email
        }
        
        isAuthenticated.value = true
        authError.value = null
        
        return { success: true, data: response }
      } catch (error: any) {
        console.error('注册失败:', error)
        authError.value = error.response?.data?.message || '注册失败，请稍后重试'
        isAuthenticated.value = false
        return { success: false, error: authError.value }
      } finally {
        isLoading.value = false
      }
    },
    
    // 用户登出
    logout() {
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      
      // 重置用户信息
      userInfo.value = {
        username: 'musiclover',
        displayName: '音乐爱好者',
        avatar: '👤',
        phone: '138****8888',
        email: 'user@example.com'
      }
      
      isAuthenticated.value = false
      authError.value = null
    },
    
    // 检查登录状态
    checkAuthStatus() {
      const token = localStorage.getItem('token')
      const userInfoStr = localStorage.getItem('userInfo')
      
      if (token && userInfoStr) {
        try {
          const user = JSON.parse(userInfoStr)
          userInfo.value = {
            username: user.username,
            displayName: user.nickname || user.username,
            avatar: user.avatar || '👤',
            phone: '',
            email: user.email
          }
          isAuthenticated.value = true
          return true
        } catch (error) {
          console.error('解析用户信息失败:', error)
          isAuthenticated.value = false
          return false
        }
      }
      isAuthenticated.value = false
      return false
    },
    
    // 设置token
    setToken(token: string) {
      localStorage.setItem('token', token)
      isAuthenticated.value = true
    },
    
    // 设置用户信息
    setUser(user: any) {
      const userInfoStr = JSON.stringify(user)
      localStorage.setItem('userInfo', userInfoStr)
      
      userInfo.value = {
        username: user.username,
        displayName: user.nickname || user.username,
        avatar: user.avatar || '👤',
        phone: '',
        email: user.email
      }
      isAuthenticated.value = true
    },
    
    // 获取token
    get token() {
      return localStorage.getItem('token')
    },
    
    // 获取用户信息
    get user() {
      const userInfoStr = localStorage.getItem('userInfo')
      return userInfoStr ? JSON.parse(userInfoStr) : null
    },
    
    // 检查是否已登录
    get isLoggedIn() {
      return isAuthenticated.value
    },
    
    // 清除错误信息
    clearError() {
      authError.value = null
    },
    
    // 刷新用户信息
    async refreshUserInfo() {
      try {
        const response = await authService.getProfile()
        const user = response.data
        
        userInfo.value = {
          username: user.username,
          displayName: user.nickname || user.username,
          avatar: user.avatar || '👤',
          phone: user.phone || '',
          email: user.email
        }
        
        localStorage.setItem('userInfo', JSON.stringify(user))
        
        return { success: true }
      } catch (error: any) {
        console.error('刷新用户信息失败:', error)
        return { success: false, error: error.response?.data?.message || '刷新失败' }
      }
    }
  }
})