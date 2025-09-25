import axios from 'axios';
import { useUserStore } from '@/stores/user'; // 导入 Pinia store

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
});

// --- 请求拦截器 ---
// 在每次请求发送前，都检查 Pinia 中是否有 token
// 如果有，就自动将它添加到请求的 Authorization 头中
apiClient.interceptors.request.use(
  (config) => {
    const userStore = useUserStore();
    const token = userStore.token;
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);


// 定义登录接口
export const login = (username, password) => {
  return apiClient.post('/user/login', {
    username: username,
    password: password
  });
};

// 新增：定义获取所有场地的接口
export const getAllCourts = () => {
  return apiClient.get('/courts');
};

// 新增：定义创建预约的接口
export const createReservation = (reservationData) => {
  return apiClient.post('/reservations', reservationData);
};

// 新增：定义获取“我的预约”列表的接口
export const getMyReservations = () => {
  return apiClient.get('/reservations/my');
};

// 新增：定义取消预约的接口
export const cancelReservation = (reservationId) => {
  // 注意，我们的后端接口是 PUT /api/reservations/{id}/cancel
  return apiClient.put(`/reservations/${reservationId}/cancel`);
};

// 新增：定义获取“我的租借”列表的接口
export const getMyRentals = () => {
  return apiClient.get('/rentals/my');
};
