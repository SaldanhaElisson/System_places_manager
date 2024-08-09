FROM alpine:latest

RUN apk add --no-cache openjdk21

ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk
ENV PATH="$JAVA_HOME/bin:$PATH"

RUN mkdir /app
WORKDIR /app
COPY target/system_places_manages-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]