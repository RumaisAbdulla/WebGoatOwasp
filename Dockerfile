FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
COPY target/webgoat-2023.4.jar webgoatowasp.jar
EXPOSE 9990
ENTRYPOINT exec java $JAVA_OPTS -jar webgoatowasp.jar
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
#ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar webgoatowasp.jar
