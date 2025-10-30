<template>
  <div class="music-player" :class="{ 'player-minimized': isMinimized }">
    <!-- 播放器主界面 -->
    <div class="player-main" v-if="!isMinimized">
      <div class="player-header">
        <h3>正在播放</h3>
        <button class="minimize-btn" @click="minimizePlayer">−</button>
      </div>
      
      <div class="player-content">
        <!-- 歌曲信息 -->
        <div class="song-info">
          <div class="album-cover">
            <img :src="currentSong.cover || '/default-cover.jpg'" alt="专辑封面">
          </div>
          <div class="song-details">
            <h4 class="song-title">{{ currentSong.title || '未选择歌曲' }}</h4>
            <p class="song-artist">{{ currentSong.artist || '未知艺术家' }}</p>
          </div>
        </div>
        
        <!-- 进度条 -->
        <div class="progress-section">
          <div class="time-display">
            <span class="current-time">{{ formatTime(currentTime) }}</span>
            <span class="duration">{{ formatTime(duration) }}</span>
          </div>
          <div class="progress-bar" @click="seekToPosition">
            <div class="progress" :style="{ width: progressPercentage + '%' }"></div>
            <div class="progress-handle" :style="{ left: progressPercentage + '%' }"></div>
          </div>
        </div>
        
        <!-- 播放控制 -->
        <div class="player-controls">
          <button class="control-btn" @click="previousSong">⏮</button>
          <button class="play-btn" @click="togglePlay">
            {{ isPlaying ? '⏸' : '▶' }}
          </button>
          <button class="control-btn" @click="nextSong">⏭</button>
        </div>
        
        <!-- 音量控制 -->
        <div class="volume-control">
          <button class="volume-btn" @click="toggleMute">
            {{ volume === 0 ? '🔇' : volume > 50 ? '🔊' : '🔉' }}
          </button>
          <div class="volume-bar" @click="setVolume">
            <div class="volume-level" :style="{ width: volume + '%' }"></div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 迷你播放器 -->
    <div class="mini-player" v-else @click="expandPlayer">
      <div class="mini-info">
        <span class="mini-title">{{ currentSong.title || '未播放' }}</span>
        <span class="mini-artist">{{ currentSong.artist || '' }}</span>
      </div>
      <div class="mini-controls">
        <button class="mini-play-btn" @click.stop="togglePlay">
          {{ isPlaying ? '⏸' : '▶' }}
        </button>
        <button class="mini-next-btn" @click.stop="nextSong">⏭</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'

// 歌曲接口
interface Song {
  id: number
  title: string
  artist: string
  cover?: string
  url?: string
  duration: number
}

// 响应式数据
const currentSong = ref<Song>({
  id: 1,
  title: '示例歌曲',
  artist: '示例艺术家',
  duration: 180
})

const isPlaying = ref(false)
const isMinimized = ref(false)
const currentTime = ref(0)
const duration = ref(180)
const volume = ref(70)
const isMuted = ref(false)

// 计算属性
const progressPercentage = computed(() => {
  return (currentTime.value / duration.value) * 100
})

// 音频播放器实例
let audioPlayer: HTMLAudioElement | null = null

// 生命周期
onMounted(() => {
  audioPlayer = new Audio()
  setupAudioEvents()
})

onUnmounted(() => {
  if (audioPlayer) {
    audioPlayer.pause()
    audioPlayer = null
  }
})

// 音频事件设置
const setupAudioEvents = () => {
  if (!audioPlayer) return
  
  audioPlayer.addEventListener('timeupdate', () => {
    currentTime.value = audioPlayer!.currentTime
  })
  
  audioPlayer.addEventListener('loadedmetadata', () => {
    duration.value = audioPlayer!.duration
  })
  
  audioPlayer.addEventListener('ended', () => {
    nextSong()
  })
}

// 播放控制方法
const togglePlay = () => {
  if (!audioPlayer) return
  
  if (isPlaying.value) {
    audioPlayer.pause()
  } else {
    // 设置音频源（这里使用示例音频）
    if (!audioPlayer.src) {
      audioPlayer.src = currentSong.value.url || 'https://example.com/sample.mp3'
    }
    audioPlayer.play()
  }
  isPlaying.value = !isPlaying.value
}

const previousSong = () => {
  // 切换到上一首歌曲的逻辑
  console.log('上一首')
  resetPlayer()
}

const nextSong = () => {
  // 切换到下一首歌曲的逻辑
  console.log('下一首')
  resetPlayer()
}

const resetPlayer = () => {
  if (audioPlayer) {
    audioPlayer.pause()
    audioPlayer.currentTime = 0
    isPlaying.value = false
    currentTime.value = 0
  }
}

