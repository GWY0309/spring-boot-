import { defineStore } from 'pinia';
import { ref } from 'vue';

// 定义一个名为 'user' 的 store
export const useUserStore = defineStore('user', () => {
  // 使用 ref 定义响应式的 state
  const token = ref(null);

  // 定义一个 action 来设置 token
  const setToken = (newToken) => {
    token.value = newToken;
  };

  // 定义一个 action 来清除 token (用于退出登录)
  const clearToken = () => {
    token.value = null;
  };

  // 将 state 和 actions return 出去，以便组件中使用
  return { token, setToken, clearToken };
});
