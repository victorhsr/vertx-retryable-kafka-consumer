FROM vertx-retryable-kafka-consumer_modules

# build project
COPY ./catalog/src /opt/app/src
COPY ./catalog/pom.xml /opt/app/pom.xml
RUN mvn -f /opt/app/pom.xml -s /usr/share/maven/ref/settings-docker.xml package -DskipTests -Dmaven.artifact.threads=30

EXPOSE 8083
ENTRYPOINT exec java -jar /opt/app/target/catalog-service-1.0.jar