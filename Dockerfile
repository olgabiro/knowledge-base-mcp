FROM eclipse-temurin:21-jre-arm64

WORKDIR /app

COPY target/mcp-poc-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "app.jar"]
