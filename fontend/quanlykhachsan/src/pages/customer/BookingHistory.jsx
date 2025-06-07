import React, { useState, useEffect } from 'react';
import { BookingAPI } from '../../routes/BookingRoute';
import customerRoute from '../../routes/CustomerRoute';
import { Spinner, Alert, Table, Container, Card, Badge, Button } from 'react-bootstrap';
import Navbar from '../public/Navbar';
import FeedbackCustomer from './FeedbackCustomer';

const BookingHistory = () => {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showFeedbackModal, setShowFeedbackModal] = useState(false);
  const [selectedBooking, setSelectedBooking] = useState(null);

  useEffect(() => {
    const fetchBookings = async () => {
      try {
        let customerId = sessionStorage.getItem('customerId');
        
        if (!customerId) {
          const userId = sessionStorage.getItem('userId');
          
          if (!userId) {
            setError('Bạn cần đăng nhập để xem lịch sử đặt phòng.');
            setLoading(false);
            return;
          }

          try {
            const customerResponse = await customerRoute.getCustomerByUserId(userId);
            
            if (customerResponse && customerResponse.customerId) {
              customerId = customerResponse.customerId;
              sessionStorage.setItem('customerId', customerId);
            } else {
              setError('Không tìm thấy thông tin khách hàng.');
              setLoading(false);
              return;
            }
          } catch (customerErr) {
            console.error('Lỗi khi lấy thông tin customer:', customerErr);
            setError('Không thể lấy thông tin khách hàng: ' + (customerErr.response?.data?.message || customerErr.message));
            setLoading(false);
            return;
          }
        }

        const response = await BookingAPI.getBookingsByCustomerId(customerId);
        if (!response || !response.data) {
          setBookings([]);
        } else {
          setBookings(response.data);
        }
      } catch (err) {
        console.error('Lỗi khi lấy lịch sử đặt phòng:', err);
        setError('Không thể tải lịch sử đặt phòng: ' + (err.response?.data?.message || err.message));
        setBookings([]);
      } finally {
        setLoading(false);
      }
    };

    fetchBookings();
  }, []);

  const handleShowFeedbackModal = (booking) => {
    setSelectedBooking(booking);
    setShowFeedbackModal(true);
  };

  const handleCloseFeedbackModal = () => {
    setShowFeedbackModal(false);
    setSelectedBooking(null);
  };

  if (loading) {
    return (
      <>
        <Navbar />
        <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: '80vh', marginTop: '80px' }}>
          <div className="text-center">
            <Spinner animation="grow" variant="primary" />
            <Spinner animation="grow" variant="success" className="mx-2" />
            <Spinner animation="grow" variant="danger" />
            <p className="mt-3 text-primary fw-bold">Đang tải lịch sử đặt phòng...</p>
          </div>
        </Container>
      </>
    );
  }

  if (error) {
    return (
      <>
        <Navbar />
        <Container className="mt-5" style={{ marginTop: '80px' }}>
          <Alert variant="danger" className="text-center shadow">
            <i className="bi bi-exclamation-triangle-fill me-2"></i>
            {error}
          </Alert>
        </Container>
      </>
    );
  }

  const getStatusColor = (status) => {
    switch (status) {
      case 'PAID': return 'success';
      case 'CANCELLED': return 'danger';
      case 'PENDING': return 'warning';
      case 'COMPLETED': return 'info';
      case 'CHECKED_OUT': return 'primary';
      default: return 'secondary';
    }
  };

  const formatDate = (dateString) => {
    const options = { day: '2-digit', month: '2-digit', year: 'numeric' };
    return new Date(dateString).toLocaleDateString('vi-VN', options);
  };

  return (
    <>
      <Navbar />
      <Container className="py-5" style={{ marginTop: '80px' }}>
        <h1 className="text-center mb-4 text-gradient-primary">
          <i className="bi bi-journal-bookmark-fill me-2"></i>
          Lịch Sử Đặt Phòng
        </h1>
        
        <Card className="shadow-lg border-0" style={{ borderRadius: '15px', overflow: 'hidden' }}>
          <Card.Header className="py-3" style={{ 
            background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
            borderBottom: 'none'
          }}>
            <h5 className="mb-0 text-white">
              <i className="bi bi-list-check me-2"></i>
              Danh sách đặt phòng
            </h5>
          </Card.Header>
          <Card.Body className="p-0">
            {(!bookings || bookings.length === 0) ? (
              <Alert variant="info" className="text-center mb-0 rounded-0">
                <i className="bi bi-info-circle-fill me-2"></i>
                Bạn chưa có đặt phòng nào.
              </Alert>
            ) : (
              <div className="table-responsive">
                <Table hover className="mb-0">
                  <thead style={{ background: '#f8f9fa' }}>
                    <tr>
                      <th className="ps-4">Mã đặt phòng</th>
                      <th>Mã phòng</th>
                      <th>Khách sạn</th>
                      <th>Loại phòng</th>
                      <th>Ngày nhận</th>
                      <th>Ngày trả</th>
                      <th>Số đêm</th>
                      <th>Trạng thái</th>
                      <th className="text-end pe-4">Tổng giá</th>
                      <th className="text-center">Hành động</th>
                    </tr>
                  </thead>
                  <tbody>
                    {bookings.map((booking) => (
                      <tr key={booking.bookingId} style={{ borderBottom: '1px solid #eee' }}>
                        <td className="ps-4 fw-bold text-primary">{booking.bookingId}</td>
                        <td className="fw-bold">{booking.roomIds?.join(', ') || '-'}</td>
                        <td>{booking.hotelName}</td>
                        <td>{booking.roomType}</td>
                        <td>
                          <div className="d-flex align-items-center">
                            <i className="bi bi-calendar-check me-2 text-success"></i>
                            {formatDate(booking.checkInDate)}
                          </div>
                        </td>
                        <td>
                          <div className="d-flex align-items-center">
                            <i className="bi bi-calendar-x me-2 text-danger"></i>
                            {formatDate(booking.checkOutDate)}
                          </div>
                        </td>
                        <td className="text-center">{booking.totalStays}</td>
                        <td>
                          <Badge pill bg={getStatusColor(booking.bookingStatus)} className="px-3 py-2">
                            {booking.bookingStatus === 'PAID' ? 'Đã thanh toán' : 
                             booking.bookingStatus === 'CANCELLED' ? 'Đã hủy' :
                             booking.bookingStatus === 'PENDING' ? 'Chờ xử lý' : 
                             booking.bookingStatus === 'COMPLETED' ? 'Hoàn thành' : 
                             booking.bookingStatus === 'CHECKED_OUT' ? 'Đã trả phòng' : booking.bookingStatus}
                          </Badge>
                        </td>
                        <td className="text-end pe-4 fw-bold" style={{ color: '#28a745' }}>
                          {booking.totalPrice.toLocaleString('vi-VN', { 
                            style: 'currency', 
                            currency: 'VND' 
                          })}
                        </td>
                        <td className="text-center">
                          {booking.bookingStatus === 'CHECKED_OUT' && (
                            <Button
                              variant="outline-primary"
                              size="sm"
                              onClick={() => handleShowFeedbackModal(booking)}
                            >
                              <i className="bi bi-chat-square-text me-1"></i>
                              Bình luận
                            </Button>
                          )}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              </div>
            )}
          </Card.Body>
          {bookings && bookings.length > 0 && (
            <Card.Footer className="py-3 text-muted" style={{ borderTop: 'none' }}>
              <div className="d-flex justify-content-between align-items-center">
                <small>
                  <i className="bi bi-info-circle me-1"></i>
                  Tổng cộng: {bookings.length} đặt phòng
                </small>
                <small>
                  <i className="bi bi-clock-history me-1"></i>
                  Cập nhật lúc: {new Date().toLocaleTimeString('vi-VN')}
                </small>
              </div>
            </Card.Footer>
          )}
        </Card>
      </Container>
      {selectedBooking && (
        <FeedbackCustomer
          show={showFeedbackModal}
          onHide={handleCloseFeedbackModal}
          booking={selectedBooking}
        />
      )}
    </>
  );
};

export default BookingHistory;