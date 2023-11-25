#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM --platform=linux/amd64 openjdk:11-jre-slim
COPY --from=build /home/app/target/miracle-0.0.1-SNAPSHOT.jar /usr/local/lib/miracle.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/usr/local/lib/miracle.jar"]