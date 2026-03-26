// src/api/client.js
import { BASE_URL } from './config';

let csrfToken = null;

export async function fetchCsrf() {
  const res = await fetch(`${BASE_URL}/api/auth/csrf`, {
    credentials: 'include',
  });
  const data = await res.json();
  csrfToken = data.token;
  return csrfToken;
}

async function getCsrf() {
  if (!csrfToken) {
    await fetchCsrf();
  }
  return csrfToken;
}

function buildHeaders(extra = {}) {
  return {
    'Content-Type': 'application/json',
    ...extra,
  };
}

export async function apiGet(path) {
  const res = await fetch(`${BASE_URL}${path}`, {
    method: 'GET',
    credentials: 'include',
    headers: buildHeaders(),
  });
  return res;
}

export async function apiPost(path, body) {
  const token = await getCsrf();
  const res = await fetch(`${BASE_URL}${path}`, {
    method: 'POST',
    credentials: 'include',
    headers: buildHeaders({ 'X-XSRF-TOKEN': token }),
    body: JSON.stringify(body),
  });
  csrfToken = null; // invalidate after use
  return res;
}

export async function apiPut(path, body) {
  const token = await getCsrf();
  const res = await fetch(`${BASE_URL}${path}`, {
    method: 'PUT',
    credentials: 'include',
    headers: buildHeaders({ 'X-XSRF-TOKEN': token }),
    body: JSON.stringify(body),
  });
  csrfToken = null;
  return res;
}

export async function apiDelete(path) {
  const token = await getCsrf();
  const res = await fetch(`${BASE_URL}${path}`, {
    method: 'DELETE',
    credentials: 'include',
    headers: buildHeaders({ 'X-XSRF-TOKEN': token }),
  });
  csrfToken = null;
  return res;
}

export async function parseResponse(res, method, url, sentBody) {
  let json = null;
  const text = await res.text();
  try {
    json = text ? JSON.parse(text) : null;
  } catch {
    json = { raw: text };
  }
  return {
    url,
    method,
    sentBody,
    status: res.status,
    ok: res.ok,
    data: json,
  };
}
