/**
 * 前端性能监控工具
 */

// 性能指标接口
export interface PerformanceMetrics {
  loadTime: number
  firstContentfulPaint: number
  largestContentfulPaint: number
  cumulativeLayoutShift: number
  firstInputDelay: number
  timeToInteractive: number
}

// 资源加载监控
export interface ResourceTiming {
  name: string
  duration: number
  size: number
  type: string
}

/**
 * 性能监控类
 */
export class PerformanceMonitor {
  private metrics: Partial<PerformanceMetrics> = {}
  private resourceTimings: ResourceTiming[] = []
  private observer: PerformanceObserver | null = null

  constructor() {
    this.init()
  }

  /**
   * 初始化性能监控
   */
  private init() {
    // 监听性能条目
    if ('PerformanceObserver' in window) {
      this.observer = new PerformanceObserver((list) => {
        list.getEntries().forEach((entry) => {
          this.handlePerformanceEntry(entry)
        })
      })

      // 监听不同类型的性能指标
      this.observer.observe({ entryTypes: ['navigation', 'paint', 'largest-contentful-paint', 'layout-shift', 'first-input'] })
      this.observer.observe({ entryTypes: ['resource'] })
    }

    // 页面加载完成时收集最终指标
    window.addEventListener('load', () => {
      this.collectFinalMetrics()
    })
  }

  /**
   * 处理性能条目
   */
  private handlePerformanceEntry(entry: PerformanceEntry) {
    switch (entry.entryType) {
      case 'navigation':
        this.handleNavigationTiming(entry as PerformanceNavigationTiming)
        break
      case 'paint':
        this.handlePaintTiming(entry as PerformancePaintTiming)
        break
      case 'largest-contentful-paint':
        this.metrics.largestContentfulPaint = entry.startTime
        break
      case 'layout-shift':
        this.handleLayoutShift(entry as LayoutShift)
        break
      case 'first-input':
        this.handleFirstInput(entry as PerformanceEventTiming)
        break
      case 'resource':
        this.handleResourceTiming(entry as PerformanceResourceTiming)
        break
    }
  }

  /**
   * 处理导航性能指标
   */
  private handleNavigationTiming(timing: PerformanceNavigationTiming) {
    this.metrics.loadTime = timing.loadEventEnd - timing.navigationStart
    this.metrics.timeToInteractive = timing.domInteractive - timing.navigationStart
  }

  /**
   * 处理绘制性能指标
   */
  private handlePaintTiming(timing: PerformancePaintTiming) {
    if (timing.name === 'first-contentful-paint') {
      this.metrics.firstContentfulPaint = timing.startTime
    }
  }

  /**
   * 处理布局偏移指标
   */
  private handleLayoutShift(entry: LayoutShift) {
    if (!entry.hadRecentInput) {
      this.metrics.cumulativeLayoutShift = (this.metrics.cumulativeLayoutShift || 0) + entry.value
    }
  }

  /**
   * 处理首次输入延迟
   */
  private handleFirstInput(entry: PerformanceEventTiming) {
    this.metrics.firstInputDelay = entry.processingStart - entry.startTime
  }

  /**
   * 处理资源加载时间
   */
  private handleResourceTiming(timing: PerformanceResourceTiming) {
    // 只监控重要的资源类型
    const importantTypes = ['script', 'css', 'image', 'font']
    const resourceType = timing.initiatorType
    
    if (importantTypes.includes(resourceType)) {
      this.resourceTimings.push({
        name: timing.name,
        duration: timing.responseEnd - timing.requestStart,
        size: timing.transferSize || 0,
        type: resourceType
      })
    }
  }

  /**
   * 收集最终性能指标
   */
  private collectFinalMetrics() {
    // 确保所有指标都已收集
    setTimeout(() => {
      this.reportMetrics()
    }, 1000)
  }

  /**
   * 报告性能指标
   */
  private reportMetrics() {
    const metrics = this.getMetrics()
    
    // 发送到性能监控服务
    this.sendToAnalytics(metrics)
    
    // 控制台输出（开发环境）
    if (process.env.NODE_ENV === 'development') {
      console.log('📊 性能指标:', metrics)
      console.log('📦 资源加载:', this.resourceTimings)
    }
  }

  /**
   * 获取性能指标
   */
  public getMetrics(): PerformanceMetrics {
    return {
      loadTime: this.metrics.loadTime || 0,
      firstContentfulPaint: this.metrics.firstContentfulPaint || 0,
      largestContentfulPaint: this.metrics.largestContentfulPaint || 0,
      cumulativeLayoutShift: this.metrics.cumulativeLayoutShift || 0,
      firstInputDelay: this.metrics.firstInputDelay || 0,
      timeToInteractive: this.metrics.timeToInteractive || 0
    }
  }

  /**
   * 发送指标到分析服务
   */
  private sendToAnalytics(metrics: PerformanceMetrics) {
    // 这里可以集成到实际的监控服务
    // 例如：Google Analytics, Sentry, 自定义监控服务等
    
    // 简单的发送实现
    if (navigator.sendBeacon) {
      const data = new Blob([JSON.stringify({
        type: 'performance',
        metrics,
        timestamp: Date.now(),
        url: window.location.href
      })], { type: 'application/json' })
      
      navigator.sendBeacon('/api/analytics/performance', data)
    }
  }

