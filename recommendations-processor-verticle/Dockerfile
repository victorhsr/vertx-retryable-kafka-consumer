FROM vertx-retryable-kafka-consumer_modules

# build project
COPY ./recommendations-processor-verticle/src /opt/app/src
COPY ./recommendations-processor-verticle/pom.xml /opt/app/pom.xml
RUN mvn -f /opt/app/pom.xml -s /usr/share/maven/ref/settings-docker.xml package -DskipTests -Dmaven.artifact.threads=30

EXPOSE 8081
ENTRYPOINT exec java -jar /opt/app/target/recommendations-processor-verticle-1.0.jar