FROM openjdk:17-jdk-slim

WORKDIR /app

# 复制Maven项目文件
COPY pom.xml .
COPY src ./src

# 安装Maven并构建项目
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

# 运行应用
CMD ["java", "-jar", "target/musicflow-backend-1.0.0.jar"]