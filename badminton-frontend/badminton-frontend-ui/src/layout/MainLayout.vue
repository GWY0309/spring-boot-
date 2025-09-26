<script setup>
import { RouterView, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import {
  HomeFilled,
  Place,
  Tickets,
  GobletSquare,
  Setting,
} from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore() // 2. 在顶层获取 store 实例

const handleLogout = () => {
  userStore.clearToken()
  ElMessage.success('您已成功退出登录')
  router.push('/')
}
</script>

<template>
  <el-container class="main-layout">
    <el-header class="header">
      <div class="logo-title">羽毛球场地预约系统</div>
      <div class="user-info">
        <span>欢迎您，{{ userStore.userInfo.username }}</span>
        <el-button type="danger" plain @click="handleLogout">退出登录</el-button>
      </div>
    </el-header>

    <el-container>
      <el-aside width="200px" class="aside">
        <el-menu
          router
          :default-active="$route.path"
          class="el-menu-vertical"
        >
          <el-menu-item index="/home/dashboard">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/home/courts">
            <el-icon><Place /></el-icon>
            <span>场地列表</span>
          </el-menu-item>
          <el-menu-item index="/home/my-reservations">
            <el-icon><Tickets /></el-icon>
            <span>我的预约</span>
          </el-menu-item>

          <el-sub-menu index="racket-menu">
            <template #title>
              <el-icon><GobletSquare /></el-icon>
              <span>租球拍</span>
            </template>
            <el-menu-item index="/home/rackets">球拍租赁</el-menu-item>
            <el-menu-item index="/home/my-rentals">我的租借</el-menu-item>
          </el-sub-menu>

          <template v-if="userStore.isAdmin">
            <el-sub-menu index="admin-menu">
              <template #title>
                <el-icon><Setting /></el-icon>
                <span>后台管理</span>
              </template>
              <el-menu-item index="/home/admin/courts">场地管理</el-menu-item>
              <el-menu-item index="/home/admin/rackets">球拍管理</el-menu-item>
              <el-menu-item index="/home/admin/rentals">租借总览</el-menu-item>
              <el-menu-item index="/home/admin/reservations">预约总览</el-menu-item>
              <el-menu-item index="/home/admin/users">用户管理</el-menu-item>
            </el-sub-menu>
          </template>
        </el-menu>
      </el-aside>
      <el-main class="main-content">
        <RouterView />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
/* style 部分保持不变 */
.main-layout {
  height: 100vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff;
  border-bottom: 1px solid #dcdfe6;
}

.logo-title {
  font-size: 20px;
  font-weight: bold;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-info span {
  margin-right: 15px;
}

.aside {
  border-right: 1px solid #dcdfe6;
}

.el-menu-vertical {
  height: 100%; /* 让菜单高度占满侧边栏 */
}

.main-content {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
