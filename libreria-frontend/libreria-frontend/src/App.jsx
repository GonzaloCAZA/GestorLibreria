// src/App.jsx
import { useState, useEffect } from 'react';
import { useAuth } from './context/AuthContext';
import LoginForm from './components/auth/LoginForm';
import RegisterForm from './components/auth/RegisterForm';
import MeSection from './components/auth/MeSection';
import Sidebar from './components/layout/Sidebar';
import ResponsePanel from './components/shared/ResponsePanel';
import ResourceSection from './components/resources/ResourceSection';
import Spinner from './components/shared/Spinner';
import {
  autoresApi, pisosApi, estanteriasApi, baldasApi,
  salasApi, usuariosApi, librosApi, prestamosApi, reservasSalaApi,
  getLibroByIsbn, getUsuarioByMail,
} from './api/resources';

const DEFAULTS = {
  autores:   { nombre: 'Gabriel Garcia Marquez', fechaNacimiento: '1927-03-06', nacionalidad: 'Colombiana' },
  pisos:     { numPiso: 1, nombre: 'Planta Principal' },
  estanterias: { categoria: 'Novela', idPiso: { id: 1 } },
  baldas:    { numero: 3, idEstanteria: { id: 1 } },
  salas:     { nombre: 'Sala Estudio 1', maximoPersonas: 6, idPiso: { id: 1 } },
  usuarios:  { mail: 'cliente@demo.com', rol: 'ROLE_CUSTOMER', moroso: false, pwd: 'Password123' },
  libros:    { titulo: 'Cien Anos de Soledad', isbn: '1234567890123', idAutor: { id: 1 }, idBalda: { id: 1 } },
  prestamos: { idUsuario: { id: 1 }, idLibro: { id: 1 }, fechaPrestamo: '2026-03-19', fechaDevolucionPrevista: '2026-04-19', fechaDevolucionReal: null },
  reservas:  { idUsuario: { id: 1 }, idSala: { id: 1 }, fechaReserva: '2026-03-19T10:00:00Z', fechaFinReserva: '2026-03-19T12:00:00Z' },
};

const APIS = {
  autores: autoresApi,
  pisos: pisosApi,
  estanterias: estanteriasApi,
  baldas: baldasApi,
  salas: salasApi,
  usuarios: usuariosApi,
  libros: librosApi,
  prestamos: prestamosApi,
  reservas: reservasSalaApi,
};

function LibrosExtras({ handle, setDetail }) {
  const [isbn, setIsbn] = useState('');
  return (
    <div>
      <label className="field-label">Buscar por ISBN</label>
      <div className="field-row">
        <input className="field-input" placeholder="ISBN" value={isbn} onChange={e => setIsbn(e.target.value)} />
        <button className="btn btn-secondary" onClick={async () => {
          const result = await handle(() => getLibroByIsbn(isbn), 'GET ISBN');
          if (result && result.ok) setDetail(result.data);
        }}>GET /isbn/ISBN</button>
      </div>
    </div>
  );
}

function UsuariosExtras({ handle, setDetail }) {
  const [mail, setMail] = useState('');
  return (
    <div>
      <label className="field-label">Buscar por email</label>
      <div className="field-row">
        <input className="field-input" placeholder="email" value={mail} onChange={e => setMail(e.target.value)} />
        <button className="btn btn-secondary" onClick={async () => {
          const result = await handle(() => getUsuarioByMail(mail), 'GET mail');
          if (result && result.ok) setDetail(result.data);
        }}>GET /mail/MAIL</button>
      </div>
    </div>
  );
}

export default function App() {
  const { user, checkAuth, logout, checked } = useAuth();
  const [authView, setAuthView] = useState('login');
  const [activeSection, setActiveSection] = useState('me');

  useEffect(() => {
    checkAuth();
  }, []);

  async function handleLogout() {
    await logout();
  }

  if (!checked) {
    return (
      <div className="app-loading">
        <Spinner label="Verificando sesión..." />
      </div>
    );
  }

  if (!user) {
    return (
      <div className="auth-page">
        <div className="auth-left">
          <div className="auth-hero">
            <span className="hero-icon">&#x2B21;</span>
            <h1 className="hero-title">Librería<br />API Tester</h1>
            <p className="hero-sub">Panel de pruebas para la REST API de la librería</p>
          </div>
        </div>
        <div className="auth-right">
          <div className="response-side">
            <ResponsePanel />
          </div>
          {authView === 'login'
            ? <LoginForm onSwitch={() => setAuthView('register')} />
            : <RegisterForm onSwitch={() => setAuthView('login')} />
          }
        </div>
      </div>
    );
  }

  function renderSection() {
    if (activeSection === 'me') return <MeSection />;
    const api = APIS[activeSection];
    const def = DEFAULTS[activeSection];
    if (!api) return <p>Sección no encontrada</p>;

    let extraActions;
    if (activeSection === 'libros') extraActions = (props) => <LibrosExtras {...props} />;
    if (activeSection === 'usuarios') extraActions = (props) => <UsuariosExtras {...props} />;

    return (
      <ResourceSection
        key={activeSection}
        name={activeSection}
        api={api}
        defaultBody={def}
        extraActions={extraActions}
      />
    );
  }

  return (
    <div className="app-layout">
      <Sidebar
        active={activeSection}
        onChange={setActiveSection}
        user={user}
        onLogout={handleLogout}
      />
      <main className="main-content">
        <div className="content-area">
          <h2 className="section-title">{activeSection}</h2>
          {renderSection()}
        </div>
        <aside className="response-aside">
          <div className="response-aside-header">Panel de respuesta</div>
          <ResponsePanel />
        </aside>
      </main>
    </div>
  );
}
