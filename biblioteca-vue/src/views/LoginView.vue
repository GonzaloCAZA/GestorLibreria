<template>
  <div class="min-h-screen bg-ink flex items-center justify-center p-5">
    <!-- Grid decoration -->
    <div class="absolute inset-0 pointer-events-none"
      style="background-image: repeating-linear-gradient(0deg,transparent,transparent 39px,rgba(200,134,10,0.08) 40px),repeating-linear-gradient(90deg,transparent,transparent 39px,rgba(200,134,10,0.05) 40px);"
    />

    <div class="relative bg-cream border border-paper-dark w-full max-w-[420px] shadow-modal animate-auth-in">
      <!-- Amber top bar -->
      <div class="h-1 bg-gradient-to-r from-amber via-amber-light to-amber" />

      <div class="px-12 py-12">
        <!-- Logo -->
        <div class="text-center mb-9">
          <span class="text-[2.4rem] block mb-2">📚</span>
          <h1 class="font-display text-[1.85rem] font-bold text-ink leading-tight">Bibliotheca</h1>
          <p class="font-body italic font-light text-ink-light text-[0.95rem] mt-1">Sistema de Gestión Bibliotecaria</p>
        </div>

        <!-- ── LOGIN / REGISTER ── -->
        <template v-if="panel === 'main'">
          <!-- Tabs -->
          <div class="flex border-b-2 border-paper mb-7">
            <button
              v-for="tab in ['login','register']"
              :key="tab"
              @click="activeTab = tab"
              :class="[
                'flex-1 py-2.5 font-body font-semibold text-[0.88rem] uppercase tracking-widest transition-colors border-b-2 -mb-[2px]',
                activeTab === tab
                  ? 'text-amber border-amber'
                  : 'text-paper-dark border-transparent hover:text-ink'
              ]"
            >{{ tab === 'login' ? 'Iniciar sesión' : 'Registrarse' }}</button>
          </div>

          <!-- LOGIN -->
          <form v-if="activeTab === 'login'" @submit.prevent="handleLogin" class="space-y-4">
            <div>
              <label class="field-label">Correo electrónico</label>
              <input v-model="loginForm.mail" type="email" class="field-input" placeholder="correo@ejemplo.com" autocomplete="email" />
              <p v-if="loginErrors.mail" class="field-error">{{ loginErrors.mail }}</p>
            </div>
            <div>
              <label class="field-label">Contraseña</label>
              <input v-model="loginForm.password" type="password" class="field-input" placeholder="••••••••" autocomplete="current-password" />
              <p v-if="loginErrors.password" class="field-error">{{ loginErrors.password }}</p>
            </div>
            <p v-if="loginErrors.general" class="field-error text-center">{{ loginErrors.general }}</p>
            <button type="submit" class="btn btn-primary w-full mt-2" :disabled="auth.loading">
              {{ auth.loading ? 'Entrando…' : 'Entrar' }}
            </button>
            <div class="text-center mt-3">
              <button type="button" @click="panel='forgot'" class="text-amber italic text-[0.88rem] underline underline-offset-2 bg-transparent border-0 cursor-pointer font-body">
                ¿Olvidaste tu contraseña?
              </button>
            </div>
          </form>

          <!-- REGISTER -->
          <form v-else @submit.prevent="handleRegister" class="space-y-4">
            <div>
              <label class="field-label">Correo electrónico</label>
              <input v-model="registerForm.mail" type="email" class="field-input" placeholder="correo@ejemplo.com" />
              <p v-if="registerErrors.mail" class="field-error">{{ registerErrors.mail }}</p>
            </div>
            <div>
              <label class="field-label">Contraseña</label>
              <input v-model="registerForm.password" type="password" class="field-input" placeholder="Mínimo 8 caracteres" />
              <p v-if="registerErrors.password" class="field-error">{{ registerErrors.password }}</p>
            </div>
            <p v-if="registerErrors.general" class="field-error text-center">{{ registerErrors.general }}</p>
            <button type="submit" class="btn btn-primary w-full mt-2" :disabled="auth.loading">
              {{ auth.loading ? 'Creando cuenta…' : 'Crear cuenta' }}
            </button>
          </form>
        </template>

        <!-- ── FORGOT PASSWORD ── -->
        <template v-else>
          <h3 class="font-display text-[1.15rem] font-semibold text-ink mb-5">Recuperar contraseña</h3>
          <form @submit.prevent="handleForgot" class="space-y-4">
            <div>
              <label class="field-label">Correo electrónico</label>
              <input v-model="forgotMail" type="email" class="field-input" placeholder="correo@ejemplo.com" />
            </div>
            <button type="submit" class="btn btn-primary w-full">Enviar código</button>
          </form>

          <!-- Reset section -->
          <div v-if="showReset" class="mt-6 space-y-4 border-t border-paper pt-6">
            <div>
              <label class="field-label">Código recibido</label>
              <input v-model="resetForm.codigo" type="text" class="field-input font-mono" placeholder="123456" />
            </div>
            <div>
              <label class="field-label">Nueva contraseña</label>
              <input v-model="resetForm.newPassword" type="password" class="field-input" placeholder="••••••••" />
            </div>
            <button class="btn btn-amber w-full" @click="handleReset">Cambiar contraseña</button>
          </div>

          <div class="text-center mt-5">
            <button @click="panel='main'" class="text-amber italic text-[0.88rem] underline underline-offset-2 bg-transparent border-0 cursor-pointer font-body">
              ← Volver al inicio de sesión
            </button>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useToastStore } from '@/stores/toast'
import { authApi } from '@/api/services'

const auth  = useAuthStore()
const toast = useToastStore()

const panel     = ref('main')
const activeTab = ref('login')

// Login
const loginForm   = reactive({ mail: '', password: '' })
const loginErrors = reactive({ mail: '', password: '', general: '' })

async function handleLogin() {
  Object.assign(loginErrors, { mail: '', password: '', general: '' })
  try {
    await auth.login(loginForm.mail, loginForm.password)
  } catch (e) {
    const details = e.response?.data?.details
    if (details) {
      Object.assign(loginErrors, details)
    } else {
      loginErrors.general = e.response?.data?.message || 'Error al iniciar sesión'
    }
  }
}

// Register
const registerForm   = reactive({ mail: '', password: '' })
const registerErrors = reactive({ mail: '', password: '', general: '' })

async function handleRegister() {
  Object.assign(registerErrors, { mail: '', password: '', general: '' })
  try {
    await auth.register(registerForm.mail, registerForm.password)
  } catch (e) {
    const details = e.response?.data?.details
    if (details) {
      Object.assign(registerErrors, details)
    } else {
      registerErrors.general = e.response?.data?.message || 'Error al registrarse'
    }
  }
}

// Forgot / Reset
const forgotMail  = ref('')
const showReset   = ref(false)
const resetForm   = reactive({ codigo: '', newPassword: '' })

async function handleForgot() {
  try {
    await authApi.forgotPassword({ mail: forgotMail.value })
    showReset.value = true
    toast.info('Si el correo existe, recibirás un código')
  } catch (e) {
    toast.error('Error al enviar el código')
  }
}

async function handleReset() {
  try {
    await authApi.resetPassword({ mail: forgotMail.value, ...resetForm })
    toast.success('Contraseña actualizada. Ya puedes iniciar sesión.')
    panel.value = 'main'
    activeTab.value = 'login'
    showReset.value = false
  } catch (e) {
    toast.error(e.response?.data?.message || 'Error al restablecer la contraseña')
  }
}
</script>
