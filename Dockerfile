FROM eclipse-temurin:17-jdk-alpine as builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean install -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup nonrootgroup; adduser --ingroup nonrootgroup --disabled-password nonrootuser; chown -R nonrootuser:nonrootgroup /app
USER nonrootuser
EXPOSE 8080
COPY --from=builder /app/target/*.jar /app/*.jar
ENTRYPOINT [ "java", "-jar", "/app/*.jar" ]