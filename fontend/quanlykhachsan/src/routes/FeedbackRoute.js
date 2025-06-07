import axios from 'axios';

class FeedbackRoute {
    static API_BASE_URL = "http://localhost:8080";

    getAuthHeaders() {
        const token = localStorage.getItem('token');
        return token ? { Authorization: `Bearer ${token}` } : {};
    }

    // Lấy feedback theo ID
    getFeedbackById(feedbackId) {
        return axios.get(`${FeedbackRoute.API_BASE_URL}/feedback/${feedbackId}`, {
            headers: this.getAuthHeaders()
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy feedback theo ID:", err.response?.data || err.message);
                throw err;
            });
    }

    // Lấy tất cả feedback
    getAllFeedbacks() {
        return axios.get(`${FeedbackRoute.API_BASE_URL}/feedback`, {
            headers: this.getAuthHeaders()
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy tất cả feedback:", err.response?.data || err.message);
                throw err;
            });
    }

    // Thêm mới feedback
    addFeedback(feedbackData) {
        return axios.post(`${FeedbackRoute.API_BASE_URL}/feedback`, feedbackData, {
            headers: {
                ...this.getAuthHeaders(),
                'Content-Type': 'application/json'
            }
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi thêm feedback:", err.response?.data || err.message);
                throw err;
            });
    }

    // Cập nhật feedback
    updateFeedback(feedbackId, feedbackData) {
        return axios.put(`${FeedbackRoute.API_BASE_URL}/feedback/${feedbackId}`, feedbackData, {
            headers: {
                ...this.getAuthHeaders(),
                'Content-Type': 'application/json'
            }
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi cập nhật feedback:", err.response?.data || err.message);
                throw err;
            });
    }

    // Xóa feedback
    deleteFeedback(feedbackId) {
        return axios.delete(`${FeedbackRoute.API_BASE_URL}/feedback/${feedbackId}`, {
            headers: this.getAuthHeaders()
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi xóa feedback:", err.response?.data || err.message);
                throw err;
            });
    }

    // Lấy feedback theo hotelId
    getFeedbacksByHotelId(hotelId) {
        return axios.get(`${FeedbackRoute.API_BASE_URL}/feedback/hotel/${hotelId}`, {
            headers: this.getAuthHeaders()
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy feedback theo hotelId:", err.response?.data || err.message);
                throw err;
            });
    }

    // Lấy feedback theo customerId
    getFeedbacksByCustomerId(customerId) {
        return axios.get(`${FeedbackRoute.API_BASE_URL}/feedback/customer/${customerId}`, {
            headers: this.getAuthHeaders()
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy feedback theo customerId:", err.response?.data || err.message);
                throw err;
            });
    }
    // Lấy feedback theo bookingId
    getFeedbacksByBookingId(bookingId) {
        return axios.get(`${FeedbackRoute.API_BASE_URL}/feedback/booking/${bookingId}`, {
            headers: this.getAuthHeaders()
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy feedback theo bookingId:", err.response?.data || err.message);
                throw err;
            });
    }
}

const feedbackRoute = new FeedbackRoute();
export default feedbackRoute;