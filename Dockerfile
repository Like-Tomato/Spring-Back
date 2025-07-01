FROM gradle:8.5-jdk21-alpine AS builder

WORKDIR /app

COPY build.gradle* settings.gradle* ./
COPY gradle gradle

RUN gradle dependencies --no-daemon || true

COPY src src

RUN gradle clean bootJar --no-daemon

FROM openjdk:21-jre-slim

WORKDIR /app

ENV TZ=Asia/Seoul
RUN apt-get update && apt-get install -y tzdata curl && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone && \
    rm -rf /var/lib/apt/lists/*

RUN groupadd -r appuser && useradd -r -g appuser appuser

COPY --from=builder /app/build/libs/*.jar app.jar

RUN mkdir -p /app/logs && chown -R appuser:appuser /app

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]