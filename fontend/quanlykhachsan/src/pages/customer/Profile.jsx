import React, { useState, useEffect } from 'react';
import customerRoute from '../../routes/CustomerRoute'; // Adjust the path if needed
import Navbar from "../public/Navbar";

const Profile = () => {
    const [formData, setFormData] = useState({
        customerName: '',
        phone: '',
        email: ''
    });
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);
    const [loading, setLoading] = useState(true);

    const userId = sessionStorage.getItem('userId') || 'sample-user-id';

    useEffect(() => {
        const fetchCustomerData = async () => {
            setLoading(true);
            try {
                const response = await customerRoute.getCustomerByUserId(userId);
                const customer = response.data;
                setFormData({
                    customerName: customer.customerName || '',
                    phone: customer.phone || '',
                    email: customer.email || ''
                });
                setLoading(false);
            } catch (err) {
                setError(err.response?.data?.message || 'Lỗi khi lấy thông tin khách hàng.');
                setLoading(false);
            }
        };

        fetchCustomerData();
    }, [userId]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        setSuccess(null);

        try {
            const response = await customerRoute.updateCustomer(userId, {
                ...formData,
                userId
            });
            setSuccess(response.message || 'Cập nhật thông tin thành công!');
            console.log('Updated customer:', response.data);
        } catch (err) {
            setError(err.response?.data?.message || 'Lỗi khi cập nhật thông tin.');
        }
    };

    if (loading) {
        return (
            <>
                <Navbar />
                <div className="text-center mt-5">Đang tải...</div>
            </>
        );
    }

    return (
        <>
            <Navbar />
            <div className="container d-flex justify-content-center align-items-center" style={{ minHeight: '90vh' }}>
                <div className="card p-4 shadow" style={{ width: '100%', maxWidth: '500px' }}>
                    <h2 className="text-center mb-4">Chỉnh sửa hồ sơ khách hàng</h2>
                    {error && <div className="alert alert-danger">{error}</div>}
                    {success && <div className="alert alert-success">{success}</div>}
                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label className="form-label">Email</label>
                            <input
                                type="email"
                                name="email"
                                value={formData.email}
                                className="form-control"
                                readOnly
                                disabled
                            />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Họ và tên</label>
                            <input
                                type="text"
                                name="customerName"
                                value={formData.customerName}
                                onChange={handleChange}
                                className="form-control"
                                placeholder="Nhập họ và tên"
                                required
                            />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Số điện thoại</label>
                            <input
                                type="text"
                                name="phone"
                                value={formData.phone}
                                onChange={handleChange}
                                className="form-control"
                                placeholder="Nhập số điện thoại (10 chữ số)"
                                pattern="^\d{10}$"
                                required
                            />
                        </div>
                        <button type="submit" className="btn btn-primary w-100">
                            Cập nhật
                        </button>
                    </form>
                </div>
            </div>
        </>
    );
};

export default Profile;
