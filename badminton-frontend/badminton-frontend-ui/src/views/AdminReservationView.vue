<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getAllReservations,
  createReservation,
  updateReservation,
  cancelReservation
} from '@/api/admin'

const reservationList = ref([])
const loading = ref(true)
const dialogVisible = ref(false)
const isEdit = ref(false)
const currentReservation = ref({})

const fetchAllReservations = async () => {
  try {
    loading.value = true
    const response = await getAllReservations()
    reservationList.value = response.data
  } catch (error) {
    ElMessage.error('获取预约列表失败！')
  } finally {
    loading.value = false
  }
}

onMounted(fetchAllReservations)

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

const handleEdit = (reservation) => {
  isEdit.value = true
  currentReservation.value = { ...reservation }
  dialogVisible.value = true
}

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
</script>

<template>
  <div>
    <div class="header-bar">
      <h2>预约总览</h2>
      <el-button type="primary" @click="handleAdd">新增预约</el-button>
    </div>
    <el-table :data="reservationList" v-loading="loading" style="width: 100%; margin-top: 20px">
      <el-table-column prop="id" label="预约ID" width="90" />
      <el-table-column prop="userId" label="用户ID" width="90" />
      <el-table-column prop="courtId" label="场地ID" width="90" />
      <el-table-column prop="reservationDate" label="预约日期" />
      <el-table-column prop="startTime" label="开始时间" />
      <el-table-column prop="endTime" label="结束时间" />
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag v-if="scope.row.status === 0" type="primary">已预约</el-tag>
          <el-tag v-else-if="scope.row.status === 1" type="success">已完成</el-tag>
          <el-tag v-else-if="scope.row.status === 2" type="info">已取消</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button
            size="small"
            type="danger"
            @click="handleCancel(scope.row.id)"
            :disabled="scope.row.status === 2"
          >取消预约</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑预约' : '新增预约'" width="500px">
      <el-form :model="currentReservation" label-width="100px">
        <el-form-item label="用户ID">
          <el-input-number v-model="currentReservation.userId" :min="1" />
        </el-form-item>
        <el-form-item label="场地ID">
          <el-input-number v-model="currentReservation.courtId" :min="1" />
        </el-form-item>
        <el-form-item label="预约日期">
          <el-date-picker
            v-model="currentReservation.reservationDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-time-select
            v-model="currentReservation.startTime"
            start="08:00"
            step="01:00"
            end="21:00"
            placeholder="选择开始时间"
          />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-time-select
            v-model="currentReservation.endTime"
            start="09:00"
            step="01:00"
            end="22:00"
            placeholder="选择结束时间"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="currentReservation.status" placeholder="请选择状态">
            <el-option label="已预约" :value="0" />
            <el-option label="已完成" :value="1" />
            <el-option label="已取消" :value="2" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>
