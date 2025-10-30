<template>
  <div class="audio-player" :class="{ 'player-expanded': isExpanded }">
    <!-- 播放器控制栏 -->
    <div class="player-controls">
      <!-- 歌曲信息 -->
      <div class="song-info">
        <img :src="currentSong?.coverUrl || '/default-cover.jpg'" alt="封面" class="cover" />
        <div class="info">
          <div class="title">{{ currentSong?.title || '未选择歌曲' }}</div>
          <div class="artist">{{ currentSong?.artist || '未知艺术家' }}</div>
        </div>
      </div>

      <!-- 播放控制 -->
      <div class="controls">
        <button class="control-btn" @click="previousSong" :disabled="!hasPrevious">
          <span>⏮</span>
        </button>
        <button class="control-btn play-btn" @click="togglePlay">
          <span>{{ isPlaying ? '⏸' : '▶' }}</span>
        </button>
        <button class="control-btn" @click="nextSong" :disabled="!hasNext">
          <span>⏭</span>
        </button>
      </div>

      <!-- 进度条 -->
      <div class="progress-section">
        <div class="time">{{ formatTime(currentTime) }}</div>
        <div class="progress-bar" @click="seekToTime">
          <div class="progress" :style="{ width: progressPercentage + '%' }"></div>
        </div>
        <div class="time">{{ formatTime(duration) }}</div>
      </div>

      <!-- 音量控制 -->
      <div class="volume-control">
        <button class="control-btn" @click="toggleMute">
          <span>{{ isMuted ? '🔇' : volume > 0.5 ? '🔊' : '🔉' }}</span>
        </button>
        <div class="volume-bar" @click="setVolume">
          <div class="volume-level" :style="{ width: volume * 100 + '%' }"></div>
        </div>
      </div>

      <!-- 扩展按钮 -->
      <button class="expand-btn" @click="toggleExpand">
        <span>{{ isExpanded ? '▼' : '▲' }}</span>
      </button>
    </div>

    <!-- 扩展面板 -->
    <div v-if="isExpanded" class="expanded-panel">
      <div class="playlist-section">
        <h3>播放列表</h3>
        <div class="playlist">
          <div 
            v-for="(song, index) in playlist" 
            :key="song.id" 
            class="playlist-item"
            :class="{ active: currentSongIndex === index }"
            @click="playSong(index)"
          >
            <span class="index">{{ index + 1 }}</span>
            <img :src="song.coverUrl || '/default-cover.jpg'" alt="封面" />
            <div class="song-info">
              <div class="title">{{ song.title }}</div>
              <div class="artist">{{ song.artist }}</div>
            </div>
            <div class="duration">{{ formatTime(song.duration) }}</div>
            <button class="remove-btn" @click.stop="removeFromPlaylist(index)">×</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { usePlayerStore } from '@/stores/player'
import type { Song } from '@/services/songService'

const playerStore = usePlayerStore()

// 响应式数据
const audioElement = ref<HTMLAudioElement | null>(null)
const isExpanded = ref(false)
const isMuted = ref(false)
const volume = ref(0.8)
const currentTime = ref(0)
const duration = ref(0)

// 计算属性
const currentSong = computed(() => playerStore.currentSong)
const isPlaying = computed(() => playerStore.isPlaying)
const playlist = computed(() => playerStore.playlist)
const currentSongIndex = computed(() => playerStore.currentSongIndex)

const hasPrevious = computed(() => currentSongIndex.value > 0)
const hasNext = computed(() => currentSongIndex.value < playlist.value.length - 1)
const progressPercentage = computed(() => {
  return duration.value > 0 ? (currentTime.value / duration.value) * 100 : 0
})

// 方法
const togglePlay = () => {
  if (isPlaying.value) {
    playerStore.pause()
  } else {
    playerStore.play()
  }
}

const previousSong = () => {
  playerStore.previous()
}

const nextSong = () => {
  playerStore.next()
}

const playSong = (index: number) => {
  playerStore.playSong(index)
}

const removeFromPlaylist = (index: number) => {
  playerStore.removeFromPlaylist(index)
}

const toggleExpand = () => {
  isExpanded.value = !isExpanded.value
}

const toggleMute = () => {
  isMuted.value = !isMuted.value
  if (audioElement.value) {
    audioElement.value.muted = isMuted.value
  }
}

const setVolume = (event: MouseEvent) => {
  if (!audioElement.value) return
  
  const volumeBar = event.currentTarget as HTMLElement
  const rect = volumeBar.getBoundingClientRect()
  const clickX = event.clientX - rect.left
  const newVolume = Math.max(0, Math.min(1, clickX / rect.width))
  
  volume.value = newVolume
  audioElement.value.volume = newVolume
  isMuted.value = newVolume === 0
}

