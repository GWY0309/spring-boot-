<script setup>
import { ref } from 'vue'
import { User, Lock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus' // 导入 Element Plus 的消息提示组件
import { useRouter } from 'vue-router' // 导入 Vue Router 的 useRouter
import { useUserStore } from '@/stores/user' // 导入我们创建的 Pinia store
import { login } from '@/api' // 导入我们封装的登录 API

// 路由实例，用于页面跳转
const router = useRouter()
// Pinia store 实例
const userStore = useUserStore()

const loginForm = ref({
  username: '',
  password: ''
})

// 登录按钮的点击事件处理函数
const handleLogin = async () => {
  // 表单校验（简单示例）
  if (!loginForm.value.username || !loginForm.value.password) {
    ElMessage.error('用户名和密码不能为空！')
    return
  }

  try {
    // 调用我们封装的 API
    const response = await login(loginForm.value.username, loginForm.value.password)

    // 从响应中获取 token
    const token = response.data.token

    // 使用 Pinia store 来存储 token
    userStore.setToken(token)

    // 弹出成功提示
    ElMessage.success('登录成功！')

    // 跳转到主页 (我们暂时还没有主页，先跳转到一个不存在的 /home 路径)
    router.push('/home')

  } catch (error) {
    // 处理登录失败的情况
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
        还没有账号？<el-link type="primary">去注册</el-link>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
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
