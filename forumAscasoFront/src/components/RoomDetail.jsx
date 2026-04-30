import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";
import api from "../api/roomApi";

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

// 🔥 Nueva función para detectar y resaltar menciones (@usuario) en el texto
const renderMessageContent = (text) => {
  if (!text) return null;
  
  // Expresión regular: busca cualquier palabra que empiece por @ seguida de letras o números
  const mentionRegex = /(@\w+)/g;
  const parts = text.split(mentionRegex);

  return parts.map((part, index) => {
    if (part.match(mentionRegex)) {
      // Si es una mención, la pintamos de azul (text-primary) y en negrita
      return (
        <strong key={index} className="text-primary">
          {part}
        </strong>
      );
    }
    // Si es texto normal, lo devolvemos tal cual
    return part;
  });
};

const RoomDetail = ({ user }) => {
  const { t } = useTranslation();
  const { id } = useParams();
  const [room, setRoom] = useState(null);
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchRoomAndMessages();
  }, [id, user]);

  const fetchRoomAndMessages = async () => {
    try {
      const [roomRes, msgRes] = await Promise.all([
        api.get(`/rooms/${id}`),
        api.get(`/messages/room/${id}`)
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
    const email = user?.email || localStorage.getItem("email");
    if (!email) return;

    try {
      await api.post(`/messages`, {
        roomId: id,
        email: email,
        content: newMessage
      });
      
      setNewMessage("");
      fetchRoomAndMessages();
    } catch (error) {
      console.error("Error al enviar", error);
    }
  };

  const handleModerateMessage = async (msgId) => {
    if (!window.confirm("¿Seguro que quieres denegar y eliminar este mensaje?")) return;
    
    try {
      await api.delete(`/messages/${msgId}`); 
      fetchRoomAndMessages(); 
    } catch (error) {
      console.error("Error al moderar el mensaje", error);
    }
  };

  if (loading) return <div className="text-center mt-5"><div className="spinner-border"></div></div>;
  if (!room) return <div className="text-center mt-5">Sala no encontrada</div>;

  const userRole = user?.role || localStorage.getItem("role");
  const userId = user?.id || Number(localStorage.getItem("userId"));

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
            const authorName = m.username || m.author?.username || 'usuario';

            const currentUserId = String(userId);
            const roomModId = String(room.moderatorId || room.moderator?.id);

            const isGlobalAdmin = userRole === "SUPERADMIN" || userRole === "ADMIN" || userRole === "MODERATOR";
            const isRoomModerator = currentUserId === roomModId;

            const canModerate = Boolean(isGlobalAdmin || isRoomModerator);

            return (
              <div key={m.id} className="mb-3 p-2 bg-white rounded shadow-sm border d-flex justify-content-between align-items-center">
                <div>
                  <strong style={{ color: stringToColor(authorName) }}>
                    @{authorName}:
                  </strong>
                  {/* 🔥 Aquí llamamos a la función para que parsee el contenido del mensaje */}
                  <span className="ms-2">{renderMessageContent(m.content)}</span>
                </div>

                {canModerate ? (
                  <div className="d-flex gap-2">
                    <button 
                      onClick={() => handleModerateMessage(m.id)}
                      className="btn btn-sm btn-danger"
                      title="Denegar y eliminar mensaje"
                    >
                      ❌ Denegar
                    </button>
                  </div>
                ) : null}
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