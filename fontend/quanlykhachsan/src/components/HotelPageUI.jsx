import React from 'react';

const HotelPageUI = ({
  hotels,
  formData,
  files,
  editingId,
  error,
  loading,
  handleInputChange,
  handleFileChange,
  handleAddHotel,
  handleEditHotel,
  handleUpdateHotel,
  handleDeleteHotel,
  handleApproveHotel,
  handleRejectHotel,
  handleSearchHotels,
  handleSearchInputChange,
  handleCancelEdit,
  searchParams,
  currentPage,
  totalPages,
  handlePageChange,
}) => {
  return (
    <div className="container mx-auto p-4 min-vh-100" style={{ background: 'linear-gradient(to bottom right, #e6f0fa, #e0e7ff, #f5e6ff)' }}>
      <h1 className="text-center mb-4 display-4 text-primary fw-bold" style={{ textShadow: '1px 1px 3px rgba(0,0,0,0.2)' }}>Quản lý khách sạn</h1>

      {error && <div className="alert alert-danger mb-4">{error}</div>}
      {loading && <div className="text-center text-primary mb-4">Đang tải...</div>}

      {/* Search Form */}
      <div className="mb-4 p-4 bg-white rounded shadow-sm">
        <h2 className="h4 mb-3 text-primary fw-semibold">Tìm kiếm khách sạn</h2>
        <form onSubmit={handleSearchHotels} className="row g-3">
          {['city', 'district', 'ward', 'street', 'houseNumber'].map((field) => (
            <div className="col-md-2" key={field}>
              <input
                type="text"
                name={field}
                value={searchParams[field]}
                onChange={handleSearchInputChange}
                placeholder={
                  field === 'city'
                    ? 'Thành phố'
                    : field === 'district'
                    ? 'Quận/Huyện'
                    : field === 'ward'
                    ? 'Phường/Xã'
                    : field === 'street'
                    ? 'Đường'
                    : 'Số nhà'
                }
                className="form-control border-secondary"
              />
            </div>
          ))}
          <div className="col-md-1">
            <button type="submit" className="btn btn-primary w-100">Tìm kiếm</button>
          </div>
        </form>
      </div>

      {/* Add/Edit Form */}
      <div className="mb-4 p-4 bg-white rounded shadow-sm">
        <h2 className="h4 mb-3 text-primary fw-semibold">
          {editingId ? 'Sửa khách sạn' : 'Thêm khách sạn'}
        </h2>
        <form onSubmit={editingId ? handleUpdateHotel : handleAddHotel} className="row g-3">
          {['name', 'city', 'district', 'ward', 'street', 'houseNumber'].map((field) => (
            <div className="col-md-3" key={field}>
              <input
                type="text"
                name={field}
                value={formData[field]}
                onChange={handleInputChange}
                placeholder={
                  field === 'name'
                    ? 'Tên khách sạn'
                    : field === 'city'
                    ? 'Thành phố'
                    : field === 'district'
                    ? 'Quận/Huyện'
                    : field === 'ward'
                    ? 'Phường/Xã'
                    : field === 'street'
                    ? 'Đường'
                    : 'Số nhà'
                }
                className="form-control border-secondary"
                required={field === 'name' || field === 'city'}
              />
            </div>
          ))}
          <div className="col-md-2">
            <input
              type="number"
              name="hotelRating"
              value={formData.hotelRating}
              onChange={handleInputChange}
              placeholder="Đánh giá (1-5)"
              className="form-control border-secondary"
              min="1"
              max="5"
            />
          </div>
          <div className="col-md-2">
            <input
              type="file"
              multiple
              onChange={handleFileChange}
              className="form-control border-secondary"
            />
          </div>
          <div className="col-md-2 d-flex align-items-end">
            <button type="submit" className="btn btn-success me-2">{editingId ? 'Cập nhật' : 'Thêm'} khách sạn</button>
            {editingId && (
              <button type="button" onClick={handleCancelEdit} className="btn btn-secondary">Hủy</button>
            )}
          </div>
        </form>
      </div>

      {/* Hotels Grid */}
      <div className="mb-4">
        <h2 className="h4 mb-3 text-primary fw-semibold">Danh sách khách sạn</h2>
        {hotels.length > 0 ? (
          <div className="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-4">
            {hotels.map((hotel) => (
              <div key={hotel.hotelId} className="col">
                <div className="card h-100 shadow-sm border-secondary">
                  <h3 className="card-title fw-semibold text-primary mt-2">{hotel.hotelName}</h3>
                  <div className="card-body p-2">
                    <p><strong>Thành phố:</strong> {hotel.city}</p>
                    <p><strong>Quận/Huyện:</strong> {hotel.district || 'N/A'}</p>
                    <p><strong>Phường/Xã:</strong> {hotel.ward || 'N/A'}</p>
                    <p><strong>Đường:</strong> {hotel.street || 'N/A'}</p>
                    <p><strong>Số nhà:</strong> {hotel.houseNumber || 'N/A'}</p>
                    <p><strong>Đánh giá:</strong> {hotel.hotelRating || 'Chưa có'}</p>
                    <p><strong>Trạng thái:</strong> {hotel.approvalStatus}</p>
                    {hotel.imageHotels && hotel.imageHotels.length > 0 ? (
                      <img
                        src={hotel.imageHotels[0].imageData}
                        alt={`Khách sạn ${hotel.hotelName}`}
                        className="card-img-top img-fluid"
                        style={{ height: '150px', objectFit: 'cover' }}
                      />
                    ) : (
                      <div className="card-img-top bg-secondary text-white d-flex align-items-center justify-content-center" style={{ height: '150px' }}>
                        Không có ảnh
                      </div>
                    )}
                  </div>
                  <div className="card-footer p-2">
                    <div className="d-flex flex-wrap gap-2">
                      <button onClick={() => handleEditHotel(hotel.hotelId)} className="btn btn-warning text-white">Sửa</button>
                      <button onClick={() => handleDeleteHotel(hotel.hotelId)} className="btn btn-danger">Xóa</button>
                      <button onClick={() => handleApproveHotel(hotel.hotelId)} className="btn btn-success">Phê duyệt</button>
                      <button onClick={() => handleRejectHotel(hotel.hotelId)} className="btn btn-secondary">Từ chối</button>
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <p className="text-center text-secondary">Không tìm thấy khách sạn</p>
        )}
      </div>

      {/* Pagination Controls */}
      <div className="d-flex justify-content-center align-items-center gap-3 mb-4">
        <button
          onClick={() => handlePageChange(currentPage - 1)}
          disabled={currentPage === 1}
          className="btn btn-primary disabled:bg-gray-300"
        >
          Trang trước
        </button>
        <span className="text-primary fw-medium">Trang {currentPage} / {totalPages}</span>
        <button
          onClick={() => handlePageChange(currentPage + 1)}
          disabled={currentPage === totalPages}
          className="btn btn-primary disabled:bg-gray-300"
        >
          Trang sau
        </button>
      </div>
    </div>
  );
};

export default HotelPageUI;