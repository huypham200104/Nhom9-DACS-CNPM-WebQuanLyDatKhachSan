# 🏨 Hệ Thống Quản Lý Đặt Phòng Khách Sạn

## 📋 Mô Tả Dự Án / Project Description

### Tiếng Việt
Hệ thống quản lý đặt phòng khách sạn là một ứng dụng web toàn diện được phát triển để hiện đại hóa quy trình quản lý và đặt phòng khách sạn. Dự án cung cấp một nền tảng tích hợp cho khách hàng, nhân viên và quản trị viên để quản lý toàn bộ hoạt động kinh doanh khách sạn.

**Mục tiêu chính:**
- Tự động hóa quy trình đặt phòng và quản lý khách sạn
- Cung cấp trải nghiệm người dùng thân thiện và dễ sử dụng
- Quản lý hiệu quả doanh thu, phòng, và khách hàng
- Hỗ trợ thanh toán trực tuyến an toàn
- Cung cấp báo cáo và thống kê chi tiết

### English
A comprehensive hotel management and booking system web application designed to modernize hotel operations. The project provides an integrated platform for customers, staff, and administrators to manage all hotel business activities.

**Main Objectives:**
- Automate booking and hotel management processes
- Provide user-friendly experience
- Efficiently manage revenue, rooms, and customers
- Support secure online payments
- Provide detailed reports and statistics

---

## 🚀 Tính Năng Chính / Key Features

### 👥 Dành cho Khách Hàng / For Customers
- ✅ Tìm kiếm và đặt phòng khách sạn
- ✅ Xem thông tin chi tiết phòng và khách sạn
- ✅ Quản lý lịch sử đặt phòng
- ✅ Thanh toán trực tuyến an toàn
- ✅ Đánh giá và phản hồi khách sạn
- ✅ Quản lý hồ sơ cá nhân
- ✅ Xem và áp dụng mã giảm giá
- ✅ Danh sách khách sạn yêu thích

### 👔 Dành cho Nhân Viên / For Staff
- ✅ Quản lý đặt phòng
- ✅ Cập nhật trạng thái phòng
- ✅ Xử lý thanh toán
- ✅ Quản lý thông tin khách hàng
- ✅ Tạo và quản lý báo cáo

### 🔐 Dành cho Quản Trị Viên / For Administrators
- ✅ Quản lý người dùng (khách hàng, nhân viên)
- ✅ Quản lý khách sạn và phòng
- ✅ Quản lý loại phòng và tiện nghi
- ✅ Quản lý mã giảm giá và khuyến mãi
- ✅ Thống kê doanh thu và báo cáo
- ✅ Quản lý hình ảnh và nội dung

---

## 🛠️ Công Nghệ Sử Dụng / Technology Stack

### Backend
- **Framework:** Spring Boot 3.4.4
- **Language:** Java 21
- **Database:** MySQL
- **ORM:** Spring Data JPA
- **Security:** Spring Security, JWT (Nimbus JOSE JWT)
- **Email:** Spring Boot Mail
- **OAuth2:** Spring Security OAuth2 Client
- **Build Tool:** Maven
- **Other Libraries:** 
  - Lombok (Code generation)
  - Spring Boot Validation
  - BCrypt (Password hashing)

### Frontend
- **Framework:** React 19.1.0
- **UI Libraries:** 
  - Bootstrap 5.3.5
  - Ant Design 5.25.1
  - React Bootstrap 2.10.9
- **Routing:** React Router DOM 7.5.0
- **HTTP Client:** Axios 1.8.4
- **Icons:** 
  - Bootstrap Icons
  - React Icons
  - Lucide React
- **Authentication:** JWT Decode, JS Cookie
- **Build Tool:** React Scripts (Create React App)

### Database Schema
- Users (Customer, Staff, Admin)
- Hotels
- Rooms & Room Types
- Bookings & Booking Rooms
- Payments
- Amenities
- Discounts
- Feedback
- Revenue Statistics
- Favorite Hotels
- Images (Hotel & Room)

---

## 📦 Cài Đặt / Installation

