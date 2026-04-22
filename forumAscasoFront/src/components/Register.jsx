import { useState } from "react";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";

const Register = () => {
  const [formData, setFormData] = useState({ email: "", username: "", password: "" });
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post("http://localhost:8686/api/auth/register", formData);
      alert("¡Registro exitoso! Ya puedes iniciar sesión.");
      navigate("/login");
    } catch (err) {
      alert("Error en el registro: " + (err.response?.data || "Verifica los datos"));
    }
  };

  return (
    <div className="row justify-content-center mt-5">
      <div className="col-md-5">
        <div className="card shadow-lg border-0 rounded-lg">
          <div className="card-header bg-primary text-white text-center py-3">
            <h3 className="mb-0">Crear Cuenta</h3>
          </div>
          <div className="card-body p-4">
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label">Correo Electrónico</label>
                <input type="email" className="form-control" required onChange={e => setFormData({...formData, email: e.target.value})} />
              </div>
              <div className="mb-3">
                <label className="form-label">Nombre de Usuario</label>
                <input type="text" className="form-control" required onChange={e => setFormData({...formData, username: e.target.value})} />
              </div>
              <div className="mb-4">
                <label className="form-label">Contraseña</label>
                <input type="password" className="form-control" required minLength="6" onChange={e => setFormData({...formData, password: e.target.value})} />
              </div>
              <button type="submit" className="btn btn-primary w-100 py-2">Registrarse</button>
            </form>
          </div>
          <div className="card-footer text-center py-3">
            <small>¿Ya tienes cuenta? <Link to="/login">Inicia sesión aquí</Link></small>
          </div>
        </div>
      </div>
    </div>
  );
};
export default Register;