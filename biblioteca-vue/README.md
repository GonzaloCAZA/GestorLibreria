# 📚 Bibliotheca — Frontend Vue 3

Frontend de gestión bibliotecaria construido con **Vue 3 + Composition API**, **Vite**, **Pinia**, **Axios** y **Tailwind CSS**.

---

## Requisitos

- Node.js ≥ 18
- Backend corriendo en `http://localhost:8080`

---

## Instalación y arranque

```bash
# 1. Instalar dependencias
npm install

# 2. Arrancar en desarrollo
npm run dev

# 3. Build de producción
npm run build
```

La app estará disponible en `http://localhost:5173`.

---

## Estructura del proyecto

```
src/
├── api/
│   ├── axios.js          # Instancia Axios + interceptor CSRF + interceptor 401
│   └── services.js       # Todos los servicios REST (authApi, librosApi, etc.)
│
├── stores/
│   ├── auth.js           # Pinia store: usuario, login, logout, isAdmin
│   └── toast.js          # Pinia store: notificaciones toast
│
├── router/
│   └── index.js          # Vue Router con guards de autenticación y rol
│
├── components/
│   ├── layout/
│   │   ├── AppSidebar.vue   # Menú lateral (adapta ítems según rol)
│   │   └── AppTopbar.vue    # Barra superior con título de sección
│   └── ui/
│       ├── AppModal.vue     # Modal reutilizable
│       ├── ConfirmModal.vue # Modal de confirmación de borrado
│       ├── DataTable.vue    # Tabla genérica con slots para celdas y acciones
│       └── ToastContainer.vue
│
├── views/
│   ├── LoginView.vue        # Login + Registro + Recuperación de contraseña
│   ├── DashboardView.vue    # Panel con estadísticas (solo admin)
│   ├── LibrosView.vue
│   ├── AutoresView.vue
│   ├── SalasView.vue
│   ├── PisosView.vue
│   ├── EstanteriasView.vue
│   ├── BaldasView.vue
│   ├── UsuariosView.vue
│   ├── PrestamosView.vue
│   └── ReservasView.vue
│
├── assets/
│   └── main.css          # Tailwind + estilos globales + animaciones
│
├── App.vue               # Shell principal: layout con sidebar o vista pública
└── main.js               # Entry point: Pinia + Router + mount
```

---

## Comportamiento por rol

| Función                         | ROLE_CUSTOMER | ROLE_ADMIN |
|---------------------------------|:---:|:---:|
| Ver catálogo (libros, autores, salas) | ✅ | ✅ |
| Crear / editar / eliminar catálogo    | ❌ | ✅ |
| Pisos, estanterías, baldas            | ❌ | ✅ |
| Usuarios                              | ❌ | ✅ |
| Préstamos                             | ❌ | ✅ |
| Reservas de sala                      | ❌ | ✅ |
| Dashboard con estadísticas            | ❌ | ✅ |

---

## CORS en el backend

Asegúrate de que el backend de Spring Boot permite el origen `http://localhost:5173` con `allowCredentials = true`.

```java
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
// o en WebMvcConfigurer:
config.addAllowedOrigin("http://localhost:5173");
config.setAllowCredentials(true);
```
