# MusicFlow Backend

AI智能音乐平台后端服务，基于Spring Boot 3.2.0构建。

## 技术栈

- **框架**: Spring Boot 3.2.0
- **数据库**: MySQL 8.0
- **安全**: Spring Security + JWT
- **ORM**: Spring Data JPA
- **构建工具**: Maven
- **Java版本**: 17

## 项目结构

```
backend/
├── src/main/java/com/musicflow/
│   ├── MusicFlowApplication.java     # 应用启动类
│   ├── config/                       # 配置类
│   │   ├── SecurityConfig.java        # 安全配置
│   │   └── PasswordConfig.java       # 密码加密配置
│   ├── controller/                    # 控制器层
│   │   ├── AuthController.java        # 认证控制器
│   │   └── SongController.java        # 歌曲控制器
│   ├── service/                       # 服务层
│   │   ├── AuthService.java           # 认证服务
│   │   ├── JwtService.java            # JWT服务
│   │   └── SongService.java           # 歌曲服务
│   ├── repository/                    # 数据访问层
│   │   ├── UserRepository.java        # 用户仓库
│   │   └── SongRepository.java        # 歌曲仓库
│   ├── entity/                        # 实体类
│   │   ├── User.java                  # 用户实体
│   │   ├── Song.java                  # 歌曲实体
│   │   └── Playlist.java              # 歌单实体
│   └── dto/                           # 数据传输对象
│       ├── AuthRequest.java           # 认证请求
│       └── AuthResponse.java          # 认证响应
└── src/main/resources/
    ├── application.yml                # 应用配置
    └── data.sql                       # 初始化数据
```

## 快速开始

### 环境要求

- Java 17+
- MySQL 8.0+
- Maven 3.6+

### 数据库配置

1. 创建数据库：
```sql
CREATE DATABASE musicflow CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改配置文件 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/musicflow
    username: your-username
    password: your-password
```

### 启动应用

```bash
# 编译项目
mvn clean compile

# 运行应用
mvn spring-boot:run

# 或打包后运行
mvn clean package
java -jar target/musicflow-backend-1.0.0.jar
```

应用将在 http://localhost:8080 启动

## API接口

### 认证接口

- **用户注册**
  ```
  POST /api/auth/register
  Content-Type: application/json
  
  {
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com"
  }
  ```

- **用户登录**
  ```
  POST /api/auth/login
  Content-Type: application/json
  
  {
    "username": "testuser",
    "password": "password123"
  }
  ```

### 歌曲接口

- **获取所有歌曲**
  ```
  GET /api/songs
  ```

- **搜索歌曲**
  ```
  GET /api/songs/search?keyword=周杰伦
  ```

- **热门歌曲**
  ```
  GET /api/songs/popular
  ```

- **趋势歌曲**
  ```
  GET /api/songs/trending
  ```

## 功能特性

- ✅ 用户注册/登录
- ✅ JWT认证
- ✅ CORS跨域支持
- ✅ 歌曲管理
- ✅ 搜索功能
- ✅ 热门歌曲排行
- ✅ 数据验证
- ✅ 异常处理

## 开发说明

### 添加新实体

1. 在 `entity` 包中创建实体类
2. 在 `repository` 包中创建Repository接口
3. 在 `service` 包中创建Service类
4. 在 `controller` 包中创建Controller类

### 数据库迁移

项目使用JPA自动生成表结构，修改实体类后会自动更新数据库。

### 安全配置

- 默认开启CORS支持
- JWT token有效期24小时
- 密码使用BCrypt加密存储

## 部署

### 生产环境配置

修改 `application.yml` 中的数据库连接和生产环境配置：

```yaml
spring:
  profiles: production
  datasource:
    url: jdbc:mysql://production-db:3306/musicflow
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

### Docker部署

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/musicflow-backend-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```