

# --- DEV: modo desarrollo con Astro y hot reload ---

FROM node:20-alpine AS dev
WORKDIR /app

# Copio los archivos de dependencias
COPY package*.json ./

# Instalo las dependencias con npm ci
RUN npm ci

# Copio todo el proyecto al contenedor
COPY . .

# Expongo el puerto 3000 (donde corre el servidor de Astro en dev)
EXPOSE 3000

# Arranco Astro en modo desarrollo, escuchando en todas las IPs
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0", "--port", "3000"]


# --- BUILD: Compilo Astro a estático ---
FROM node:20-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm install 
COPY . .
RUN npm run build

# --- PROD: Servidor Nginx que sirve los estáticos ---
FROM nginx:1.27-alpine

# Limpia los archivos por defecto de Nginx
RUN rm -rf /usr/share/nginx/html/*

# Copia los archivos estáticos desde /app/dist (NO desde /dist/client)
COPY --from=build /app/dist /usr/share/nginx/html

# Asegura permisos de lectura
RUN chmod -R 755 /usr/share/nginx/html

EXPOSE 80