const seekToTime = (event: MouseEvent) => {
  if (!audioElement.value || !duration.value) return
  
  const progressBar = event.currentTarget as HTMLElement
  const rect = progressBar.getBoundingClientRect()
  const clickX = event.clientX - rect.left
  const newTime = (clickX / rect.width) * duration.value
  
  currentTime.value = newTime
  audioElement.value.currentTime = newTime
}

const formatTime = (seconds: number): string => {
  if (!seconds || isNaN(seconds)) return '0:00'
  
  const mins = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60)
  return `${mins}:${secs.toString().padStart(2, '0')}`
}

// 音频事件监听
const setupAudioListeners = () => {
  if (!audioElement.value) return
  
  audioElement.value.addEventListener('timeupdate', () => {
    currentTime.value = audioElement.value?.currentTime || 0
  })
  
  audioElement.value.addEventListener('loadedmetadata', () => {
    duration.value = audioElement.value?.duration || 0
  })
  
  audioElement.value.addEventListener('ended', () => {
    playerStore.next()
  })
}

// 生命周期
onMounted(() => {
  audioElement.value = playerStore.audioElement
  setupAudioListeners()
})

onUnmounted(() => {
  // 清理事件监听器
})
</script>

<style scoped>
.audio-player {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(15, 20, 25, 0.95);
  backdrop-filter: blur(20px);
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  z-index: 1000;
  transition: all 0.3s ease;
}

.player-expanded {
  height: 400px;
}

.player-controls {
  display: flex;
  align-items: center;
  padding: 1rem;
  gap: 1.5rem;
}

.song-info {
  display: flex;
  align-items: center;
  gap: 1rem;
  min-width: 200px;
}

.cover {
  width: 50px;
  height: 50px;
  border-radius: 8px;
  object-fit: cover;
}

.info .title {
  font-weight: 600;
  font-size: 0.9rem;
}

.info .artist {
  font-size: 0.8rem;
  color: rgba(255, 255, 255, 0.7);
}

.controls {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.control-btn {
  background: none;
  border: none;
  color: white;
  font-size: 1.2rem;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 50%;
  transition: all 0.2s ease;
}

.control-btn:hover:not(:disabled) {
  background: rgba(255, 255, 255, 0.1);
}

.control-btn:disabled {
  opacity: 0.3;
  cursor: not-allowed;
}

.play-btn {
  background: #4cc9f0;
  font-size: 1.5rem;
}

.play-btn:hover {
  background: #3ab0d9;
  transform: scale(1.1);
}

.progress-section {
  display: flex;
  align-items: center;
  gap: 1rem;
  flex: 1;
}

.time {
  font-size: 0.8rem;
  color: rgba(255, 255, 255, 0.7);
  min-width: 40px;
}

.progress-bar {
  flex: 1;
  height: 4px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 2px;
  cursor: pointer;
  position: relative;
}

.progress {
  height: 100%;
  background: #4cc9f0;
  border-radius: 2px;
  transition: width 0.1s ease;
}

.volume-control {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  min-width: 120px;
}

.volume-bar {
  flex: 1;
  height: 4px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 2px;
  cursor: pointer;
  position: relative;
}

.volume-level {
  height: 100%;
  background: #4cc9f0;
  border-radius: 2px;
}

.expand-btn {
  background: none;
  border: none;
  color: white;
  cursor: pointer;
  padding: 0.5rem;
}

.expanded-panel {
  height: calc(100% - 80px);
  padding: 1rem;
  overflow-y: auto;
}

.playlist-section h3 {
  margin-bottom: 1rem;
  font-size: 1.1rem;
}

.playlist {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.playlist-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 0.5rem;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.playlist-item:hover {
  background: rgba(255, 255, 255, 0.05);
}

.playlist-item.active {
  background: rgba(76, 201, 240, 0.2);
}

.playlist-item img {
  width: 40px;
  height: 40px;
  border-radius: 4px;
  object-fit: cover;
}

.playlist-item .song-info {
  flex: 1;
}

.playlist-item .title {
  font-size: 0.9rem;
  font-weight: 500;
}

.playlist-item .artist {
  font-size: 0.8rem;
  color: rgba(255, 255, 255, 0.7);
}

.playlist-item .duration {
  font-size: 0.8rem;
  color: rgba(255, 255, 255, 0.5);
}

.remove-btn {
  background: none;
  border: none;
  color: rgba(255, 255, 255, 0.5);
  cursor: pointer;
  font-size: 1.2rem;
  padding: 0.2rem;
}

.remove-btn:hover {
  color: #ff4757;
}

@media (max-width: 768px) {
  .player-controls {
    padding: 0.8rem;
    gap: 1rem;
  }
  
  .song-info {
    min-width: 150px;
  }
  
  .cover {
    width: 40px;
    height: 40px;
  }
  
  .volume-control {
    min-width: 80px;
  }
}
</style>