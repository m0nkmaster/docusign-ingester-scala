FROM openjdk:11
WORKDIR /app
COPY . /app
RUN sbt compile
EXPOSE 8080
CMD ["sbt", "run"]