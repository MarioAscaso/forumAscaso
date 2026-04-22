import { useState, useEffect } from 'react';
import axios from 'axios';

function AdminPanel() {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [isModerated, setIsModerated] = useState(false);
  
  const [users, setUsers] = useState([]);
  const [rooms, setRooms] = useState([]);
  const [loading, setLoading] = useState(true);
  
  // NUEVO: Estado para los errores (adiós window.alert)
  const [errorMsg, setErrorMsg] = useState(null);
  const [successMsg, setSuccessMsg] = useState(null);

  const API_URL = 'http://localhost:8686/api';

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    setLoading(true);
    setErrorMsg(null);
    
    const token = localStorage.getItem('token');
    if (!token) {
        setErrorMsg("No tienes sesión iniciada o el token es inválido.");
        setLoading(false);
        return;
    }

    const config = { headers: { Authorization: `Bearer ${token}` } };

    try {
      const [uRes, rRes] = await Promise.all([
        axios.get(`${API_URL}/users`, config),
        axios.get(`${API_URL}/rooms`, config)
      ]);
      setUsers(uRes.data);
      setRooms(rRes.data);
    } catch (err) {
      console.error("Error cargando datos:", err);
      setErrorMsg("Error al cargar datos. Asegúrate de tener permisos de administrador.");
    } finally {
      setLoading(false);
    }
  };

  const handleCreateRoom = async (e) => {
    e.preventDefault();
    setErrorMsg(null);
    setSuccessMsg(null);
    
    const token = localStorage.getItem('token');
    try {
      await axios.post(`${API_URL}/rooms`, { name, description, isModerated }, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setSuccessMsg('Sala creada con éxito');
      setName(''); setDescription(''); setIsModerated(false);
      fetchData();
    } catch (err) { 
      setErrorMsg('Error al crear la sala.');
    }
  };

  const applySanction = async (userId, type, days, reason) => {
    if (!window.confirm(`¿Seguro que quieres aplicar ${type}?`)) return;
    setErrorMsg(null);
    setSuccessMsg(null);
    
    const token = localStorage.getItem('token');
    try {
      await axios.post(`${API_URL}/sanctions`, { userId, type, days, reason }, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setSuccessMsg('Sanción aplicada correctamente');
    } catch (err) { 
      setErrorMsg('Error al aplicar sanción.');
    }
  };

  if (loading) return <div className="container mt-5">Cargando panel de control...</div>;

  return (
    <div className="container mt-5">
      <h1>Panel de SuperAdministración</h1>

      {/* ALERTAS ESTÉTICAS DE BOOTSTRAP */}
      {errorMsg && (
        <div className="alert alert-danger alert-dismissible fade show" role="alert">
          {errorMsg}
          <button type="button" className="btn-close" onClick={() => setErrorMsg(null)}></button>
        </div>
      )}
      {successMsg && (
        <div className="alert alert-success alert-dismissible fade show" role="alert">
          {successMsg}
          <button type="button" className="btn-close" onClick={() => setSuccessMsg(null)}></button>
        </div>
      )}

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
              {users.length > 0 ? (
                users.map(u => (
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
                ))
              ) : (
                <tr><td colSpan="3" className="text-center">No se encontraron usuarios.</td></tr>
              )}
            </tbody>
          </table>
        </div>
      </section>
    </div>
  );
}

export default AdminPanel;