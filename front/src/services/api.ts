import request from '@/utils/request'

// 定义User类型
export interface User {
  id?: number
  name: string
  email: string
  age?: number
  createdAt?: string
  updatedAt?: string
}

// 定义分页数据接口
export interface PageData<T> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}

// 定义通用的API响应格式
export interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

// 示例API服务
export class ApiService {
  // 获取用户信息
  static getUserInfo(userId: string) {
    return request.get<ApiResponse<User>>(`/users/${userId}`)
  }

  // 创建用户
  static createUser(userData: Omit<User, 'id' | 'createdAt' | 'updatedAt'>) {
    return request.post<ApiResponse<User>>('/users', userData)
  }

  // 更新用户
  static updateUser(userId: string, userData: Partial<User>) {
    return request.put<ApiResponse<User>>(`/users/${userId}`, userData)
  }

  // 删除用户
  static deleteUser(userId: string) {
    return request.delete<ApiResponse<void>>(`/users/${userId}`)
  }

  // 获取用户列表
  static getUserList(params?: { page?: number; size?: number }) {
    return request.get<ApiResponse<PageData<User>>>('/users/page', { params })
  }
}

export default ApiService