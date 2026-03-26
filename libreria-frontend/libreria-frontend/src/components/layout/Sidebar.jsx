// src/components/layout/Sidebar.jsx

const SECTIONS = [
  { id: 'me', label: '👤 Mi cuenta' },
  { id: 'autores', label: '✍️ Autores' },
  { id: 'pisos', label: '🏢 Pisos' },
  { id: 'estanterias', label: '📚 Estanterías' },
  { id: 'baldas', label: '🗄️ Baldas' },
  { id: 'salas', label: '🚪 Salas' },
  { id: 'usuarios', label: '👥 Usuarios' },
  { id: 'libros', label: '📖 Libros' },
  { id: 'prestamos', label: '🔖 Préstamos' },
  { id: 'reservas', label: '📅 Reservas Sala' },
];

export default function Sidebar({ active, onChange, user, onLogout }) {
  return (
    <aside className="sidebar">
      <div className="sidebar-brand">
        <span className="brand-icon">⬡</span>
        <span className="brand-text">Librería<br /><small>API Tester</small></span>
      </div>

      <div className="sidebar-user">
        <div className="user-dot" />
        <div className="user-info">
          <span className="user-mail">{user?.mail || '—'}</span>
          <span className="user-rol">{user?.rol || ''}</span>
        </div>
      </div>

      <nav className="sidebar-nav">
        {SECTIONS.map(s => (
          <button
            key={s.id}
            className={`nav-item ${active === s.id ? 'active' : ''}`}
            onClick={() => onChange(s.id)}
          >
            {s.label}
          </button>
        ))}
      </nav>

      <div className="sidebar-footer">
        <button className="btn btn-logout" onClick={onLogout}>Cerrar sesión</button>
      </div>
    </aside>
  );
}
