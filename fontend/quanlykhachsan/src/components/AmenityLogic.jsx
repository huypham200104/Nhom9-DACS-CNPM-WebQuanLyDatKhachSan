import { useState, useEffect } from 'react';
import amenityRoute from '../routes/AmenityRoute';

const AmenityLogic = () => {
    const [amenities, setAmenities] = useState([]);
    const [formData, setFormData] = useState({ amenityName: '', description: '', amenityStatus: 'AVAILABLE' });
    const [editingId, setEditingId] = useState(null);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    // Lấy danh sách tiện nghi khi component được mount
    useEffect(() => {
        fetchAmenities();
    }, []);

    const fetchAmenities = async () => {
        try {
            const response = await amenityRoute.getAllAmenities();
            setAmenities(response.data || []);
            setError(null);
        } catch (err) {
            setError('Không thể tải danh sách tiện nghi');
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleAddAmenity = async () => {
        if (!formData.amenityName) {
            setError('Tên tiện nghi không được để trống');
            return;
        }
        try {
            await amenityRoute.addAmenity(formData);
            setSuccess('Thêm tiện nghi thành công');
            setFormData({ amenityName: '', description: '', amenityStatus: 'AVAILABLE' });
            fetchAmenities();
            setError(null);
        } catch (err) {
            setError('Lỗi khi thêm tiện nghi');
        }
    };

    const handleEditAmenity = (amenity) => {
        setEditingId(amenity.amenityId);
        setFormData({ 
            amenityName: amenity.amenityName, 
            description: amenity.description, 
            amenityStatus: amenity.amenityStatus 
        });
    };

    const handleUpdateAmenity = async () => {
        if (!formData.amenityName) {
            setError('Tên tiện nghi không được để trống');
            return;
        }
        try {
            await amenityRoute.updateAmenity(editingId, formData);
            setSuccess('Cập nhật tiện nghi thành công');
            setFormData({ amenityName: '', description: '', amenityStatus: 'AVAILABLE' });
            setEditingId(null);
            fetchAmenities();
            setError(null);
        } catch (err) {
            setError('Lỗi khi cập nhật tiện nghi');
        }
    };

    const handleDeleteAmenity = async (id) => {
        if (window.confirm('Bạn có chắc muốn xóa tiện nghi này?')) {
            try {
                await amenityRoute.deleteAmenity(id);
                setSuccess('Xóa tiện nghi thành công');
                fetchAmenities();
                setError(null);
            } catch (err) {
                setError('Lỗi khi xóa tiện nghi');
            }
        }
    };

    const handleCancelEdit = () => {
        setEditingId(null);
        setFormData({ amenityName: '', description: '', amenityStatus: 'AVAILABLE' });
    };

    return {
        amenities,
        formData,
        editingId,
        error,
        success,
        setSuccess,
        handleInputChange,
        handleAddAmenity,
        handleEditAmenity,
        handleUpdateAmenity,
        handleDeleteAmenity,
        handleCancelEdit,
    };
};

export default AmenityLogic;