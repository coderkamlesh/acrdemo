# Stage 1: Build native image with GraalVM 25
FROM ghcr.io/graalvm/native-image-community:25 AS build
WORKDIR /app

# Install Maven (native-image image mein bundled nahi hota)
RUN microdnf install -y tar gzip && \
    curl -fsSL https://archive.apache.org/dist/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz | tar -xz -C /opt && \
    ln -s /opt/apache-maven-3.9.9/bin/mvn /usr/local/bin/mvn && \
    microdnf clean all

# Dependencies pehle copy karo — layer caching ke liye
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Source copy karke native binary build karo
COPY src ./src
RUN mvn -Pnative native:compile -DskipTests

# Stage 2: Minimal runtime
FROM oraclelinux:9-slim
WORKDIR /app

RUN adduser --system --uid 1001 spring
USER spring:spring

COPY --from=build /app/target/acrdemo /app/acrdemo

EXPOSE 8080
ENTRYPOINT ["/app/acrdemo"]