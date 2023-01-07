FROM ghcr.io/graalvm/graalvm-ce:ol7-java11-20.3.1.2

ADD . /build
WORKDIR /build

RUN yum install -y unzip zip

RUN \
    curl -s "https://get.sdkman.io" | bash; \
    source "$HOME/.sdkman/bin/sdkman-init.sh"; \
    sdk install maven; \
    # Install GraalVM Native Image
    gu install native-image;

RUN source "$HOME/.sdkman/bin/sdkman-init.sh" && mvn --version

RUN native-image --version

RUN source "$HOME/.sdkman/bin/sdkman-init.sh" && ./compile.sh

FROM oraclelinux:7-slim

COPY --from=0 "/build/target/native-image/spring-boot-graal" spring-boot-graal

CMD [ "sh", "-c", "./spring-boot-graal" ]