# MusicFlow API Postman测试文档

## 基础信息
- **Base URL**: `http://localhost:8080/api`
- **端口**: 8080
- **Context Path**: /api

## 测试前准备
1. 确保后端服务已启动
2. 确保MySQL数据库已连接并初始化
3. 准备测试文件（音频和图片）

## 1. 认证接口测试

### 1.1 用户注册
**请求**:
- **方法**: POST
- **URL**: `/api/auth/register`
- **Headers**: `Content-Type: application/json`

**请求体**:
```json
{
    "username": "testuser",
    "password": "Test123456",
    "email": "test@example.com"
}
```

**预期响应**:
```json
{
    "code": 200,
    "message": "注册成功",
    "data": {
        "token": "jwt_token_here",
        "username": "testuser",
        "email": "test@example.com"
    }
}
```

### 1.2 用户登录
**请求**:
- **方法**: POST
- **URL**: `/api/auth/login`
- **Headers**: `Content-Type: application/json`

**请求体**:
```json
{
    "username": "testuser",
    "password": "Test123456"
}
```

**预期响应**:
```json
{
    "code": 200,
    "message": "登录成功",
    "data": {
        "token": "jwt_token_here",
        "username": "testuser",
        "email": "test@example.com"
    }
}
```

### 1.3 获取用户信息
**请求**:
- **方法**: GET
- **URL**: `/api/auth/profile`
- **Headers**: `Authorization: Bearer {token}`

**预期响应**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "id": 1,
        "username": "testuser",
        "email": "test@example.com",
        "avatar": null,
        "bio": null,
        "createdAt": "2024-01-01T00:00:00"
    }
}
```

### 1.4 更新用户信息
**请求**:
- **方法**: PUT
- **URL**: `/api/auth/profile`
- **Headers**: `Authorization: Bearer {token}`, `Content-Type: application/json`

**请求体**:
```json
{
    "bio": "这是一个测试用户",
    "avatar": "avatar_url"
}
```

**预期响应**:
```json
{
    "code": 200,
    "message": "更新成功",
    "data": {
        "id": 1,
        "username": "testuser",
        "email": "test@example.com",
        "avatar": "avatar_url",
        "bio": "这是一个测试用户",
        "createdAt": "2024-01-01T00:00:00"
    }
}
```

### 1.5 用户登出
**请求**:
- **方法**: POST
- **URL**: `/api/auth/logout`
- **Headers**: `Authorization: Bearer {token}`

**预期响应**:
```json
{
    "code": 200,
    "message": "登出成功",
    "data": null
}
```

## 2. 歌曲接口测试

### 2.1 获取所有歌曲
**请求**:
- **方法**: GET
- **URL**: `/api/songs`

**预期响应**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": [
        {
            "id": 1,
            "title": "测试歌曲1",
            "artist": "测试艺术家",
            "album": "测试专辑",
            "duration": 180,
            "url": "song_url_1.mp3",
            "cover": "cover_url_1.jpg",
            "playCount": 100
        }
    ]
}
```

### 2.2 根据ID获取歌曲
**请求**:
- **方法**: GET
- **URL**: `/api/songs/1`

**预期响应**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "id": 1,
        "title": "测试歌曲1",
        "artist": "测试艺术家",
        "album": "测试专辑",
        "duration": 180,
        "url": "song_url_1.mp3",
        "cover": "cover_url_1.jpg",
        "playCount": 100
    }
}
```

### 2.3 搜索歌曲
**请求**:
- **方法**: GET
- **URL**: `/api/songs/search?keyword=测试`

**预期响应**:
```json
{
    "code": 200,
    "message": "搜索成功",
    "data": [
        {
            "id": 1,
            "title": "测试歌曲1",
            "artist": "测试艺术家",
            "album": "测试专辑",
            "duration": 180,
            "url": "song_url_1.mp3",
            "cover": "cover_url_1.jpg",
            "playCount": 100
        }
    ]
}
```

### 2.4 获取热门歌曲
**请求**:
- **方法**: GET
- **URL**: `/api/songs/popular`

**预期响应**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": [
        {
            "id": 1,
            "title": "热门歌曲1",
            "artist": "热门艺术家",
            "album": "热门专辑",
            "duration": 200,
            "url": "popular_song_1.mp3",
            "cover": "popular_cover_1.jpg",
            "playCount": 1000
        }
    ]
}
```

### 2.5 根据心情获取歌曲
**请求**:
- **方法**: GET
- **URL**: `/api/songs/by-mood/happy?limit=10`

