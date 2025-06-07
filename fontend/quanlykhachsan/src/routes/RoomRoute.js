import axios from 'axios';

const API_BASE_URL = "http://localhost:8080";

class RoomRoute {
    // Lấy danh sách tất cả phòng với phân trang
    getAllRooms(page = 0, size = 5) {
        return axios.get(`${API_BASE_URL}/rooms/all`, { params: { page, size } })
            .then(res => res.data.data) // Extract data from ApiResponse
            .catch(err => {
                console.error("Lỗi khi lấy danh sách phòng:", err.response?.data?.message || err.message);
                throw err;
            });
    }

    // Lấy thông tin phòng theo ID
    getRoomById(roomId) {
        return axios.get(`${API_BASE_URL}/rooms/${roomId}`)
            .then(res => res.data.data) // Extract data from ApiResponse
            .catch(err => {
                console.error("Lỗi khi lấy thông tin phòng:", err.response?.data?.message || err.message);
                throw err;
            });
    }

    // Thêm phòng mới với dữ liệu phòng và file ảnh
    addRoom(roomDto, files) {
        const formData = new FormData();
        formData.append('roomDto', JSON.stringify(roomDto)); // Send roomDto as JSON string
        if (files && files.length > 0) {
            files.forEach(file => formData.append('files', file));
        }

        return axios.post(`${API_BASE_URL}/rooms`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        })
            .then(res => res.data.data) // Extract data from ApiResponse
            .catch(err => {
                console.error("Lỗi khi thêm phòng:", err.response?.data?.message || err.message);
                throw err;
            });
    }

    // Cập nhật phòng - FIXED: Sử dụng FormData thay vì @ModelAttribute
    updateRoom(roomId, roomDto, files) {
        const formData = new FormData();
        
        // Append room data fields individually (for @ModelAttribute binding)
        Object.keys(roomDto).forEach(key => {
            if (roomDto[key] !== null && roomDto[key] !== undefined) {
                formData.append(key, roomDto[key]);
            }
        });
        
        // Append files
        if (files && files.length > 0) {
            files.forEach(file => formData.append('files', file));
        }

        return axios.put(`${API_BASE_URL}/rooms/${roomId}`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        })
            .then(res => res.data.data) // Extract data from ApiResponse
            .catch(err => {
                console.error("Lỗi khi cập nhật phòng:", err.response?.data?.message || err.message);
                throw err;
            });
    }

    // Xóa phòng
    deleteRoom(roomId) {
        return axios.delete(`${API_BASE_URL}/rooms/${roomId}`)
            .then(res => res.data.data) // Extract data from ApiResponse (null for delete)
            .catch(err => {
                console.error("Lỗi khi xóa phòng:", err.response?.data?.message || err.message);
                throw err;
            });
    }

    // Lấy danh sách phòng theo ID khách sạn
    getRoomByHotelId(hotelId) {
        return axios.get(`${API_BASE_URL}/rooms/hotel/${hotelId}`)
            .then(res => res.data.data) // Extract data from ApiResponse
            .catch(err => {
                console.error("Lỗi khi lấy danh sách phòng theo khách sạn:", err.response?.data?.message || err.message);
                throw err;
            });
    }

    // FIXED: Lấy danh sách phòng trống - Sử dụng parameters khớp với controller
getAvailableRooms(city, checkInDate, checkOutDate, adults, children = 0, page = 0, size = 5) {
    // Normalize city name - remove extra spaces and encode properly
    const normalizedCity = city.trim();
    
    console.log('Searching with params:', {
        city: normalizedCity,
        checkInDate,
        checkOutDate,
        adults,
        children,
        page,
        size
    });

    return axios.get(`${API_BASE_URL}/rooms/available`, {
        params: { 
            city: normalizedCity,  // Axios will handle encoding automatically
            checkInDate,    // LocalDate format: yyyy-MM-dd
            checkOutDate,   // LocalDate format: yyyy-MM-dd
            adults,         // Integer adults
            children,       // Integer children (optional)
            page,           // Pagination
            size            // Pagination
        },
        // Add timeout and better error handling
        timeout: 30000,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    })
        .then(res => {
            console.log('API response:', res.data);
            return res.data.data;
        })
        .catch(err => {
            console.error("Full error object:", err);
            console.error("Error response:", err.response?.data);
            console.error("Error status:", err.response?.status);
            console.error("Error message:", err.response?.data?.message || err.message);
            throw err;
        });
}

    // Lấy danh sách ngày trống của phòng theo ID
    getAvailableDatesForRoom(roomId, startDate, endDate) {
        return axios.get(`${API_BASE_URL}/rooms/${roomId}/available-dates`, {
            params: { 
                startDate,  // LocalDate format: yyyy-MM-dd
                endDate     // LocalDate format: yyyy-MM-dd
            }
        })
            .then(res => res.data) // Direct list of dates, not wrapped in ApiResponse
            .catch(err => {
                console.error("Lỗi khi lấy danh sách ngày trống:", err.response?.data?.[0] || err.message);
                throw err;
            });
    }

    // BONUS: Helper method để format date cho API
    formatDateForAPI(date) {
        if (date instanceof Date) {
            return date.toISOString().split('T')[0]; // yyyy-MM-dd format
        }
        return date; // Assume already in correct format
    }

    // BONUS: Convenience method với date formatting
    getAvailableRoomsWithDateFormat(city, checkInDate, checkOutDate, adults, children = 0, page = 0, size = 5) {
        return this.getAvailableRooms(
            city,
            this.formatDateForAPI(checkInDate),
            this.formatDateForAPI(checkOutDate),
            adults,
            children,
            page,
            size
        );
    }
        // Lấy danh sách phòng theo ID khách sạn với phân trang
    getRoomsByHotelId(hotelId, page = 0, size = 5) {
        return axios.get(`${API_BASE_URL}/rooms/hotel/${hotelId}/rooms`, {
            params: { page, size }
        })
            .then(res => res.data.data) // Extract data from ApiResponse
            .catch(err => {
                console.error("Lỗi khi lấy danh sách phòng theo ID khách sạn:", err.response?.data?.message || err.message);
                throw err;
            });
    }

    // Lấy danh sách phòng theo thành phố với phân trang
    getRoomsByCity(city, page = 0, size = 5) {
        return axios.get(`${API_BASE_URL}/rooms/city/${city}`, {
            params: { page, size }
        })
            .then(res => res.data.data) // Extract data from ApiResponse
            .catch(err => {
                console.error("Lỗi khi lấy danh sách phòng theo thành phố:", err.response?.data?.message || err.message);
                throw err;
            });
    }
}

const roomRoute = new RoomRoute();
export default roomRoute;