<template>
  <div class="animate-fade-slide">
    <!-- Stats grid -->
    <div class="grid grid-cols-2 md:grid-cols-3 xl:grid-cols-6 gap-4 mb-8">
      <div v-for="s in stats" :key="s.label" :class="['stat-card', s.color]">
        <div class="text-[0.68rem] font-semibold uppercase tracking-[0.1em] text-ink-light mb-2">{{ s.label }}</div>
        <div class="font-display text-[2.2rem] font-bold text-ink leading-none">
          {{ loading ? '…' : s.value }}
        </div>
        <div class="absolute top-4 right-4 text-[1.5rem] opacity-10">{{ s.icon }}</div>
      </div>
    </div>

    <!-- Welcome -->
    <div class="bg-white border border-paper-dark shadow-card px-8 py-7">
      <h2 class="font-display text-[1.4rem] font-semibold text-ink mb-2">
        Bienvenido, <span class="italic text-amber">{{ auth.user?.mail }}</span>
      </h2>
      <p class="font-body text-ink-light leading-relaxed text-[1.05rem]">
        Usa el menú lateral para navegar entre secciones. Como administrador tienes acceso completo
        a la gestión del catálogo, usuarios, préstamos y reservas de sala.
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { librosApi, autoresApi, usuariosApi, prestamosApi, reservasApi } from '@/api/services'

const auth    = useAuthStore()
const loading = ref(true)

const stats = ref([
  { label: 'Libros',    icon: '📖', value: 0, color: '' },
  { label: 'Autores',   icon: '✍️',  value: 0, color: 'teal' },
  { label: 'Usuarios',  icon: '👥', value: 0, color: '' },
  { label: 'Morosos',   icon: '⚠️', value: 0, color: 'red' },
  { label: 'Préstamos', icon: '↩️', value: 0, color: 'teal' },
  { label: 'Reservas',  icon: '📅', value: 0, color: '' },
])

onMounted(async () => {
  try {
    const [libros, autores, usuarios, prestamos, reservas] = await Promise.all([
      librosApi.getAll(),
      autoresApi.getAll(),
      usuariosApi.getAll(),
      prestamosApi.getAll(),
      reservasApi.getAll(),
    ])
    stats.value[0].value = libros.data.length
    stats.value[1].value = autores.data.length
    stats.value[2].value = usuarios.data.length
    stats.value[3].value = usuarios.data.filter(u => u.moroso).length
    stats.value[4].value = prestamos.data.length
    stats.value[5].value = reservas.data.length
  } finally {
    loading.value = false
  }
})
</script>
