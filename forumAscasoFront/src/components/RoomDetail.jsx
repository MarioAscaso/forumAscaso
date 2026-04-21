import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { fetchMessagesByRoom, createMessage, fetchPendingMessages, updateMessageStatus } from '../api/roomApi';

// NUEVO: Función para descifrar el JWT y sacar el rol
const getUserRole = () => {
  const token = localStorage.getItem('token');
  if (!token) return null;
  try {
    // Decodificamos la parte central del JWT (el payload)
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const payload = JSON.parse(window.atob(base64));
    
    console.log("Datos del token:", payload); // <-- Útil para ver qué datos te manda Java
    
    // Spring Security suele guardar el rol en "role", "roles" o "authorities"
    // Lo pasamos todo a texto para buscar fácilmente
    const rolesString = JSON.stringify(payload); 
    
    if (rolesString.includes('SUPERADMIN') || rolesString.includes('MODERATOR')) {
      return 'ADMIN_OR_MOD';
    }
    return 'PARTICIPANT';
  } catch (error) {
    console.error("Error al leer el token", error);
    return 'PARTICIPANT';
  }
};

function RoomDetail() {
  const { id } = useParams();
  const [messages, setMessages] = useState([]);
  const [pendingMessages, setPendingMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  
  // NUEVO: Guardamos el rol del usuario actual
  const userRole = getUserRole();
  const canModerate = userRole === 'ADMIN_OR_MOD';

  const loadMessages = () => {
    fetchMessagesByRoom(id).then(res => setMessages(res.data)).catch(console.error);
    
    // NUEVO: Solo pedimos los mensajes pendientes si el usuario tiene permisos
    if (canModerate) {
      fetchPendingMessages(id).then(res => setPendingMessages(res.data)).catch(console.error);
    }
  };

  useEffect(() => { loadMessages(); }, [id]);

  const handleSendMessage = async (e) => {
    e.preventDefault();
    if (!newMessage.trim()) return;
    try {
      await createMessage(id, newMessage);
      setNewMessage('');
      loadMessages(); 
      alert("Mensaje enviado. Si la sala es moderada, espera a su aprobación.");
    } catch (err) { console.error(err); }
  };

  const handleModerate = async (messageId, status) => {
    try {
      await updateMessageStatus(messageId, status);
      loadMessages(); 
    } catch (err) {
      console.error("Error al moderar:", err);
    }
  };

  return (
    <div style={{ maxWidth: '800px', margin: 'auto', padding: '20px' }}>
      <Link to="/"><button style={{ marginBottom: '20px' }}>← Volver</button></Link>
      <h2>Sala de Chat</h2>

      {/* NUEVO: Ocultamos toda esta bandeja si NO es moderador/admin */}
      {canModerate && pendingMessages.length > 0 && (
        <div style={{ backgroundColor: '#fff3cd', padding: '15px', borderRadius: '5px', marginBottom: '20px', border: '1px solid #ffe69c' }}>
          <h3 style={{ color: '#856404', marginTop: 0 }}>🛡️ Bandeja de Moderación</h3>
          {pendingMessages.map(msg => (
             <div key={msg.id} style={{ backgroundColor: 'white', padding: '10px', marginBottom: '10px', borderRadius: '5px' }}>
               <strong>@{msg.authorUsername}</strong>: {msg.content}
               <div style={{ marginTop: '10px', display: 'flex', gap: '10px' }}>
                 <button onClick={() => handleModerate(msg.id, 'APPROVED')} style={{ backgroundColor: '#28a745', color: 'white', cursor: 'pointer', padding: '5px 10px', border: 'none', borderRadius: '3px' }}>Aprobar ✅</button>
                 <button onClick={() => handleModerate(msg.id, 'REJECTED')} style={{ backgroundColor: '#dc3545', color: 'white', cursor: 'pointer', padding: '5px 10px', border: 'none', borderRadius: '3px' }}>Rechazar ❌</button>
               </div>
             </div>
          ))}
        </div>
      )}

      <div style={{ border: '1px solid #ccc', padding: '20px', minHeight: '300px', backgroundColor: '#f9f9f9', marginBottom: '20px' }}>
        {messages.map(msg => (
          <div key={msg.id} style={{ backgroundColor: 'white', padding: '10px', borderRadius: '8px', marginBottom: '10px' }}>
            <strong style={{ color: '#0056b3' }}>@{msg.authorUsername}</strong>
            <p style={{ margin: 0 }}>{msg.content}</p>
          </div>
        ))}
      </div>

      <form onSubmit={handleSendMessage} style={{ display: 'flex', gap: '10px' }}>
        <input type="text" value={newMessage} onChange={(e) => setNewMessage(e.target.value)} placeholder="Escribe..." style={{ flexGrow: 1, padding: '10px' }} />
        <button type="submit" style={{ padding: '10px 20px', cursor: 'pointer' }}>Enviar</button>
      </form>
    </div>
  );
}

export default RoomDetail;