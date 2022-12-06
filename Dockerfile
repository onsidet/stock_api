## For Java 8, try this
## FROM openjdk:8-jdk-alpine
#
## For Java 11, try this
#FROM adoptopenjdk/openjdk11:alpine-jre
#
#VOLUME /logs
#
## Refer to Maven build -> finalName
#ARG JAR_FILE=target/bbu-app-server-0.0.1-SNAPSHOT.jar
#
## cd /opt/app
#WORKDIR /opt/bbu-app-server
#
## cp target/spring-boot-web.jar /opt/app/app.jar
#COPY ${JAR_FILE} bbu-app-server.jar
#
#EXPOSE 20033
## java -jar /opt/app/app.jar
#ENTRYPOINT ["java","-Duser.timezone=TH","-jar","bbu-app-server.jar"]

FROM openjdk:11
EXPOSE 8181
ADD target/stock_api-0.0.1-SNAPSHOT.war stock_api.jar
ENTRYPOINT ["java","-jar","/stock_api.jar"]