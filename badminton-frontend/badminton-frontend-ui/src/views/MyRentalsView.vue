<script setup>
import { ref, onMounted } from 'vue';
import { getMyRentals, returnRacket } from '@/api'; // 导入新的API
import { ElMessage, ElMessageBox } from 'element-plus';

const rentalList = ref([]);
const loading = ref(true);

const fetchMyRentals = async () => {
  try {
    loading.value = true;
    const response = await getMyRentals();
    rentalList.value = response.data;
  } catch (error) {
    ElMessage.error('获取租借列表失败！');
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchMyRentals();
});

// --- 新增：处理归还按钮的点击事件 ---
const handleReturn = (rentalId) => {
  ElMessageBox.confirm(
    '您确定要归还这把球拍吗？',
    '确认归还',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info',
    }
  )
    .then(async () => {
      try {
        await returnRacket(rentalId);
        ElMessage.success('球拍归还成功！');
        // 归还成功后，立即刷新列表以显示最新状态
        fetchMyRentals();
      } catch (error) {
        ElMessage.error(error.response?.data || '归还失败，请稍后再试');
      }
    })
    .catch(() => {
      ElMessage.info('操作已取消');
    });
};
</script>

<template>
  <div>
    <h2>我的租借记录</h2>
    <el-table :data="rentalList" v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="租借ID" width="100" />
      <el-table-column prop="racketId" label="球拍ID" width="100" />
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
            type="success"
            size="small"
            @click="handleReturn(scope.row.id)"
            :disabled="scope.row.status !== 0"
          >
            归还
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
