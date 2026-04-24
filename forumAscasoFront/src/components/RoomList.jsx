import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";

const RoomList = () => {
  const [rooms, setRooms] = useState([]);
  const [favorites, setFavorites] = useState([]); // Guardará los IDs
  const userEmail = localStorage.getItem('email');

  useEffect(() => {
    fetchRooms();
    if (userEmail) fetchFavorites();
  }, [userEmail]);

  const fetchRooms = async () => {
    try {
      const config = { headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } };
      const res = await axios.get("http://localhost:8686/api/rooms", config);
      setRooms(res.data);
    } catch (err) {
      console.error("Error al cargar salas", err);
    }
  };

  const fetchFavorites = async () => {
    try {
      const config = { headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } };
      const res = await axios.get(`http://localhost:8686/api/favorites/${userEmail}`, config);
      setFavorites(res.data);
    } catch (err) {
      console.error("Error al cargar favoritos", err);
    }
  };

  const toggleFavorite = async (roomId) => {
    try {
      const config = { headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } };
      await axios.post(`http://localhost:8686/api/favorites/${userEmail}/${roomId}`, {}, config);
      
      // Actualizamos estado visual al instante
      if (favorites.includes(roomId)) {
        setFavorites(favorites.filter(id => id !== roomId));
      } else {
        setFavorites([...favorites, roomId]);
      }
    } catch (err) {
      console.error("Error al cambiar favorito", err);
    }
  };

  const favoriteRooms = rooms.filter(room => favorites.includes(room.id));
  const otherRooms = rooms.filter(room => !favorites.includes(room.id));

  // Función auxiliar para renderizar la tarjeta
  const renderRoomCard = (room, isFav) => (
    <div className="col-md-4 mb-4" key={room.id}>
      <div className="card h-100 shadow-sm hover-shadow transition border-0 bg-light">
        <div className="card-body">
          <div className="d-flex justify-content-between align-items-start mb-2">
            <h5 className="card-title fw-bold mb-0">{room.name}</h5>
            <button 
              onClick={() => toggleFavorite(room.id)} 
              className={`btn btn-sm ${isFav ? 'btn-warning text-dark' : 'btn-outline-secondary'}`}
              title="Favorito"
            >
              {isFav ? '★' : '☆'}
            </button>
          </div>
          {room.isModerated && <span className="badge bg-danger mb-2">Bajo Moderación</span>}
          <p className="card-text text-muted">{room.description || "Sin descripción"}</p>
        </div>
        <div className="card-footer bg-transparent border-0 pb-3">
          <Link to={`/room/${room.id}`} className="btn btn-dark w-100">Entrar a la sala</Link>
        </div>
      </div>
    </div>
  );

  return (
    <div>
      {favoriteRooms.length > 0 && (
        <>
          <h2 className="mb-4 text-warning fw-bold">★ Mis Salas Favoritas</h2>
          <div className="row">{favoriteRooms.map(r => renderRoomCard(r, true))}</div>
          <hr className="my-5 opacity-25" />
        </>
      )}

      <h2 className="mb-4 fw-bold">Todas las Salas</h2>
      <div className="row">{otherRooms.map(r => renderRoomCard(r, false))}</div>
    </div>
  );
};
export default RoomList;