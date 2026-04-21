import { useState } from 'react';
import axios from 'axios';

function Register({ onRegisterSuccess, onGoToLogin }) {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      // Hacemos el POST al endpoint de registro que tienes en Java
      await axios.post('http://localhost:8686/api/users/register', {
        username,
        email,
        password
      });
      
      alert("¡Registro exitoso! Ahora puedes iniciar sesión.");
      onRegisterSuccess(); // Volvemos a la pantalla de login
    } catch (err) {
      console.error(err);
      setError("Error al registrar el usuario. Revisa los datos o si el email ya existe.");
    }
  };

  return (
    <div style={{ maxWidth: '300px', margin: 'auto', textAlign: 'center' }}>
      <h2>Crear Cuenta</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <form onSubmit={handleRegister} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
        <input 
          type="text" 
          placeholder="Nombre de usuario" 
          onChange={(e) => setUsername(e.target.value)} 
          required 
        />
        <input 
          type="email" 
          placeholder="Email" 
          onChange={(e) => setEmail(e.target.value)} 
          required 
        />
        <input 
          type="password" 
          placeholder="Contraseña" 
          onChange={(e) => setPassword(e.target.value)} 
          required 
        />
        <button type="submit">Registrarse</button>
      </form>
      <p style={{ marginTop: '15px' }}>
        ¿Ya tienes cuenta? <button onClick={onGoToLogin}>Inicia Sesión</button>
      </p>
    </div>
  );
}

export default Register;