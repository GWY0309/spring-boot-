<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
// API imports
import { getAvailableRackets } from '@/api'
import { addRacket, updateRacket, deleteRacket } from '@/api/admin'
// Icon imports
import { Plus, Edit, Delete } from '@element-plus/icons-vue'

const racketList = ref([])
const loading = ref(true)
const dialogVisible = ref(false)
const isEdit = ref(false)
const currentRacket = ref({})

// 获取所有球拍
const fetchRackets = async () => {
  try {
    loading.value = true
    // 假设这个API能返回所有球拍供管理
    const response = await getAvailableRackets()
    racketList.value = response.data
  } catch (error) {
    ElMessage.error('获取球拍列表失败！')
  } finally {
    loading.value = false
  }
}

onMounted(fetchRackets)

// 打开新增弹窗
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

// 打开编辑弹窗
const handleEdit = (racket) => {
  isEdit.value = true
  currentRacket.value = { ...racket }
  dialogVisible.value = true
}

// 删除逻辑
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

// 提交表单
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
    fetchRackets()
  } catch (error) {
    ElMessage.error(error.response?.data || '操作失败！')
  }
}

// 状态Tag帮助函数
const getStatusTagType = (status) => {
  if (status === 0) return 'success'
  if (status === 1) return 'warning'
  return 'danger'
}
</script>

<template>
  <div class="page-container">
    <div class="header-bar">
      <h2>球拍管理</h2>
      <el-button type="primary" :icon="Plus" @click="handleAdd" class="add-button">新增球拍</el-button>
    </div>

    <el-row :gutter="20" v-loading="loading">
      <el-col :xs="24" :sm="12" :md="8" v-for="racket in racketList" :key="racket.id">
        <div class="racket-card">
          <div class="card-top">
            <el-tag :type="getStatusTagType(racket.status)" effect="dark" round>
              {{ racket.status === 0 ? '可用' : racket.status === 1 ? '已租出' : '维修中' }}
            </el-tag>
            <div class="actions">
              <el-button :icon="Edit" circle @click="handleEdit(racket)" />
              <el-button :icon="Delete" circle type="danger" @click="handleDelete(racket.id)" />
            </div>
          </div>
          <div class="card-content">
            <h3 class="racket-brand">{{ racket.brand }}</h3>
            <p class="racket-model">{{ racket.model }}</p>
          </div>
          <div class="card-footer">
            <div class="info-item">
              <span class="label">租金</span>
              <span class="value">¥{{ racket.rentalPricePerHour }}/小时</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑球拍' : '新增球拍'"
      width="600px"
      class="neumorphism-dialog"
    >
      <el-form :model="currentRacket" label-position="top">
        <el-form-item label="品牌">
          <el-input v-model="currentRacket.brand" />
        </el-form-item>
        <el-form-item label="型号">
          <el-input v-model="currentRacket.model" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="currentRacket.status" placeholder="请选择状态" style="width: 100%;">
            <el-option label="可用" :value="0" />
            <el-option label="已租出" :value="1" />
            <el-option label="维修中" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="租金/小时 (元)">
          <el-input-number v-model="currentRacket.rentalPricePerHour" :min="0" style="width: 100%;" />
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

.racket-card {
  background-color: #fff;
  border-radius: 12px;
  margin-bottom: 20px;
  border: 1px solid #eef0f3;
  transition: all 0.3s ease;
  overflow: hidden;
}

.racket-card:hover {
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
  min-height: 80px; /* 给内容区一个最小高度，防止卡片高度不一 */
}

.racket-brand {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 5px 0;
  color: #303133;
}

.racket-model {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.card-footer {
  padding: 15px 20px;
  background-color: #fafafa;
  border-top: 1px solid #eef0f3;
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
