import api from '@/utils/api'

export interface FileUploadResponse {
  id: number
  filename: string
  url: string
  size: number
  mimeType: string
  uploadedAt: string
}

export interface FileInfo {
  id: number
  filename: string
  url: string
  size: number
  mimeType: string
  uploadedAt: string
  isPublic: boolean
}

export const fileService = {
  // 上传文件
  async uploadFile(file: File, isPublic = false): Promise<FileUploadResponse> {
    const formData = new FormData()
    formData.append('file', file)
    formData.append('isPublic', isPublic.toString())
    
    const response = await api.post('/files/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    
    return response.data
  },

  // 获取文件列表
  async getFiles(page = 1, limit = 20): Promise<FileInfo[]> {
    const response = await api.get(`/files?page=${page}&limit=${limit}`)
    return response.data
  },

  // 获取文件详情
  async getFileInfo(fileId: number): Promise<FileInfo> {
    const response = await api.get(`/files/${fileId}`)
    return response.data
  },

  // 下载文件
  async downloadFile(fileId: number): Promise<Blob> {
    const response = await api.get(`/files/${fileId}/download`, {
      responseType: 'blob'
    })
    return response.data
  },

  // 删除文件
  async deleteFile(fileId: number): Promise<void> {
    await api.delete(`/files/${fileId}`)
  },

  // 更新文件可见性
  async updateFileVisibility(fileId: number, isPublic: boolean): Promise<void> {
    await api.put(`/files/${fileId}/visibility`, { isPublic })
  },

  // 上传头像
  async uploadAvatar(file: File): Promise<FileUploadResponse> {
    const formData = new FormData()
    formData.append('file', file)
    
    const response = await api.post('/files/avatar', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    
    return response.data
  },

  // 上传歌曲封面
  async uploadSongCover(file: File): Promise<FileUploadResponse> {
    const formData = new FormData()
    formData.append('file', file)
    
    const response = await api.post('/files/song-cover', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    
    return response.data
  },

  // 上传歌单封面
  async uploadPlaylistCover(file: File): Promise<FileUploadResponse> {
    const formData = new FormData()
    formData.append('file', file)
    
    const response = await api.post('/files/playlist-cover', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    
    return response.data
  }
}