// src/api/resources.js
import { apiGet, apiPost, apiPut, apiDelete, parseResponse } from './client';
import { BASE_URL } from './config';

function makeResource(basePath) {
  return {
    list: async (query = '') => {
      const url = `${basePath}${query ? '?' + query : ''}`;
      const res = await apiGet(url);
      return parseResponse(res, 'GET', `${BASE_URL}${url}`, null);
    },
    get: async (id) => {
      const url = `${basePath}/${id}`;
      const res = await apiGet(url);
      return parseResponse(res, 'GET', `${BASE_URL}${url}`, null);
    },
    create: async (body) => {
      const res = await apiPost(basePath, body);
      return parseResponse(res, 'POST', `${BASE_URL}${basePath}`, body);
    },
    update: async (id, body) => {
      const url = `${basePath}/${id}`;
      const res = await apiPut(url, body);
      return parseResponse(res, 'PUT', `${BASE_URL}${url}`, body);
    },
    remove: async (id) => {
      const url = `${basePath}/${id}`;
      const res = await apiDelete(url);
      return parseResponse(res, 'DELETE', `${BASE_URL}${url}`, null);
    },
  };
}

export const autoresApi = makeResource('/api/autores');
export const pisosApi = makeResource('/api/pisos');
export const estanteriasApi = makeResource('/api/estanterias');
export const baldasApi = makeResource('/api/baldas');
export const salasApi = makeResource('/api/salas');
export const usuariosApi = makeResource('/api/usuarios');
export const librosApi = makeResource('/api/libros');
export const prestamosApi = makeResource('/api/prestamos');
export const reservasSalaApi = makeResource('/api/reservas-sala');

export async function getLibroByIsbn(isbn) {
  const url = `/api/libros/isbn/${isbn}`;
  const res = await apiGet(url);
  return parseResponse(res, 'GET', `${BASE_URL}${url}`, null);
}

export async function getUsuarioByMail(mail) {
  const url = `/api/usuarios/mail/${encodeURIComponent(mail)}`;
  const res = await apiGet(url);
  return parseResponse(res, 'GET', `${BASE_URL}${url}`, null);
}