**预期响应**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": [
        {
            "id": 1,
            "title": "快乐歌曲1",
            "artist": "快乐艺术家",
            "album": "快乐专辑",
            "duration": 180,
            "url": "happy_song_1.mp3",
            "cover": "happy_cover_1.jpg",
            "playCount": 500
        }
    ]
}
```

### 2.6 记录歌曲播放
**请求**:
- **方法**: POST
- **URL**: `/api/songs/1/play`

**预期响应**:
```json
{
    "code": 200,
    "message": "播放记录成功",
    "data": null
}
```

## 3. 歌单接口测试

### 3.1 获取所有歌单
**请求**:
- **方法**: GET
- **URL**: `/api/playlists?page=0&size=20`

**预期响应**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "content": [
            {
                "id": 1,
                "name": "测试歌单",
                "description": "这是一个测试歌单",
                "cover": "playlist_cover.jpg",
                "songCount": 10,
                "likeCount": 50,
                "isPublic": true
            }
        ],
        "totalElements": 1,
        "totalPages": 1
    }
}
```

### 3.2 获取我的歌单
**请求**:
- **方法**: GET
- **URL**: `/api/playlists/my`
- **Headers**: `Authorization: Bearer {token}`

**预期响应**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": []
}
```

### 3.3 创建歌单
**请求**:
- **方法**: POST
- **URL**: `/api/playlists`
- **Headers**: `Authorization: Bearer {token}`, `Content-Type: application/json`

**请求体**:
```json
{
    "name": "我的新歌单",
    "description": "这是我创建的新歌单",
    "isPublic": true
}
```

**预期响应**:
```json
{
    "code": 200,
    "message": "创建成功",
    "data": null
}
```

## 4. 社交接口测试

### 4.1 关注用户
**请求**:
- **方法**: POST
- **URL**: `/api/social/follow/2`
- **Headers**: `Authorization: Bearer {token}`

**预期响应**:
```json
{
    "code": 200,
    "message": "关注成功",
    "data": null
}
```

### 4.2 取消关注
**请求**:
- **方法**: POST
- **URL**: `/api/social/unfollow/2`
- **Headers**: `Authorization: Bearer {token}`

**预期响应**:
```json
{
    "code": 200,
    "message": "取消关注成功",
    "data": null
}
```

### 4.3 获取粉丝列表
**请求**:
- **方法**: GET
- **URL**: `/api/social/followers`
- **Headers**: `Authorization: Bearer {token}`

**预期响应**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": [
        {
            "id": 2,
            "username": "follower1",
            "email": "follower1@example.com",
            "avatar": null,
            "bio": null,
            "createdAt": "2024-01-01T00:00:00"
        }
    ]
}
```

### 4.4 点赞内容
**请求**:
- **方法**: POST
- **URL**: `/api/social/song/1/like`
- **Headers**: `Authorization: Bearer {token}`

**预期响应**:
```json
{
    "code": 200,
    "message": "点赞成功",
    "data": null
}
```

### 4.5 添加评论
**请求**:
- **方法**: POST
- **URL**: `/api/social/song/1/comment`
- **Headers**: `Authorization: Bearer {token}`, `Content-Type: application/json`

**请求体**:
```json
"这是一条测试评论"
```

**预期响应**:
```json
{
    "code": 200,
    "message": "评论成功",
    "data": {
        "id": 1,
        "content": "这是一条测试评论",
        "userId": 1,
        "targetType": "song",
        "targetId": 1,
        "createdAt": "2024-01-01T00:00:00"
    }
}
```

## 5. AI接口测试

### 5.1 AI聊天
**请求**:
- **方法**: POST
- **URL**: `/api/ai/chat`
- **Headers**: `Authorization: Bearer {token}`, `Content-Type: application/json`

**请求体**:
```json
{
    "message": "推荐一些适合学习的音乐",
    "context": "学习"
}
```

**预期响应**:
```json
{
    "code": 200,
    "message": "AI回复成功",
    "data": {
        "response": "为您推荐以下适合学习的音乐...",
        "recommendations": [
            {
                "id": 1,
                "title": "学习音乐1",
                "artist": "学习艺术家",
                "album": "学习专辑",
                "duration": 180,
                "url": "study_song_1.mp3",
                "cover": "study_cover_1.jpg",
                "playCount": 200
            }
        ]
    }
}
```

### 5.2 情绪分析
**请求**:
- **方法**: POST
- **URL**: `/api/ai/emotion/analyze`
- **Headers**: `Content-Type: application/json`

**请求体**:
```json
"今天天气真好，心情很愉快"
```

**预期响应**:
```json
{
    "code": 200,
    "message": "情绪分析成功",
    "data": {
        "id": 1,
        "text": "今天天气真好，心情很愉快",
        "emotion": "happy",
        "confidence": 0.85,
        "keywords": ["天气", "好", "心情", "愉快"],
        "createdAt": "2024-01-01T00:00:00"
    }
}
```

### 5.3 获取个性化推荐
**请求**:
- **方法**: GET
- **URL**: `/api/ai/personalized/recommendations?limit=15`
- **Headers**: `Authorization: Bearer {token}`

