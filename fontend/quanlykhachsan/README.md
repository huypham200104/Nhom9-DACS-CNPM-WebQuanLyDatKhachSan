# 🏨 Frontend - Hệ Thống Quản Lý Khách Sạn

## Giới Thiệu / Introduction

Frontend của hệ thống quản lý khách sạn được xây dựng bằng React, cung cấp giao diện người dùng hiện đại và thân thiện cho khách hàng, nhân viên và quản trị viên.

## Công Nghệ / Technology Stack

- **React** 19.1.0 - Core framework
- **React Router DOM** 7.5.0 - Routing and navigation
- **Bootstrap** 5.3.5 - UI framework
- **Ant Design** 5.25.1 - Advanced UI components
- **Axios** 1.8.4 - HTTP client for API calls
- **JWT Decode** 4.0.0 - Token handling
- **Lucide React** - Modern icon library

## Cấu Trúc Thư Mục / Project Structure

```
src/
├── pages/
│   ├── admin/              # Trang dành cho Admin
│   │   └── AdminDashboard.jsx
│   ├── customer/           # Trang dành cho Khách hàng
│   │   ├── BookingCustomer.jsx
│   │   ├── BookingHistory.jsx
│   │   ├── RoomCustomer.jsx
│   │   ├── FeedbackCustomer.jsx
│   │   ├── Profile.jsx
│   │   ├── PaymentSuccess.jsx
│   │   ├── PaymentError.jsx
│   │   └── ...
│   ├── staff/              # Trang dành cho Nhân viên
│   │   └── StaffDashboard.jsx
│   └── public/             # Trang công khai
│       ├── HomePage.jsx
│       ├── LoginPage.jsx
│       ├── Register.jsx
│       ├── Navbar.jsx
│       ├── Footer.jsx
│       └── Search.jsx
├── components/             # Components tái sử dụng
├── services/              # API services
├── utils/                 # Utility functions
├── App.js                # Main application component
└── index.js              # Entry point
```

## Cài Đặt / Installation

### Yêu Cầu
- Node.js >= 16.x
- npm >= 8.x

### Các Bước Cài Đặt

1. **Di chuyển vào thư mục frontend:**
```bash
cd fontend/quanlykhachsan
```

2. **Cài đặt dependencies:**
```bash
npm install
```

3. **Cấu hình API endpoint:**

Tạo file `.env` trong thư mục gốc của frontend:
```env
REACT_APP_API_URL=http://localhost:8080/api
```

4. **Chạy ứng dụng:**
```bash
npm start
```

Ứng dụng sẽ mở tại [http://localhost:3000](http://localhost:3000)

## Scripts Có Sẵn / Available Scripts

### `npm start`
Chạy ứng dụng ở chế độ development.
- Mở [http://localhost:3000](http://localhost:3000) để xem trong trình duyệt
- Trang sẽ tự động reload khi bạn thay đổi code
- Hiển thị lỗi lint trong console

### `npm test`
Chạy test runner ở chế độ interactive watch.

### `npm run build`
Build ứng dụng cho production vào thư mục `build`.
- Bundle React ở chế độ production
- Tối ưu hóa build để đạt hiệu suất tốt nhất
- Code được minify và filenames có hash
- Sẵn sàng để deploy!

### `npm run eject`
**Lưu ý: Đây là thao tác một chiều!**

Nếu cần tùy chỉnh cấu hình build chi tiết, bạn có thể eject. Tuy nhiên, thường không cần thiết cho hầu hết các dự án.

## Tính Năng Chính / Main Features

### Giao Diện Công Khai
- 🏠 Trang chủ với danh sách khách sạn nổi bật
- 🔍 Tìm kiếm khách sạn theo địa điểm, ngày, giá
- 📱 Responsive design cho mọi thiết bị
- 🔐 Đăng nhập / Đăng ký

### Giao Diện Khách Hàng
- 🛏️ Xem và đặt phòng
- 📅 Lịch sử đặt phòng
- 💳 Thanh toán trực tuyến
- ⭐ Đánh giá và phản hồi
- 👤 Quản lý hồ sơ cá nhân
- 🎫 Áp dụng mã giảm giá

### Giao Diện Nhân Viên
- 📊 Dashboard quản lý
- 🏨 Quản lý đặt phòng
- 🔄 Cập nhật trạng thái phòng
- 👥 Quản lý thông tin khách hàng

### Giao Diện Admin
- 📈 Dashboard thống kê tổng quan
- 🏢 Quản lý khách sạn và phòng
- 👥 Quản lý người dùng
- 🎁 Quản lý khuyến mãi
- 📊 Báo cáo doanh thu

## API Integration

Frontend giao tiếp với backend thông qua REST API:

```javascript
// Example API call using Axios
import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

// Get hotels
const getHotels = async () => {
  const response = await axios.get(`${API_BASE_URL}/hotels`);
  return response.data;
};

// Book a room
const createBooking = async (bookingData) => {
  const response = await axios.post(`${API_BASE_URL}/bookings`, bookingData, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.data;
};
```

## Authentication Flow

1. User đăng nhập qua `/login`
2. Backend trả về JWT token
3. Token được lưu trong cookies/localStorage
4. Mọi request sau đó đều gửi kèm token trong header
5. Protected routes kiểm tra token trước khi cho phép truy cập

## Styling

Dự án sử dụng kết hợp:
- **Bootstrap 5** cho layout và components cơ bản
- **Ant Design** cho các components phức tạp (Table, Modal, Form, etc.)
- **Custom CSS** cho styling riêng biệt
- **Bootstrap Icons** và **Lucide React** cho icons

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Deployment

### Production Build
```bash
npm run build
```

Build tạo ra thư mục `build/` chứa static files có thể deploy lên:
- Vercel
- Netlify
- GitHub Pages
- AWS S3 + CloudFront
- Nginx/Apache server

### Environment Variables
Đảm bảo cấu hình đúng các biến môi trường cho production:
```env
REACT_APP_API_URL=https://api.your-domain.com
```

## Troubleshooting

### Port đã được sử dụng
Nếu port 3000 đã được sử dụng, bạn có thể chỉ định port khác:
```bash
PORT=3001 npm start
```

### CORS Issues
Đảm bảo backend đã cấu hình CORS cho phép origin của frontend.

### Build Errors
Nếu gặp lỗi khi build:
```bash
rm -rf node_modules package-lock.json
npm install
npm run build
```

## Contributing

1. Tạo branch mới cho feature/bug fix
2. Commit changes với message rõ ràng
3. Push lên repository
4. Tạo Pull Request

## Support

Nếu gặp vấn đề, vui lòng tạo issue trên GitHub repository hoặc liên hệ team phát triển.

---

**Phát triển bởi Nhóm 9 - Đồ Án Chuyên Sâu CNPM** 🚀
