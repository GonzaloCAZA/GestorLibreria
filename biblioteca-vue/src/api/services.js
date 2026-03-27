import api from './axios'

// ── AUTH ──────────────────────────────────────────────────
export const authApi = {
  me:             ()       => api.get('/api/auth/me'),
  login:          (body)   => api.post('/api/auth/login', body),
  register:       (body)   => api.post('/api/auth/register', body),
  logout:         ()       => api.post('/api/auth/logout'),
  forgotPassword: (body)   => api.post('/api/auth/forgot-password', body),
  resetPassword:  (body)   => api.post('/api/auth/reset-password', body),
}

// ── USUARIOS ──────────────────────────────────────────────
export const usuariosApi = {
  getAll:  (params) => api.get('/api/usuarios', { params }),
  getById: (id)     => api.get(`/api/usuarios/${id}`),
  create:  (body)   => api.post('/api/usuarios', body),
  update:  (id, body) => api.put(`/api/usuarios/${id}`, body),
  remove:  (id)     => api.delete(`/api/usuarios/${id}`),
}

// ── AUTORES ───────────────────────────────────────────────
export const autoresApi = {
  getAll:  (params) => api.get('/api/autores', { params }),
  getById: (id)     => api.get(`/api/autores/${id}`),
  create:  (body)   => api.post('/api/autores', body),
  update:  (id, body) => api.put(`/api/autores/${id}`, body),
  remove:  (id)     => api.delete(`/api/autores/${id}`),
}

// ── PISOS ─────────────────────────────────────────────────
export const pisosApi = {
  getAll:  ()       => api.get('/api/pisos'),
  getById: (id)     => api.get(`/api/pisos/${id}`),
  create:  (body)   => api.post('/api/pisos', body),
  update:  (id, body) => api.put(`/api/pisos/${id}`, body),
  remove:  (id)     => api.delete(`/api/pisos/${id}`),
}

// ── ESTANTERÍAS ───────────────────────────────────────────
export const estanteriasApi = {
  getAll:  ()       => api.get('/api/estanterias'),
  getById: (id)     => api.get(`/api/estanterias/${id}`),
  create:  (body)   => api.post('/api/estanterias', body),
  update:  (id, body) => api.put(`/api/estanterias/${id}`, body),
  remove:  (id)     => api.delete(`/api/estanterias/${id}`),
}

// ── BALDAS ────────────────────────────────────────────────
export const baldasApi = {
  getAll:  ()       => api.get('/api/baldas'),
  getById: (id)     => api.get(`/api/baldas/${id}`),
  create:  (body)   => api.post('/api/baldas', body),
  update:  (id, body) => api.put(`/api/baldas/${id}`, body),
  remove:  (id)     => api.delete(`/api/baldas/${id}`),
}

// ── LIBROS ────────────────────────────────────────────────
export const librosApi = {
  getAll:    (params) => api.get('/api/libros', { params }),
  getById:   (id)     => api.get(`/api/libros/${id}`),
  getByIsbn: (isbn)   => api.get(`/api/libros/isbn/${isbn}`),
  create:    (body)   => api.post('/api/libros', body),
  update:    (id, body) => api.put(`/api/libros/${id}`, body),
  remove:    (id)     => api.delete(`/api/libros/${id}`),
}

// ── SALAS ─────────────────────────────────────────────────
export const salasApi = {
  getAll:  ()       => api.get('/api/salas'),
  getById: (id)     => api.get(`/api/salas/${id}`),
  create:  (body)   => api.post('/api/salas', body),
  update:  (id, body) => api.put(`/api/salas/${id}`, body),
  remove:  (id)     => api.delete(`/api/salas/${id}`),
}

// ── PRÉSTAMOS ─────────────────────────────────────────────
export const prestamosApi = {
  getAll:  ()       => api.get('/api/prestamos'),
  getById: (id)     => api.get(`/api/prestamos/${id}`),
  create:  (body)   => api.post('/api/prestamos', body),
  update:  (id, body) => api.put(`/api/prestamos/${id}`, body),
  remove:  (id)     => api.delete(`/api/prestamos/${id}`),
}

// ── RESERVAS DE SALA ──────────────────────────────────────
export const reservasApi = {
  getAll:  ()       => api.get('/api/reservas-sala'),
  getById: (id)     => api.get(`/api/reservas-sala/${id}`),
  create:  (body)   => api.post('/api/reservas-sala', body),
  update:  (id, body) => api.put(`/api/reservas-sala/${id}`, body),
  remove:  (id)     => api.delete(`/api/reservas-sala/${id}`),
}
