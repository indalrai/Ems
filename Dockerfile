FROM openjdk:8 
ADD target/*.jar spark-ems-backend-app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","spark-ems-backend-app.jar"]