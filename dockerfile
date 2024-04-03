FROM openjdk:17
ARG JAR_FILE=build/libs/app.jar
COPY pharmacy.csv ./
COPY ${JAR_FILE} ./app.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java","-jar","/app.jar"]