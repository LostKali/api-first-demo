FROM gradle:8.14-jdk21 AS compile-image

WORKDIR /app

COPY admin-service /app/admin-service
COPY *.kts /app/

USER root
RUN chown -R root /app

RUN gradle admin-service:admin-service-app:bootJar --no-daemon --gradle-user-home /app/.gradle --no-build-cache --no-configuration-cache

FROM openjdk:21 AS runtime-image

COPY --from=compile-image /app/admin-service/admin-service-app/build/libs/admin-service-app-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]