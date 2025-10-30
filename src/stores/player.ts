import { defineStore } from 'pinia'
import { ref, computed, watch } from 'vue'
import type { Song } from '@/services/songService'

export interface PlaybackState {
  currentTime: number
  duration: number
  volume: number
  isMuted: boolean
  isPlaying: boolean
  isBuffering: boolean
}

export interface Playlist {
  id: string
  name: string
  songs: Song[]
  currentIndex: number
}

export const usePlayerStore = defineStore('player', () => {
  // 播放状态
  const playbackState = ref<PlaybackState>({
    currentTime: 0,
    duration: 0,
    volume: 0.8,
    isMuted: false,
    isPlaying: false,
    isBuffering: false
  })

  // 当前播放列表
  const currentPlaylist = ref<Playlist>({
    id: 'default',
    name: '默认播放列表',
    songs: [],
    currentIndex: -1
  })

  // 当前播放歌曲
  const currentSong = ref<Song | null>(null)

  // 播放历史
  const playHistory = ref<Song[]>([])

  // 播放模式：normal, repeat-one, repeat-all, shuffle
  const playMode = ref<'normal' | 'repeat-one' | 'repeat-all' | 'shuffle'>('normal')

  // 音频元素引用
  const audioElement = ref<HTMLAudioElement | null>(null)

  // 计算属性
  const progress = computed(() => {
    if (playbackState.value.duration === 0) return 0
    return (playbackState.value.currentTime / playbackState.value.duration) * 100
  })

  const hasNextSong = computed(() => {
    if (!currentPlaylist.value.songs.length) return false
    
    switch (playMode.value) {
      case 'repeat-one':
        return true
      case 'repeat-all':
        return true
      case 'shuffle':
        return currentPlaylist.value.songs.length > 1
      default:
        return currentPlaylist.value.currentIndex < currentPlaylist.value.songs.length - 1
    }
  })

  const hasPreviousSong = computed(() => {
    if (!currentPlaylist.value.songs.length) return false
    
    switch (playMode.value) {
      case 'repeat-one':
        return true
      case 'repeat-all':
        return true
      case 'shuffle':
        return currentPlaylist.value.songs.length > 1
      default:
        return currentPlaylist.value.currentIndex > 0
    }
  })

  // 初始化音频元素
  const initializeAudio = () => {
    if (!audioElement.value) {
      audioElement.value = new Audio()
      
      // 设置音频事件监听器
      audioElement.value.addEventListener('timeupdate', handleTimeUpdate)
      audioElement.value.addEventListener('loadedmetadata', handleLoadedMetadata)
      audioElement.value.addEventListener('ended', handleEnded)
      audioElement.value.addEventListener('waiting', handleWaiting)
      audioElement.value.addEventListener('canplay', handleCanPlay)
      audioElement.value.addEventListener('error', handleError)
      
      // 设置初始音量
      audioElement.value.volume = playbackState.value.volume
    }
  }

  // 音频事件处理
  const handleTimeUpdate = () => {
    if (audioElement.value) {
      playbackState.value.currentTime = audioElement.value.currentTime
    }
  }

  const handleLoadedMetadata = () => {
    if (audioElement.value) {
      playbackState.value.duration = audioElement.value.duration
    }
  }

  const handleEnded = () => {
    playbackState.value.isPlaying = false
    nextSong()
  }

  const handleWaiting = () => {
    playbackState.value.isBuffering = true
  }

  const handleCanPlay = () => {
    playbackState.value.isBuffering = false
  }

  const handleError = (error: Event) => {
    console.error('音频播放错误:', error)
    playbackState.value.isPlaying = false
    playbackState.value.isBuffering = false
  }

  // 播放控制方法
  const play = async (song?: Song) => {
    if (song) {
      await setCurrentSong(song)
    }
    
    if (audioElement.value && currentSong.value) {
      try {
        await audioElement.value.play()
        playbackState.value.isPlaying = true
        
        // 添加到播放历史
        if (currentSong.value) {
          playHistory.value = playHistory.value.filter(s => s.id !== currentSong.value?.id)
          playHistory.value.unshift(currentSong.value)
          
          // 限制播放历史长度
          if (playHistory.value.length > 50) {
            playHistory.value = playHistory.value.slice(0, 50)
          }
        }
      } catch (error) {
        console.error('播放失败:', error)
        playbackState.value.isPlaying = false
      }
    }
  }

  const pause = () => {
    if (audioElement.value) {
      audioElement.value.pause()
      playbackState.value.isPlaying = false
    }
  }

  const togglePlay = () => {
    if (playbackState.value.isPlaying) {
      pause()
    } else {
      play()
    }
  }

  const setCurrentSong = async (song: Song) => {
    if (audioElement.value) {
      // 暂停当前播放
      pause()
      
      // 设置新的音频源
      audioElement.value.src = song.audioUrl
      currentSong.value = song
      playbackState.value.currentTime = 0
      
      // 更新播放列表索引
      const index = currentPlaylist.value.songs.findIndex(s => s.id === song.id)
      if (index !== -1) {
        currentPlaylist.value.currentIndex = index
      }
      
      // 预加载音频
      await audioElement.value.load()
    }
  }

  const nextSong = () => {
    if (!hasNextSong.value) return
    
    let nextIndex: number
    
    switch (playMode.value) {
      case 'repeat-one':
        nextIndex = currentPlaylist.value.currentIndex
        break
      case 'repeat-all':
        nextIndex = (currentPlaylist.value.currentIndex + 1) % currentPlaylist.value.songs.length
        break
      case 'shuffle':
        nextIndex = Math.floor(Math.random() * currentPlaylist.value.songs.length)
        break
      default:
        nextIndex = currentPlaylist.value.currentIndex + 1
    }
    
    const nextSong = currentPlaylist.value.songs[nextIndex]
    if (nextSong) {
      setCurrentSong(nextSong)
      play()
    }
  }

  const previousSong = () => {
    if (!hasPreviousSong.value) return
    
    let prevIndex: number
    
    switch (playMode.value) {
      case 'repeat-one':
        prevIndex = currentPlaylist.value.currentIndex
        break
      case 'repeat-all':
        prevIndex = (currentPlaylist.value.currentIndex - 1 + currentPlaylist.value.songs.length) % currentPlaylist.value.songs.length
        break
      case 'shuffle':
        prevIndex = Math.floor(Math.random() * currentPlaylist.value.songs.length)
        break
      default:
        prevIndex = currentPlaylist.value.currentIndex - 1
    }
    
    const prevSong = currentPlaylist.value.songs[prevIndex]
    if (prevSong) {
      setCurrentSong(prevSong)
      play()
    }
  }

  const seek = (time: number) => {
    if (audioElement.value) {
      audioElement.value.currentTime = time
      playbackState.value.currentTime = time
    }
  }

  const setVolume = (volume: number) => {
    const newVolume = Math.max(0, Math.min(1, volume))
    playbackState.value.volume = newVolume
    
    if (audioElement.value) {
      audioElement.value.volume = newVolume
    }
  }

  const toggleMute = () => {
    playbackState.value.isMuted = !playbackState.value.isMuted
    
    if (audioElement.value) {
      audioElement.value.muted = playbackState.value.isMuted
    }
  }

  const setPlayMode = (mode: 'normal' | 'repeat-one' | 'repeat-all' | 'shuffle') => {
    playMode.value = mode
  }

  const setPlaylist = (songs: Song[], playlistName = '播放列表') => {
    currentPlaylist.value = {
      id: Date.now().toString(),
      name: playlistName,
      songs,
      currentIndex: 0
    }
    
    if (songs.length > 0) {
      setCurrentSong(songs[0])
    }
  }

  const addToPlaylist = (song: Song) => {
    if (!currentPlaylist.value.songs.find(s => s.id === song.id)) {
      currentPlaylist.value.songs.push(song)
    }
  }

  const removeFromPlaylist = (songId: number) => {
    const index = currentPlaylist.value.songs.findIndex(s => s.id === songId)
    if (index !== -1) {
      currentPlaylist.value.songs.splice(index, 1)
      
      // 如果删除的是当前播放的歌曲
      if (currentPlaylist.value.currentIndex === index) {
        if (currentPlaylist.value.songs.length > 0) {
          setCurrentSong(currentPlaylist.value.songs[Math.min(index, currentPlaylist.value.songs.length - 1)])
        } else {
          currentSong.value = null
          playbackState.value.isPlaying = false
        }
      }
    }
  }

  // 初始化
  initializeAudio()

  // 监听本地存储变化
  watch(playbackState.value, (newState) => {
    localStorage.setItem('musicflow_player_state', JSON.stringify(newState))
  }, { deep: true })

  watch(playMode, (newMode) => {
    localStorage.setItem('musicflow_play_mode', newMode)
  })

  return {
    // 状态
    playbackState,
    currentPlaylist,
    currentSong,
    playHistory,
    playMode,
    
    // 计算属性
    progress,
    hasNextSong,
    hasPreviousSong,
    
    // 方法
    play,
    pause,
    togglePlay,
    setCurrentSong,
    nextSong,
    previousSong,
    seek,
    setVolume,
    toggleMute,
    setPlayMode,
    setPlaylist,
    addToPlaylist,
    removeFromPlaylist
  }
})