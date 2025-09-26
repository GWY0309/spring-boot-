<script setup>
// ... <script setup> 部分的代码保持完全不变 ...
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllCourts } from '@/api'
import { addCourt, updateCourt, deleteCourt } from '@/api/admin'
import { Plus, Edit, Delete } from '@element-plus/icons-vue' // 导入新图标

const courtList = ref([])
const loading = ref(true)
const dialogVisible = ref(false)
const isEdit = ref(false)
const currentCourt = ref({})

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

const handleEdit = (court) => {
  isEdit.value = true
  currentCourt.value = { ...court }
  dialogVisible.value = true
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除这个场地吗？此操作不可恢复。', '警告', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteCourt(id)
      ElMessage.success('场地删除成功！')
      fetchCourts()
    } catch (error) {
      ElMessage.error(error.response?.data || '删除失败！')
    }
  })
}

const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await updateCourt(currentCourt.value.id, currentCourt.value)
      ElMessage.success('场地信息更新成功！')
    } else {
      await addCourt(currentCourt.value)
      ElMessage.success('新场地添加成功！')
    }
    dialogVisible.value = false
    fetchCourts()
  } catch (error) {
    ElMessage.error(error.response?.data || '操作失败！')
  }
}

// 根据状态返回不同的 tag 类型
const getStatusTagType = (status) => {
  if (status === 0) return 'success'
  if (status === 1) return 'warning'
  return 'danger'
}
</script>

<template>
  <div class="page-container">
    <div class="header-bar">
      <h2>场地管理</h2>
      <el-button type="primary" :icon="Plus" @click="handleAdd" class="add-button">新增场地</el-button>
    </div>

    <el-row :gutter="20" v-loading="loading">
      <el-col :xs="24" :sm="12" :md="8" v-for="court in courtList" :key="court.id">
        <div class="court-card">
          <div class="card-top">
            <el-tag :type="getStatusTagType(court.status)" effect="dark" round>
              {{ court.status === 0 ? '可用' : court.status === 1 ? '维修中' : '已停用' }}
            </el-tag>
            <div class="actions">
              <el-button :icon="Edit" circle @click="handleEdit(court)" />
              <el-button :icon="Delete" circle type="danger" @click="handleDelete(court.id)" />
            </div>
          </div>
          <div class="card-content">
            <h3 class="court-name">{{ court.name }}</h3>
            <p class="court-type">{{ court.type }}</p>
          </div>
          <div class="card-footer">
            <div class="info-item">
              <span class="label">价格</span>
              <span class="value">¥{{ court.pricePerHour }}/小时</span>
            </div>
            <div class="info-item">
              <span class="label">开放时间</span>
              <span class="value">{{ court.openTime }} - {{ court.closeTime }}</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑场地' : '新增场地'"
      width="600px"
      class="neumorphism-dialog"
    >
      <el-form :model="currentCourt" label-position="top">
        <el-form-item label="场地名称">
          <el-input v-model="currentCourt.name" />
        </el-form-item>
        <el-form-item label="类型">
          <el-input v-model="currentCourt.type" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="currentCourt.status" placeholder="请选择状态" style="width: 100%;">
            <el-option label="可用" :value="0" />
            <el-option label="维修中" :value="1" />
            <el-option label="已停用" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格/小时 (元)">
          <el-input-number v-model="currentCourt.pricePerHour" :min="0" style="width: 100%;"/>
        </el-form-item>
        <el-form-item label="开放时间">
          <el-time-picker v-model="currentCourt.openTime" value-format="HH:mm:ss" style="width: 100%;"/>
        </el-form-item>
        <el-form-item label="关闭时间">
          <el-time-picker v-model="currentCourt.closeTime" value-format="HH:mm:ss" style="width: 100%;"/>
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
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.add-button {
  box-shadow: 0 2px 12px 0 rgba(64, 158, 255, 0.5);
}

.court-card {
  background-color: #fff;
  border-radius: 12px;
  margin-bottom: 20px;
  border: 1px solid #eef0f3;
  transition: all 0.3s ease;
  overflow: hidden;
}

.court-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px 0 rgba(0, 0, 0, 0.1);
}

.card-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background-color: #fafafa;
  border-bottom: 1px solid #eef0f3;
}

.card-content {
  padding: 20px;
}

.court-name {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 5px 0;
  color: #303133;
}

.court-type {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.card-footer {
  padding: 15px 20px;
  background-color: #fafafa;
  border-top: 1px solid #eef0f3;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-item {
  display: flex;
  flex-direction: column;
}

.info-item .label {
  font-size: 12px;
  color: #909399;
}

.info-item .value {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
}

.actions {
  display: flex;
  gap: 10px;
}
</style>
