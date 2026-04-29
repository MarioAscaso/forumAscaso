import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import { useTranslation } from "react-i18next"; // 🔥 Añadido para el Multilingüe

const stringToColor = (str) => {
  if (!str) return '#000000';
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    hash = str.charCodeAt(i) + ((hash << 5) - hash);
  }
  let color = '#';
  for (let i = 0; i < 3; i++) {
    const value = (hash >> (i * 8)) & 0xFF;
    color += ('00' + value.toString(16)).slice(-2);
  }
  return color;
};

const RoomDetail = ({ user }) => {
  const { t } = useTranslation(); // 🔥 Iniciamos el traductor
  const { id } = useParams();
  const [room, setRoom] = useState(null);
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchRoomAndMessages();
  }, [id, user]);

  const fetchRoomAndMessages = async () => {
    const token = user?.token || localStorage.getItem("token");
    if (!token) return;

    try {
      const headers = { Authorization: `Bearer ${token}` };
      const [roomRes, msgRes] = await Promise.all([
        axios.get(`http://localhost:8686/api/rooms/${id}`, { headers }),
        axios.get(`http://localhost:8686/api/messages/room/${id}`, { headers })
      ]);
      
      setRoom(roomRes.data);
      setMessages(msgRes.data);
    } catch (error) {
      console.error("Error al cargar la sala", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSendMessage = async (e) => {
    e.preventDefault();
    const token = user?.token || localStorage.getItem("token");
    const email = user?.email || localStorage.getItem("email");
    
    if (!token || !email) return;

    try {
      await axios.post(`http://localhost:8686/api/messages`, {
        roomId: id,
        email: email,
        content: newMessage
      }, { headers: { Authorization: `Bearer ${token}` } });
      
      setNewMessage("");
      fetchRoomAndMessages();
    } catch (error) {
      console.error("Error al enviar", error);
    }
  };

  if (loading) return <div className="text-center mt-5"><div className="spinner-border"></div></div>;
  if (!room) return <div className="text-center mt-5">Sala no encontrada</div>;

  return (
    <div className="card shadow border-0 mt-4">
      <div className="card-header bg-dark text-white p-4">
        <h2>{room.name}</h2>
        <p className="mb-0 opacity-75">{room.description}</p>
      </div>
      
      <div className="card-body bg-light" style={{ height: "400px", overflowY: "auto" }}>
        {messages.length === 0 ? (
          <p className="text-muted text-center mt-4">{t('no_msgs')}</p> 
        ) : (
          messages.map((m) => {
            // 🔥 BÚSQUEDA INTELIGENTE: Busca el nombre en la raíz o dentro del objeto autor
            const authorName = m.username || m.author?.username || 'usuario';

            return (
              <div key={m.id} className="mb-3 p-2 bg-white rounded shadow-sm border">
                <strong style={{ color: stringToColor(authorName) }}>
                  @{authorName}:
                </strong>
                <span className="ms-2">{m.content}</span>
              </div>
            );
          })
        )}
      </div>

      <div className="card-footer bg-white p-3">
        <form onSubmit={handleSendMessage} className="d-flex gap-2">
          <input 
            className="form-control" 
            value={newMessage} 
            onChange={(e) => setNewMessage(e.target.value)} 
            placeholder={t('write_msg')} 
            required 
          />
          <button className="btn btn-dark px-4">{t('btn_send')}</button>
        </form>
      </div>
    </div>
  );
};

export default RoomDetail;