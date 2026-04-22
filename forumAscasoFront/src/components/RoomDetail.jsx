import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";

const RoomDetail = ({ user }) => {
  const { id } = useParams();
  const [room, setRoom] = useState({});
  const [messages, setMessages] = useState([]);
  const [pending, setPending] = useState([]);
  const [mySanctions, setMySanctions] = useState([]);
  const [content, setContent] = useState("");
  
  const config = { headers: { Authorization: `Bearer ${user.token}` } };
  const isModOrAdmin = user.role === 'MODERATOR' || user.role === 'SUPERADMIN';

  const loadData = async () => {
    try {
      // Pedimos datos de la sala, los mensajes y las sanciones al mismo tiempo
      const [roomRes, msgRes, sancRes] = await Promise.all([
        axios.get(`http://localhost:8686/api/rooms`, config),
        axios.get(`http://localhost:8686/api/messages/room/${id}`, config),
        axios.get(`http://localhost:8686/api/sanctions/my-sanctions`, config).catch(() => ({ data: [] }))
      ]);
      
      const currentRoom = roomRes.data.find(r => r.id === parseInt(id));
      if(currentRoom) setRoom(currentRoom);

      setMessages(msgRes.data.filter(m => m.status === 'APPROVED'));
      setPending(msgRes.data.filter(m => m.status === 'PENDING'));
      setMySanctions(sancRes.data);
    } catch (err) { console.error("Error cargando datos de la sala", err); }
  };

  useEffect(() => { loadData(); }, [id]);

  const handleSend = async (e) => {
    e.preventDefault();
    if (!content.trim()) return;

    try {
      await axios.post(`http://localhost:8686/api/messages/room/${id}`, { content }, config);
      setContent(""); 
      loadData();
      
      // Alertas mejoradas dependiendo del rol y la sala
      if (room.isModerated && user.role === 'PARTICIPANT') {
          alert("📨 Tu mensaje ha sido enviado y será revisado por un moderador antes de publicarse.");
      } else {
          alert("✅ Mensaje publicado con éxito.");
      }

    } catch (err) {
      // Capturamos el error exacto de Spring Boot
      const serverError = err.response?.data;
      
      if (serverError && serverError.includes("bloqueada")) {
          alert(`🚫 ACCESO DENEGADO\n\n${serverError}\n\nSi crees que es un error, contacta con el administrador.`);
      } else {
          alert(`⚠️ Error al enviar: ${serverError || "Fallo de conexión"}`);
      }
    }
  };

  const moderate = async (msgId, status) => {
    try {
      await axios.patch(`http://localhost:8686/api/messages/${msgId}/status?status=${status}`, {}, config);
      loadData();
    } catch (err) { alert("Error al procesar la moderación"); }
  };

  const applySanction = async (userId, type) => {
    const reason = prompt("Indica el motivo de la infracción:");
    if (!reason) return;
    try {
      const days = type === 'TEMPORARY_BAN' ? 7 : null;
      await axios.post(`http://localhost:8686/api/sanctions`, { userId, type, reason, days }, config);
      alert("Sanción registrada correctamente en el sistema");
    } catch (err) { alert("Error al aplicar la sanción"); }
  };

  return (
    <div className="container mt-4">
      
      {/* Título de la Sala */}
      <div className="mb-4">
        <h2>{room.name || "Cargando..."}</h2>
        {room.isModerated && <span className="badge bg-warning text-dark shadow-sm">Sala Moderada</span>}
      </div>

      {/* AVISO VISUAL DE SANCIONES PARA EL USUARIO */}
      {mySanctions.length > 0 && (
        <div className="alert alert-danger shadow-sm border-danger border-2">
          <h5 className="alert-heading mb-2">⚠️ Tienes sanciones activas en tu cuenta:</h5>
          <ul className="mb-0">
            {mySanctions.map(s => <li key={s.id}><strong>{s.type}</strong>: {s.reason}</li>)}
          </ul>
        </div>
      )}

      <div className="row">
        {/* PANEL IZQUIERDO: BANDEJA DE MODERADOR */}
        {isModOrAdmin && pending.length > 0 && (
          <div className="col-md-4">
            <div className="card border-warning mb-4 shadow-sm">
              <div className="card-header bg-warning text-dark fw-bold">
                📥 Revisión Pendiente ({pending.length})
              </div>
              <div className="card-body bg-light" style={{ maxHeight: '500px', overflowY: 'auto' }}>
                {pending.map(m => (
                  <div key={m.id} className="border-bottom border-warning pb-3 mb-3">
                    <p className="small mb-2"><strong>{m.authorUsername}:</strong> {m.content}</p>
                    <div className="d-flex gap-1 mb-2">
                      <button className="btn btn-success btn-sm flex-grow-1" onClick={() => moderate(m.id, 'APPROVED')}>✅ Aprobar</button>
                      <button className="btn btn-danger btn-sm flex-grow-1" onClick={() => moderate(m.id, 'REJECTED')}>❌ Rechazar</button>
                    </div>
                    {/* Botón de Sanción con Dropdown */}
                    <div className="dropdown w-100">
                      <button className="btn btn-outline-dark btn-sm w-100 dropdown-toggle" data-bs-toggle="dropdown">
                        ⚖️ Sancionar Autor
                      </button>
                      <ul className="dropdown-menu w-100 shadow">
                        <li><button className="dropdown-item" onClick={() => applySanction(m.authorId, 'WARNING')}>⚠️ Enviar Aviso</button></li>
                        <li><button className="dropdown-item text-warning" onClick={() => applySanction(m.authorId, 'TEMPORARY_BAN')}>⛔ Banear 7 días</button></li>
                        <li><hr className="dropdown-divider" /></li>
                        <li><button className="dropdown-item text-danger fw-bold" onClick={() => applySanction(m.authorId, 'PERMANENT_BAN')}>💀 Expulsión Permanente</button></li>
                      </ul>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}

        {/* PANEL DERECHO: MURO PÚBLICO DE CHAT */}
        <div className={isModOrAdmin ? "col-md-8" : "col-md-12"}>
          <div className="card shadow-sm border-0">
            <div className="card-body" style={{ height: '500px', overflowY: 'auto', backgroundColor: '#f0f2f5' }}>
              {messages.length === 0 ? <p className="text-center text-muted mt-5">Nadie ha escrito todavía. ¡Rompe el hielo!</p> :
                messages.map(m => (
                <div key={m.id} className={`d-flex mb-3 ${m.authorUsername === user.username ? 'justify-content-end' : 'justify-content-start'}`}>
                  <div className={`p-3 rounded-3 shadow-sm ${m.authorUsername === user.username ? 'bg-primary text-white' : 'bg-white'}`} style={{ maxWidth: '80%' }}>
                    <div className="d-flex justify-content-between align-items-center mb-1 gap-3">
                      <small className={m.authorUsername === user.username ? 'text-light fw-bold' : 'text-primary fw-bold'}>
                        {m.authorUsername}
                      </small>
                    </div>
                    <p className="mb-0">{m.content}</p>
                  </div>
                </div>
              ))}
            </div>
            
            {/* Input de envío de mensaje */}
            <div className="card-footer bg-white border-top-0 p-3">
              <form onSubmit={handleSend} className="input-group input-group-lg shadow-sm">
                <input type="text" className="form-control border-end-0" value={content} onChange={e => setContent(e.target.value)} placeholder="Escribe un mensaje aquí..." required />
                <button type="submit" className="btn btn-primary px-4 fw-bold">Enviar 🚀</button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RoomDetail;