import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Card, Container, Form, Row, Col, Button, Alert } from 'react-bootstrap';
import Navbar from '../public/Navbar';
import Footer from '../public/Footer';
import RoomInfoSection from './RoomInfoSection';
import DiscountSection from './DiscountSection';
import paymentRoute from '../../routes/PaymentRoute';
import BookingAPI from '../../routes/BookingRoute';

const BookingCustomer = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const room = location.state?.room;

  // State management
  const [checkInDate, setCheckInDate] = useState('');
  const [checkOutDate, setCheckOutDate] = useState('');
  const [numberOfGuests, setNumberOfGuests] = useState(1);
  const [specialRequests, setSpecialRequests] = useState('');
  const [discountApplied, setDiscountApplied] = useState(null);
  const [originalPrice, setOriginalPrice] = useState(0); // Store original price
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [isLoggedIn, setIsLoggedIn] = useState(true);

  // Check if room data exists
  useEffect(() => {
    if (!room) {
      setError('Không có thông tin phòng để hiển thị.');
    }
  }, [room]);

  // Check authentication userId
  useEffect(() => {
    const userId = sessionStorage.getItem('userId');
    if (!userId) {
      setIsLoggedIn(false);
    }
  }, []);

  // Handle OK button click to redirect to login
  const handleLoginRedirect = () => {
    navigate('/login');
  };

  // Get today's date in yyyy-mm-dd format
  const getTodayDate = () => {
    const today = new Date();
    return today.toISOString().split('T')[0];
  };

  const today = getTodayDate();

  // Calculate number of nights
  const calculateNights = () => {
    if (!checkInDate || !checkOutDate) return 0;
    
    const checkIn = new Date(checkInDate);
    const checkOut = new Date(checkOutDate);
    const timeDiff = checkOut.getTime() - checkIn.getTime();
    const nights = Math.ceil(timeDiff / (1000 * 3600 * 24));
    
    return nights > 0 ? nights : 0;
  };

  // Calculate total price
  const calculateTotal = () => {
    const nights = calculateNights();
    if (nights === 0) return 0;
    
    const price = room?.price || 0;
    const subtotal = nights * price;
    
    if (discountApplied) {
      const discountAmount = (subtotal * discountApplied.percentage) / 100;
      return subtotal - discountAmount;
    }
    
    return subtotal;
  };

  // Handle discount applied
  const handleDiscountApplied = (discount) => {
    setDiscountApplied(discount);
    setOriginalPrice(discount.originalPrice); // Store original price
  };

  // Handle discount removed
  const handleDiscountRemoved = () => {
    setDiscountApplied(null);
    setOriginalPrice(0);
  };

  // Handle payment
 // Handle payment