// 进度条控制
const seekToPosition = (event: MouseEvent) => {
  if (!audioPlayer) return
  
  const progressBar = event.currentTarget as HTMLElement
  const clickPosition = event.offsetX
  const progressBarWidth = progressBar.offsetWidth
  const seekTime = (clickPosition / progressBarWidth) * duration.value
  
  audioPlayer.currentTime = seekTime
  currentTime.value = seekTime
}

// 音量控制
const setVolume = (event: MouseEvent) => {
  const volumeBar = event.currentTarget as HTMLElement
  const clickPosition = event.offsetX
  const volumeBarWidth = volumeBar.offsetWidth
  const newVolume = (clickPosition / volumeBarWidth) * 100
  
  volume.value = Math.max(0, Math.min(100, newVolume))
  
  if (audioPlayer) {
    audioPlayer.volume = volume.value / 100
    if (isMuted.value && volume.value > 0) {
      isMuted.value = false
    }
  }
}

const toggleMute = () => {
  if (!audioPlayer) return
  
  if (isMuted.value) {
    audioPlayer.volume = volume.value / 100
    isMuted.value = false
  } else {
    audioPlayer.volume = 0
    isMuted.value = true
  }
}

// 播放器界面控制
const minimizePlayer = () => {
  isMinimized.value = true
}

const expandPlayer = () => {
  isMinimized.value = false
}

// 工具方法
const formatTime = (seconds: number) => {
  const mins = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60)
  return `${mins}:${secs.toString().padStart(2, '0')}`
}

// 外部方法：设置当前歌曲
const setCurrentSong = (song: Song) => {
  currentSong.value = song
  resetPlayer()
  
  if (audioPlayer) {
    audioPlayer.src = song.url || ''
    duration.value = song.duration
  }
}

// 暴露方法给父组件
defineExpose({
  setCurrentSong,
  togglePlay,
  nextSong,
  previousSong
})
</script>

<style scoped>
.music-player {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  z-index: 1000;
  box-shadow: 0 -2px 20px rgba(0, 0, 0, 0.3);
}

.player-main {
  padding: 20px;
}

.player-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.player-header h3 {
  margin: 0;
  font-size: 1.2em;
}

.minimize-btn {
  background: none;
  border: none;
  color: white;
  font-size: 1.5em;
  cursor: pointer;
  padding: 5px 10px;
}

.player-content {
  display: grid;
  grid-template-columns: 1fr 2fr 1fr;
  gap: 30px;
  align-items: center;
}

.song-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.album-cover {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.1);
}

.album-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.song-details h4 {
  margin: 0 0 5px 0;
  font-size: 1.1em;
}

.song-details p {
  margin: 0;
  opacity: 0.8;
  font-size: 0.9em;
}

.progress-section {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.time-display {
  display: flex;
  justify-content: space-between;
  font-size: 0.9em;
  opacity: 0.8;
}

.progress-bar {
  position: relative;
  height: 4px;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 2px;
  cursor: pointer;
}

.progress {
  height: 100%;
  background: white;
  border-radius: 2px;
  transition: width 0.1s;
}

.progress-handle {
  position: absolute;
  top: 50%;
  transform: translate(-50%, -50%);
  width: 12px;
  height: 12px;
  background: white;
  border-radius: 50%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.player-controls {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
}

.control-btn, .play-btn {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  color: white;
  font-size: 1.5em;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
}

.play-btn {
  background: white;
  color: #667eea;
  font-size: 1.8em;
}

.control-btn:hover, .play-btn:hover {
  transform: scale(1.1);
}

.volume-control {
  display: flex;
  align-items: center;
  gap: 10px;
}

.volume-btn {
  background: none;
  border: none;
  color: white;
  font-size: 1.2em;
  cursor: pointer;
}

.volume-bar {
  width: 80px;
  height: 4px;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 2px;
  cursor: pointer;
}

.volume-level {
  height: 100%;
  background: white;
  border-radius: 2px;
  transition: width 0.1s;
}

/* 迷你播放器样式 */
.mini-player {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  cursor: pointer;
  transition: all 0.3s;
}

.mini-player:hover {
  background: rgba(255, 255, 255, 0.1);
}

.mini-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.mini-title {
  font-weight: 600;
  font-size: 0.9em;
}

.mini-artist {
  font-size: 0.8em;
  opacity: 0.8;
}

.mini-controls {
  display: flex;
  gap: 10px;
  align-items: center;
}

.mini-play-btn, .mini-next-btn {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  color: white;
  font-size: 1.2em;
  width: 35px;
  height: 35px;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.mini-play-btn:hover, .mini-next-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .player-content {
    grid-template-columns: 1fr;
    gap: 20px;
    text-align: center;
  }
  
  .song-info {
    justify-content: center;
  }
  
  .volume-control {
    justify-content: center;
  }
}
</style>