### Yêu Cầu Hệ Thống / Prerequisites

#### Backend Requirements
- Java Development Kit (JDK) 21 or higher
- Maven 3.6+
- MySQL 8.0+
- IDE: IntelliJ IDEA, Eclipse, or VS Code (recommended)

#### Frontend Requirements
- Node.js 16.x or higher
- npm 8.x or higher (or yarn)
- Modern web browser (Chrome, Firefox, Safari, Edge)

### Bước 1: Clone Repository
```bash
git clone https://github.com/huypham200104/Nhom9-DACS-CNPM-WebQuanLyDatKhachSan.git
cd Nhom9-DACS-CNPM-WebQuanLyDatKhachSan
```

### Bước 2: Cấu Hình Database / Database Setup

1. **Tạo database trong MySQL:**
```sql
CREATE DATABASE quanlykhachsan_dacs CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. **Import các file SQL trong thư mục `Database/`:**
```bash
cd Database
mysql -u root -p quanlykhachsan_dacs < quanlykhachsan_dacs_user.sql
mysql -u root -p quanlykhachsan_dacs < quanlykhachsan_dacs_hotel.sql
mysql -u root -p quanlykhachsan_dacs < quanlykhachsan_dacs_room.sql
# Import tất cả các file SQL còn lại...
```

3. **Cấu hình kết nối database:**

Tạo file `application.properties` trong `backend/QuanLyKhachSan/src/main/resources/`:
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/quanlykhachsan_dacs
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true

# Server Configuration
server.port=8080

# JWT Configuration
jwt.secret=your_secret_key_here
jwt.expiration=86400000

# Email Configuration (Gmail example)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### Bước 3: Cài Đặt Backend

```bash
cd backend/QuanLyKhachSan

# Cài đặt dependencies
./mvnw clean install

# Chạy ứng dụng
./mvnw spring-boot:run
```

Backend sẽ chạy tại: `http://localhost:8080`

### Bước 4: Cài Đặt Frontend

```bash
cd fontend/quanlykhachsan

# Cài đặt dependencies
npm install

# Chạy ứng dụng development
npm start
```

Frontend sẽ chạy tại: `http://localhost:3000`

---

## 🏃 Chạy Ứng Dụng / Running the Application

### Development Mode

**Terminal 1 - Backend:**
```bash
cd backend/QuanLyKhachSan
./mvnw spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd fontend/quanlykhachsan
npm start
```

### Production Build

**Backend:**
```bash
cd backend/QuanLyKhachSan
./mvnw clean package
java -jar target/QuanLyKhachSan-0.0.1-SNAPSHOT.jar
```

**Frontend:**
```bash
cd fontend/quanlykhachsan
npm run build
# Sau đó deploy thư mục build/ lên web server
```

---

## 📁 Cấu Trúc Thư Mục / Project Structure

```
Nhom9-DACS-CNPM-WebQuanLyDatKhachSan/
├── backend/
│   └── QuanLyKhachSan/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/com/example/QuanLyKhachSan/
│       │   │   │   ├── controller/    # REST API Controllers
│       │   │   │   ├── service/       # Business Logic
│       │   │   │   ├── repository/    # Data Access Layer
│       │   │   │   ├── entity/        # JPA Entities
│       │   │   │   ├── dto/           # Data Transfer Objects
│       │   │   │   ├── exception/     # Exception Handling
│       │   │   │   ├── response/      # API Response Models
│       │   │   │   └── config/        # Configuration Classes
│       │   │   └── resources/
│       │   │       └── application.properties
│       │   └── test/
│       ├── pom.xml
│       └── uploads/                   # Uploaded files storage
├── fontend/
│   └── quanlykhachsan/
│       ├── public/
│       ├── src/
│       │   ├── pages/
│       │   │   ├── admin/            # Admin pages
│       │   │   ├── customer/         # Customer pages
│       │   │   ├── staff/            # Staff pages
│       │   │   └── public/           # Public pages
│       │   ├── components/           # Reusable components
│       │   ├── services/             # API services
│       │   └── utils/                # Utility functions
│       ├── package.json
│       └── README.md
├── Database/                          # SQL files
│   ├── quanlykhachsan_dacs_user.sql
│   ├── quanlykhachsan_dacs_hotel.sql
│   └── ...
└── README.md                          # This file
```

