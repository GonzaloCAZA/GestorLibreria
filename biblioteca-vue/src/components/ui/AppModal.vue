<template>
  <Teleport to="body">
    <Transition name="modal">
      <div
        v-if="modelValue"
        class="fixed inset-0 z-[500] flex items-center justify-center p-5"
        style="background: rgba(26,18,8,0.65);"
        @click.self="$emit('update:modelValue', false)"
      >
        <div class="bg-cream border border-paper-dark w-full max-w-lg max-h-[90vh] overflow-y-auto shadow-modal">
          <!-- Header -->
          <div class="px-7 py-5 border-b border-paper-dark bg-ink flex items-center justify-between">
            <h3 class="font-display text-[1.05rem] text-amber-light">{{ title }}</h3>
            <button
              class="text-paper hover:text-amber-light transition-colors bg-transparent border-0 cursor-pointer text-xl leading-none"
              @click="$emit('update:modelValue', false)"
            >✕</button>
          </div>

          <!-- Body -->
          <div class="px-7 py-6">
            <slot />
          </div>

          <!-- Footer -->
          <div v-if="$slots.footer" class="px-7 py-4 border-t border-paper flex justify-end gap-2.5">
            <slot name="footer" />
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup>
defineProps({ modelValue: Boolean, title: String })
defineEmits(['update:modelValue'])
</script>

<style scoped>
.modal-enter-active, .modal-leave-active { transition: opacity 0.25s; }
.modal-enter-from, .modal-leave-to { opacity: 0; }
.modal-enter-active .bg-cream, .modal-leave-active .bg-cream {
  transition: transform 0.25s;
}
.modal-enter-from .bg-cream { transform: translateY(16px) scale(0.98); }
</style>
