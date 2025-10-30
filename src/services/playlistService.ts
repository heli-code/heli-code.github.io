import api from '@/utils/api'

export interface Playlist {
  id: number
  name: string
  description: string
  coverUrl: string
  songCount: number
  isPublic: boolean
  createdAt: string
}

export interface PlaylistRequest {
  name: string
  description?: string
  isPublic?: boolean
}

export const playlistService = {
  // 获取用户歌单
  async getUserPlaylists(): Promise<Playlist[]> {
    const response = await api.get('/playlists/user')
    return response.data
  },

  // 创建歌单
  async createPlaylist(data: PlaylistRequest): Promise<Playlist> {
    const response = await api.post('/playlists', data)
    return response.data
  },

  // 更新歌单
  async updatePlaylist(id: number, data: PlaylistRequest): Promise<Playlist> {
    const response = await api.put(`/playlists/${id}`, data)
    return response.data
  },

  // 删除歌单
  async deletePlaylist(id: number): Promise<void> {
    await api.delete(`/playlists/${id}`)
  },

  // 获取歌单详情
  async getPlaylistDetail(id: number): Promise<Playlist> {
    const response = await api.get(`/playlists/${id}`)
    return response.data
  },

  // 添加歌曲到歌单
  async addSongToPlaylist(playlistId: number, songId: number): Promise<void> {
    await api.post(`/playlists/${playlistId}/songs/${songId}`)
  },

  // 从歌单移除歌曲
  async removeSongFromPlaylist(playlistId: number, songId: number): Promise<void> {
    await api.delete(`/playlists/${playlistId}/songs/${songId}`)
  }
}