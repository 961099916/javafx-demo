import axios from 'axios'
import type { AxiosInstance, AxiosResponse, AxiosError } from 'axios'

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api', // 直接指向后端服务地址
  timeout: 15000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  }
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 在发送请求之前做些什么
    // 可以在这里添加token等认证信息
    // if (store.getters.token) {
    //   config.headers['Authorization'] = `Bearer ${store.getters.token}`
    // }
    return config
  },
  (error: AxiosError) => {
    // 对请求错误做些什么
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    // 对响应数据做点什么
    const res = response.data

    // 根据实际的后端返回格式进行调整
    if (res.code !== 200) {
      console.error('响应错误:', res.message || 'Error')
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  (error: AxiosError) => {
    // 对响应错误做点什么
    console.error('响应错误:', error)
    return Promise.reject(error)
  }
)

export default service
