import { BrowserRouter as Router, Routes, Route, Navigate, Link } from "react-router-dom";
import { useState, useEffect } from "react";
import Login from "./components/Login";
import Register from "./components/Register";
import RoomList from "./components/RoomList";
import RoomDetail from "./components/RoomDetail";
import AdminPanel from "./components/AdminPanel";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';

function App() {
  const [user, setUser] = useState(null);

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
    window.location.href = "/login";
  };

  return (
    <Router>
      {user && (
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark mb-4 shadow">
          <div className="container">
            <Link className="navbar-brand fw-bold" to="/">AscasoForum</Link>
            <div className="navbar-nav ms-auto align-items-center">
              <Link className="nav-link" to="/">Salas</Link>
              {user.role === "SUPERADMIN" && (
                <Link className="nav-link text-warning fw-bold" to="/admin">⚙️ Panel Admin</Link>
              )}
              <span className="navbar-text ms-3 me-3 text-white">
                <span className="badge bg-secondary me-2">{user.role}</span>
                {user.username}
              </span>
              <button className="btn btn-outline-danger btn-sm" onClick={handleLogout}>Salir</button>
            </div>
          </div>
        </nav>
      )}

      <div className="container">
        <Routes>
          <Route path="/login" element={!user ? <Login setUser={setUser} /> : <Navigate to="/" />} />
          <Route path="/register" element={!user ? <Register /> : <Navigate to="/" />} />
          <Route path="/" element={user ? <RoomList /> : <Navigate to="/login" />} />
          <Route path="/room/:id" element={user ? <RoomDetail user={user} /> : <Navigate to="/login" />} />
          <Route path="/admin" element={user?.role === "SUPERADMIN" ? <AdminPanel /> : <Navigate to="/" />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;