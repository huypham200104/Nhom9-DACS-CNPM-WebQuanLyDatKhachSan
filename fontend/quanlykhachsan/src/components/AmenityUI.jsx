import AmenityLogic from './AmenityLogic';

const AmenityUI = () => {
    const {
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
    } = AmenityLogic();

    return (
        <div className="container-fluid py-4">
            <div className="row">
                <div className="col-12">
                    <div className="card my-4">
                        <div className="card-header p-0 position-relative mt-n4 mx-3 z-index-2">
    <div 
        className="border-radius-lg pt-4 pb-3"
        style={{
            background: 'linear-gradient(195deg, rgb(103, 102, 229), rgb(130, 129, 237))',
            boxShadow: '0 4px 20px 0 rgba(0, 0, 0, 0.14), 0 7px 10px -5px rgba(103, 102, 229, 0.4)'
        }}
    >
        <h6 className="text-white text-capitalize ps-3">Quản lý tiện nghi</h6>
    </div>
</div>
                        <div className="card-body px-0 pb-2">
                            <div className="container">
                                {/* Thông báo lỗi và thành công */}
                                {error && (
                                    <div className="alert alert-danger alert-dismissible fade show mx-3" role="alert">
                                        <span className="alert-text">{error}</span>
                                        <button type="button" className="btn-close" onClick={() => setSuccess(null)} aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                )}
                                {success && (
                                    <div className="alert alert-success alert-dismissible fade show mx-3" role="alert">
                                        <span className="alert-text">{success}</span>
                                        <button type="button" className="btn-close" onClick={() => setSuccess(null)} aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                )}

                                {/* Form thêm/sửa tiện nghi */}
                                <div className="row">
                                    <div className="col-md-6">
                                        <div className="card mb-4">
                                            <div className="card-header bg-transparent">
                                                <h5 className="mb-0">{editingId ? 'Sửa tiện nghi' : 'Thêm tiện nghi mới'}</h5>
                                            </div>
                                            <div className="card-body">
                                                <form>
                                                    <div className="mb-3">
                                                        <label htmlFor="amenityName" className="form-label">Tên tiện nghi</label>
                                                        <input
                                                            type="text"
                                                            className="form-control"
                                                            id="amenityName"
                                                            name="amenityName"
                                                            value={formData.amenityName}
                                                            onChange={handleInputChange}
                                                            placeholder="Nhập tên tiện nghi"
                                                        />
                                                    </div>
                                                    <div className="mb-3">
                                                        <label htmlFor="description" className="form-label">Mô tả</label>
                                                        <textarea
                                                            className="form-control"
                                                            id="description"
                                                            name="description"
                                                            rows="3"
                                                            value={formData.description}
                                                            onChange={handleInputChange}
                                                            placeholder="Nhập mô tả tiện nghi"
                                                        />
                                                    </div>
                                                    <div className="mb-3">
                                                        <label htmlFor="amenityStatus" className="form-label">Trạng thái</label>
                                                        <select
                                                            className="form-select"
                                                            id="amenityStatus"
                                                            name="amenityStatus"
                                                            value={formData.amenityStatus}
                                                            onChange={handleInputChange}
                                                        >
                                                            <option value="AVAILABLE">Có sẵn</option>
                                                            <option value="UNAVAILABLE">Không có sẵn</option>
                                                        </select>
                                                    </div>
                                                    <div className="d-flex justify-content-end gap-2">
                                                        {editingId ? (
                                                            <>
                                                                <button type="button" className="btn btn-outline-dark" onClick={handleCancelEdit}>
                                                                    Hủy
                                                                </button>
                                                                <button type="button" className="btn btn-primary" onClick={handleUpdateAmenity}>
                                                                    <i className="fas fa-save me-1"></i> Cập nhật
                                                                </button>
                                                            </>
                                                        ) : (
                                                            <button type="button" className="btn btn-primary" onClick={handleAddAmenity}>
                                                                <i className="fas fa-plus me-1"></i> Thêm mới
                                                            </button>
                                                        )}
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>

                                    {/* Bảng danh sách tiện nghi */}
                                    <div className="col-md-6">
                                        <div className="card mb-4">
                                            <div className="card-header bg-transparent">
                                                <h5 className="mb-0">Danh sách tiện nghi</h5>
                                            </div>
                                            <div className="card-body p-0">
                                                <div className="table-responsive p-0">
                                                    <table className="table align-items-center mb-0">
                                                        <thead>
                                                            <tr>
                                                                <th className="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Tên tiện nghi</th>
                                                                <th className="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-2">Mô tả</th>
                                                                <th className="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Trạng thái</th>
                                                                <th className="text-secondary opacity-7"></th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            {amenities.length > 0 ? (
                                                                amenities.map((amenity) => (
                                                                    <tr key={amenity.amenityId}>
                                                                        <td>
                                                                            <div className="d-flex px-2 py-1">
                                                                                <div className="d-flex flex-column justify-content-center">
                                                                                    <h6 className="mb-0 text-sm">{amenity.amenityName}</h6>
                                                                                    <p className="text-xs text-secondary mb-0">ID: {amenity.amenityId}</p>
                                                                                </div>
                                                                            </div>
                                                                        </td>
                                                                        <td>
                                                                            <p className="text-xs font-weight-bold mb-0">{amenity.description}</p>
                                                                        </td>
                                                                        <td className="align-middle text-center text-sm">
                                                                            <span className={`badge badge-sm ${amenity.amenityStatus === 'AVAILABLE' ? 'bg-gradient-success' : 'bg-gradient-secondary'}`}>
                                                                                {amenity.amenityStatus === 'AVAILABLE' ? 'Có sẵn' : 'Không có sẵn'}
                                                                            </span>
                                                                        </td>
                                                                        <td className="align-middle">
                                                                            <div className="d-flex gap-1">
                                                                                <button
                                                                                    className="btn btn-link text-warning mb-0 px-2"
                                                                                    onClick={() => handleEditAmenity(amenity)}
                                                                                    title="Sửa"
                                                                                >
                                                                                    <i className="fas fa-pencil-alt"></i>
                                                                                </button>
                                                                                <button
                                                                                    className="btn btn-link text-danger mb-0 px-2"
                                                                                    onClick={() => handleDeleteAmenity(amenity.amenityId)}
                                                                                    title="Xóa"
                                                                                >
                                                                                    <i className="fas fa-trash-alt"></i>
                                                                                </button>
                                                                            </div>
                                                                        </td>
                                                                    </tr>
                                                                ))
                                                            ) : (
                                                                <tr>
                                                                    <td colSpan="4" className="text-center py-4">
                                                                        <div className="d-flex flex-column align-items-center">
                                                                            <i className="fas fa-inbox fa-2x text-secondary mb-2"></i>
                                                                            <p className="text-muted">Không có tiện nghi nào</p>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                            )}
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AmenityUI;