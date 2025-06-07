import axios from 'axios';

// Base URL cho API
const BASE_URL = 'http://localhost:8080/api/bookings';

// Tạo axios instance với config chung
const api = axios.create({
  baseURL: BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor để xử lý response
api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('API Error:', error);
    return Promise.reject(error);
  }
);

export const BookingAPI = {
  // Lấy tất cả booking với phân trang
  getAllBookings: async (page = 0, size = 10) => {
    try {
      const response = await api.get('', {
        params: { page, size }
      });
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Tạo booking mới
  createBooking: async (bookingData) => {
    try {
      const response = await api.post('', bookingData);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Lấy booking theo ID
  getBookingById: async (bookingId) => {
    try {
      const response = await api.get(`/${bookingId}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Cập nhật booking
  updateBooking: async (bookingId, bookingData) => {
    try {
      const response = await api.put(`/${bookingId}`, bookingData);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Lấy booking theo customer ID
  getBookingsByCustomerId: async (customerId) => {
    try {
      const response = await api.get(`/customer/${customerId}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Lấy booking theo room ID
  getBookingsByRoomId: async (roomId) => {
    try {
      const response = await api.get(`/room/${roomId}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Lấy booking theo hotel ID với phân trang
  getBookingsByHotelId: async (hotelId, page = 0, size = 10) => {
    try {
      const response = await api.get(`/hotel/${hotelId}`, {
        params: { page, size }
      });
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Kiểm tra phòng có sẵn
  checkRoomAvailability: async (roomId, checkInDate, checkOutDate) => {
    try {
      const response = await api.get(`/room/${roomId}/availability`, {
        params: { checkInDate, checkOutDate }
      });
      return response.data;
    } catch (error) {
      console.error("Lỗi khi kiểm tra tính khả dụng của phòng:", error.response?.data || error.message);
      throw error;
    }
  },

  // Tính tổng giá
  calculateTotalPrice: async (roomId, checkInDate, checkOutDate, discountId = null) => {
    try {
      const params = {
        roomId,
        checkInDate,
        checkOutDate
      };
      if (discountId) {
        params.discountId = discountId;
      }
      
      const response = await api.get('/calculate-price', { params });
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Lấy booking theo trạng thái
  getBookingsByStatus: async (bookingStatus) => {
    try {
      const response = await api.get(`/status/${bookingStatus}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Lấy booking theo khoảng thời gian
  getBookingsByDateRange: async (startDate, endDate, page = 0, size = 10) => {
    try {
      const response = await api.get('/date-range', {
        params: {
          startDate,
          endDate,
          page,
          size
        }
      });
      return response.data;
    } catch (error) {
      throw error;
    }
  }
};

export default BookingAPI;