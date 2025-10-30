<template>
  <div class="auth-container">
    <div class="auth-background">
      <div class="background-overlay"></div>
    </div>
    
    <div class="auth-content">
      <div class="auth-card">
        <div class="auth-header">
          <h1 class="auth-title">MusicFlow</h1>
          <p class="auth-subtitle">发现属于你的音乐世界</p>
        </div>
        
        <div class="auth-tabs">
          <button 
            class="tab-btn" 
            :class="{ active: activeTab === 'login' }"
            @click="activeTab = 'login'"
          >
            登录
          </button>
          <button 
            class="tab-btn" 
            :class="{ active: activeTab === 'register' }"
            @click="activeTab = 'register'"
          >
            注册
          </button>
        </div>
        
        <!-- 注册方式选择 -->
        <div v-if="activeTab === 'register'" class="register-type-tabs">
          <button 
            class="register-tab-btn" 
            :class="{ active: registerType === 'email' }"
            @click="registerType = 'email'"
          >
            邮箱注册
          </button>
          <button 
            class="register-tab-btn" 
            :class="{ active: registerType === 'phone' }"
            @click="registerType = 'phone'"
          >
            手机号注册
          </button>
        </div>
        
        <!-- 登录表单 -->
        <form v-if="activeTab === 'login'" class="auth-form" @submit.prevent="handleLogin">
          <div class="form-group">
            <label for="login-username">用户名</label>
            <input 
              id="login-username"
              type="text" 
              v-model="loginForm.username"
              placeholder="请输入邮箱或手机号"
              required
            >
            <div class="input-hint">
              <span class="hint-icon">💡</span>
              支持邮箱或手机号登录
            </div>
          </div>
          
          <div class="form-group">
            <label for="login-password">密码</label>
            <input 
              id="login-password"
              type="password" 
              v-model="loginForm.password"
              placeholder="请输入密码"
              required
            >
          </div>
          
          <div class="form-options">
            <label class="remember-me">
              <input type="checkbox" v-model="loginForm.remember">
              <span>记住我</span>
            </label>
            <a href="#" class="forgot-password">忘记密码？</a>
          </div>
          
          <button type="submit" class="auth-btn primary">登录</button>
          
          <div class="auth-divider">
            <span>或</span>
          </div>
          
          <div class="social-login">
            <button type="button" class="social-btn google">
              <span class="social-icon">🔍</span>
              使用 Google 登录
            </button>
            <button type="button" class="social-btn wechat">
              <span class="social-icon">💬</span>
              使用微信登录
            </button>
          </div>
        </form>
        
        <!-- 邮箱注册表单 -->
        <form v-else-if="activeTab === 'register' && registerType === 'email'" class="auth-form" @submit.prevent="handleEmailRegister">
          <div class="form-group">
            <label for="register-email">邮箱</label>
            <input 
              id="register-email"
              type="email" 
              v-model="emailRegisterForm.email"
              placeholder="请输入邮箱地址"
              required
            >
          </div>
          
          <div class="form-group">
            <label for="register-password">密码</label>
            <input 
              id="register-password"
              type="password" 
              v-model="emailRegisterForm.password"
              placeholder="请输入密码（至少8位）"
              required
              minlength="8"
            >
          </div>
          
          <div class="form-group">
            <label for="register-confirm">确认密码</label>
            <input 
              id="register-confirm"
              type="password" 
              v-model="emailRegisterForm.confirmPassword"
              placeholder="请再次输入密码"
              required
            >
          </div>
          
          <div class="form-options">
            <label class="agree-terms">
              <input type="checkbox" v-model="emailRegisterForm.agreeTerms" required>
              <span>我已阅读并同意 <a href="#">服务条款</a> 和 <a href="#">隐私政策</a></span>
            </label>
          </div>
          
          <button type="submit" class="auth-btn primary">邮箱注册</button>
        </form>
        
        <!-- 手机号注册表单 -->
        <form v-else-if="activeTab === 'register' && registerType === 'phone'" class="auth-form" @submit.prevent="handlePhoneRegister">
          <div class="form-group">
            <label for="register-phone">手机号</label>
            <div class="phone-input-group">
              <select v-model="phoneRegisterForm.countryCode" class="country-code">
                <option value="+86">+86</option>
                <option value="+1">+1</option>
                <option value="+852">+852</option>
                <option value="+853">+853</option>
                <option value="+886">+886</option>
              </select>
              <input 
                id="register-phone"
                type="tel" 
                v-model="phoneRegisterForm.phone"
                placeholder="请输入手机号码"
                required
                maxlength="11"
              >
            </div>
          </div>
          
          <div class="form-group">
            <label for="register-verification">验证码</label>
            <div class="verification-input-group">
              <input 
                id="register-verification"
                type="text" 
                v-model="phoneRegisterForm.verificationCode"
                placeholder="请输入验证码"
                required
                maxlength="6"
              >
              <button 
                type="button" 
                class="send-code-btn"
                :disabled="!canSendCode || countdown > 0"
                @click="sendVerificationCode"
              >
                {{ countdown > 0 ? `${countdown}s后重发` : '获取验证码' }}
              </button>
            </div>
          </div>
          
          <div class="form-group">
            <label for="register-password-phone">密码</label>
            <input 
              id="register-password-phone"
              type="password" 
              v-model="phoneRegisterForm.password"
              placeholder="请输入密码（至少8位）"
              required
              minlength="8"
            >
          </div>
          
          <div class="form-group">
            <label for="register-confirm-phone">确认密码</label>
            <input 
              id="register-confirm-phone"
              type="password" 
              v-model="phoneRegisterForm.confirmPassword"
              placeholder="请再次输入密码"
              required
            >
          </div>
          
          <div class="form-options">
            <label class="agree-terms">
              <input type="checkbox" v-model="phoneRegisterForm.agreeTerms" required>
              <span>我已阅读并同意 <a href="#">服务条款</a> 和 <a href="#">隐私政策</a></span>
            </label>
          </div>
          
          <button type="submit" class="auth-btn primary">手机号注册</button>
        </form>
        
        <div class="auth-footer">
          <p v-if="activeTab === 'login'">
            还没有账号？ <a href="#" @click="activeTab = 'register'">立即注册</a>
          </p>
          <p v-else>
            已有账号？ <a href="#" @click="activeTab = 'login'">立即登录</a>
          </p>
        </div>
      </div>
    </div>
    
    <!-- 返回按钮 -->
    <button class="back-btn" @click="goBack">
      <span class="back-icon">←</span>
      返回
    </button>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 当前激活的标签页
