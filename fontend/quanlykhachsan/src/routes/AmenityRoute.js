import axios from 'axios';

const API_BASE_URL = "http://localhost:8080";

class AmenityRoute {
    // Lấy thông tin tiện nghi theo ID
    getAmenityById(amenityId) {
        return axios.get(`${API_BASE_URL}/amenities/${amenityId}`)
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy thông tin tiện nghi:", err.response?.data || err.message);
                throw err;
            });
    }

    // Lấy danh sách tất cả tiện nghi
    getAllAmenities() {
        return axios.get(`${API_BASE_URL}/amenities/all`)
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy danh sách tiện nghi:", err.response?.data || err.message);
                throw err;
            });
    }

    // Thêm tiện nghi mới
    addAmenity(amenity) {
        return axios.post(`${API_BASE_URL}/amenities/add`, amenity)
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi thêm tiện nghi:", err.response?.data || err.message);
                throw err;
            });
    }

    // Cập nhật tiện nghi
    updateAmenity(amenityId, amenity) {
        return axios.put(`${API_BASE_URL}/amenities/update/${amenityId}`, amenity)
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi cập nhật tiện nghi:", err.response?.data || err.message);
                throw err;
            });
    }

    // Xóa tiện nghi
    deleteAmenity(amenityId) {
        return axios.delete(`${API_BASE_URL}/amenities/delete/${amenityId}`)
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi xóa tiện nghi:", err.response?.data || err.message);
                throw err;
            });
    }
}

const amenityRoute = new AmenityRoute();
export default amenityRoute;