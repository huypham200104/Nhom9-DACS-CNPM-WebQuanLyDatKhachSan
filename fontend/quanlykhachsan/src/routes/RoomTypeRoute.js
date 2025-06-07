import axios from 'axios';

const API_BASE_URL = "http://localhost:8080";

class RoomTypeRoute {
    // Lấy thông tin loại phòng theo ID
    getRoomTypeById(roomTypeId) {
        return axios.get(`${API_BASE_URL}/room-types/${roomTypeId}`)
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy thông tin loại phòng:", err.response?.data || err.message);
                throw err;
            });
    }

    // Lấy thông tin loại phòng theo tên
    getRoomTypeByName(roomTypeName) {
        return axios.get(`${API_BASE_URL}/room-types/name/${roomTypeName}`)
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy thông tin loại phòng theo tên:", err.response?.data || err.message);
                throw err;
            });
    }

    // Lấy danh sách tất cả loại phòng
    getAllRoomTypes() {
        return axios.get(`${API_BASE_URL}/room-types/all`)
            .then(res => res.data.data || []) // Trả về mảng data, hoặc mảng rỗng nếu không có
            .catch(err => {
                console.error("Lỗi khi lấy danh sách loại phòng:", err.response?.data || err.message);
                throw err;
            });
    }

    // Thêm loại phòng mới
    addRoomType(roomType) {
        return axios.post(`${API_BASE_URL}/room-types`, roomType)
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi thêm loại phòng:", err.response?.data || err.message);
                throw err;
            });
    }

    // Cập nhật loại phòng
    updateRoomType(roomTypeId, roomType) {
        return axios.put(`${API_BASE_URL}/room-types/${roomTypeId}`, roomType)
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi cập nhật loại phòng:", err.response?.data || err.message);
                throw err;
            });
    }

    // Xóa loại phòng
    deleteRoomType(roomTypeId) {
        return axios.delete(`${API_BASE_URL}/room-types/${roomTypeId}`)
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi xóa loại phòng:", err.response?.data || err.message);
                throw err;
            });
    }
}

const roomTypeRoute = new RoomTypeRoute();
export default roomTypeRoute;