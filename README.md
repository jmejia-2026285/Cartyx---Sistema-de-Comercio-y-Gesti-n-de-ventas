# auditoria-usuario-producto-in4av

Sistema de tienda con productos, login con roles, y auditoría de acciones.

## Qué hace el sistema
- Cualquier persona puede registrarse desde la app (queda con rol `cajero`).
- Con sesión iniciada, cualquier usuario administra productos: crear, editar, eliminar, listar.
- Solo el rol `administrador` puede ver el panel de Auditoría, donde aparece cada
  inicio de sesión, cierre de sesión, y cada creación/edición/eliminación de producto,
  con quién lo hizo y cuándo.

## Cómo correrlo
1. Ejecuta `src/org/josemejia/system/config/DDL_auditoria.sql` en tu MySQL (crea la base
   y las tablas).
2. Ejecuta `src/org/josemejia/system/config/CRUD_auditoria.sql` en tu MySQL (crea los
   procedimientos almacenados que usa la aplicación para el create/read/update/delete).
3. La tabla `usuarios` empieza vacía. Crea tu primer administrador a mano, una sola vez,
   directamente en MySQL Workbench, llamando al procedimiento almacenado:
   ```sql
   call sp_usuario_crear('Admin', 'Sistema', 'admin@correo.com', 'admin', '123', 'administrador');
   ```
4. Revisa que `src/org/josemejia/system/config/Enviroment.java` tenga tu usuario y
   contraseña reales de MySQL (ese archivo no se tocó, sigue como lo tenías).
5. Corre el proyecto normal desde NetBeans (F6), o `ant run` desde la terminal.
6. Cualquiera que se registre desde el formulario de la app queda como `cajero`
   automáticamente.

## Estructura de paquetes
```
org.josemejia.system
 ├── config      -> conexión a la base de datos y variables de entorno
 ├── controller  -> un controlador Java por cada FXML
 ├── model       -> User, Producto, Auditoria (clases planas, solo datos)
 ├── repository  -> acceso a la base de datos con CallableStatement, llamando a los
 │                  procedimientos almacenados definidos en CRUD_auditoria.sql
 ├── service     -> reglas del negocio (login, permisos, registrar auditoría)
 ├── utils       -> SceneManager, SessionManager, ViewFactory
 └── view        -> archivos .fxml y .css
```

## Flujo de navegación
```
Login → (Registrarse) → Registro → (Guardar) → Login
Login → (Ingresar) → Dashboard
Dashboard → (Productos) → gestión de productos → (Volver al Menú) → Dashboard
Dashboard → (Auditoría, solo administrador) → tabla de auditoría → (Volver al Menú) → Dashboard
Dashboard → (Cerrar Sesión) → Login
```

## Guía de Scene Builder por ventana

### LoginView.fxml
fx:controller: `org.josemejia.system.controller.LoginController`

| Componente | fx:id | Evento |
|---|---|---|
| TextField | txtUsuario | — |
| PasswordField | txtPassword | — |
| Label | lblError | — |
| Button | btnLogin | onAction="#handleLogin" |
| Hyperlink | linkRegistro | onAction="#handleIrARegistro" |

### CreateAccountView.fxml
fx:controller: `org.josemejia.system.controller.RegistroController`

| Componente | fx:id | Evento |
|---|---|---|
| TextField | txtNombre | — |
| TextField | txtApellidos | — |
| TextField | txtUsuario | — |
| TextField | txtCorreo | — |
| PasswordField | txtPassword | — |
| PasswordField | txtConfirmarPassword | — |
| Label | lblError | — |
| Button | btnRegistrarse | onAction="#handleRegistrarse" |
| Hyperlink | linkVolverLogin | onAction="#handleVolverLogin" |

### DashboardView.fxml
fx:controller: `org.josemejia.system.controller.DashboardController`

| Componente | fx:id | Evento |
|---|---|---|
| Label | lblUsuario | — |
| Button ("Productos") | (sin fx:id) | onAction="#handleProductos" |
| Button ("Auditoría") | btnAuditoria | onAction="#handleAuditoria" |
| Button ("Cerrar Sesión") | (sin fx:id) | onAction="#handleCerrarSesion" |

### ProductoView.fxml
fx:controller: `org.josemejia.system.controller.ProductoController`

| Componente | fx:id | Evento |
|---|---|---|
| TableView | tablaProductos | — |
| TableColumn | colNombre | — |
| TableColumn | colPrecio | — |
| TableColumn | colDescripcion | — |
| TableColumn | colCategoria | — |
| TextField | txtNombre | — |
| TextField | txtPrecio | — |
| TextField | txtDescripcion | — |
| TextField | txtCategoria | — |
| Button ("Guardar") | (sin fx:id) | onAction="#handleGuardar" |
| Button ("Eliminar") | (sin fx:id) | onAction="#handleEliminar" |
| Button ("Limpiar") | (sin fx:id) | onAction="#handleLimpiar" |
| Button ("Volver al Menú") | (sin fx:id) | onAction="#handleVolver" |

### AuditoriaView.fxml
fx:controller: `org.josemejia.system.controller.AuditoriaController`

| Componente | fx:id | Evento |
|---|---|---|
| TableView | tablaAuditoria | — |
| TableColumn | colUsuario | — |
| TableColumn | colAccion | — |
| TableColumn | colEntidad | — |
| TableColumn | colDetalle | — |
| TableColumn | colFecha | — |
| Button ("Volver al Menú") | (sin fx:id) | onAction="#handleVolver" |

## Notas
- Las contraseñas se guardan tal cual las escribe el usuario (texto plano), igual que
  en la versión original del proyecto. No se agregó ningún tipo de cifrado.
- El rol se asigna siempre automáticamente en el registro público (`cajero`).
  El único `administrador` es el que se inserta a mano en el paso 3 de "Cómo correrlo".
- Toda la base de datos (tablas, columnas, procedimientos) está en minúsculas.
- Todo el acceso a datos (create, read, update, delete) pasa por procedimientos
  almacenados definidos en `CRUD_auditoria.sql`, no por inserts/selects sueltos en
  el código Java. Cada repositorio (`UserRepository`, `ProductoRepository`,
  `AuditoriaRepository`) invoca esos procedimientos con `CallableStatement`.
