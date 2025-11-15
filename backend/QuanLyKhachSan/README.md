# 🏨 Backend - Hệ Thống Quản Lý Khách Sạn

## Giới Thiệu / Introduction

Backend của hệ thống quản lý khách sạn được xây dựng bằng Spring Boot, cung cấp RESTful API cho toàn bộ nghiệp vụ quản lý khách sạn, bao gồm đặt phòng, thanh toán, quản lý người dùng và báo cáo.

## Công Nghệ / Technology Stack

- **Spring Boot** 3.4.4 - Main framework
- **Java** 21 - Programming language
- **Spring Data JPA** - Data access and ORM
- **MySQL** - Relational database
- **Spring Security** - Authentication and authorization
- **JWT (Nimbus JOSE JWT)** 9.37.3 - Token-based authentication
- **Spring Mail** - Email notifications
- **OAuth2** - Social login integration
- **Lombok** - Code generation and boilerplate reduction
- **BCrypt** - Password encryption
- **Maven** - Build and dependency management

## Cấu Trúc Thư Mục / Project Structure

```
QuanLyKhachSan/
├── src/
│   ├── main/
│   │   ├── java/com/example/QuanLyKhachSan/
│   │   │   ├── controller/          # REST Controllers
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── HotelController.java
│   │   │   │   ├── RoomController.java
│   │   │   │   ├── BookingController.java
│   │   │   │   ├── PaymentController.java
│   │   │   │   └── ...
│   │   │   ├── service/             # Business Logic Layer
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── HotelService.java
│   │   │   │   ├── RoomService.java
│   │   │   │   └── ...
│   │   │   ├── repository/          # Data Access Layer (JPA)
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── HotelRepository.java
│   │   │   │   ├── RoomRepository.java
│   │   │   │   └── ...
│   │   │   ├── entity/              # JPA Entities (Database Models)
│   │   │   │   ├── User.java
│   │   │   │   ├── Hotel.java
│   │   │   │   ├── Room.java
│   │   │   │   ├── Booking.java
│   │   │   │   ├── Payment.java
│   │   │   │   └── ...
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   ├── CustomerUserDto.java
│   │   │   │   ├── StaffUserDto.java
│   │   │   │   ├── RoomDto.java
│   │   │   │   ├── PaymentDto.java
│   │   │   │   └── ...
│   │   │   ├── config/              # Configuration Classes
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── JwtConfig.java
│   │   │   │   ├── CorsConfig.java
│   │   │   │   └── ...
│   │   │   ├── exception/           # Exception Handling
│   │   │   │   ├── GlobalException.java
│   │   │   │   ├── CustomExceptions.java
│   │   │   │   └── ...
│   │   │   ├── response/            # API Response Models
│   │   │   │   ├── ApiResponse.java
│   │   │   │   ├── AuthenticationResponse.java
│   │   │   │   └── ...
│   │   │   └── QuanLyKhachSanApplication.java  # Main Application
│   │   └── resources/
│   │       ├── application.properties           # Configuration file
│   │       └── static/                          # Static resources
│   └── test/
│       └── java/com/example/QuanLyKhachSan/
│           └── QuanLyKhachSanApplicationTests.java
├── uploads/                         # Uploaded files storage
├── pom.xml                          # Maven configuration
└── README.md                        # This file
```

## Yêu Cầu Hệ Thống / Prerequisites

- **Java Development Kit (JDK)** 21 or higher
- **Maven** 3.6+
- **MySQL** 8.0+
- **IDE:** IntelliJ IDEA (recommended), Eclipse, or VS Code

## Cài Đặt / Installation

### 1. Clone Repository
```bash
git clone https://github.com/huypham200104/Nhom9-DACS-CNPM-WebQuanLyDatKhachSan.git
cd Nhom9-DACS-CNPM-WebQuanLyDatKhachSan/backend/QuanLyKhachSan
```

### 2. Cấu Hình Database

**Tạo database:**
```sql
CREATE DATABASE quanlykhachsan_dacs CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**Import SQL files:**
```bash
cd ../../Database
mysql -u root -p quanlykhachsan_dacs < *.sql
```

### 3. Cấu Hình Application

Tạo file `src/main/resources/application.properties`:

```properties
# ===========================
# Database Configuration
# ===========================
spring.datasource.url=jdbc:mysql://localhost:3306/quanlykhachsan_dacs?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=your_mysql_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# ===========================
# JPA / Hibernate Configuration
# ===========================
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true

# ===========================
# Server Configuration
# ===========================
server.port=8080
server.servlet.context-path=/

