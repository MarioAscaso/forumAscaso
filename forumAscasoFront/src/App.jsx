import { useEffect, useState } from 'react';
import { fetchRooms } from './api/roomApi';
import Login from './components/Login';

function App() {
  const [rooms, setRooms] = useState([]);
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem('token'));

  const loadRooms = () => {
    fetchRooms()
      .then(res => setRooms(res.data))
      .catch(err => console.error("Error al cargar salas:", err));
  };

  useEffect(() => {
    if (isLoggedIn) loadRooms();
  }, [isLoggedIn]);

  if (!isLoggedIn) {
    return <Login onLoginSuccess={() => setIsLoggedIn(true)} />;
  }

  return (
    <div>
      <h1>Foro Ascaso - Salas</h1>
      <button onClick={() => { localStorage.removeItem('token'); setIsLoggedIn(false); }}>Cerrar sesión</button>
      {rooms.map(room => (
        <div key={room.id} style={{ border: '1px solid #ccc', margin: '10px', padding: '10px' }}>
          <h3>{room.name}</h3>
          <p>{room.description}</p>
        </div>
      ))}
    </div>
  );
}

export default App;