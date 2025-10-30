<template>
  <div class="social-interaction">
    <!-- 点赞按钮 -->
    <button 
      class="like-btn" 
      :class="{ liked: isLiked }"
      @click="toggleLike"
    >
      <span class="like-icon">{{ isLiked ? '❤️' : '🤍' }}</span>
      <span class="like-count">{{ likeCount }}</span>
    </button>
    
    <!-- 评论按钮 -->
    <button class="comment-btn" @click="toggleComment">
      <span class="comment-icon">💬</span>
      <span class="comment-count">{{ commentCount }}</span>
    </button>
    
    <!-- 分享按钮 -->
    <button class="share-btn" @click="shareContent">
      <span class="share-icon">📤</span>
      <span class="share-text">分享</span>
    </button>
    
    <!-- 关注按钮 -->
    <button 
      v-if="showFollow"
      class="follow-btn" 
      :class="{ following: isFollowing }"
      @click="toggleFollow"
    >
      <span class="follow-text">
        {{ isFollowing ? '已关注' : '关注' }}
      </span>
    </button>
    
    <!-- 评论输入框 -->
    <div v-if="showCommentInput" class="comment-input-section">
      <textarea 
        v-model="commentText" 
        placeholder="写下你的评论..."
        class="comment-textarea"
        rows="3"
      ></textarea>
      <div class="comment-actions">
        <button class="cancel-btn" @click="cancelComment">取消</button>
        <button class="submit-btn" @click="submitComment">发布</button>
      </div>
    </div>
    
    <!-- 分享模态框 -->
    <div v-if="showShareModal" class="share-modal">
      <div class="modal-content">
        <h3>分享到</h3>
        <div class="share-options">
          <button class="share-option" @click="shareToWechat">
            <span class="option-icon">💬</span>
            <span>微信</span>
          </button>
          <button class="share-option" @click="shareToQQ">
            <span class="option-icon">💙</span>
            <span>QQ</span>
          </button>
          <button class="share-option" @click="shareToWeibo">
            <span class="option-icon">📱</span>
            <span>微博</span>
          </button>
          <button class="share-option" @click="copyLink">
            <span class="option-icon">🔗</span>
            <span>复制链接</span>
          </button>
        </div>
        <button class="close-modal" @click="closeShareModal">关闭</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

// Props
interface Props {
  targetId: number
  targetType: 'song' | 'playlist' | 'user' | 'post'
  initialLiked?: boolean
  initialLikeCount?: number
  initialCommentCount?: number
  initialFollowing?: boolean
  showFollow?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  initialLiked: false,
  initialLikeCount: 0,
  initialCommentCount: 0,
  initialFollowing: false,
  showFollow: false
})

// 响应式数据
const isLiked = ref(props.initialLiked)
const likeCount = ref(props.initialLikeCount)
const commentCount = ref(props.initialCommentCount)
const isFollowing = ref(props.initialFollowing)
const showCommentInput = ref(false)
const showShareModal = ref(false)
const commentText = ref('')

// 方法
const toggleLike = async () => {
  try {
    if (isLiked.value) {
      // 取消点赞
      likeCount.value--
      // 调用取消点赞API
      console.log('取消点赞:', props.targetId, props.targetType)
    } else {
      // 点赞
      likeCount.value++
      // 调用点赞API
      console.log('点赞:', props.targetId, props.targetType)
    }
    isLiked.value = !isLiked.value
  } catch (error) {
    console.error('点赞操作失败:', error)
  }
}

const toggleComment = () => {
  showCommentInput.value = !showCommentInput.value
}

const submitComment = async () => {
  if (!commentText.value.trim()) return
  
  try {
    // 调用评论API
    console.log('发布评论:', commentText.value, props.targetId, props.targetType)
    commentCount.value++
    commentText.value = ''
    showCommentInput.value = false
    
    // 触发评论成功事件
    emit('commentSubmitted', commentText.value)
  } catch (error) {
    console.error('评论发布失败:', error)
  }
}