# ===========================
# JWT Configuration
# ===========================
jwt.secret=your_super_secret_key_here_minimum_32_characters_long
jwt.expiration=86400000
# Expiration time in milliseconds (86400000 = 24 hours)

# ===========================
# Email Configuration (Gmail)
# ===========================
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_specific_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true

# ===========================
# File Upload Configuration
# ===========================
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
spring.servlet.multipart.location=uploads/

# ===========================
# Logging Configuration
# ===========================
logging.level.root=INFO
logging.level.com.example.QuanLyKhachSan=DEBUG
logging.level.org.springframework.security=DEBUG
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n

# ===========================
# CORS Configuration
# ===========================
cors.allowed.origins=http://localhost:3000,http://localhost:3001
```

### 4. Build và Run

**Sử dụng Maven wrapper:**
```bash
# Build project
./mvnw clean install

# Run application
./mvnw spring-boot:run
```

**Hoặc sử dụng Maven:**
```bash
# Build project
mvn clean install

# Run application
mvn spring-boot:run
```

**Chạy JAR file:**
```bash
# Build JAR
./mvnw clean package

# Run JAR
java -jar target/QuanLyKhachSan-0.0.1-SNAPSHOT.jar
```

Application sẽ chạy tại: **http://localhost:8080**

## API Endpoints

### Authentication APIs
```
POST   /api/auth/register          - Đăng ký tài khoản mới
POST   /api/auth/login             - Đăng nhập
POST   /api/auth/logout            - Đăng xuất
POST   /api/auth/refresh-token     - Làm mới token
POST   /api/auth/forgot-password   - Quên mật khẩu
POST   /api/auth/reset-password    - Đặt lại mật khẩu
```

### Hotel APIs
```
GET    /api/hotels                 - Lấy danh sách khách sạn
GET    /api/hotels/{id}            - Lấy chi tiết khách sạn
POST   /api/hotels                 - Tạo khách sạn mới (Admin)
PUT    /api/hotels/{id}            - Cập nhật khách sạn (Admin)
DELETE /api/hotels/{id}            - Xóa khách sạn (Admin)
GET    /api/hotels/search          - Tìm kiếm khách sạn
GET    /api/hotels/featured        - Lấy khách sạn nổi bật
```

### Room APIs
```
GET    /api/rooms                  - Lấy danh sách phòng
GET    /api/rooms/{id}             - Lấy chi tiết phòng
POST   /api/rooms                  - Tạo phòng mới (Admin/Staff)
PUT    /api/rooms/{id}             - Cập nhật phòng (Admin/Staff)
DELETE /api/rooms/{id}             - Xóa phòng (Admin)
GET    /api/rooms/available        - Lấy phòng trống
GET    /api/rooms/hotel/{hotelId}  - Lấy phòng theo khách sạn
```

### Booking APIs
```
GET    /api/bookings               - Lấy danh sách đặt phòng
GET    /api/bookings/{id}          - Lấy chi tiết đặt phòng
POST   /api/bookings               - Tạo đặt phòng mới
PUT    /api/bookings/{id}          - Cập nhật đặt phòng
DELETE /api/bookings/{id}          - Hủy đặt phòng
GET    /api/bookings/user/{userId} - Lấy đặt phòng theo user
GET    /api/bookings/history       - Lịch sử đặt phòng
```

### Payment APIs
```
GET    /api/payments               - Lấy danh sách thanh toán
GET    /api/payments/{id}          - Lấy chi tiết thanh toán
POST   /api/payments               - Tạo thanh toán mới
PUT    /api/payments/{id}          - Cập nhật thanh toán
GET    /api/payments/booking/{id}  - Lấy thanh toán theo booking
```

### User APIs
```
GET    /api/users                  - Lấy danh sách user (Admin)
GET    /api/users/{id}             - Lấy chi tiết user
PUT    /api/users/{id}             - Cập nhật user
DELETE /api/users/{id}             - Xóa user (Admin)
GET    /api/users/profile          - Lấy profile hiện tại
PUT    /api/users/profile          - Cập nhật profile
```

### Feedback APIs
```
GET    /api/feedbacks              - Lấy danh sách feedback
GET    /api/feedbacks/{id}         - Lấy chi tiết feedback
POST   /api/feedbacks              - Tạo feedback mới
PUT    /api/feedbacks/{id}         - Cập nhật feedback
DELETE /api/feedbacks/{id}         - Xóa feedback
GET    /api/feedbacks/hotel/{id}   - Lấy feedback theo khách sạn
```

### Discount APIs
```
GET    /api/discounts              - Lấy danh sách mã giảm giá
GET    /api/discounts/{id}         - Lấy chi tiết mã giảm giá
POST   /api/discounts              - Tạo mã giảm giá (Admin)
PUT    /api/discounts/{id}         - Cập nhật mã giảm giá (Admin)
DELETE /api/discounts/{id}         - Xóa mã giảm giá (Admin)
POST   /api/discounts/validate     - Validate mã giảm giá
```

### Statistics APIs
```
GET    /api/statistics/revenue     - Thống kê doanh thu (Admin)
GET    /api/statistics/bookings    - Thống kê đặt phòng (Admin)
GET    /api/statistics/customers   - Thống kê khách hàng (Admin)
GET    /api/statistics/rooms       - Thống kê phòng (Admin)
```

## Security

### Authentication Flow
1. User gửi credentials (username/password) tới `/api/auth/login`
2. Server xác thực và trả về JWT token
3. Client lưu token (localStorage/cookie)
4. Mọi request sau đó gửi token trong header: `Authorization: Bearer <token>`
5. Server validate token và cho phép/từ chối truy cập

### Authorization (Role-Based)
- **CUSTOMER:** Khách hàng - Đặt phòng, xem lịch sử, thanh toán
- **STAFF:** Nhân viên - Quản lý đặt phòng, cập nhật phòng
- **ADMIN:** Quản trị viên - Full access, quản lý toàn bộ hệ thống

### Password Security
- Passwords được hash bằng BCrypt
- Minimum password length: 8 characters
- Password complexity requirements (recommended)

## Testing

### Unit Tests
```bash
./mvnw test
```

### Integration Tests
```bash
./mvnw verify
```

### API Testing
Sử dụng Postman hoặc Swagger để test API endpoints.

## Database Schema

### Main Tables
- **user** - Người dùng (Customer, Staff, Admin)
- **hotel** - Khách sạn
- **room** - Phòng
- **room_type** - Loại phòng
- **amenity** - Tiện nghi
- **booking** - Đặt phòng
- **booking_room** - Chi tiết đặt phòng
- **payment** - Thanh toán
- **feedback** - Đánh giá
- **discount** - Mã giảm giá
- **image_hotel** - Hình ảnh khách sạn
- **image_room** - Hình ảnh phòng
- **favorite_hotel** - Khách sạn yêu thích
- **revenue_statistics** - Thống kê doanh thu

## Logging

Application sử dụng SLF4J với Logback:
- **INFO:** General information
- **DEBUG:** Detailed debugging information
- **ERROR:** Error messages and stack traces
- **WARN:** Warning messages

Logs được output ra console và có thể cấu hình để ghi vào file.

## Error Handling

Application sử dụng Global Exception Handler để xử lý lỗi thống nhất:

```json
{
  "status": "ERROR",
  "code": "404",
  "message": "Hotel not found",
  "timestamp": "2024-11-15T14:30:00Z"
}
```

## Performance Optimization

- **Database indexing** trên các trường thường xuyên query
- **Connection pooling** với HikariCP
- **Caching** với Spring Cache (nếu có)
- **Lazy loading** cho JPA relationships
- **Pagination** cho các list endpoints

## Deployment

### Production Checklist
- [ ] Update `application.properties` với production values
- [ ] Enable HTTPS
- [ ] Configure production database
- [ ] Set secure JWT secret
- [ ] Configure email service
- [ ] Enable CORS cho production domain
- [ ] Setup monitoring và logging
- [ ] Configure backup strategy

### Build Production JAR
```bash
./mvnw clean package -DskipTests
```

### Run in Production
```bash
java -jar -Dspring.profiles.active=prod target/QuanLyKhachSan-0.0.1-SNAPSHOT.jar
```

## Troubleshooting

### Port Already in Use
```bash
# Kill process on port 8080
lsof -ti:8080 | xargs kill -9

# Hoặc đổi port trong application.properties
server.port=8081
```

### Database Connection Failed
- Kiểm tra MySQL đã chạy: `mysql --version`
- Kiểm tra credentials trong `application.properties`
- Kiểm tra firewall không block port 3306

### JWT Token Errors
- Đảm bảo `jwt.secret` có ít nhất 32 characters
- Kiểm tra token expiration time
- Verify token format trong request header

## Contributing

1. Fork repository
2. Tạo branch mới: `git checkout -b feature/your-feature`
3. Commit changes: `git commit -m 'Add some feature'`
4. Push to branch: `git push origin feature/your-feature`
5. Tạo Pull Request

## Support

Liên hệ team phát triển hoặc tạo issue trên GitHub nếu gặp vấn đề.

---

**Phát triển bởi Nhóm 9 - Đồ Án Chuyên Sâu CNPM** 🚀
