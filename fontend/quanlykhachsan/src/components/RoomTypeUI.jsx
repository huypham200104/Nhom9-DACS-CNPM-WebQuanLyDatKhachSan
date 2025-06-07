import RoomTypeLogic from './RoomTypeLogic';

const RoomTypeUI = () => {
  const {
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
  } = RoomTypeLogic();

  return (
    <div className="container mt-4">
      <h1 className="h2 mb-4">Quản lý loại phòng</h1>

      {/* Biểu mẫu thêm/sửa loại phòng */}
      <div className="card mb-4">
        <div className="card-header">
          {editingId ? 'Chỉnh sửa loại phòng' : 'Thêm loại phòng mới'}
        </div>
        <div className="card-body">
          <div className="mb-3">
            <input
              type="text"
              name="roomTypeName"
              value={formData.roomTypeName}
              onChange={handleInputChange}
              placeholder="Tên loại phòng (VD: Deluxe Room)"
              className="form-control"
              required
            />
          </div>
          <div className="mb-3">
            <label className="form-label">Tiện ích</label>
            <div className="overflow-auto border p-2 rounded" style={{ maxHeight: '150px' }}>
              {loading && <p className="text-muted">Đang tải tiện ích...</p>}
              {!loading && (!amenities || amenities.length === 0) && (
                <p className="text-muted">Không có tiện ích nào.</p>
              )}
              {!loading && amenities?.length > 0 && amenities.map((amenity) => (
                <div key={amenity.amenityId} className="form-check">
                  <input
                    className="form-check-input"
                    type="checkbox"
                    checked={formData.amenityIds.includes(amenity.amenityId)}
                    onChange={() => handleAmenityChange(amenity.amenityId)}
                    disabled={loading}
                  />
                  <label className="form-check-label">
                    {amenity.amenityName} ({amenity.amenityStatus})
                  </label>
                </div>
              ))}
            </div>
          </div>
          <div className="d-flex gap-2">
            {editingId ? (
              <>
                <button 
                  onClick={handleUpdateRoomType} 
                  className="btn btn-primary"
                  disabled={loading}
                >
                  {loading ? 'Đang cập nhật...' : 'Cập nhật'}
                </button>
                <button 
                  onClick={handleCancelEdit} 
                  className="btn btn-secondary"
                  disabled={loading}
                >
                  Hủy
                </button>
              </>
            ) : (
              <button 
                onClick={handleAddRoomType} 
                className="btn btn-success"
                disabled={loading}
              >
                {loading ? 'Đang thêm...' : 'Thêm'}
              </button>
            )}
          </div>
          {error && <p className="text-danger mt-2">{error}</p>}
        </div>
      </div>

      {/* Danh sách loại phòng */}
      <div className="card">
        <div className="card-header">Danh sách loại phòng</div>
        <div className="card-body">
          {loading && <p className="text-muted">Đang tải...</p>}
          {!loading && (!roomTypes || roomTypes.length === 0) && (
            <p className="text-muted">Không có loại phòng nào.</p>
          )}
          {!loading && roomTypes?.length > 0 && roomTypes.map((roomType) => (
            <div 
              key={roomType.roomTypeId} 
              className="d-flex justify-content-between align-items-center p-2 border rounded mb-2"
            >
              <div>
                <p className="mb-1"><strong>{roomType.roomTypeName}</strong></p>
                <p className="text-muted mb-1">ID: {roomType.roomTypeId}</p>
                <p className="text-muted">
                  Tiện ích: {roomType.amenityNames?.join(', ') || 'Không có tiện ích'}
                </p>
              </div>
              <div>
                <button 
                  onClick={() => handleEditRoomType(roomType)} 
                  className="btn btn-warning me-2"
                  disabled={loading}
                >
                  Sửa
                </button>
                <button 
                  onClick={() => handleDeleteRoomType(roomType.roomTypeId)} 
                  className="btn btn-danger"
                  disabled={loading}
                >
                  Xóa
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default RoomTypeUI;