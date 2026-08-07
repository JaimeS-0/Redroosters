
# --- DEV: Modo desarrollo con Astro y hot reload ---
FROM node:20-alpine AS dev
WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . .

EXPOSE 3000
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0", "--port", "3000"]


# --- BUILD: Compilación de Astro SSR ---
FROM node:20-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm install 

COPY . .
RUN npm run build


# --- PROD: Servidor dinámico Node.js ---
FROM node:20-alpine AS prod
WORKDIR /app

COPY package*.json ./
RUN npm install --omit=dev

COPY --from=build /app/dist ./dist

ENV HOST=0.0.0.0
ENV PORT=80
EXPOSE 80

CMD ["node", "./dist/server/entry.mjs"]