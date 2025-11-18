# ETAPA 1: BUILD (Compilación y Empaquetado)
# Usamos una imagen con JDK para construir el proyecto.
FROM eclipse-temurin:21-jdk-alpine AS build

# INSTALACIÓN DE MAVEN Y DEPENDENCIAS
# Instala Maven usando el gestor de paquetes Alpine (apk)
RUN apk update && apk add --no-cache maven

WORKDIR /app
# Copia el archivo de configuración de Maven
COPY pom.xml .
# Copia el código fuente
COPY src /app/src

# Compila y empaqueta la aplicación
RUN mvn clean package

# ----------------------------------------------------------------------

# ETAPA 2: RUNTIME (Ejecución)
FROM eclipse-temurin:21-jre-alpine

# Argumento para obtener el nombre del JAR (ajusta si es necesario)
ARG JAR_FILE=/app/target/*.jar

# Copia el JAR final desde la etapa de construcción a la imagen final
COPY --from=build ${JAR_FILE} app.jar

# Puerto por defecto de Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app.jar"]