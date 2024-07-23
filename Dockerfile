#FROM openjdk:17-jdk-alpine
#
#RUN addgroup -S app && adduser -S app -G app
#
#USER app
#
#COPY target/*.jar app.jar
#
#ENTRYPOINT ["java", "-jar", "/app.jar"]


#FROM eclipse-temurin:17-jdk-focal
#
#WORKDIR /app
#
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#RUN ./mvnw dependency:go-offline
#
#COPY src ./src
#
#CMD ["./mvnw", "spring-boot:run"]


FROM openjdk:17-jdk-alpine
EXPOSE 8080
ADD target/spring-boot-docker.jar spring-boot-docker.jar
ENTRYPOINT ["java","-jar","/spring-boot-docker.jar"]

