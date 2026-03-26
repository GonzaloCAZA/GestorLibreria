// src/components/shared/Spinner.jsx
export default function Spinner({ label = 'Cargando...' }) {
  return (
    <div className="spinner-wrap">
      <div className="spinner" />
      <span className="spinner-label">{label}</span>
    </div>
  );
}
