<template>
  <div class="animate-fade-slide">
    <DataTable title="Pisos" :columns="columns" :rows="rows" :loading="loading">
      <template #actions>
        <button class="btn btn-primary btn-sm" @click="openForm()">+ Nuevo piso</button>
      </template>
      <template #rowActions="{ row }">
        <div class="flex gap-2">
          <button class="btn btn-ghost btn-sm" @click="openForm(row)">Editar</button>
          <button class="btn btn-danger btn-sm" @click="confirmRemove(row)">Eliminar</button>
        </div>
      </template>
    </DataTable>

    <AppModal v-model="showModal" :title="editing ? 'Editar piso' : 'Nuevo piso'">
      <div class="space-y-4">
        <div>
          <label class="field-label">Número de piso</label>
          <input v-model.number="form.numPiso" type="number" class="field-input" />
        </div>
        <div>
          <label class="field-label">Nombre</label>
          <input v-model="form.nombre" type="text" class="field-input" />
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
import { ref, reactive, onMounted } from 'vue'
import { useToastStore } from '@/stores/toast'
import { pisosApi } from '@/api/services'
import DataTable from '@/components/ui/DataTable.vue'
import AppModal from '@/components/ui/AppModal.vue'
import ConfirmModal from '@/components/ui/ConfirmModal.vue'

const toast = useToastStore()
const rows    = ref([])
const loading = ref(true)
const saving  = ref(false)
const showModal = ref(false); const showConfirm = ref(false)
const editing = ref(false);   const toDelete = ref(null)
const form = reactive({ id: null, numPiso: 1, nombre: '' })

const columns = [
  { key: 'id',      label: '#',      mono: true },
  { key: 'numPiso', label: 'Número', mono: true },
  { key: 'nombre',  label: 'Nombre' },
]

async function load() {
  loading.value = true
  try { rows.value = (await pisosApi.getAll()).data } finally { loading.value = false }
}

function openForm(row = null) {
  if (row) { editing.value = true; Object.assign(form, { id: row.id, numPiso: row.numPiso, nombre: row.nombre }) }
  else { editing.value = false; Object.assign(form, { id: null, numPiso: 1, nombre: '' }) }
  showModal.value = true
}

async function save() {
  saving.value = true
  try {
    const body = { numPiso: form.numPiso, nombre: form.nombre }
    if (editing.value) await pisosApi.update(form.id, body)
    else               await pisosApi.create(body)
    toast.success('Piso guardado'); showModal.value = false; await load()
  } catch (e) { toast.error(e.response?.data?.message || 'Error') }
  finally { saving.value = false }
}

function confirmRemove(row) { toDelete.value = row; showConfirm.value = true }
async function remove() {
  try { await pisosApi.remove(toDelete.value.id); toast.success('Piso eliminado'); showConfirm.value = false; await load() }
  catch (e) { toast.error(e.response?.data?.message || 'Error') }
}

onMounted(load)
</script>
