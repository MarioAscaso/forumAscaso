import { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, useNavigate } from 'react-router-dom';
import Login from './components/Login';
import Register from './components/Register';
import RoomList from './components/RoomList';
import RoomDetail from './components/RoomDetail';

function AppRoutes() {
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem('token'));
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token');
    setIsLoggedIn(false);
    navigate('/login'); // Al salir, lo mandamos al login
  };

  return (
    <Routes>
      {/* RUTAS PROTEGIDAS (Solo si estás logueado) */}
      <Route 
        path="/" 
        element={isLoggedIn ? <RoomList onLogout={handleLogout} /> : <Navigate to="/login" />} 
      />
      <Route 
        path="/room/:id" 
        element={isLoggedIn ? <RoomDetail /> : <Navigate to="/login" />} 
      />

      {/* RUTAS PÚBLICAS (Login y Registro) */}
      <Route 
        path="/login" 
        element={
          !isLoggedIn ? (
            <Login 
              onLoginSuccess={() => { setIsLoggedIn(true); navigate('/'); }} 
              onGoToRegister={() => navigate('/register')} 
            />
          ) : (
            <Navigate to="/" />
          )
        } 
      />
      <Route 
        path="/register" 
        element={
          !isLoggedIn ? (
            <Register 
              onRegisterSuccess={() => navigate('/login')} 
              onGoToLogin={() => navigate('/login')} 
            />
          ) : (
            <Navigate to="/" />
          )
        } 
      />
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