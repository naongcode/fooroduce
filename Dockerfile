# OpenJDK 21 이미지를 베이스로 사용
FROM openjdk:21-jdk-slim

# 컨테이너 내부에서 작업디렉토리
WORKDIR /app

# WAR 파일을 컨테이너에 복사
COPY build/libs/fooroduce-1.0-SNAPSHOT.war /app/fooroduce.war

# timezone 환경설정
ENV TZ=Asia/Seoul

# War 파일 실행
CMD ["java", "-jar", "-Duser.timezone=Asia/Seoul", "/app/fooroduce.war"]

# 애플리케이션 포트 열기
EXPOSE 8080
