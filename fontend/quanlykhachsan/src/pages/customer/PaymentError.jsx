import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import Navbar from '../public/Navbar';
import Footer from '../public/Footer';

const PaymentError = () => {
  const location = useLocation();
  const navigate = useNavigate();
  
  const { error, responseCode } = location.state || {};

  const getErrorIcon = (code) => {
    switch (code) {
      case '24':
        return 'bi-x-circle';
      case '51':
        return 'bi-credit-card-2-front';
      case '11':
        return 'bi-clock';
      default:
        return 'bi-exclamation-triangle';
    }
  };

  const getErrorColor = (code) => {
    switch (code) {
      case '24':
        return 'text-warning';
      case '51':
        return 'text-danger';
      case '11':
        return 'text-info';
      default:
        return 'text-danger';
    }
  };

  return (
    <div className="d-flex flex-column min-vh-100">
      <Navbar />
      <div className="container flex-grow-1 d-flex align-items-center justify-content-center">
        <div className="card shadow-lg p-5 text-center" style={{ maxWidth: '500px' }}>
          <div className={`display-1 mb-4 ${getErrorColor(responseCode)}`}>
            <i className={`bi ${getErrorIcon(responseCode)}`}></i>
          </div>
          
          <h2 className="text-danger mb-3">Thanh toán không thành công</h2>
          
          <div className="alert alert-danger mb-4">
            <p className="mb-0">
              {error || 'Đã có lỗi xảy ra trong quá trình thanh toán'}
            </p>
            {responseCode && (
              <small className="text-muted">Mã lỗi: {responseCode}</small>
            )}
          </div>

          <div className="mb-4">
            <h5>Các bước tiếp theo:</h5>
            <ul className="list-unstyled text-start">
              <li className="mb-2">
                <i className="bi bi-check-circle text-success me-2"></i>
                Kiểm tra lại thông tin thẻ/tài khoản
              </li>
              <li className="mb-2">
                <i className="bi bi-check-circle text-success me-2"></i>
                Đảm bảo tài khoản có đủ số dư
              </li>
              <li className="mb-2">
                <i className="bi bi-check-circle text-success me-2"></i>
                Thử lại với phương thức thanh toán khác
              </li>
              <li className="mb-2">
                <i className="bi bi-check-circle text-success me-2"></i>
                Liên hệ ngân hàng nếu vấn đề tiếp tục
              </li>
            </ul>
          </div>

          <div className="d-flex gap-3 justify-content-center">
            <button
              className="btn btn-primary btn-lg"
              onClick={() => navigate('/rooms')}
            >
              <i className="bi bi-arrow-clockwise me-2"></i>
              Thử lại
            </button>
            <button
              className="btn btn-outline-secondary btn-lg"
              onClick={() => navigate('/')}
            >
              <i className="bi bi-house me-2"></i>
              Trang chủ
            </button>
          </div>

          <div className="mt-4 pt-3 border-top">
            <p className="text-muted mb-0">
              Cần hỗ trợ? 
              <a href="tel:1900xxxx" className="text-decoration-none ms-2">
                <i className="bi bi-telephone me-1"></i>1900-XXXX
              </a>
            </p>
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default PaymentError;