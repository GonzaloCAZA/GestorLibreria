<template>
  <header class="sticky top-0 z-40 bg-cream border-b border-paper-dark h-[60px] px-8 flex items-center justify-between">
    <h1 class="font-display text-[1.35rem] font-semibold text-ink">{{ title }}</h1>
    <slot />
  </header>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const titles = {
  '/dashboard':   'Panel de control',
  '/libros':      'Libros',
  '/autores':     'Autores',
  '/salas':       'Salas de estudio',
  '/pisos':       'Pisos',
  '/estanterias': 'Estanterías',
  '/baldas':      'Baldas',
  '/usuarios':    'Usuarios',
  '/prestamos':   'Préstamos',
  '/reservas':    'Reservas de sala',
}

const title = computed(() => titles[route.path] ?? 'Bibliotheca')
</script>
