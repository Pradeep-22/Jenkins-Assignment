FROM openjdk:17-jdk-slim

WORKDIR /app

COPY MyWebApp/target/MyWebApp.war /app/MyWebApp.war

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/MyWebApp.war"]
