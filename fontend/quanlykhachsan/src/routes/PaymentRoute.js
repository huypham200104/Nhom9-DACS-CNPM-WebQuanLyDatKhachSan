import axios from 'axios';

class PaymentRoute {
    static API_BASE_URL = "http://localhost:8080";

    getAuthHeaders() {
        const token = localStorage.getItem('token');
        return token ? { Authorization: `Bearer ${token}` } : {};
    }

    // Cập nhật method createPayment để nhận đầy đủ thông tin booking
    createPayment(amount, content = "Payment for order", customerId, roomId, checkInDate, checkOutDate, numberOfGuests, specialRequests) {
        const params = { 
            amount, 
            content,
            customerId,
            roomId,
            checkInDate,
            checkOutDate,
            numberOfGuests,
            specialRequests
        };
        
        return axios.get(`${PaymentRoute.API_BASE_URL}/api/payments/create`, {
            params,
            headers: this.getAuthHeaders()
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi tạo thanh toán:", err.response?.data || err.message);
                throw err;
            });
    }

    getPaymentInfo(vnp_Amount, vnp_TxnRef, vnp_ResponseCode, vnp_BankCode, vnp_PayDate) {
        const params = { 
            vnp_Amount, 
            vnp_TxnRef, 
            vnp_ResponseCode, 
            vnp_BankCode, 
            vnp_PayDate 
        };
        return axios.get(`${PaymentRoute.API_BASE_URL}/api/payments/payment-info`, {
            params,
            headers: this.getAuthHeaders()
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy thông tin thanh toán:", err.response?.data || err.message);
                throw err;
            });
    }

    // Thêm method để xử lý callback sau thanh toán thành công
// Đảm bảo method handlePaymentCallback đã được implement đúng
handlePaymentCallback(paymentData) {
    return axios.post(`${PaymentRoute.API_BASE_URL}/api/payments/callback`, paymentData, {
        headers: {
            ...this.getAuthHeaders(),
            'Content-Type': 'application/json'
        }
    })
        .then(res => res.data)
        .catch(err => {
            console.error("Lỗi khi xử lý callback thanh toán:", err.response?.data || err.message);
            throw err;
        });
}
}

const paymentRoute = new PaymentRoute();
export default paymentRoute;