<template>
  <div class="example-component">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>Element Plus 示例</span>
        </div>
      </template>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-button type="primary" @click="handlePrimaryClick">主要按钮</el-button>
          <el-button type="success" @click="handleSuccessClick">成功按钮</el-button>
          <el-button type="warning" @click="handleWarningClick">警告按钮</el-button>
          <el-button type="danger" @click="handleDangerClick">危险按钮</el-button>
        </el-col>
        
        <el-col :span="12">
          <el-input v-model="inputValue" placeholder="请输入内容" />
          <el-button type="primary" @click="showMessage">显示消息</el-button>
        </el-col>
      </el-row>
      
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="24">
          <el-table :data="tableData" style="width: 100%">
            <el-table-column prop="date" label="日期" width="180" />
            <el-table-column prop="name" label="姓名" width="180" />
            <el-table-column prop="address" label="地址" />
          </el-table>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

// 输入框绑定值
const inputValue = ref('')

// 表格数据
const tableData = ref([
  {
    date: '2023-01-01',
    name: '张三',
    address: '北京市朝阳区'
  },
  {
    date: '2023-01-02',
    name: '李四',
    address: '上海市浦东新区'
  },
  {
    date: '2023-01-03',
    name: '王五',
    address: '广州市天河区'
  }
])

// 按钮点击事件处理函数
const handlePrimaryClick = () => {
  ElMessage.success('主要按钮被点击了！')
}

const handleSuccessClick = () => {
  ElMessage.success('成功按钮被点击了！')
}

const handleWarningClick = () => {
  ElMessage.warning('警告按钮被点击了！')
}

const handleDangerClick = () => {
  ElMessage.error('危险按钮被点击了！')
}

const showMessage = () => {
  if (inputValue.value) {
    ElMessage.info(`输入的内容是: ${inputValue.value}`)
  } else {
    ElMessage.warning('请输入内容后再点击按钮！')
  }
}
</script>

<style scoped>
.example-component {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.box-card {
  margin-bottom: 20px;
}
</style>