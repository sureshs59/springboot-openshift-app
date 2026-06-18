FROM registry.access.redhat.com/ubi8/openjdk-11:latest

WORKDIR /app

COPY target/springboot-openshift-app-1.0.0.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
