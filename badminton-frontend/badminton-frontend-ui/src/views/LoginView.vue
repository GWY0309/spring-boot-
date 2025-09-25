<script setup>
import { ref } from 'vue'
import { User, Lock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { login } from '@/api'

const router = useRouter()
// const userStore = useUserStore() // <--- 删除这一行

const loginForm = ref({
  username: '',
  password: ''
})

const handleLogin = async () => {
  if (!loginForm.value.username || !loginForm.value.password) {
    ElMessage.error('用户名和密码不能为空！')
    return
  }
  try {
    const response = await login(loginForm.value.username, loginForm.value.password)
    const token = response.data.token

    const userStore = useUserStore() // <--- 在这里调用
    userStore.setToken(token)

    ElMessage.success('登录成功！')
    router.push('/home')
  } catch (error) {
    ElMessage.error(error.response?.data || '登录失败，请检查您的用户名和密码')
  }
}
</script>

<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <span>羽毛球场地预约管理系统</span>
        </div>
      </template>

      <el-form :model="loginForm" label-width="0px">
        <el-form-item>
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            :prefix-icon="User"
            clearable
          ></el-input>
        </el-form-item>

        <el-form-item>
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            :prefix-icon="Lock"
            show-password
          ></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" style="width: 100%;" @click="handleLogin">
            登 录
          </el-button>
        </el-form-item>
      </el-form>

      <div class="register-link">
        还没有账号？
        <el-link type="primary">去注册</el-link>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
/* style 部分保持不变 */
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;

  background-image: url("/login-bg.jpg");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

.login-card {
  width: 400px;
  border-radius: 20px; /* 增加圆角，更像iOS风格 */
  border: 1px solid rgba(255, 255, 255, 0.18); /* 添加一个非常微妙的白色边框 */

  /* --- 磨砂玻璃效果核心代码 --- */
  background-color: rgba(255, 255, 255, 0.25); /* 半透明的白色背景 */
  backdrop-filter: blur(10px); /* 对背景进行10像素的模糊处理 */
  -webkit-backdrop-filter: blur(10px); /* 兼容 Safari 浏览器 */
  box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.2); /* 添加一个更柔和的阴影 */
}

.card-header {
  text-align: center;
  font-size: 24px;
  font-weight: 600; /* 字体加粗，但比 bold 稍细，更优雅 */
  color: #333; /* 字体颜色加深，增加对比度 */
}

/* 针对 Element Plus 输入框的样式微调 */
.el-input {
  --el-input-bg-color: rgba(255, 255, 255, 0.5); /* 让输入框也带一点透明效果 */
  --el-input-border-radius: 10px; /* 输入框圆角 */
}

.register-link {
  text-align: center;
  margin-top: 10px;
}
</style>
