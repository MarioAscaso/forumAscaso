import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useTranslation } from "react-i18next";
import api from "../api/roomApi"; // 🔥 Importamos tu API configurada

const RoomList = ({ user }) => {
  const { t } = useTranslation();
  const [rooms, setRooms] = useState([]);
  const [favoriteIds, setFavoriteIds] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => { fetchData(); }, [user]);

  const fetchData = async () => {
    setLoading(true);
    try {
      // 🔥 Peticiones súper limpias sin pasar headers manualmente
      const [roomsRes, favsRes] = await Promise.all([
        api.get("/rooms"),
        api.get("/users/me/favorites")
      ]);
      setRooms(roomsRes.data);
      setFavoriteIds(favsRes.data.map(f => f.id));
    } catch (error) { 
      console.error("Error cargando salas", error); 
    } finally { 
      setLoading(false); 
    }
  };

  const toggleFavorite = async (roomId) => {
    try {
      await api.post(`/users/me/favorites/${roomId}`);
      fetchData();
    } catch (error) { 
      console.error("Error al cambiar favorito", error); 
    }
  };

  if (loading) return <div className="text-center mt-5"><div className="spinner-border"></div></div>;

  const favoriteRooms = rooms.filter(r => favoriteIds.includes(r.id));
  const otherRooms = rooms.filter(r => !favoriteIds.includes(r.id));

  const RoomCard = ({ room, isFav }) => (
    <div className="col-md-4 mb-4" key={room.id}>
      <div className={`card h-100 shadow-sm border-0 ${isFav ? 'bg-light border-warning' : ''}`}>
        <div className="card-body">
          <div className="d-flex justify-content-between align-items-start">
            <h5 className="card-title fw-bold text-dark">{room.name}</h5>
            <button onClick={() => toggleFavorite(room.id)} className="btn btn-sm btn-link text-warning fs-5 p-0 text-decoration-none">
              {isFav ? '⭐' : '☆'}
            </button>
          </div>
          <p className="card-text text-muted">{room.description}</p>
          {room.isModerated && <span className="badge bg-info text-dark mb-3">{t('moderated')}</span>}
        </div>
        <div className="card-footer bg-white border-0 pb-3 pt-0">
          <Link to={`/rooms/${room.id}`} className="btn btn-dark w-100">{t('enter_room')}</Link>
        </div>
      </div>
    </div>
  );

  return (
    <div className="container mt-4">
      {favoriteRooms.length > 0 && (
        <div className="mb-5">
          <h3 className="fw-bold mb-3">{t('fav_rooms')}</h3>
          <div className="row">
            {favoriteRooms.map(room => <RoomCard key={room.id} room={room} isFav={true} />)}
          </div>
        </div>
      )}
      <div>
        <h3 className="fw-bold mb-3">
          {favoriteRooms.length > 0 ? t('explore_rooms') : t('all_rooms')}
        </h3>
        <div className="row">
          {otherRooms.map(room => <RoomCard key={room.id} room={room} isFav={false} />)}
        </div>
        {otherRooms.length === 0 && <p className="text-muted">{t('no_more_rooms')}</p>}
      </div>
    </div>
  );
};

export default RoomList;