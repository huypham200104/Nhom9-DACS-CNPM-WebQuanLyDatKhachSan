import React, { useState, useEffect } from 'react';
import RoomPageLogic from './RoomPageLogic';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Button, Table, Modal, Form, Alert, Spinner, Image } from 'react-bootstrap';

const RoomPageUI = () => {
  const {
    rooms,
    roomTypes,
    loading,
    error,
    currentRoom,
    isAddModalOpen,
    isEditModalOpen,
    addRoom,
    updateRoom,
    deleteRoom,
    openAddModal,
    openEditModal,
    closeModals,
    getRoomTypeName, // FIXED: Use the helper function from logic
  } = RoomPageLogic();

  // Retrieve hotelId from sessionStorage, fallback to empty string if not found
  const initialHotelId = sessionStorage.getItem('hotelId') || '';

  const [formData, setFormData] = useState({
    description: '',
    maxGuests: '',
    price: '',
    roomSize: '',
    roomStatus: 'AVAILABLE',
    hotelId: initialHotelId,
    roomTypeName: '',
    bedType: 'KING',
  });
  const [files, setFiles] = useState([]);
  const [formError, setFormError] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (currentRoom && isEditModalOpen) {
      setFormData({
        description: currentRoom.description || '',
        maxGuests: currentRoom.maxGuests?.toString() || '',
        price: currentRoom.price?.toString() || '',
        roomSize: currentRoom.roomSize?.toString() || '',
        roomStatus: currentRoom.roomStatus || 'AVAILABLE',
        hotelId: currentRoom.hotelId || sessionStorage.getItem('hotelId') || '',
        roomTypeName: currentRoom.roomTypeName || '',
        bedType: currentRoom.bedType || 'KING',
      });
      setFiles([]);
      setFormError(null);
    }
  }, [currentRoom, isEditModalOpen]);

  const validateForm = () => {
    if (!formData.hotelId) return 'Hotel ID is required. Please ensure you are logged in with a valid hotel session.';
    if (!formData.roomTypeName) return 'Please select a room type';
    if (!formData.maxGuests || isNaN(formData.maxGuests) || parseInt(formData.maxGuests) <= 0)
      return 'Max guests must be a positive number';
    if (!formData.price || isNaN(formData.price) || parseInt(formData.price) <= 0)
      return 'Price must be a positive number';
    if (!formData.roomSize || isNaN(formData.roomSize) || parseInt(formData.roomSize) <= 0)
      return 'Room size must be a positive number';
    if (files.length > 0) {
      for (const file of files) {
        if (!file.type.startsWith('image/')) return 'Only image files are allowed';
        if (file.size > 5 * 1024 * 1024) return 'Each image must be under 5MB';
      }
    }
    return null;
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
    setFormError(null); // Clear error on input change
  };

  const handleFileChange = (e) => {
    setFiles(Array.from(e.target.files));
    setFormError(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const validationError = validateForm();
    if (validationError) {
      setFormError(validationError);
      return;
    }

    setSubmitting(true);
    const data = { ...formData };
    try {
      let success;
      if (isAddModalOpen) {
        success = await addRoom(data, files);
      } else if (isEditModalOpen && currentRoom) {
        success = await updateRoom(currentRoom.roomId, data, files);
      }
      if (success) {
        handleCloseModals();
      }
    } finally {
      setSubmitting(false);
    }
  };

  const handleCloseModals = () => {
    closeModals();
    setFormData({
      description: '',
      maxGuests: '',
      price: '',
      roomSize: '',
      roomStatus: 'AVAILABLE',
      hotelId: sessionStorage.getItem('hotelId') || '',
      roomTypeName: '',
      bedType: 'KING',
    });
    setFiles([]);
    setFormError(null);
  };

  // REMOVED: No longer needed as we use getRoomTypeName from logic
  // const getRoomTypeName = (roomTypeId) => {
  //   const roomType = roomTypes.find((rt) => rt.roomTypeId === roomTypeId);
  //   return roomType ? roomType.roomTypeName : 'Unknown';
  // };

  return (
    <div className="container mt-4">
      <h2>Room Management</h2>
      {error && <Alert variant="danger">{error}</Alert>}
      {loading && <Spinner animation="border" aria-label="Loading rooms" />}
      <Button variant="primary" onClick={openAddModal} className="mb-3" aria-label="Add new room">
        Add Room
      </Button>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Description</th>
            <th>Max Guests</th>
            <th>Price</th>
            <th>Room Size</th>
            <th>Status</th>
            <th>Room Type</th>
            <th>Bed Type</th>
            <th>Images</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {Array.isArray(rooms) && rooms.length > 0 ? (
            rooms.map((room) => (
              <tr key={room.roomId}>
                <td>{room.description || 'N/A'}</td>
                <td>{room.maxGuests || 'N/A'}</td>
                <td>{room.price ? room.price.toLocaleString() + ' VND' : 'N/A'}</td>
                <td>{room.roomSize ? room.roomSize + ' m²' : 'N/A'}</td>
                <td>{room.roomStatus || 'N/A'}</td>
                <td>{getRoomTypeName(room.roomTypeId)}</td> {/* FIXED: This should now work correctly */}
                <td>{room.bedType || 'N/A'}</td>
                <td>
                  {Array.isArray(room.imageRooms) && room.imageRooms.length > 0 ? (
                    room.imageRooms.map((img) => (
                      img.imageData && img.imageData.startsWith('data:image') ? (
                        <Image
                          key={img.imageRoomId}
                          src={img.imageData}
                          alt={`Room ${room.description}`}
                          thumbnail
                          style={{ width: '50px', height: '50px', marginRight: '5px' }}
                        />
                      ) : (
                        <span key={img.imageRoomId}>Invalid image</span>
                      )
                    ))
                  ) : (
                    'No images'
                  )}
                </td>
                <td>
                  <Button
                    variant="warning"
                    size="sm"
                    onClick={() => openEditModal(room)}
                    className="me-2"
                    aria-label={`Edit room ${room.description}`}
                  >
                    Edit
                  </Button>
                  <Button
                    variant="danger"
                    size="sm"
                    onClick={() => deleteRoom(room.roomId)}
                    aria-label={`Delete room ${room.description}`}
                  >
                    Delete
                  </Button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="9" className="text-center">
                No rooms available
              </td>
            </tr>
          )}
        </tbody>
      </Table>

      <Modal show={isAddModalOpen || isEditModalOpen} onHide={closeModals} aria-labelledby="room-modal-title">
        <Modal.Header closeButton>
          <Modal.Title id="room-modal-title">{isAddModalOpen ? 'Add Room' : 'Edit Room'}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {formError && <Alert variant="danger">{formError}</Alert>}
          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
              <Form.Label>Description</Form.Label>
              <Form.Control
                type="text"
                name="description"
                value={formData.description}
                onChange={handleInputChange}
                required
                aria-describedby="description-help"
              />
              <Form.Text id="description-help" muted>
                Enter a brief description of the room.
              </Form.Text>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Max Guests</Form.Label>
              <Form.Control
                type="number"
                name="maxGuests"
                value={formData.maxGuests}
                onChange={handleInputChange}
                required
                min="1"
                aria-describedby="maxGuests-help"
              />
              <Form.Text id="maxGuests-help" muted>
                Enter the maximum number of guests (positive number).
              </Form.Text>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Price (VND)</Form.Label>
              <Form.Control
                type="number"
                name="price"
                value={formData.price}
                onChange={handleInputChange}
                required
                min="1"
                aria-describedby="price-help"
              />
              <Form.Text id="price-help" muted>
                Enter the price per night (positive number).
              </Form.Text>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Room Size (m²)</Form.Label>
              <Form.Control
                type="number"
                name="roomSize"
                value={formData.roomSize}
                onChange={handleInputChange}
                required
                min="1"
                aria-describedby="roomSize-help"
              />
              <Form.Text id="roomSize-help" muted>
                Enter the room size in square meters (positive number).
              </Form.Text>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Status</Form.Label>
              <Form.Select
                name="roomStatus"
                value={formData.roomStatus}
                onChange={handleInputChange}
                aria-describedby="roomStatus-help"
              >
                <option value="AVAILABLE">Available</option>
                <option value="OCCUPIED">Occupied</option>
                <option value="MAINTENANCE">Maintenance</option>
              </Form.Select>
              <Form.Text id="roomStatus-help" muted>
                Select the current status of the room.
              </Form.Text>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Bed Type</Form.Label>
              <Form.Select
                name="bedType"
                value={formData.bedType}
                onChange={handleInputChange}
                aria-describedby="bedType-help"
              >
                <option value="SINGLE">Single</option>
                <option value="DOUBLE">Double</option>
                <option value="QUEEN">Queen</option>
                <option value="KING">King</option>
              </Form.Select>
              <Form.Text id="bedType-help" muted>
                Select the type of bed in the room.
              </Form.Text>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Room Type</Form.Label>
              <Form.Select
                name="roomTypeName"
                value={formData.roomTypeName}
                onChange={handleInputChange}
                required
                aria-describedby="roomTypeName-help"
              >
                <option value="">Select Room Type</option>
                {Array.isArray(roomTypes) && roomTypes.length > 0 ? (
                  roomTypes.map((rt) => (
                    <option key={rt.roomTypeId} value={rt.roomTypeName}>
                      {rt.roomTypeName}
                    </option>
                  ))
                ) : (
                  <option value="" disabled>No room types available</option>
                )}
              </Form.Select>
              <Form.Text id="roomTypeName-help" muted>
                Select the type of room (e.g., VIP, STANDARD).
              </Form.Text>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Images</Form.Label>
              <Form.Control
                type="file"
                multiple
                accept="image/*"
                onChange={handleFileChange}
                aria-describedby="images-help"
              />
              <Form.Text id="images-help" muted>
                Upload images (max 5MB each, image formats only).
              </Form.Text>
            </Form.Group>
            <Button variant="primary" type="submit" disabled={submitting}>
              {submitting ? <Spinner animation="border" size="sm" /> : isAddModalOpen ? 'Add' : 'Update'}
            </Button>
          </Form>
        </Modal.Body>
      </Modal>
    </div>
  );
};

export default RoomPageUI;