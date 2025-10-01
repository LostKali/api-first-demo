FROM gradle:8.14-jdk21 AS compile-image

WORKDIR /app

COPY report-generator-service /app/report-generator-service
COPY *.kts /app/

USER root
RUN chown -R root /app

RUN gradle report-generator-service:report-generator-service-app:bootJar --no-daemon --gradle-user-home /app/.gradle --no-build-cache --no-configuration-cache

FROM openjdk:21 AS runtime-image

COPY --from=compile-image /app/report-generator-service/report-generator-service-app/build/libs/report-generator-service-app-*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"] 