<template>
  <button 
    :class="buttonClasses"
    :disabled="disabled"
    @click="handleClick"
    :type="type"
  >
    <span v-if="loading" class="button-loading">
      <span class="spinner"></span>
    </span>
    <span v-else-if="icon" class="button-icon">
      {{ icon }}
    </span>
    <span class="button-content">
      <slot></slot>
    </span>
  </button>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  variant?: 'primary' | 'secondary' | 'danger' | 'ghost'
  size?: 'sm' | 'md' | 'lg'
  disabled?: boolean
  loading?: boolean
  icon?: string
  type?: 'button' | 'submit' | 'reset'
  block?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  variant: 'primary',
  size: 'md',
  disabled: false,
  loading: false,
  type: 'button',
  block: false
})

const emit = defineEmits<{
  click: [event: MouseEvent]
}>()

const buttonClasses = computed(() => ({
  'btn': true,
  [`btn-${props.variant}`]: true,
  [`btn-${props.size}`]: true,
  'btn-disabled': props.disabled,
  'btn-loading': props.loading,
  'btn-block': props.block,
  'btn-icon-only': !$slots.default && props.icon
}))

const handleClick = (event: MouseEvent) => {
  if (!props.disabled && !props.loading) {
    emit('click', event)
  }
}
</script>

<style scoped>
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  border: none;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  text-decoration: none;
  font-family: inherit;
  position: relative;
  overflow: hidden;
}

/* 尺寸 */
.btn-sm {
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
  min-height: 32px;
}

.btn-md {
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
  min-height: 40px;
}

.btn-lg {
  padding: 1rem 2rem;
  font-size: 1.125rem;
  min-height: 48px;
}

/* 变体 */
.btn-primary {
  background: var(--primary-color);
  color: white;
}

.btn-primary:hover:not(.btn-disabled) {
  background: #3ab0d9;
  transform: translateY(-1px);
}

.btn-secondary {
  background: rgba(255, 255, 255, 0.1);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.btn-secondary:hover:not(.btn-disabled) {
  background: rgba(255, 255, 255, 0.15);
  border-color: rgba(255, 255, 255, 0.3);
}

.btn-danger {
  background: #ff4757;
  color: white;
}

.btn-danger:hover:not(.btn-disabled) {
  background: #ff3742;
}

.btn-ghost {
  background: transparent;
  color: var(--text-color);
  border: 1px solid transparent;
}

.btn-ghost:hover:not(.btn-disabled) {
  background: rgba(255, 255, 255, 0.05);
  border-color: rgba(255, 255, 255, 0.1);
}

/* 状态 */
.btn-disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none !important;
}

.btn-loading {
  pointer-events: none;
}

.btn-block {
  width: 100%;
}

.btn-icon-only {
  padding: 0.5rem;
  aspect-ratio: 1;
}

/* 加载动画 */
.button-loading {
  display: flex;
  align-items: center;
}

.spinner {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: white;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 图标 */
.button-icon {
  font-size: 1.2em;
  line-height: 1;
}

/* 内容 */
.button-content {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

/* 涟漪效果 */
.btn::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  transform: translate(-50%, -50%);
  transition: width 0.3s ease, height 0.3s ease;
}

.btn:active::after {
  width: 100%;
  height: 100%;
}

/* 响应式 */
@media (max-width: 768px) {
  .btn-md {
    padding: 0.625rem 1.25rem;
    font-size: 0.9rem;
  }
  
  .btn-lg {
    padding: 0.875rem 1.75rem;
    font-size: 1rem;
  }
}
</style>