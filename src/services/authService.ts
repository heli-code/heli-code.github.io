import api from '@/utils/api'

export interface LoginRequest {
  username: string
  password: string
}

export interface RegisterRequest {
  username: string
  email: string
  password: string
}

export interface AuthResponse {
  token: string
  type: string
  user: {
    id: number
    username: string
    email: string
    nickname: string
    avatar: string
    createdAt: string
  }
}

export const authService = {
  // 用户登录
  async login(credentials: LoginRequest): Promise<AuthResponse> {
    const response = await api.post('/auth/login', credentials)
    return response
  },

  // 用户注册
  async register(userData: RegisterRequest): Promise<AuthResponse> {
    const response = await api.post('/auth/register', userData)
    return response
  },

  // 获取用户信息
  async getProfile(): Promise<any> {
    const response = await api.get('/auth/profile')
    return response
  },

  // 刷新token
  async refreshToken(): Promise<{ token: string }> {
    const response = await api.post('/auth/refresh')
    return response
  }
}