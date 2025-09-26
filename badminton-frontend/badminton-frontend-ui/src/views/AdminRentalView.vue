<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllRentals, forceReturnRacket } from '@/api/admin'
import { RefreshLeft } from '@element-plus/icons-vue' // 导入图标

const rentalList = ref([])
const loading = ref(true)

const fetchRentals = async () => {
  try {
    loading.value = true
    const response = await getAllRentals()
    rentalList.value = response.data
  } catch (error) {
    ElMessage.error('获取租借列表失败！')
  } finally {
    loading.value = false
  }
}

onMounted(fetchRentals)

const handleForceReturn = (rental) => {
  ElMessageBox.confirm(
    `确定要为用户 ${rental.userId} 强制归还球拍 ${rental.racketId} 吗？`,
    '确认操作',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await forceReturnRacket(rental.id)
      ElMessage.success('操作成功！')
      fetchRentals() // 刷新列表
    } catch (error) {
      ElMessage.error(error.response?.data || '操作失败！')
    }
  })
}

// 格式化日期时间，如果不存在则显示 N/A
const formatDateTime = (dateTime) => {
  if (!dateTime) return 'N/A'
  return new Date(dateTime).toLocaleString()
}
</script>

<template>
  <div class="page-container">
    <div class="header-bar">
      <h2>租借总览</h2>
    </div>

    <div v-loading="loading" class="rental-list-container">
      <div v-for="rental in rentalList" :key="rental.id" class="rental-card">
        <div class="card-main-info">
          <div class="info-group">
            <span class="label">租借ID:</span>
            <span class="value strong">{{ rental.id }}</span>
          </div>
          <div class="info-group">
            <span class="label">用户ID:</span>
            <span class="value">{{ rental.userId }}</span>
          </div>
          <div class="info-group">
            <span class="label">球拍ID:</span>
            <span class="value">{{ rental.racketId }}</span>
          </div>
        </div>
        <div class="card-time-info">
          <div class="info-group">
            <span class="label">租借时间:</span>
            <span class="value">{{ formatDateTime(rental.rentalTime) }}</span>
          </div>
          <div class="info-group">
            <span class="label">归还时间:</span>
            <span class="value">{{ formatDateTime(rental.returnTime) }}</span>
          </div>
        </div>
        <div class="card-status-action">
          <el-tag :type="rental.status === 0 ? 'primary' : 'success'" size="large">
            {{ rental.status === 0 ? '租借中' : '已归还' }}
          </el-tag>
          <div class="total-cost">
            <span class="label">费用:</span>
            <span class="value">¥ {{ rental.totalCost || '0.00' }}</span>
          </div>
          <el-button
            type="warning"
            :icon="RefreshLeft"
            @click="handleForceReturn(rental)"
            :disabled="rental.status !== 0"
            round
          >
            强制归还
          </el-button>
        </div>
      </div>
      <el-empty v-if="!loading && rentalList.length === 0" description="暂无租借记录" />
    </div>
  </div>
</template>

<style scoped>
.page-container {
  padding: 10px;
}

.header-bar {
  margin-bottom: 25px;
}

.rental-list-container {
  display: flex;
  flex-direction: column;
  gap: 15px; /* 卡片之间的间距 */
}

.rental-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  border: 1px solid #eef0f3;
  transition: box-shadow 0.3s ease;
}

.rental-card:hover {
  box-shadow: 0 4px 16px rgba(0,0,0,0.1);
}

.card-main-info, .card-time-info, .card-status-action {
  display: flex;
  align-items: center;
  gap: 30px; /* 组内元素间距 */
}

.card-status-action {
  justify-content: flex-end;
  min-width: 300px; /* 保证右侧对齐 */
}

.info-group {
  display: flex;
  flex-direction: column;
}

.label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
}

.value {
  font-size: 14px;
  color: #303133;
}
.value.strong {
  font-weight: 600;
  font-size: 16px;
}

.total-cost .value {
  color: #E6A23C;
  font-weight: bold;
}
</style>
