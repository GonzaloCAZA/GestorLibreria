# API Backend para Implementacion Frontend

## Instrucciones para la IA del frontend

Usa este documento como contrato de integracion.

Reglas:

- Base URL local: `http://localhost:8080`
- Todos los requests al backend deben usar `credentials: "include"`.
- La sesion se guarda en cookie HttpOnly.
- La sesion dura 1 dia.
- Para cualquier `POST`, `PUT` o `DELETE`, primero obtén CSRF con `GET /api/auth/csrf`.
- Después envía el token en el header `X-XSRF-TOKEN`.
- Si recibes `401`, redirige a login.
- Si recibes `403`, muestra error de permisos.
- Si recibes `400`, pinta errores de validacion si existe `details`.
- Los listados principales ya son paginados.

Cliente base recomendado:

```ts
const API_URL = "http://localhost:8080";

export async function getCsrfToken() {
  const res = await fetch(`${API_URL}/api/auth/csrf`, {
    method: "GET",
    credentials: "include"
  });

  if (!res.ok) {
    throw new Error("No se pudo obtener CSRF");
  }

  const data = await res.json();
  return data.token;
}

export async function apiFetch(path: string, init: RequestInit = {}) {
  const method = (init.method ?? "GET").toUpperCase();
  const headers = new Headers(init.headers ?? {});

  if (method !== "GET" && method !== "HEAD" && method !== "OPTIONS") {
    const csrfToken = await getCsrfToken();
    headers.set("X-XSRF-TOKEN", csrfToken);
  }

  if (!headers.has("Content-Type") && init.body) {
    headers.set("Content-Type", "application/json");
  }

  const response = await fetch(`${API_URL}${path}`, {
    ...init,
    headers,
    credentials: "include"
  });

  if (response.status === 204) {
    return null;
  }

  const contentType = response.headers.get("content-type") ?? "";
  const data = contentType.includes("application/json")
    ? await response.json()
    : await response.text();

  if (!response.ok) {
    throw { status: response.status, data };
  }

  return data;
}
```

## Sesion

- Duracion actual: `86400000 ms`
- Equivale a 24 horas
- Login y register dejan la sesion iniciada

## Formato de error

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

## Paginacion

Endpoints paginados:

- `GET /api/autores`
- `GET /api/libros`
- `GET /api/libros/categorias/{categoria}`
- `GET /api/usuarios`
- `GET /api/salas`
- `GET /api/prestamos`
- `GET /api/reservas-sala`

Query params soportados:

- `page`: indice base 0
- `size`: tamano de pagina
- `sort`: formato `campo,direccion`

Ejemplo:

```http
GET /api/libros?page=0&size=12&sort=titulo,asc
```

Respuesta:

```json
{
  "content": [],
  "number": 0,
  "size": 12,
  "totalElements": 120,
  "totalPages": 10,
  "first": true,
  "last": false,
  "empty": false
}
```

## Auth

### `GET /api/auth/csrf`

Publico: `si`

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

Respuesta:

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

Respuesta:

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

Respuesta:

```json
{
  "message": "Logout correcto"
}
```

### `GET /api/auth/me`

Publico: `no`

Respuesta:

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

Respuesta:

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

Respuesta:

```json
{
  "message": "Contrasena actualizada correctamente"
}
```

## Roles y permisos

- `ROLE_CUSTOMER`: lectura de catalogo y uso de sesion.
- `ROLE_ADMIN`: acceso total.

Permisos actuales:

- `GET` catalogo: autenticado
- `POST/PUT/DELETE` catalogo: admin
- usuarios: admin
- prestamos: admin
- reservas de sala: admin

## Usuarios

Todos requieren `ROLE_ADMIN`.

### `GET /api/usuarios`

Query params opcionales:

- `rol`
- `moroso`
- `page`
- `size`
- `sort`

Respuesta:

```json
{
  "content": [
    {
      "id": 1,
      "mail": "admin@demo.com",
      "rol": "ROLE_ADMIN",
      "moroso": false,
      "creado": "2026-03-26T12:00:00Z",
      "actualizado": "2026-03-26T12:00:00Z"
    }
  ],
  "number": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true,
  "empty": false
}
```

