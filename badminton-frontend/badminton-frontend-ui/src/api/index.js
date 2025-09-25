import axios from 'axios';

// 创建一个 Axios 实例，并配置基础URL
const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api', // 您的后端API基础地址
  timeout: 10000, // 请求超时时间
});

// 定义登录接口
export const login = (username, password) => {
  return apiClient.post('/user/login', {
    username: username,
    password: password
  });
};

// 您可以在此文件中继续导出其他所有API请求
// export const getCourts = () => apiClient.get('/courts');