const activeTab = ref<'login' | 'register'>('login')

// 注册类型
const registerType = ref<'email' | 'phone'>('email')

// 登录表单数据
const loginForm = reactive({
  username: '', // 可以是邮箱或手机号
  password: '',
  remember: false
})

// 邮箱注册表单数据
const emailRegisterForm = reactive({
  email: '',
  password: '',
  confirmPassword: '',
  agreeTerms: false
})

// 手机号注册表单数据
const phoneRegisterForm = reactive({
  countryCode: '+86',
  phone: '',
  verificationCode: '',
  password: '',
  confirmPassword: '',
  agreeTerms: false
})

// 验证码倒计时
const countdown = ref(0)
const canSendCode = computed(() => {
  return phoneRegisterForm.phone.length === 11 && /^1[3-9]\d{9}$/.test(phoneRegisterForm.phone)
})

// 发送验证码
const sendVerificationCode = () => {
  if (!canSendCode.value) return
  
  // 模拟发送验证码
  console.log('发送验证码到:', phoneRegisterForm.countryCode + phoneRegisterForm.phone)
  
  // 开始倒计时
  countdown.value = 60
  const timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(timer)
    }
  }, 1000)
}

// 处理登录
const handleLogin = () => {
  console.log('登录信息:', loginForm)
  
  // 模拟登录验证
  const isEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(loginForm.username)
  const isPhone = /^1[3-9]\d{9}$/.test(loginForm.username)
  
  if (!isEmail && !isPhone) {
    alert('请输入有效的邮箱地址或手机号')
    return
  }
  
  // 检查是否已有保存的用户数据
  const existingUserData = localStorage.getItem('musicflow_user')
  let userInfo
  
  if (existingUserData) {
    // 如果已有用户数据，保留原有的个人资料信息
    const existingUser = JSON.parse(existingUserData)
    userInfo = {
      ...existingUser,
      isLoggedIn: true,
      loginType: isEmail ? 'email' : 'phone'
    }
    
    // 检查登录的用户名是否与保存的用户名匹配
    if (existingUser.username !== loginForm.username) {
      // 如果用户名不匹配，说明是不同账号登录，需要重置个人资料
      alert('检测到不同账号登录，个人资料将被重置')
      userInfo = {
        isLoggedIn: true,
        username: loginForm.username,
        displayName: isEmail ? loginForm.username.split('@')[0] : `用户${loginForm.username.slice(-4)}`,
        avatar: '👤',
        loginType: isEmail ? 'email' : 'phone'
      }
    }
  } else {
    // 新用户，创建默认信息
    userInfo = {
      isLoggedIn: true,
      username: loginForm.username,
      displayName: isEmail ? loginForm.username.split('@')[0] : `用户${loginForm.username.slice(-4)}`,
      avatar: '👤',
      loginType: isEmail ? 'email' : 'phone'
    }
  }
  
  // 保存到localStorage
  localStorage.setItem('musicflow_user', JSON.stringify(userInfo))
  
  // 触发全局用户状态更新
  window.dispatchEvent(new CustomEvent('userStatusChanged', { detail: userInfo }))
  
  // 登录成功后跳转到首页
  router.push('/')
}

