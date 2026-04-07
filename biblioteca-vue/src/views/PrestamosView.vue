<template>
  <div class="animate-fade-slide">
    <DataTable title="Préstamos" :columns="columns" :rows="rows" :loading="loading">
      <template #actions>
        <button class="btn btn-primary btn-sm" @click="openForm()">+ Nuevo préstamo</button>
      </template>

      <template #cell-fechaDevolucionReal="{ value }">
        <span v-if="value" class="text-teal font-semibold text-[0.88rem]">✓ {{ value }}</span>
        <span v-else class="text-danger italic text-[0.88rem]">Pendiente</span>
      </template>

      <template #rowActions="{ row }">
        <div class="flex gap-2">
          <button class="btn btn-ghost btn-sm" @click="openForm(row)">Editar</button>
          <button class="btn btn-danger btn-sm" @click="confirmRemove(row)">Eliminar</button>
        </div>
      </template>
    </DataTable>

    <AppModal v-model="showModal" :title="editing ? 'Editar préstamo' : 'Nuevo préstamo'">
      <div class="space-y-4">
        <div>
          <label class="field-label">Usuario</label>
          <select v-model="form.idUsuario" class="field-input">
            <option v-for="u in usuarios" :key="u.id" :value="u.id">{{ u.mail }}</option>
          </select>
        </div>
        <div>
          <label class="field-label">Libro</label>
          <select v-model="form.idLibro" class="field-input">
            <option v-for="l in libros" :key="l.id" :value="l.id">{{ l.titulo }}</option>
          </select>
        </div>
        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="field-label">Fecha préstamo</label>
            <input v-model="form.fechaPrestamo" type="date" class="field-input" />
          </div>
          <div>
            <label class="field-label">Devolución prevista</label>
            <input v-model="form.fechaDevolucionPrevista" type="date" class="field-input" />
          </div>
        </div>
        <div>
          <label class="field-label">Devolución real <span class="text-ink-light font-normal normal-case tracking-normal">(dejar vacío si no devuelto)</span></label>
          <input v-model="form.fechaDevolucionReal" type="date" class="field-input" />
        </div>
      </div>
      <template #footer>
        <button class="btn btn-ghost" @click="showModal = false">Cancelar</button>
        <button class="btn btn-primary" @click="save" :disabled="saving">{{ saving ? 'Guardando…' : 'Guardar' }}</button>
      </template>
    </AppModal>

    <ConfirmModal v-model="showConfirm" :label="`Préstamo #${toDelete?.id} — ${toDelete?.libroTitulo}`" @confirm="remove" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useToastStore } from '@/stores/toast'
import { prestamosApi, usuariosApi, librosApi } from '@/api/services'
import DataTable from '@/components/ui/DataTable.vue'
import AppModal from '@/components/ui/AppModal.vue'
import ConfirmModal from '@/components/ui/ConfirmModal.vue'

const toast = useToastStore()
const rows     = ref([])
const usuarios = ref([])
const libros   = ref([])
const loading  = ref(true)
const saving   = ref(false)
const showModal   = ref(false); const showConfirm = ref(false)
const editing     = ref(false); const toDelete    = ref(null)

const today   = new Date().toISOString().split('T')[0]
const in30    = new Date(Date.now() + 30 * 86400000).toISOString().split('T')[0]

const form = reactive({
  id: null, idUsuario: null, idLibro: null,
  fechaPrestamo: today, fechaDevolucionPrevista: in30, fechaDevolucionReal: ''
})

const columns = [
  { key: 'id',                      label: '#',                 mono: true },
  { key: 'usuarioMail',             label: 'Usuario' },
  { key: 'libroTitulo',             label: 'Libro' },
  { key: 'fechaPrestamo',           label: 'F. Préstamo',       mono: true },
  { key: 'fechaDevolucionPrevista', label: 'F. Devolución',     mono: true },
  { key: 'fechaDevolucionReal',     label: 'Devuelto' },
]

async function load() {
  loading.value = true
  try {
    const [p, u, l] = await Promise.all([prestamosApi.getAll(), usuariosApi.getAll(), librosApi.getAll()])
    rows.value     = p.data
    usuarios.value = u.data
    libros.value   = l.data
  } finally { loading.value = false }
}

function openForm(row = null) {
  if (row) {
    editing.value = true
    Object.assign(form, {
      id: row.id, idUsuario: row.usuarioId, idLibro: row.libroId,
      fechaPrestamo: row.fechaPrestamo, fechaDevolucionPrevista: row.fechaDevolucionPrevista,
      fechaDevolucionReal: row.fechaDevolucionReal ?? ''
    })
  } else {
    editing.value = false
    Object.assign(form, { id: null, idUsuario: usuarios.value[0]?.id, idLibro: libros.value[0]?.id, fechaPrestamo: today, fechaDevolucionPrevista: in30, fechaDevolucionReal: '' })
  }
  showModal.value = true
}

async function save() {
  saving.value = true
  const body = {
    idUsuario: { id: form.idUsuario }, idLibro: { id: form.idLibro },
    fechaPrestamo: form.fechaPrestamo, fechaDevolucionPrevista: form.fechaDevolucionPrevista,
    fechaDevolucionReal: form.fechaDevolucionReal || null
  }
  try {
    if (editing.value) await prestamosApi.update(form.id, body)
    else               await prestamosApi.create(body)
    toast.success('Préstamo guardado'); showModal.value = false; await load()
  } catch (e) { toast.error(e.response?.data?.message || 'Error') }
  finally { saving.value = false }
}

function confirmRemove(row) { toDelete.value = row; showConfirm.value = true }
async function remove() {
  try { await prestamosApi.remove(toDelete.value.id); toast.success('Préstamo eliminado'); showConfirm.value = false; await load() }
  catch (e) { toast.error(e.response?.data?.message || 'Error') }
}

onMounted(load)
</script>
