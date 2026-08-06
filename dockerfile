
FROM node:22-alpine AS frontend-build

WORKDIR /frontend

COPY frontend/package.json frontend/package-lock.json ./
RUN npm ci --legacy-peer-deps

COPY frontend/ ./

ARG VITE_OIDC_AUTHORITY
ARG VITE_OIDC_CLIENT_ID=event-ticket-platform-app

ENV VITE_OIDC_AUTHORITY=${VITE_OIDC_AUTHORITY}
ENV VITE_OIDC_CLIENT_ID=${VITE_OIDC_CLIENT_ID}

RUN npm run build


FROM eclipse-temurin:21-jdk-jammy AS backend-build

WORKDIR /backend

COPY tickets/mvnw ./
COPY tickets/.mvn .mvn
COPY tickets/pom.xml ./

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY tickets/src src
COPY --from=frontend-build \
    /frontend/dist \
    src/main/resources/static

RUN ./mvnw clean package -DskipTests


FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=backend-build /backend/target/*.jar app.jar

ENV SERVER_PORT=10000

EXPOSE 10000

ENTRYPOINT ["java", "-jar", "app.jar"]