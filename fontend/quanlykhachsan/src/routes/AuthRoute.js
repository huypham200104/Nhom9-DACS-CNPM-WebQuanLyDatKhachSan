import axios from 'axios';

const API_BASE_URL = "http://localhost:8080";

class AuthRoute {
    // Đăng nhập
    login(loginDto) {
        return axios.post(`${API_BASE_URL}/login`, loginDto, {
            withCredentials: true // Để gửi và nhận cookies
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi đăng nhập:", err.response?.data || err.message);
                throw err;
            });
    }

    // Kiểm tra token
    introspect(authResponse) {
        return axios.post(`${API_BASE_URL}/introspect`, authResponse, {
            withCredentials: true // Để gửi và nhận cookies
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi kiểm tra token:", err.response?.data || err.message);
                throw err;
            });
    }

    // Đăng xuất
    logout() {
        return axios.post(`${API_BASE_URL}/logout`, {}, {
            withCredentials: true // Để gửi và nhận cookies
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi đăng xuất:", err.response?.data || err.message);
                throw err;
            });
    }
}

const authRoute = new AuthRoute();
export default authRoute;