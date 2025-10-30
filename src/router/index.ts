import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw, NavigationGuardNext, RouteLocationNormalized } from 'vue-router'
import { useUserStore } from '@/stores/user'

// 路由元信息类型定义
interface RouteMeta {
  title?: string
  requiresAuth?: boolean
  guestOnly?: boolean
  permissions?: string[]
}

// 懒加载组件函数，添加预加载提示
const lazyLoad = (component: () => Promise<any>, loadingComponent?: any) => {
  return () => ({
    component: component(),
    loading: loadingComponent
  })
}

// 预加载函数，用于关键页面的预加载
const preloadComponent = (component: () => Promise<any>) => {
  // 在空闲时间预加载组件
  if ('requestIdleCallback' in window) {
    requestIdleCallback(() => {
      component()
    })
  } else {
    // 降级处理，延迟预加载
    setTimeout(() => {
      component()
    }, 2000)
  }
}

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: lazyLoad(() => import('@/views/DiscoverView.vue')),
    meta: { title: '发现音乐', requiresAuth: false, preload: true }
  },
  {
    path: '/home',
    name: 'HomeOld',
    component: lazyLoad(() => import('@/views/HomeView.vue')),
    meta: { title: '旧首页', requiresAuth: false }
  },
  {
    path: '/library',
    name: 'Library',
    component: lazyLoad(() => import('@/views/LibraryView.vue')),
    meta: { title: '我的音乐', requiresAuth: true }
  },
  {
    path: '/social',
    name: 'Social',
    component: lazyLoad(() => import('@/views/SocialView.vue')),
    meta: { title: '音乐社交', requiresAuth: true }
  },
  {
    path: '/ai',
    name: 'AI',
    component: lazyLoad(() => import('@/views/AIView.vue')),
    meta: { title: 'AI助手', requiresAuth: true }
  },
  {
    path: '/search',
    name: 'Search',
    component: lazyLoad(() => import('@/views/SearchView.vue')),
    meta: { title: '搜索', requiresAuth: false }
  },
  {
    path: '/auth',
    name: 'Auth',
    component: lazyLoad(() => import('@/views/AuthView.vue')),
    meta: { title: '登录注册', guestOnly: true }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: lazyLoad(() => import('@/views/ProfileView.vue')),
    meta: { title: '个人资料', requiresAuth: true }
  },
  {
    path: '/auth-test',
    name: 'AuthTest',
    component: lazyLoad(() => import('@/views/AuthTestView.vue')),
    meta: { title: '认证测试', requiresAuth: false }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: lazyLoad(() => import('@/views/NotFoundView.vue')),
    meta: { title: '页面未找到' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext) => {
  const userStore = useUserStore()
  const meta = to.meta as RouteMeta
  
  // 设置页面标题
  if (meta.title) {
    document.title = `${meta.title} - MusicFlow`
  }
  
  // 检查认证状态
  const isAuthenticated = userStore.isAuthenticated
  
  // 需要认证的路由
  if (meta.requiresAuth && !isAuthenticated) {
    next({ name: 'Auth', query: { redirect: to.fullPath } })
    return
  }
  
  // 仅限未登录用户访问的路由
  if (meta.guestOnly && isAuthenticated) {
    next({ name: 'Home' })
    return
  }
  
  // 权限检查（预留功能）
  if (meta.permissions && meta.permissions.length > 0) {
    // 这里可以添加具体的权限检查逻辑
    const hasPermission = true // 暂时设为true，后续实现具体权限检查
    if (!hasPermission) {
      next({ name: 'NotFound' })
      return
    }
  }
  
  // 预加载相关页面（性能优化）
  if (meta.preload) {
    // 预加载用户可能访问的页面
    const preloadRoutes = ['/library', '/social', '/ai']
    preloadRoutes.forEach(route => {
      const routeRecord = router.resolve(route)
      if (routeRecord.matched.length > 0) {
        const component = routeRecord.matched[0].components?.default
        if (typeof component === 'function') {
          preloadComponent(component as () => Promise<any>)
        }
      }
    })
  }
  
  next()
})

// 路由后置守卫 - 用于页面统计等
router.afterEach((to: RouteLocationNormalized, from: RouteLocationNormalized) => {
  // 可以在这里添加页面访问统计
  console.log(`路由跳转: ${from.path} -> ${to.path}`)
})

// 路由错误处理
router.onError((error) => {
  console.error('路由错误:', error)
})

export default router