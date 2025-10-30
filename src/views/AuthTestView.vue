<template>
  <div class="auth-test-container">
    <div class="test-header">
      <h1>用户认证测试</h1>
      <p>测试前后端数据交互和认证流程</p>
    </div>

    <div class="test-sections">
      <!-- 注册测试 -->
      <div class="test-section">
        <h2>1. 用户注册测试</h2>
        <div class="test-form">
          <div class="form-group">
            <label>用户名:</label>
            <input v-model="registerData.username" placeholder="输入用户名" />
          </div>
          <div class="form-group">
            <label>邮箱:</label>
            <input v-model="registerData.email" placeholder="输入邮箱" />
          </div>
          <div class="form-group">
            <label>密码:</label>
            <input v-model="registerData.password" type="password" placeholder="输入密码" />
          </div>
          <button @click="testRegister" :disabled="registerLoading" class="test-btn">
            {{ registerLoading ? '注册中...' : '测试注册' }}
          </button>
        </div>
        <div v-if="registerResult" class="test-result">
          <h4>注册结果:</h4>
          <pre>{{ JSON.stringify(registerResult, null, 2) }}</pre>
        </div>
      </div>

      <!-- 登录测试 -->
      <div class="test-section">
        <h2>2. 用户登录测试</h2>
        <div class="test-form">
          <div class="form-group">
            <label>用户名:</label>
            <input v-model="loginData.username" placeholder="输入用户名" />
          </div>
          <div class="form-group">
            <label>密码:</label>
            <input v-model="loginData.password" type="password" placeholder="输入密码" />
          </div>
          <button @click="testLogin" :disabled="loginLoading" class="test-btn">
            {{ loginLoading ? '登录中...' : '测试登录' }}
          </button>
        </div>
        <div v-if="loginResult" class="test-result">
          <h4>登录结果:</h4>
          <pre>{{ JSON.stringify(loginResult, null, 2) }}</pre>
        </div>
      </div>

      <!-- 获取用户信息测试 -->
      <div class="test-section">
        <h2>3. 获取用户信息测试</h2>
        <button @click="testGetProfile" :disabled="profileLoading" class="test-btn">
          {{ profileLoading ? '获取中...' : '获取用户信息' }}
        </button>
        <div v-if="profileResult" class="test-result">
          <h4>用户信息:</h4>
          <pre>{{ JSON.stringify(profileResult, null, 2) }}</pre>
        </div>
      </div>

      <!-- 状态显示 -->
      <div class="test-section">
        <h2>4. 当前认证状态</h2>
        <div class="status-info">
          <p><strong>Token:</strong> {{ userStore.token ? '已设置' : '未设置' }}</p>
          <p><strong>用户信息:</strong> {{ userStore.user ? '已登录' : '未登录' }}</p>
          <p><strong>登录状态:</strong> {{ userStore.isLoggedIn ? '已登录' : '未登录' }}</p>
        </div>
      </div>
    </div>

    <!-- 错误信息显示 -->
    <div v-if="errorMessage" class="error-message">
      <h3>错误信息:</h3>
      <p>{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useUserStore } from '@/stores/user'
import { authService } from '@/services/authService'

const userStore = useUserStore()

// 测试数据
const registerData = reactive({
  username: '',
  email: '',
  password: ''
})

const loginData = reactive({
  username: '',
  password: ''
})

// 测试状态
const registerLoading = ref(false)
const loginLoading = ref(false)
const profileLoading = ref(false)

// 测试结果
const registerResult = ref<any>(null)
const loginResult = ref<any>(null)
const profileResult = ref<any>(null)
const errorMessage = ref<string>('')

// 测试注册
const testRegister = async () => {
  registerLoading.value = true
  errorMessage.value = ''
  
  try {
    const result = await authService.register(registerData)
    registerResult.value = result
    console.log('注册成功:', result)
  } catch (error: any) {
    errorMessage.value = error.message || '注册失败'
    console.error('注册失败:', error)
  } finally {
    registerLoading.value = false
  }
}

// 测试登录
const testLogin = async () => {
  loginLoading.value = true
  errorMessage.value = ''
  
  try {
    const result = await authService.login(loginData)
    loginResult.value = result
    
    // 更新用户状态
    if (result.token) {
      userStore.setToken(result.token)
      userStore.setUser(result.user)
    }
    
    console.log('登录成功:', result)
  } catch (error: any) {
    errorMessage.value = error.message || '登录失败'
    console.error('登录失败:', error)
  } finally {
    loginLoading.value = false
  }
}

// 测试获取用户信息
const testGetProfile = async () => {
  profileLoading.value = true
  errorMessage.value = ''
  
  try {
    const result = await authService.getProfile()
    profileResult.value = result
    console.log('获取用户信息成功:', result)
  } catch (error: any) {
    errorMessage.value = error.message || '获取用户信息失败'
    console.error('获取用户信息失败:', error)
  } finally {
    profileLoading.value = false
  }
}
</script>

<style scoped>
.auth-test-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
  background: var(--surface-color);
  border-radius: 12px;
  margin-top: 2rem;
}

.test-header {
  text-align: center;
  margin-bottom: 2rem;
}

.test-header h1 {
  color: var(--primary-color);
  margin-bottom: 0.5rem;
}

.test-sections {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.test-section {
  background: rgba(255, 255, 255, 0.05);
  padding: 1.5rem;
  border-radius: 8px;
  border: 1px solid var(--border-color);
}

.test-section h2 {
  color: var(--text-color);
  margin-bottom: 1rem;
  font-size: 1.2rem;
}

.test-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group label {
  font-weight: 600;
  color: var(--text-secondary);
}

.form-group input {
  padding: 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: 6px;
  background: rgba(255, 255, 255, 0.1);
  color: var(--text-color);
  font-size: 1rem;
}

.form-group input:focus {
  outline: none;
  border-color: var(--primary-color);
}

.test-btn {
  padding: 0.75rem 1.5rem;
  background: var(--primary-color);
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.test-btn:hover:not(:disabled) {
  background: #3aa8d9;
  transform: translateY(-1px);
}

.test-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.test-result {
  margin-top: 1rem;
  padding: 1rem;
  background: rgba(0, 0, 0, 0.2);
  border-radius: 6px;
  border-left: 4px solid var(--primary-color);
}

.test-result h4 {
  margin-bottom: 0.5rem;
  color: var(--primary-color);
}

.test-result pre {
  white-space: pre-wrap;
  word-break: break-all;
  font-size: 0.9rem;
  color: var(--text-secondary);
}

.status-info {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.status-info p {
  margin: 0;
  padding: 0.5rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 4px;
}

.error-message {
  margin-top: 2rem;
  padding: 1rem;
  background: rgba(244, 67, 54, 0.1);
  border: 1px solid rgba(244, 67, 54, 0.3);
  border-radius: 6px;
  color: #f44336;
}

.error-message h3 {
  margin-bottom: 0.5rem;
}

@media (max-width: 768px) {
  .auth-test-container {
    padding: 1rem;
    margin-top: 1rem;
  }
  
  .test-section {
    padding: 1rem;
  }
}
</style>