import React, { useState, useEffect } from 'react';
import { Button, Table, Form, Modal, Alert, Spinner, Card, Badge } from 'react-bootstrap';
import { FaUserPlus, FaEdit, FaTrash, FaHotel, FaCalendarAlt, FaEnvelope, FaUserTie } from 'react-icons/fa';
import useStaffHotelLogic from './StaffHotelLogic';

const StaffHotelUI = () => {
  const {
    loading,
    error,
    staffList,
    hotels,
    hotelsLoaded,
    addStaff,
    updateStaff,
    deleteStaff,
    setError,
    fetchHotels,
  } = useStaffHotelLogic();

  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    position: 'RECEPTIONIST',
    hotelId: '',
    startDate: '',
    endDate: '',
  });
  const [editMode, setEditMode] = useState(false);
  const [currentStaffId, setCurrentStaffId] = useState(null);

  const positionOptions = [
    { value: 'RECEPTIONIST', label: 'Lễ tân' },
    { value: 'MANAGER', label: 'Quản lý' },
  ];

  const positionColors = {
    RECEPTIONIST: 'primary',
    MANAGER: 'success'
  };

  // Debug effect để xem hotels data
  useEffect(() => {
    console.log('Hotels in UI:', hotels);
    console.log('Hotels loaded:', hotelsLoaded);
  }, [hotels, hotelsLoaded]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Validate hotel selection
    if (!formData.hotelId) {
      setError('Vui lòng chọn khách sạn');
      return;
    }
    
    const staffData = {
      ...formData,
      position: formData.position.toUpperCase(),
    };

    console.log('Submitting staff data:', staffData); // Debug log

    try {
      if (editMode) {
        const result = await updateStaff(currentStaffId, staffData);
        if (!result.success) throw new Error(result.message);
      } else {
        const result = await addStaff(staffData);
        if (!result.success) throw new Error(result.message);
      }
      handleCloseModal();
    } catch (err) {
      console.error('Submit error:', err);
      // Error is already set in useStaffHotelLogic
    }
  };

  const handleEdit = (staff) => {
    console.log('Editing staff:', staff); // Debug log
    
    setFormData({
      email: staff.email,
      password: '',
      position: staff.position,
      hotelId: staff.hotel?.hotelId || staff.hotelId || '', // Handle both formats
      startDate: staff.startDate,
      endDate: staff.endDate || '',
    });
    setCurrentStaffId(staff.staffHotelId);
    setEditMode(true);
    setShowModal(true);
  };

  const handleDelete = async (staffId) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa nhân viên này?')) {
      try {
        const result = await deleteStaff(staffId);
        if (!result.success) throw new Error(result.message);
      } catch (err) {
        console.error('Delete error:', err);
      }
    }
  };

  const handleOpenModal = () => {
    // Đảm bảo chọn hotel đầu tiên nếu có
    const defaultHotelId = hotels.length > 0 ? hotels[0].hotelId : '';
    
    setFormData({
      email: '',
      password: '',
      position: 'RECEPTIONIST',
      hotelId: defaultHotelId,
      startDate: '',
      endDate: '',
    });
    setEditMode(false);
    setCurrentStaffId(null);
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setError(null);
  };

  const formatDate = (dateString) => {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleDateString('vi-VN');
  };

  // Function to get hotel name by ID
