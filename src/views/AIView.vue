<template>
  <div class="ai-view">
    <div class="ai-content">
      <div class="page-header">
        <BackButton />
        <h1>AI音乐助手</h1>
        <div class="mood-indicator" :class="currentMood">
          <span class="mood-icon">{{ moodIcons[currentMood] }}</span>
          <span class="mood-text">{{ moodTexts[currentMood] }}</span>
        </div>
      </div>
      <div class="ai-interface">
        <div class="ai-sidebar">
          <div class="ai-avatar">
            <div class="avatar-icon">🤖</div>
            <h3>音乐助手</h3>
            <p>专业导师型</p>
            <div class="mood-analysis">
              <h4>情绪识别</h4>
              <div class="mood-tags">
                <span 
                  v-for="mood in detectedMoods" 
                  :key="mood"
                  class="mood-tag"
                  :class="mood"
                >
                  {{ mood }}
                </span>
              </div>
            </div>
          </div>
          
          <div class="ai-features">
            <h4>AI功能</h4>
            <button 
              v-for="feature in features" 
              :key="feature.id"
              :class="['feature-btn', { active: activeFeature === feature.id }]"
              @click="selectFeature(feature.id)"
            >
              <span class="feature-icon">{{ feature.icon }}</span>
              {{ feature.name }}
            </button>
          </div>
          
          <div class="scene-selector">
            <h4>场景选择</h4>
            <div class="scene-buttons">
              <button 
                v-for="scene in scenes" 
                :key="scene.id"
                :class="['scene-btn', { active: currentScene === scene.id }]"
                @click="selectScene(scene.id)"
              >
                {{ scene.name }}
              </button>
            </div>
          </div>
        </div>
        
        <div class="ai-chat">
          <div class="chat-header">
            <div class="chat-info">
              <h3>智能对话</h3>
              <p>基于您的情绪和场景推荐音乐</p>
            </div>
            <div class="emotion-display">
              <span class="emotion-label">当前情绪:</span>
              <span class="emotion-value" :class="currentEmotion">{{ currentEmotion }}</span>
            </div>
          </div>
          
          <div class="chat-messages">
            <div 
              v-for="message in messages" 
              :key="message.id"
              :class="['message', message.type, { 'has-mood': message.mood }]"
            >
              <div class="message-avatar">
                <span v-if="message.type === 'user'">👤</span>
                <span v-else>🤖</span>
              </div>
              <div class="message-content">
                <div class="message-text">{{ message.content }}</div>
                <div v-if="message.mood" class="message-mood">
                  <span class="mood-badge" :class="message.mood">{{ message.mood }}</span>
                </div>
                <div v-if="message.recommendations" class="recommendations">
                  <div class="recommendation-item" v-for="song in message.recommendations" :key="song.id">
                    <span class="song-title">{{ song.title }}</span>
                    <span class="song-artist">{{ song.artist }}</span>
                  </div>
                </div>
              </div>
              <div class="message-time">
                {{ message.time }}
              </div>
            </div>
          </div>
          
          <div class="chat-input-section">
            <div class="quick-emotions">
              <button 
                v-for="emotion in quickEmotions" 
                :key="emotion"
                class="quick-emotion-btn"
                @click="addEmotionToMessage(emotion)"
              >
                {{ emotion }}
              </button>
            </div>
            <div class="chat-input">
              <input 
                v-model="inputMessage" 
                type="text" 
                placeholder="描述您的心情或想要的音乐类型..."
                @keyup.enter="sendMessage"
              />
              <button @click="sendMessage">发送</button>
            </div>
          </div>
        
        <div class="ai-recommendations">
          <h3>智能推荐</h3>
          <div class="recommendation-cards">
            <div class="rec-card" v-for="rec in recommendations" :key="rec.id">
              <div class="rec-cover"></div>
              <div class="rec-info">
                <h4>{{ rec.title }}</h4>
                <p>{{ rec.description }}</p>
                <span class="rec-mood">{{ rec.mood }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import BackButton from '@/components/BackButton.vue'

// 情绪识别相关数据
const currentMood = ref('neutral')
const currentEmotion = ref('平静')
const detectedMoods = ref<string[]>([])

// 情绪图标和文本
const moodIcons = {
  happy: '😊',
  sad: '😢',
  excited: '🎉',
  relaxed: '😌',
  focused: '🧠',
  neutral: '😐'
}

const moodTexts = {
  happy: '开心',
  sad: '悲伤',
  excited: '兴奋',
  relaxed: '放松',
  focused: '专注',
  neutral: '平静'
}

// AI功能
const features = [
  { id: 'recommend', name: '智能推荐', icon: '🎵' },
  { id: 'mood', name: '情绪识别', icon: '😊' },
  { id: 'create', name: '歌单创作', icon: '📝' },
  { id: 'analyze', name: '音乐分析', icon: '🔍' }
]

// 场景选择
const scenes = [
  { id: 'work', name: '工作学习' },
  { id: 'sport', name: '运动健身' },
  { id: 'relax', name: '放松休息' },
  { id: 'party', name: '派对聚会' }
]

// 快速情绪按钮
const quickEmotions = ['开心', '悲伤', '兴奋', '放松', '专注', '平静']

const activeFeature = ref('recommend')
const currentScene = ref('work')
const inputMessage = ref('')
const messages = ref([
  {
    id: 1,
    type: 'ai',
    content: '您好！我是您的AI音乐助手，我可以根据您的心情和场景为您推荐合适的音乐。请告诉我您现在的心情或想要什么样的音乐吧！',
    time: '10:00',
    mood: '友好'
  }
])

const recommendations = ref([
  {
    id: 1,
    title: '专注工作歌单',
    description: '适合工作学习的轻音乐',
    mood: '专注'
  },
  {
    id: 2,
    title: '运动能量歌单',
    description: '高能量运动音乐',
    mood: '兴奋'
  }
])

// 计算属性：根据消息内容分析情绪
const analyzeEmotion = computed(() => {
  const lastMessage = messages.value[messages.value.length - 1]
  if (!lastMessage || lastMessage.type !== 'user') return 'neutral'
  
  const content = lastMessage.content.toLowerCase()
  
  if (content.includes('开心') || content.includes('高兴') || content.includes('快乐')) {
    return 'happy'
  } else if (content.includes('悲伤') || content.includes('难过') || content.includes('失落')) {
    return 'sad'
  } else if (content.includes('兴奋') || content.includes('激动') || content.includes('活力')) {
    return 'excited'
  } else if (content.includes('放松') || content.includes('平静') || content.includes('舒缓')) {
    return 'relaxed'
  } else if (content.includes('专注') || content.includes('工作') || content.includes('学习')) {
    return 'focused'
  }
  
  return 'neutral'
})

// 方法
const selectFeature = (featureId: string) => {
  activeFeature.value = featureId
}

const selectScene = (sceneId: string) => {
  currentScene.value = sceneId
  // 根据场景更新推荐
  updateRecommendations()
}

const addEmotionToMessage = (emotion: string) => {
  inputMessage.value += ` 我现在感觉很${emotion}`
}

const sendMessage = () => {
  if (!inputMessage.value.trim()) return
  
  // 添加用户消息
  const userMessage = {
    id: Date.now(),
    type: 'user',
    content: inputMessage.value,
    time: new Date().toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' }),
    mood: detectEmotionFromText(inputMessage.value)
  }
  
  messages.value.push(userMessage)
  
  // 分析情绪并更新
  updateEmotionAnalysis(inputMessage.value)
  
  // 清空输入框
  const message = inputMessage.value
  inputMessage.value = ''
  
  // 模拟AI回复
  setTimeout(() => {
    generateAIResponse(message)
  }, 1000)
}

// 情绪检测函数
const detectEmotionFromText = (text: string): string => {
  const lowerText = text.toLowerCase()
  
  if (lowerText.includes('开心') || lowerText.includes('高兴') || lowerText.includes('快乐')) {
    return '开心'
  } else if (lowerText.includes('悲伤') || lowerText.includes('难过') || lowerText.includes('失落')) {
    return '悲伤'
  } else if (lowerText.includes('兴奋') || lowerText.includes('激动') || lowerText.includes('活力')) {
    return '兴奋'
  } else if (lowerText.includes('放松') || lowerText.includes('平静') || lowerText.includes('舒缓')) {
    return '放松'
  } else if (lowerText.includes('专注') || lowerText.includes('工作') || lowerText.includes('学习')) {
    return '专注'
  }
  
  return '平静'
}

// 更新情绪分析
const updateEmotionAnalysis = (text: string) => {
  const emotion = detectEmotionFromText(text)
  currentEmotion.value = emotion
  
  // 更新检测到的情绪标签
  if (!detectedMoods.value.includes(emotion)) {
    detectedMoods.value.push(emotion)
    // 最多保留5个情绪标签
    if (detectedMoods.value.length > 5) {
      detectedMoods.value.shift()
    }
  }
  
  // 更新当前情绪状态
  switch (emotion) {
    case '开心':
      currentMood.value = 'happy'
      break
    case '悲伤':
      currentMood.value = 'sad'
      break
    case '兴奋':
      currentMood.value = 'excited'
      break
    case '放松':
      currentMood.value = 'relaxed'
      break
    case '专注':
      currentMood.value = 'focused'
      break
    default:
      currentMood.value = 'neutral'
  }
}

// 生成AI回复
const generateAIResponse = (userMessage: string) => {
  const lowerMessage = userMessage.toLowerCase()
  let response = ''
  let recommendations = []
  
  // 根据用户消息生成不同的回复
  if (lowerMessage.includes('工作') || lowerMessage.includes('学习')) {
    response = '为您推荐一些专注力提升的音乐，适合工作学习时听：
• 轻音乐
• 古典音乐
• 环境音乐'
    recommendations = [
      { id: 1, title: '专注工作', artist: '轻音乐合集' },
      { id: 2, title: '学习时光', artist: '古典精选' }
    ]
  } else if (lowerMessage.includes('运动') || lowerMessage.includes('健身')) {
    response = '运动时刻需要高能量音乐！为您推荐：
• 电子音乐
• 流行舞曲
• 摇滚音乐'
    recommendations = [
      { id: 3, title: '运动能量', artist: '电子舞曲' },
      { id: 4, title: '健身节奏', artist: '流行热单' }
    ]
  } else if (lowerMessage.includes('放松') || lowerText.includes('休息')) {
    response = '放松时刻，这些舒缓音乐最适合：
• 轻音乐
• 自然声音
• 冥想音乐'
    recommendations = [
      { id: 5, title: '放松时刻', artist: '自然之声' },
      { id: 6, title: '冥想空间', artist: '治愈音乐' }
    ]
  } else {
    response = '根据您的心情，我为您推荐一些适合的音乐。您可以告诉我更具体的需求，比如想要什么风格的音乐？'
  }
  
  // 添加AI回复
  const aiMessage = {
    id: Date.now() + 1,
    type: 'ai',
    content: response,
    time: new Date().toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' }),
    mood: '友好',
    recommendations: recommendations
  }
  
  messages.value.push(aiMessage)
  
  // 滚动到底部
  setTimeout(() => {
    const chatMessages = document.querySelector('.chat-messages')
    if (chatMessages) {
      chatMessages.scrollTop = chatMessages.scrollHeight
    }
  }, 100)
}

// 更新推荐
const updateRecommendations = () => {
  // 根据当前场景更新推荐
  switch (currentScene.value) {
    case 'work':
      recommendations.value = [
        { id: 1, title: '专注工作歌单', description: '适合工作学习的轻音乐', mood: '专注' },
        { id: 2, title: '学习时光', description: '提高专注力的音乐', mood: '专注' }
      ]
      break
    case 'sport':
      recommendations.value = [
        { id: 3, title: '运动能量歌单', description: '高能量运动音乐', mood: '兴奋' },
        { id: 4, title: '健身节奏', description: '适合健身的音乐', mood: '活力' }
      ]
      break
    case 'relax':
      recommendations.value = [
        { id: 5, title: '放松时刻', description: '舒缓放松音乐', mood: '放松' },
        { id: 6, title: '冥想空间', description: '冥想和放松音乐', mood: '平静' }
      ]
      break
    case 'party':
      recommendations.value = [
        { id: 7, title: '派对热单', description: '派对热门音乐', mood: '兴奋' },
        { id: 8, title: '欢乐时光', description: '欢乐聚会音乐', mood: '快乐' }
      ]
      break
  }
}

// 生命周期
onMounted(() => {
  // 初始化情绪分析
  updateEmotionAnalysis('')
})
</script>

<style scoped lang="scss">
.ai-view {
  padding: 2rem;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
  color: white;
  min-height: 100vh;
  
  .page-header {
    display: flex;
    align-items: center;
    gap: 1rem;
    margin-bottom: 2rem;
    
    h1 {
      font-size: 2rem;
      color: #4cc9f0;
      margin: 0;
    }
  }
  
  .ai-interface {
    display: grid;
    grid-template-columns: 250px 1fr 300px;
    gap: 2rem;
    height: 70vh;
  }
  
  .ai-sidebar {
    background: rgba(255, 255, 255, 0.1);
    border-radius: 12px;
    padding: 1.5rem;
    
    .ai-avatar {
      text-align: center;
      margin-bottom: 2rem;
      
      .avatar-icon {
        font-size: 3rem;
        margin-bottom: 1rem;
      }
      
      h3 {
        margin: 0.5rem 0;
        font-size: 1.2rem;
      }
      
      p {
        margin: 0;
        color: rgba(255, 255, 255, 0.7);
        font-size: 0.9rem;
      }
    }
    
    .ai-features {
      h4 {
        margin-bottom: 1rem;
        color: #4cc9f0;
      }
      
      .feature-btn {
        display: block;
        width: 100%;
        background: rgba(255, 255, 255, 0.1);
        border: none;
        color: white;
        padding: 0.8rem;
        margin-bottom: 0.5rem;
        border-radius: 8px;
        cursor: pointer;
        transition: all 0.3s ease;
        text-align: left;
        
        &.active, &:hover {
          background: rgba(76, 201, 240, 0.3);
          color: #4cc9f0;
        }
      }
    }
  }
  
  .ai-chat {
    display: flex;
    flex-direction: column;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 12px;
    padding: 1.5rem;
    
    .chat-messages {
      flex: 1;
      overflow-y: auto;
      margin-bottom: 1rem;
      
      .message {
        margin-bottom: 1rem;
        
        &.user {
          text-align: right;
          
          .message-content {
            background: rgba(76, 201, 240, 0.3);
            margin-left: auto;
          }
        }
        
        &.ai {
          text-align: left;
          
          .message-content {
            background: rgba(255, 255, 255, 0.2);
            margin-right: auto;
          }
        }
        
        .message-content {
          display: inline-block;
          max-width: 70%;
          padding: 0.8rem 1rem;
          border-radius: 15px;
          margin-bottom: 0.3rem;
        }
        
        .message-time {
          font-size: 0.8rem;
          color: rgba(255, 255, 255, 0.5);
        }
      }
    }
    
    .chat-input {
      display: flex;
      gap: 0.5rem;
      
      input {
        flex: 1;
        background: rgba(255, 255, 255, 0.1);
        border: none;
        color: white;
        padding: 0.8rem 1rem;
        border-radius: 20px;
        font-size: 1rem;
        
        &::placeholder {
          color: rgba(255, 255, 255, 0.5);
        }
        
        &:focus {
          outline: none;
          background: rgba(255, 255, 255, 0.15);
        }
      }
      
      button {
        background: #4cc9f0;
        border: none;
        color: white;
        padding: 0.8rem 1.5rem;
        border-radius: 20px;
        cursor: pointer;
        font-size: 1rem;
        transition: background 0.3s ease;
        
        &:hover {
          background: #3ab0d9;
        }
      }
    }
  }
  
  .ai-recommendations {
    background: rgba(255, 255, 255, 0.1);
    border-radius: 12px;
    padding: 1.5rem;
    
    h3 {
      margin-bottom: 1rem;
      color: #4cc9f0;
    }
    
    .recommendation-cards {
      display: flex;
      flex-direction: column;
      gap: 1rem;
    }
    
    .rec-card {
      background: rgba(255, 255, 255, 0.1);
      border-radius: 8px;
      padding: 1rem;
      display: flex;
      align-items: center;
      gap: 1rem;
      transition: transform 0.3s ease;
      
      &:hover {
        transform: translateY(-2px);
        background: rgba(255, 255, 255, 0.15);
      }
      
      .rec-cover {
        width: 60px;
        height: 60px;
        background: linear-gradient(45deg, #4cc9f0, #f72585);
        border-radius: 8px;
        flex-shrink: 0;
      }
      
      .rec-info {
        flex: 1;
        
        h4 {
          margin: 0 0 0.3rem 0;
          font-size: 0.9rem;
        }
        
        p {
          margin: 0 0 0.3rem 0;
          font-size: 0.8rem;
          color: rgba(255, 255, 255, 0.7);
        }
        
        .rec-mood {
          background: rgba(76, 201, 240, 0.2);
          color: #4cc9f0;
          padding: 0.2rem 0.5rem;
          border-radius: 10px;
          font-size: 0.7rem;
        }
      }
    }
  }
}
</style>