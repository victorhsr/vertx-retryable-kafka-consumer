FROM maven:3.5.2-jdk-8

COPY ./modules /opt/modules
COPY ./commons /opt/commons
COPY ./jdbc /opt/jdbc
COPY ./event /opt/event
COPY ./kafka /opt/kafka
COPY ./person-event /opt/person-event
COPY ./recommendations-event /opt/recommendations-event
COPY ./recommendations-processor /opt/recommendations-processor
COPY ./vertx-base-server /opt/vertx-base-server

RUN mvn -B -f /opt/modules/pom.xml -s /usr/share/maven/ref/settings-docker.xml install