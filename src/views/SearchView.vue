<template>
  <div class="search-view">
    <div class="search-content">
      <div class="page-header">
        <BackButton />
        <h1>搜索</h1>
      </div>
      
      <div class="search-header">
        <div class="search-box">
          <input 
            v-model="searchQuery" 
            type="text" 
            placeholder="搜索歌曲、歌手、专辑..."
            @keyup.enter="performSearch"
          />
          <button @click="performSearch">搜索</button>
        </div>
        
        <div class="search-filters">
          <button 
            v-for="filter in filters" 
            :key="filter"
            :class="['filter-btn', { active: activeFilter === filter }]"
            @click="activeFilter = filter"
          >
            {{ filter }}
          </button>
        </div>
      </div>
      
      <div class="search-results">
        <!-- 歌曲结果 -->
        <section v-if="activeFilter === '歌曲'" class="songs-section">
          <h2>歌曲</h2>
          <div class="songs-list">
            <div class="song-item" v-for="i in 8" :key="i">
              <div class="song-number">{{ i }}</div>
              <div class="song-cover"></div>
              <div class="song-info">
                <h3>搜索结果歌曲 {{ i }}</h3>
                <p>歌手名称</p>
              </div>
              <div class="song-duration">3:45</div>
              <div class="song-actions">
                <button class="action-btn">播放</button>
                <button class="action-btn">收藏</button>
              </div>
            </div>
          </div>
        </section>
        
        <!-- 歌手结果 -->
        <section v-if="activeFilter === '歌手'" class="artists-section">
          <h2>歌手</h2>
          <div class="artists-grid">
            <div class="artist-card" v-for="i in 6" :key="i">
              <div class="artist-avatar"></div>
              <h3>歌手 {{ i }}</h3>
              <p>{{ i * 100 }} 首歌曲</p>
            </div>
          </div>
        </section>
        
        <!-- 专辑结果 -->
        <section v-if="activeFilter === '专辑'" class="albums-section">
          <h2>专辑</h2>
          <div class="albums-grid">
            <div class="album-card" v-for="i in 6" :key="i">
              <div class="album-cover"></div>
              <div class="album-info">
                <h3>搜索结果专辑 {{ i }}</h3>
                <p>歌手名称</p>
                <span>{{ i * 10 }} 首歌曲</span>
              </div>
            </div>
          </div>
        </section>
        
        <!-- 歌单结果 -->
        <section v-if="activeFilter === '歌单'" class="playlists-section">
          <h2>歌单</h2>
          <div class="playlists-grid">
            <div class="playlist-card" v-for="i in 6" :key="i">
              <div class="playlist-cover"></div>
              <div class="playlist-info">
                <h3>搜索结果歌单 {{ i }}</h3>
                <p>创建者：用户 {{ i }}</p>
                <span>{{ i * 20 }} 首歌曲</span>
              </div>
            </div>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import BackButton from '@/components/BackButton.vue'

const searchQuery = ref('')
const activeFilter = ref('歌曲')
const filters = ['歌曲', '歌手', '专辑', '歌单']

const performSearch = () => {
  if (!searchQuery.value.trim()) return
  console.log('搜索:', searchQuery.value)
}
</script>

<style scoped lang="scss">
.search-view {
  padding: 2rem;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
  color: white;
  min-height: 100vh;
  
  .page-header {
    display: flex;
    align-items: center;
    gap: 1rem;
    margin-bottom: 1rem;
    
    h1 {
      font-size: 2rem;
      color: #4cc9f0;
      margin: 0;
    }
  }
  
  .search-header {
    margin-bottom: 2rem;
  }
  
  .search-box {
    display: flex;
    gap: 1rem;
    margin-bottom: 1.5rem;
    
    input {
      flex: 1;
      background: rgba(255, 255, 255, 0.1);
      border: none;
      color: white;
      padding: 1rem 1.5rem;
      border-radius: 25px;
      font-size: 1.1rem;
      
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
      padding: 1rem 2rem;
      border-radius: 25px;
      cursor: pointer;
      font-size: 1.1rem;
      transition: background 0.3s ease;
      
      &:hover {
        background: #3ab0d9;
      }
    }
  }
  
  .search-filters {
    display: flex;
    gap: 1rem;
    
    .filter-btn {
      background: rgba(255, 255, 255, 0.1);
      border: none;
      color: rgba(255, 255, 255, 0.7);
      padding: 0.8rem 1.5rem;
      border-radius: 20px;
      cursor: pointer;
      transition: all 0.3s ease;
      
      &.active, &:hover {
        background: rgba(76, 201, 240, 0.3);
        color: #4cc9f0;
      }
    }
  }
  
  .search-results {
    h2 {
      font-size: 1.5rem;
      margin-bottom: 1rem;
      color: #4cc9f0;
    }
  }
  
  .songs-list {
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }
  
  .song-item {
    display: flex;
    align-items: center;
    background: rgba(255, 255, 255, 0.1);
    padding: 1rem;
    border-radius: 12px;
    transition: background 0.3s ease;
    
    &:hover {
      background: rgba(255, 255, 255, 0.15);
    }
    
    .song-number {
      width: 30px;
      text-align: center;
      margin-right: 1rem;
      color: rgba(255, 255, 255, 0.7);
    }
    
    .song-cover {
      width: 50px;
      height: 50px;
      background: linear-gradient(45deg, #4cc9f0, #f72585);
      border-radius: 8px;
      margin-right: 1rem;
    }
    
    .song-info {
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
    
    .song-duration {
      margin: 0 1rem;
      color: rgba(255, 255, 255, 0.7);
    }
    
    .song-actions {
      display: flex;
      gap: 0.5rem;
      
      .action-btn {
        background: rgba(255, 255, 255, 0.1);
        border: none;
        color: white;
        padding: 0.5rem 1rem;
        border-radius: 15px;
        cursor: pointer;
        font-size: 0.8rem;
        transition: background 0.3s ease;
        
        &:hover {
          background: rgba(76, 201, 240, 0.3);
        }
      }
    }
  }
  
  .artists-grid, .albums-grid, .playlists-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 1.5rem;
  }
  
  .artist-card, .album-card, .playlist-card {
    background: rgba(255, 255, 255, 0.1);
    border-radius: 12px;
    padding: 1.5rem;
    text-align: center;
    transition: transform 0.3s ease;
    
    &:hover {
      transform: translateY(-5px);
      background: rgba(255, 255, 255, 0.15);
    }
  }
  
  .artist-avatar, .album-cover, .playlist-cover {
    width: 120px;
    height: 120px;
    background: linear-gradient(45deg, #4cc9f0, #f72585);
    border-radius: 50%;
    margin: 0 auto 1rem auto;
  }
  
  .album-cover, .playlist-cover {
    border-radius: 8px;
  }
  
  h3 {
    margin: 0.5rem 0;
    font-size: 1rem;
  }
  
  p {
    margin: 0.5rem 0;
    font-size: 0.9rem;
    color: rgba(255, 255, 255, 0.7);
  }
  
  span {
    font-size: 0.8rem;
    color: rgba(255, 255, 255, 0.6);
  }
}
</style>