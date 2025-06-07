import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Container, Card, Spinner, Alert, Button } from 'react-bootstrap';
import Navbar from '../public/Navbar';
import Footer from '../public/Footer';
import paymentRoute from '../../routes/PaymentRoute';

const PaymentCallback = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');
  const [bookingInfo, setBookingInfo] = useState(null);

  useEffect(() => {
    let isProcessing = false; // Flag to prevent double processing
    
    const processPaymentCallback = async () => {
      // Prevent multiple calls
      if (isProcessing) {
        console.log('Payment callback already processing, skipping...');
        return;
      }
      
      isProcessing = true;
      
      try {
        // Parse URL parameters từ VNPay callback
        const urlParams = new URLSearchParams(location.search);
        const paymentData = {
          vnp_Amount: urlParams.get('vnp_Amount'),
          vnp_TxnRef: urlParams.get('vnp_TxnRef'),
          vnp_ResponseCode: urlParams.get('vnp_ResponseCode'),
          vnp_BankCode: urlParams.get('vnp_BankCode'),
          vnp_PayDate: urlParams.get('vnp_PayDate'),
          vnp_TransactionNo: urlParams.get('vnp_TransactionNo'),
          vnp_SecureHash: urlParams.get('vnp_SecureHash')
        };

        console.log('Payment callback data:', paymentData);

        // Kiểm tra các parameter bắt buộc
        if (!paymentData.vnp_TxnRef || !paymentData.vnp_Amount) {
          setError('Thiếu thông tin thanh toán từ VNPay');
          setLoading(false);
          return;
        }

        // Kiểm tra response code
        if (paymentData.vnp_ResponseCode !== '00') {
          setError('Thanh toán không thành công. Mã lỗi: ' + paymentData.vnp_ResponseCode);
          setLoading(false);
          return;
        }

        // Gọi API callback để tạo booking và payment
        const response = await paymentRoute.handlePaymentCallback(paymentData);
        
        console.log('Callback response:', response);

        if (response.status === 'success') {
          setSuccess(true);
          setBookingInfo({
            bookingId: response.bookingId,
            paymentId: response.paymentId,
            amount: paymentData.vnp_Amount,
            transactionRef: paymentData.vnp_TxnRef
          });
        } else if (response.status === 'already_processed' || response.status === 'already_exists') {
          // Handle case where payment was already processed
          setSuccess(true);
          setBookingInfo({
            amount: paymentData.vnp_Amount,
            transactionRef: paymentData.vnp_TxnRef,
            message: 'Giao dịch đã được xử lý trước đó'
          });
        } else {
          setError('Có lỗi xảy ra khi xử lý thanh toán: ' + response.message);
        }

      } catch (err) {
        console.error('Error processing payment callback:', err);
        
        // Handle specific error cases
        if (err.response?.status === 400 && err.response?.data?.message?.includes('already processed')) {
          setSuccess(true);
          setBookingInfo({
            message: 'Giao dịch đã được xử lý thành công trước đó'
          });
        } else {
          setError('Có lỗi xảy ra khi xử lý thanh toán: ' + (err.response?.data?.message || err.message));
        }
      } finally {
        setLoading(false);
        isProcessing = false;
      }
    };

    processPaymentCallback();
  }, [location.search]);

  const handleGoToBookings = () => {
    navigate('/bookings'); // Chuyển đến trang danh sách booking
  };

  const handleGoHome = () => {
    navigate('/');
  };

  return (
    <div className="flex flex-col min-vh-100 bg-gray-50">
      <Navbar />
      <div className="flex flex-col flex-grow-1 items-center justify-center">
        <Container className="pt-10 pb-8 max-w-2xl">
          <Card className="shadow-lg rounded-xl border-0">
            <Card.Body className="p-5 text-center">
              {loading ? (
                <div>
                  <Spinner animation="border" variant="primary" className="mb-3" />
                  <h4 className="text-gray-800 mb-3">Đang xử lý thanh toán...</h4>
                  <p className="text-gray-600">Vui lòng đợi trong giây lát</p>
                </div>
              ) : success ? (
                <div>
                  <div className="text-success mb-4">
                    <i className="fas fa-check-circle" style={{ fontSize: '4rem' }}></i>
                  </div>
                  <h4 className="text-success mb-3">Thanh toán thành công!</h4>
                  <Alert variant="success" className="rounded-lg">
                    <strong>Thông tin đặt phòng:</strong>
                    <ul className="list-unstyled mt-2 mb-0">
                      <li><strong>Mã đặt phòng:</strong> {bookingInfo?.bookingId}</li>
                      <li><strong>Mã thanh toán:</strong> {bookingInfo?.paymentId}</li>
                      <li><strong>Số tiền:</strong> {parseInt(bookingInfo?.amount / 100).toLocaleString()} VND</li>
                      <li><strong>Mã giao dịch:</strong> {bookingInfo?.transactionRef}</li>
                    </ul>
                  </Alert>
                  <div className="mt-4">
                    <Button 
                      variant="primary" 
                      className="me-3 rounded-lg"
                      onClick={handleGoToBookings}
                    >
                      Xem đặt phòng
                    </Button>
                    <Button 
                      variant="secondary" 
                      className="rounded-lg"
                      onClick={handleGoHome}
                    >
                      Về trang chủ
                    </Button>
                  </div>
                </div>
              ) : (
                <div>
                  <div className="text-danger mb-4">
                    <i className="fas fa-times-circle" style={{ fontSize: '4rem' }}></i>
                  </div>
                  <h4 className="text-danger mb-3">Thanh toán thất bại!</h4>
                  <Alert variant="danger" className="rounded-lg">
                    {error}
                  </Alert>
                  <div className="mt-4">
                    <Button 
                      variant="primary" 
                      className="me-3 rounded-lg"
                      onClick={() => navigate(-1)}
                    >
                      Thử lại
                    </Button>
                    <Button 
                      variant="secondary" 
                      className="rounded-lg"
                      onClick={handleGoHome}
                    >
                      Về trang chủ
                    </Button>
                  </div>
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

export default PaymentCallback;