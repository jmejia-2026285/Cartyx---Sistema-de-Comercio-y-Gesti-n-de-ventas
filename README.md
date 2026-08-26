# auditoria-usuario-producto-in4av

Sistema de tienda con productos, login con roles, y auditoría de acciones.

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

