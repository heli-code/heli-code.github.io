const axios = require('axios');

// 测试后端连接
async function testBackendConnection() {
  console.log('🔍 测试后端连接...');
  
  try {
    // 测试后端健康检查
    const response = await axios.get('http://localhost:8080/api/health', {
      timeout: 5000
    });
    
    console.log('✅ 后端连接成功');
    console.log('响应状态:', response.status);
    console.log('响应数据:', response.data);
    
    return true;
  } catch (error) {
    if (error.code === 'ECONNREFUSED') {
      console.log('❌ 后端服务未启动，请先启动后端服务');
      console.log('启动命令: cd backend && mvn spring-boot:run');
    } else if (error.response) {
      console.log('✅ 后端服务已启动，但健康检查接口不存在');
      console.log('状态码:', error.response.status);
    } else {
      console.log('❌ 连接错误:', error.message);
    }
    
    return false;
  }
}

// 测试前端代理
async function testFrontendProxy() {
  console.log('\n🔍 测试前端代理配置...');
  
  try {
    // 测试前端代理到后端
    const response = await axios.get('http://localhost:5173/api/auth', {
      timeout: 5000
    });
    
    console.log('✅ 前端代理配置正确');
    console.log('代理响应状态:', response.status);
    
    return true;
  } catch (error) {
    if (error.code === 'ECONNREFUSED') {
      console.log('❌ 前端开发服务器未启动，请先启动前端服务');
      console.log('启动命令: npm run dev');
    } else if (error.response) {
      console.log('✅ 前端代理已配置，但后端接口可能不存在');
      console.log('代理响应状态:', error.response.status);
    } else {
      console.log('❌ 代理测试错误:', error.message);
    }
    
    return false;
  }
}

// 测试用户注册流程
async function testUserRegistration() {
  console.log('\n🔍 测试用户注册流程...');
  
  const testUser = {
    username: `testuser_${Date.now()}`,
    email: `test_${Date.now()}@example.com`,
    password: 'Test123456'
  };
  
  try {
    const response = await axios.post('http://localhost:5173/api/auth/register', testUser, {
      timeout: 10000,
      headers: {
        'Content-Type': 'application/json'
      }
    });
    
    console.log('✅ 用户注册成功');
    console.log('响应状态:', response.status);
    console.log('响应数据:', JSON.stringify(response.data, null, 2));
    
    return true;
  } catch (error) {
    if (error.response) {
      console.log('注册请求已发送，但后端可能返回错误');
      console.log('状态码:', error.response.status);
      console.log('错误信息:', error.response.data?.message || error.message);
    } else {
      console.log('❌ 注册测试错误:', error.message);
    }
    
    return false;
  }
}

// 主测试函数
async function runTests() {
  console.log('🚀 开始前后端连接测试\n');
  
  const backendOk = await testBackendConnection();
  const proxyOk = await testFrontendProxy();
  
  if (backendOk && proxyOk) {
    console.log('\n✅ 基础连接测试通过，开始功能测试...');
    await testUserRegistration();
  } else {
    console.log('\n❌ 基础连接测试失败，请先解决连接问题');
  }
  
  console.log('\n📋 测试完成');
  console.log('\n💡 下一步操作建议:');
  console.log('1. 确保后端服务运行: cd backend && mvn spring-boot:run');
  console.log('2. 确保前端服务运行: npm run dev');
  console.log('3. 访问 http://localhost:5173/auth-test 进行完整测试');
}

// 运行测试
runTests().catch(console.error);