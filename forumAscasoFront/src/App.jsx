import { BrowserRouter as Router, Routes, Route, Navigate, Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { useTranslation } from "react-i18next";
import Login from "./components/Login";
import Register from "./components/Register";
import RoomList from "./components/RoomList";
import RoomDetail from "./components/RoomDetail";
import AdminPanel from "./components/AdminPanel";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';

function App() {
  const [user, setUser] = useState(null);
  const { t, i18n } = useTranslation();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      setUser({
        token,
        role: localStorage.getItem("role"),
        email: localStorage.getItem("email"),
        username: localStorage.getItem("username")
      });
    }
  }, []);

  const handleLogout = () => {
    localStorage.clear();
    setUser(null);
  };

  const toggleLanguage = () => {
    const newLang = i18n.language === 'es' ? 'en' : 'es';
    i18n.changeLanguage(newLang);
  };

  return (
    <Router>
      {user && (
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark mb-4 shadow">
          <div className="container">
            <Link className="navbar-brand fw-bold" to="/">AscasoForum</Link>
            <div className="navbar-nav ms-auto align-items-center">
              <Link className="nav-link" to="/">{t('nav_rooms')}</Link>
              {user.role === "SUPERADMIN" && (
                <Link className="nav-link text-warning fw-bold" to="/admin">{t('nav_admin')}</Link>
              )}
              <span className="navbar-text ms-3 me-3 text-white">
                <span className="badge bg-secondary me-2">{user.role}</span>
                {user.username}
              </span>
              <button className="btn btn-outline-danger btn-sm" onClick={handleLogout}>{t('nav_logout')}</button>
              <button className="btn btn-light btn-sm ms-2 fw-bold" onClick={toggleLanguage}>
                {t('lang_button')}
              </button>
            </div>
          </div>
        </nav>
      )}

      <div className="container">
        <Routes>
          <Route path="/login" element={!user ? <Login setUser={setUser} /> : <Navigate to="/" />} />
          <Route path="/register" element={!user ? <Register /> : <Navigate to="/" />} />
          <Route path="/" element={user ? <RoomList user={user} /> : <Navigate to="/login" />} />
          <Route path="/rooms/:id" element={user ? <RoomDetail user={user} /> : <Navigate to="/login" />} />
          <Route path="/admin" element={user?.role === "SUPERADMIN" ? <AdminPanel /> : <Navigate to="/" />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;