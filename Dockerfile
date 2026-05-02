# stage 1: build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
#build ra file .jar, bỏ qua test
RUN mvn clean package -DskipTests

# stage 2: run
# Dùng image JRE nhỏ gọn (alpine = bản Linux nhẹ)
FROM eclipse-temurin:17-jre
WORKDIR /app
# Copy file .jar từ stage "build" sang
COPY --from=build /app/target/*.jar app.jar

# Tạo thư mục lưu file upload
RUN mkdir -p uploads/prices

# Khai báo port app sẽ lắng nghe
EXPOSE 8080

# Lệnh chạy khi container start
ENTRYPOINT ["java", "-jar", "app.jar"]