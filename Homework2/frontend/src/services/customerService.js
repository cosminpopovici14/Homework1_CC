import api from './api';

export const  customerService = {
  getAll: () => api.get('/customers').then(res => res.data),
  create: (customer) => api.post('/customers', customer).then(res => res.data),
  update: (id, customer) => api.put(`/customers/${id}`, customer).then(res => res.data),
  delete: (id) => api.delete(`/customers/${id}`).then(res => res.data),
};
