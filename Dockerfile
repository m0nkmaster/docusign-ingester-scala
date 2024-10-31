FROM eclipse-temurin:17-jdk-jammy AS builder

RUN apt-get update && \
    apt-get install -y curl && \
    curl -L -o sbt-1.9.7.deb https://repo.scala-sbt.org/scalasbt/debian/sbt-1.9.7.deb && \
    dpkg -i sbt-1.9.7.deb && \
    rm sbt-1.9.7.deb && \
    apt-get clean

WORKDIR /app
COPY . .
RUN sbt clean assembly

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/scala-2.13/docusign-ingester-assembly.jar app.jar

ENV PORT=8080
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
