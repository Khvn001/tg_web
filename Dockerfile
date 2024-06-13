FROM eclipse-temurin:22-jdk-alpine
ARG JAR_FILE=service-client/target/*.jar
COPY ${JAR_FILE} app.jar
RUN apk add --no-cache fontconfig ttf-dejavu
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]