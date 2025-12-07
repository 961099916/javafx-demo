import { defineStore } from 'pinia'
import { ApiService } from '@/services/api'

// 定义用户状态接口
interface UserState {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  userInfo: any | null
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  userList: any[]
  loading: boolean
  error: string | null
}

// 创建用户store
export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    userInfo: null,
    userList: [],
    loading: false,
    error: null
  }),

  getters: {
    isLoggedIn: (state) => !!state.userInfo,
    getUserInfo: (state) => state.userInfo,
    getUserList: (state) => state.userList,
    isLoading: (state) => state.loading,
    getError: (state) => state.error
  },

  actions: {
    async fetchUserInfo(userId: string) {
      this.loading = true
      this.error = null
      try {
        const response = await ApiService.getUserInfo(userId)
        this.userInfo = response.data.data // 访问API响应中的data字段
      } catch (error) {
        this.error = (error as Error).message || '获取用户信息失败'
        console.error('获取用户信息失败:', error)
      } finally {
        this.loading = false
      }
    },

    async fetchUserList(
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      params?: any
    ) {
      this.loading = true
      this.error = null
      try {
        const response = await ApiService.getUserList(params)
        this.userList = response.data.data // 访问API响应中的data字段
      } catch (error) {
        this.error = (error as Error).message || '获取用户列表失败'
        console.error('获取用户列表失败:', error)
      } finally {
        this.loading = false
      }
    },

    async createUser(
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      userData: any
    ) {
      this.loading = true
      this.error = null
      try {
        const response = await ApiService.createUser(userData)
        // 添加成功后刷新用户列表
        await this.fetchUserList()
        return response.data.data // 访问API响应中的data字段
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
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      userData: any
    ) {
      this.loading = true
      this.error = null
      try {
        const response = await ApiService.updateUser(userId, userData)
        // 更新成功后刷新用户信息
        await this.fetchUserInfo(userId)
        return response.data.data // 访问API响应中的data字段
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
        return response.data.data // 访问API响应中的data字段
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