const getHotelNameById = (hotelId) => {
  if (!Array.isArray(hotels)) {
    console.warn('hotels is not an array:', hotels); // Debug
    return 'N/A';
  }
  const hotel = hotels.find(h => h.hotelId === hotelId);
  return hotel ? hotel.hotelName : 'N/A';
};

  // Function to get hotel name from staff object
  const getStaffHotelName = (staff) => {
    // Try multiple possible formats
    if (staff.hotelName) return staff.hotelName;
    if (staff.hotel && staff.hotel.hotelName) return staff.hotel.hotelName;
    if (staff.hotel && staff.hotel.hotelId) return getHotelNameById(staff.hotel.hotelId);
    if (staff.hotelId) return getHotelNameById(staff.hotelId);
    return 'N/A';
  };

  return (

    <div className="container-fluid py-4">
      <div className="row">
        <div className="col-12">
          <Card className="shadow">
            <Card.Header className="bg-primary text-white">
              <div className="d-flex justify-content-between align-items-center">
                <h4 className="mb-0">
                  <FaUserTie className="me-2" />
                  Quản lý nhân viên
                </h4>
                <Button 
                  variant="light" 
                  onClick={handleOpenModal} 
                  disabled={loading || !hotelsLoaded}
                  className="d-flex align-items-center"
                >
                  <FaUserPlus className="me-2" />
                  Thêm nhân viên
                </Button>
              </div>
            </Card.Header>
            
            <Card.Body>
              {error && (
                <Alert variant="danger" onClose={() => setError(null)} dismissible className="mb-4">
                  <strong>Lỗi!</strong> {error}
                </Alert>
              )}

              {/* Show loading for hotels if not loaded */}
              {!hotelsLoaded && (
                <Alert variant="info" className="mb-4">
                  <Spinner animation="border" size="sm" className="me-2" />
                  Đang tải danh sách khách sạn...
                </Alert>
              )}

              {/* Show warning if no hotels */}
              {hotelsLoaded && hotels.length === 0 && (
                <Alert variant="warning" className="mb-4">
                  <strong>Chú ý!</strong> Chưa có khách sạn nào. Vui lòng thêm khách sạn trước khi thêm nhân viên.
                </Alert>
              )}

              {loading && staffList.length === 0 ? (
                <div className="text-center py-5">
                  <Spinner animation="border" role="status" variant="primary">
                    <span className="visually-hidden">Đang tải...</span>
                  </Spinner>
                  <p className="mt-3">Đang tải dữ liệu nhân viên...</p>
                </div>
              ) : staffList.length === 0 ? (
                <div className="text-center py-5">
                  <img 
                    src="/empty-state.svg" 
                    alt="No staff" 
                    style={{ width: '200px', opacity: 0.7 }}
                    className="mb-4"
                  />
                  <h5 className="text-muted">Chưa có nhân viên nào</h5>
                  <p className="text-muted">Hãy thêm nhân viên mới để bắt đầu</p>
                  <Button 
                    variant="primary" 
                    onClick={handleOpenModal}
                    disabled={!hotelsLoaded || hotels.length === 0}
                  >
                    <FaUserPlus className="me-2" />
                    Thêm nhân viên đầu tiên
                  </Button>
                </div>
              ) : (
                <div className="table-responsive">
                  <Table hover className="align-middle">
                    <thead className="table-light">
                      <tr>
                        <th>Email</th>
                        <th>Vị trí</th>
                        <th>Khách sạn</th>
                        <th>Thời gian làm việc</th>
                        <th className="text-end">Thao tác</th>
                      </tr>
                    </thead>
                    <tbody>
                      {staffList.map((staff) => (
                        <tr key={staff.staffHotelId}>
                          <td>
                            <div className="d-flex align-items-center">
                              <div className="bg-primary bg-opacity-10 rounded-circle p-2 me-3">
                                <FaEnvelope className="text-primary" />
                              </div>
                              <div>
                                <strong>{staff.email}</strong>
                              </div>
                            </div>
                          </td>
                          <td>
                            <Badge bg={positionColors[staff.position]} className="text-uppercase">
                              {positionOptions.find((p) => p.value === staff.position)?.label || staff.position}
                            </Badge>
                          </td>
                          <td>
                            <div className="d-flex align-items-center">
                              <div className="bg-info bg-opacity-10 rounded-circle p-2 me-3">
                                <FaHotel className="text-info" />
                              </div>
                              <div>
                                <strong>{getStaffHotelName(staff)}</strong>
                              </div>
                            </div>
                          </td>
                          <td>
                            <div className="d-flex align-items-center">
                              <div className="bg-warning bg-opacity-10 rounded-circle p-2 me-3">
                                <FaCalendarAlt className="text-warning" />
                              </div>
                              <div>
                                <div>
                                  <small className="text-muted">Bắt đầu:</small> {formatDate(staff.startDate)}
                                </div>
                                <div>
                                  <small className="text-muted">Kết thúc:</small> {staff.endDate ? formatDate(staff.endDate) : 'Đang làm việc'}
                                </div>
                              </div>
                            </div>
                          </td>
                          <td className="text-end">
                            <Button
                              variant="outline-primary"
                              size="sm"
                              className="me-2"
                              onClick={() => handleEdit(staff)}
                              disabled={loading}
                            >
                              <FaEdit className="me-1" />
                              Sửa
                            </Button>
                            <Button
                              variant="outline-danger"
                              size="sm"
                              onClick={() => handleDelete(staff.staffHotelId)}
                              disabled={loading}
                            >
                              <FaTrash className="me-1" />
                              Xóa
                            </Button>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </Table>
                </div>
              )}
            </Card.Body>
          </Card>
        </div>
      </div>

      {/* Add/Edit Modal */}
      <Modal show={showModal} onHide={handleCloseModal} centered>
        <Modal.Header closeButton className="border-0 pb-0">
          <Modal.Title>
            <h5 className="fw-bold">
              {editMode ? 'Cập nhật thông tin nhân viên' : 'Thêm nhân viên mới'}
            </h5>
          </Modal.Title>
        </Modal.Header>
        <Form onSubmit={handleSubmit}>
          <Modal.Body className="pt-0">
            {error && (
              <Alert variant="danger" className="mb-4">
                <strong>Lỗi!</strong> {error}
              </Alert>
            )}

            <Form.Group className="mb-3">
              <Form.Label className="fw-bold">Email</Form.Label>
              <div className="input-group">
                <span className="input-group-text">
                  <FaEnvelope />
                </span>
                <Form.Control
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleInputChange}
                  required
                  disabled={editMode}
                  placeholder="Nhập email nhân viên"
                />
              </div>
            </Form.Group>

            {!editMode && (
              <Form.Group className="mb-3">
                <Form.Label className="fw-bold">Mật khẩu</Form.Label>
                <div className="input-group">
                  <span className="input-group-text">
                    <i className="fas fa-lock"></i>
                  </span>
                  <Form.Control
                    type="password"
                    name="password"
                    value={formData.password}
                    onChange={handleInputChange}
                    required
                    placeholder="Nhập mật khẩu (tối thiểu 6 ký tự)"
                  />
                </div>
              </Form.Group>
            )}

            <Form.Group className="mb-3">
              <Form.Label className="fw-bold">Vị trí</Form.Label>
              <div className="input-group">
                <span className="input-group-text">
                  <i className="fas fa-user-tag"></i>
                </span>
                <Form.Select
                  name="position"
                  value={formData.position}
                  onChange={handleInputChange}
                  required
                >
                  {positionOptions.map((option) => (
                    <option key={option.value} value={option.value}>
                      {option.label}
                    </option>
                  ))}
                </Form.Select>
              </div>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label className="fw-bold">
                Khách sạn
                {!hotelsLoaded && <Spinner animation="border" size="sm" className="ms-2" />}
              </Form.Label>
              <div className="input-group">
                <span className="input-group-text">
                  <FaHotel />
                </span>
                {hotelsLoaded && hotels.length > 0 ? (
                  <Form.Select
                    name="hotelId"
                    value={formData.hotelId}
                    onChange={handleInputChange}
                    required
                  >
                    <option value="">Chọn khách sạn</option>
                    {hotels.map((hotel) => (
                      <option key={hotel.hotelId} value={hotel.hotelId}>
                        {hotel.hotelName} (ID: {hotel.hotelId})
                      </option>
                    ))}
                  </Form.Select>
                ) : hotelsLoaded && hotels.length === 0 ? (
                  <Form.Control
                    type="text"
                    disabled
                    value="Không có khách sạn nào - Vui lòng thêm khách sạn trước"
                  />
                ) : (
                  <Form.Control
                    type="text"
                    disabled
                    value="Đang tải danh sách khách sạn..."
                  />
                )}
              </div>
              {hotelsLoaded && hotels.length === 0 && (
                <Form.Text className="text-danger">
                  Cần thêm ít nhất một khách sạn trước khi thêm nhân viên
                </Form.Text>
              )}
            </Form.Group>

            <div className="row">
              <div className="col-md-6">
                <Form.Group className="mb-3">
                  <Form.Label className="fw-bold">Ngày bắt đầu</Form.Label>
                  <div className="input-group">
                    <span className="input-group-text">
                      <FaCalendarAlt />
                    </span>
                    <Form.Control
                      type="date"
                      name="startDate"
                      value={formData.startDate}
                      onChange={handleInputChange}
                      required
                    />
                  </div>
                </Form.Group>
              </div>
              <div className="col-md-6">
                <Form.Group className="mb-3">
                  <Form.Label className="fw-bold">Ngày kết thúc (nếu có)</Form.Label>
                  <div className="input-group">
                    <span className="input-group-text">
                      <FaCalendarAlt />
                    </span>
                    <Form.Control
                      type="date"
                      name="endDate"
                      value={formData.endDate}
                      onChange={handleInputChange}
                    />
                  </div>
                </Form.Group>
              </div>
            </div>

            {/* Debug info - remove in production */}
            {process.env.NODE_ENV === 'development' && (
              <div className="mt-3 p-2 bg-light rounded">
                <small>
                  <strong>Debug:</strong> Hotels loaded: {hotelsLoaded ? 'Yes' : 'No'}, 
                  Hotels count: {hotels.length},
                  Selected hotel: {formData.hotelId}
                </small>
              </div>
            )}
          </Modal.Body>
          <Modal.Footer className="border-0">
            <Button variant="outline-secondary" onClick={handleCloseModal} disabled={loading}>
              Hủy bỏ
            </Button>
            <Button
              variant="primary"
              type="submit"
              disabled={loading || !hotelsLoaded || hotels.length === 0}
              className="d-flex align-items-center"
            >
              {loading ? (
                <>
                  <Spinner animation="border" size="sm" className="me-2" />
                  Đang xử lý...
                </>
              ) : editMode ? (
                'Cập nhật thông tin'
              ) : (
                'Thêm nhân viên mới'
              )}
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
    </div>
  );
};

export default StaffHotelUI;