import { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import { useTranslation } from "react-i18next"; // 🔥 Añadido

const RoomList = ({ user }) => {
  const { t } = useTranslation(); // 🔥 Añadido
  const [rooms, setRooms] = useState([]);
  const [favoriteIds, setFavoriteIds] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => { fetchData(); }, [user]);

  const fetchData = async () => {
    setLoading(true);
    const token = user?.token || localStorage.getItem("token");
    if (!token) { setLoading(false); return; }
    const headers = { Authorization: `Bearer ${token}` };
    try {
      const [roomsRes, favsRes] = await Promise.all([
        axios.get("http://localhost:8686/api/rooms", { headers }),
        axios.get("http://localhost:8686/api/users/me/favorites", { headers })
      ]);
      setRooms(roomsRes.data);
      setFavoriteIds(favsRes.data.map(f => f.id));
    } catch (error) { console.error("Error cargando salas", error); }
    finally { setLoading(false); }
  };

  const toggleFavorite = async (roomId) => {
    const token = user?.token || localStorage.getItem("token");
    if (!token) return;
    try {
      await axios.post(`http://localhost:8686/api/users/me/favorites/${roomId}`, {}, {
        headers: { Authorization: `Bearer ${token}` }
      });
      fetchData();
    } catch (error) { console.error("Error al cambiar favorito", error); }
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
          {room.isModerated && <span className="badge bg-info text-dark mb-3">{t('moderated')}</span>} {/* 🔥 Traducido */}
        </div>
        <div className="card-footer bg-white border-0 pb-3 pt-0">
          <Link to={`/rooms/${room.id}`} className="btn btn-dark w-100">{t('enter_room')}</Link> {/* 🔥 Traducido */}
        </div>
      </div>
    </div>
  );

  return (
    <div className="container mt-4">
      {favoriteRooms.length > 0 && (
        <div className="mb-5">
          <h3 className="fw-bold mb-3">{t('fav_rooms')}</h3> {/* 🔥 Traducido */}
          <div className="row">
            {favoriteRooms.map(room => <RoomCard key={room.id} room={room} isFav={true} />)}
          </div>
        </div>
      )}
      <div>
        <h3 className="fw-bold mb-3">
          {favoriteRooms.length > 0 ? t('explore_rooms') : t('all_rooms')} {/* 🔥 Traducido */}
        </h3>
        <div className="row">
          {otherRooms.map(room => <RoomCard key={room.id} room={room} isFav={false} />)}
        </div>
        {otherRooms.length === 0 && <p className="text-muted">{t('no_more_rooms')}</p>} {/* 🔥 Traducido */}
      </div>
    </div>
  );
};

export default RoomList;