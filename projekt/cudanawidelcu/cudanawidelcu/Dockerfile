FROM openjdk:11-jre

ADD target/cudanawidelcu-1.0.0.RELEASE.jar app.jar
EXPOSE 1111
EXPOSE 2222
EXPOSE 3333

ENV SERVICE_ENV=r

ENTRYPOINT ["java","-jar","/app.jar"]
