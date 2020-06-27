FROM adoptopenjdk/openjdk11:alpine-jre
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080