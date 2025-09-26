<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
// API imports
import { getAllUsers, updateUser, deleteUser } from '@/api/admin'
// Pinia Store for getting current admin's ID
import { useUserStore } from '@/stores/user'
// Icon imports
import { User, Phone, Postcard, Edit, Delete } from '@element-plus/icons-vue'

const userList = ref([])
const loading = ref(true)
const dialogVisible = ref(false)
const currentUser = ref({})
const userStore = useUserStore() // 获取 store 实例

// 获取用户列表
const fetchUsers = async () => {
  try {
    loading.value = true
    const response = await getAllUsers()
    userList.value = response.data
  } catch (error) {
    ElMessage.error('获取用户列表失败！')
  } finally {
    loading.value = false
  }
}

onMounted(fetchUsers)

// 打开编辑弹窗
const handleEdit = (user) => {
  currentUser.value = { ...user } // 使用深拷贝，避免直接修改列表数据
  dialogVisible.value = true
}

// 删除用户
const handleDelete = (user) => {
  ElMessageBox.confirm(`确定要删除用户 "${user.username}" 吗？此操作不可恢复。`, '警告', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteUser(user.id)
      ElMessage.success('用户删除成功！')
      fetchUsers() // 刷新列表
    } catch (error) {
      ElMessage.error(error.response?.data || '删除失败！')
    }
  })
}

// 提交编辑
const handleSubmit = async () => {
  try {
    const dataToUpdate = {
      nickname: currentUser.value.nickname,
      phone: currentUser.value.phone,
      role: currentUser.value.role
    }
    await updateUser(currentUser.value.id, dataToUpdate)
    ElMessage.success('用户信息更新成功！')
    dialogVisible.value = false
    fetchUsers()
  } catch (error) {
    ElMessage.error(error.response?.data || '更新失败！')
  }
}
</script>

<template>
  <div class="page-container">
    <div class="header-bar">
      <h2>用户管理</h2>
    </div>

    <div v-loading="loading" class="user-list-container">
      <div v-for="user in userList" :key="user.id" class="user-card">
        <div class="card-header">
          <el-avatar :size="50" :icon="User" />
          <div class="user-identity">
            <h3 class="username">{{ user.username }}</h3>
            <p class="nickname">{{ user.nickname }}</p>
          </div>
          <div class="actions">
            <el-button :icon="Edit" circle @click="handleEdit(user)" />
            <el-button
              :icon="Delete"
              circle
              type="danger"
              @click="handleDelete(user)"
              :disabled="user.role === 'ADMIN' || user.id === userStore.userInfo.id"
            />
          </div>
        </div>
        <div class="card-body">
          <div class="info-item">
            <el-icon><Postcard /></el-icon>
            <span>ID: {{ user.id }}</span>
          </div>
          <div class="info-item">
            <el-icon><Phone /></el-icon>
            <span>手机: {{ user.phone || '未设置' }}</span>
          </div>
        </div>
        <div class="card-footer">
          <el-tag :type="user.role === 'ADMIN' ? 'success' : 'info'" effect="dark">
            {{ user.role }}
          </el-tag>
          <span class="create-time">注册于: {{ new Date(user.createTime).toLocaleDateString() }}</span>
        </div>
      </div>
      <el-empty v-if="!loading && userList.length === 0" description="暂无用户记录" />
    </div>

    <el-dialog
      v-model="dialogVisible"
      title="编辑用户信息"
      width="600px"
      class="neumorphism-dialog"
    >
      <el-form :model="currentUser" label-position="top">
        <el-form-item label="用户名">
          <el-input v-model="currentUser.username" disabled />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="currentUser.nickname" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="currentUser.phone" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="currentUser.role" placeholder="选择角色" style="width: 100%;">
            <el-option label="普通用户" value="USER" />
            <el-option label="管理员" value="ADMIN" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">保存更改</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
/* 样式与之前版本类似，增加了 actions 区域 */
.page-container {
  padding: 10px;
}
.header-bar {
  margin-bottom: 25px;
}
.user-list-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); /* 稍微加宽 */
  gap: 20px;
}
.user-card {
  background-color: #fff;
  border-radius: 12px;
  border: 1px solid #eef0f3;
  transition: all 0.3s ease;
  overflow: hidden;
}
.user-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px 0 rgba(0, 0, 0, 0.08);
}
.card-header {
  display: flex;
  align-items: center;
  padding: 20px;
  gap: 15px;
  background-color: #fafcfe;
  border-bottom: 1px solid #eef0f3;
}
.user-identity {
  flex-grow: 1; /* 让身份信息占据更多空间 */
  display: flex;
  flex-direction: column;
}
.actions {
  display: flex;
  gap: 10px;
}
.username {
  font-size: 16px;
  font-weight: 600;
  margin: 0;
  color: #303133;
}
.nickname {
  font-size: 14px;
  color: #909399;
  margin: 0;
}
.card-body {
  padding: 20px;
  font-size: 14px;
  color: #606266;
}
.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
}
.info-item:last-child {
  margin-bottom: 0;
}
.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background-color: #fafcfe;
  border-top: 1px solid #eef0f3;
}
.create-time {
  font-size: 12px;
  color: #909399;
}
</style>
