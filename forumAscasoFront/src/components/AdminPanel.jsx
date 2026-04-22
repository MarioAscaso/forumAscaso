import { useState, useEffect } from 'react';
import axios from 'axios';

function AdminPanel() {
  // Estados para creación de salas (Originales)
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [isModerated, setIsModerated] = useState(false);
  
  // Estados para gestión de usuarios (Nuevos)
  const [users, setUsers] = useState([]);
  const [rooms, setRooms] = useState([]);
  const [loading, setLoading] = useState(true);

  const API_URL = 'http://localhost:8686/api';
  const config = { headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } };

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [uRes, rRes] = await Promise.all([
        axios.get(`${API_URL}/admin/users`, config),
        axios.get(`${API_URL}/rooms`, config)
      ]);
      setUsers(uRes.data);
      setRooms(rRes.data);
      setLoading(false);
    } catch (err) {
      console.error("Error cargando datos:", err);
      setLoading(false);
    }
  };

  // Función original para crear salas
  const handleCreateRoom = async (e) => {
    e.preventDefault();
    try {
      await axios.post(`${API_URL}/rooms`, { name, description, isModerated }, config);
      alert('Sala creada con éxito');
      setName(''); setDescription(''); setIsModerated(false);
      fetchData();
    } catch (err) { alert('Error al crear sala'); }
  };

  // Nueva función para aplicar sanciones
  const applySanction = async (userId, type, days, reason) => {
    if (!window.confirm(`¿Seguro que quieres aplicar ${type}?`)) return;
    try {
      await axios.post(`${API_URL}/sanctions`, { userId, type, days, reason }, config);
      alert('Sanción aplicada correctamente');
    } catch (err) { alert('Error al aplicar sanción'); }
  };

  if (loading) return <div className="container mt-5">Cargando panel de control...</div>;

  return (
    <div className="container mt-5">
      <h1>Panel de SuperAdministración</h1>

      {/* SECCIÓN 1: CREAR SALAS (Original) */}
      <section className="card p-4 mb-5 shadow-sm">
        <h2>Crear Nueva Sala</h2>
        <form onSubmit={handleCreateRoom} className="mt-3">
          <div className="mb-3">
            <input className="form-control" placeholder="Nombre de la sala" value={name} onChange={e => setName(e.target.value)} required />
          </div>
          <div className="mb-3">
            <textarea className="form-control" placeholder="Descripción" value={description} onChange={e => setDescription(e.target.value)} />
          </div>
          <div className="form-check mb-3">
            <input className="form-check-input" type="checkbox" id="modCheck" checked={isModerated} onChange={e => setIsModerated(e.target.checked)} />
            <label className="form-check-label" htmlFor="modCheck">¿Requiere moderación de mensajes?</label>
          </div>
          <button type="submit" className="btn btn-primary">Crear Sala</button>
        </form>
      </section>

      {/* SECCIÓN 2: GESTIÓN DE USUARIOS Y SANCIONES (Nueva) */}
      <section className="card p-4 shadow-sm">
        <h2>Usuarios Registrados</h2>
        <div className="table-responsive mt-3">
          <table className="table table-hover">
            <thead className="table-light">
              <tr>
                <th>Username</th>
                <th>Rol Actual</th>
                <th>Acciones de Moderación</th>
              </tr>
            </thead>
            <tbody>
              {users.map(u => (
                <tr key={u.id}>
                  <td><strong>{u.username}</strong><br/><small className="text-muted">{u.email}</small></td>
                  <td><span className="badge bg-info text-dark">{u.role}</span></td>
                  <td>
                    <div className="btn-group btn-group-sm">
                      <button className="btn btn-warning" onClick={() => applySanction(u.id, 'WARNING', null, 'Conducta inapropiada')}>⚠️ Aviso</button>
                      <button className="btn btn-outline-danger" onClick={() => applySanction(u.id, 'TEMPORARY_BAN', 7, 'Incumplimiento de normas')}>🚫 7 Días</button>
                      <button className="btn btn-danger" onClick={() => applySanction(u.id, 'PERMANENT_BAN', null, 'Infracción muy grave')}>💀 Expulsar</button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>
    </div>
  );
}

export default AdminPanel;