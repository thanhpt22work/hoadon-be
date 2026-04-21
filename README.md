# HóaDơn Invoice Management API

REST API backend cho hệ thống quản lý hóa đơn sử dụng Spring Boot, Java 17, và PostgreSQL.

## Yêu cầu

- Java 17+
- Maven 3.8+
- PostgreSQL 12+

## Cấu trúc Project

```
src/
├── main/
│   ├── java/com/hoadon/
│   │   ├── controller/      # REST Controllers
│   │   ├── service/         # Business Logic
│   │   ├── repository/      # Data Access
│   │   ├── entity/          # JPA Entities
│   │   ├── dto/             # Data Transfer Objects
│   │   └── HoaDonApplication.java
│   └── resources/
│       └── application.properties
└── test/
```

## Cài đặt

1. **Clone project**
   ```bash
   cd HOADON-BE
   ```

2. **Cấu hình Database**
   - Tạo database PostgreSQL:
   ```sql
   CREATE DATABASE hoadon_db;
   ```
   - Cập nhật `application.properties` với credentials PostgreSQL của bạn

3. **Build project**
   ```bash
   mvn clean install
   ```

4. **Chạy application**
   ```bash
   mvn spring-boot:run
   ```

Server sẽ chạy tại `http://localhost:8080/api`

## API Endpoints

### Invoices
- `POST /api/invoices` - Tạo hóa đơn mới
- `GET /api/invoices` - Lấy danh sách hóa đơn (có phân trang)
- `GET /api/invoices/{id}` - Lấy chi tiết hóa đơn
- `PUT /api/invoices/{id}` - Cập nhật hóa đơn
- `DELETE /api/invoices/{id}` - Xóa hóa đơn
- `GET /api/invoices/status/{status}` - Lọc theo trạng thái (pending/paid/overdue)
- `GET /api/invoices/search?keyword=...` - Tìm kiếm theo tên khách hàng

## Trạng thái Hóa đơn

- `PENDING` - Chờ thanh toán
- `PAID` - Đã thanh toán
- `OVERDUE` - Quá hạn

## Công nghệ sử dụng

- **Spring Boot 3.2.0** - Web framework
- **Spring Data JPA** - ORM
- **PostgreSQL** - Database
- **Lombok** - Code generation
- **Maven** - Build tool

## CORS Configuration

API hỗ trợ CORS cho các origin:
- `http://localhost:5173` (Vite dev server)
- `http://localhost:3000` (React dev server)

## Development

### Chạy Tests
```bash
mvn test
```

### Build Production JAR
```bash
mvn clean package -DskipTests
java -jar target/hoadon-api-1.0.0.jar
```

## Notes

- Swagger/OpenAPI documentation có thể được thêm sau
- Authentication/Authorization sẽ được implement sau
- Email notifications cho customer sẽ được thêm vào
