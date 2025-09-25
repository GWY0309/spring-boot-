<script setup>
import { ref, onMounted } from 'vue';
import { getAvailableRackets, rentRacket } from '@/api'; // 导入获取和租借球拍的 API
import { ElMessage, ElMessageBox } from 'element-plus';
import { useRouter } from 'vue-router';

const router = useRouter();
const racketList = ref([]);
const loading = ref(true);

// 获取可用球拍列表
const fetchRackets = async () => {
  try {
    loading.value = true;
    const response = await getAvailableRackets();
    racketList.value = response.data;
  } catch (error) {
    ElMessage.error('获取可用球拍列表失败！');
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchRackets();
});

// 处理租借按钮点击事件
const handleRent = (racketId) => {
  ElMessageBox.confirm(
    '您确定要租借这把球拍吗？',
    '确认租借',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info',
    }
  )
    .then(async () => {
      try {
        await rentRacket(racketId);
        ElMessage.success('球拍租借成功！您可以前往“我的租借”页面查看。');
        // 租借成功后，刷新列表，可以看到被租借的球拍消失
        fetchRackets();
      } catch (error) {
        ElMessage.error(error.response?.data || '租借失败，请稍后再试');
      }
    })
    .catch(() => {
      ElMessage.info('操作已取消');
    });
};
</script>

<template>
  <div>
    <h2>球拍租赁</h2>
    <p>这里显示所有当前可供租借的球拍。</p>
    <el-table :data="racketList" v-loading="loading" style="width: 100%; margin-top: 20px;">
      <el-table-column prop="id" label="球拍ID" width="100" />
      <el-table-column prop="brand" label="品牌" />
      <el-table-column prop="model" label="型号" />
      <el-table-column prop="rentalPricePerHour" label="租金/小时 (元)" />
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button
            type="primary"
            size="small"
            @click="handleRent(scope.row.id)"
          >
            租借
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
