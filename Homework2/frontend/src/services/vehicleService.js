import api from './api';

export const vehicleService = {
  getAll: () => api.get('/vehicles').then(res => res.data),
  create: (vehicle) => api.post('/vehicles', vehicle).then(res => res.data),
  update: (id, vehicle) => api.put(`/vehicles/${id}`, vehicle).then(res => res.data),
  delete: (id) => api.delete(`/vehicles/${id}`).then(res => res.data),
};
