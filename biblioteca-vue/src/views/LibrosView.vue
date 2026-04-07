<template>
  <div class="animate-fade-slide">
    <DataTable title="Catálogo de libros" :columns="columns" :rows="filtered" :loading="loading">
      <template #actions>
        <div class="relative">
          <span class="absolute left-3 top-1/2 -translate-y-1/2 text-[0.8rem] pointer-events-none">🔍</span>
          <input v-model="search" type="text" placeholder="Buscar por título…"
            class="field-input pl-8 py-2 text-[0.92rem] w-56" />
        </div>
        <span class="text-[0.82rem] italic text-ink-light">{{ filtered.length }} libro(s)</span>
        <button v-if="auth.isAdmin" class="btn btn-primary btn-sm" @click="openForm()">+ Nuevo libro</button>
      </template>

      <template #filters>
        <!-- intentionally empty, search is in actions -->
      </template>

      <!-- Custom cells -->
      <template #cell-estanteriaCategoria="{ value }">
        <span class="badge badge-ok">{{ value ?? '—' }}</span>
      </template>

      <template #rowActions="{ row }" v-if="auth.isAdmin">
        <div class="flex gap-2">
          <button class="btn btn-ghost btn-sm" @click="openForm(row)">Editar</button>
          <button class="btn btn-danger btn-sm" @click="confirmRemove(row)">Eliminar</button>
        </div>
      </template>
    </DataTable>

    <!-- Form Modal -->
    <AppModal v-model="showModal" :title="editing ? 'Editar libro' : 'Nuevo libro'">
      <div class="space-y-4">
        <div>
          <label class="field-label">Título *</label>
          <input v-model="form.titulo" type="text" class="field-input" />
          <p v-if="formErrors.titulo" class="field-error">{{ formErrors.titulo }}</p>
        </div>
        <div>
          <label class="field-label">ISBN *</label>
          <input v-model="form.isbn" type="text" class="field-input font-mono" />
          <p v-if="formErrors.isbn" class="field-error">{{ formErrors.isbn }}</p>
        </div>
        <div>
          <label class="field-label">Autor</label>
          <select v-model="form.idAutor" class="field-input">
            <option v-for="a in autores" :key="a.id" :value="a.id">{{ a.nombre }}</option>
          </select>
        </div>
        <div>
          <label class="field-label">Balda</label>
          <select v-model="form.idBalda" class="field-input">
            <option v-for="b in baldas" :key="b.id" :value="b.id">
              Balda {{ b.numero }} — {{ b.estanteriaCategoria }} (Piso {{ b.pisoNumero }})
            </option>
          </select>
        </div>
      </div>
      <template #footer>
        <button class="btn btn-ghost" @click="showModal = false">Cancelar</button>
        <button class="btn btn-primary" @click="save" :disabled="saving">
          {{ saving ? 'Guardando…' : 'Guardar' }}
        </button>
      </template>
    </AppModal>

    <!-- Confirm Delete -->
    <ConfirmModal v-model="showConfirm" :label="toDelete?.titulo" @confirm="remove" />
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useToastStore } from '@/stores/toast'
import { librosApi, autoresApi, baldasApi } from '@/api/services'
import DataTable from '@/components/ui/DataTable.vue'
import AppModal from '@/components/ui/AppModal.vue'
import ConfirmModal from '@/components/ui/ConfirmModal.vue'

const auth = useAuthStore()
const toast = useToastStore()

const rows = ref([])
const autores = ref([])
const baldas = ref([])
const loading = ref(true)
const search = ref('')
const saving = ref(false)

const showModal = ref(false)
const showConfirm = ref(false)
const editing = ref(false)
const toDelete = ref(null)

const form = reactive({ id: null, titulo: '', isbn: '', idAutor: null, idBalda: null })
const formErrors = reactive({ titulo: '', isbn: '' })

const columns = [
  { key: 'id', label: '#', mono: true },
  { key: 'titulo', label: 'Título' },
  { key: 'isbn', label: 'ISBN', mono: true },
  { key: 'autorNombre', label: 'Autor' },
  { key: 'estanteriaCategoria', label: 'Categoría' },
  { key: 'pisoNombre', label: 'Piso' },
]

const filtered = computed(() => {
  const q = search.value.toLowerCase()
  return rows.value.filter(r => r.titulo.toLowerCase().includes(q))
})

async function load() {
  loading.value = true
  try {
    const [l, a, b] = await Promise.all([librosApi.getAll(), autoresApi.getAll(), baldasApi.getAll()])
    rows.value = l.data
    autores.value = a.data
    baldas.value = b.data
  } finally {
    loading.value = false
  }
}

function openForm(row = null) {
  Object.assign(formErrors, { titulo: '', isbn: '' })
  if (row) {
    editing.value = true
    Object.assign(form, { id: row.id, titulo: row.titulo, isbn: row.isbn, idAutor: row.autorId, idBalda: row.baldaId })
  } else {
    editing.value = false
    Object.assign(form, { id: null, titulo: '', isbn: '', idAutor: autores.value[0]?.id, idBalda: baldas.value[0]?.id })
  }
  showModal.value = true
}

async function save() {
  saving.value = true
  Object.assign(formErrors, { titulo: '', isbn: '' })
  const body = { titulo: form.titulo, isbn: form.isbn, idAutor: { id: form.idAutor }, idBalda: { id: form.idBalda } }
  try {
    if (editing.value) await librosApi.update(form.id, body)
    else await librosApi.create(body)
    toast.success('Libro guardado correctamente')
    showModal.value = false
    await load()
  } catch (e) {
    const details = e.response?.data?.details
    if (details) Object.assign(formErrors, details)
    else toast.error(e.response?.data?.message || 'Error al guardar')
  } finally {
    saving.value = false
  }
}

function confirmRemove(row) { toDelete.value = row; showConfirm.value = true }

async function remove() {
  try {
    await librosApi.remove(toDelete.value.id)
    toast.success('Libro eliminado')
    showConfirm.value = false
    await load()
  } catch (e) {
    toast.error(e.response?.data?.message || 'Error al eliminar')
  }
}

onMounted(load)
</script>
