import axios from 'axios';
import { useUserStore } from '@/stores/user';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
});

apiClient.interceptors.request.use(
  (config) => {
    // 将 useUserStore 的调用放在拦截器函数内部，这是正确的
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

// 新增：定义注册接口
export const register = (userData) => {
  return apiClient.post('/user/register', userData)
}

// 定义获取所有场地的接口
export const getAllCourts = () => {
  return apiClient.get('/courts');
};

// 定义创建预约的接口
export const createReservation = (reservationData) => {
  return apiClient.post('/reservations', reservationData);
};

// 定义获取“我的预约”列表的接口
export const getMyReservations = () => {
  return apiClient.get('/reservations/my');
};

// 定义取消预约的接口
export const cancelReservation = (reservationId) => {
  return apiClient.put(`/reservations/${reservationId}/cancel`);
};

// 定义获取“我的租借”列表的接口
export const getMyRentals = () => {
  return apiClient.get('/rentals/my');
};

// 定义归还球拍的接口
export const returnRacket = (rentalId) => {
  return apiClient.put(`/rentals/${rentalId}/return`);
};

// --- 新增：补上缺失的函数 ---
export const getAvailableRackets = () => {
  return apiClient.get('/rackets');
};

export const rentRacket = (racketId) => {
  return apiClient.post(`/rackets/${racketId}/rent`);
};

// --- 个人中心 API ---
export const getProfile = () => {
  return apiClient.get('/user/profile');
};

export const updateProfile = (profileData) => {
  return apiClient.put('/user/profile', profileData);
};

export const changePassword = (oldPassword, newPassword) => {
  return apiClient.put('/user/change-password', { oldPassword, newPassword });
};
