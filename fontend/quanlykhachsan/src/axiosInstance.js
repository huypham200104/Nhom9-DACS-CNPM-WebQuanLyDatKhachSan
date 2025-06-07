import axios from "axios";

// Tạo một instance dùng chung
const axiosInstance = axios.create({
  baseURL: "http://localhost:8080", // Đổi theo backend bạn
  withCredentials: true, // Nếu dùng session
});

// Interceptor để xử lý lỗi 401 toàn cục
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      // Nếu là 401 (chưa đăng nhập), chuyển hướng sang trang login
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
