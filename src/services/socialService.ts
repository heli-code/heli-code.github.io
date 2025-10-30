import api from '@/utils/api'

export interface User {
  id: number
  username: string
  nickname: string
  avatar: string
  bio: string
  followers: number
  following: number
  isFollowing: boolean
}

export interface Post {
  id: number
  userId: number
  content: string
  songId?: number
  songTitle?: string
  songArtist?: string
  likes: number
  comments: number
  isLiked: boolean
  createdAt: string
  user: User
}

export interface Comment {
  id: number
  postId: number
  userId: number
  content: string
  likes: number
  isLiked: boolean
  createdAt: string
  user: User
}

export interface FollowRequest {
  userId: number
}

export interface CreatePostRequest {
  content: string
  songId?: number
}

export interface CreateCommentRequest {
  postId: number
  content: string
}

export const socialService = {
  // 获取推荐用户
  async getRecommendedUsers(): Promise<User[]> {
    const response = await api.get('/social/users/recommended')
    return response.data
  },

  // 搜索用户
  async searchUsers(keyword: string): Promise<User[]> {
    const response = await api.get(`/social/users/search?keyword=${encodeURIComponent(keyword)}`)
    return response.data
  },

  // 获取用户详情
  async getUserDetail(userId: number): Promise<User> {
    const response = await api.get(`/social/users/${userId}`)
    return response.data
  },

  // 关注用户
  async followUser(request: FollowRequest): Promise<void> {
    await api.post('/social/follow', request)
  },

  // 取消关注
  async unfollowUser(userId: number): Promise<void> {
    await api.delete(`/social/follow/${userId}`)
  },

  // 获取关注列表
  async getFollowing(): Promise<User[]> {
    const response = await api.get('/social/following')
    return response.data
  },

  // 获取粉丝列表
  async getFollowers(): Promise<User[]> {
    const response = await api.get('/social/followers')
    return response.data
  },

  // 获取动态
  async getPosts(page = 1, limit = 20): Promise<Post[]> {
    const response = await api.get(`/social/posts?page=${page}&limit=${limit}`)
    return response.data
  },

  // 创建动态
  async createPost(request: CreatePostRequest): Promise<Post> {
    const response = await api.post('/social/posts', request)
    return response.data
  },

  // 删除动态
  async deletePost(postId: number): Promise<void> {
    await api.delete(`/social/posts/${postId}`)
  },

  // 点赞动态
  async likePost(postId: number): Promise<void> {
    await api.post(`/social/posts/${postId}/like`)
  },

  // 取消点赞
  async unlikePost(postId: number): Promise<void> {
    await api.delete(`/social/posts/${postId}/like`)
  },

  // 获取动态评论
  async getPostComments(postId: number): Promise<Comment[]> {
    const response = await api.get(`/social/posts/${postId}/comments`)
    return response.data
  },

  // 添加评论
  async addComment(request: CreateCommentRequest): Promise<Comment> {
    const response = await api.post('/social/comments', request)
    return response.data
  },

  // 删除评论
  async deleteComment(commentId: number): Promise<void> {
    await api.delete(`/social/comments/${commentId}`)
  },

  // 点赞评论
  async likeComment(commentId: number): Promise<void> {
    await api.post(`/social/comments/${commentId}/like`)
  },

  // 取消点赞评论
  async unlikeComment(commentId: number): Promise<void> {
    await api.delete(`/social/comments/${commentId}/like`)
  }
}