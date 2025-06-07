import React, { useState } from 'react';
import { Card, Form, Row, Col, Button, Alert } from 'react-bootstrap';
import discountRoute from '../../routes/DiscountRoute';

const DiscountSection = ({ onDiscountApplied, onDiscountRemoved, bookingAmount, hotelId }) => {
  const [discountCode, setDiscountCode] = useState('');
  const [discountApplied, setDiscountApplied] = useState(null);
  const [discountError, setDiscountError] = useState('');
  const [discountDetails, setDiscountDetails] = useState(null);

  // Check discount code
  const checkDiscountCode = async () => {
    setDiscountError('');

    if (!discountCode.trim()) {
      setDiscountError('Vui lòng nhập mã giảm giá');
      return;
    }

    if (!bookingAmount || !hotelId) {
      setDiscountError('Thông tin đặt phòng không đầy đủ để kiểm tra mã giảm giá');
      return;
    }

    try {
      const response = await discountRoute.validateDiscount(discountCode, bookingAmount, hotelId);
      setDiscountDetails({
        percentage: response.percentage,
        description: response.description || 'Mã giảm giá hợp lệ',
        discountAmount: (bookingAmount * response.percentage) / 100,
        originalPrice: bookingAmount,
        finalPrice: bookingAmount - (bookingAmount * response.percentage) / 100
      });
      setDiscountError('');
    } catch (err) {
      setDiscountDetails(null);
      setDiscountError(err.response?.data?.message || 'Mã giảm giá không hợp lệ hoặc đã hết hạn');
    }
  };

  // Apply discount code
  const applyDiscountCode = () => {
    if (!discountDetails) {
      setDiscountError('Vui lòng kiểm tra mã giảm giá trước khi áp dụng');
      return;
    }

    setDiscountApplied(discountDetails);
    onDiscountApplied({
      percentage: discountDetails.percentage,
      description: discountDetails.description,
      originalPrice: discountDetails.originalPrice // Pass original price to parent
    });
    setDiscountError('');
  };

  // Remove discount
  const removeDiscount = () => {
    setDiscountApplied(null);
    setDiscountCode('');
    setDiscountDetails(null);
    setDiscountError('');
    onDiscountRemoved();
  };

  return (
    <Card className="shadow-md rounded-xl border-0 mb-6 max-w-2xl mx-auto">
      <Card.Header className="bg-blue-600 text-white rounded-t-xl py-3">
        <h5 className="m-0 font-semibold text-center">Mã giảm giá</h5>
      </Card.Header>
      <Card.Body className="p-4">
        <Form>
          <Row className="align-items-center">
            <Col xs={12} md={6} className="mb-3 mb-md-0">
              <Form.Control
                type="text"
                placeholder="Nhập mã giảm giá"
                value={discountCode}
                onChange={(e) => setDiscountCode(e.target.value)}
                disabled={discountApplied}
                className="rounded-lg border-gray-300 focus:ring-2 focus:ring-blue-500"
                aria-label="Nhập mã giảm giá"
              />
            </Col>
            <Col xs={12} md={6}>
              <div className="flex gap-3 justify-center">
                <Button
                  variant="secondary"
                  disabled={discountApplied || !discountCode.trim()}
                  onClick={checkDiscountCode}
                  className="rounded-lg px-4 py-2 bg-gray-500 hover:bg-gray-600 text-white"
                >
                  Kiểm tra
                </Button>
                <Button
                  variant="primary"
                  onClick={applyDiscountCode}
                  disabled={discountApplied || !discountDetails}
                  className="rounded-lg px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white"
                >
                  Áp dụng
                </Button>
                {discountApplied && (
                  <Button
                    variant="danger"
                    onClick={removeDiscount}
                    className="rounded-lg px-4 py-2 bg-red-600 hover:bg-red-700 text-white"
                  >
                    Xóa mã
                  </Button>
                )}
              </div>
            </Col>
          </Row>
          {discountError && (
            <Alert variant="danger" className="mt-3 rounded-lg py-2">
              {discountError}
            </Alert>
          )}
          {discountDetails && !discountApplied && (
            <Alert variant="info" className="mt-3 rounded-lg py-2">
              <div>
                <p><strong>Mã giảm giá:</strong> {discountDetails.description}</p>
                <p><strong>Phần trăm giảm:</strong> {discountDetails.percentage}%</p>
                <p><strong>Giá trước giảm:</strong> {discountDetails.originalPrice.toLocaleString()} VND</p>
                <p><strong>Số tiền giảm:</strong> {discountDetails.discountAmount.toLocaleString()} VND</p>
                <p><strong>Giá sau giảm:</strong> {discountDetails.finalPrice.toLocaleString()} VND</p>
              </div>
            </Alert>
          )}
          {discountApplied && (
            <Alert variant="success" className="mt-3 rounded-lg py-2">
              <div>
                <p>✅ Đã áp dụng mã giảm giá: <strong>{discountApplied.description}</strong></p>
                <p><strong>Phần trăm giảm:</strong> {discountApplied.percentage}%</p>
                <p><strong>Giá trước giảm:</strong> {discountApplied.originalPrice.toLocaleString()} VND</p>
                <p><strong>Số tiền giảm:</strong> {discountApplied.discountAmount.toLocaleString()} VND</p>
                <p><strong>Giá sau giảm:</strong> {discountApplied.finalPrice.toLocaleString()} VND</p>
              </div>
            </Alert>
          )}
        </Form>
      </Card.Body>
    </Card>
  );
};

export default DiscountSection;