FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY . .

# ✅ FIX: give execute permission to mvnw
RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/ComplaintManagementSystem-0.0.1-SNAPSHOT.jar"]
