<template>
  <div class="animate-fade-slide">
    <DataTable title="Autores" :columns="columns" :rows="filtered" :loading="loading">
      <template #actions>
        <input v-model="search" type="text" placeholder="Buscar…" class="field-input py-2 text-[0.92rem] w-52" />
        <button v-if="auth.isAdmin" class="btn btn-primary btn-sm" @click="openForm()">+ Nuevo autor</button>
      </template>

      <template #rowActions="{ row }" v-if="auth.isAdmin">
        <div class="flex gap-2">
          <button class="btn btn-ghost btn-sm" @click="openForm(row)">Editar</button>
          <button class="btn btn-danger btn-sm" @click="confirmRemove(row)">Eliminar</button>
        </div>
      </template>
    </DataTable>

    <AppModal v-model="showModal" :title="editing ? 'Editar autor' : 'Nuevo autor'">
      <div class="space-y-4">
        <div>
          <label class="field-label">Nombre completo *</label>
          <input v-model="form.nombre" type="text" class="field-input" />
          <p v-if="formErrors.nombre" class="field-error">{{ formErrors.nombre }}</p>
        </div>
        <div>
          <label class="field-label">Nacionalidad</label>
          <input v-model="form.nacionalidad" type="text" class="field-input" />
        </div>
        <div>
          <label class="field-label">Fecha de nacimiento</label>
          <input v-model="form.fechaNacimiento" type="date" class="field-input" />
        </div>
      </div>
      <template #footer>
        <button class="btn btn-ghost" @click="showModal = false">Cancelar</button>
        <button class="btn btn-primary" @click="save" :disabled="saving">{{ saving ? 'Guardando…' : 'Guardar' }}</button>
      </template>
    </AppModal>

    <ConfirmModal v-model="showConfirm" :label="toDelete?.nombre" @confirm="remove" />
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useToastStore } from '@/stores/toast'
import { autoresApi } from '@/api/services'
import DataTable from '@/components/ui/DataTable.vue'
import AppModal from '@/components/ui/AppModal.vue'
import ConfirmModal from '@/components/ui/ConfirmModal.vue'

const auth  = useAuthStore()
const toast = useToastStore()

const rows    = ref([])
const loading = ref(true)
const search  = ref('')
const saving  = ref(false)
const showModal   = ref(false)
const showConfirm = ref(false)
const editing     = ref(false)
const toDelete    = ref(null)

const form = reactive({ id: null, nombre: '', nacionalidad: '', fechaNacimiento: '' })
const formErrors = reactive({ nombre: '' })

const columns = [
  { key: 'id',              label: '#',            mono: true },
  { key: 'nombre',          label: 'Nombre' },
  { key: 'nacionalidad',    label: 'Nacionalidad' },
  { key: 'fechaNacimiento', label: 'Nacimiento',   mono: true },
]

const filtered = computed(() => {
  const q = search.value.toLowerCase()
  return rows.value.filter(r => r.nombre.toLowerCase().includes(q))
})

async function load() {
  loading.value = true
  try { rows.value = (await autoresApi.getAll()).data }
  finally { loading.value = false }
}

function openForm(row = null) {
  formErrors.nombre = ''
  if (row) {
    editing.value = true
    Object.assign(form, { id: row.id, nombre: row.nombre, nacionalidad: row.nacionalidad ?? '', fechaNacimiento: row.fechaNacimiento ?? '' })
  } else {
    editing.value = false
    Object.assign(form, { id: null, nombre: '', nacionalidad: '', fechaNacimiento: '' })
  }
  showModal.value = true
}

async function save() {
  saving.value = true
  formErrors.nombre = ''
  const body = { nombre: form.nombre, nacionalidad: form.nacionalidad, fechaNacimiento: form.fechaNacimiento || null }
  try {
    if (editing.value) await autoresApi.update(form.id, body)
    else               await autoresApi.create(body)
    toast.success('Autor guardado')
    showModal.value = false
    await load()
  } catch (e) {
    const d = e.response?.data?.details
    if (d) Object.assign(formErrors, d)
    else toast.error(e.response?.data?.message || 'Error')
  } finally { saving.value = false }
}

function confirmRemove(row) { toDelete.value = row; showConfirm.value = true }

async function remove() {
  try {
    await autoresApi.remove(toDelete.value.id)
    toast.success('Autor eliminado')
    showConfirm.value = false
    await load()
  } catch (e) { toast.error(e.response?.data?.message || 'Error') }
}

onMounted(load)
</script>
