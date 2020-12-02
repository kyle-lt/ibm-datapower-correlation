FROM openjdk:11.0.2-jre-slim 

# AppD
RUN apt-get update
RUN apt-get install -y curl jq unzip
COPY downloadJavaAgentLatest.sh .
RUN ./downloadJavaAgentLatest.sh

RUN mkdir /app
WORKDIR /app

ADD ./target/client-0.0.1-SNAPSHOT.jar .

### OpenShift Considerations... - don't need this right now
### Setup user for build execution and application runtime
#ENV APP_ROOT=/app
#ENV PATH=${APP_ROOT}:${PATH} HOME=${APP_ROOT}
#RUN chmod -R u+x ${APP_ROOT} && \
#    chgrp -R 0 ${APP_ROOT} && \
#    chmod -R g=u ${APP_ROOT} /etc/passwd

### Setup user for AppD agent access
#ENV APPD_ROOT=/opt/appdynamics
#RUN chmod -R u+x ${APPD_ROOT} && \
#    chgrp -R 0 ${APPD_ROOT} && \
#    chmod -R g=u ${APPD_ROOT} /etc/passwd

### Containers should NOT run as root as a good practice
#USER 10001
#WORKDIR ${APP_ROOT}

ENTRYPOINT ["java", "-javaagent:/opt/appdynamics/java/javaagent.jar", "-jar", "client-0.0.1-SNAPSHOT.jar"]
