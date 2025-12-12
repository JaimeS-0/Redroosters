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
Este archivo contiene TODAS las variables necesarias:
puertos - Base de datos - Credenciales del admin - URLs internas/externas del backend

4. Ejecuta la app con Docker:
```
docker compose up --build -d 
```

## Esto levantara:

<table>
  <thead>
    <tr>
      <th>Servicio</th>
      <th>Puerto local</th>
      <th>Descripción</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Frontend (Astro)</td>
      <td><a href="http://localhost" target="_blank">http://localhost</a></td>
      <td>Sirve toda la web</td>
    </tr>
    <tr>
      <td>Backend (Spring Boot)</td>
      <td><a href="http://localhost:9000" target="_blank">http://localhost:9000</a></td>
      <td>API + Swagger</td>
    </tr>
    <tr>
      <td>PostgreSQL</td>
      <td>localhost:5432</td>
      <td>Base de datos</td>
    </tr>
    <tr>
      <td>NGINX</td>
      <td>puerto 80</td>
      <td>Reverse proxy para front + API</td>
    </tr>
  </tbody>
</table>

## ⚠️ Algunas funciones de la plataforma solo están disponibles para ==administradores==.
Para tener todas las funcionalidades de la app, inicia sesión con las credenciales definidas en tu archivo .env, 
* por defecto son:
```
ADMIN_EMAIL=admin@gmail.com
ADMIN_PASSWORD=12345678910

```

### Tengo pendite crear una rama prod para poder desplegar la app sin errores y tener una rama en locar y otra en produccion.

#### 📋 La documentacion de todo el proyecto la tengo por separada en Notion. (me falta ajustar muchas cosas nuevas)















