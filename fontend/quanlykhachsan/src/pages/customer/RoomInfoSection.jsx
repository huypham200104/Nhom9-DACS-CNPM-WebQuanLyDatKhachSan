import React, { useState, useEffect } from 'react';
import { Card, Row, Col } from 'react-bootstrap';
import roomTypeRoute from '../../routes/RoomTypeRoute';

const RoomInfoSection = ({ room }) => {
  const [roomTypeName, setRoomTypeName] = useState('N/A');
  const [loadingRoomType, setLoadingRoomType] = useState(false);

  // Fetch room type name based on roomTypeId
  useEffect(() => {
    const fetchRoomTypeName = async () => {
      if (!room?.roomTypeId) {
        console.log('No roomTypeId provided, room object:', room);
        setRoomTypeName('N/A');
        return;
      }

      setLoadingRoomType(true);
      try {
        const roomType = await roomTypeRoute.getRoomTypeById(room.roomTypeId);
        console.log('Room type response:', roomType);
        const name = roomType?.roomTypeName || roomType?.data?.roomTypeName || 'N/A';
        setRoomTypeName(name);
      } catch (err) {
        console.error('Lỗi khi lấy tên loại phòng:', err);
        setRoomTypeName('N/A');
      } finally {
        setLoadingRoomType(false);
      }
    };
    fetchRoomTypeName();
  }, [room]);

  if (!room) {
    return null;
  }

  const hotel = room.hotel || {};
  const price = room.price || 0;

  return (
    <Row className="mb-6">
      <Col md={6} className="mb-4">
        <Card className="shadow-md rounded-xl border-0">
          <Card.Header className="bg-blue-600 text-white rounded-t-xl py-3">
            <h5 className="m-0 font-semibold">Thông tin phòng</h5>
          </Card.Header>
          <Card.Body className="p-4">
            <p className="mb-2"><strong>Mã phòng:</strong> {room.roomId || 'N/A'}</p>
            <p className="mb-2"><strong>Mô tả:</strong> {room.description || 'N/A'}</p>
            <p className="mb-2">
              <strong>Loại phòng:</strong>{' '}
              {loadingRoomType ? 'Đang tải...' : roomTypeName}
            </p>
            <p className="mb-2"><strong>Kích thước:</strong> {room.roomSize ? `${room.roomSize} m²` : 'N/A'}</p>
            <p className="mb-2"><strong>Sức chứa tối đa:</strong> {room.maxGuests ? `${room.maxGuests} khách` : 'N/A'}</p>
            <p className="mb-0"><strong>Giá mỗi đêm:</strong> {price.toLocaleString()} VND</p>
          </Card.Body>
        </Card>
      </Col>
      <Col md={6} className="mb-4">
        <Card className="shadow-md rounded-xl border-0">
          <Card.Header className="bg-blue-600 text-white rounded-t-xl py-3">
            <h5 className="m-0 font-semibold">Thông tin khách sạn</h5>
          </Card.Header>
          <Card.Body className="p-4">
            <p className="mb-2"><strong>Tên khách sạn:</strong> {hotel.hotelName || 'N/A'}</p>
            <p className="mb-2"><strong>Thành phố:</strong> {hotel.city || 'N/A'}</p>
            <p className="mb-2"><strong>Quận/Huyện:</strong> {hotel.district || 'N/A'}</p>
            <p className="mb-2"><strong>Phường:</strong> {hotel.ward || 'N/A'}</p>
            <p className="mb-2"><strong>Đường:</strong> {hotel.street || 'N/A'}</p>
            <p className="mb-2"><strong>Số nhà:</strong> {hotel.houseNumber || 'N/A'}</p>
            <p className="mb-0"><strong>Sao:</strong> {hotel.starRating || 0} ★</p>
          </Card.Body>
        </Card>
      </Col>
    </Row>
  );
};

export default RoomInfoSection;