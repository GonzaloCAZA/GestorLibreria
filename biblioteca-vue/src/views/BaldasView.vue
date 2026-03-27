<template>
  <div class="animate-fade-slide">
    <DataTable title="Baldas" :columns="columns" :rows="rows" :loading="loading">
      <template #actions>
        <button class="btn btn-primary btn-sm" @click="openForm()">+ Nueva balda</button>
      </template>
      <template #rowActions="{ row }">
        <div class="flex gap-2">
          <button class="btn btn-ghost btn-sm" @click="openForm(row)">Editar</button>
          <button class="btn btn-danger btn-sm" @click="confirmRemove(row)">Eliminar</button>
        </div>
      </template>
    </DataTable>

    <AppModal v-model="showModal" :title="editing ? 'Editar balda' : 'Nueva balda'">
      <div class="space-y-4">
        <div>
          <label class="field-label">Número de balda</label>
          <input v-model.number="form.numero" type="number" min="1" class="field-input" />
        </div>
        <div>
          <label class="field-label">Estantería</label>
          <select v-model="form.idEstanteria" class="field-input">
            <option v-for="e in estanterias" :key="e.id" :value="e.id">
              {{ e.categoria }} — Piso {{ e.pisoNumero }}
            </option>
          </select>
        </div>
      </div>
      <template #footer>
        <button class="btn btn-ghost" @click="showModal = false">Cancelar</button>
        <button class="btn btn-primary" @click="save" :disabled="saving">{{ saving ? 'Guardando…' : 'Guardar' }}</button>
      </template>
    </AppModal>

    <ConfirmModal v-model="showConfirm" :label="`Balda ${toDelete?.numero}`" @confirm="remove" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useToastStore } from '@/stores/toast'
import { baldasApi, estanteriasApi } from '@/api/services'
import DataTable from '@/components/ui/DataTable.vue'
import AppModal from '@/components/ui/AppModal.vue'
import ConfirmModal from '@/components/ui/ConfirmModal.vue'

const toast = useToastStore()
const rows        = ref([])
const estanterias = ref([])
const loading     = ref(true)
const saving      = ref(false)
const showModal   = ref(false); const showConfirm = ref(false)
const editing     = ref(false); const toDelete    = ref(null)
const form = reactive({ id: null, numero: 1, idEstanteria: null })

const columns = [
  { key: 'id',                  label: '#',          mono: true },
  { key: 'numero',              label: 'Número',      mono: true },
  { key: 'estanteriaCategoria', label: 'Categoría' },
  { key: 'pisoNombre',          label: 'Piso' },
]

async function load() {
  loading.value = true
  try {
    const [b, e] = await Promise.all([baldasApi.getAll(), estanteriasApi.getAll()])
    rows.value        = b.data
    estanterias.value = e.data
  } finally { loading.value = false }
}

function openForm(row = null) {
  if (row) { editing.value = true; Object.assign(form, { id: row.id, numero: row.numero, idEstanteria: row.estanteriaId }) }
  else { editing.value = false; Object.assign(form, { id: null, numero: 1, idEstanteria: estanterias.value[0]?.id }) }
  showModal.value = true
}

async function save() {
  saving.value = true
  try {
    const body = { numero: form.numero, idEstanteria: { id: form.idEstanteria } }
    if (editing.value) await baldasApi.update(form.id, body)
    else               await baldasApi.create(body)
    toast.success('Balda guardada'); showModal.value = false; await load()
  } catch (e) { toast.error(e.response?.data?.message || 'Error') }
  finally { saving.value = false }
}

function confirmRemove(row) { toDelete.value = row; showConfirm.value = true }
async function remove() {
  try { await baldasApi.remove(toDelete.value.id); toast.success('Balda eliminada'); showConfirm.value = false; await load() }
  catch (e) { toast.error(e.response?.data?.message || 'Error') }
}

onMounted(load)
</script>
