import api from './api';

export const optimizationService = {
  solve: () => api.post('/optimization/solve').then(res => res.data),
};
