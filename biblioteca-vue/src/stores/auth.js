import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '@/api/services'
import router from '@/router'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const loading = ref(false)

  const isAuthenticated = computed(() => !!user.value)
  const isAdmin = computed(() => user.value?.rol === 'ROLE_ADMIN')

  async function fetchMe() {
    const { data } = await authApi.me()
    user.value = data
  }

  async function login(mail, password) {
    loading.value = true
    try {
      const { data } = await authApi.login({ mail, password })
      user.value = data
      router.push(isAdmin.value ? '/dashboard' : '/libros')
    } finally {
      loading.value = false
    }
  }

  async function register(mail, password) {
    loading.value = true
    try {
      const { data } = await authApi.register({ mail, password })
      user.value = data
      router.push('/libros')
    } finally {
      loading.value = false
    }
  }

  async function logout() {
    try { await authApi.logout() } catch (_) {}
    user.value = null
    router.push('/login')
  }

  function clear() {
    user.value = null
  }

  return { user, loading, isAuthenticated, isAdmin, fetchMe, login, register, logout, clear }
})
