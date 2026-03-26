// src/components/auth/RegisterForm.jsx
import { useState } from 'react';
import { register } from '../../api/auth';
import { useAuth } from '../../context/AuthContext';
import { useResponse } from '../../context/ResponseContext';
import Alert from '../shared/Alert';
import Spinner from '../shared/Spinner';

const ROLES = ['ROLE_CUSTOMER', 'ROLE_ADMIN', 'ROLE_DEV'];

export default function RegisterForm({ onSwitch }) {
  const { setUser } = useAuth();
  const { setResponse } = useResponse();
  const [mail, setMail] = useState('nuevo@demo.com');
  const [password, setPassword] = useState('Password123');
  const [rol, setRol] = useState('ROLE_CUSTOMER');
  const [loading, setLoading] = useState(false);
  const [alert, setAlert] = useState(null);

  async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);
    setAlert(null);
    const result = await register(mail, password, rol);
    setResponse(result);
    setLoading(false);
    if (result.ok) {
      setAlert({ type: 'success', message: 'Registro exitoso. Ahora puedes iniciar sesión.' });
      setUser(result.data);
    } else {
      setAlert({ type: 'error', message: `Error ${result.status}: ${JSON.stringify(result.data)}` });
    }
  }

  return (
    <div className="auth-card">
      <h2 className="auth-title">Crear cuenta</h2>
      {loading && <Spinner />}
      {alert && <Alert type={alert.type} message={alert.message} onClose={() => setAlert(null)} />}
      <form onSubmit={handleSubmit} className="auth-form">
        <label className="field-label">Email</label>
        <input className="field-input" type="email" value={mail} onChange={e => setMail(e.target.value)} required />
        <label className="field-label">Contraseña</label>
        <input className="field-input" type="password" value={password} onChange={e => setPassword(e.target.value)} required />
        <label className="field-label">Rol</label>
        <select className="field-input" value={rol} onChange={e => setRol(e.target.value)}>
          {ROLES.map(r => <option key={r} value={r}>{r}</option>)}
        </select>
        <button className="btn btn-primary full-width" type="submit" disabled={loading}>
          Registrarse
        </button>
      </form>
      <p className="auth-switch">
        ¿Ya tienes cuenta? <button className="link-btn" onClick={onSwitch}>Iniciar sesión</button>
      </p>
    </div>
  );
}
