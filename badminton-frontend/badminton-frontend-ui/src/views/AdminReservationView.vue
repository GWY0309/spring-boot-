<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getAllReservations } from '@/api/admin' // 导入新的API

const reservationList = ref([])
const loading = ref(true)

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
</script>

<template>
  <div>
    <h2>预约总览</h2>
    <p>这里展示了系统内所有的用户预约记录。</p>
    <el-table :data="reservationList" v-loading="loading" style="width: 100%; margin-top: 20px;">
      <el-table-column prop="id" label="预约ID" width="90" />
      <el-table-column prop="userId" label="用户ID" width="90" />
      <el-table-column prop="courtId" label="场地ID" width="90" />
      <el-table-column prop="reservationDate" label="预约日期" />
      <el-table-column prop="startTime" label="开始时间" />
      <el-table-column prop="endTime" label="结束时间" />
      <el-table-column prop="totalCost" label="费用 (元)" />
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag v-if="scope.row.status === 0" type="primary">已预约</el-tag>
          <el-tag v-else-if="scope.row.status === 1" type="success">已完成</el-tag>
          <el-tag v-else-if="scope.row.status === 2" type="info">已取消</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" />
    </el-table>
  </div>
</template>

<style scoped>
/* 可以根据需要添加样式 */
</style>
