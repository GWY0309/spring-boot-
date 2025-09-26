// src/api/admin.js

import axios from 'axios'
import { useUserStore } from '@/stores/user'

// 创建一个专门用于 admin API 的 axios 实例
const adminApiClient = axios.create({
  baseURL: 'http://localhost:8080/api/admin', // 注意基础 URL 指向 /api/admin
  timeout: 10000
})

// 为这个实例也添加请求拦截器，自动附加 Token
adminApiClient.interceptors.request.use(
  (config) => {
    const userStore = useUserStore()
    const token = userStore.token
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// --- 场地管理 API ---

// 获取所有场地列表 (GET /api/admin/courts -> 复用普通用户的查询)
// 我们复用之前的 getAllCourts 即可，这里不用重复写

// 新增一个场地 (POST /api/admin/courts)
export const addCourt = (courtData) => {
  return adminApiClient.post('/courts', courtData)
}

// 更新一个场地 (PUT /api/admin/courts/{id})
export const updateCourt = (id, courtData) => {
  return adminApiClient.put(`/courts/${id}`, courtData)
}

// 删除一个场地 (DELETE /api/admin/courts/{id})
export const deleteCourt = (id) => {
  return adminApiClient.delete(`/courts/${id}`)
}

// 新增一个球拍 (POST /api/admin/rackets)
export const addRacket = (racketData) => {
  return adminApiClient.post('/rackets', racketData)
}

// 更新一个球拍 (PUT /api/admin/rackets/{id})
export const updateRacket = (id, racketData) => {
  return adminApiClient.put(`/rackets/${id}`, racketData)
}

// 删除一个球拍 (DELETE /api/admin/rackets/{id})
export const deleteRacket = (id) => {
  return adminApiClient.delete(`/rackets/${id}`)
}

// --- 租借管理 API ---

// (管理员)获取所有租借记录
export const getAllRentals = () => {
  return adminApiClient.get('/rentals')
}

// (管理员)强制归还球拍
export const forceReturnRacket = (rentalId) => {
  return adminApiClient.put(`/rentals/${rentalId}/return`)
}

// ------------------

// (管理员)获取所有预约记录
export const getAllReservations = () => {
  return adminApiClient.get('/reservations')
}

export const createReservation = (reservationData) => {
  return adminApiClient.post('/reservations', reservationData)
}

export const updateReservation = (id, reservationData) => {
  return adminApiClient.put(`/reservations/${id}`, reservationData)
}

export const cancelReservation = (id) => {
  return adminApiClient.put(`/reservations/${id}/cancel`)
}

// --- 用户管理 API ---

// (管理员)获取所有用户列表
export const getAllUsers = () => {
  return adminApiClient.get('/users')
}

// --- 用户管理 CRUD API ---
export const updateUser = (id, userData) => {
  return adminApiClient.put(`/users/${id}`, userData);
};

export const deleteUser = (id) => {
  return adminApiClient.delete(`/users/${id}`);
};
