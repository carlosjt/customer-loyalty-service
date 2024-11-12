
# Customer Loyalty Service

This project uses Quarkus, the Supersonic Subatomic Java Framework.

## Prerequisites

Before running the application, ensure you have the following installed:

1. **Java OpenJDK 17**:
    - You can download OpenJDK 17 from [AdoptOpenJDK](https://adoptopenjdk.net/).
    - After downloading, set up the `JAVA_HOME` environment variable to point to the OpenJDK installation directory.

   ```shell
   # Verify installation
   java -version
   ```

2. **Maven 3.9.1**:
    - Download Maven 3.9.1 from the [Apache Maven website](https://maven.apache.org/download.cgi).
    - Extract the downloaded file and add the `bin` directory to your system's `PATH`.

   ```shell
   # Verify installation
   mvn -v
   ```

3. **Docker Desktop**:
    - Download Docker Desktop from the [official Docker website](https://www.docker.com/products/docker-desktop) and follow the installation instructions for your operating system.
    - Docker is required to run the PostgreSQL database for this application and to containerize the application.

   ```shell
   # Verify Docker installation
   docker --version
   ```

## Running the application in dev mode

You can run your application in dev mode, which enables live coding, using:

```shell
./mvnw compile quarkus:dev
```

> **_NOTE:_** Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.
