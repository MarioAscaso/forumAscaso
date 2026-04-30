import { useState, useEffect } from 'react';
import { useTranslation } from "react-i18next";
import api from '../api/roomApi'; // 🔥 Importamos tu API configurada

function AdminPanel() {
  const { t } = useTranslation();
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [isModerated, setIsModerated] = useState(false);
  const [selectedUser, setSelectedUser] = useState('');
  const [selectedRoom, setSelectedRoom] = useState('');
  const [users, setUsers] = useState([]);
  const [rooms, setRooms] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMsg, setErrorMsg] = useState(null);
  const [successMsg, setSuccessMsg] = useState(null);

  useEffect(() => { fetchData(); }, []);

  const fetchData = async () => {
    setLoading(true);
    try {
      const [uRes, rRes] = await Promise.all([
        api.get('/users'),
        api.get('/rooms')
      ]);
      setUsers(uRes.data); 
      setRooms(rRes.data);
    } catch (err) { 
      setErrorMsg("Error cargando datos"); 
    } finally { 
      setLoading(false); 
    }
  };

  const handleCreateRoom = async (e) => {
    e.preventDefault();
    try {
      await api.post('/rooms', { name, description, isModerated });
      setSuccessMsg('OK');
      setName(''); setDescription(''); setIsModerated(false);
      fetchData();
    } catch (err) { setErrorMsg('Error al crear sala'); }
  };

  const handleAssignModerator = async (e) => {
    e.preventDefault();
    try {
      await api.patch(`/rooms/${selectedRoom}/assign-moderator`, { moderatorId: selectedUser });
      setSelectedUser(''); setSelectedRoom('');
      fetchData();
    } catch (err) { setErrorMsg('Error al asignar moderador'); }
  };

  // 🔥 Funciones preparadas para los botones de gestión
  const handleWarning = (userId) => console.log("Aviso para usuario:", userId);
  const handleBan = (userId) => console.log("Ban 7 días para usuario:", userId);
  const handleExpel = (userId) => console.log("Expulsar usuario:", userId);

  const renderRoleBadge = (u) => {
    const role = u.role?.toUpperCase() || 'PARTICIPANT';
    if (role === 'PERNABANNED') return <span className="badge bg-danger">💀 PERNABANNED</span>;
    if (u.banUntil && new Date(u.banUntil) > new Date()) {
      const days = Math.ceil((new Date(u.banUntil) - new Date()) / (1000 * 60 * 60 * 24));
      return <span className="badge bg-warning text-dark">{t('status_banned', { days })}</span>;
    }
    return <span className="badge bg-secondary">{role}</span>;
  };

  if (loading) return <div className="mt-5 text-center"><div className="spinner-border"></div></div>;

  return (
    <div className="container mt-5">
      <h2 className="fw-bold mb-4">{t('admin_title')}</h2>

      <div className="row mb-4">
        <div className="col-md-6 mb-3">
          <div className="card p-4 shadow-sm bg-light h-100 border-0">
            <h4 className="fw-bold">{t('create_room')}</h4>
            <form onSubmit={handleCreateRoom} className="mt-2">
              <input className="form-control mb-2" placeholder={t('room_name')} value={name} onChange={e => setName(e.target.value)} required />
              <textarea className="form-control mb-2" placeholder={t('description')} value={description} onChange={e => setDescription(e.target.value)} />
              <div className="form-check mb-3">
                <input className="form-check-input" type="checkbox" id="modCheck" checked={isModerated} onChange={e => setIsModerated(e.target.checked)} />
                <label className="form-check-label" htmlFor="modCheck">{t('requires_mod')}</label>
              </div>
              <button type="submit" className="btn btn-dark w-100">{t('btn_create')}</button>
            </form>
          </div>
        </div>

        <div className="col-md-6 mb-3">
          <div className="card p-4 shadow-sm bg-light h-100 border-0">
            <h4 className="fw-bold">{t('assign_mod')}</h4>
            <form onSubmit={handleAssignModerator} className="mt-2">
              <select className="form-select mb-3" value={selectedUser} onChange={e => setSelectedUser(e.target.value)} required>
                <option value="">{t('select_user')}</option>
                {users.map(u => <option key={u.id} value={u.id}>{u.username}</option>)}
              </select>
              <select className="form-select mb-3" value={selectedRoom} onChange={e => setSelectedRoom(e.target.value)} required>
                <option value="">{t('select_room')}</option>
                {rooms.map(r => <option key={r.id} value={r.id}>{r.name}</option>)}
              </select>
              <button type="submit" className="btn btn-warning w-100 fw-bold">{t('btn_nominate')}</button>
            </form>
          </div>
        </div>
      </div>

      <div className="card shadow-sm border-0">
        <div className="card-header bg-dark text-white p-3">
          <h5 className="mb-0">{t('user_mgmt')}</h5>
        </div>
        <table className="table align-middle mb-0">
          <thead className="table-light">
            <tr><th>{t('col_user')}</th><th>{t('col_status')}</th><th>{t('col_actions')}</th></tr>
          </thead>
          <tbody>
            {users.map(u => (
              <tr key={u.id}>
                <td><strong>{u.username}</strong></td>
                <td>{renderRoleBadge(u)}</td>
                <td>
                  <div className="btn-group btn-group-sm">
                    <button onClick={() => handleWarning(u.id)} className="btn btn-outline-warning">{t('btn_warning')}</button>
                    <button onClick={() => handleBan(u.id)} className="btn btn-outline-danger">{t('btn_7days')}</button>
                    <button onClick={() => handleExpel(u.id)} className="btn btn-danger">{t('btn_expel')}</button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default AdminPanel;