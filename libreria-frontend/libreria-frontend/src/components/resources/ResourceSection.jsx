// src/components/resources/ResourceSection.jsx
import { useState, useCallback } from 'react';
import Spinner from '../shared/Spinner';
import Alert from '../shared/Alert';
import JsonEditor from '../shared/JsonEditor';
import { useResponse } from '../../context/ResponseContext';

export default function ResourceSection({ name, api, defaultBody, extraActions }) {
  const { setResponse } = useResponse();
  const [loading, setLoading] = useState(false);
  const [alert, setAlert] = useState(null);
  const [items, setItems] = useState(null);
  const [detail, setDetail] = useState(null);
  const [formBody, setFormBody] = useState(defaultBody);
  const [editId, setEditId] = useState('');
  const [deleteId, setDeleteId] = useState('');
  const [detailId, setDetailId] = useState('');
  const [queryStr, setQueryStr] = useState('');

  const handle = useCallback(async (fn, label) => {
    setLoading(true);
    setAlert(null);
    try {
      const result = await fn();
      setResponse(result);
      if (!result.ok) {
        const msg = result.status === 401
          ? '401 No autenticado'
          : result.status === 403
          ? '403 Acceso denegado'
          : `Error ${result.status}: ${JSON.stringify(result.data)}`;
        setAlert({ type: 'error', message: msg });
      } else {
        setAlert({ type: 'success', message: `${label} completado — ${result.status}` });
      }
      return result;
    } finally {
      setLoading(false);
    }
  }, [setResponse]);

  async function handleList() {
    const result = await handle(() => api.list(queryStr), 'Listado');
    if (result?.ok) setItems(result.data);
  }

  async function handleGet() {
    const result = await handle(() => api.get(detailId), 'Detalle');
    if (result?.ok) setDetail(result.data);
  }

  async function handleCreate() {
    await handle(() => api.create(formBody), 'Creación');
  }

  async function handleUpdate() {
    await handle(() => api.update(editId, formBody), 'Actualización');
  }

  async function handleDelete() {
    await handle(() => api.remove(deleteId), 'Borrado');
  }

  return (
    <div className="resource-section">
      {loading && <Spinner label="Llamando a la API..." />}
      {alert && (
        <Alert
          type={alert.type}
          message={alert.message}
          onClose={() => setAlert(null)}
        />
      )}

      {/* LIST */}
      <div className="resource-block">
        <h3 className="block-title">Listar</h3>
        <div className="field-row">
          <input
            className="field-input"
            placeholder="Query string (ej: nombre=Gabriel)"
            value={queryStr}
            onChange={e => setQueryStr(e.target.value)}
          />
          <button className="btn btn-primary" onClick={handleList}>GET List</button>
        </div>
        {items !== null && (
          <div className="table-wrap">
            {Array.isArray(items) ? (
              items.length === 0 ? (
                <p className="no-items">Sin resultados</p>
              ) : (
                <table className="data-table">
                  <thead>
                    <tr>
                      {Object.keys(items[0]).map(k => (
                        <th key={k}>{k}</th>
                      ))}
                    </tr>
                  </thead>
                  <tbody>
                    {items.map((item, i) => (
                      <tr key={i}>
                        {Object.values(item).map((v, j) => (
                          <td key={j}>{typeof v === 'object' ? JSON.stringify(v) : String(v ?? '')}</td>
                        ))}
                      </tr>
                    ))}
                  </tbody>
                </table>
              )
            ) : (
              <pre className="response-code">{JSON.stringify(items, null, 2)}</pre>
            )}
          </div>
        )}
      </div>

      {/* DETAIL */}
      <div className="resource-block">
        <h3 className="block-title">Ver detalle por ID</h3>
        <div className="field-row">
          <input
            className="field-input"
            placeholder="ID"
            value={detailId}
            onChange={e => setDetailId(e.target.value)}
          />
          <button className="btn btn-secondary" onClick={handleGet}>GET /{'{id}'}</button>
        </div>
        {detail && (
          <pre className="response-code small">{JSON.stringify(detail, null, 2)}</pre>
        )}
      </div>

      {/* CREATE / EDIT */}
      <div className="resource-block">
        <h3 className="block-title">Crear / Editar</h3>
        <JsonEditor
          value={defaultBody}
          onChange={setFormBody}
          label="Body (editable)"
        />
        <div className="field-row mt-sm">
          <button className="btn btn-create" onClick={handleCreate}>POST — Crear</button>
        </div>
        <div className="field-row mt-sm">
          <input
            className="field-input"
            placeholder="ID para editar"
            value={editId}
            onChange={e => setEditId(e.target.value)}
          />
          <button className="btn btn-warning" onClick={handleUpdate}>PUT /{'{id}'}</button>
        </div>
      </div>

      {/* DELETE */}
      <div className="resource-block">
        <h3 className="block-title">Borrar</h3>
        <div className="field-row">
          <input
            className="field-input"
            placeholder="ID a borrar"
            value={deleteId}
            onChange={e => setDeleteId(e.target.value)}
          />
          <button className="btn btn-danger" onClick={handleDelete}>DELETE /{'{id}'}</button>
        </div>
      </div>

      {/* EXTRA ACTIONS */}
      {extraActions && (
        <div className="resource-block">
          <h3 className="block-title">Acciones adicionales</h3>
          {extraActions({ handle, setItems, setDetail })}
        </div>
      )}
    </div>
  );
}
