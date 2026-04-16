FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw -B dependency:go-offline

COPY src/ src/
RUN ./mvnw -B -DskipTests package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

ENV SERVER_PORT=8080 \
    GG_JTE_DEVELOPMENT_MODE=false \
    GG_JTE_USE_PRECOMPILED_TEMPLATES=true \
    JAVA_OPTS="-XX:+UseG1GC -Xms256m -Xmx384m -Xss512k -Djava.security.egd=file:/dev/./urandom"

COPY --from=build /app/target/mysafefloridahome-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
