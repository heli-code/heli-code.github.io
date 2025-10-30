<template>
  <div class="social-view">
    <div class="social-content">
      <div class="page-header">
        <BackButton />
        <h1>音乐社交</h1>
      </div>
      <div class="social-features">
        <section class="friends-section">
          <h2>音乐好友</h2>
          <div class="friends-list">
            <div class="friend-card" v-for="i in 6" :key="i">
              <div class="friend-avatar"></div>
              <div class="friend-info">
                <h3>好友 {{ i }}</h3>
                <p>正在听：热门歌曲</p>
                <span class="online-status">在线</span>
              </div>
              <button class="chat-btn">聊天</button>
            </div>
          </div>
        </section>
        
        <section class="shared-playlists">
          <h2>共享歌单</h2>
          <div class="shared-grid">
            <div class="shared-card" v-for="i in 4" :key="i">
              <div class="shared-cover"></div>
              <div class="shared-info">
                <h3>共享歌单 {{ i }}</h3>
                <p>由 好友 {{ i }} 创建</p>
                <div class="shared-stats">
                  <span>🎵 {{ i * 3 }} 首歌曲</span>
                  <span>👥 {{ i * 2 }} 人参与</span>
                </div>
              </div>
            </div>
          </div>
        </section>
        
        <section class="music-rooms">
          <h2>音乐房间</h2>
          <div class="rooms-grid">
            <div class="room-card" v-for="i in 3" :key="i">
              <div class="room-header">
                <h3>音乐房间 {{ i }}</h3>
                <span class="room-status">进行中</span>
              </div>
              <div class="room-info">
                <p>主题：{{ ['流行音乐', '电子舞曲', '古典音乐'][i-1] }}</p>
                <p>在线人数：{{ i * 3 }}</p>
              </div>
              <button class="join-btn">加入房间</button>
            </div>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import BackButton from '@/components/BackButton.vue'
// 社交功能逻辑
</script>

<style scoped lang="scss">
.social-view {
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
  
  .social-features {
    display: flex;
    flex-direction: column;
    gap: 3rem;
  }
  
  section {
    h2 {
      font-size: 1.5rem;
      margin-bottom: 1rem;
      color: #4cc9f0;
    }
  }
  
  .friends-list {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 1rem;
  }
  
  .friend-card {
    display: flex;
    align-items: center;
    background: rgba(255, 255, 255, 0.1);
    padding: 1rem;
    border-radius: 12px;
    transition: transform 0.3s ease;
    
    &:hover {
      transform: translateY(-2px);
      background: rgba(255, 255, 255, 0.15);
    }
    
    .friend-avatar {
      width: 50px;
      height: 50px;
      background: linear-gradient(45deg, #4cc9f0, #f72585);
      border-radius: 50%;
      margin-right: 1rem;
    }
    
    .friend-info {
      flex: 1;
      
      h3 {
        margin: 0 0 0.5rem 0;
        font-size: 1rem;
      }
      
      p {
        margin: 0 0 0.5rem 0;
        font-size: 0.9rem;
        color: rgba(255, 255, 255, 0.7);
      }
      
      .online-status {
        background: rgba(76, 201, 240, 0.2);
        color: #4cc9f0;
        padding: 0.2rem 0.5rem;
        border-radius: 12px;
        font-size: 0.8rem;
      }
    }
    
    .chat-btn {
      background: rgba(76, 201, 240, 0.3);
      border: none;
      color: white;
      padding: 0.5rem 1rem;
      border-radius: 20px;
      cursor: pointer;
      transition: background 0.3s ease;
      
      &:hover {
        background: rgba(76, 201, 240, 0.5);
      }
    }
  }
  
  .shared-grid, .rooms-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 1.5rem;
  }
  
  .shared-card, .room-card {
    background: rgba(255, 255, 255, 0.1);
    border-radius: 12px;
    padding: 1rem;
    transition: transform 0.3s ease;
    
    &:hover {
      transform: translateY(-5px);
      background: rgba(255, 255, 255, 0.15);
    }
  }
  
  .shared-card {
    .shared-cover {
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
      margin: 0.5rem 0;
      font-size: 0.9rem;
      color: rgba(255, 255, 255, 0.7);
    }
    
    .shared-stats {
      display: flex;
      gap: 1rem;
      font-size: 0.8rem;
      color: rgba(255, 255, 255, 0.6);
    }
  }
  
  .room-card {
    .room-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 1rem;
      
      h3 {
        margin: 0;
        font-size: 1.1rem;
      }
      
      .room-status {
        background: rgba(76, 201, 240, 0.2);
        color: #4cc9f0;
        padding: 0.3rem 0.8rem;
        border-radius: 15px;
        font-size: 0.8rem;
      }
    }
    
    .room-info {
      margin-bottom: 1rem;
      
      p {
        margin: 0.5rem 0;
        font-size: 0.9rem;
        color: rgba(255, 255, 255, 0.7);
      }
    }
    
    .join-btn {
      width: 100%;
      background: #4cc9f0;
      border: none;
      color: white;
      padding: 0.8rem;
      border-radius: 8px;
      cursor: pointer;
      font-size: 1rem;
      transition: background 0.3s ease;
      
      &:hover {
        background: #3ab0d9;
      }
    }
  }
}
</style>