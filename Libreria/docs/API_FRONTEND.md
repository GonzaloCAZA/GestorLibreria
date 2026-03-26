# Manual de Endpoints para Frontend

## Base URL

- Desarrollo local: `http://localhost:8080`
- Prefijo API: `/api`

## Autenticacion y sesion

- La autenticacion usa cookie JWT `HttpOnly`.
- El frontend debe enviar siempre credenciales:

```ts
fetch(url, {
  credentials: "include"
})
```

- El backend usa CSRF para peticiones mutadoras.
- Antes de `POST`, `PUT` o `DELETE`, conviene pedir token CSRF en `GET /api/auth/csrf`.
- Luego enviar el header `X-XSRF-TOKEN` con el valor recibido o con el valor de la cookie `XSRF-TOKEN`.

Ejemplo base:

```ts
await fetch("http://localhost:8080/api/auth/csrf", {
  credentials: "include"
})

await fetch("http://localhost:8080/api/auth/login", {
  method: "POST",
  credentials: "include",
  headers: {
    "Content-Type": "application/json",
    "X-XSRF-TOKEN": xsrfToken
  },
  body: JSON.stringify({
    mail: "admin@demo.com",
    password: "12345678"
  })
})
```

## Roles

- `ROLE_CUSTOMER`: usuario normal.
- `ROLE_ADMIN`: administrador.

Permisos actuales:

- Lectura de catalogo: cualquier usuario autenticado.
- Escritura de catalogo: solo `ROLE_ADMIN`.
- Usuarios: solo `ROLE_ADMIN`.
- Prestamos: solo `ROLE_ADMIN`.
- Reservas de sala: solo `ROLE_ADMIN`.

## Respuestas de error

Formato general:

```json
{
  "timestamp": "2026-03-26T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Solicitud no valida",
  "details": {
    "mail": "must not be blank"
  }
}
```

Codigos habituales:

- `400`: validacion o datos invalidos.
- `401`: no autenticado o credenciales incorrectas.
- `403`: autenticado pero sin permisos.
- `404`: recurso no encontrado.
- `409`: conflicto, por ejemplo email duplicado.

## Auth

### `GET /api/auth/csrf`

Uso:

- Obtiene el token CSRF para futuras peticiones `POST`, `PUT`, `DELETE`.

Respuesta:

```json
{
  "token": "..."
}
```

### `POST /api/auth/register`

Publico: `si`

Body:

```json
{
  "mail": "user@demo.com",
  "password": "12345678"
}
```

Notas:

- El rol no se envia.
- El backend asigna siempre `ROLE_CUSTOMER`.
- Si va bien, ademas de responder, deja la sesion iniciada por cookie.

Respuesta 200:

```json
{
  "id": 1,
  "mail": "user@demo.com",
  "rol": "ROLE_CUSTOMER",
  "message": "Usuario registrado correctamente"
}
```

### `POST /api/auth/login`

Publico: `si`

Body:

```json
{
  "mail": "user@demo.com",
  "password": "12345678"
}
```

Respuesta 200:

```json
{
  "id": 1,
  "mail": "user@demo.com",
  "rol": "ROLE_CUSTOMER",
  "message": "Login correcto"
}
```

### `POST /api/auth/logout`

Publico: `no`

Body: vacio

Respuesta 200:

```json
{
  "message": "Logout correcto"
}
```

### `GET /api/auth/me`

Publico: `no`

Uso:

- Recupera el usuario autenticado actual.
- Ideal para hidratar el estado global de sesion al cargar la app.

Respuesta 200:

```json
{
  "id": 1,
  "mail": "user@demo.com",
  "rol": "ROLE_CUSTOMER",
  "message": "Usuario autenticado"
}
```

### `POST /api/auth/forgot-password`

Publico: `si`

Body:

```json
{
  "mail": "user@demo.com"
}
```

Respuesta 200:

```json
{
  "message": "Si el correo existe, se ha enviado un codigo de recuperacion"
}
```

### `POST /api/auth/reset-password`

Publico: `si`

Body:

```json
{
  "mail": "user@demo.com",
  "codigo": "123456",
  "newPassword": "nuevaPassword123"
}
```

Respuesta 200:

```json
{
  "message": "Contrasena actualizada correctamente"
}
```

## Usuarios

Todos los endpoints de usuarios requieren `ROLE_ADMIN`.

### `GET /api/usuarios`

Query params opcionales:

- `rol`
- `moroso`

