FROM java:8

ARG JAR_FILE

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","-Duser.timezone=GMT+8","/app.jar"]

EXPOSE 8080