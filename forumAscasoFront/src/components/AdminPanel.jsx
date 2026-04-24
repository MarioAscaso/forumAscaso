import { useState, useEffect } from 'react';
import axios from 'axios';

function AdminPanel() {
  const [users, setUsers] = useState([]);
  const [rooms, setRooms] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMsg, setErrorMsg] = useState(null);
  const [successMsg, setSuccessMsg] = useState(null);

  const API_URL = 'http://localhost:8686/api';

  useEffect(() => { fetchData(); }, []);

  const fetchData = async () => {
    setLoading(true);
    const token = localStorage.getItem('token');
    if (!token) return setLoading(false);
    try {
      const [uRes, rRes] = await Promise.all([
        axios.get(`${API_URL}/users`, { headers: { Authorization: `Bearer ${token}` } }),
        axios.get(`${API_URL}/rooms`, { headers: { Authorization: `Bearer ${token}` } })
      ]);
      setUsers(uRes.data); setRooms(rRes.data);
    } catch (err) { setErrorMsg("Error de carga."); }
    finally { setLoading(false); }
  };

  const handleBanUser = async (userId) => {
    if (!window.confirm("¿Expulsar PERMANENTEMENTE?")) return;
    try {
      await axios.patch(`${API_URL}/users/${userId}/ban`, {}, {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
      });
      setSuccessMsg('Usuario expulsado permanentemente.');
      fetchData();
    } catch (err) { setErrorMsg('Error al expulsar.'); }
  };

  const applySanction = async (userId, type, days, reason) => {
    try {
      await axios.post(`${API_URL}/sanctions`, { userId, type, days, reason }, {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
      });
      setSuccessMsg('Sanción aplicada');
      fetchData();
    } catch (err) { setErrorMsg('Error al sancionar.'); }
  };

  const renderRoleBadge = (u) => {
    const role = u.role?.toUpperCase() || 'PARTICIPANT';
    if (role === 'PERNABANNED') return <span className="badge bg-danger">💀 PERNABANNED</span>;
    
    if (u.banUntil && new Date(u.banUntil) > new Date()) {
      const days = Math.ceil((new Date(u.banUntil) - new Date()) / (1000 * 60 * 60 * 24));
      return <span className="badge bg-warning text-dark">🚫 BANNED ({days} días)</span>;
    }
    
    return <span className="badge bg-secondary">{role}</span>;
  };

  if (loading) return <div className="mt-5">Cargando...</div>;

  return (
    <div className="container mt-5">
      <h2 className="fw-bold mb-4">Panel de SuperAdministración</h2>
      {errorMsg && <div className="alert alert-danger">{errorMsg}</div>}
      {successMsg && <div className="alert alert-success">{successMsg}</div>}

      <div className="card shadow-sm">
        <table className="table align-middle mb-0">
          <thead className="table-dark">
            <tr><th>Usuario</th><th>Estado / Rol</th><th>Acciones</th></tr>
          </thead>
          <tbody>
            {users.map(u => {
              const role = u.role?.toUpperCase();
              const isBlocked = role === 'PERNABANNED' || (u.banUntil && new Date(u.banUntil) > new Date());

              return (
                <tr key={u.id} className={isBlocked ? 'table-light' : ''}>
                  <td><strong>{u.username}</strong><br/><small className="text-muted">{u.email}</small></td>
                  <td>{renderRoleBadge(u)}</td>
                  <td>
                    <div className="btn-group btn-group-sm">
                      <button className="btn btn-outline-warning" onClick={() => applySanction(u.id, 'WARNING', null, 'Conducta')} disabled={isBlocked}>⚠️ Aviso</button>
                      <button className="btn btn-outline-danger" onClick={() => applySanction(u.id, 'TEMPORARY_BAN', 7, 'Normas')} disabled={isBlocked}>🚫 7 Días</button>
                      <button className="btn btn-danger" onClick={() => handleBanUser(u.id)} disabled={isBlocked}>💀 Expulsar</button>
                    </div>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default AdminPanel;