<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAvailableRackets } from '@/api' // 复用获取可用球拍列表的API
import { addRacket, updateRacket, deleteRacket } from '@/api/admin' // 导入管理员球拍API

const racketList = ref([])
const loading = ref(true)
const dialogVisible = ref(false)
const isEdit = ref(false)
const currentRacket = ref({})

// 获取所有球拍
const fetchRackets = async () => {
  try {
    loading.value = true
    // 注意：getAvailableRackets 通常只获取可用的。如果后台有一个获取所有球拍的接口会更好。
    // 但基于现有API，我们暂时先用这个，并假设它能返回所有球拍供管理。
    const response = await getAvailableRackets()
    racketList.value = response.data
  } catch (error) {
    ElMessage.error('获取球拍列表失败！')
  } finally {
    loading.value = false
  }
}

onMounted(fetchRackets)

// 打开新增球拍弹窗
const handleAdd = () => {
  isEdit.value = false
  currentRacket.value = {
    brand: '',
    model: '',
    status: 0,
    rentalPricePerHour: 10
  }
  dialogVisible.value = true
}

// 打开编辑球拍弹窗
const handleEdit = (racket) => {
  isEdit.value = true
  currentRacket.value = { ...racket }
  dialogVisible.value = true
}

// 处理删除球拍
const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除这把球拍吗？此操作不可恢复。', '警告', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteRacket(id)
      ElMessage.success('球拍删除成功！')
      fetchRackets() // 刷新列表
    } catch (error) {
      ElMessage.error(error.response?.data || '删除失败！')
    }
  })
}

// 提交表单（新增或修改）
const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await updateRacket(currentRacket.value.id, currentRacket.value)
      ElMessage.success('球拍信息更新成功！')
    } else {
      await addRacket(currentRacket.value)
      ElMessage.success('新球拍添加成功！')
    }
    dialogVisible.value = false
    fetchRackets() // 重新加载数据
  } catch (error) {
    ElMessage.error(error.response?.data || '操作失败！')
  }
}
</script>

<template>
  <div>
    <div class="header-bar">
      <h2>球拍管理</h2>
      <el-button type="primary" @click="handleAdd">新增球拍</el-button>
    </div>

    <el-table :data="racketList" v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="brand" label="品牌" />
      <el-table-column prop="model" label="型号" />
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag v-if="scope.row.status === 0" type="success">可用</el-tag>
          <el-tag v-else-if="scope.row.status === 1" type="warning">已租出</el-tag>
          <el-tag v-else type="danger">维修中</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="rentalPricePerHour" label="租金/小时 (元)" />
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.id)"
          >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑球拍' : '新增球拍'" width="500px">
      <el-form :model="currentRacket" label-width="100px">
        <el-form-item label="品牌">
          <el-input v-model="currentRacket.brand" />
        </el-form-item>
        <el-form-item label="型号">
          <el-input v-model="currentRacket.model" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="currentRacket.status" placeholder="请选择状态">
            <el-option label="可用" :value="0" />
            <el-option label="已租出" :value="1" />
            <el-option label="维修中" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="租金/小时">
          <el-input-number v-model="currentRacket.rentalPricePerHour" :min="0" />
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
