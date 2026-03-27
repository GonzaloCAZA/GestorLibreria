<template>
  <div>
    <!-- Toast portal -->
    <ToastContainer />

    <!-- Auth pages: no layout -->
    <RouterView v-if="isPublicRoute" />

    <!-- App shell with sidebar -->
    <div v-else class="flex min-h-screen">
      <AppSidebar />
      <div class="flex-1 flex flex-col" style="margin-left: 260px;">
        <AppTopbar />
        <main class="flex-1 p-8 animate-fade-slide">
          <RouterView />
        </main>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import AppSidebar from '@/components/layout/AppSidebar.vue'
import AppTopbar from '@/components/layout/AppTopbar.vue'
import ToastContainer from '@/components/ui/ToastContainer.vue'
import router from '@/router'

const route = useRoute()
const auth  = useAuthStore()

const isPublicRoute = computed(() => route.meta.public)

// Listen for 401 events from axios interceptor
onMounted(() => {
  window.addEventListener('auth:unauthorized', () => {
    auth.clear()
    router.push('/login')
  })
})
</script>
