FROM adoptopenjdk/openjdk11:alpine-jre
COPY target/*.jar app.jar
COPY groups.csv groups.csv
COPY items.csv items.csv
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8084