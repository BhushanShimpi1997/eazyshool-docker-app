
FROM openjdk:17

MAINTAINER bhushan shimpi<bhushan.shimpi@gmail.com>

ADD target/EazySchoolApp-release-1.0.jar  /usr/app/EazySchoolApp-release-1.0.jar

WORKDIR /usr/app

EXPOSE 8080

ENTRYPOINT ["java","-jar","EazySchoolApp-release-1.0.jar"]


