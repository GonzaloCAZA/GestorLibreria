// src/components/auth/MeSection.jsx
import { useState } from 'react';
import { getMe } from '../../api/auth';
import { useAuth } from '../../context/AuthContext';
import { useResponse } from '../../context/ResponseContext';
import Spinner from '../shared/Spinner';
import Alert from '../shared/Alert';

export default function MeSection() {
  const { user, setUser } = useAuth();
  const { setResponse } = useResponse();
  const [loading, setLoading] = useState(false);
  const [alert, setAlert] = useState(null);

  async function handleRefresh() {
    setLoading(true);
    setAlert(null);
    const result = await getMe();
    setResponse(result);
    setLoading(false);
    if (result.ok) {
      setUser(result.data);
      setAlert({ type: 'success', message: 'Datos actualizados' });
    } else {
      setAlert({ type: 'error', message: `Error ${result.status}: ${JSON.stringify(result.data)}` });
    }
  }

  return (
    <div className="resource-section">
      <h2 className="section-title">Mi cuenta — /api/auth/me</h2>
      {loading && <Spinner />}
      {alert && <Alert type={alert.type} message={alert.message} onClose={() => setAlert(null)} />}
      <div className="resource-block">
        <div className="me-card">
          {user ? (
            <table className="data-table">
              <tbody>
                {Object.entries(user).map(([k, v]) => (
                  <tr key={k}>
                    <td className="me-key">{k}</td>
                    <td>{typeof v === 'object' ? JSON.stringify(v) : String(v ?? '')}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          ) : (
            <p>No hay datos de usuario cargados.</p>
          )}
        </div>
        <button className="btn btn-secondary mt-sm" onClick={handleRefresh} disabled={loading}>
          🔄 Recargar /api/auth/me
        </button>
      </div>
    </div>
  );
}
