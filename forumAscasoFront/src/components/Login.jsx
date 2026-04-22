import { useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";

const Login = ({ setUser }) => {
  const [formData, setFormData] = useState({ email: "", password: "" });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post("http://localhost:8686/api/auth/login", formData);
      const { token, role, username } = res.data; // Asumiendo que tu backend devuelve esto
      
      localStorage.setItem("token", token);
      localStorage.setItem("role", role || "PARTICIPANT"); // Fallback si no viene rol
      localStorage.setItem("email", formData.email);
      localStorage.setItem("username", username || formData.email.split('@')[0]);

      // Refrescamos la web para que App.jsx pille los datos
      window.location.href = "/"; 
    } catch (err) {
      alert("Error al iniciar sesión. Revisa tus credenciales.");
    }
  };

  return (
    <div className="row justify-content-center mt-5">
      <div className="col-md-5">
        <div className="card shadow-lg border-0 rounded-lg">
          <div className="card-header bg-dark text-white text-center py-3">
            <h3 className="mb-0">Iniciar Sesión</h3>
          </div>
          <div className="card-body p-4">
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label">Correo Electrónico</label>
                <input type="email" className="form-control" required onChange={e => setFormData({...formData, email: e.target.value})} />
              </div>
              <div className="mb-4">
                <label className="form-label">Contraseña</label>
                <input type="password" className="form-control" required onChange={e => setFormData({...formData, password: e.target.value})} />
              </div>
              <button type="submit" className="btn btn-dark w-100 py-2">Entrar</button>
            </form>
          </div>
          <div className="card-footer text-center py-3">
            <small>¿No tienes cuenta? <Link to="/register">Regístrate gratis</Link></small>
          </div>
        </div>
      </div>
    </div>
  );
};
export default Login;