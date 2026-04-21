import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8686/api'
});

// Interceptor para inyectar el token en cada petición
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// --- RUTAS DE SALAS ---
export const fetchRooms = () => api.get('/rooms');

// --- RUTAS DE MENSAJES ---
export const fetchMessagesByRoom = (roomId) => api.get(`/messages/room/${roomId}`);
export const createMessage = (roomId, content) => api.post(`/messages/room/${roomId}`, { content });

// --- RUTAS DE MODERACIÓN ---
export const fetchPendingMessages = (roomId) => api.get(`/messages/room/${roomId}/pending`);
export const updateMessageStatus = (messageId, status) => api.patch(`/messages/${messageId}/status`, { status });