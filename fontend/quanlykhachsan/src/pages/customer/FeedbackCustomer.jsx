import React, { useState, useEffect } from 'react';
import { Modal, Button, Form, Alert, Spinner } from 'react-bootstrap';
import feedbackRoute from '../../routes/FeedbackRoute';
import roomRoute from '../../routes/RoomRoute';
import { BookingAPI } from '../../routes/BookingRoute';

const FeedbackCustomer = ({ show, onHide, booking }) => {
  const [rating, setRating] = useState(0);
  const [content,  setContent] = useState('');
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [hotelId, setHotelId] = useState(null);
  const [loadingHotel, setLoadingHotel] = useState(true);

  useEffect(() => {
    const fetchHotelId = async () => {
      try {
        if (!booking || !booking.bookingId) {
          setError('Thông tin đặt phòng không hợp lệ hoặc thiếu bookingId.');
          setLoadingHotel(false);
          return;
        }

        // Gọi API để lấy thông tin booking từ bookingId
        const bookingData = await BookingAPI.getBookingById(booking.bookingId);
        if (!bookingData || !bookingData.data || !bookingData.data.roomIds || bookingData.data.roomIds.length === 0) {
          setError('Không tìm thấy thông tin phòng từ đặt phòng này.');
          setLoadingHotel(false);
          return;
        }

        const roomId = bookingData.data.roomIds[0]; // Lấy roomId đầu tiên từ danh sách
        // Gọi API để lấy thông tin phòng từ roomId
        const roomData = await roomRoute.getRoomById(roomId);
        if (!roomData || !roomData.hotelId) {
          setError('Không tìm thấy thông tin khách sạn từ phòng này.');
          setLoadingHotel(false);
          return;
        }

        setHotelId(roomData.hotelId);
      } catch (err) {
        const errorMessage = err.response?.data?.message || err.message;
        setError(`Lỗi khi lấy thông tin khách sạn: ${errorMessage}`);
        console.error('Lỗi trong fetchHotelId:', errorMessage, err);
      } finally {
        setLoadingHotel(false);
      }
    };

    if (show) {
      fetchHotelId();
    }
  }, [show, booking]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccess(false);
    setSubmitting(true);

    const customerId = sessionStorage.getItem('customerId');
    if (!customerId) {
      setError('Vui lòng đăng nhập để gửi đánh giá.');
      setSubmitting(false);
      return;
    }

    if (!hotelId) {
      setError('Không thể xác định khách sạn để gửi đánh giá.');
      setSubmitting(false);
      return;
    }

    if (rating < 1 || rating > 5) {
      setError('Điểm đánh giá phải từ 1 đến 5 sao.');
      setSubmitting(false);
      return;
    }

    if (!content.trim()) {
      setError('Nội dung đánh giá không được để trống.');
      setSubmitting(false);
      return;
    }

    // Sửa lại cấu trúc dữ liệu gửi đi - sử dụng ID trực tiếp thay vì object nested
    const feedbackData = {
      hotelId: hotelId,           // Thay đổi từ hotel: { hotelId }
      customerId: customerId,     // Thay đổi từ customer: { customerId }
      bookingId: booking.bookingId, // Thay đổi từ booking: { bookingId }
      rating,
      content
      // Bỏ createdAt và updatedAt vì backend tự động tạo
    };

    console.log('Tham số gửi đi trong POST request:', feedbackData);

    try {
      const response = await feedbackRoute.addFeedback(feedbackData);
      console.log('Phản hồi từ API:', response);
      setSuccess(true);
      setRating(0);
      setContent('');
      setTimeout(() => {
        onHide();
        setSuccess(false);
      }, 2000);
   } catch (err) {
  const errorMessage = err.response?.data?.message || err.message;

  if (
    errorMessage.includes('Duplicate entry') &&
    errorMessage.includes('unique_booking_id')
  ) {
    setError('Bạn đã đánh giá đơn đặt phòng này rồi.');
  } else {
    setError(`Không thể gửi đánh giá: ${errorMessage}`);
  }

  console.error('Lỗi khi gửi đánh giá:', errorMessage, err);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>
          <i className="bi bi-star-fill me-2 text-warning"></i>
          Đánh giá đặt phòng #{booking.bookingId?.substring(0, 8) || 'N/A'}
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {loadingHotel ? (
          <div className="text-center">
            <Spinner animation="border" size="sm" className="me-2" />
            Đang tải thông tin khách sạn...
          </div>
        ) : (
          <>
            {success && (
              <Alert variant="success">
                <i className="bi bi-check-circle-fill me-2"></i>
                Đánh giá của bạn đã được gửi thành công!
              </Alert>
            )}
            {error && (
              <Alert variant="danger">
                <i className="bi bi-exclamation-triangle-fill me-2"></i>
                {error}
              </Alert>
            )}
            <Form onSubmit={handleSubmit}>
              <Form.Group className="mb-3">
                <Form.Label>
                  <i className="bi bi-star me-2"></i>
                  Điểm đánh giá
                </Form.Label>
                <Form.Control
                  as="select"
                  value={rating}
                  onChange={(e) => setRating(Number(e.target.value))}
                  required
                >
                  <option value="0" disabled>Chọn điểm (1-5)</option>
                  {[1, 2, 3, 4, 5].map((num) => (
                    <option key={num} value={num}>{num} sao</option>
                  ))}
                </Form.Control>
              </Form.Group>
              <Form.Group className="mb-3">
                <Form.Label>
                  <i className="bi bi-chat-square-text me-2"></i>
                  Nội dung đánh giá
                </Form.Label>
                <Form.Control
                  as="textarea"
                  rows={4}
                  value={content}
                  onChange={(e) => setContent(e.target.value)}
                  placeholder="Nhập nội dung đánh giá của bạn..."
                  required
                />
              </Form.Group>
              <Button
                variant="primary"
                type="submit"
                disabled={submitting || rating === 0 || !content.trim() || loadingHotel}
                className="w-100"
              >
                {submitting ? (
                  <>
                    <Spinner as="span" animation="border" size="sm" className="me-2" />
                    Đang gửi...
                  </>
                ) : (
                  <>
                    <i className="bi bi-send me-2"></i>
                    Gửi đánh giá
                  </>
                )}
              </Button>
            </Form>
          </>
        )}
      </Modal.Body>
    </Modal>
    
  );
};

export default FeedbackCustomer;