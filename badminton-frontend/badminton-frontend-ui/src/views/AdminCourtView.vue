<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllCourts } from '@/api' // 复用获取场地列表的API
import { addCourt, updateCourt, deleteCourt } from '@/api/admin' // 导入管理员API

const courtList = ref([])
const loading = ref(true)
const dialogVisible = ref(false)
const isEdit = ref(false)
const currentCourt = ref({})

// 获取所有场地
const fetchCourts = async () => {
  try {
    loading.value = true
    const response = await getAllCourts()
    courtList.value = response.data
  } catch (error) {
    ElMessage.error('获取场地列表失败！')
  } finally {
    loading.value = false
  }
}

onMounted(fetchCourts)

// 打开新增场地弹窗
const handleAdd = () => {
  isEdit.value = false
  currentCourt.value = {
    name: '',
    type: '室内',
    status: 0,
    pricePerHour: 50,
    openTime: '08:00:00',
    closeTime: '22:00:00'
  }
  dialogVisible.value = true
}

// 打开编辑场地弹窗
const handleEdit = (court) => {
  isEdit.value = true
  currentCourt.value = { ...court } // 复制一份，避免直接修改列表数据
  dialogVisible.value = true
}

// 处理删除场地
const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除这个场地吗？此操作不可恢复。', '警告', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteCourt(id)
      ElMessage.success('场地删除成功！')
      fetchCourts() // 刷新列表
    } catch (error) {
      ElMessage.error(error.response?.data || '删除失败！')
    }
  })
}

// 提交表单（新增或修改）
const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      // 执行更新逻辑
      await updateCourt(currentCourt.value.id, currentCourt.value)
      ElMessage.success('场地信息更新成功！')
    } else {
      // 执行新增逻辑
      await addCourt(currentCourt.value)
      ElMessage.success('新场地添加成功！')
    }
    dialogVisible.value = false
    fetchCourts() // 重新加载数据
  } catch (error) {
    ElMessage.error(error.response?.data || '操作失败！')
  }
}
</script>

<template>
  <div>
    <div class="header-bar">
      <h2>场地管理</h2>
      <el-button type="primary" @click="handleAdd">新增场地</el-button>
    </div>

    <el-table :data="courtList" v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="场地名称" />
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag v-if="scope.row.status === 0" type="success">可用</el-tag>
          <el-tag v-else-if="scope.row.status === 1" type="warning">维修中</el-tag>
          <el-tag v-else type="danger">已停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="pricePerHour" label="价格/小时" />
      <el-table-column prop="openTime" label="开放时间" />
      <el-table-column prop="closeTime" label="关闭时间" />
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.id)"
          >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑场地' : '新增场地'" width="500px">
      <el-form :model="currentCourt" label-width="100px">
        <el-form-item label="场地名称">
          <el-input v-model="currentCourt.name" />
        </el-form-item>
        <el-form-item label="类型">
          <el-input v-model="currentCourt.type" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="currentCourt.status" placeholder="请选择状态">
            <el-option label="可用" :value="0" />
            <el-option label="维修中" :value="1" />
            <el-option label="已停用" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格/小时">
          <el-input-number v-model="currentCourt.pricePerHour" :min="0" />
        </el-form-item>
        <el-form-item label="开放时间">
          <el-time-picker v-model="currentCourt.openTime" value-format="HH:mm:ss" />
        </el-form-item>
        <el-form-item label="关闭时间">
          <el-time-picker v-model="currentCourt.closeTime" value-format="HH:mm:ss" />
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
