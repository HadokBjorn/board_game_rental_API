# Estágio de construção
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . .

# Resolva as dependências do Maven e compile o projeto
RUN mvn clean package -DskipTests

# Etapa de execução
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar api.jar
EXPOSE 8080
CMD ["java", "-jar", "api.jar"]

