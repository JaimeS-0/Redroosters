
# Imagen con JDK y Maven para compilar el proyecto
FROM eclipse-temurin:21-jdk AS build

# Se copiará el código del proyecto
WORKDIR /app

# Copia todo el contenido del backend
COPY . .

# Compila el proyecto con Maven y genera el jar
RUN ./mvnw package -DskipTests

# Una imagen más ligera solo con JRE para ejecutar el jar
FROM eclipse-temurin:21-jre

#  Instalamos ffmpeg en la imagen runtime para calcular la duración de las canciones automaticamente
RUN apt-get update && apt-get install -y ffmpeg && rm -rf /var/lib/apt/lists/*

# Directorio donde se colocará el jar final
WORKDIR /app

# Copia el jar desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto interno del contenedor
EXPOSE 9000

# Arranca la aplicación Spring Boot
ENTRYPOINT ["java","-jar","app.jar"]
