import { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, useNavigate } from 'react-router-dom';
import Login from './components/Login';
import Register from './components/Register';
import RoomList from './components/RoomList';
import RoomDetail from './components/RoomDetail';

function AppRoutes() {
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem('token'));
  
  // Función para obtener el rol del token guardado
  const getUserRole = () => {
    const token = localStorage.getItem('token');
    if (!token) return null;
    const payload = JSON.parse(window.atob(token.split('.')[1]));
    return payload.role; // Aquí leemos el rol que añadimos en Java
  };

  const role = getUserRole();

  return (
    <Routes>
      {/* RUTA PÚBLICA */}
      <Route path="/login" element={<Login onLoginSuccess={() => setIsLoggedIn(true)} />} />

      {/* RUTA PROTEGIDA: Panel Admin (Solo SUPERADMIN) */}
      <Route 
        path="/admin" 
        element={isLoggedIn && role === 'SUPERADMIN' ? <AdminPanel /> : <Navigate to="/" />} 
      />

      {/* RUTAS NORMALES */}
      <Route path="/" element={isLoggedIn ? <RoomList /> : <Navigate to="/login" />} />
      <Route path="/room/:id" element={isLoggedIn ? <RoomDetail /> : <Navigate to="/login" />} />
    </Routes>
  );
}

function App() {
  return (
    <Router>
      <AppRoutes />
    </Router>
  );
}

export default App;