# =============================
# Etapa 1: Construcción del frontend con Astro
# =============================

# Imagen base ligera de Node.js 20
FROM node:20-alpine3.19 AS build

# Carpeta de trabajo dentro del contenedor
WORKDIR /app

# Copio los archivos de dependencias primero
# Esto permite aprovechar la caché de Docker si no cambian
COPY package.json package-lock.json* ./

# Instalo las dependencias con "npm ci"
# (instala exactamente lo que está en package-lock.json, limpio y reproducible)
RUN npm ci

# Copio todo el código del frontend al contenedor
COPY . .

# Compilo el proyecto Astro -> genera la carpeta "dist" con HTML/CSS/JS estáticos
RUN npm run build


# =============================
# Etapa 2: Servir los estáticos con Nginx
# =============================

# Imagen base de Nginx muy ligera
FROM nginx:1.27-alpine

# copiamos el build de Astro a la carpeta que sirve nginx
COPY --from=build /app/dist /usr/share/nginx/html

# Defino un chequeo de salud:
# cada 30s hace un wget a http://localhost (dentro del contenedor)
# si falla, Docker marca el contenedor como unhealthy
# HEALTHCHECK --interval=30s --timeout=3s CMD wget -qO- http://localhost/ || exit 1

# Expongo el puerto 80, que es donde Nginx escucha por defecto
EXPOSE 80
