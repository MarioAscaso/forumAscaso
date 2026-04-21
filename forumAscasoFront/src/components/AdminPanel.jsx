import { useState, useEffect } from 'react';
import axios from 'axios';

function AdminPanel() {
  const [users, setUsers] = useState([]);
  const [rooms, setRooms] = useState([]);
  const [loading, setLoading] = useState(true);

  // Cargar datos al abrir el panel
  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const token = localStorage.getItem('token');
      const config = { headers: { Authorization: `Bearer ${token}` } };
      
      // Ajusta estas rutas a las que tengas en tu Backend
      const usersRes = await axios.get('http://localhost:8686/api/admin/users', config);
      const roomsRes = await axios.get('http://localhost:8686/api/rooms', config);
      
      setUsers(usersRes.data);
      setRooms(roomsRes.data);
      setLoading(false);
    } catch (err) {
      console.error("Error al cargar datos de admin:", err);
      setLoading(false);
    }
  };

  const assignModerator = async (roomId, userId) => {
    try {
      const token = localStorage.getItem('token');
      await axios.patch(`http://localhost:8686/api/rooms/${roomId}/assign-moderator`, 
        { moderatorId: userId },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      alert('Moderador asignado con éxito');
    } catch (err) {
      alert('Error: ' + (err.response?.data || 'No se pudo asignar'));
    }
  };

  if (loading) return <p>Cargando panel...</p>;

  return (
    <div style={{ padding: '20px' }}>
      <h1>Panel de SuperAdministrador</h1>

      <section>
        <h2>Asignar Moderadores a Salas</h2>
        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr>
              <th>Sala</th>
              <th>Acción</th>
            </tr>
          </thead>
          <tbody>
            {rooms.map(room => (
              <tr key={room.id} style={{ borderBottom: '1px solid #ccc' }}>
                <td>{room.name}</td>
                <td>
                  <select onChange={(e) => assignModerator(room.id, e.target.value)}>
                    <option value="">Seleccionar Moderador...</option>
                    {users.filter(u => u.role === 'MODERATOR').map(user => (
                      <option key={user.id} value={user.id}>{user.username}</option>
                    ))}
                  </select>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </section>
    </div>
  );
}

export default AdminPanel;