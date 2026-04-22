import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";

const RoomList = () => {
  const [rooms, setRooms] = useState([]);

  useEffect(() => {
    const fetchRooms = async () => {
      try {
        const config = { headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } };
        const res = await axios.get("http://localhost:8686/api/rooms", config);
        setRooms(res.data);
      } catch (err) {
        console.error("Error al cargar salas", err);
      }
    };
    fetchRooms();
  }, []);

  return (
    <div>
      <h2 className="mb-4">Salas de Discusión</h2>
      <div className="row">
        {rooms.map(room => (
          <div className="col-md-4 mb-4" key={room.id}>
            <div className="card h-100 shadow-sm hover-shadow transition">
              <div className="card-body">
                <h5 className="card-title d-flex justify-content-between align-items-center">
                  {room.name}
                  {room.isModerated && <span className="badge bg-warning text-dark" style={{fontSize: '0.6em'}}>Moderada</span>}
                </h5>
                <p className="card-text text-muted">{room.description || "Sin descripción"}</p>
              </div>
              <div className="card-footer bg-white border-top-0 pb-3">
                <Link to={`/room/${room.id}`} className="btn btn-outline-primary w-100">Entrar a la sala</Link>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
export default RoomList;