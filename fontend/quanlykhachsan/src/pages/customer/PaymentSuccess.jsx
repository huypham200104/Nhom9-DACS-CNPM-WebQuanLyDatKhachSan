import React from 'react';
import { useNavigate } from 'react-router-dom';

const PaymentSuccess = ({ paymentData }) => {
  const navigate = useNavigate();

  // Định dạng ngày
  const formatDate = (dateStr) => {
    if (!dateStr) return '';
    const year = dateStr.substring(0, 4);
    const month = dateStr.substring(4, 6);
    const day = dateStr.substring(6, 8);
    const hour = dateStr.substring(8, 10);
    const minute = dateStr.substring(10, 12);
    const second = dateStr.substring(12, 14);
    return `${day}/${month}/${year} ${hour}:${minute}:${second}`;
  };

  // Định dạng số tiền
  const formatAmount = (amount) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND',
    }).format(amount);
  };

  return (
    <div className="card shadow p-4 mx-auto" style={{ maxWidth: '600px' }}>
      <div className="card-body text-center">
        <h2 className="card-title text-success mb-4">
          <i className="bi bi-check-circle-fill me-2"></i>Thanh toán thành công!
        </h2>
        <p className="card-text">{paymentData.message}</p>
        <ul className="list-group list-group-flush mb-4">
          <li className="list-group-item">
            <strong>Mã giao dịch:</strong> {paymentData.transactionId}
          </li>
          <li className="list-group-item">
            <strong>Thời gian giao dịch:</strong> {formatDate(paymentData.transactionDate)}
          </li>
          <li className="list-group-item">
            <strong>Số tiền:</strong> {formatAmount(paymentData.amount)}
          </li>
        </ul>
        <button
          className="btn btn-primary"
          onClick={() => navigate('/')}
        >
          <i className="bi bi-house me-2"></i>Quay lại trang chủ
        </button>
      </div>
    </div>
  );
};

export default PaymentSuccess;