<template>
  <div class="library-view">
    <div class="library-content">
      <div class="page-header">
        <BackButton />
        <h1>我的音乐</h1>
      </div>
      <div class="library-tabs">
        <button 
          v-for="tab in tabs" 
          :key="tab" 
          :class="['tab-btn', { active: activeTab === tab }]"
          @click="activeTab = tab"
        >
          {{ tab }}
        </button>
      </div>
      
      <div class="tab-content">
        <!-- 我喜欢 -->
        <div v-if="activeTab === '我喜欢'" class="favorites-section">
          <div class="music-list">
            <div class="music-item" v-for="i in 10" :key="i">
              <div class="item-number">{{ i }}</div>
              <div class="item-cover"></div>
              <div class="item-info">
                <h3>我喜欢的歌曲 {{ i }}</h3>
                <p>歌手名称</p>
              </div>
              <div class="item-duration">3:45</div>
              <div class="item-actions">
                <button class="action-btn">播放</button>
                <button class="action-btn">移除</button>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 我的歌单 -->
        <div v-if="activeTab === '我的歌单'" class="playlists-section">
          <div class="playlist-grid">
            <div class="playlist-card" v-for="i in 6" :key="i">
              <div class="playlist-cover"></div>
              <div class="playlist-info">
                <h3>我的歌单 {{ i }}</h3>
                <p>{{ i * 5 }} 首歌曲</p>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 最近播放 -->
        <div v-if="activeTab === '最近播放'" class="recent-section">
          <div class="recent-list">
            <div class="recent-item" v-for="i in 8" :key="i">
              <div class="recent-cover"></div>
              <div class="recent-info">
                <h3>最近播放歌曲 {{ i }}</h3>
                <p>播放时间：{{ i }}小时前</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import BackButton from '@/components/BackButton.vue'

const tabs = ['我喜欢', '我的歌单', '最近播放']
const activeTab = ref('我喜欢')
</script>

<style scoped lang="scss">
.library-view {
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
  
  .library-tabs {
    display: flex;
    gap: 1rem;
    margin-bottom: 2rem;
    border-bottom: 1px solid rgba(255, 255, 255, 0.2);
    
    .tab-btn {
      background: none;
      border: none;
      color: rgba(255, 255, 255, 0.7);
      padding: 1rem 2rem;
      font-size: 1rem;
      cursor: pointer;
      border-bottom: 3px solid transparent;
      transition: all 0.3s ease;
      
      &.active {
        color: #4cc9f0;
        border-bottom-color: #4cc9f0;
      }
      
      &:hover {
        color: white;
      }
    }
  }
  
  .music-list, .recent-list {
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }
  
  .music-item, .recent-item {
    display: flex;
    align-items: center;
    background: rgba(255, 255, 255, 0.1);
    padding: 1rem;
    border-radius: 12px;
    transition: background 0.3s ease;
    
    &:hover {
      background: rgba(255, 255, 255, 0.15);
    }
    
    .item-number, .item-cover, .recent-cover {
      width: 40px;
      height: 40px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 1rem;
    }
    
    .item-cover, .recent-cover {
      background: linear-gradient(45deg, #4cc9f0, #f72585);
      border-radius: 8px;
    }
    
    .item-info {
      flex: 1;
      
      h3 {
        margin: 0 0 0.5rem 0;
        font-size: 1rem;
      }
      
      p {
        margin: 0;
        font-size: 0.9rem;
        color: rgba(255, 255, 255, 0.7);
      }
    }
    
    .item-duration {
      margin: 0 1rem;
      color: rgba(255, 255, 255, 0.7);
    }
    
    .item-actions {
      display: flex;
      gap: 0.5rem;
      
      .action-btn {
        background: rgba(255, 255, 255, 0.1);
        border: none;
        color: white;
        padding: 0.5rem 1rem;
        border-radius: 20px;
        cursor: pointer;
        font-size: 0.8rem;
        transition: background 0.3s ease;
        
        &:hover {
          background: rgba(76, 201, 240, 0.3);
        }
      }
    }
  }
  
  .playlist-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 1.5rem;
  }
  
  .playlist-card {
    background: rgba(255, 255, 255, 0.1);
    border-radius: 12px;
    padding: 1rem;
    transition: transform 0.3s ease;
    
    &:hover {
      transform: translateY(-5px);
      background: rgba(255, 255, 255, 0.15);
    }
    
    .playlist-cover {
      width: 100%;
      height: 120px;
      background: linear-gradient(45deg, #4cc9f0, #f72585);
      border-radius: 8px;
      margin-bottom: 0.5rem;
    }
    
    h3 {
      margin: 0.5rem 0;
      font-size: 1rem;
    }
    
    p {
      margin: 0;
      font-size: 0.9rem;
      color: rgba(255, 255, 255, 0.7);
    }
  }
}
</style>