import { useState, useEffect } from 'react';
import roomTypeRoute from '../routes/RoomTypeRoute';
import amenityRoute from '../routes/AmenityRoute';

const RoomTypeLogic = () => {
  const [roomTypes, setRoomTypes] = useState([]);
  const [amenities, setAmenities] = useState([]);
  const [formData, setFormData] = useState({
    roomTypeName: '',
    amenityIds: [],
  });
  const [editingId, setEditingId] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const loadData = async () => {
      setLoading(true);
      try {
        // Tải amenities trước
        const amenityResponse = await amenityRoute.getAllAmenities();
        // Xử lý nếu API trả về { data: [...] }
        const amenitiesData = Array.isArray(amenityResponse.data) 
          ? amenityResponse.data 
          : Array.isArray(amenityResponse) 
            ? amenityResponse 
            : [];
        setAmenities(amenitiesData);

        // Tải roomTypes sau khi có amenities
        const roomTypeResponse = await roomTypeRoute.getAllRoomTypes();
        const types = Array.isArray(roomTypeResponse) ? roomTypeResponse : [];
        // Ánh xạ amenityIds sang amenityNames
        const enrichedTypes = types.map((roomType) => ({
          ...roomType,
          amenityNames: roomType.amenityIds
            .map((id) => {
              const amenity = amenitiesData.find((a) => a.amenityId === id);
              return amenity ? amenity.amenityName : null;
            })
            .filter((name) => name !== null),
        }));
        setRoomTypes(enrichedTypes);
        setError(null);
      } catch (err) {
        setError(err.message || 'Lỗi khi tải dữ liệu');
        setRoomTypes([]);
        setAmenities([]);
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleAmenityChange = (amenityId) => {
    setFormData((prev) => {
      const newAmenityIds = prev.amenityIds.includes(amenityId)
        ? prev.amenityIds.filter((id) => id !== amenityId)
        : [...prev.amenityIds, amenityId];
      return { ...prev, amenityIds: newAmenityIds };
    });
  };

  const handleAddRoomType = async () => {
    setLoading(true);
    try {
      const response = await roomTypeRoute.addRoomType({
        roomTypeName: formData.roomTypeName,
        amenityIds: formData.amenityIds,
      });
      if (response && response.roomTypeId) {
        const newRoomType = {
          ...response,
          amenityNames: formData.amenityIds
            .map((id) => {
              const amenity = amenities.find((a) => a.amenityId === id);
              return amenity ? amenity.amenityName : null;
            })
            .filter((name) => name !== null),
        };
        setRoomTypes([...roomTypes, newRoomType]);
        setFormData({ roomTypeName: '', amenityIds: [] });
        setError(null);
      } else {
        throw new Error('Dữ liệu trả về không hợp lệ');
      }
    } catch (err) {
      setError(err.message || 'Lỗi khi thêm loại phòng');
    } finally {
      setLoading(false);
    }
  };

  const handleEditRoomType = (roomType) => {
    setEditingId(roomType.roomTypeId);
    setFormData({
      roomTypeName: roomType.roomTypeName,
      amenityIds: Array.isArray(roomType.amenityIds) ? roomType.amenityIds : [],
    });
  };

  const handleUpdateRoomType = async () => {
    setLoading(true);
    try {
      const response = await roomTypeRoute.updateRoomType(editingId, {
        roomTypeName: formData.roomTypeName,
        amenityIds: formData.amenityIds,
      });
      if (response && response.roomTypeId) {
        const updatedRoomType = {
          ...response,
          amenityNames: formData.amenityIds
            .map((id) => {
              const amenity = amenities.find((a) => a.amenityId === id);
              return amenity ? amenity.amenityName : null;
            })
            .filter((name) => name !== null),
        };
        setRoomTypes(
          roomTypes.map((rt) =>
            rt.roomTypeId === editingId ? updatedRoomType : rt
          )
        );
        setFormData({ roomTypeName: '', amenityIds: [] });
        setEditingId(null);
        setError(null);
      } else {
        throw new Error('Dữ liệu trả về không hợp lệ');
      }
    } catch (err) {
      setError(err.message || 'Lỗi khi cập nhật loại phòng');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteRoomType = async (roomTypeId) => {
    setLoading(true);
    try {
      await roomTypeRoute.deleteRoomType(roomTypeId);
      setRoomTypes(roomTypes.filter((rt) => rt.roomTypeId !== roomTypeId));
      setError(null);
    } catch (err) {
      setError(err.message || 'Lỗi khi xóa loại phòng');
    } finally {
      setLoading(false);
    }
  };

  const handleCancelEdit = () => {
    setFormData({ roomTypeName: '', amenityIds: [] });
    setEditingId(null);
  };

  return {
    roomTypes,
    amenities,
    formData,
    editingId,
    error,
    loading,
    handleInputChange,
    handleAmenityChange,
    handleAddRoomType,
    handleEditRoomType,
    handleUpdateRoomType,
    handleDeleteRoomType,
    handleCancelEdit,
  };
};

export default RoomTypeLogic;