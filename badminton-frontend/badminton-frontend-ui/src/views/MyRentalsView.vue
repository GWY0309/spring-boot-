<script setup>
import { ref, onMounted } from 'vue';
import { getMyRentals } from '@/api'; // 导入API
import { ElMessage } from 'element-plus';

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
            :disabled="scope.row.status !== 0"
          >
            归还
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
