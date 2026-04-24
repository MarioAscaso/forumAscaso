import { useState } from "react";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";
import Swal from "sweetalert2";

const Login = ({ setUser }) => {
  const [formData, setFormData] = useState({ email: "", password: "" });
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);

    try {
      // Petición al backend para iniciar sesión
      const res = await axios.post("http://localhost:8686/api/auth/login", formData);
      const { token, role, username } = res.data; 
      
      const userEmail = formData.email;
      const userRole = role || "PARTICIPANT"; 
      const userUsername = username || userEmail.split('@')[0];

      // Guardamos la sesión en el almacenamiento local
      localStorage.setItem("token", token);
      localStorage.setItem("role", userRole);
      localStorage.setItem("email", userEmail);
      localStorage.setItem("username", userUsername);

      // --- LIMPIEZA DE PANTALLA OSCURA (Backdrop) ---
      // Eliminamos cualquier rastro de capas de Bootstrap o modales antes de cambiar de vista
      document.body.classList.remove('modal-open');
      document.body.style.overflow = 'auto';
      document.body.style.paddingRight = '0px';
      document.querySelectorAll('.modal-backdrop').forEach(el => el.remove());

      // Actualizamos el estado global del usuario en App.jsx
      setUser({
        token,
        role: userRole,
        email: userEmail,
        username: userUsername
      });

      // Navegamos a la home sin recargar la página (SPA)
      navigate("/"); 
      
    } catch (err) {
      // Alerta elegante con SweetAlert2 en lugar del alert nativo
      Swal.fire({
        title: 'Error al acceder',
        text: 'Revisa tu correo o contraseña.',
        icon: 'error',
        confirmButtonText: 'Reintentar',
        confirmButtonColor: '#212529'
      });
    } finally {
      setIsLoading(false);
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
                <input 
                  type="email" 
                  className="form-control" 
                  placeholder="ejemplo@correo.com"
                  required 
                  onChange={e => setFormData({...formData, email: e.target.value})} 
                />
              </div>
              <div className="mb-4">
                <label className="form-label">Contraseña</label>
                <input 
                  type="password" 
                  className="form-control" 
                  placeholder="********"
                  required 
                  onChange={e => setFormData({...formData, password: e.target.value})} 
                />
              </div>
              <button 
                type="submit" 
                className="btn btn-dark w-100 py-2" 
                disabled={isLoading}
              >
                {isLoading ? (
                  <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                ) : (
                  "Entrar"
                )}
              </button>
            </form>
          </div>
          <div className="card-footer text-center py-3 bg-white border-0">
            <small className="text-muted">
              ¿No tienes cuenta? <Link to="/register" className="text-dark fw-bold">Regístrate aquí</Link>
            </small>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;