  /**
   * 销毁监控器
   */
  public destroy() {
    if (this.observer) {
      this.observer.disconnect()
    }
  }
}

/**
 * 懒加载优化工具
 */
export class LazyLoader {
  private observer: IntersectionObserver | null = null
  private loadedElements = new Set<Element>()

  constructor() {
    this.initIntersectionObserver()
  }

  /**
   * 初始化交叉观察器
   */
  private initIntersectionObserver() {
    if ('IntersectionObserver' in window) {
      this.observer = new IntersectionObserver((entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting && !this.loadedElements.has(entry.target)) {
            this.loadElement(entry.target)
          }
        })
      }, {
        rootMargin: '50px 0px', // 提前50px开始加载
        threshold: 0.1
      })
    }
  }

  /**
   * 观察元素进行懒加载
   */
  public observe(element: Element, loadCallback: () => void) {
    if (this.loadedElements.has(element)) {
      loadCallback()
      return
    }

    if (this.observer) {
      // 存储加载回调
      element.setAttribute('data-lazy-callback', loadCallback.toString())
      this.observer.observe(element)
    } else {
      // 不支持IntersectionObserver，直接加载
      loadCallback()
    }
  }

  /**
   * 加载元素
   */
  private loadElement(element: Element) {
    const callbackStr = element.getAttribute('data-lazy-callback')
    if (callbackStr) {
      try {
        const callback = eval(`(${callbackStr})`) as () => void
        callback()
        this.loadedElements.add(element)
        this.observer?.unobserve(element)
      } catch (error) {
        console.error('懒加载回调执行失败:', error)
      }
    }
  }

  /**
   * 销毁懒加载器
   */
  public destroy() {
    if (this.observer) {
      this.observer.disconnect()
    }
    this.loadedElements.clear()
  }
}

/**
 * 图片懒加载组件
 */
export function lazyLoadImage(
  imgElement: HTMLImageElement,
  src: string,
  placeholder?: string
) {
  // 设置占位图
  if (placeholder) {
    imgElement.src = placeholder
  }

  // 创建IntersectionObserver进行懒加载
  const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        imgElement.src = src
        imgElement.onload = () => {
          imgElement.classList.add('loaded')
        }
        observer.unobserve(imgElement)
      }
    })
  })

  observer.observe(imgElement)
}

/**
 * 防抖函数 - 优化频繁触发的事件
 */
export function debounce<T extends (...args: any[]) => void>(
  func: T,
  wait: number
): (...args: Parameters<T>) => void {
  let timeout: NodeJS.Timeout | null = null
  
  return (...args: Parameters<T>) => {
    if (timeout) {
      clearTimeout(timeout)
    }
    
    timeout = setTimeout(() => {
      func(...args)
    }, wait)
  }
}

/**
 * 节流函数 - 控制函数执行频率
 */
export function throttle<T extends (...args: any[]) => void>(
  func: T,
  limit: number
): (...args: Parameters<T>) => void {
  let inThrottle: boolean = false
  
  return (...args: Parameters<T>) => {
    if (!inThrottle) {
      func(...args)
      inThrottle = true
      setTimeout(() => {
        inThrottle = false
      }, limit)
    }
  }
}

/**
 * 预加载关键资源
 */
export function preloadCriticalResources() {
  const criticalResources = [
    // 关键CSS和JS文件
    '/src/styles/main.css',
    '/src/utils/api.js'
  ]

  criticalResources.forEach((resource) => {
    const link = document.createElement('link')
    link.rel = 'preload'
    link.href = resource
    
    if (resource.endsWith('.css')) {
      link.as = 'style'
    } else if (resource.endsWith('.js')) {
      link.as = 'script'
    }
    
    document.head.appendChild(link)
  })
}

/**
 * 检查网络连接状态
 */
export function checkNetworkStatus(): Promise<boolean> {
  return new Promise((resolve) => {
    if ('connection' in navigator) {
      const connection = (navigator as any).connection
      resolve(connection.effectiveType !== 'slow-2g' && connection.effectiveType !== '2g')
    } else {
      // 默认认为网络良好
      resolve(true)
    }
  })
}

/**
 * 根据网络状态调整加载策略
 */
export async function adaptiveLoading() {
  const isGoodNetwork = await checkNetworkStatus()
  
  if (!isGoodNetwork) {
    // 网络较差时的优化策略
    console.log('📶 网络状态较差，启用优化加载策略')
    
    // 延迟加载非关键资源
    const nonCriticalResources = document.querySelectorAll('[data-lazy]')
    nonCriticalResources.forEach((element) => {
      element.setAttribute('data-loading', 'delayed')
    })
    
    // 降低图片质量
    const images = document.querySelectorAll('img[data-src]')
    images.forEach((img) => {
      const originalSrc = img.getAttribute('data-src')
      if (originalSrc) {
        // 可以在这里添加低质量版本的处理逻辑
        img.setAttribute('src', originalSrc)
      }
    })
  }
}

// 导出单例实例
export const performanceMonitor = new PerformanceMonitor()
export const lazyLoader = new LazyLoader()

// 默认导出
export default {
  PerformanceMonitor,
  LazyLoader,
  performanceMonitor,
  lazyLoader,
  lazyLoadImage,
  debounce,
  throttle,
  preloadCriticalResources,
  checkNetworkStatus,
  adaptiveLoading
}