Ejemplos:

- `/api/usuarios`
- `/api/usuarios?rol=ROLE_ADMIN`
- `/api/usuarios?moroso=true`

Respuesta:

```json
[
  {
    "id": 1,
    "mail": "admin@demo.com",
    "rol": "ROLE_ADMIN",
    "moroso": false,
    "creado": "2026-03-26T12:00:00Z",
    "actualizado": "2026-03-26T12:00:00Z"
  }
]
```

### `GET /api/usuarios/{id}`

Respuesta:

```json
{
  "id": 1,
  "mail": "admin@demo.com",
  "rol": "ROLE_ADMIN",
  "moroso": false,
  "creado": "2026-03-26T12:00:00Z",
  "actualizado": "2026-03-26T12:00:00Z"
}
```

### `GET /api/usuarios/mail/{mail}`

Ejemplo:

- `/api/usuarios/mail/admin@demo.com`

Respuesta igual que `GET /api/usuarios/{id}`.

### `POST /api/usuarios`

Body:

```json
{
  "mail": "nuevo@demo.com",
  "rol": "ROLE_CUSTOMER",
  "moroso": false,
  "password": "12345678"
}
```

### `PUT /api/usuarios/{id}`

Body:

```json
{
  "mail": "nuevo@demo.com",
  "rol": "ROLE_CUSTOMER",
  "moroso": true,
  "password": "passwordOpcional123"
}
```

Notas:

- `password` es opcional en update.
- Si se envia vacio o `null`, no cambia.

### `DELETE /api/usuarios/{id}`

Respuesta: `204 No Content`

## Catalogo

Lectura: cualquier usuario autenticado.

Escritura: solo `ROLE_ADMIN`.

## Autores

### `GET /api/autores`

Filtros opcionales:

- `nombre`
- `nacionalidad`
- `fechaNacimiento` en formato `YYYY-MM-DD`

Ejemplos:

- `/api/autores`
- `/api/autores?nombre=cerv`
- `/api/autores?nacionalidad=Espana`
- `/api/autores?fechaNacimiento=1947-09-21`

Modelo:

```json
{
  "id": 1,
  "nombre": "Gabriel Garcia Marquez",
  "fechaNacimiento": "1927-03-06",
  "nacionalidad": "Colombiana",
  "creado": "2026-03-26T12:00:00Z",
  "actualizado": "2026-03-26T12:00:00Z"
}
```

### `GET /api/autores/{id}`

### `POST /api/autores`

Admin.

Body:

```json
{
  "nombre": "Gabriel Garcia Marquez",
  "fechaNacimiento": "1927-03-06",
  "nacionalidad": "Colombiana"
}
```

### `PUT /api/autores/{id}`

Admin. Mismo body que create.

### `DELETE /api/autores/{id}`

Admin. Respuesta `204`.

## Pisos

Modelo:

```json
{
  "id": 1,
  "numPiso": 1,
  "nombre": "Primera planta",
  "creado": "2026-03-26T12:00:00Z",
  "actualizado": "2026-03-26T12:00:00Z"
}
```

Endpoints:

- `GET /api/pisos`
- `GET /api/pisos/{id}`
- `POST /api/pisos` admin
- `PUT /api/pisos/{id}` admin
- `DELETE /api/pisos/{id}` admin

Body create/update:

```json
{
  "numPiso": 1,
  "nombre": "Primera planta"
}
```

## Estanterias

Modelo:

```json
{
  "id": 1,
  "categoria": "Novela",
  "idPiso": {
    "id": 1
  },
  "creado": "2026-03-26T12:00:00Z",
  "actualizado": "2026-03-26T12:00:00Z"
}
```

Endpoints:

- `GET /api/estanterias`
- `GET /api/estanterias/{id}`
- `POST /api/estanterias` admin
- `PUT /api/estanterias/{id}` admin
- `DELETE /api/estanterias/{id}` admin

Body create/update:

```json
{
  "categoria": "Novela",
  "idPiso": {
    "id": 1
  }
}
```

## Baldas

Modelo:

```json
{
  "id": 1,
  "numero": 3,
  "idEstanteria": {
    "id": 2
  },
  "creado": "2026-03-26T12:00:00Z",
  "actualizado": "2026-03-26T12:00:00Z"
}
```

Endpoints:

- `GET /api/baldas`
- `GET /api/baldas/{id}`
- `POST /api/baldas` admin
- `PUT /api/baldas/{id}` admin
- `DELETE /api/baldas/{id}` admin

