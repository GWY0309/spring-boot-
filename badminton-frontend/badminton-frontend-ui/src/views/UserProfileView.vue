<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getProfile, updateProfile, changePassword } from '@/api'
import { User, Key, EditPen } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user' // 1. 导入 userStore
import { useRouter } from 'vue-router'     // 2. 导入 useRouter

const userStore = useUserStore() // 3. 初始化 userStore
const router = useRouter()       // 4. 初始化 router

const profileForm = ref({
  username: '',
  nickname: '',
  phone: ''
})

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const activeSegment = ref('profile')

onMounted(async () => {
  try {
    const res = await getProfile()
    profileForm.value = res.data
  } catch (error) {
    ElMessage.error('获取个人信息失败！')
  }
})

const handleUpdateProfile = async () => {
  try {
    const params = {
      nickname: profileForm.value.nickname,
      phone: profileForm.value.phone
    }
    await updateProfile(params)
    ElMessage.success('信息更新成功！')
  } catch (error) {
    ElMessage.error('信息更新失败！')
  }
}

// 5. 这是修改后的密码处理函数
const handleChangePassword = async () => {
  if (!passwordForm.value.oldPassword || !passwordForm.value.newPassword) {
    ElMessage.error('旧密码和新密码不能为空！');
    return;
  }
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    ElMessage.error('新密码两次输入不一致！')
    return
  }
  try {
    await changePassword(passwordForm.value.oldPassword, passwordForm.value.newPassword)

    // 清除本地 Token 和用户信息
    userStore.clearToken()

    // 弹出提示
    ElMessage.success('账号信息已发生改变，请重新登录！')

    // 跳转到登录页
    router.push('/')

  } catch (error) {
    ElMessage.error(error.response?.data || '密码修改失败！')
  }
}
</script>

<template>
  <div class="profile-container">
    <div class="profile-card">
      <div class="card-header">
        <el-avatar :size="60" :icon="User"/>
        <h2 class="username">{{ profileForm.username }}</h2>
        <p class="nickname">{{ profileForm.nickname }}</p>
      </div>

      <div class="segmented-control">
        <el-radio-group v-model="activeSegment">
          <el-radio-button :label="'profile'" :value="'profile'">
            <el-icon class="icon">
              <EditPen/>
            </el-icon>
            个人信息
          </el-radio-button>
          <el-radio-button :label="'password'" :value="'password'">
            <el-icon class="icon">
              <Key/>
            </el-icon>
            修改密码
          </el-radio-button>
        </el-radio-group>
      </div>

      <div class="form-content">
        <div v-if="activeSegment === 'profile'" class="form-wrapper">
          <el-form :model="profileForm" label-width="80px" label-position="left">
            <el-form-item label="昵称">
              <el-input v-model="profileForm.nickname"/>
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="profileForm.phone"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleUpdateProfile">保存修改</el-button>
            </el-form-item>
          </el-form>
        </div>

        <div v-if="activeSegment === 'password'" class="form-wrapper">
          <el-form :model="passwordForm" label-width="100px" label-position="left">
            <el-form-item label="当前密码">
              <el-input v-model="passwordForm.oldPassword" type="password" show-password/>
            </el-form-item>
            <el-form-item label="新密码">
              <el-input v-model="passwordForm.newPassword" type="password" show-password/>
            </el-form-item>
            <el-form-item label="确认新密码">
              <el-input v-model="passwordForm.confirmPassword" type="password" show-password/>
            </el-form-item>
            <el-form-item>
              <el-button type="danger" @click="handleChangePassword">确认修改</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.profile-container {
  padding: 20px;
  display: flex;
  justify-content: center;
}

.profile-card {
  width: 100%;
  max-width: 700px;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border: 1px solid #e5e5e5;
  padding: 30px;
}

.card-header {
  text-align: center;
  margin-bottom: 30px;
}

.username {
  font-size: 22px;
  font-weight: 600;
  margin-top: 15px;
  margin-bottom: 5px;
  color: #333;
}

.nickname {
  font-size: 16px;
  color: #888;
  margin: 0;
}

.segmented-control {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
}

.segmented-control .el-radio-button :deep(.el-radio-button__inner) {
  display: flex;
  align-items: center;
  gap: 8px; /* 图标和文字的间距 */
  font-size: 14px;
}

.form-content {
  padding: 0 20px;
}
</style>
