<template>
  <div class="floating-ai" :class="{ 'ai-active': isActive, 'ai-minimized': isMinimized }">
    <!-- AI助手按钮 -->
    <button class="ai-button" @click="toggleAI">
      <span class="ai-icon">🤖</span>
      <span class="ai-pulse" v-if="hasNewMessage"></span>
    </button>

    <!-- AI聊天窗口 -->
    <div v-if="isActive" class="ai-chat-window">
      <!-- 标题栏 -->
      <div class="chat-header">
        <div class="header-left">
          <span class="ai-avatar">🤖</span>
          <div class="ai-info">
            <h4>AI音乐助手</h4>
            <span class="status" :class="{ online: isOnline }">
              {{ isOnline ? '在线' : '离线' }}
            </span>
          </div>
        </div>
        <div class="header-right">
          <button class="header-btn" @click="toggleMinimize">
            <span>{{ isMinimized ? '📈' : '📉' }}</span>
          </button>
          <button class="header-btn" @click="clearChat">
            <span>🗑️</span>
          </button>
          <button class="header-btn" @click="closeChat">
            <span>✕</span>
          </button>
        </div>
      </div>

      <!-- 聊天内容 -->
      <div class="chat-content" ref="chatContent">
        <div 
          v-for="message in messages" 
          :key="message.id"
          class="message"
          :class="{ 'user-message': message.role === 'user', 'ai-message': message.role === 'assistant' }"
        >
          <div class="message-avatar">
            <span>{{ message.role === 'user' ? '👤' : '🤖' }}</span>
          </div>
          <div class="message-content">
            <div class="message-text">{{ message.content }}</div>
            <div class="message-time">{{ formatTime(message.timestamp) }}</div>
          </div>
        </div>

        <!-- 加载状态 -->
        <div v-if="isLoading" class="loading-message">
          <div class="message-avatar">
            <span>🤖</span>
          </div>
          <div class="message-content">
            <div class="typing-indicator">
              <span></span>
              <span></span>
              <span></span>
            </div>
          </div>
        </div>
      </div>

      <!-- 快速操作 -->
      <div v-if="!isMinimized" class="quick-actions">
        <button 
          v-for="action in quickActions" 
          :key="action.id"
          class="quick-action"
          @click="sendQuickMessage(action.message)"
        >
          <span class="action-icon">{{ action.icon }}</span>
          <span class="action-text">{{ action.text }}</span>
        </button>
      </div>

      <!-- 输入框 -->
      <div v-if="!isMinimized" class="chat-input-section">
        <div class="input-wrapper">
          <textarea
            v-model="inputMessage"
            placeholder="向AI助手提问..."
            class="chat-input"
            @keydown.enter="handleKeydown"
            rows="1"
            ref="chatInput"
          ></textarea>
          <button class="send-btn" @click="sendMessage" :disabled="!inputMessage.trim()">
            <span>📤</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, nextTick, onMounted, onUnmounted } from 'vue'
import { aiService, type AIChatMessage } from '@/services/aiService'

// 响应式数据
const isActive = ref(false)
const isMinimized = ref(false)
const isOnline = ref(true)
const isLoading = ref(false)
const hasNewMessage = ref(false)
const inputMessage = ref('')
const messages = ref<AIChatMessage[]>([
  {
    id: 1,
    role: 'assistant',
    content: '你好！我是AI音乐助手，我可以帮你推荐音乐、分析听歌习惯、创建个性化歌单。有什么可以帮你的吗？',
    timestamp: new Date().toISOString()
  }
])

// 引用
const chatContent = ref<HTMLElement | null>(null)
const chatInput = ref<HTMLTextAreaElement | null>(null)

// 快速操作
const quickActions = [
  { id: 1, icon: '🎵', text: '推荐歌曲', message: '根据我的听歌历史推荐一些歌曲' },
  { id: 2, icon: '🎭', text: '心情推荐', message: '我现在心情很好，推荐一些欢快的歌曲' },
  { id: 3, icon: '🏃', text: '运动音乐', message: '推荐适合运动的音乐' },
  { id: 4, icon: '📊', text: '听歌分析', message: '分析我的听歌习惯' }
]

