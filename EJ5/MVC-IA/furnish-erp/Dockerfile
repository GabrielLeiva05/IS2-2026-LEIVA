# ==============================================================================
# Dockerfile - build multi-stage de Furnish ERP
# ==============================================================================
# Etapa 1 (build): usa una imagen con Maven + JDK 21 para compilar el
# proyecto y generar el JAR ejecutable. Esta imagen NO forma parte de la
# imagen final (se descarta), por lo que la imagen resultante es liviana.
# ==============================================================================
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

# Se copia primero solo el pom.xml para aprovechar la cache de capas de
# Docker: si las dependencias no cambian, no se vuelven a descargar en cada
# build aunque cambie el código fuente.
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

# ==============================================================================
# Etapa 2 (runtime): imagen final, liviana, solo con el JRE necesario para
# ejecutar la aplicación (no incluye Maven ni el código fuente).
# ==============================================================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /build/target/furnish-erp.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
