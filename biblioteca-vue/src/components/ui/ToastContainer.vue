<template>
  <Teleport to="body">
    <div class="fixed top-5 right-5 z-[9000] flex flex-col gap-2">
      <TransitionGroup name="toast">
        <div
          v-for="t in toast.toasts"
          :key="t.id"
          :class="[
            'px-4 py-3 font-body text-[0.95rem] shadow-modal max-w-xs',
            'border-l-4 bg-ink text-cream',
            t.type === 'success' ? 'border-teal' : t.type === 'error' ? 'border-danger' : 'border-amber'
          ]"
        >
          {{ t.message }}
        </div>
      </TransitionGroup>
    </div>
  </Teleport>
</template>

<script setup>
import { useToastStore } from '@/stores/toast'
const toast = useToastStore()
</script>

<style scoped>
.toast-enter-active { animation: toastIn 0.3s ease both; }
.toast-leave-active { transition: opacity 0.25s, transform 0.25s; }
.toast-leave-to { opacity: 0; transform: translateX(20px); }
@keyframes toastIn {
  from { opacity: 0; transform: translateX(20px); }
  to   { opacity: 1; transform: none; }
}
</style>
