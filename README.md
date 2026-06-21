# 🍔 Food Store — Sistema de Gestión de Pedidos de Comida

Trabajo Final Integrador — Programación 3  
Tecnicatura Universitaria en Programación a Distancia — UTN  
Ciclo lectivo 2026

---

## 📋 Descripción

Food Store es una aplicación web full stack orientada a la gestión de un negocio de comidas.
Permite a los administradores gestionar el catálogo y los pedidos, y a los clientes
navegar productos, armar un carrito y realizar compras.

---

## 🛠️ Tecnologías utilizadas

### Backend
- Java 17+
- Spring Boot 3.4.5
- Spring Data JPA / Hibernate
- PostgreSQL
- Lombok
- Gradle
- SpringDoc OpenAPI (Swagger)

### Frontend
- TypeScript
- Vite
- HTML5
- CSS3

---

## 📁 Estructura del proyecto

```text
TFI-FoodStore/
├── backend/
│   ├── src/main/java/pedidos/
│   │   ├── config/
│   │   ├── controllers/
│   │   ├── dtos/
│   │   ├── entidades/
│   │   ├── enums/
│   │   ├── exceptions/
│   │   ├── repositories/
│   │   └── services/
│   ├── src/main/resources/
│   │   └── application.properties
│   └── src/test/java/pedidos/
│       └── Tests unitarios e integración
├── frontend/
│   ├── src/
│   │   ├── css/
│   │   ├── pages/
│   │   │   ├── admin/
│   │   │   ├── auth/
│   │   │   ├── client/
│   │   │   └── store/
│   │   ├── types/
│   │   └── utils/
│   └── index.html
├── build.gradle
└── README.md

---
## ⚙️ Requisitos previos

- Java 17 o superior
- Node.js 18 o superior
- PostgreSQL 14 o superior
- NetBeans (recomendado para el backend)
- VSCode (recomendado para el frontend)

---

## 🚀 Instalación y configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/nicolaspannunzio/Programacion_3_TFI_UTN.git
cd TFI-FoodStore
```

### 2. Configurar la base de datos

Crear una base de datos en PostgreSQL:

```sql
CREATE DATABASE delivery_app_p3;
```

### 3. Configurar el backend

Abrí `backend/src/main/resources/application.properties` y configurá tus credenciales:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/delivery_app_p3
spring.datasource.username=postgres
spring.datasource.password=TU_CONTRASEÑA
```

### 4. Ejecutar el backend

Desde NetBeans, abrí el proyecto `backend/` y ejecutá con **Run** (F6).

El servidor arranca en `http://localhost:8080`.

Al iniciar por primera vez, se crea automáticamente un usuario administrador:
- **Email:** admin@admin.com
- **Contraseña:** 123456

### 5. Ejecutar el frontend

```bash
cd frontend
npm install
npm run dev
```

El frontend queda disponible en `http://localhost:5173`.

---

## 👤 Roles del sistema

| Rol | Acceso |
|-----|--------|
| ADMIN | Panel de administración, CRUD de categorías, productos y pedidos |
| USUARIO | Catálogo, carrito, checkout, historial de pedidos |

---

## 🔗 Endpoints principales de la API

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | /api/auth/login | Iniciar sesión |
| POST | /api/auth/register | Registrar usuario |
| GET | /api/categories | Listar categorías |
| POST | /api/categories | Crear categoría |
| PUT | /api/categories/{id} | Actualizar categoría |
| DELETE | /api/categories/{id} | Eliminar categoría |
| GET | /api/products | Listar productos |
| POST | /api/products | Crear producto |
| PUT | /api/products/{id} | Actualizar producto |
| DELETE | /api/products/{id} | Eliminar producto |
| GET | /api/orders | Listar todos los pedidos |
| POST | /api/orders | Crear pedido |
| GET | /api/orders/usuario/{id} | Pedidos por usuario |
| PUT | /api/orders/{id} | Actualizar estado del pedido |

---
## Documentación interactiva disponible en

http://localhost:8080/swagger-ui/index.html


## 🎥 Video demostración

👉 [Drive](https://drive.google.com/drive/folders/1l4eFkg1P3TOgYG9FCds0FD9-IniPT5E8)

---
## 📄 Documentación PDF

👉 [Descargar informe](InformeFinal-Pannunzio_Nicolas.pdf)

---

## 🏗️ Decisiones técnicas destacadas

- **Soft delete:** ninguna entidad se elimina físicamente de la base de datos.
Se marca con `eliminado = true` y se filtra en todas las consultas.
- **BaseRepository:** repositorio genérico que centraliza el soft delete,
el `findByIdOrThrow` y el filtrado de eliminados para todas las entidades.
- **Manejo global de excepciones:** `@RestControllerAdvice` centraliza los errores
devolviendo siempre un JSON con `status`, `mensaje` y `timestamp`.
- **Autenticación con localStorage:** sin JWT, solo para fines educativos.
El rol se valida en el frontend según los datos guardados en sesión.
- **Transacciones:** la creación de pedidos usa `@Transactional` para garantizar
que si falla alguna validación de stock, se hace rollback completo.

---

## 📚 Bibliografía y recursos

- [Documentación oficial Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Documentación oficial Vite](https://vitejs.dev/guide/)
- [Documentación oficial TypeScript](https://www.typescriptlang.org/docs/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Lombok](https://projectlombok.org/)
- [SpringDoc OpenAPI](https://springdoc.org/)


## Autor

**Nicolás A. Pannunzio** – Full Stack Developer & QA Automation 
🔗 [Perfil de LinkedIn](https://www.linkedin.com/in/nicolas-a-pannunzio-/)

