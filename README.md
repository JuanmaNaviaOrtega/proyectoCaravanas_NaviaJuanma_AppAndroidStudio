# App Android - Reservas de Autocaravanas

Aplicación móvil Android desarrollada en **Kotlin** para la gestión de reservas de autocaravanas del Proyecto AutoCaravanas "Una vida para disfrutar". Permite a los usuarios registrarse, iniciar sesión, consultar vehículos disponibles, crear, editar y eliminar reservas, así como gestionar su perfil. La app se comunica con un **API RESTful** desarrollado en Laravel, usando **Retrofit** y el patrón **MVVM**.

---

## Tabla de Contenidos

- [Características](#características)
- [Arquitectura y Tecnologías](#arquitectura-y-tecnologías)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Uso de la Aplicación](#uso-de-la-aplicación)
- [Comunicación con el API](#comunicación-con-el-api)
- [Gestión de Reservas](#gestión-de-reservas)
- [Gestión de Vehículos](#gestión-de-vehículos)
- [Gestión de Usuario y Perfil](#gestión-de-usuario-y-perfil)
- [Pagos y Seguridad](#pagos-y-seguridad)
- [Notas y Mejoras](#notas-y-mejoras)
- [Licencia](#licencia)

---

## Características

- **Registro e inicio de sesión** de usuarios (con persistencia de token JWT).
- **Listado de vehículos** y consulta de disponibilidad por fechas.
- **Creación, edición y eliminación de reservas**.
- **Visualización de reservas activas** y detalle de cada reserva.
- **Gestión de perfil de usuario**.
- **Validaciones**: máximo 5 reservas futuras, mínimo 2 días (julio/agosto mínimo 7), máximo 60 días de antelación.
- **Interfaz moderna** con Material Design y navegación por fragments.
- **Persistencia de sesión** usando SharedPreferences.
- **Gestión de errores y estados de carga**.

---

## Arquitectura y Tecnologías

- **Lenguaje:** Kotlin
- **Arquitectura:** MVVM (Model-View-ViewModel)
- **Networking:** Retrofit2 + Gson
- **Persistencia local:** SharedPreferences
- **UI:** Material Design, Navigation Component, RecyclerView, CardView
- **Gestión de dependencias:** Gradle
- **API REST:** Laravel

---

## Estructura del Proyecto

```
app/
├── API/
│   ├── ApiService.kt
│   ├── RetrofitClient.kt
│   └── responses/
│       └── CreateReservaRequest.kt
├── Auth/
│   ├── AuthViewModel.kt
│   ├── LoginFragment.kt
│   └── RegisterFragment.kt
├── gestiosReservas/
│   └── UpdateReservaRequest.kt
├── Models/
│   ├── Reserva.kt
│   ├── Resource.kt
│   ├── User.kt
│   └── Vehiculo.kt
├── reservations/
│   ├── adapters/ReservasAdapter.kt
│   ├── CreateReservaFragment.kt
│   ├── EditarReservaFragment.kt
│   ├── ReservaDetailFragment.kt
│   ├── ReservasFragment.kt
│   ├── ReservaViewModel.kt
│   └── ReservaViewModelFactory.kt
├── utils/
│   └── SharedPrefs.kt
├── vehicles/
│   ├── adapter/VehiculosAdapter.kt
│   ├── ListadoCaravanasFragment.kt
│   ├── VehiculosDisponiblesFragment.kt
│   ├── VehiclesFragment.kt
│   └── VehiculoViewModel.kt
├── HomeFragment.kt
├── MainActivity.kt
└── ProfileFragment.kt
```

---



## Uso de la Aplicación

### 1. **Registro y Login**
- Desde la pantalla de inicio, puedes registrarte o iniciar sesión.
- El token de autenticación se guarda en SharedPreferences y se usa en todas las peticiones protegidas.

### 2. **Navegación Principal**
- **Inicio:** Acceso rápido a reservas, vehículos, perfil y listado de caravanas.
- **Menú lateral:** Navegación entre las principales secciones.

### 3. **Reservas**
- Consulta tus reservas activas.
- Crea nuevas reservas seleccionando fechas y vehículo disponible.
- Edita o elimina reservas existentes (máximo 5 futuras).
- Visualiza el detalle de cada reserva.

### 4. **Vehículos**
- Consulta el listado de todos los vehículos.
- Busca vehículos disponibles para unas fechas concretas.

### 5. **Perfil**
- Consulta tus datos personales.
- Cierra sesión desde el perfil.

---

## Comunicación con el API

La app se comunica con el backend Laravel a través de un **API RESTful** protegido con tokens (Sanctum).  
**Principales endpoints usados:**

- **Autenticación:**
  - `POST /api/login` (login)
  - `POST /api/register` (registro)
- **Reservas:**
  - `GET /api/reservas` (listar reservas del usuario)
  - `POST /api/reservas` (crear reserva)
  - `GET /api/reservas/{id}` (detalle)
  - `PUT /api/reservas/{id}` (editar)
  - `DELETE /api/reservas/{id}` (eliminar)
- **Vehículos:**
  - `GET /api/vehiculos` (todos)
  - `GET /api/vehiculos/disponibles?fecha_inicio=YYYY-MM-DD&fecha_fin=YYYY-MM-DD`
- **Usuario:**
  - `GET /api/user/profile`

**Todas las rutas requieren el header:**
```
Authorization: Bearer <token>
```

---

## Gestión de Reservas

- **Restricciones:**
  - Máximo 5 reservas futuras por usuario.
  - Mínimo 2 días de reserva (julio/agosto mínimo 7 días).
  - Máximo 60 días de antelación.

- **Estados:**
  - Solo se muestran reservas futuras.
  - Las reservas pasadas se mueven a un historial (visible solo para el admin en el backend).

---

## Gestión de Vehículos

- Consulta todos los vehículos o solo los disponibles para unas fechas.
- Visualización en listas con RecyclerView y CardView.

---

## Gestión de Usuario y Perfil

- Consulta de datos personales.
- Cierre de sesión (elimina el token local).

---



---

## Notas y Mejoras

- **MVVM:** Toda la lógica de negocio y acceso a datos está separada de la UI.
- **Retrofit:** Todas las llamadas a la API son asíncronas y gestionan estados de carga y error.
- **Validaciones:** La app valida fechas y restricciones antes de enviar las reservas.
- **Mejoras posibles:**
  - Notificaciones push para cambios en reservas.
  - Soporte offline (Room).
  - Gestión de imágenes de vehículos.
  - Panel de administración móvil.

---

## Licencia

Proyecto realizado para la asignatura de PMDM (2º DAM).  
**Autor:** Juanma Navia Ortega  


---

