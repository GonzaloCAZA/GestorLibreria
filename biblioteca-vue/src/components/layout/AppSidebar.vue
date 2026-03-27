<template>
  <aside class="fixed top-0 left-0 bottom-0 w-[260px] bg-ink flex flex-col z-50 overflow-y-auto">
    <!-- Grid lines decoration -->
    <div class="absolute inset-0 sidebar-grid pointer-events-none" />

    <!-- Logo -->
    <div class="relative px-6 py-7 border-b border-amber/20">
      <div class="font-display text-amber-light text-[1.3rem] font-bold leading-tight">
        📚 Bibliotheca
      </div>
      <div class="font-body italic text-paper-dark text-[0.78rem] mt-0.5 font-light">
        Sistema de gestión
      </div>
    </div>

    <!-- User info -->
    <div class="relative px-6 py-4 border-b border-white/5">
      <div class="text-white font-semibold text-[0.9rem] truncate">{{ auth.user?.mail }}</div>
      <span :class="['badge mt-1', auth.isAdmin ? 'badge-admin' : 'badge-customer']">
        {{ auth.isAdmin ? 'Admin' : 'Cliente' }}
      </span>
    </div>

    <!-- Navigation -->
    <nav class="relative flex-1 py-3">
      <template v-for="item in navItems" :key="item.id ?? item.label">
        <!-- Section label -->
        <div
          v-if="item.separator"
          class="px-6 pt-5 pb-1.5 text-[0.65rem] font-semibold uppercase tracking-[0.12em] text-amber/50"
        >
          {{ item.label }}
        </div>

        <!-- Nav link -->
        <RouterLink
          v-else
          :to="item.path"
          class="flex items-center gap-3 px-6 py-2.5 text-[0.95rem] text-cream/60 border-l-[3px] border-transparent transition-all duration-150 hover:bg-amber/8 hover:text-amber-pale no-underline"
          active-class="!text-amber-light !bg-amber/12 !border-amber font-semibold"
        >
          <span class="w-5 text-center text-base shrink-0">{{ item.icon }}</span>
          {{ item.label }}
        </RouterLink>
      </template>
    </nav>

    <!-- Logout -->
    <div class="relative px-5 py-4 border-t border-white/5">
      <button @click="auth.logout()" class="btn btn-ghost btn-sm w-full">
        Cerrar sesión
      </button>
    </div>
  </aside>
</template>

<script setup>
import { computed } from 'vue'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()

const navItems = computed(() => {
  const items = []

  if (auth.isAdmin) {
    items.push({ separator: true, label: 'General' })
    items.push({ path: '/dashboard', icon: '◈', label: 'Panel de control' })
  }

  items.push({ separator: true, label: 'Catálogo' })
  items.push({ path: '/libros',  icon: '📖', label: 'Libros' })
  items.push({ path: '/autores', icon: '✍️',  label: 'Autores' })
  items.push({ path: '/salas',   icon: '🚪', label: 'Salas' })

  if (auth.isAdmin) {
    items.push({ path: '/pisos',       icon: '🏢', label: 'Pisos' })
    items.push({ path: '/estanterias', icon: '🗄️', label: 'Estanterías' })
    items.push({ path: '/baldas',      icon: '📐', label: 'Baldas' })

    items.push({ separator: true, label: 'Gestión' })
    items.push({ path: '/usuarios',  icon: '👥', label: 'Usuarios' })
    items.push({ path: '/prestamos', icon: '↩️', label: 'Préstamos' })
    items.push({ path: '/reservas',  icon: '📅', label: 'Reservas de sala' })
  }

  return items
})
</script>
