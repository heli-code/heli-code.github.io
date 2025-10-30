<template>
  <button 
    class="back-button"
    @click="goBack"
    v-if="showBackButton"
  >
    <span class="back-icon">←</span>
    返回
  </button>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 只有在不是首页时才显示返回按钮
const showBackButton = computed(() => {
  return router.currentRoute.value.path !== '/'
})

const goBack = () => {
  if (window.history.length > 1) {
    router.back()
  } else {
    // 如果没有历史记录，则返回首页
    router.push('/')
  }
}
</script>

<style scoped lang="scss">
.back-button {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: rgba(255, 255, 255, 0.1);
  border: none;
  color: white;
  padding: 0.8rem 1.5rem;
  border-radius: 20px;
  cursor: pointer;
  font-size: 1rem;
  transition: all 0.3s ease;
  margin-right: 1rem;
  
  &:hover {
    background: rgba(76, 201, 240, 0.3);
    transform: translateY(-1px);
  }
  
  .back-icon {
    font-size: 1.2rem;
    font-weight: bold;
  }
}

@media (max-width: 768px) {
  .back-button {
    padding: 0.6rem 1rem;
    font-size: 0.9rem;
    margin-right: 0.5rem;
  }
}
</style>