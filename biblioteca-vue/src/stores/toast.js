import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useToastStore = defineStore('toast', () => {
  const toasts = ref([])
  let _id = 0

  function add(message, type = 'info', duration = 3500) {
    const id = ++_id
    toasts.value.push({ id, message, type })
    setTimeout(() => remove(id), duration)
  }

  function remove(id) {
    toasts.value = toasts.value.filter(t => t.id !== id)
  }

  const success = (msg) => add(msg, 'success')
  const error   = (msg) => add(msg, 'error')
  const info    = (msg) => add(msg, 'info')

  return { toasts, add, remove, success, error, info }
})
