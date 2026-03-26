// src/components/auth/LoginForm.jsx
import { useState } from 'react';
import { login } from '../../api/auth';
import { useAuth } from '../../context/AuthContext';
import { useResponse } from '../../context/ResponseContext';
import Alert from '../shared/Alert';
import Spinner from '../shared/Spinner';

export default function LoginForm({ onSwitch }) {
  const { setUser } = useAuth();
  const { setResponse } = useResponse();
  const [mail, setMail] = useState('usuario@demo.com');
  const [password, setPassword] = useState('Password123');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);
    setError('');
    const result = await login(mail, password);
    setResponse(result);
    setLoading(false);
    if (result.ok) {
      setUser(result.data);
    } else {
      setError(result.status === 401
        ? 'Credenciales incorrectas'
        : `Error ${result.status}: ${JSON.stringify(result.data)}`);
    }
  }

  return (
    <div className="auth-card">
      <h2 className="auth-title">Iniciar sesión</h2>
      {loading && <Spinner />}
      {error && <Alert message={error} onClose={() => setError('')} />}
      <form onSubmit={handleSubmit} className="auth-form">
        <label className="field-label">Email</label>
        <input className="field-input" type="email" value={mail} onChange={e => setMail(e.target.value)} required />
        <label className="field-label">Contraseña</label>
        <input className="field-input" type="password" value={password} onChange={e => setPassword(e.target.value)} required />
        <button className="btn btn-primary full-width" type="submit" disabled={loading}>
          Entrar
        </button>
      </form>
      <p className="auth-switch">
        ¿Sin cuenta? <button className="link-btn" onClick={onSwitch}>Registrarse</button>
      </p>
    </div>
  );
}
