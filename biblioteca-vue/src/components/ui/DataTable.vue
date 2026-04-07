<template>
  <div class="card">
    <!-- Card header slot -->
    <div class="card-header">
      <h2 class="card-title">{{ title }}</h2>
      <div class="flex items-center gap-3 flex-wrap">
        <slot name="actions" />
      </div>
    </div>

    <!-- Filters slot -->
    <div v-if="$slots.filters" class="px-6 py-3 border-b border-paper bg-cream-dark flex items-center gap-3 flex-wrap">
      <slot name="filters" />
    </div>

    <!-- Table -->
    <div class="overflow-x-auto">
      <table class="lib-table">
        <thead>
          <tr>
            <th v-for="col in columns" :key="col.key">{{ col.label }}</th>
            <th v-if="$slots.rowActions">Acciones</th>
          </tr>
        </thead>
        <tbody>
          <!-- Loading -->
          <tr v-if="loading">
            <td :colspan="columns.length + ($slots.rowActions ? 1 : 0)" class="text-center py-10 text-ink-light italic">
              Cargando…
            </td>
          </tr>

          <!-- Empty -->
          <tr v-else-if="!rows.length">
            <td :colspan="columns.length + ($slots.rowActions ? 1 : 0)" class="text-center py-14 text-ink-light italic">
              <span class="block text-4xl mb-3 opacity-30">📭</span>
              Sin registros
            </td>
          </tr>

          <!-- Rows -->
          <tr v-else v-for="row in rows" :key="row.id">
            <td
              v-for="col in columns"
              :key="col.key"
              :class="col.mono ? 'font-mono text-[0.82rem] text-ink-light' : ''"
            >
              <slot :name="`cell-${col.key}`" :row="row" :value="row[col.key]">
                {{ row[col.key] ?? '—' }}
              </slot>
            </td>
            <td v-if="$slots.rowActions" class="whitespace-nowrap">
              <slot name="rowActions" :row="row" />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
defineProps({
  title:   { type: String, required: true },
  columns: { type: Array,  required: true },
  rows:    { type: Array,  default: () => [] },
  loading: { type: Boolean, default: false },
})
</script>
