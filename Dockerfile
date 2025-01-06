# Usamos una imagen de OpenJDK base
FROM openjdk:17-jdk-slim

# Instalar Maven
RUN apt-get update && apt-get install -y maven

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos el archivo pom.xml al contenedor
COPY pom.xml .

# Descargar las dependencias de Maven sin necesidad de compilar el proyecto
RUN mvn dependency:go-offline

# Copiamos el resto del código fuente
COPY src ./src

# Construir el JAR del proyecto
RUN mvn clean package

# Exponer el puerto que usará la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación (ajustar si es necesario)
CMD ["java", "-jar", "target/ToDoApp-0.0.1-SNAPSHOT.jar"]
