<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
// API imports
import {
  getAllReservations,
  createReservation,
  updateReservation,
  cancelReservation
} from '@/api/admin'
// Icon imports
import { Plus, Edit, CircleClose, Search, Refresh } from '@element-plus/icons-vue'

const reservationList = ref([])
const loading = ref(true)
const dialogVisible = ref(false)
const isEdit = ref(false)
const currentReservation = ref({})
const searchParams = ref({
  userId: null,
  status: null
})

// 获取所有预约
const fetchAllReservations = async () => {
  try {
    loading.value = true
    const params = {
      userId: searchParams.value.userId || null,
      status: searchParams.value.status,
    };
    const response = await getAllReservations(params)
    reservationList.value = response.data
  } catch (error) {
    ElMessage.error('获取预约列表失败！')
  } finally {
    loading.value = false
  }
}

onMounted(fetchAllReservations)

const handleSearch = () => {
  fetchAllReservations()
}

const handleReset = () => {
  searchParams.value.userId = null
  searchParams.value.status = null
  fetchAllReservations()
}

// 打开新增弹窗
const handleAdd = () => {
  isEdit.value = false
  currentReservation.value = {
    userId: null,
    courtId: null,
    reservationDate: '',
    startTime: '',
    endTime: '',
    status: 0
  }
  dialogVisible.value = true
}

// 打开编辑弹窗
const handleEdit = (reservation) => {
  isEdit.value = true
  currentReservation.value = { ...reservation }
  dialogVisible.value = true
}

// 取消预约
const handleCancel = (id) => {
  ElMessageBox.confirm('确定要取消该用户的这条预约吗？', '确认取消', {
    confirmButtonText: '确定',
    cancelButtonText: '点错了',
    type: 'warning'
  }).then(async () => {
    try {
      await cancelReservation(id)
      ElMessage.success('预约已取消')
      fetchAllReservations()
    } catch (error) {
      ElMessage.error(error.response?.data || '操作失败！')
    }
  })
}

// 提交表单
const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await updateReservation(currentReservation.value.id, currentReservation.value)
      ElMessage.success('预约信息更新成功！')
    } else {
      await createReservation(currentReservation.value)
      ElMessage.success('新预约添加成功！')
    }
    dialogVisible.value = false
    fetchAllReservations()
  } catch (error) {
    ElMessage.error(error.response?.data || '操作失败！')
  }
}

// 状态Tag帮助函数
const getStatusTagType = (status) => {
  if (status === 0) return 'primary'
  if (status === 1) return 'success'
  return 'info'
}
</script>

<template>
  <div class="page-container">
    <div class="header-bar">
      <h2>预约总览</h2>
      <div class="search-area">
        <el-input-number
          v-model="searchParams.userId"
          placeholder="按用户ID搜索"
          :controls="false"
          style="width: 150px; margin-right: 10px;"
        />
        <el-select
          v-model="searchParams.status"
          placeholder="按状态筛选"
          clearable
          style="width: 150px; margin-right: 10px;"
        >
          <el-option label="已预约" :value="0" />
          <el-option label="已完成" :value="1" />
          <el-option label="已取消" :value="2" />
        </el-select>
        <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
        <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        <el-button type="primary" :icon="Plus" @click="handleAdd" class="add-button">新增预约</el-button>
      </div>
    </div>

    <div v-loading="loading" class="reservation-list-container">
      <div v-for="reservation in reservationList" :key="reservation.id" class="reservation-card">
        <div class="card-main-info">
          <div class="info-group">
            <span class="label">预约ID:</span>
            <span class="value strong">{{ reservation.id }}</span>
          </div>
          <div class="info-group">
            <span class="label">用户ID:</span>
            <span class="value">{{ reservation.userId }}</span>
          </div>
          <div class="info-group">
            <span class="label">场地ID:</span>
            <span class="value">{{ reservation.courtId }}</span>
          </div>
        </div>
        <div class="card-time-info">
          <div class="info-group">
            <span class="label">日期:</span>
            <span class="value">{{ reservation.reservationDate }}</span>
          </div>
          <div class="info-group">
            <span class="label">时间:</span>
            <span class="value">{{ reservation.startTime }} - {{ reservation.endTime }}</span>
          </div>
        </div>
        <div class="card-status-action">
          <el-tag :type="getStatusTagType(reservation.status)" size="large">
            {{ reservation.status === 0 ? '已预约' : reservation.status === 1 ? '已完成' : '已取消' }}
          </el-tag>
          <div class="total-cost">
            <span class="label">费用:</span>
            <span class="value">¥ {{ reservation.totalCost || '0.00' }}</span>
          </div>
          <div>
            <el-button :icon="Edit" circle @click="handleEdit(reservation)" />
            <el-button :icon="CircleClose" circle type="danger" @click="handleCancel(reservation.id)" :disabled="reservation.status === 2"/>
          </div>
        </div>
      </div>
      <el-empty v-if="!loading && reservationList.length === 0" description="没有找到符合条件的预约记录" />
    </div>

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑预约' : '新增预约'"
      width="600px"
      class="neumorphism-dialog"
    >
      <el-form :model="currentReservation" label-position="top">
        <el-form-item label="用户ID">
          <el-input-number v-model="currentReservation.userId" :min="1" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="场地ID">
          <el-input-number v-model="currentReservation.courtId" :min="1" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="预约日期">
          <el-date-picker
            v-model="currentReservation.reservationDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-time-select
            v-model="currentReservation.startTime"
            start="08:00"
            step="01:00"
            end="21:00"
            placeholder="选择开始时间"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-time-select
            v-model="currentReservation.endTime"
            start="09:00"
            step="01:00"
            end="22:00"
            placeholder="选择结束时间"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="currentReservation.status" placeholder="请选择状态" style="width: 100%;">
            <el-option label="已预约" :value="0" />
            <el-option label="已完成" :value="1" />
            <el-option label="已取消" :value="2" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确认</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.page-container {
  padding: 10px;
}
.header-bar {
  margin-bottom: 25px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.add-button {
  margin-left: 10px; /* 给新增按钮一点左边距 */
  box-shadow: 0 2px 12px 0 rgba(64, 158, 255, 0.5);
}
.reservation-list-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
}
.reservation-card {
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
.reservation-card:hover {
  box-shadow: 0 4px 16px rgba(0,0,0,0.1);
}
.card-main-info, .card-time-info, .card-status-action {
  display: flex;
  align-items: center;
  gap: 30px;
}
.card-status-action {
  justify-content: flex-end;
  min-width: 300px;
  gap: 20px;
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
  color: #67C23A;
  font-weight: bold;
}
</style>
