FROM vertx-retryable-kafka-consumer_modules

# build project
COPY ./recommendations/src /opt/app/src
COPY ./recommendations/pom.xml /opt/app/pom.xml
RUN mvn -f /opt/app/pom.xml -s /usr/share/maven/ref/settings-docker.xml package -DskipTests -Dmaven.artifact.threads=30

EXPOSE 8082
ENTRYPOINT exec java -jar /opt/app/target/recommendations-service-1.0.jar
