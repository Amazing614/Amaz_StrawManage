import request from './request'

// 用户端
export function getFaqCategories() {
  return request.get('/faq/categories')
}

// 管理端
export function getFaqList(params) {
  return request.get('/admin/faq/list', { params })
}

export function createFaq(data) {
  return request.post('/admin/faq', data)
}

export function updateFaq(id, data) {
  return request.put(`/admin/faq/${id}`, data)
}

export function deleteFaq(id) {
  return request.delete(`/admin/faq/${id}`)
}
