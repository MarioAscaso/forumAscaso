import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";

const stringToColor = (str) => {
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
  const { id } = useParams();
  const [room, setRoom] = useState(null);
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("");

  useEffect(() => {
    fetchRoom();
    fetchMessages();
  }, [id]);

  const fetchRoom = async () => {
    const res = await axios.get(`http://localhost:8686/api/rooms/${id}`, {
      headers: { Authorization: `Bearer ${user.token}` }
    });
    setRoom(res.data);
  };

  const fetchMessages = async () => {
    const res = await axios.get(`http://localhost:8686/api/messages/room/${id}`, {
      headers: { Authorization: `Bearer ${user.token}` }
    });
    setMessages(res.data);
  };

  const handleSendMessage = async (e) => {
    e.preventDefault();
    await axios.post(`http://localhost:8686/api/messages`, {
      roomId: id,
      email: user.email,
      content: newMessage
    }, { headers: { Authorization: `Bearer ${user.token}` } });
    setNewMessage("");
    fetchMessages();
  };

  if (!room) return <div>Cargando...</div>;

  return (
    <div className="card shadow border-0">
      <div className="card-header bg-dark text-white p-4">
        <h2>{room.name}</h2>
        <p className="mb-0 opacity-75">{room.description}</p>
      </div>
      <div className="card-body" style={{ height: "400px", overflowY: "auto" }}>
        {messages.map((m) => (
          <div key={m.id} className="mb-3 p-2 bg-light rounded shadow-sm">
            <strong style={{ color: stringToColor(m.username || 'Anon') }}>
              @{m.username || 'usuario'}:
            </strong>
            <span className="ms-2">{m.content}</span>
          </div>
        ))}
      </div>
      <div className="card-footer bg-white p-3">
        <form onSubmit={handleSendMessage} className="d-flex gap-2">
          <input className="form-control" value={newMessage} onChange={(e) => setNewMessage(e.target.value)} placeholder="Escribe un mensaje..." required />
          <button className="btn btn-dark">Enviar</button>
        </form>
      </div>
    </div>
  );
};

export default RoomDetail;