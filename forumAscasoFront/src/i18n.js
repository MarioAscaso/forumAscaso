import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';

const resources = {
  es: {
    translation: {
      "nav_rooms": "Salas",
      "nav_admin": "⚙️ Panel Admin",
      "nav_logout": "Salir",
      "lang_button": "🇬🇧 Inglés",
      "fav_rooms": "⭐ Tus Salas Favoritas",
      "all_rooms": "🌐 Todas las salas",
      "explore_rooms": "🌐 Explorar más salas",
      "enter_room": "Entrar a la Sala",
      "moderated": "Moderada",
      "no_more_rooms": "No hay más salas disponibles.",
      "admin_title": "Panel de SuperAdministración",
      "create_room": "Crear Nueva Sala",
      "room_name": "Nombre de la sala",
      "description": "Descripción",
      "requires_mod": "¿Requiere moderación?",
      "btn_create": "Crear Sala",
      "assign_mod": "Asignar Moderador",
      "select_user": "Selecciona un Usuario...",
      "select_room": "Selecciona una Sala...",
      "btn_nominate": "Nombrar Moderador",
      "user_mgmt": "Gestión de Usuarios",
      "col_user": "Usuario",
      "col_status": "Estado / Rol",
      "col_actions": "Acciones",
      "btn_warning": "⚠️ Aviso",
      "btn_7days": "🚫 7 Días",
      "btn_expel": "💀 Expulsar",
      "status_banned": "BANNED ({{days}} días)",
      "write_msg": "Escribe un mensaje...",
      "btn_send": "Enviar",
      "no_msgs": "No hay mensajes aún. ¡Sé el primero en escribir!"
    }
  },
  en: {
    translation: {
      "nav_rooms": "Rooms",
      "nav_admin": "⚙️ Admin Panel",
      "nav_logout": "Logout",
      "lang_button": "🇪🇸 Spanish",
      "fav_rooms": "⭐ Your Favorite Rooms",
      "all_rooms": "🌐 All Rooms",
      "explore_rooms": "🌐 Explore More Rooms",
      "enter_room": "Enter Room",
      "moderated": "Moderated",
      "no_more_rooms": "No more rooms available.",
      "admin_title": "SuperAdmin Panel",
      "create_room": "Create New Room",
      "room_name": "Room Name",
      "description": "Description",
      "requires_mod": "Requires moderation?",
      "btn_create": "Create Room",
      "assign_mod": "Assign Moderator",
      "select_user": "Select User...",
      "select_room": "Select Room...",
      "btn_nominate": "Nominate Moderator",
      "user_mgmt": "User Management",
      "col_user": "User",
      "col_status": "Status / Role",
      "col_actions": "Actions",
      "btn_warning": "⚠️ Warning",
      "btn_7days": "🚫 7 Days",
      "btn_expel": "💀 Expel",
      "status_banned": "BANNED ({{days}} days)",
      "write_msg": "Write a message...",
      "btn_send": "Send",
      "no_msgs": "No messages yet. Be the first to write!"
    }
  }
};

i18n.use(initReactI18next).init({
  resources,
  lng: "es",
  fallbackLng: "es",
  interpolation: { escapeValue: false }
});

export default i18n;