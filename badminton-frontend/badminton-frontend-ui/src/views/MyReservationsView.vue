<script setup>
import { ref, onMounted, computed } from 'vue'; // 导入 computed
import { getMyReservations, cancelReservation } from '@/api'; // 导入新的API
import { ElMessage, ElMessageBox } from 'element-plus';

const reservationList = ref([]);
const loading = ref(true);

const fetchMyReservations = async () => {
  try {
    loading.value = true;
    const response = await getMyReservations();
    reservationList.value = response.data;
  } catch (error) {
    ElMessage.error('获取预约列表失败！');
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchMyReservations();
});

// --- 新增：判断取消按钮是否应该被禁用 ---
const isCancelDisabled = (reservation) => {
  // 如果状态不是“已预约”，则禁用
  if (reservation.status !== 0) {
    return true;
  }
  // 将日期和时间字符串拼接起来，创建可比较的 Date 对象
  const reservationDateTime = new Date(`${reservation.reservationDate}T${reservation.startTime}`);
  // 如果预约时间在当前时间之前，则禁用
  return reservationDateTime < new Date();
};

// --- 新增：处理取消按钮点击事件 ---
const handleCancel = (reservationId) => {
  ElMessageBox.confirm(
    '您确定要取消这个预约吗？此操作不可逆。',
    '确认取消',
    {
      confirmButtonText: '确定',
      cancelButtonText: '点错了',
      type: 'warning',
    }
  )
    .then(async () => {
      // 用户点击了“确定”
      try {
        await cancelReservation(reservationId);
        ElMessage.success('预约已成功取消');
        // 刷新列表以显示最新状态
        fetchMyReservations();
      } catch (error) {
        ElMessage.error(error.response?.data || '取消失败，请稍后再试');
      }
    })
    .catch(() => {
      // 用户点击了“点错了”或关闭了弹窗
      ElMessage.info('操作已取消');
    });
};
</script>

<template>
  <div>
    <h2>我的预约记录</h2>
    <el-table :data="reservationList" v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="预约ID" width="100" />
      <el-table-column prop="courtId" label="场地ID" width="100" />
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
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button
            type="danger"
            size="small"
            @click="handleCancel(scope.row.id)"
            :disabled="isCancelDisabled(scope.row)"
          >
            取消预约
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