// 计算属性
const messageCount = computed(() => messages.value.length)

// 方法
const toggleAI = () => {
  isActive.value = !isActive.value
  if (isActive.value) {
    hasNewMessage.value = false
    nextTick(() => {
      scrollToBottom()
      focusInput()
    })
  }
}

const toggleMinimize = () => {
  isMinimized.value = !isMinimized.value
}

const closeChat = () => {
  isActive.value = false
  isMinimized.value = false
}

const clearChat = () => {
  messages.value = [
    {
      id: Date.now(),
      role: 'assistant',
      content: '聊天记录已清空，有什么可以帮你的吗？',
      timestamp: new Date().toISOString()
    }
  ]
  nextTick(scrollToBottom)
}

const sendMessage = async () => {
  if (!inputMessage.value.trim()) return

  const userMessage: AIChatMessage = {
    id: Date.now(),
    role: 'user',
    content: inputMessage.value.trim(),
    timestamp: new Date().toISOString()
  }

  messages.value.push(userMessage)
  const messageText = inputMessage.value.trim()
  inputMessage.value = ''

  isLoading.value = true
  nextTick(scrollToBottom)

  try {
    const response = await aiService.chatWithAI({
      message: messageText,
      context: {
        mood: 'normal',
        activity: 'listening',
        timeOfDay: getTimeOfDay()
      }
    })

    const aiMessage: AIChatMessage = {
      id: Date.now() + 1,
      role: 'assistant',
      content: response.content,
      timestamp: new Date().toISOString()
    }

    messages.value.push(aiMessage)
    
    // 如果有新消息且窗口未激活，显示提示
    if (!isActive.value) {
      hasNewMessage.value = true
    }
  } catch (error) {
    console.error('AI聊天错误:', error)
    
    const errorMessage: AIChatMessage = {
      id: Date.now() + 1,
      role: 'assistant',
      content: '抱歉，我暂时无法处理你的请求。请稍后再试。',
      timestamp: new Date().toISOString()
    }
    
    messages.value.push(errorMessage)
  } finally {
    isLoading.value = false
    nextTick(scrollToBottom)
  }
}

const sendQuickMessage = (message: string) => {
  inputMessage.value = message
  sendMessage()
}

const handleKeydown = (event: KeyboardEvent) => {
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault()
    sendMessage()
  }
}

const scrollToBottom = () => {
  if (chatContent.value) {
    chatContent.value.scrollTop = chatContent.value.scrollHeight
  }
}

const focusInput = () => {
  if (chatInput.value) {
    chatInput.value.focus()
  }
}

const formatTime = (timestamp: string): string => {
  const date = new Date(timestamp)
  return date.toLocaleTimeString('zh-CN', { 
    hour: '2-digit', 
    minute: '2-digit' 
  })
}

const getTimeOfDay = (): string => {
  const hour = new Date().getHours()
  if (hour >= 5 && hour < 12) return 'morning'
  if (hour >= 12 && hour < 18) return 'afternoon'
  if (hour >= 18 && hour < 22) return 'evening'
  return 'night'
}

// 自动调整输入框高度
const adjustInputHeight = () => {
  if (chatInput.value) {
    chatInput.value.style.height = 'auto'
    chatInput.value.style.height = chatInput.value.scrollHeight + 'px'
  }
}

// 生命周期
onMounted(() => {
  // 加载聊天历史
  loadChatHistory()
  
  // 监听输入框变化
  if (chatInput.value) {
    chatInput.value.addEventListener('input', adjustInputHeight)
  }
})

onUnmounted(() => {
  if (chatInput.value) {
    chatInput.value.removeEventListener('input', adjustInputHeight)
  }
})

const loadChatHistory = async () => {
  try {
    const history = await aiService.getChatHistory()
    if (history.length > 0) {
      messages.value = history
    }
  } catch (error) {
    console.error('加载聊天历史失败:', error)
  }
}
</script>

<style scoped>
.floating-ai {
  position: fixed;
  bottom: 100px;
  right: 30px;
  z-index: 1000;
}