const handlePayment = async () => {
  if (!checkInDate || !checkOutDate) {
    setError('Vui lòng chọn ngày check-in và check-out');
    return;
  }

  if (numberOfGuests < 1) {
    setError('Số lượng khách phải ít nhất là 1');
    return;
  }

  const userId = sessionStorage.getItem('userId');
  if (!userId) {
    setIsLoggedIn(false);
    return;
  }

  setLoading(true);
  setError('');

  try {
    // Đảm bảo định dạng ngày tháng yyyy-MM-dd
    const formattedCheckInDate = checkInDate;
    const formattedCheckOutDate = checkOutDate;

    // Kiểm tra tính khả dụng của phòng
    console.log('Checking room availability:', { roomId: room.roomId, checkInDate: formattedCheckInDate, checkOutDate: formattedCheckOutDate });
    const response = await BookingAPI.checkRoomAvailability(
      room.roomId,
      formattedCheckInDate,
      formattedCheckOutDate
    );
    console.log('Room availability response:', response);

    // Kiểm tra response từ API
    if (!response.data) {
      setError(response.message || 'Phòng đã được đặt cho khoảng thời gian này. Vui lòng chọn ngày khác.');
      setLoading(false);
      return;
    }

    const total = calculateTotal(); // Use the discounted total
    const content = `Booking room ${room.roomId || 'N/A'} from ${checkInDate} to ${checkOutDate}`;

    console.log('Sending payment request:', {
      amount: total,
      content,
      customerId: sessionStorage.getItem('customerId'),
      roomIds: [room.roomId],
      checkInDate: formattedCheckInDate,
      checkOutDate: formattedCheckOutDate,
      numberOfGuests,
      specialRequests: specialRequests || null
    });

    const paymentResponse = await paymentRoute.createPayment(
      total, // Pass the discounted total
      content,
      sessionStorage.getItem('customerId'),
      room.roomId,
      formattedCheckInDate,
      formattedCheckOutDate,
      numberOfGuests,
      specialRequests || null
    );

    console.log('Payment API response:', paymentResponse);

    // Check for payment URL in response
    const paymentUrl = paymentResponse?.URL || paymentResponse?.url || paymentResponse?.data?.URL || paymentResponse?.data?.url;
    if (paymentResponse?.status === 'Ok' && paymentUrl && typeof paymentUrl === 'string') {
      console.log('Redirecting to payment URL:', paymentUrl);
      window.location.href = paymentUrl;
    } else {
      setError('Không nhận được URL thanh toán hợp lệ từ server. Vui lòng thử lại.');
      console.log('Invalid payment response structure or status:', paymentResponse);
    }
  } catch (err) {
    console.error('Payment error:', err);
    console.error('Error details:', err.response?.data, err.response?.status);
    const errorMessage = err.response?.data?.message || err.message || 'Phòng đã được đặt hoặc có lỗi xảy ra. Vui lòng kiểm tra lại hoặc thử ngày khác.';
    setError(errorMessage);
    if (err.response?.status === 401) {
      setIsLoggedIn(false);
    }
  } finally {
    setLoading(false);
  }
  };

  // Render login required screen
  if (!isLoggedIn) {
    return (
      <div className="flex flex-col min-h-screen bg-gray-100">
        <Navbar />
        <div className="flex flex-col flex-grow items-center justify-center">
          <div className="bg-white shadow-lg rounded-xl p-8 text-center border-0 max-w-md w-full">
            <h4 className="text-2xl font-bold text-gray-800 mb-4">Chưa đăng nhập</h4>
            <p className="text-gray-600 mb-6">Bạn cần đăng nhập để thực hiện thanh toán.</p>
            <Button
              className="w-3/4 rounded-lg py-3 px-6 bg-blue-600 hover:bg-blue-700 text-white font-medium"
              onClick={handleLoginRedirect}
              aria-label="Chuyển đến trang đăng nhập"
            >
              Chuyển đến đăng nhập
            </Button>
          </div>
        </div>
        <Footer />
      </div>
    );
  }

  // Render no room data screen
  if (!room) {
    return (
      <div className="flex flex-col min-h-screen bg-gray-100">
        <Navbar />
        <div className="flex flex-col flex-grow items-center justify-center">
          <div className="bg-white shadow-lg rounded-xl p-8 text-center border-0 max-w-md w-full">
            <Alert variant="warning" className="rounded-lg shadow-sm">
              Không có thông tin phòng để hiển thị.
            </Alert>
          </div>
        </div>
        <Footer />
      </div>
    );
  }

  const nights = calculateNights();
  const price = room?.price || 0;
  const subtotal = nights * price;
  const total = calculateTotal();
  const discountAmount = discountApplied ? (subtotal * discountApplied.percentage) / 100 : 0;

  return (
    <div className="flex flex-col min-h-screen bg-gray-100">
      <Navbar />
      <div className="flex flex-col flex-grow items-center justify-center py-12">
        <Container className="max-w-5xl">
          <h3 className="text-3xl font-bold text-gray-800 mb-8 text-center">Đặt Phòng</h3>
          
          {/* Room and Hotel Info */}
          <RoomInfoSection room={room} />

          {/* Check-in/Check-out Section */}
          <Card className="shadow-lg rounded-xl border-0 mb-8 max-w-3xl mx-auto">
            <Card.Header className="bg-blue-600 text-white rounded-t-xl py-4">
              <h5 className="m-0 font-semibold text-lg text-center">Thông tin đặt phòng</h5>
            </Card.Header>
            <Card.Body className="p-6">
              <Form className="space-y-6">
                <Form.Group as={Row} className="justify-content-center">
                  <Col xs={12} md={10}>
                    <Form.Label className="font-medium text-gray-700 text-lg">Check-in:</Form.Label>
                    <Form.Control
                      type="date"
                      min={today}
                      value={checkInDate}
                      onChange={(e) => {
                        setCheckInDate(e.target.value);
                        if (checkOutDate && e.target.value >= checkOutDate) {
                          setCheckOutDate('');
                        }
                      }}
                      className="rounded-lg border-gray-300 focus:ring-2 focus:ring-blue-500 text-lg"
                      aria-label="Chọn ngày check-in"
                    />
                  </Col>
                </Form.Group>
                <Form.Group as={Row} className="justify-content-center">
                  <Col xs={12} md={10}>
                    <Form.Label className="font-medium text-gray-700 text-lg">Check-out:</Form.Label>
                    <Form.Control
                      type="date"
                      min={checkInDate ? new Date(new Date(checkInDate).getTime() + 86400000).toISOString().split('T')[0] : today}
                      value={checkOutDate}
                      onChange={(e) => setCheckOutDate(e.target.value)}
                      className="rounded-lg border-gray-300 focus:ring-2 focus:ring-blue-500 text-lg"
                      aria-label="Chọn ngày check-out"
                    />
                  </Col>
                </Form.Group>
                <Form.Group as={Row} className="justify-content-center">
                  <Col xs={12} md={10}>
                    <Form.Label className="font-medium text-gray-700 text-lg">Số lượng khách:</Form.Label>
                    <Form.Control
                      type="number"
                      min="1"
                      max={room.maxGuests || 10}
                      value={numberOfGuests}
                      onChange={(e) => setNumberOfGuests(parseInt(e.target.value) || 1)}
                      className="rounded-lg border-gray-300 focus:ring-2 focus:ring-blue-500 text-lg"
                      aria-label="Số lượng khách"
                    />
                  </Col>
                </Form.Group>
                <Form.Group as={Row} className="justify-content-center">
                  <Col xs={12} md={10}>
                    <Form.Label className="font-medium text-gray-700 text-lg">Yêu cầu đặc biệt (tùy chọn):</Form.Label>
                    <Form.Control
                      as="textarea"
                      rows={4}
                      placeholder="Nhập yêu cầu đặc biệt nếu có..."
                      value={specialRequests}
                      onChange={(e) => setSpecialRequests(e.target.value)}
                      className="rounded-lg border-gray-300 focus:ring-2 focus:ring-blue-500 text-lg"
                      aria-label="Yêu cầu đặc biệt"
                    />
                  </Col>
                </Form.Group>
                {nights > 0 && (
                  <Alert variant="info" className="rounded-lg text-center py-3 text-lg">
                    <strong>Số đêm:</strong> {nights} đêm | <strong>Số khách:</strong> {numberOfGuests} người
                  </Alert>
                )}
              </Form>
            </Card.Body>
          </Card>

          {/* Discount Code Section */}
          <DiscountSection 
            onDiscountApplied={handleDiscountApplied}
            onDiscountRemoved={handleDiscountRemoved}
            bookingAmount={subtotal}
            hotelId={room.hotelId}
          />

          {/* Payment Section */}
          <Card className="shadow-lg rounded-xl border-0 mb-8 max-w-3xl mx-auto">
            <Card.Header className="bg-blue-600 text-white rounded-t-xl py-4">
              <h5 className="m-0 font-semibold text-lg text-center">Phương thức thanh toán</h5>
            </Card.Header>
            <Card.Body className="p-6">
              <Form>
                <Form.Check
                  type="radio"
                  label="VNPay"
                  name="paymentMethod"
                  id="vnpay"
                  defaultChecked
                  className="mb-4 text-gray-700 text-lg"
                  aria-label="Chọn thanh toán bằng VNPay"
                />
              </Form>
              <hr className="my-4" />
              {nights > 0 ? (
                <div>
                  <div className="flex justify-between text-gray-700 mb-3 text-lg">
                    <span>Giá phòng ({nights} đêm):</span>
                    <span>{subtotal.toLocaleString()} VND</span>
                  </div>
                  {discountApplied && (
                    <div className="flex justify-between text-green-600 mb-3 text-lg">
                      <span>Giảm giá ({discountApplied.percentage}%):</span>
                      <span>-{discountAmount.toLocaleString()} VND</span>
                    </div>
                  )}
                  <hr className="my-4" />
                  <div className="flex justify-between text-gray-800 font-semibold text-xl">
                    <h5>Tổng tiền:</h5>
                    <h5 className="text-blue-600">{total.toLocaleString()} VND</h5>
                  </div>
                  {error && (
                    <Alert variant="danger" className="mt-4 rounded-lg py-3 text-lg">
                      {error}
                    </Alert>
                  )}
                  <Button
                    className="mt-4 w-full rounded-lg py-3 bg-green-600 hover:bg-green-700 text-white font-medium text-lg"
                    disabled={!checkInDate || !checkOutDate || loading}
                    onClick={handlePayment}
                    aria-label={`Thanh toán ${total.toLocaleString()} VND`}
                  >
                    {loading ? 'Đang xử lý...' : `Thanh toán ${total.toLocaleString()} VND`}
                  </Button>
                </div>
              ) : (
                <div className="text-center text-gray-500">
                  <p className="mb-4 text-lg">Vui lòng chọn ngày check-in và check-out để xem tổng tiền</p>
                  <Button className="rounded-lg px-6 py-3 bg-gray-400 text-white text-lg" disabled>
                    Chọn ngày để tiếp tục
                  </Button>
                </div>
              )}
            </Card.Body>
          </Card>
        </Container>
      </div>
      <Footer />
    </div>
  );
};

export default BookingCustomer;