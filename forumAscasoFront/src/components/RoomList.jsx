import { useEffect, useState } from 'react';
import { fetchRooms } from '../api/roomApi';
import { Link } from 'react-router-dom'; // <--- 1. Importa Link aquí

function RoomList({ onLogout }) {
  const [rooms, setRooms] = useState([]);

  useEffect(() => {
    fetchRooms()
      .then(res => setRooms(res.data))
      .catch(err => console.error("Error al cargar salas:", err));
  }, []);

  return (
    <div style={{ padding: '20px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h1>Foro Ascaso - Salas</h1>
        <button onClick={onLogout}>Cerrar sesión</button>
      </div>
      
      {rooms.length === 0 ? <p>No hay salas disponibles.</p> : null}
      
      {rooms.map(room => (
        <div key={room.id} style={{ border: '1px solid #ccc', margin: '10px 0', padding: '15px', borderRadius: '5px' }}>
          <h3>{room.name}</h3>
          <p>{room.description}</p>
          
          {/* 2. Añade este Link para viajar a la sala */}
          <Link to={`/room/${room.id}`}>
            <button style={{ marginTop: '10px', cursor: 'pointer' }}>Entrar a la sala</button>
          </Link>
          
        </div>
      ))}
    </div>
  );
}

export default RoomList; // (Asegúrate de que exporta RoomList)