**预期响应**:
```json
{
    "code": 200,
    "message": "个性化推荐成功",
    "data": [
        {
            "id": 1,
            "title": "个性化推荐歌曲1",
            "artist": "推荐艺术家",
            "album": "推荐专辑",
            "duration": 180,
            "url": "personal_song_1.mp3",
            "cover": "personal_cover_1.jpg",
            "playCount": 150
        }
    ]
}
```

## 6. 文件上传下载测试

### 6.1 上传音频文件
**请求**:
- **方法**: POST
- **URL**: `/api/files/audio`
- **Headers**: `Authorization: Bearer {token}`
- **Body**: Form-data
  - Key: `file`
  - Value: 选择音频文件 (mp3, wav, flac, aac)

**预期响应**:
```json
{
    "code": 200,
    "message": "音频文件上传成功",
    "data": "2024/01/01/20240101120000_abc123.mp3"
}
```

### 6.2 上传图片文件
**请求**:
- **方法**: POST
- **URL**: `/api/files/image`
- **Headers**: `Authorization: Bearer {token}`
- **Body**: Form-data
  - Key: `file`
  - Value: 选择图片文件 (jpg, jpeg, png, gif, webp)

**预期响应**:
```json
{
    "code": 200,
    "message": "图片文件上传成功",
    "data": "2024/01/01/20240101120000_def456.jpg"
}
```

### 6.3 下载文件
**请求**:
- **方法**: GET
- **URL**: `/api/files/download/2024/01/01/20240101120000_abc123.mp3`

**预期响应**: 文件下载流

### 6.4 预览文件
**请求**:
- **方法**: GET
- **URL**: `/api/files/preview/2024/01/01/20240101120000_def456.jpg`

**预期响应**: 图片预览

### 6.5 获取文件信息
**请求**:
- **方法**: GET
- **URL**: `/api/files/info/2024/01/01/20240101120000_abc123.mp3`

**预期响应**:
```json
{
    "code": 200,
    "message": "文件信息获取成功",
    "data": {
        "name": "20240101120000_abc123.mp3",
        "path": "2024/01/01/20240101120000_abc123.mp3",
        "size": 5242880,
        "sizeDescription": "5.0 MB",
        "contentType": "audio/mpeg",
        "lastModified": 1704096000000
    }
}
```

## 7. JWT令牌验证测试

### 7.1 令牌生成验证
- 注册/登录后检查返回的token格式
- 验证token包含必要字段：username, userId, email
- 验证token有效期（24小时）

### 7.2 令牌验证测试
1. **有效令牌测试**: 使用有效token访问需要认证的接口
2. **无效令牌测试**: 使用无效token访问接口，应返回401错误
3. **过期令牌测试**: 使用过期token访问接口，应返回401错误
4. **无令牌测试**: 不提供token访问需要认证的接口，应返回401错误

### 7.3 令牌刷新测试（如果实现）
- 测试令牌即将过期时的自动刷新机制

## 测试脚本

### Postman环境变量设置
```javascript
// 在Tests标签中设置环境变量
pm.environment.set("baseUrl", "http://localhost:8080/api");

// 登录后保存token
if (pm.response.code === 200) {
    const responseData = pm.response.json();
    pm.environment.set("authToken", responseData.data.token);
}
```

### 预请求脚本示例
```javascript
// 设置请求头
pm.request.headers.add({
    key: 'Authorization',
    value: 'Bearer ' + pm.environment.get('authToken')
});
```

### 测试断言示例
```javascript
// 检查响应状态码
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

// 检查响应结构
pm.test("Response has correct structure", function () {
    const response = pm.response.json();
    pm.expect(response).to.have.property('code');
    pm.expect(response).to.have.property('message');
    pm.expect(response).to.have.property('data');
});

// 检查业务逻辑
pm.test("Login returns valid token", function () {
    const response = pm.response.json();
    pm.expect(response.data.token).to.be.a('string');
    pm.expect(response.data.token.length).to.be.greaterThan(0);
});
```

## 测试执行顺序

建议按以下顺序执行测试：

1. **认证相关测试**
   - 注册用户
   - 用户登录
   - 获取用户信息
   - 更新用户信息
   - 用户登出

2. **文件上传测试**
   - 上传音频文件
   - 上传图片文件
   - 文件信息查询

3. **歌曲相关测试**
   - 获取所有歌曲
   - 搜索歌曲
   - 根据心情获取歌曲

4. **社交功能测试**
   - 关注/取消关注
   - 点赞/评论
   - 获取社交动态

5. **AI功能测试**
   - AI聊天
   - 情绪分析
   - 个性化推荐

6. **综合测试**
   - 令牌验证测试
   - 错误处理测试
   - 性能测试

## 注意事项

1. **测试数据清理**: 测试完成后清理测试数据
2. **并发测试**: 注意接口的并发安全性
3. **文件大小限制**: 测试文件上传的大小限制
4. **错误处理**: 验证各种错误情况的处理
5. **安全性**: 测试JWT令牌的安全性

通过以上测试用例，可以全面验证MusicFlow系统的所有功能模块。