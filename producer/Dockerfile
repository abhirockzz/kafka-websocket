FROM anapsix/alpine-java:latest

RUN mkdir app 

WORKDIR "/app"

COPY target/kafka-producer.jar .

CMD ["java", "-jar", "kafka-producer.jar"]
