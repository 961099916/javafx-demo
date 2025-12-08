<template>
  <div class="user-management">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <el-button type="primary" @click="showCreateDialog">创建用户</el-button>
        </div>
      </template>

      <!-- 用户列表 -->
      <el-table 
        :data="userList" 
        style="width: 100%" 
        v-loading="isLoading"
        element-loading-text="加载中..."
      >
        <el-table-column prop="id" label="ID" width="180" />
        <el-table-column prop="name" label="姓名" width="180" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" @click="editUser(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteUser(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        layout="prev, pager, next"
        :total="pagination.total"
        :page-size="pagination.pageSize"
        :current-page="pagination.currentPage"
        @current-change="handlePageChange"
        style="margin-top: 20px; text-align: right;"
      />
    </el-card>

    <!-- 创建/编辑用户对话框 -->
    <el-dialog 
      :title="dialogTitle" 
      v-model="dialogVisible" 
      width="500px"
      @close="resetForm"
    >
      <el-form :model="userForm" :rules="userRules" ref="userFormRef" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="userForm.name" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitUser" :loading="isLoading">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 错误提示 -->
    <el-alert
      v-if="errorMessage"
      :title="errorMessage"
      type="error"
      show-icon
      closable
      @close="clearError"
      style="margin-top: 20px;"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { useUserStore } from '@/stores/user'
import type { User } from '@/services/api'

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 使用用户store
const userStore = useUserStore()

// 表单引用
const userFormRef = ref<FormInstance>()

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('创建用户')
const isEditing = ref(false)
const editingUserId = ref('')

// 用户表单数据
const userForm = reactive({
  name: '',
  email: ''
})

// 表单验证规则
const userRules = reactive<FormRules>({
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
})

// 计算属性
const userList = computed(() => userStore.getUserList)
const isLoading = computed(() => userStore.isLoading)
const errorMessage = computed(() => userStore.getError)
const pagination = computed(() => ({
  currentPage: currentPage.value,
  pageSize: pageSize.value,
  total: userStore.getTotal // 从store中获取总条数
}))

// 方法
const showCreateDialog = () => {
  dialogTitle.value = '创建用户'
  isEditing.value = false
  dialogVisible.value = true
}

const editUser = (user: User) => {
  dialogTitle.value = '编辑用户'
  isEditing.value = true
  editingUserId.value = user.id?.toString() || ''
  userForm.name = user.name
  userForm.email = user.email
  dialogVisible.value = true
}

const submitUser = async () => {
  if (!userFormRef.value) return
  
  await userFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEditing.value) {
          // 更新用户
          await userStore.updateUser(editingUserId.value, {
            name: userForm.name,
            email: userForm.email
          })
          ElMessage.success('用户更新成功')
        } else {
          // 创建用户
          await userStore.createUser({
            name: userForm.name,
            email: userForm.email
          })
          ElMessage.success('用户创建成功')
        }
        dialogVisible.value = false
      } catch (error) {
        console.error('操作失败:', error)
      }
    }
  })
}

const deleteUser = (userId: number) => {
  ElMessageBox.confirm('确定要删除这个用户吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await userStore.deleteUser(userId.toString())
      ElMessage.success('用户删除成功')
    } catch (error) {
      console.error('删除失败:', error)
    }
  }).catch(() => {
    // 用户取消删除
  })
}

const resetForm = () => {
  userForm.name = ''
  userForm.email = ''
  if (userFormRef.value) {
    userFormRef.value.resetFields()
  }
}

const clearError = () => {
  userStore.clearError()
}

// 组件挂载时获取用户列表
onMounted(() => {
  fetchUsersWithPagination()
})

// 获取带分页的用户列表
const fetchUsersWithPagination = async () => {
  try {
    await userStore.fetchUserList({
      page: currentPage.value,
      size: pageSize.value
    })
    // 更新总条数（已经在store中处理了）
    total.value = userStore.getTotal
  } catch (error) {
    console.error('获取用户列表失败:', error)
  }
}

// 处理页码变化
const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchUsersWithPagination()
}
</script>

<style scoped>
.user-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dialog-footer {
  text-align: right;
}
</style>