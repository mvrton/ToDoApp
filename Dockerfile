# Usar una imagen base con Maven y OpenJDK 17
FROM maven:3.8.6-openjdk-17-slim AS build

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el código fuente del proyecto al contenedor
COPY . .

# Ejecutar Maven para compilar el proyecto y generar el JAR
RUN mvn clean install

# Usar una imagen base más ligera con solo OpenJDK 17 para ejecutar la app
FROM openjdk:17-slim

# Copiar el archivo JAR generado al contenedor final
COPY --from=build /app/target/ToDoApp-0.0.1-SNAPSHOT.jar /app/ToDoApp.jar

# Ejecutar la aplicación Java
CMD ["java", "-jar", "/app/ToDoApp.jar"]
