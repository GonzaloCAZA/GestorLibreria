<template>
  <div class="animate-fade-slide">
    <DataTable title="Reservas de sala" :columns="columns" :rows="rows" :loading="loading">
      <template #actions>
        <button class="btn btn-primary btn-sm" @click="openForm()">+ Nueva reserva</button>
      </template>

      <template #cell-fechaReserva="{ value }">
        {{ value ? new Date(value).toLocaleString('es-ES', { dateStyle: 'short', timeStyle: 'short' }) : '—' }}
      </template>
      <template #cell-fechaFinReserva="{ value }">
        {{ value ? new Date(value).toLocaleString('es-ES', { dateStyle: 'short', timeStyle: 'short' }) : '—' }}
      </template>

      <template #rowActions="{ row }">
        <div class="flex gap-2">
          <button class="btn btn-ghost btn-sm" @click="openForm(row)">Editar</button>
          <button class="btn btn-danger btn-sm" @click="confirmRemove(row)">Eliminar</button>
        </div>
      </template>
    </DataTable>

    <AppModal v-model="showModal" :title="editing ? 'Editar reserva' : 'Nueva reserva'">
      <div class="space-y-4">
        <div>
          <label class="field-label">Usuario</label>
          <select v-model="form.idUsuario" class="field-input">
            <option v-for="u in usuarios" :key="u.id" :value="u.id">{{ u.mail }}</option>
          </select>
        </div>
        <div>
          <label class="field-label">Sala</label>
          <select v-model="form.idSala" class="field-input">
            <option v-for="s in salas" :key="s.id" :value="s.id">{{ s.nombre }} — {{ s.pisoNombre }}</option>
          </select>
        </div>
        <div>
          <label class="field-label">Inicio</label>
          <input v-model="form.fechaReserva" type="datetime-local" class="field-input" />
        </div>
        <div>
          <label class="field-label">Fin</label>
          <input v-model="form.fechaFinReserva" type="datetime-local" class="field-input" />
        </div>
      </div>
      <template #footer>
        <button class="btn btn-ghost" @click="showModal = false">Cancelar</button>
        <button class="btn btn-primary" @click="save" :disabled="saving">{{ saving ? 'Guardando…' : 'Guardar' }}</button>
      </template>
    </AppModal>

    <ConfirmModal v-model="showConfirm" :label="`Reserva #${toDelete?.id}`" @confirm="remove" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useToastStore } from '@/stores/toast'
import { reservasApi, usuariosApi, salasApi } from '@/api/services'
import DataTable from '@/components/ui/DataTable.vue'
import AppModal from '@/components/ui/AppModal.vue'
import ConfirmModal from '@/components/ui/ConfirmModal.vue'

const toast = useToastStore()
const rows     = ref([])
const usuarios = ref([])
const salas    = ref([])
const loading  = ref(true)
const saving   = ref(false)
const showModal   = ref(false); const showConfirm = ref(false)
const editing     = ref(false); const toDelete    = ref(null)

const form = reactive({ id: null, idUsuario: null, idSala: null, fechaReserva: '', fechaFinReserva: '' })

const columns = [
  { key: 'id',              label: '#',       mono: true },
  { key: 'usuarioMail',     label: 'Usuario' },
  { key: 'salaNombre',      label: 'Sala' },
  { key: 'pisoNombre',      label: 'Piso' },
  { key: 'fechaReserva',    label: 'Inicio' },
  { key: 'fechaFinReserva', label: 'Fin' },
]

// ISO → datetime-local string
const toLocal = (iso) => iso ? iso.slice(0, 16) : ''

async function load() {
  loading.value = true
  try {
    const [r, u, s] = await Promise.all([reservasApi.getAll(), usuariosApi.getAll(), salasApi.getAll()])
    rows.value     = r.data
    usuarios.value = u.data
    salas.value    = s.data
  } finally { loading.value = false }
}

function openForm(row = null) {
  if (row) {
    editing.value = true
    Object.assign(form, { id: row.id, idUsuario: row.usuarioId, idSala: row.salaId, fechaReserva: toLocal(row.fechaReserva), fechaFinReserva: toLocal(row.fechaFinReserva) })
  } else {
    editing.value = false
    Object.assign(form, { id: null, idUsuario: usuarios.value[0]?.id, idSala: salas.value[0]?.id, fechaReserva: '', fechaFinReserva: '' })
  }
  showModal.value = true
}

async function save() {
  saving.value = true
  const body = {
    idUsuario: { id: form.idUsuario }, idSala: { id: form.idSala },
    fechaReserva:    new Date(form.fechaReserva).toISOString(),
    fechaFinReserva: new Date(form.fechaFinReserva).toISOString(),
  }
  try {
    if (editing.value) await reservasApi.update(form.id, body)
    else               await reservasApi.create(body)
    toast.success('Reserva guardada'); showModal.value = false; await load()
  } catch (e) { toast.error(e.response?.data?.message || 'Error') }
  finally { saving.value = false }
}

function confirmRemove(row) { toDelete.value = row; showConfirm.value = true }
async function remove() {
  try { await reservasApi.remove(toDelete.value.id); toast.success('Reserva eliminada'); showConfirm.value = false; await load() }
  catch (e) { toast.error(e.response?.data?.message || 'Error') }
}

onMounted(load)
</script>
