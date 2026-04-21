# HOADON-BE Backend Development Guide

## Project: Spring Boot Invoice Management API

### Tech Stack
- Java 17
- Spring Boot 3.2.0
- PostgreSQL 12+
- Maven 3.8+
- Lombok

### Project Structure
```
HOADON-BE/
├── src/main/java/com/hoadon/
│   ├── controller/       # REST API endpoints
│   ├── service/          # Business logic layer
│   ├── repository/       # Data access layer
│   ├── entity/           # JPA entities
│   ├── dto/              # Data transfer objects
│   └── HoaDonApplication.java
├── src/main/resources/
│   └── application.properties
├── pom.xml
├── README.md
└── .gitignore
```

### Database Setup
```sql
CREATE DATABASE hoadon_db;
```

Update `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hoadon_db
spring.datasource.username=postgres
spring.datasource.password=<your-password>
```

### Build & Run
```bash
# Build
mvn clean install

# Run
mvn spring-boot:run

# Test
mvn test

# Package for production
mvn clean package -DskipTests
java -jar target/hoadon-api-1.0.0.jar
```

### API Base URL
`http://localhost:8080/api`

### Key Features
✅ CRUD operations for invoices
✅ Filter by status (pending/paid/overdue)
✅ Search by customer name
✅ Pagination support
✅ CORS enabled for frontend
✅ Automatic timestamps (created_at, updated_at)

### Next Steps (Future Enhancements)
- Add Swagger/OpenAPI documentation
- Implement Spring Security & JWT authentication
- Add email notifications
- Add file export (PDF, Excel)
- Add reporting/analytics endpoints
- Implement soft delete
- Add transaction logging
