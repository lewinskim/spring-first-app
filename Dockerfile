FROM azul/zulu-openjdk:11
COPY target/serving-web-content-0.0.1-SNAPSHOT.jar .
EXPOSE 8000
CMD java -jar serving-web-content-0.0.1-SNAPSHOT.jar