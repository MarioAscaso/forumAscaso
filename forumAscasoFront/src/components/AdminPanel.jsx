import { useState, useEffect } from 'react';
import axios from 'axios';
import { useTranslation } from "react-i18next"; // 🔥 Añadido

function AdminPanel() {
  const { t } = useTranslation(); // 🔥 Añadido
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
    } catch (err) { setErrorMsg("Error"); }
    finally { setLoading(false); }
  };

  const handleCreateRoom = async (e) => {
    e.preventDefault();
    try {
      await axios.post(`${API_URL}/rooms`, { name, description, isModerated }, {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
      });
      setSuccessMsg('OK');
      setName(''); setDescription(''); setIsModerated(false);
      fetchData();
    } catch (err) { setErrorMsg('Error'); }
  };

  const handleAssignModerator = async (e) => {
    e.preventDefault();
    try {
      await axios.patch(`${API_URL}/rooms/${selectedRoom}/assign-moderator`, { moderatorId: selectedUser }, {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
      });
      setSelectedUser(''); setSelectedRoom('');
      fetchData();
    } catch (err) { setErrorMsg('Error'); }
  };

  const renderRoleBadge = (u) => {
    const role = u.role?.toUpperCase() || 'PARTICIPANT';
    if (role === 'PERNABANNED') return <span className="badge bg-danger">💀 PERNABANNED</span>;
    if (u.banUntil && new Date(u.banUntil) > new Date()) {
      const days = Math.ceil((new Date(u.banUntil) - new Date()) / (1000 * 60 * 60 * 24));
      return <span className="badge bg-warning text-dark">{t('status_banned', { days })}</span>;
    }
    return <span className="badge bg-secondary">{role}</span>;
  };

  if (loading) return <div className="mt-5 text-center">...</div>;

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
                    <button className="btn btn-outline-warning">{t('btn_warning')}</button>
                    <button className="btn btn-outline-danger">{t('btn_7days')}</button>
                    <button className="btn btn-danger">{t('btn_expel')}</button>
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