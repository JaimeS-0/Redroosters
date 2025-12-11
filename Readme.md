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
- JavaScript

### 🧰 DevOps
- Docker + Docker Compose
- NGINX como proxy inverso
- Archivos `.env` para configuración segura

---

## ⚙️ Cómo ejecutar en local (modo desarrollo)

1. Clona este repositorio
```
https://github.com/JaimeS-0/Redroosters.git
```
2. Entra en la carpeta
```
cd redroosters
```
3. Crea un archivo `.env` -> Mirar el `.env.example` para guia
(ejecuta este comando para renombrarlo a .env)
```
cp .env.example .env
```
4. Ejecuta la app con Docker:
```
docker compose up --build -d 
```

#### 📋 La documentacion de todo el proyecto la tengo por separada en Notion. (me falta ajustar muchas cosas nuevas)






