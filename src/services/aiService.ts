import api from '@/utils/api'

export interface AIRecommendation {
  id: number
  title: string
  description: string
  songs: Array<{
    id: number
    title: string
    artist: string
    reason: string
  }>
  createdAt: string
}

export interface AIChatMessage {
  id: number
  role: 'user' | 'assistant'
  content: string
  timestamp: string
}

export interface AIChatRequest {
  message: string
  context?: {
    mood?: string
    activity?: string
    timeOfDay?: string
    recentSongs?: number[]
  }
}

export const aiService = {
  // 获取AI推荐歌单
  async getRecommendations(): Promise<AIRecommendation[]> {
    const response = await api.get('/ai/recommendations')
    return response.data
  },

  // 基于心情获取推荐
  async getMoodRecommendations(mood: string): Promise<AIRecommendation[]> {
    const response = await api.get(`/ai/recommendations/mood?mood=${encodeURIComponent(mood)}`)
    return response.data
  },

  // 与AI聊天获取音乐推荐
  async chatWithAI(request: AIChatRequest): Promise<AIChatMessage> {
    const response = await api.post('/ai/chat', request)
    return response.data
  },

  // 获取聊天历史
  async getChatHistory(): Promise<AIChatMessage[]> {
    const response = await api.get('/ai/chat/history')
    return response.data
  },

  // 分析听歌习惯
  async analyzeListeningHabits(): Promise<{
    favoriteGenres: string[]
    listeningPatterns: string[]
    recommendations: string[]
  }> {
    const response = await api.get('/ai/analysis/habits')
    return response.data
  },

  // 生成个性化歌单
  async generatePersonalPlaylist(theme: string): Promise<{
    playlistId: number
    name: string
    description: string
    songs: number[]
  }> {
    const response = await api.post('/ai/generate-playlist', { theme })
    return response.data
  }
}