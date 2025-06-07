import React from 'react';
import DiscountLogic from './DiscountLogic';

const DiscountUI = () => {
    const {
        discounts,
        formData,
        editingId,
        error,
        success,
        handleInputChange,
        handleAddDiscount,
        handleEditDiscount,
        handleUpdateDiscount,
        handleDeleteDiscount,
        handleCancelEdit,
    } = DiscountLogic();

    return (
        <div className="container mt-4">
            <div className="card">
                <div className="card-header bg-primary text-white">
                    <h1 className="h4">Quản lý mã giảm giá</h1>
                </div>
                
                <div className="card-body">
                    {/* Status messages */}
                    {error && <div className="alert alert-danger">{error}</div>}
                    {success && <div className="alert alert-success">{success}</div>}

                    {/* Form */}
                    <div className="mb-4">
                        <h2 className="h5 mb-3">{editingId ? 'Cập nhật mã giảm giá' : 'Thêm mã giảm giá mới'}</h2>
                        <div className="row g-3">
                            <div className="col-md-4">
                                <label htmlFor="nameDiscount" className="form-label">Tên mã giảm giá</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    id="nameDiscount"
                                    name="nameDiscount"
                                    placeholder="Tên mã giảm giá"
                                    value={formData.nameDiscount || ''}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <div className="col-md-2">
                                <label htmlFor="percentage" className="form-label">Phần trăm giảm</label>
                                <input
                                    type="number"
                                    className="form-control"
                                    id="percentage"
                                    name="percentage"
                                    placeholder="%"
                                    value={formData.percentage || 0}
                                    onChange={handleInputChange}
                                    min="0"
                                    max="100"
                                />
                            </div>
                            <div className="col-md-3">
                                <label htmlFor="discountCode" className="form-label">Mã giảm giá</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    id="discountCode"
                                    name="discountCode"
                                    placeholder="Mã giảm giá"
                                    value={formData.discountCode || ''}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <div className="col-md-3">
                                <label htmlFor="startDate" className="form-label">Ngày bắt đầu</label>
                                <input
                                    type="date"
                                    className="form-control"
                                    id="startDate"
                                    name="startDate"
                                    value={formData.startDate || ''}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <div className="col-md-3">
                                <label htmlFor="endDate" className="form-label">Ngày kết thúc</label>
                                <input
                                    type="date"
                                    className="form-control"
                                    id="endDate"
                                    name="endDate"
                                    value={formData.endDate || ''}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <div className="col-md-3">
                                <label htmlFor="numberOfUses" className="form-label">Số lần sử dụng</label>
                                <input
                                    type="number"
                                    className="form-control"
                                    id="numberOfUses"
                                    name="numberOfUses"
                                    placeholder="Số lần"
                                    value={formData.numberOfUses || 0}
                                    onChange={handleInputChange}
                                    min="0"
                                />
                            </div>
                            <div className="col-md-3">
                                <label htmlFor="minimumBookingAmount" className="form-label">Số tiền đặt phòng tối thiểu</label>
                                <input
                                    type="number"
                                    className="form-control"
                                    id="minimumBookingAmount"
                                    name="minimumBookingAmount"
                                    placeholder="Số tiền"
                                    value={formData.minimumBookingAmount || 0}
                                    onChange={handleInputChange}
                                    min="0"
                                />
                            </div>
                            <div className="col-md-3">
                                <label htmlFor="status" className="form-label">Trạng thái</label>
                                <select
                                    className="form-select"
                                    id="status"
                                    name="status"
                                    value={formData.status || 'ACTIVE'}
                                    onChange={handleInputChange}
                                >
                                    <option value="ACTIVE">Hoạt động</option>
                                    <option value="INACTIVE">Không hoạt động</option>
                                    <option value="EXPIRED">Hết hạn</option>
                                </select>
                            </div>
                            <div className="col-md-12 d-flex align-items-end">
                                <div>
                                    <button 
                                        className={`btn ${editingId ? 'btn-warning' : 'btn-success'} me-2`}
                                        onClick={editingId ? handleUpdateDiscount : handleAddDiscount}
                                    >
                                        {editingId ? 'Cập nhật' : 'Thêm mới'}
                                    </button>
                                    {editingId && (
                                        <button className="btn btn-secondary" onClick={handleCancelEdit}>
                                            Hủy bỏ
                                        </button>
                                    )}
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Discount list */}
                    <div>
                        <h2 className="h5 mb-3">Danh sách mã giảm giá</h2>
                        {discounts.length > 0 ? (
                            <div className="row">
                                {discounts.map((discount) => (
                                    <div key={discount.discountId} className="col-md-6 mb-3">
                                        <div className={`card ${discount.status === 'ACTIVE' ? 'border-success' : 'border-secondary'}`}>
                                            <div className="card-header d-flex justify-content-between align-items-center">
                                                <strong>{discount.nameDiscount}</strong>
                                                <span className={`badge ${discount.status === 'ACTIVE' ? 'bg-success' : 'bg-secondary'}`}>
                                                    {discount.status}
                                                </span>
                                            </div>
                                            <div className="card-body">
                                                <div className="row">
                                                    <div className="col-6">
                                                        <p className="mb-1"><strong>Giảm giá:</strong> {discount.percentage}%</p>
                                                        <p className="mb-1"><strong>Code:</strong> <code>{discount.discountCode}</code></p>
                                                        <p className="mb-1"><strong>Số tiền tối thiểu:</strong> {discount.minimumBookingAmount.toLocaleString()} VNĐ</p>
                                                    </div>
                                                    <div className="col-6">
                                                        <p className="mb-1"><strong>Ngày bắt đầu:</strong> {discount.startDate}</p>
                                                        <p className="mb-1"><strong>Ngày kết thúc:</strong> {discount.endDate}</p>
                                                        <p className="mb-1"><strong>Trạng thái:</strong> {discount.status}</p>
                                                    </div>
                                                </div>
                                                <div className="mt-2">
                                                    <p className="mb-1"><strong>Số lần sử dụng:</strong> 
                                                        <span className="ms-1">
                                                            {discount.usedCount} / {discount.numberOfUses}
                                                        </span>
                                                    </p>
                                                    <div className="progress mt-1" style={{height: '5px'}}>
                                                        <div 
                                                            className="progress-bar bg-info" 
                                                            role="progressbar" 
                                                            style={{width: `${(discount.usedCount / discount.numberOfUses) * 100}%`}}
                                                        ></div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div className="card-footer bg-transparent d-flex justify-content-end">
                                                <button 
                                                    className="btn btn-sm btn-outline-primary me-2"
                                                    onClick={() => handleEditDiscount(discount)}
                                                >
                                                    <i className="bi bi-pencil"></i> Sửa
                                                </button>
                                                <button 
                                                    className="btn btn-sm btn-outline-danger"
                                                    onClick={() => handleDeleteDiscount(discount.discountId)}
                                                >
                                                    <i className="bi bi-trash"></i> Xóa
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <div className="alert alert-info">Không có mã giảm giá nào.</div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default DiscountUI;