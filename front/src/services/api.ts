import request from '@/utils/request'

// 定义通用的API响应格式
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

// 示例API服务
export class ApiService {
  // 获取用户信息
  static getUserInfo(userId: string) {
    return request.get<ApiResponse<any>>(`/user/${userId}`)
  }

  // 创建用户
  static createUser(userData: any) {
    return request.post<ApiResponse<any>>('/user', userData)
  }

  // 更新用户
  static updateUser(userId: string, userData: any) {
    return request.put<ApiResponse<any>>(`/user/${userId}`, userData)
  }

  // 删除用户
  static deleteUser(userId: string) {
    return request.delete<ApiResponse<any>>(`/user/${userId}`)
  }

  // 获取用户列表
  static getUserList(params?: any) {
    return request.get<ApiResponse<any[]>>('/users', { params })
  }
}

export default ApiService