### `GET /api/usuarios/{id}`

### `GET /api/usuarios/mail/{mail}`

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

### `DELETE /api/usuarios/{id}`

Respuesta: `204`

## Catalogo

## Autores

### `GET /api/autores`

Filtros:

- `nombre`
- `nacionalidad`
- `fechaNacimiento=YYYY-MM-DD`
- `page`
- `size`
- `sort`

### `GET /api/autores/{id}`

Respuesta:

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

### `POST /api/autores`

Admin.

```json
{
  "nombre": "Gabriel Garcia Marquez",
  "fechaNacimiento": "1927-03-06",
  "nacionalidad": "Colombiana"
}
```

### `PUT /api/autores/{id}`

Admin. Mismo body.

### `DELETE /api/autores/{id}`

Admin. Respuesta `204`.

## Pisos

### `GET /api/pisos`

### `GET /api/pisos/{id}`

Respuesta:

```json
{
  "id": 1,
  "numPiso": 1,
  "nombre": "Primera planta",
  "creado": "2026-03-26T12:00:00Z",
  "actualizado": "2026-03-26T12:00:00Z"
}
```

### `POST /api/pisos`

Admin.

```json
{
  "numPiso": 1,
  "nombre": "Primera planta"
}
```

### `PUT /api/pisos/{id}`

Admin. Mismo body.

### `DELETE /api/pisos/{id}`

Admin. Respuesta `204`.

## Estanterias

### `GET /api/estanterias`

### `GET /api/estanterias/{id}`

Respuesta:

```json
{
  "id": 1,
  "categoria": "Novela",
  "pisoId": 1,
  "pisoNumero": 1,
  "pisoNombre": "Primera planta",
  "creado": "2026-03-26T12:00:00Z",
  "actualizado": "2026-03-26T12:00:00Z"
}
```

### `POST /api/estanterias`

Admin.

```json
{
  "categoria": "Novela",
  "idPiso": {
    "id": 1
  }
}
```

### `PUT /api/estanterias/{id}`

Admin. Mismo body.

### `DELETE /api/estanterias/{id}`

Admin. Respuesta `204`.

## Baldas

### `GET /api/baldas`

### `GET /api/baldas/{id}`

Respuesta:

```json
{
  "id": 1,
  "numero": 3,
  "estanteriaId": 2,
  "estanteriaCategoria": "Novela",
  "pisoId": 1,
  "pisoNumero": 1,
  "pisoNombre": "Primera planta",
  "creado": "2026-03-26T12:00:00Z",
  "actualizado": "2026-03-26T12:00:00Z"
}
```

### `POST /api/baldas`

Admin.

```json
{
  "numero": 3,
  "idEstanteria": {
    "id": 2
  }
}
```

### `PUT /api/baldas/{id}`

Admin. Mismo body.

### `DELETE /api/baldas/{id}`

Admin. Respuesta `204`.

## Libros

### `GET /api/libros`

Filtro opcional:

- `titulo`
- `categoria`
- `page`
- `size`
- `sort`

Ejemplos:

- `/api/libros?page=0&size=12&sort=titulo,asc`
- `/api/libros?titulo=soledad&page=0&size=10`
- `/api/libros?categoria=Novela&page=0&size=10`

### `GET /api/libros/{id}`

### `GET /api/libros/isbn/{isbn}`

### `GET /api/libros/categorias/{categoria}`

Endpoint dedicado para pedir libros por categoria.

Ejemplo:

- `/api/libros/categorias/Novela?page=0&size=10&sort=titulo,asc`

Respuesta:

