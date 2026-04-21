import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { fetchMessagesByRoom, createMessage } from '../api/roomApi'; // Importamos createMessage

function RoomDetail() {
  const { id } = useParams();
  const [messages, setMessages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  // Nuevo estado para el mensaje que estamos escribiendo
  const [newMessage, setNewMessage] = useState('');
  const [sending, setSending] = useState(false);

  // Función para cargar los mensajes (la sacamos fuera para poder reutilizarla)
  const loadMessages = () => {
    fetchMessagesByRoom(id)
      .then(res => {
        setMessages(res.data);
        setLoading(false);
      })
      .catch(err => {
        console.error("Error al cargar mensajes:", err);
        setError("No se pudieron cargar los mensajes.");
        setLoading(false);
      });
  };

  useEffect(() => {
    loadMessages();
  }, [id]);

  // Función para enviar el mensaje nuevo
  const handleSendMessage = async (e) => {
    e.preventDefault();
    if (!newMessage.trim()) return; // No enviar mensajes vacíos

    setSending(true);
    try {
      await createMessage(id, newMessage);
      setNewMessage(''); // Limpiamos la caja de texto
      loadMessages(); // Recargamos los mensajes para ver el nuestro
    } catch (err) {
      console.error("Error al enviar el mensaje:", err);
      alert("Hubo un error al enviar el mensaje.");
    } finally {
      setSending(false);
    }
  };

  return (
    <div style={{ maxWidth: '800px', margin: 'auto', padding: '20px' }}>
      <Link to="/">
        <button style={{ marginBottom: '20px', cursor: 'pointer' }}>← Volver a las salas</button>
      </Link>
      
      <h2>Sala de Chat</h2>

      {/* Zona de Mensajes */}
      <div style={{ border: '1px solid #ccc', borderRadius: '5px', padding: '20px', minHeight: '300px', backgroundColor: '#f9f9f9', marginBottom: '20px', maxHeight: '500px', overflowY: 'auto' }}>
        {loading && <p>Cargando mensajes...</p>}
        {error && <p style={{ color: 'red' }}>{error}</p>}
        
        {!loading && !error && messages.length === 0 && (
          <p style={{ fontStyle: 'italic', color: '#666' }}>No hay mensajes en esta sala todavía. ¡Sé el primero en escribir!</p>
        )}

        {!loading && messages.map(msg => (
          <div key={msg.id} style={{ 
            backgroundColor: 'white', 
            padding: '10px 15px', 
            borderRadius: '8px', 
            marginBottom: '10px',
            boxShadow: '0 1px 3px rgba(0,0,0,0.1)'
          }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '5px' }}>
              <strong style={{ color: '#0056b3' }}>@{msg.authorUsername}</strong>
              <span style={{ fontSize: '0.8em', color: '#888' }}>
                {new Date(msg.creationDate).toLocaleString()}
              </span>
            </div>
            <p style={{ margin: 0 }}>{msg.content}</p>
          </div>
        ))}
      </div>

      {/* Formulario para Enviar Nuevo Mensaje */}
      <form onSubmit={handleSendMessage} style={{ display: 'flex', gap: '10px' }}>
        <input 
          type="text" 
          value={newMessage}
          onChange={(e) => setNewMessage(e.target.value)}
          placeholder="Escribe tu mensaje aquí..." 
          style={{ flexGrow: 1, padding: '10px', borderRadius: '5px', border: '1px solid #ccc' }}
          disabled={sending}
        />
        <button type="submit" disabled={sending || !newMessage.trim()} style={{ padding: '10px 20px', cursor: 'pointer' }}>
          {sending ? 'Enviando...' : 'Enviar'}
        </button>
      </form>

    </div>
  );
}

export default RoomDetail;