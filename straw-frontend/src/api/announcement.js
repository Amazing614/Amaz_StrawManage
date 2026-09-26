import request from './request'

export function getPublishedAnnouncements(params) {
  return request.get('/announcement/published', { params })
}

export function getAnnouncementList(params) {
  return request.get('/announcement/list', { params })
}

export function getAnnouncementDetail(id) {
  return request.get(`/announcement/${id}`)
}

export function createAnnouncement(data) {
  return request.post('/announcement/create', data)
}

export function updateAnnouncement(id, data) {
  return request.put(`/announcement/${id}`, data)
}

export function deleteAnnouncement(id) {
  return request.delete(`/announcement/${id}`)
}
