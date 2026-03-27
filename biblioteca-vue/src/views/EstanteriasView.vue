<template>
  <div class="animate-fade-slide">
    <DataTable title="Estanterías" :columns="columns" :rows="rows" :loading="loading">
      <template #actions>
        <button class="btn btn-primary btn-sm" @click="openForm()">+ Nueva estantería</button>
      </template>
      <template #rowActions="{ row }">
        <div class="flex gap-2">
          <button class="btn btn-ghost btn-sm" @click="openForm(row)">Editar</button>
          <button class="btn btn-danger btn-sm" @click="confirmRemove(row)">Eliminar</button>
        </div>
      </template>
    </DataTable>

    <AppModal v-model="showModal" :title="editing ? 'Editar estantería' : 'Nueva estantería'">
      <div class="space-y-4">
        <div>
          <label class="field-label">Categoría</label>
          <input v-model="form.categoria" type="text" class="field-input" placeholder="Ej: Novela, Ciencia…" />
        </div>
        <div>
          <label class="field-label">Piso</label>
          <select v-model="form.idPiso" class="field-input">
            <option v-for="p in pisos" :key="p.id" :value="p.id">{{ p.nombre }} (Piso {{ p.numPiso }})</option>
          </select>
        </div>
      </div>
      <template #footer>
        <button class="btn btn-ghost" @click="showModal = false">Cancelar</button>
        <button class="btn btn-primary" @click="save" :disabled="saving">{{ saving ? 'Guardando…' : 'Guardar' }}</button>
      </template>
    </AppModal>

    <ConfirmModal v-model="showConfirm" :label="toDelete?.categoria" @confirm="remove" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useToastStore } from '@/stores/toast'
import { estanteriasApi, pisosApi } from '@/api/services'
import DataTable from '@/components/ui/DataTable.vue'
import AppModal from '@/components/ui/AppModal.vue'
import ConfirmModal from '@/components/ui/ConfirmModal.vue'

const toast = useToastStore()
const rows    = ref([])
const pisos   = ref([])
const loading = ref(true)
const saving  = ref(false)
const showModal = ref(false); const showConfirm = ref(false)
const editing = ref(false);   const toDelete = ref(null)
const form = reactive({ id: null, categoria: '', idPiso: null })

const columns = [
  { key: 'id',        label: '#',          mono: true },
  { key: 'categoria', label: 'Categoría' },
  { key: 'pisoNombre',label: 'Piso' },
]

async function load() {
  loading.value = true
  try {
    const [e, p] = await Promise.all([estanteriasApi.getAll(), pisosApi.getAll()])
    rows.value  = e.data
    pisos.value = p.data
  } finally { loading.value = false }
}

function openForm(row = null) {
  if (row) { editing.value = true; Object.assign(form, { id: row.id, categoria: row.categoria, idPiso: row.pisoId }) }
  else { editing.value = false; Object.assign(form, { id: null, categoria: '', idPiso: pisos.value[0]?.id }) }
  showModal.value = true
}

async function save() {
  saving.value = true
  try {
    const body = { categoria: form.categoria, idPiso: { id: form.idPiso } }
    if (editing.value) await estanteriasApi.update(form.id, body)
    else               await estanteriasApi.create(body)
    toast.success('Estantería guardada'); showModal.value = false; await load()
  } catch (e) { toast.error(e.response?.data?.message || 'Error') }
  finally { saving.value = false }
}

function confirmRemove(row) { toDelete.value = row; showConfirm.value = true }
async function remove() {
  try { await estanteriasApi.remove(toDelete.value.id); toast.success('Eliminada'); showConfirm.value = false; await load() }
  catch (e) { toast.error(e.response?.data?.message || 'Error') }
}

onMounted(load)
</script>
