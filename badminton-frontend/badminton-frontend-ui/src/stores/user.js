import { defineStore } from 'pinia'
import { ref, computed } from 'vue' // 1. 导入 computed
import { jwtDecode } from 'jwt-decode' // 2. 导入解码库

export const useUserStore = defineStore('user', () => {
  // State
  const token = ref(null)
  const userInfo = ref({
    id: null,
    username: '',
    role: ''
  })

  // Getters (类似于 computed 属性)
  const isAdmin = computed(() => userInfo.value.role === 'ADMIN')

  // Actions
  const setToken = (newToken) => {
    token.value = newToken
    if (newToken) {
      // 如果有新Token，解码并存储用户信息
      const decoded = jwtDecode(newToken)
      userInfo.value = {
        id: decoded.id,
        username: decoded.sub, // 'sub' 是 JWT 的标准字段，代表用户名
        role: decoded.role
      }
    } else {
      // 如果 token 被清空，也清空用户信息
      clearToken()
    }
  }

  const clearToken = () => {
    token.value = null
    userInfo.value = {
      id: null,
      username: '',
      role: ''
    }
  }

  return { token, userInfo, isAdmin, setToken, clearToken }
})
