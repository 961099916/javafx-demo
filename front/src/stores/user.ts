import { defineStore } from 'pinia'
import { ApiService, type User, type PageData } from '@/services/api'

// 定义用户状态接口
interface UserState {
  userInfo: User | null
  userList: User[]
  loading: boolean
  error: string | null
  total: number
}

// 创建用户store
export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    userInfo: null,
    userList: [],
    loading: false,
    error: null,
    total: 0
  }),

  getters: {
    isLoggedIn: (state) => !!state.userInfo,
    getUserInfo: (state) => state.userInfo,
    getUserList: (state) => state.userList,
    isLoading: (state) => state.loading,
    getError: (state) => state.error,
    getTotal: (state) => state.total
  },

  actions: {
    async fetchUserInfo(userId: string) {
      this.loading = true
      this.error = null
      try {
        const response = await ApiService.getUserInfo(userId)
        // 访问API响应中的data字段
        if (response && response.data) {
          this.userInfo = response.data as unknown as User
        }
      } catch (error) {
        this.error = (error as Error).message || '获取用户信息失败'
        console.error('获取用户信息失败:', error)
      } finally {
        this.loading = false
      }
    },

    async fetchUserList(
      params?: { page?: number; size?: number }
    ) {
      this.loading = true
      this.error = null
      try {
        const response = await ApiService.getUserList(params)
        // 根据实际的API响应结构调整
        if (response && response.data) {
          const data = response.data as unknown as PageData<User>
          if (data && data.records) {
            // 如果是分页数据结构
            this.userList = data.records
            this.total = data.total
          } else if (Array.isArray(data)) {
            // 如果是普通数组
            this.userList = data
            this.total = data.length
          } else {
            this.userList = []
            this.total = 0
          }
        }
      } catch (error) {
        this.error = (error as Error).message || '获取用户列表失败'
        console.error('获取用户列表失败:', error)
      } finally {
        this.loading = false
      }
    },

    async createUser(
      userData: Omit<User, 'id' | 'createdAt' | 'updatedAt'>
    ) {
      this.loading = true
      this.error = null
      try {
        const response = await ApiService.createUser(userData)
        // 添加成功后刷新用户列表
        await this.fetchUserList()
        return response.data // 访问API响应中的data字段
      } catch (error) {
        this.error = (error as Error).message || '创建用户失败'
        console.error('创建用户失败:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    async updateUser(
      userId: string,
      userData: Partial<User>
    ) {
      this.loading = true
      this.error = null
      try {
        const response = await ApiService.updateUser(userId, userData)
        // 更新成功后刷新用户信息
        await this.fetchUserInfo(userId)
        return response.data // 访问API响应中的data字段
      } catch (error) {
        this.error = (error as Error).message || '更新用户失败'
        console.error('更新用户失败:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    async deleteUser(userId: string) {
      this.loading = true
      this.error = null
      try {
        const response = await ApiService.deleteUser(userId)
        // 删除成功后刷新用户列表
        await this.fetchUserList()
        return response.data // 访问API响应中的data字段
      } catch (error) {
        this.error = (error as Error).message || '删除用户失败'
        console.error('删除用户失败:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    clearError() {
      this.error = null
    }
  }
})