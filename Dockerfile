# Estágio de construção
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY . .
# Conceda permissões de execução ao mvnw
RUN chmod +x mvnw

# Execute o comando mvnw clean install
RUN ./mvnw clean install

# Estágio de execução
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar api.jar
EXPOSE 8080
CMD ["java", "-jar", "api.jar"]
