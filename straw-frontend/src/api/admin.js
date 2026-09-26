import request from './request'

export function getUserList(params) {
  return request.get('/admin/users', { params })
}

export function updateUserStatus(id, status) {
  return request.put(`/admin/user/${id}/status`, null, { params: { status } })
}

export function getDashboard() {
  return request.get('/admin/dashboard')
}

export function getRegionStatistics() {
  return request.get('/admin/statistics/region')
}

// Excel导出
export function exportStrawExcel(params) {
  return request.get('/admin/export/straw', { params, responseType: 'blob' })
}

export function exportOrderExcel(params) {
  return request.get('/admin/export/order', { params, responseType: 'blob' })
}

export function exportPurchaseExcel(params) {
  return request.get('/admin/export/purchase', { params, responseType: 'blob' })
}

export function getAdminPurchaseList(params) {
  return request.get('/admin/purchase/list', { params })
}