Body create/update:

```json
{
  "numero": 3,
  "idEstanteria": {
    "id": 2
  }
}
```

## Libros

### `GET /api/libros`

Filtro opcional:

- `titulo`

Ejemplos:

- `/api/libros`
- `/api/libros?titulo=quijote`

### `GET /api/libros/{id}`

### `GET /api/libros/isbn/{isbn}`

Modelo:

```json
{
  "id": 1,
  "titulo": "Cien anos de soledad",
  "isbn": "9780307474728",
  "idAutor": {
    "id": 1
  },
  "idBalda": {
    "id": 2
  },
  "creado": "2026-03-26T12:00:00Z",
  "actualizado": "2026-03-26T12:00:00Z"
}
```

### `POST /api/libros`

Admin.

```json
{
  "titulo": "Cien anos de soledad",
  "isbn": "9780307474728",
  "idAutor": {
    "id": 1
  },
  "idBalda": {
    "id": 2
  }
}
```

### `PUT /api/libros/{id}`

Admin. Mismo body que create.

### `DELETE /api/libros/{id}`

Admin. Respuesta `204`.

## Salas

Modelo:

```json
{
  "id": 1,
  "nombre": "Sala 1",
  "maximoPersonas": 8,
  "idPiso": {
    "id": 1
  },
  "creado": "2026-03-26T12:00:00Z",
  "actualizado": "2026-03-26T12:00:00Z"
}
```

Endpoints:

- `GET /api/salas`
- `GET /api/salas/{id}`
- `POST /api/salas` admin
- `PUT /api/salas/{id}` admin
- `DELETE /api/salas/{id}` admin

Body create/update:

```json
{
  "nombre": "Sala 1",
  "maximoPersonas": 8,
  "idPiso": {
    "id": 1
  }
}
```

## Prestamos

Todos los endpoints de prestamos requieren `ROLE_ADMIN`.

Modelo:

```json
{
  "id": 1,
  "idUsuario": {
    "id": 5
  },
  "idLibro": {
    "id": 10
  },
  "fechaPrestamo": "2026-03-26",
  "fechaDevolucionPrevista": "2026-04-26",
  "fechaDevolucionReal": null,
  "creado": "2026-03-26T12:00:00Z",
  "actualizado": "2026-03-26T12:00:00Z"
}
```

Endpoints:

- `GET /api/prestamos`
- `GET /api/prestamos/{id}`
- `POST /api/prestamos`
- `PUT /api/prestamos/{id}`
- `DELETE /api/prestamos/{id}`

Body create/update:

```json
{
  "idUsuario": {
    "id": 5
  },
  "idLibro": {
    "id": 10
  },
  "fechaPrestamo": "2026-03-26",
  "fechaDevolucionPrevista": "2026-04-26",
  "fechaDevolucionReal": null
}
```

## Reservas de sala

Todos los endpoints de reservas requieren `ROLE_ADMIN`.

Modelo:

```json
{
  "id": 1,
  "idUsuario": {
    "id": 5
  },
  "idSala": {
    "id": 2
  },
  "fechaReserva": "2026-03-26T10:00:00Z",
  "fechaFinReserva": "2026-03-26T12:00:00Z",
  "creado": "2026-03-26T09:00:00Z",
  "actualizado": "2026-03-26T09:00:00Z"
}
```

Endpoints:

- `GET /api/reservas-sala`
- `GET /api/reservas-sala/{id}`
- `POST /api/reservas-sala`
- `PUT /api/reservas-sala/{id}`
- `DELETE /api/reservas-sala/{id}`

Body create/update:

```json
{
  "idUsuario": {
    "id": 5
  },
  "idSala": {
    "id": 2
  },
  "fechaReserva": "2026-03-26T10:00:00Z",
  "fechaFinReserva": "2026-03-26T12:00:00Z"
}
```

## Recomendaciones para frontend

- Centraliza `fetch` con `credentials: "include"`.
- Antes de cualquier mutacion, asegúrate de tener token CSRF.
- Usa `/api/auth/me` al arrancar la app para saber si hay sesion activa.
- Guarda en estado frontend `id`, `mail` y `rol` del usuario autenticado.
- Oculta botones de admin si el rol no es `ROLE_ADMIN`.
- Si recibes `401`, redirige a login.
- Si recibes `403`, muestra error de permisos.
- Si recibes `400`, pinta los campos de `details`.
