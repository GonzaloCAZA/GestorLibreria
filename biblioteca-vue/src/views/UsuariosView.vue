<template>
  <div class="animate-fade-slide">
    <DataTable title="Usuarios" :columns="columns" :rows="filtered" :loading="loading">
      <template #actions>
        <select v-model="filterRol" class="field-input py-2 text-[0.9rem] w-44">
          <option value="">Todos los roles</option>
          <option value="ROLE_ADMIN">Admin</option>
          <option value="ROLE_CUSTOMER">Cliente</option>
        </select>
        <select v-model="filterMoroso" class="field-input py-2 text-[0.9rem] w-36">
          <option value="">Todos</option>
          <option value="true">Morosos</option>
          <option value="false">Sin deuda</option>
        </select>
        <button class="btn btn-primary btn-sm" @click="openForm()">+ Nuevo usuario</button>
      </template>

      <template #cell-rol="{ value }">
        <span :class="['badge', value === 'ROLE_ADMIN' ? 'badge-admin' : 'badge-customer']">
          {{ value === 'ROLE_ADMIN' ? 'Admin' : 'Cliente' }}
        </span>
      </template>
      <template #cell-moroso="{ row }">
        <span v-if="row.moroso" class="badge badge-moroso">Sí</span>
        <span v-else class="text-teal font-semibold text-[0.85rem]">No</span>
      </template>
      <template #cell-creado="{ value }">
        {{ value ? new Date(value).toLocaleDateString('es-ES') : '—' }}
      </template>

      <template #rowActions="{ row }">
        <div class="flex gap-2">
          <button class="btn btn-ghost btn-sm" @click="openForm(row)">Editar</button>
          <button class="btn btn-danger btn-sm" @click="confirmRemove(row)">Eliminar</button>
        </div>
      </template>
    </DataTable>

    <AppModal v-model="showModal" :title="editing ? 'Editar usuario' : 'Nuevo usuario'">
      <div class="space-y-4">
        <div>
          <label class="field-label">Correo electrónico</label>
          <input v-model="form.mail" type="email" class="field-input" />
          <p v-if="formErrors.mail" class="field-error">{{ formErrors.mail }}</p>
        </div>
        <div>
          <label class="field-label">Contraseña{{ editing ? ' (dejar vacío para no cambiar)' : ' *' }}</label>
          <input v-model="form.password" type="password" class="field-input" placeholder="••••••••" />
          <p v-if="formErrors.password" class="field-error">{{ formErrors.password }}</p>
        </div>
        <div>
          <label class="field-label">Rol</label>
          <select v-model="form.rol" class="field-input">
            <option value="ROLE_CUSTOMER">Cliente</option>
            <option value="ROLE_ADMIN">Admin</option>
          </select>
        </div>
        <div>
          <label class="field-label">Moroso</label>
          <select v-model="form.moroso" class="field-input">
            <option :value="false">No</option>
            <option :value="true">Sí</option>
          </select>
        </div>
      </div>
      <template #footer>
        <button class="btn btn-ghost" @click="showModal = false">Cancelar</button>
        <button class="btn btn-primary" @click="save" :disabled="saving">{{ saving ? 'Guardando…' : 'Guardar' }}</button>
      </template>
    </AppModal>

    <ConfirmModal v-model="showConfirm" :label="toDelete?.mail" @confirm="remove" />
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useToastStore } from '@/stores/toast'
import { usuariosApi } from '@/api/services'
import DataTable from '@/components/ui/DataTable.vue'
import AppModal from '@/components/ui/AppModal.vue'
import ConfirmModal from '@/components/ui/ConfirmModal.vue'

const toast = useToastStore()
const rows        = ref([])
const loading     = ref(true)
const saving      = ref(false)
const filterRol   = ref('')
const filterMoroso = ref('')
const showModal   = ref(false); const showConfirm = ref(false)
const editing     = ref(false); const toDelete    = ref(null)

const form = reactive({ id: null, mail: '', password: '', rol: 'ROLE_CUSTOMER', moroso: false })
const formErrors = reactive({ mail: '', password: '' })

const columns = [
  { key: 'id',      label: '#',       mono: true },
  { key: 'mail',    label: 'Correo' },
  { key: 'rol',     label: 'Rol' },
  { key: 'moroso',  label: 'Moroso' },
  { key: 'creado',  label: 'Creado',  mono: true },
]

const filtered = computed(() => {
  let r = rows.value
  if (filterRol.value)    r = r.filter(u => u.rol === filterRol.value)
  if (filterMoroso.value !== '') r = r.filter(u => String(u.moroso) === filterMoroso.value)
  return r
})

async function load() {
  loading.value = true
  try { rows.value = (await usuariosApi.getAll()).data } finally { loading.value = false }
}

function openForm(row = null) {
  Object.assign(formErrors, { mail: '', password: '' })
  if (row) {
    editing.value = true
    Object.assign(form, { id: row.id, mail: row.mail, password: '', rol: row.rol, moroso: row.moroso })
  } else {
    editing.value = false
    Object.assign(form, { id: null, mail: '', password: '', rol: 'ROLE_CUSTOMER', moroso: false })
  }
  showModal.value = true
}

async function save() {
  saving.value = true
  Object.assign(formErrors, { mail: '', password: '' })
  if (!editing.value && !form.password) {
    formErrors.password = 'La contraseña es obligatoria'
    saving.value = false
    return
  }
  const body = { mail: form.mail, rol: form.rol, moroso: form.moroso }
  if (form.password) body.password = form.password
  try {
    if (editing.value) await usuariosApi.update(form.id, body)
    else               await usuariosApi.create(body)
    toast.success('Usuario guardado'); showModal.value = false; await load()
  } catch (e) {
    const d = e.response?.data?.details
    if (d) Object.assign(formErrors, d)
    else toast.error(e.response?.data?.message || 'Error')
  } finally { saving.value = false }
}

function confirmRemove(row) { toDelete.value = row; showConfirm.value = true }
async function remove() {
  try { await usuariosApi.remove(toDelete.value.id); toast.success('Usuario eliminado'); showConfirm.value = false; await load() }
  catch (e) { toast.error(e.response?.data?.message || 'Error') }
}

onMounted(load)
</script>