const cancelComment = () => {
  commentText.value = ''
  showCommentInput.value = false
}

const shareContent = () => {
  showShareModal.value = true
}

const closeShareModal = () => {
  showShareModal.value = false
}

const shareToWechat = () => {
  console.log('分享到微信')
  closeShareModal()
}

const shareToQQ = () => {
  console.log('分享到QQ')
  closeShareModal()
}

const shareToWeibo = () => {
  console.log('分享到微博')
  closeShareModal()
}

const copyLink = async () => {
  const shareUrl = `${window.location.origin}/${props.targetType}/${props.targetId}`
  try {
    await navigator.clipboard.writeText(shareUrl)
    console.log('链接已复制到剪贴板')
    closeShareModal()
  } catch (error) {
    console.error('复制链接失败:', error)
  }
}

const toggleFollow = async () => {
  try {
    if (isFollowing.value) {
      // 取消关注
      console.log('取消关注用户:', props.targetId)
    } else {
      // 关注
      console.log('关注用户:', props.targetId)
    }
    isFollowing.value = !isFollowing.value
    
    // 触发关注状态变化事件
    emit('followChanged', isFollowing.value)
  } catch (error) {
    console.error('关注操作失败:', error)
  }
}

// 事件
const emit = defineEmits<{
  commentSubmitted: [content: string]
  followChanged: [isFollowing: boolean]
}>()

// 暴露方法给父组件
defineExpose({
  updateLikeStatus(liked: boolean, count: number) {
    isLiked.value = liked
    likeCount.value = count
  },
  updateCommentCount(count: number) {
    commentCount.value = count
  },
  updateFollowStatus(following: boolean) {
    isFollowing.value = following
  }
})
</script>

<style scoped>
.social-interaction {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 10px 0;
}

/* 按钮基础样式 */
.like-btn, .comment-btn, .share-btn, .follow-btn {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 8px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 20px;
  background: white;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;
}

.like-btn:hover, .comment-btn:hover, .share-btn:hover, .follow-btn:hover {
  background: #f5f5f5;
  transform: translateY(-1px);
}

/* 点赞按钮 */
.like-btn.liked {
  border-color: #ff4757;
  background: #ffe6e9;
  color: #ff4757;
}

/* 关注按钮 */
.follow-btn.following {
  border-color: #667eea;
  background: #667eea;
  color: white;
}

/* 图标样式 */
.like-icon, .comment-icon, .share-icon {
  font-size: 16px;
}

/* 评论输入区域 */
.comment-input-section {
  width: 100%;
  margin-top: 10px;
}

.comment-textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  resize: vertical;
  font-size: 14px;
}

.comment-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 10px;
}

.cancel-btn, .submit-btn {
  padding: 8px 16px;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  background: white;
  cursor: pointer;
  font-size: 14px;
}

.submit-btn {
  background: #667eea;
  color: white;
  border-color: #667eea;
}

.cancel-btn:hover {
  background: #f5f5f5;
}

.submit-btn:hover {
  background: #5a6fd8;
}

/* 分享模态框 */
.share-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 12px;
  max-width: 400px;
  width: 90%;
}

.modal-content h3 {
  margin: 0 0 20px 0;
  text-align: center;
}

.share-options {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
  margin-bottom: 20px;
}

.share-option {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 15px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  background: white;
  cursor: pointer;
  transition: all 0.3s;
}

.share-option:hover {
  background: #f5f5f5;
  transform: translateY(-2px);
}

.option-icon {
  font-size: 24px;
}

.close-modal {
  width: 100%;
  padding: 10px;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  background: white;
  cursor: pointer;
}

.close-modal:hover {
  background: #f5f5f5;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .social-interaction {
    gap: 10px;
  }
  
  .like-btn, .comment-btn, .share-btn, .follow-btn {
    padding: 6px 10px;
    font-size: 12px;
  }
  
  .share-options {
    grid-template-columns: 1fr;
  }
}
</style>