import axios from 'axios'
import { useUserStore } from '@/stores/user'

// 创建axios实例
const api = axios.create({
  baseURL: '/api', // 使用代理路径
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器 - 添加认证token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    // 添加请求时间戳防止缓存
    if (config.method === 'get') {
      config.params = {
        ...config.params,
        _t: Date.now()
      }
    }
    
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器 - 统一处理错误
api.interceptors.response.use(
  (response) => {
    // 统一处理成功响应 - 匹配后端ApiResponse格式
    if (response.data && response.data.success === true) {
      return response.data.data
    } else {
      // 业务逻辑错误
      return Promise.reject(new Error(response.data?.message || '请求失败'))
    }
  },
  (error) => {
    const userStore = useUserStore()
    
    if (error.response) {
      // 服务器返回错误状态码
      const { status, data } = error.response
      
      switch (status) {
        case 401:
          // token过期或无效，清除本地存储并跳转到登录页
          localStorage.removeItem('token')
          localStorage.removeItem('userInfo')
          userStore.logout()
          
          // 如果不是登录页面，则跳转到登录页
          if (!window.location.pathname.includes('/auth')) {
            window.location.href = '/auth'
          }
          break
          
        case 403:
          // 权限不足
          console.error('权限不足:', data?.message)
          break
          
        case 404:
          // 资源不存在
          console.error('资源不存在:', data?.message)
          break
          
        case 500:
          // 服务器内部错误
          console.error('服务器内部错误:', data?.message)
          break
          
        default:
          console.error('请求错误:', data?.message)
      }
      
      // 返回统一的错误格式
      return Promise.reject({
        code: status,
        message: data?.message || '请求失败',
        data: data?.data
      })
    } else if (error.request) {
      // 请求未收到响应
      console.error('网络错误:', error.message)
      return Promise.reject({
        code: -1,
        message: '网络连接失败，请检查网络设置',
        data: null
      })
    } else {
      // 请求配置错误
      console.error('请求配置错误:', error.message)
      return Promise.reject({
        code: -2,
        message: '请求配置错误',
        data: null
      })
    }
  }
)

// 添加重试机制
const retryApi = async (fn: () => Promise<any>, retries = 3, delay = 1000) => {
  try {
    return await fn()
  } catch (error: any) {
    if (retries > 0 && error.code === -1) {
      // 网络错误时重试
      await new Promise(resolve => setTimeout(resolve, delay))
      return retryApi(fn, retries - 1, delay * 2)
    }
    throw error
  }
}

// 导出带重试的API调用
api.retry = retryApi

export default api