

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


# --- BUILD: Compilo Astro para producción ---

FROM node:20-alpine AS build
WORKDIR /app

# Instalo dependencias y compilo el proyecto
COPY package*.json ./
RUN npm ci --silent --no-audit --prefer-offline
COPY . .
RUN npm run build


# --- PROD: versión final que sirve los archivos estáticos con Nginx ---

FROM nginx:1.27-alpine

# Copio el resultado del build a la carpeta que Nginx usa por defecto
COPY --from=build /app/dist /usr/share/nginx/html

# Expongo el puerto 80, donde escuchará Nginx
EXPOSE 80
