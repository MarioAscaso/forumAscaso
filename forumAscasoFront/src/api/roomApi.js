import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8686/api'
});

// Esto es un "Interceptor": inyecta el token automáticamente si existe
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const fetchRooms = () => api.get('/rooms');