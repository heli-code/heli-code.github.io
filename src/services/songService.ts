import api from '@/utils/api'

export interface Song {
  id: number
  title: string
  artist: string
  album: string
  duration: number
  coverUrl: string
  audioUrl: string
  playCount: number
  likeCount: number
  createdAt: string
}

export const songService = {
  // 获取所有歌曲
  async getSongs(): Promise<Song[]> {
    const response = await api.get('/songs')
    return response.data
  },

  // 搜索歌曲
  async searchSongs(keyword: string): Promise<Song[]> {
    const response = await api.get(`/songs/search?keyword=${encodeURIComponent(keyword)}`)
    return response.data
  },

  // 获取热门歌曲
  async getPopularSongs(): Promise<Song[]> {
    const response = await api.get('/songs/popular')
    return response.data
  },

  // 获取趋势歌曲
  async getTrendingSongs(): Promise<Song[]> {
    const response = await api.get('/songs/trending')
    return response.data
  },

  // 获取歌曲详情
  async getSongDetail(id: number): Promise<Song> {
    const response = await api.get(`/songs/${id}`)
    return response.data
  },

  // 记录播放历史
  async recordPlayHistory(songId: number): Promise<void> {
    await api.post(`/songs/${songId}/play`)
  }
}