.ai-button {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, #4cc9f0, #f72585);
  border: none;
  color: white;
  font-size: 1.5rem;
  cursor: pointer;
  box-shadow: 0 4px 20px rgba(76, 201, 240, 0.4);
  transition: all 0.3s ease;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.ai-button:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 30px rgba(76, 201, 240, 0.6);
}

.ai-pulse {
  position: absolute;
  top: -5px;
  right: -5px;
  width: 12px;
  height: 12px;
  background: #ff4757;
  border-radius: 50%;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% { transform: scale(1); opacity: 1; }
  50% { transform: scale(1.2); opacity: 0.7; }
  100% { transform: scale(1); opacity: 1; }
}

.ai-chat-window {
  position: absolute;
  bottom: 80px;
  right: 0;
  width: 350px;
  height: 500px;
  background: rgba(15, 20, 25, 0.95);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.ai-minimized .ai-chat-window {
  height: 60px;
}

.chat-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem;
  background: rgba(255, 255, 255, 0.05);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.ai-avatar {
  font-size: 1.5rem;
}

.ai-info h4 {
  margin: 0;
  font-size: 1rem;
  font-weight: 600;
}

.status {
  font-size: 0.8rem;
  color: rgba(255, 255, 255, 0.6);
}

.status.online {
  color: #4cc9f0;
}

.header-right {
  display: flex;
  gap: 0.5rem;
}

.header-btn {
  background: none;
  border: none;
  color: rgba(255, 255, 255, 0.7);
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.header-btn:hover {
  background: rgba(255, 255, 255, 0.1);
  color: white;
}

.chat-content {
  flex: 1;
  overflow-y: auto;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.message {
  display: flex;
  gap: 0.75rem;
  max-width: 85%;
}

.user-message {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.ai-message {
  align-self: flex-start;
}

.message-avatar {
  font-size: 1.2rem;
  flex-shrink: 0;
}

.message-content {
  background: rgba(255, 255, 255, 0.1);
  padding: 0.75rem 1rem;
  border-radius: 16px;
  backdrop-filter: blur(10px);
}

.user-message .message-content {
  background: rgba(76, 201, 240, 0.2);
  border-bottom-right-radius: 4px;
}

.ai-message .message-content {
  border-bottom-left-radius: 4px;
}

.message-text {
  font-size: 0.9rem;
  line-height: 1.4;
  word-wrap: break-word;
}

.message-time {
  font-size: 0.7rem;
  color: rgba(255, 255, 255, 0.5);
  margin-top: 0.25rem;
}

.loading-message {
  display: flex;
  gap: 0.75rem;
  align-self: flex-start;
}

.typing-indicator {
  display: flex;
  gap: 4px;
  padding: 0.75rem 1rem;
}

.typing-indicator span {
  width: 6px;
  height: 6px;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 50%;
  animation: typing 1.4s infinite;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% { transform: translateY(0); }
  30% { transform: translateY(-10px); }
}

.quick-actions {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0.5rem;
  padding: 1rem;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.quick-action {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  padding: 0.6rem;
  color: white;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 0.8rem;
}

.quick-action:hover {
  background: rgba(255, 255, 255, 0.1);
  transform: translateY(-1px);
}

.action-icon {
  font-size: 1rem;
}

.chat-input-section {
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  padding: 1rem;
}

.input-wrapper {
  display: flex;
  align-items: flex-end;
  gap: 0.5rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  padding: 0.75rem;
}

.chat-input {
  flex: 1;
  background: none;
  border: none;
  color: white;
  font-size: 0.9rem;
  resize: none;
  outline: none;
  max-height: 120px;
  font-family: inherit;
}

.chat-input::placeholder {
  color: rgba(255, 255, 255, 0.5);
}

.send-btn {
  background: #4cc9f0;
  border: none;
  color: white;
  padding: 0.5rem;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.2s ease;
  flex-shrink: 0;
}

.send-btn:hover:not(:disabled) {
  background: #3ab0d9;
}

.send-btn:disabled {
  background: rgba(255, 255, 255, 0.1);
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .floating-ai {
    bottom: 80px;
    right: 20px;
  }
  
  .ai-chat-window {
    width: calc(100vw - 40px);
    right: 20px;
    left: 20px;
  }
}
</style>