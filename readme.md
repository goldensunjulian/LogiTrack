# 📦 LogiTrack — API REST de Gestión de Bodegas e Inventarios

Backend desarrollado en **Spring Boot** para **LogiTrack S.A.**, orientado al control centralizado de bodegas, productos, inventario en tiempo real, trazabilidad de movimientos logísticos (entradas, salidas y transferencias) y auditoría automática de cambios en base de datos.

---

## 🚀 Tecnologías utilizadas

* **Java 21**
* **Spring Boot 4.1.2** (Web MVC, Data JPA, Security, Validation)
* **Hibernate ORM 7.4**
* **MySQL 8.0**
* **JWT (`jjwt 0.11.5`)** para autenticación *stateless* basada en tokens
* **SpringDoc OpenAPI 3 / Swagger UI** para documentación interactiva de la API
* **Lombok**

---

## 🏗️ Estructura del proyecto

```text
src/main/java/com/example/LogiTrack/
├── auth/          # Autenticación, generación/validación de JWT y filtro de seguridad
├── config/        # Configuración de Spring Security, CORS y Swagger/OpenAPI
├── controller/    # Controladores REST que exponen los endpoints /api/** y /auth/**
├── dto/           # Objetos de transferencia de datos (Request / Response) con validaciones
├── exception/     # Excepciones personalizadas y manejo global (@RestControllerAdvice)
├── mapper/        # Conversión entre entidades JPA y DTOs
├── modelo/        # Entidades JPA mapeadas a las tablas de MySQL
├── repository/    # Interfaces Spring Data JPA y consultas personalizadas
├── service/       # Lógica de negocio, reglas de stock y control transaccional
└── util/          # Utilidades transversales (AuditoriaListener y serialización JSON)
```

---

## 📋 Requisitos previos

* **JDK 21** instalado y configurado en el `PATH`.
* **MySQL Server 8.0** en ejecución.
* **Maven** (o el wrapper `./mvnw` incluido en el proyecto).

---

## ⚙️ Instalación y ejecución

### 1. Crear la base de datos
Ejecuta el script `database_terminal.sql` incluido en el proyecto para crear el esquema `logitrack_db`, las tablas, los roles y los datos iniciales:

```bash
mysql -u root -p < database_terminal.sql
```

### 2. Configurar credenciales de MySQL
Verifica o ajusta tus credenciales locales en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/logitrack_db
spring.datasource.username=root
spring.datasource.password=tu_contraseña
```

### 3. Levantar el servidor
Desde la raíz del proyecto ejecuta:

```bash
./mvnw spring-boot:run
```
*(En Windows puedes usar `mvnw.cmd spring-boot:run` o ejecutar `LogiTrackApplication` directamente desde IntelliJ IDEA).*

El servidor quedará activo en `http://localhost:8080`.

---

## 🔑 Usuarios de prueba (Data inicial)

El script `database_terminal.sql` precarga tres usuarios con distintos niveles de acceso:

| Rol | Username | Contraseña | Permisos principales |
| :--- | :--- | :--- | :--- |
| `SUPERADMIN` | `julian` | `1234` | Control total: usuarios, credenciales, auditoría, bodegas, categorías, productos e inventario. |
| `ADMIN` | `admin` | `1234` | Gestión de bodegas, categorías, productos y operación completa de inventario y movimientos. |
| `EMPLEADO` | `carlos` | `1234` | Consulta de catálogo/inventario, registro de movimientos y visualización de reportes. |

---

## 🧪 Documentación y pruebas de la API

Con la aplicación en ejecución, puedes explorar y probar todos los endpoints desde **Swagger UI**:

* **Swagger UI:** `http://localhost:8080/swagger-ui.html`
* **OpenAPI JSON:** `http://localhost:8080/v3/api-docs`

### Flujo básico de autenticación
1. Envía una petición `POST /auth/login` con:
   ```json
   {
     "username": "julian",
     "password": "1234"
   }
   ```
2. Copia el `token` devuelto en la respuesta.
3. En Swagger UI haz clic en **Authorize** e ingresa el token (o envía el encabezado `Authorization: Bearer <token>` en Postman/cURL).

---

## 📌 Endpoints principales

| Módulo | Base URL | Descripción |
| :--- | :--- | :--- |
| **Autenticación** | `/auth` | Inicio de sesión (`/login`) y registro de empleados (`/crearEmpleado`). |
| **Usuarios** | `/api/usuario` | Gestión de usuarios y activación/desactivación lógica. |
| **Credenciales** | `/api/credenciales` | Consulta de cuentas y roles asignados. |
| **Bodegas** | `/api/bodega` | CRUD de bodegas, asignación de encargados y filtro de activas. |
| **Categorías** | `/api/categoria` | Gestión de categorías de productos. |
| **Productos** | `/api/producto` | Catálogo de productos, precios y activación/desactivación. |
| **Inventario** | `/api/inventario` | Stock por bodega, alertas de stock bajo (`/stockBajo`) y reporte consolidado. |
| **Movimientos** | `/api/movimiento` | Registro de entradas, salidas y transferencias entre bodegas. |
| **Detalle Mov.** | `/api/movimientodetalle` | Ítems por movimiento y reporte de productos más movilizados. |
| **Auditoría** | `/api/auditoria` | Historial automático (`INSERT`, `UPDATE`, `DELETE`) con estado antes y después. |

---

## 👨‍💻 Autor

Desarrollado por **Julian Tristan** — © 2026 LogiTrack S.A.