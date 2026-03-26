// src/components/shared/ResponsePanel.jsx
import { useResponse } from '../../context/ResponseContext';

export default function ResponsePanel() {
  const { response } = useResponse();

  if (!response) {
    return (
      <div className="response-panel empty">
        <p className="response-empty-msg">Aquí aparecerá la respuesta de la API</p>
      </div>
    );
  }

  const statusClass = response.status >= 200 && response.status < 300
    ? 'status-ok'
    : response.status >= 400 && response.status < 500
    ? 'status-warn'
    : 'status-error';

  return (
    <div className="response-panel">
      <div className="response-meta">
        <span className={`status-badge ${statusClass}`}>{response.status}</span>
        <span className="method-badge">{response.method}</span>
        <span className="response-url">{response.url}</span>
      </div>

      {response.sentBody && (
        <div className="response-section">
          <div className="response-label">Body enviado</div>
          <pre className="response-code">{JSON.stringify(response.sentBody, null, 2)}</pre>
        </div>
      )}

      <div className="response-section">
        <div className="response-label">Respuesta JSON</div>
        <pre className="response-code">{JSON.stringify(response.data, null, 2)}</pre>
      </div>
    </div>
  );
}
