# 1단계: Maven 빌드
FROM maven:3-openjdk-17 AS build

# 작업 디렉터리 설정
WORKDIR /app

# Maven 의존성 캐싱
COPY stat-service/pom.xml ./stat-service/
RUN mvn -f ./gateway-service/pom.xml dependency:go-offline

# 프로젝트 소스 복사 및 빌드
COPY stat-service ./stat-service
RUN mvn -f ./stat-service/pom.xml clean package -DskipTests

# 2단계: 애플리케이션 실행
FROM openjdk:17-jdk-slim

# 작업 디렉터리 설정
WORKDIR /app

# 빌드 결과물 복사
COPY --from=build /app/stat-service/target/*.jar app.jar

# 포트 설정
EXPOSE 8082

# 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]