// 处理邮箱注册
const handleEmailRegister = () => {
  if (emailRegisterForm.password !== emailRegisterForm.confirmPassword) {
    alert('两次输入的密码不一致')
    return
  }
  
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(emailRegisterForm.email)) {
    alert('请输入有效的邮箱地址')
    return
  }
  
  console.log('邮箱注册信息:', emailRegisterForm)
  
  // 检查是否已有保存的用户数据
  const existingUserData = localStorage.getItem('musicflow_user')
  let userInfo
  
  if (existingUserData) {
    // 如果已有用户数据，保留原有的个人资料信息
    const existingUser = JSON.parse(existingUserData)
    userInfo = {
      ...existingUser,
      isLoggedIn: true,
      loginType: 'email'
    }
    
    // 检查注册的用户名是否与保存的用户名匹配
    if (existingUser.username !== emailRegisterForm.email) {
      // 如果用户名不匹配，说明是不同账号注册，需要重置个人资料
      alert('检测到不同账号注册，个人资料将被重置')
      userInfo = {
        isLoggedIn: true,
        username: emailRegisterForm.email,
        displayName: emailRegisterForm.email.split('@')[0],
        avatar: '👤',
        loginType: 'email'
      }
    }
  } else {
    // 新用户，创建默认信息
    userInfo = {
      isLoggedIn: true,
      username: emailRegisterForm.email,
      displayName: emailRegisterForm.email.split('@')[0],
      avatar: '👤',
      loginType: 'email'
    }
  }
  
  // 保存到localStorage
  localStorage.setItem('musicflow_user', JSON.stringify(userInfo))
  
  // 触发全局用户状态更新
  window.dispatchEvent(new CustomEvent('userStatusChanged', { detail: userInfo }))
  
  // 注册成功后跳转到首页
  router.push('/')
}

// 处理手机号注册
const handlePhoneRegister = () => {
  if (phoneRegisterForm.password !== phoneRegisterForm.confirmPassword) {
    alert('两次输入的密码不一致')
    return
  }
  
  if (!/^1[3-9]\d{9}$/.test(phoneRegisterForm.phone)) {
    alert('请输入有效的手机号码')
    return
  }
  
  if (!/^\d{6}$/.test(phoneRegisterForm.verificationCode)) {
    alert('请输入6位验证码')
    return
  }
  
  console.log('手机号注册信息:', phoneRegisterForm)
  
  // 检查是否已有保存的用户数据
  const existingUserData = localStorage.getItem('musicflow_user')
  let userInfo
  
  if (existingUserData) {
    // 如果已有用户数据，保留原有的个人资料信息
    const existingUser = JSON.parse(existingUserData)
    userInfo = {
      ...existingUser,
      isLoggedIn: true,
      loginType: 'phone'
    }
    
    // 检查注册的用户名是否与保存的用户名匹配
    if (existingUser.username !== phoneRegisterForm.phone) {
      // 如果用户名不匹配，说明是不同账号注册，需要重置个人资料
      alert('检测到不同账号注册，个人资料将被重置')
      userInfo = {
        isLoggedIn: true,
        username: phoneRegisterForm.phone,
        displayName: `用户${phoneRegisterForm.phone.slice(-4)}`,
        avatar: '👤',
        loginType: 'phone'
      }
    }
  } else {
    // 新用户，创建默认信息
    userInfo = {
      isLoggedIn: true,
      username: phoneRegisterForm.phone,
      displayName: `用户${phoneRegisterForm.phone.slice(-4)}`,
      avatar: '👤',
      loginType: 'phone'
    }
  }
  
  // 保存到localStorage
  localStorage.setItem('musicflow_user', JSON.stringify(userInfo))
  
  // 触发全局用户状态更新
  window.dispatchEvent(new CustomEvent('userStatusChanged', { detail: userInfo }))
  
  // 注册成功后跳转到首页
  router.push('/')
}

// 返回上一页
const goBack = () => {
  router.back()
}
</script>

<style scoped>
.auth-container {
  min-height: 100vh;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.auth-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="music" x="0" y="0" width="20" height="20" patternUnits="userSpaceOnUse"><circle cx="10" cy="10" r="2" fill="rgba(255,255,255,0.1)"/></pattern></defs><rect width="100" height="100" fill="url(%23music)"/></svg>');
}

.background-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
}

.auth-content {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 400px;
  padding: 2rem;
}

.auth-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 2rem;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
}

.auth-header {
  text-align: center;
  margin-bottom: 2rem;
}

.auth-title {
  font-size: 2.5rem;
  font-weight: bold;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin: 0 0 0.5rem 0;
}

.auth-subtitle {
  color: #666;
  margin: 0;
  font-size: 0.9rem;
}

