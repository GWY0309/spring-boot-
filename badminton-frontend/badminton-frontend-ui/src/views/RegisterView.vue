<script setup>
import { ref, onMounted } from 'vue' // 1. 在这里导入 onMounted
import { User, Lock, Edit } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { register } from '@/api'

const router = useRouter()

const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: ''
})

// 2. 添加 onMounted 钩子
onMounted(() => {
  // 每次进入页面时，都清空表单
  registerForm.value = {
    username: '',
    password: '',
    confirmPassword: '',
    nickname: ''
  }
})

const handleRegister = async () => {
  // ... handleRegister 函数内容保持不变
  if (!registerForm.value.username || !registerForm.value.password || !registerForm.value.nickname) {
    ElMessage.error('用户名、密码和昵称不能为空！')
    return
  }
  if (registerForm.value.password !== registerForm.value.confirmPassword) {
    ElMessage.error('两次输入的密码不一致！')
    return
  }

  try {
    const params = {
      username: registerForm.value.username,
      password: registerForm.value.password,
      nickname: registerForm.value.nickname
    }
    await register(params)
    ElMessage.success('恭喜您，注册成功！即将跳转到登录页。')
    setTimeout(() => {
      router.push('/')
    }, 1000)
  } catch (error) {
    ElMessage.error(error.response?.data || '注册失败，请稍后再试')
  }
}
</script>

<template>
  <div class="register-container">
    <el-card class="register-card">
      <template #header>
        <div class="card-header">
          <span>新用户注册</span>
        </div>
      </template>

      <el-form :model="registerForm" label-width="0px">
        <el-form-item>
          <el-input
            v-model="registerForm.username"
            placeholder="请输入用户名"
            :prefix-icon="User"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="registerForm.nickname"
            placeholder="请输入您的昵称"
            :prefix-icon="Edit"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码"
            :prefix-icon="Lock"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="请再次确认密码"
            :prefix-icon="Lock"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="handleRegister">
            立即注册
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-link">
        已有账号？<el-link type="primary" @click="router.push('/')">返回登录</el-link>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-image: url('/login-bg.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

.register-card {
  width: 400px;
  border-radius: 20px;
  background-color: rgba(255, 255, 255, 0.25);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.18);
}

.card-header {
  text-align: center;
  font-size: 24px;
  font-weight: 600;
  color: #333;
}

.el-input {
  --el-input-bg-color: rgba(255, 255, 255, 0.5);
  --el-input-border-radius: 10px;
}

.login-link {
  text-align: center;
  margin-top: 10px;
}
</style>
