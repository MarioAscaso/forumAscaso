import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";

const RoomDetail = ({ user }) => {
  const { id } = useParams();
  const [messages, setMessages] = useState([]);
  const [pending, setPending] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  
  const config = { headers: { Authorization: `Bearer ${user.token}` } };
  const isModOrAdmin = user.role === 'MODERATOR' || user.role === 'SUPERADMIN';

  const loadMessages = async () => {
    try {
      const res = await axios.get(`http://localhost:8686/api/messages/room/${id}`, config);
      setMessages(res.data.filter(m => m.status === 'APPROVED'));
      setPending(res.data.filter(m => m.status === 'PENDING'));
    } catch (err) { console.error("Error cargando mensajes", err); }
  };

  useEffect(() => { loadMessages(); }, [id]);

  const handleSend = async (e) => {
    e.preventDefault();
    if (!newMessage.trim()) return;
    try {
      await axios.post(`http://localhost:8686/api/messages/room/${id}`, { content: newMessage }, config);
      setNewMessage("");
      loadMessages();
    } catch (err) { alert(err.response?.data || "No puedes enviar mensajes."); }
  };

  // Acciones de Moderador
  const moderateMessage = async (msgId, status) => {
    try {
      await axios.patch(`http://localhost:8686/api/messages/${msgId}/status?status=${status}`, {}, config);
      loadMessages();
    } catch (err) { alert("Error al moderar"); }
  };

  const applySanction = async (userId, type) => {
    const reason = prompt("Indica el motivo de la sanción:");
    if (!reason) return;
    try {
      await axios.post(`http://localhost:8686/api/sanctions`, { userId, type, reason, days: type === 'TEMPORARY_BAN' ? 7 : null }, config);
      alert("Sanción aplicada con éxito");
    } catch (err) { alert("Error al sancionar"); }
  };

  return (
    <div className="row">
      {/* VISTA DE MODERACIÓN (A la izquierda, solo visible para Admin/Mod) */}
      {isModOrAdmin && (
        <div className="col-md-4 mb-4">
          <div className="card border-warning shadow-sm">
            <div className="card-header bg-warning text-dark fw-bold">
              📥 Bandeja de Revisión ({pending.length})
            </div>
            <div className="card-body" style={{ maxHeight: '600px', overflowY: 'auto' }}>
              {pending.length === 0 ? <p className="text-muted small">No hay mensajes pendientes.</p> : 
                pending.map(m => (
                  <div key={m.id} className="border-bottom pb-3 mb-3">
                    <div className="d-flex justify-content-between mb-1">
                      <strong>{m.authorUsername}</strong>
                      <small className="text-muted">{new Date(m.createdAt).toLocaleTimeString()}</small>
                    </div>
                    <p className="small mb-2">{m.content}</p>
                    <div className="d-flex gap-1 mb-2">
                      <button className="btn btn-success btn-sm w-50" onClick={() => moderateMessage(m.id, 'APPROVED')}>Aprobar</button>
                      <button className="btn btn-danger btn-sm w-50" onClick={() => moderateMessage(m.id, 'REJECTED')}>Rechazar</button>
                    </div>
                    {/* Botones de sanción rápida */}
                    <div className="dropdown w-100">
                      <button className="btn btn-outline-dark btn-sm dropdown-toggle w-100" data-bs-toggle="dropdown">
                        ⚖️ Sancionar
                      </button>
                      <ul className="dropdown-menu w-100">
                        <li><button className="dropdown-item" onClick={() => applySanction(m.authorId, 'WARNING')}>⚠️ Enviar Aviso</button></li>
                        <li><button className="dropdown-item text-warning" onClick={() => applySanction(m.authorId, 'TEMPORARY_BAN')}>⛔ Banear 7 días</button></li>
                        <li><button className="dropdown-item text-danger fw-bold" onClick={() => applySanction(m.authorId, 'PERMANENT_BAN')}>💀 Baneo Permanente</button></li>
                      </ul>
                    </div>
                  </div>
                ))
              }
            </div>
          </div>
        </div>
      )}

      {/* MURO PÚBLICO (A la derecha, visible para todos) */}
      <div className={isModOrAdmin ? "col-md-8" : "col-md-12"}>
        <div className="card shadow-sm">
          <div className="card-header bg-white">
            <h4 className="mb-0">Muro de la Sala</h4>
          </div>
          <div className="card-body" style={{ height: '500px', overflowY: 'auto', backgroundColor: '#f8f9fa' }}>
            {messages.length === 0 ? <p className="text-center text-muted mt-5">No hay mensajes. ¡Sé el primero!</p> :
              messages.map(m => (
                <div key={m.id} className="d-flex mb-3">
                  <div className={`p-3 rounded shadow-sm ${m.authorUsername === user.username ? 'bg-primary text-white ms-auto' : 'bg-white border'}`} style={{ maxWidth: '75%' }}>
                    <div className="d-flex justify-content-between align-items-center mb-1 gap-3">
                      <small className={m.authorUsername === user.username ? 'text-light' : 'text-primary fw-bold'}>
                        {m.authorUsername}
                      </small>
                      <small style={{ fontSize: '0.7em', opacity: 0.7 }}>{new Date(m.createdAt).toLocaleTimeString()}</small>
                    </div>
                    <p className="mb-0">{m.content}</p>
                  </div>
                </div>
              ))
            }
          </div>
          <div className="card-footer bg-white">
            <form onSubmit={handleSend} className="d-flex gap-2">
              <input type="text" className="form-control" value={newMessage} onChange={e => setNewMessage(e.target.value)} placeholder="Escribe tu mensaje aquí..." />
              <button type="submit" className="btn btn-primary px-4">Enviar</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};
export default RoomDetail;