<template>
  <div class="animate-fade-slide">
    <DataTable title="Salas de estudio" :columns="columns" :rows="rows" :loading="loading">
      <template #actions>
        <button v-if="auth.isAdmin" class="btn btn-primary btn-sm" @click="openForm()">+ Nueva sala</button>
      </template>
      <template #cell-maximoPersonas="{ value }">{{ value }} personas</template>
      <template #rowActions="{ row }" v-if="auth.isAdmin">
        <div class="flex gap-2">
          <button class="btn btn-ghost btn-sm" @click="openReserva(row)">Reservar</button>
          <button class="btn btn-ghost btn-sm" @click="openForm(row)">Editar</button>
          <button class="btn btn-danger btn-sm" @click="confirmRemove(row)">Eliminar</button>
        </div>
      </template>
    </DataTable>

    <AppModal v-model="showModal" :title="editing ? 'Editar sala' : 'Nueva sala'">
      <div class="space-y-4">
        <div>
          <label class="field-label">Nombre *</label>
          <input v-model="form.nombre" type="text" class="field-input" />
        </div>
        <div>
          <label class="field-label">Máximo de personas</label>
          <input v-model.number="form.maximoPersonas" type="number" min="1" class="field-input" />
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

    <ConfirmModal v-model="showConfirm" :label="toDelete?.nombre" @confirm="remove" />

    <!-- Modal de Reserva -->
    <AppModal v-model="showReservaModal" title="Reservar sala">
      <div class="space-y-4">
        <p class="text-sm font-medium">Sala: <span class="font-semibold">{{ selectedSalaForReserva?.nombre }}</span></p>
        <div>
          <label class="field-label">Fecha *</label>
          <input v-model="reservaForm.fecha" type="date" class="field-input" />
        </div>
        <div>
          <label class="field-label">Hora *</label>
          <input v-model="reservaForm.hora" type="time" class="field-input" />
        </div>
      </div>
      <template #footer>
        <button class="btn btn-ghost" @click="showReservaModal = false">Cancelar</button>
        <button class="btn btn-primary" @click="saveReserva" :disabled="savingReserva">{{ savingReserva ? 'Guardando…' : 'Reservar' }}</button>
      </template>
    </AppModal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useToastStore } from '@/stores/toast'
import { salasApi, pisosApi, reservasApi } from '@/api/services'
import DataTable from '@/components/ui/DataTable.vue'
import AppModal from '@/components/ui/AppModal.vue'
import ConfirmModal from '@/components/ui/ConfirmModal.vue'

const auth  = useAuthStore()
const toast = useToastStore()

const rows    = ref([])
const pisos   = ref([])
const loading = ref(true)
const saving  = ref(false)
const savingReserva = ref(false)
const showModal   = ref(false)
const showReservaModal = ref(false)
const showConfirm = ref(false)
const editing     = ref(false)
const toDelete    = ref(null)
const selectedSalaForReserva = ref(null)
const form = reactive({ id: null, nombre: '', maximoPersonas: 8, idPiso: null })
const reservaForm = reactive({ fecha: '', hora: '' })

const columns = [
  { key: 'id',             label: '#',    mono: true },
  { key: 'nombre',         label: 'Nombre' },
  { key: 'maximoPersonas', label: 'Capacidad' },
  { key: 'pisoNombre',     label: 'Piso' },
]

async function load() {
  loading.value = true
  try {
    const [s, p] = await Promise.all([salasApi.getAll(), pisosApi.getAll()])
    rows.value  = s.data
    pisos.value = p.data
  } finally { loading.value = false }
}

function openForm(row = null) {
  if (row) {
    editing.value = true
    Object.assign(form, { id: row.id, nombre: row.nombre, maximoPersonas: row.maximoPersonas, idPiso: row.pisoId })
  } else {
    editing.value = false
    Object.assign(form, { id: null, nombre: '', maximoPersonas: 8, idPiso: pisos.value[0]?.id })
  }
  showModal.value = true
}

async function save() {
  saving.value = true
  try {
    const body = { nombre: form.nombre, maximoPersonas: form.maximoPersonas, idPiso: { id: form.idPiso } }
    if (editing.value) await salasApi.update(form.id, body)
    else               await salasApi.create(body)
    toast.success('Sala guardada')
    showModal.value = false
    await load()
  } catch (e) { toast.error(e.response?.data?.message || 'Error') }
  finally { saving.value = false }
}

function confirmRemove(row) { toDelete.value = row; showConfirm.value = true }

async function remove() {
  try {
    await salasApi.remove(toDelete.value.id)
    toast.success('Sala eliminada')
    showConfirm.value = false
    await load()
  } catch (e) { toast.error(e.response?.data?.message || 'Error') }
}

function openReserva(row) {
  selectedSalaForReserva.value = row
  Object.assign(reservaForm, { fecha: '', hora: '' })
  showReservaModal.value = true
}

async function saveReserva() {
  if (!reservaForm.fecha || !reservaForm.hora) {
    toast.error('Por favor completa la fecha y hora')
    return
  }
  
  savingReserva.value = true
  try {
    const body = {
      fechaReserva: reservaForm.fecha,
      horaInicio: reservaForm.hora,
      idSala: { id: selectedSalaForReserva.value.id }
    }
    await reservasApi.create(body)
    toast.success('Reserva creada correctamente')
    showReservaModal.value = false
    await load()
  } catch (e) {
    toast.error(e.response?.data?.message || 'Error al crear la reserva')
  } finally {
    savingReserva.value = false
  }
}

onMounted(load)
</script>
