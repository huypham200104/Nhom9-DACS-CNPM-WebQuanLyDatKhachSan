import { useState, useEffect, useCallback } from 'react';
import discountRoute from '../routes/DiscountRoute';

const DiscountLogic = () => {
    const [discounts, setDiscounts] = useState([]);
    const [formData, setFormData] = useState({
        nameDiscount: '',
        discountCode: '',
        percentage: 0,
        startDate: '',
        endDate: '',
        numberOfUses: 0,
        usedCount: 0,
        minimumBookingAmount: 0,
        status: 'ACTIVE',
        hotel: { hotelId: '' }
    });
    const [editingId, setEditingId] = useState(null);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    const getHotelId = () => sessionStorage.getItem('hotelId');

    const fetchDiscounts = useCallback(async () => {
        const hotelId = getHotelId();
        if (!hotelId) {
            setError('Không tìm thấy hotelId trong sessionStorage');
            setDiscounts([]);
            return;
        }
        try {
            const response = await discountRoute.getDiscountsByHotelId(hotelId);
            setDiscounts(Array.isArray(response) ? response : response.data || []);
            setError(null);
        } catch (err) {
            console.error('Lỗi fetch discounts:', err);
            setError('Không thể tải danh sách mã giảm giá');
            setDiscounts([]);
        }
    }, []);

    useEffect(() => {
        const hotelId = getHotelId();
        if (hotelId) {
            setFormData(prev => ({ ...prev, hotel: { hotelId } }));
            fetchDiscounts();
        } else {
            setError('Không tìm thấy hotelId trong sessionStorage');
        }
    }, [fetchDiscounts]);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: name === 'percentage' || name === 'numberOfUses' || name === 'usedCount' || name === 'minimumBookingAmount'
                ? parseInt(value) || 0
                : value
        });
    };

    const handleAddDiscount = async () => {
        const hotelId = getHotelId();
        if (!hotelId) {
            setError('Không tìm thấy hotelId trong sessionStorage');
            return;
        }
        if (!formData.discountCode || !formData.percentage || !formData.nameDiscount) {
            setError('Tên, mã giảm giá và phần trăm giảm không được để trống');
            return;
        }
        try {
            const payload = {
                ...formData,
                hotel: { hotelId }
            };
            await discountRoute.addDiscount(payload);
            setSuccess('Thêm mã giảm giá thành công');
            setFormData({
                nameDiscount: '',
                discountCode: '',
                percentage: 0,
                startDate: '',
                endDate: '',
                numberOfUses: 0,
                usedCount: 0,
                minimumBookingAmount: 0,
                status: 'ACTIVE',
                hotel: { hotelId }
            });
            fetchDiscounts();
            setError(null);
        } catch (err) {
            console.error('Lỗi khi thêm mã giảm giá:', err);
            setError('Lỗi khi thêm mã giảm giá');
        }
    };

    const handleEditDiscount = (discount) => {
        const hotelId = getHotelId();
        if (!hotelId) {
            setError('Không tìm thấy hotelId trong sessionStorage');
            return;
        }
        setEditingId(discount.discountId);
        setFormData({
            nameDiscount: discount.nameDiscount,
            discountCode: discount.discountCode,
            percentage: discount.percentage,
            startDate: discount.startDate,
            endDate: discount.endDate,
            numberOfUses: discount.numberOfUses,
            usedCount: discount.usedCount,
            minimumBookingAmount: discount.minimumBookingAmount,
            status: discount.status,
            hotel: { hotelId }
        });
    };

    const handleUpdateDiscount = async () => {
        const hotelId = getHotelId();
        if (!hotelId) {
            setError('Không tìm thấy hotelId trong sessionStorage');
            return;
        }
        if (!formData.discountCode || !formData.percentage || !formData.nameDiscount) {
            setError('Tên, mã giảm giá và phần trăm giảm không được để trống');
            return;
        }
        try {
            const updatedDiscount = {
                discountId: editingId,
                ...formData,
                hotel: { hotelId }
            };
            await discountRoute.updateDiscount(updatedDiscount);
            setSuccess('Cập nhật mã giảm giá thành công');
            setFormData({
                nameDiscount: '',
                discountCode: '',
                percentage: 0,
                startDate: '',
                endDate: '',
                numberOfUses: 0,
                usedCount: 0,
                minimumBookingAmount: 0,
                status: 'ACTIVE',
                hotel: { hotelId }
            });
            setEditingId(null);
            fetchDiscounts();
            setError(null);
        } catch (err) {
            console.error('Lỗi khi cập nhật mã giảm giá:', err);
            setError('Lỗi khi cập nhật mã giảm giá');
        }
    };

    const handleDeleteDiscount = async (id) => {
        const hotelId = getHotelId();
        if (!hotelId) {
            setError('Không tìm thấy hotelId trong sessionStorage');
            return;
        }
        if (window.confirm('Bạn có chắc muốn xóa mã giảm giá này?')) {
            try {
                await discountRoute.deleteDiscount(id);
                setSuccess('Xóa mã giảm giá thành công');
                fetchDiscounts();
                setError(null);
            } catch (err) {
                console.error('Lỗi khi xóa mã giảm giá:', err);
                setError('Lỗi khi xóa mã giảm giá');
            }
        }
    };

    const handleCancelEdit = () => {
        const hotelId = getHotelId();
        setEditingId(null);
        setFormData({
            nameDiscount: '',
            discountCode: '',
            percentage: 0,
            startDate: '',
            endDate: '',
            numberOfUses: 0,
            usedCount: 0,
            minimumBookingAmount: 0,
            status: 'ACTIVE',
            hotel: { hotelId: hotelId || '' }
        });
    };

    return {
        discounts,
        formData,
        editingId,
        error,
        success,
        setSuccess,
        handleInputChange,
        handleAddDiscount,
        handleEditDiscount,
        handleUpdateDiscount,
        handleDeleteDiscount,
        handleCancelEdit,
    };
};

export default DiscountLogic;