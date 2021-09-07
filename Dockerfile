FROM openjdk:8-jdk-alpine

ARG JAR_FILE=backend/target/*.jar

COPY ${JAR_FILE} app.jar
COPY key.pem key.pem
COPY cert.pem cert.pem
COPY keystore.p12 keystore.p12
COPY database/db_backup/user_images.zip user_images.zip

RUN unzip user_images.zip -d . && rm user_images.zip

ENTRYPOINT ["java","-jar","/app.jar"]

EXPOSE 8080 4200
