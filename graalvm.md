#  Spring Boot 3 & Graalvm

#### Paketo Buildpack for Native Image 5.8.0
https://github.com/paketo-buildpacks/native-image

#### Paketo Buildpack for Spring Boot 5.22.0
https://github.com/paketo-buildpacks/spring-boot

#### Paketo Buildpack for Executable JAR 6.5.0
https://github.com/paketo-buildpacks/executable-jar


```shell

sdk install java 22.3.r17-grl

sdk use java 22.3.r17-grl

```

#### Maven

```shell
./mvnw -Pnative native:compile
./mvnw -PnativeTest test
```

#### Gradle

```shell
./gradlew nativeCompile
./gradlew nativeTest
```

## Framework version

We are using the following Framework versions

| Framework   | Version      |
|-------------|--------------|
| Spring Boot | 3.0.1        |

## Prerequisites

- [`Java 17+`](https://www.oracle.com/java/technologies/downloads/#java17)
- [`Docker`](https://www.docker.com/)
- [`Docker-Compose`](https://docs.docker.com/compose/install/)

## Bash scripts