```json
{
  "content": [
    {
      "id": 1,
      "titulo": "Cien anos de soledad",
      "isbn": "9780307474728",
      "autorId": 1,
      "autorNombre": "Gabriel Garcia Marquez",
      "baldaId": 2,
      "baldaNumero": 3,
      "estanteriaId": 7,
      "estanteriaCategoria": "Novela",
      "pisoId": 1,
      "pisoNumero": 1,
      "pisoNombre": "Primera planta",
      "creado": "2026-03-26T12:00:00Z",
      "actualizado": "2026-03-26T12:00:00Z"
    }
  ],
  "number": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true,
  "empty": false
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

Admin. Mismo body.

### `DELETE /api/libros/{id}`

Admin. Respuesta `204`.

## Salas

### `GET /api/salas`

Filtros opcionales:

- `nombre`
- `maximoPersonas`
- `page`
- `size`
- `sort`

### `GET /api/salas/{id}`

Respuesta:

```json
{
  "id": 1,
  "nombre": "Sala 1",
  "maximoPersonas": 8,
  "pisoId": 1,
  "pisoNumero": 1,
  "pisoNombre": "Primera planta",
  "creado": "2026-03-26T12:00:00Z",
  "actualizado": "2026-03-26T12:00:00Z"
}
```

### `POST /api/salas`

Admin.

```json
{
  "nombre": "Sala 1",
  "maximoPersonas": 8,
  "idPiso": {
    "id": 1
  }
}
```

### `PUT /api/salas/{id}`

Admin. Mismo body.

### `DELETE /api/salas/{id}`

Admin. Respuesta `204`.

## Prestamos

Todos requieren `ROLE_ADMIN`.

### `GET /api/prestamos`

Filtros opcionales:

- `fechaPrestamo`
- `fechaDevolucionPrevista`
- `fechaDevolucionReal`
- `page`
- `size`
- `sort`

### `GET /api/prestamos/{id}`

Respuesta:

```json
{
  "id": 1,
  "usuarioId": 5,
  "usuarioMail": "cliente@demo.com",
  "libroId": 10,
  "libroTitulo": "Cien anos de soledad",
  "libroIsbn": "9780307474728",
  "fechaPrestamo": "2026-03-26",
  "fechaDevolucionPrevista": "2026-04-26",
  "fechaDevolucionReal": null,
  "creado": "2026-03-26T12:00:00Z",
  "actualizado": "2026-03-26T12:00:00Z"
}
```

### `POST /api/prestamos`

Admin.

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

### `PUT /api/prestamos/{id}`

Admin. Mismo body.

### `DELETE /api/prestamos/{id}`

Admin. Respuesta `204`.

## Reservas de sala

Todos requieren `ROLE_ADMIN`.

### `GET /api/reservas-sala`

Filtros opcionales:

- `fechaReserva`
- `fechaFinReserva`
- `page`
- `size`
- `sort`

### `GET /api/reservas-sala/{id}`

Respuesta:

```json
{
  "id": 1,
  "usuarioId": 5,
  "usuarioMail": "cliente@demo.com",
  "salaId": 2,
  "salaNombre": "Sala 1",
  "salaMaximoPersonas": 8,
  "pisoId": 1,
  "pisoNumero": 1,
  "pisoNombre": "Primera planta",
  "fechaReserva": "2026-03-26T10:00:00Z",
  "fechaFinReserva": "2026-03-26T12:00:00Z",
  "creado": "2026-03-26T09:00:00Z",
  "actualizado": "2026-03-26T09:00:00Z"
}
```

### `POST /api/reservas-sala`

Admin.

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

### `PUT /api/reservas-sala/{id}`

Admin. Mismo body.

### `DELETE /api/reservas-sala/{id}`

Admin. Respuesta `204`.

## Resumen rapido para implementar

- Usa `/api/auth/me` al arrancar la app.
- Guarda `id`, `mail`, `rol`.
- Oculta UI admin si `rol !== "ROLE_ADMIN"`.
- Usa los campos planos de respuesta como `autorNombre`, `pisoNumero`, `usuarioMail`.
- En formularios de escritura sigue enviando ids anidados por ahora:
  - `{ "idAutor": { "id": 1 } }`
  - `{ "idPiso": { "id": 1 } }`
  - `{ "idUsuario": { "id": 5 } }`