.auth-tabs {
  display: flex;
  background: #f5f5f5;
  border-radius: 10px;
  padding: 4px;
  margin-bottom: 1rem;
}

.tab-btn {
  flex: 1;
  padding: 0.8rem;
  border: none;
  background: transparent;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
}

.tab-btn.active {
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.register-type-tabs {
  display: flex;
  background: #f0f0f0;
  border-radius: 8px;
  padding: 3px;
  margin-bottom: 2rem;
}

.register-tab-btn {
  flex: 1;
  padding: 0.6rem;
  border: none;
  background: transparent;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
  font-size: 0.9rem;
}

.register-tab-btn.active {
  background: white;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

.auth-form {
  .form-group {
    margin-bottom: 1.5rem;
    
    label {
      display: block;
      margin-bottom: 0.5rem;
      font-weight: 500;
      color: #333;
    }
    
    input {
      width: 100%;
      padding: 0.8rem 1rem;
      border: 2px solid #e0e0e0;
      border-radius: 10px;
      font-size: 1rem;
      transition: border-color 0.3s ease;
      
      &:focus {
        outline: none;
        border-color: #667eea;
      }
    }
    
    .phone-input-group {
      display: flex;
      gap: 0.5rem;
      
      .country-code {
        flex: 0 0 80px;
        padding: 0.8rem 0.5rem;
        border: 2px solid #e0e0e0;
        border-radius: 10px;
        background: white;
        cursor: pointer;
        
        &:focus {
          outline: none;
          border-color: #667eea;
        }
      }
      
      input {
        flex: 1;
      }
    }
    
    .verification-input-group {
      display: flex;
      gap: 0.5rem;
      
      input {
        flex: 1;
      }
      
      .send-code-btn {
        flex: 0 0 120px;
        padding: 0.8rem 0.5rem;
        border: 2px solid #667eea;
        border-radius: 10px;
        background: #667eea;
        color: white;
        font-weight: 500;
        cursor: pointer;
        transition: all 0.3s ease;
        
        &:hover:not(:disabled) {
          background: #5a6fd8;
          border-color: #5a6fd8;
        }
        
        &:disabled {
          background: #cccccc;
          border-color: #cccccc;
          cursor: not-allowed;
        }
      }
    }
  }
  
  .form-options {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1.5rem;
    font-size: 0.9rem;
    
    .remember-me, .agree-terms {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      
      input {
        margin: 0;
      }
    }
    
    .forgot-password {
      color: #667eea;
      text-decoration: none;
      
      &:hover {
        text-decoration: underline;
      }
    }
  }
  
  .auth-btn {
    width: 100%;
    padding: 1rem;
    border: none;
    border-radius: 10px;
    font-size: 1rem;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.3s ease;
    
    &.primary {
      background: linear-gradient(135deg, #667eea, #764ba2);
      color: white;
      
      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
      }
    }
  }
  
  .auth-divider {
    text-align: center;
    margin: 1.5rem 0;
    position: relative;
    
    span {
      background: white;
      padding: 0 1rem;
      color: #999;
      font-size: 0.9rem;
    }
    
    &::before {
      content: '';
      position: absolute;
      top: 50%;
      left: 0;
      right: 0;
      height: 1px;
      background: #e0e0e0;
      z-index: -1;
    }
  }
  
  .social-login {
    display: flex;
    flex-direction: column;
    gap: 0.8rem;
    
    .social-btn {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 0.8rem;
      padding: 0.8rem;
      border: 2px solid #e0e0e0;
      border-radius: 10px;
      background: white;
      cursor: pointer;
      transition: all 0.3s ease;
      font-weight: 500;
      
      &:hover {
        border-color: #667eea;
        transform: translateY(-1px);
      }
      
      &.google {
        .social-icon {
          color: #DB4437;
        }
      }
      
      &.wechat {
        .social-icon {
          color: #07C160;
        }
      }
    }
  }
}

.auth-footer {
  text-align: center;
  margin-top: 1.5rem;
  font-size: 0.9rem;
  color: #666;
  
  a {
    color: #667eea;
    text-decoration: none;
    font-weight: 500;
    
    &:hover {
      text-decoration: underline;
    }
  }
}

.back-btn {
  position: absolute;
  top: 2rem;
  left: 2rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.8rem 1.5rem;
  background: rgba(255, 255, 255, 0.9);
  border: none;
  border-radius: 25px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
  
  &:hover {
    background: white;
    transform: translateX(-5px);
  }
  
  .back-icon {
    font-size: 1.2rem;
  }
}

@media (max-width: 480px) {
  .auth-content {
    padding: 1rem;
  }
  
  .auth-card {
    padding: 1.5rem;
  }
  
  .back-btn {
    top: 1rem;
    left: 1rem;
  }
}
</style>