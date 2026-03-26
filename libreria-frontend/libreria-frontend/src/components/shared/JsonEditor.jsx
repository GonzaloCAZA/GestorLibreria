// src/components/shared/JsonEditor.jsx
import { useState, useEffect } from 'react';

export default function JsonEditor({ value, onChange, label = 'Body JSON' }) {
  const [text, setText] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    setText(JSON.stringify(value, null, 2));
  }, []);

  function handleChange(e) {
    const raw = e.target.value;
    setText(raw);
    try {
      const parsed = JSON.parse(raw);
      setError('');
      onChange(parsed);
    } catch {
      setError('JSON inválido');
    }
  }

  return (
    <div className="json-editor">
      <label className="field-label">{label}</label>
      <textarea
        className={`json-textarea ${error ? 'json-error' : ''}`}
        value={text}
        onChange={handleChange}
        rows={10}
        spellCheck={false}
      />
      {error && <span className="json-error-msg">{error}</span>}
    </div>
  );
}
