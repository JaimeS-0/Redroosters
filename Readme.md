# 🎵 RedRoosters – Plataforma musical

**RedRoosters** es una plataforma musical profesional que permite gestionar artistas, canciones y usuarios registrados.  
Incluye un panel de administración protegido, sistema de autenticación con JWT, gestión de favoritos.

Este proyecto está desarrollado para un productor musical y se ha construido con buenas prácticas en seguridad, arquitectura REST y despliegue con Docker y NGINX.

---

## Tecnologías principales

### 🖥️ Backend
- Java 21 + Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- PostgreSQL
- OpenAPI (Swagger)

### 🌐 Frontend
- Astro
- Tailwind CSS
- JavaScript (fetch/Axios)

### 🧰 DevOps
- Docker + Docker Compose
- NGINX como proxy inverso
- Archivos `.env` para configuración segura

---

## ⚙️ Cómo ejecutar (modo desarrollo)

1. Clona este repositorio
```
git clone [URL del repositorio]
```
2. Crea un archivo `.env` -> Mirar el `.env.example`


3. Ejecuta la app con Docker:
```
docker compose up --build
```
