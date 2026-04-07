import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true,
})

// ── CSRF: fetch token and inject before mutating requests ──
async function getCsrfToken() {
  const { data } = await api.get('/api/auth/csrf')
  return data.token
}

api.interceptors.request.use(async (config) => {
  const method = config.method?.toUpperCase()
  if (!['GET', 'HEAD', 'OPTIONS'].includes(method)) {
    const token = await getCsrfToken()
    config.headers['X-XSRF-TOKEN'] = token
  }
  return config
})

// ── Global error interceptor ──
api.interceptors.response.use(
  (res) => res,
  (error) => {
    const status = error.response?.status
    if (status === 401) {
      // Let stores/components handle redirect
      window.dispatchEvent(new CustomEvent('auth:unauthorized'))
    }
    return Promise.reject(error)
  }
)

export default api
