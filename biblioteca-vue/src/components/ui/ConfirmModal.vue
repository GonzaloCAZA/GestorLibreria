<template>
  <AppModal v-model="modelValue" title="Confirmar eliminación" @update:modelValue="$emit('update:modelValue', $event)">
    <p class="text-[1.05rem] leading-relaxed text-ink-light italic">
      ¿Seguro que deseas eliminar <strong class="text-ink not-italic">{{ label }}</strong>?
      Esta acción no se puede deshacer.
    </p>
    <template #footer>
      <button class="btn btn-ghost" @click="$emit('update:modelValue', false)">Cancelar</button>
      <button class="btn btn-danger" @click="$emit('confirm')">Sí, eliminar</button>
    </template>
  </AppModal>
</template>

<script setup>
import AppModal from './AppModal.vue'
defineProps({ modelValue: Boolean, label: String })
defineEmits(['update:modelValue', 'confirm'])
</script>
