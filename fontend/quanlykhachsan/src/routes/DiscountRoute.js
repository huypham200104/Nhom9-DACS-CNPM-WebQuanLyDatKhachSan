import axios from 'axios';

const API_BASE_URL = "http://localhost:8080";

class DiscountRoute {
    // Lấy thông tin mã giảm giá theo ID
    getDiscountById(discountId) {
        return axios.get(`${API_BASE_URL}/discounts/${discountId}`)
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy thông tin mã giảm giá:", err.response?.data || err.message);
                throw err;
            });
    }

    // Lấy danh sách tất cả mã giảm giá
    getAllDiscounts() {
        return axios.get(`${API_BASE_URL}/discounts/all`)
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy danh sách mã giảm giá:", err.response?.data || err.message);
                throw err;
            });
    }

    // Thêm mã giảm giá mới
    addDiscount(discount) {
        return axios.post(`${API_BASE_URL}/discounts/add`, discount)
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi thêm mã giảm giá:", err.response?.data || err.message);
                throw err;
            });
    }

    // Cập nhật mã giảm giá
    updateDiscount(discount) {
        return axios.put(`${API_BASE_URL}/discounts/update`, discount)
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi cập nhật mã giảm giá:", err.response?.data || err.message);
                throw err;
            });
    }

    // Xóa mã giảm giá
    deleteDiscount(discountId) {
        return axios.delete(`${API_BASE_URL}/discounts/delete/${discountId}`)
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi xóa mã giảm giá:", err.response?.data || err.message);
                throw err;
            });
    }

    // Lấy mã giảm giá theo mã code
    getDiscountByCode(discountCode) {
        return axios.get(`${API_BASE_URL}/discounts/code/${discountCode}`)
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy mã giảm giá theo code:", err.response?.data || err.message);
                throw err;
            });
    }

    // Lấy danh sách mã giảm giá theo ID khách sạn
    getDiscountsByHotelId(hotelId) {
        return axios.get(`${API_BASE_URL}/discounts/hotel/${hotelId}`)
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy mã giảm giá theo ID khách sạn:", err.response?.data || err.message);
                throw err;
            });
    }

    // Xác thực mã giảm giá
    validateDiscount(discountCode, bookingAmount, hotelId) {
        return axios.post(`${API_BASE_URL}/discounts/validate`, null, {
            params: {
                discountCode,
                bookingAmount,
                hotelId
            }
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi xác thực mã giảm giá:", err.response?.data || err.message);
                throw err;
            });
    }
}

const discountRoute = new DiscountRoute();
export default discountRoute;