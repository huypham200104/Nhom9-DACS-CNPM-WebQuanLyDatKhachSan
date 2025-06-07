import axios from 'axios';

const API_BASE_URL = "http://localhost:8080/customers";

class CustomerRoute {
    // Đăng ký khách hàng
    registerCustomer(customer) {
        return axios.post(`${API_BASE_URL}/register`, customer)
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi đăng ký khách hàng:", err.response?.data || err.message);
                throw err;
            });
    }

    // Cập nhật khách hàng
    updateCustomer(userId, customer) {
        return axios.put(`${API_BASE_URL}/${userId}`, customer)
            .then(res => res.data)
            .catch(err => {
                console.error(`Lỗi cập nhật khách hàng ID ${userId}:`, err.response?.data || err.message);
                throw err;
            });
    }

    // Xóa khách hàng theo ID
    deleteCustomer(customerId) {
        return axios.delete(`${API_BASE_URL}/${customerId}`)
            .then(res => res.data)
            .catch(err => {
                console.error(`Lỗi xóa khách hàng ID ${customerId}:`, err.response?.data || err.message);
                throw err;
            });
    }

    // Lấy thông tin khách hàng theo ID
    getCustomerById(customerId) {
        return axios.get(`${API_BASE_URL}/${customerId}`)
            .then(res => res.data)
            .catch(err => {
                console.error(`Lỗi lấy khách hàng ID ${customerId}:`, err.response?.data || err.message);
                throw err;
            });
    }

    // Lấy thông tin khách hàng theo userId
    getCustomerByUserId(userId) {
        return axios.get(`${API_BASE_URL}/user/${userId}`)
            .then(res => res.data)
            .catch(err => {
                console.error(`Lỗi lấy khách hàng theo userId ${userId}:`, err.response?.data || err.message);
                throw err;
            });
    }

    // Lấy danh sách tất cả khách hàng có phân trang
    getAllCustomers(page = 0, size = 10) {
        return axios.get(`${API_BASE_URL}/all`, {
            params: { page, size }
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi lấy danh sách khách hàng:", err.response?.data || err.message);
                throw err;
            });
    }

    // Tìm kiếm khách hàng theo từ khóa
    searchCustomers(keyword, page = 0, size = 10) {
        return axios.get(`${API_BASE_URL}/search`, {
            params: { keyword, page, size }
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi tìm kiếm khách hàng:", err.response?.data || err.message);
                throw err;
            });
    }

    // Lấy danh sách khách hàng theo booking ID
    getCustomersByBookingId(bookingId) {
        return axios.get(`${API_BASE_URL}/booking/${bookingId}`)
            .then(res => res.data)
            .catch(err => {
                console.error(`Lỗi lấy khách hàng theo booking ID ${bookingId}:`, err.response?.data || err.message);
                throw err;
            });
    }

    // Lấy danh sách khách hàng theo ngày nhận phòng
    getCustomersByCheckInDate(checkInDate) {
        return axios.get(`${API_BASE_URL}/check-in`, {
            params: { checkInDate }
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi lấy khách hàng theo ngày nhận phòng:", err.response?.data || err.message);
                throw err;
            });
    }

    // Lấy danh sách khách hàng theo ngày trả phòng
    getCustomersByCheckOutDate(checkOutDate) {
        return axios.get(`${API_BASE_URL}/check-out`, {
            params: { checkOutDate }
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi lấy khách hàng theo ngày trả phòng:", err.response?.data || err.message);
                throw err;
            });
    }
}

const customerRoute = new CustomerRoute();
export default customerRoute;