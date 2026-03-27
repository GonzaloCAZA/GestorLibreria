import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const routes = [
  { path: '/login',     component: () => import('@/views/LoginView.vue'),      meta: { public: true } },
  { path: '/dashboard', component: () => import('@/views/DashboardView.vue'),  meta: { adminOnly: true } },
  { path: '/libros',    component: () => import('@/views/LibrosView.vue') },
  { path: '/autores',   component: () => import('@/views/AutoresView.vue') },
  { path: '/salas',     component: () => import('@/views/SalasView.vue') },
  { path: '/pisos',         component: () => import('@/views/PisosView.vue'),        meta: { adminOnly: true } },
  { path: '/estanterias',   component: () => import('@/views/EstanteriasView.vue'),  meta: { adminOnly: true } },
  { path: '/baldas',        component: () => import('@/views/BaldasView.vue'),       meta: { adminOnly: true } },
  { path: '/usuarios',      component: () => import('@/views/UsuariosView.vue'),     meta: { adminOnly: true } },
  { path: '/prestamos',     component: () => import('@/views/PrestamosView.vue'),    meta: { adminOnly: true } },
  { path: '/reservas',      component: () => import('@/views/ReservasView.vue'),     meta: { adminOnly: true } },
  { path: '/:pathMatch(.*)*', redirect: '/libros' },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach(async (to) => {
  const auth = useAuthStore()

  // Try to rehydrate session on first load
  if (!auth.isAuthenticated && !to.meta.public) {
    try {
      await auth.fetchMe()
    } catch (_) {
      return '/login'
    }
  }

  if (to.meta.adminOnly && !auth.isAdmin) {
    return '/libros'
  }

  if (to.meta.public && auth.isAuthenticated) {
    return auth.isAdmin ? '/dashboard' : '/libros'
  }

  return true
})

export default router
