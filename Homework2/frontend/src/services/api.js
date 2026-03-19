import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api', // Adresa de baza a backend-ului Spring Boot
});

export default api;
