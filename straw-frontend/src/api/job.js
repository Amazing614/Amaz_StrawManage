import request from './request'

export function createSchedule(data) {
  return request.post('/job/schedule', data)
}

export function getJobList(params) {
  return request.get('/job/list', { params })
}

export function updateProgress(id, progress) {
  return request.put(`/job/${id}/progress`, null, { params: { progress } })
}

export function getJobStatistics() {
  return request.get('/job/statistics')
}

export function getJobProviders() {
  return request.get('/job/providers')
}
