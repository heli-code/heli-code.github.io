# MusicFlow - AI智能音乐平台

一个完整的音乐流媒体平台，包含前端Vue.js应用和后端Spring Boot服务。

## 项目结构

```
musicflow/
├── frontend/                 # Vue.js前端应用
│   ├── src/
│   │   ├── components/      # Vue组件
│   │   ├── views/           # 页面视图
│   │   ├── router/          # 路由配置
│   │   ├── stores/          # 状态管理
│   │   └── styles/          # 样式文件
│   ├── package.json
│   └── vite.config.ts
├── backend/                  # Spring Boot后端服务
│   ├── src/main/java/com/musicflow/
│   │   ├── config/          # 配置类
│   │   ├── controller/       # 控制器
│   │   ├── service/         # 服务层
│   │   ├── repository/      # 数据访问层
│   │   ├── entity/          # 实体类
│   │   └── dto/             # 数据传输对象
│   ├── src/main/resources/  # 资源文件
│   ├── pom.xml
│   └── README.md
└── README.md
```

## 技术栈

### 前端技术栈
- **框架**: Vue 3 + TypeScript
- **构建工具**: Vite
- **路由**: Vue Router 4
- **状态管理**: Pinia
- **HTTP客户端**: Axios
- **UI组件**: 自定义组件
- **样式**: SCSS + CSS3

### 后端技术栈
- **框架**: Spring Boot 3.2.0
- **数据库**: MySQL 8.0
- **安全**: Spring Security + JWT
- **ORM**: Spring Data JPA
- **构建工具**: Maven
- **Java版本**: 17

## 快速开始

### 1. 前端启动

```bash
# 进入前端目录
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端应用将在 http://localhost:5173 启动

### 2. 后端启动

#### 环境准备
- 安装Java 17+
- 安装MySQL 8.0+
- 创建数据库：`musicflow`

```bash
# 进入后端目录
cd backend

# 启动应用
mvn spring-boot:run

# 或使用启动脚本
start.bat
```

后端服务将在 http://localhost:8080 启动

### 3. 数据库配置

1. 创建数据库：
```sql
CREATE DATABASE musicflow CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改后端配置文件 `backend/src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/musicflow
    username: your-username
    password: your-password
```

## 功能特性

### 前端功能
- ✅ 响应式设计
- ✅ 日间/夜间主题切换
- ✅ 音乐播放器
- ✅ 歌曲搜索
- ✅ 用户个人中心
- ✅ 歌单管理
- ✅ AI助手界面

### 后端功能
- ✅ 用户认证（JWT）
- ✅ 歌曲管理
- ✅ 歌单管理
- ✅ 搜索功能
- ✅ 热门排行
- ✅ 数据验证
- ✅ 异常处理
- ✅ CORS跨域支持

## API接口文档

### 认证接口
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录

### 歌曲接口
- `GET /api/songs` - 获取所有歌曲
- `GET /api/songs/search?keyword=xxx` - 搜索歌曲
- `GET /api/songs/popular` - 热门歌曲
- `GET /api/songs/trending` - 趋势歌曲

### 歌单接口
- `GET /api/playlists/user/{userId}` - 获取用户歌单
- `POST /api/playlists` - 创建歌单
- `POST /api/playlists/{playlistId}/songs/{songId}` - 添加歌曲到歌单
- `DELETE /api/playlists/{playlistId}` - 删除歌单

## 开发说明

### 前端开发
```bash
cd frontend
npm run dev          # 开发模式
npm run build        # 生产构建
npm run preview      # 预览构建
```

### 后端开发
```bash
cd backend
mvn clean compile    # 编译
mvn spring-boot:run  # 运行
mvn test            # 测试
```

## 部署说明

### 生产环境部署

1. **前端部署**：
```bash
cd frontend
npm run build
# 将dist目录部署到Web服务器
```

2. **后端部署**：
```bash
cd backend
mvn clean package
java -jar target/musicflow-backend-1.0.0.jar
```

### Docker部署

前端和后端都可以使用Docker容器化部署，具体配置参考各目录下的Dockerfile。

## 贡献指南

1. Fork项目
2. 创建功能分支
3. 提交更改
4. 推送到分支
5. 创建Pull Request

## 许可证

MIT License

## 联系方式

如有问题或建议，请联系项目维护者。