# Fase 1: Build (opzionale se buildi prima con Maven sul tuo PC)
FROM eclipse-temurin:17-jre
# Copia il JAR generato da Maven nella cartella target
COPY target/*.jar app.jar
# Comando per avviare l'app
ENTRYPOINT ["java", "-jar", "/app.jar"]