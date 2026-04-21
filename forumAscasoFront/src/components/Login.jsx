import { useState } from 'react';
import axios from 'axios';

function Login({ onLoginSuccess, onGoToRegister }) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8686/api/users/login', { email, password });
      localStorage.setItem('token', response.data); 
      onLoginSuccess();
    } catch (err) {
      alert("Error al iniciar sesión: Credenciales incorrectas");
    }
  };

  return (
    <div style={{ maxWidth: '300px', margin: 'auto', textAlign: 'center' }}>
      <h2>Iniciar Sesión</h2>
      <form onSubmit={handleLogin} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
        <input type="email" placeholder="Email" onChange={(e) => setEmail(e.target.value)} required />
        <input type="password" placeholder="Contraseña" onChange={(e) => setPassword(e.target.value)} required />
        <button type="submit">Entrar</button>
      </form>
      <p style={{ marginTop: '15px' }}>
        ¿No tienes cuenta? <button onClick={onGoToRegister}>Regístrate aquí</button>
      </p>
    </div>
  );
}

export default Login;