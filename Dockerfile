# docker build -t springio/busroute-service .
# docker run -p 8088:8088 --name busroute-service springio/busroute-service
# docker run -p 8088:8088 -d --name busroute-service springio/busroute-service

FROM adoptopenjdk/openjdk11
ARG JAR_FILE=target/MyBusRouteService.jar
COPY ${JAR_FILE} app.jar
COPY ./tests/docker/example /data/
ENV pathname /data/example
ENTRYPOINT ["java","-jar","/app.jar"]