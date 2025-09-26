<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllRentals, forceReturnRacket } from '@/api/admin'

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
</script>

<template>
  <div>
    <h2>租借总览</h2>
    <el-table :data="rentalList" v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="租借ID" width="80" />
      <el-table-column prop="racketId" label="球拍ID" width="80" />
      <el-table-column prop="userId" label="用户ID" width="80" />
      <el-table-column prop="rentalTime" label="租借时间" />
      <el-table-column prop="returnTime" label="归还时间" />
      <el-table-column prop="totalCost" label="费用 (元)" />
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag v-if="scope.row.status === 0" type="primary">租借中</el-tag>
          <el-tag v-else-if="scope.row.status === 1" type="success">已归还</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button
            type="warning"
            size="small"
            @click="handleForceReturn(scope.row)"
            :disabled="scope.row.status !== 0"
          >
            强制归还
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
