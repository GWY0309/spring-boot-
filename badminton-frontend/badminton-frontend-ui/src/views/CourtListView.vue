<script setup>
import { ref, onMounted } from 'vue';
import { getAllCourts, createReservation } from '@/api'; // 导入新的API
import { ElMessage, ElMessageBox } from 'element-plus';

const courtList = ref([]);
const loading = ref(true);

// --- 新增：用于预约弹窗的状态 ---
const dialogVisible = ref(false); // 控制弹窗的显示与隐藏
const reservationForm = ref({
  courtId: null,
  courtName: '', // 用于在弹窗中显示场地名称
  reservationDate: '',
  startTime: '',
  endTime: ''
});

// 获取场地列表的函数 (不变)
const fetchCourts = async () => {
  try {
    loading.value = true;
    const response = await getAllCourts();
    courtList.value = response.data;
  } catch (error) {
    ElMessage.error('获取场地列表失败！');
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchCourts();
});

// --- 新增：处理预约按钮点击事件 ---
const handleReserveClick = (court) => {
  // 填充表单的初始数据
  reservationForm.value = {
    courtId: court.id,
    courtName: court.name,
    reservationDate: '',
    startTime: '',
    endTime: ''
  };
  // 显示弹窗
  dialogVisible.value = true;
};

// --- 新增：处理提交预约的逻辑 ---
const handleSubmitReservation = async () => {
  if (!reservationForm.value.reservationDate || !reservationForm.value.startTime || !reservationForm.value.endTime) {
    ElMessage.error('请完整填写预约信息！');
    return;
  }

  try {
    const params = {
      courtId: reservationForm.value.courtId,
      reservationDate: reservationForm.value.reservationDate,
      startTime: reservationForm.value.startTime,
      endTime: reservationForm.value.endTime
    };
    await createReservation(params);
    ElMessage.success('恭喜您，预约成功！');
    dialogVisible.value = false; // 关闭弹窗
    // 可以在这里重新加载场地列表或用户的预约列表，我们暂时简化
  } catch (error) {
    ElMessage.error(error.response?.data || '预约失败，请稍后再试');
  }
};
</script>

<template>
  <div>
    <h2>场地列表</h2>
    <el-table :data="courtList" v-loading="loading" style="width: 100%">
      <el-table-column prop="name" label="场地名称" />
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag v-if="scope.row.status === 0" type="success">可用</el-tag>
          <el-tag v-else-if="scope.row.status === 1" type="warning">维修中</el-tag>
          <el-tag v-else type="danger">已停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="pricePerHour" label="价格/小时 (元)" />
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button
            type="primary"
            size="small"
            @click="handleReserveClick(scope.row)"
            :disabled="scope.row.status !== 0"
          >
            预约
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" title="场地预约" width="500px">
      <el-form :model="reservationForm" label-width="80px">
        <el-form-item label="场地名称">
          <el-input v-model="reservationForm.courtName" disabled />
        </el-form-item>
        <el-form-item label="预约日期">
          <el-date-picker
            v-model="reservationForm.reservationDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-time-select
            v-model="reservationForm.startTime"
            start="08:00"
            step="01:00"
            end="21:00"
            placeholder="选择开始时间"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-time-select
            v-model="reservationForm.endTime"
            start="09:00"
            step="01:00"
            end="22:00"
            placeholder="选择结束时间"
            style="width: 100%;"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitReservation">
          确认预约
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>
