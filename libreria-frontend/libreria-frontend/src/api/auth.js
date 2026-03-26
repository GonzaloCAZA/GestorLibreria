// src/api/auth.js
import { apiGet, apiPost, parseResponse } from './client';
import { BASE_URL } from './config';

export async function login(mail, password) {
  const body = { mail, password };
  const res = await apiPost('/api/auth/login', body);
  return parseResponse(res, 'POST', `${BASE_URL}/api/auth/login`, body);
}

export async function register(mail, password, rol) {
  const body = { mail, password, rol };
  const res = await apiPost('/api/auth/register', body);
  return parseResponse(res, 'POST', `${BASE_URL}/api/auth/register`, body);
}

export async function logout() {
  const res = await apiPost('/api/auth/logout', {});
  return parseResponse(res, 'POST', `${BASE_URL}/api/auth/logout`, null);
}

export async function getMe() {
  const res = await apiGet('/api/auth/me');
  return parseResponse(res, 'GET', `${BASE_URL}/api/auth/me`, null);
}
