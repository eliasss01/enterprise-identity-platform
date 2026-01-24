FROM eclipse-temurin:17-jre
WORKDIR /app
# Prende il file generato dal tuo comando Maven locale
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]