---

## 🔗 API Documentation

### Base URL
- Development: `http://localhost:8080/api`
- Production: `https://your-domain.com/api`

### Main Endpoints

#### Authentication
- `POST /api/auth/register` - Đăng ký tài khoản mới
- `POST /api/auth/login` - Đăng nhập
- `POST /api/auth/logout` - Đăng xuất
- `POST /api/auth/refresh-token` - Làm mới token

#### Hotels
- `GET /api/hotels` - Lấy danh sách khách sạn
- `GET /api/hotels/{id}` - Lấy chi tiết khách sạn
- `POST /api/hotels` - Tạo khách sạn mới (Admin)
- `PUT /api/hotels/{id}` - Cập nhật khách sạn (Admin)
- `DELETE /api/hotels/{id}` - Xóa khách sạn (Admin)

#### Rooms
- `GET /api/rooms` - Lấy danh sách phòng
- `GET /api/rooms/{id}` - Lấy chi tiết phòng
- `POST /api/rooms` - Tạo phòng mới (Admin/Staff)
- `PUT /api/rooms/{id}` - Cập nhật phòng (Admin/Staff)
- `DELETE /api/rooms/{id}` - Xóa phòng (Admin)

#### Bookings
- `GET /api/bookings` - Lấy danh sách đặt phòng
- `GET /api/bookings/{id}` - Lấy chi tiết đặt phòng
- `POST /api/bookings` - Tạo đặt phòng mới
- `PUT /api/bookings/{id}` - Cập nhật đặt phòng
- `DELETE /api/bookings/{id}` - Hủy đặt phòng

#### Payments
- `POST /api/payments` - Tạo thanh toán
- `GET /api/payments/{id}` - Lấy thông tin thanh toán
- `GET /api/payments/booking/{bookingId}` - Lấy thanh toán theo booking

#### Users
- `GET /api/users/profile` - Lấy thông tin profile
- `PUT /api/users/profile` - Cập nhật profile
- `GET /api/users` - Lấy danh sách người dùng (Admin)

---

## 🧪 Testing

### Backend Testing
```bash
cd backend/QuanLyKhachSan
./mvnw test
```

### Frontend Testing
```bash
cd fontend/quanlykhachsan
npm test
```

---

## 🔐 Bảo Mật / Security

- **Authentication:** JWT (JSON Web Token)
- **Authorization:** Role-based access control (Customer, Staff, Admin)
- **Password Hashing:** BCrypt
- **HTTPS:** Recommended for production
- **CORS:** Configured for secure cross-origin requests
- **SQL Injection Prevention:** Prepared statements via JPA
- **XSS Protection:** Input validation and sanitization

---

## 👥 Thành Viên Nhóm / Team Members

**Nhóm 9 - Đồ Án Chuyên Sâu CNPM**

- Thành viên 1: [Tên]
- Thành viên 2: [Tên]
- Thành viên 3: [Tên]
- Thành viên 4: [Tên]
- Thành viên 5: [Tên]

**Giảng viên hướng dẫn:** [Tên giảng viên]

---

## 📝 License

This project is developed for educational purposes as part of the Software Engineering course project.

---

## 📞 Liên Hệ / Contact

- **Repository:** [https://github.com/huypham200104/Nhom9-DACS-CNPM-WebQuanLyDatKhachSan](https://github.com/huypham200104/Nhom9-DACS-CNPM-WebQuanLyDatKhachSan)
- **Issues:** [GitHub Issues](https://github.com/huypham200104/Nhom9-DACS-CNPM-WebQuanLyDatKhachSan/issues)

---

## 🙏 Acknowledgments

- Spring Boot Documentation
- React Documentation
- Bootstrap Documentation
- Ant Design Documentation
- MySQL Documentation
- Stack Overflow Community

---

**© 2024 Nhóm 9 - Đồ Án Chuyên Sâu CNPM. All rights reserved.**
