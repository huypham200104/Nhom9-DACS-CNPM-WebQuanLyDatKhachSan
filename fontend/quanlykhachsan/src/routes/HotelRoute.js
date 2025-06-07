import axios from 'axios';

class HotelRoute {
    static API_BASE_URL = "http://localhost:8080";

    getAuthHeaders() {
        const token = localStorage.getItem('token');
        return token ? { Authorization: `Bearer ${token}` } : {};
    }

    getAllHotels(page = 1, size = 8) {
        return axios.get(`${HotelRoute.API_BASE_URL}/hotels/all`, {
            params: { page, size },
            headers: this.getAuthHeaders()
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy danh sách khách sạn:", err.response?.data || err.message);
                throw err;
            });
    }

    getHotelById(hotelId) {
        return axios.get(`${HotelRoute.API_BASE_URL}/hotels/${hotelId}`, {
            headers: this.getAuthHeaders()
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy thông tin khách sạn:", err.response?.data || err.message);
                throw err;
            });
    }

    addHotel(hotelDto, files) {
        const mappedDto = { ...hotelDto, hotelName: hotelDto.name };
        delete mappedDto.name;
        const formData = new FormData();
        formData.append('hotelDto', JSON.stringify(mappedDto));
        if (files && files.length > 0) {
            files.forEach(file => formData.append('files', file));
        }

        return axios.post(`${HotelRoute.API_BASE_URL}/hotels`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
                ...this.getAuthHeaders()
            }
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi thêm khách sạn:", err.response?.data || err.message);
                throw err;
            });
    }

    updateHotel(hotelId, hotelDto, files) {
        const mappedDto = { ...hotelDto, hotelName: hotelDto.name };
        delete mappedDto.name;
        const formData = new FormData();
        formData.append('hotelDto', JSON.stringify(mappedDto));
        if (files && files.length > 0) {
            files.forEach(file => formData.append('files', file));
        }

        return axios.put(`${HotelRoute.API_BASE_URL}/hotels/${hotelId}`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
                ...this.getAuthHeaders()
            }
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi cập nhật khách sạn:", err.response?.data || err.message);
                throw err;
            });
    }

    deleteHotel(hotelId) {
        return axios.delete(`${HotelRoute.API_BASE_URL}/hotels/${hotelId}`, {
            headers: this.getAuthHeaders()
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi xóa khách sạn:", err.response?.data || err.message);
                throw err;
            });
    }

    approveHotel(hotelId) {
        return axios.put(`${HotelRoute.API_BASE_URL}/hotels/${hotelId}/approve`, {}, {
            headers: this.getAuthHeaders()
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi phê duyệt khách sạn:", err.response?.data || err.message);
                throw err;
            });
    }

    rejectHotel(hotelId) {
        return axios.put(`${HotelRoute.API_BASE_URL}/hotels/${hotelId}/reject`, {}, {
            headers: this.getAuthHeaders()
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi từ chối khách sạn:", err.response?.data || err.message);
                throw err;
            });
    }

    getHotelByName(hotelName) {
        return axios.get(`${HotelRoute.API_BASE_URL}/hotels/name/${hotelName}`, {
            headers: this.getAuthHeaders()
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy khách sạn theo tên:", err.response?.data || err.message);
                throw err;
            });
    }

    getHotelByCity(city, page = 1, size = 8) {
        return axios.get(`${HotelRoute.API_BASE_URL}/hotels/city/${city}`, {
            params: { page, size },
            headers: this.getAuthHeaders()
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy danh sách khách sạn theo thành phố:", err.response?.data || err.message);
                throw err;
            });
    }

    getHotelByDistrict(district, page = 1, size = 8) {
        return axios.get(`${HotelRoute.API_BASE_URL}/hotels/district/${district}`, {
            params: { page, size },
            headers: this.getAuthHeaders()
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy danh sách khách sạn theo quận:", err.response?.data || err.message);
                throw err;
            });
    }

    getHotelByWard(ward, page = 1, size = 8) {
        return axios.get(`${HotelRoute.API_BASE_URL}/hotels/ward/${ward}`, {
            params: { page, size },
            headers: this.getAuthHeaders()
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy danh sách khách sạn theo phường:", err.response?.data || err.message);
                throw err;
            });
    }

    getHotelByStreet(street, page = 1, size = 8) {
        return axios.get(`${HotelRoute.API_BASE_URL}/hotels/street/${street}`, {
            params: { page, size },
            headers: this.getAuthHeaders()
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy danh sách khách sạn theo đường:", err.response?.data || err.message);
                throw err;
            });
    }

    searchHotels({ city, district, ward, street, houseNumber }, page = 1, size = 8) {
        const params = { page, size };
        if (city) params.city = city.replace(/\s+/g, '+');
        if (district) params.district = district.replace(/\s+/g, '+');
        if (ward) params.ward = ward.replace(/\s+/g, '+');
        if (street) params.street = street.replace(/\s+/g, '+');
        if (houseNumber) params.houseNumber = houseNumber.replace(/\s+/g, '+');

        if (Object.keys(params).length === 2) { // Chỉ có page và size
            return Promise.resolve({ data: { content: [] } });
        }

        return axios.get(`${HotelRoute.API_BASE_URL}/hotels/search`, {
            params,
            headers: this.getAuthHeaders()
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi tìm kiếm khách sạn:", err.response?.data || err.message);
                throw err;
            });
    }

    getHotelByUserId(userId) {
        return axios.get(`${HotelRoute.API_BASE_URL}/hotels`, {
            params: { user_id: userId },
            headers: this.getAuthHeaders()
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy khách sạn theo ID người dùng:", err.response?.data || err.message);
                throw err;
            });
    }

    getAllHotelNames() {
        return axios.get(`${HotelRoute.API_BASE_URL}/hotels/names`, {
            headers: this.getAuthHeaders()
        })
            .then(res => res.data)
            .catch(err => {
                console.error("Lỗi khi lấy danh sách tên khách sạn:", err.response?.data || err.message);
                throw err;
            });
    }
}

const hotelRoute = new HotelRoute();
export default hotelRoute;