import React, { useState } from 'react';
import {
  Button,
  Card,
  Col,
  Form,
  FormControl,
  Row,
  Container,
} from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import roomRoute from '../../routes/RoomRoute';
import './Search.css';

const Search = ({ onSearch }) => {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useState({
    city: '',
    checkIn: '',
    checkOut: '',
    adults: 1,
    children: 0,
  });
  const [guestDropdownOpen, setGuestDropdownOpen] = useState(false);
  const [isSearching, setIsSearching] = useState(false);

  const handleSearchChange = (e) => {
    const { name, value } = e.target;
    setSearchParams({ ...searchParams, [name]: value });
  };

  const handleGuestChange = (type, increment) => {
    setSearchParams((prev) => ({
      ...prev,
      [type]: Math.max(0, (prev[type] || 0) + (increment ? 1 : -1)),
    }));
  };

  const handleSearchSubmit = async (e) => {
    e.preventDefault();
    
    // Kiểm tra city bắt buộc
    if (!searchParams.city || !searchParams.city.trim()) {
      alert('Please enter a city to search');
      return;
    }

    // Kiểm tra ngày check-in và check-out nếu có
    if (searchParams.checkIn && searchParams.checkOut && searchParams.checkIn >= searchParams.checkOut) {
      alert('Check-out date must be after check-in date');
      return;
    }
    if (searchParams.checkIn && !searchParams.checkOut) {
      alert('Please select a check-out date');
      return;
    }
    if (!searchParams.checkIn && searchParams.checkOut) {
      alert('Please select a check-in date');
      return;
    }

    setIsSearching(true);
    try {
      let response = null;
      
      // Kiểm tra đầy đủ thông tin: city, checkIn, checkOut
      const hasFullSearchParams = searchParams.city && searchParams.checkIn && searchParams.checkOut;
      
      if (hasFullSearchParams) {
        // Gọi getAvailableRooms khi có đầy đủ thông tin
        response = await roomRoute.getAvailableRooms(
          searchParams.city.trim(),
          searchParams.checkIn,
          searchParams.checkOut,
          searchParams.adults,
          searchParams.children,
          0, // page mặc định
          5  // size mặc định
        );
      } else {
        // Gọi getRoomsByCity khi chỉ có city
        response = await roomRoute.getRoomsByCity(
          searchParams.city.trim(),
          0, // page mặc định
          5  // size mặc định
        );
      }

      // Chuẩn hóa roomsData
      const roomsData = response.content || response || [];

      // Gọi onSearch nếu được truyền từ parent (tùy chọn)
      if (onSearch) {
        await onSearch(searchParams);
      }

      // Chuyển hướng đến RoomCustomer với kết quả tìm kiếm
      navigate('/rooms', {
        state: {
          searchParams: {
            ...searchParams,
            city: searchParams.city.trim(), // Chuẩn hóa city
          },
          roomsData: {
            content: Array.isArray(roomsData) ? roomsData : [],
            totalPages: response.totalPages || 1
          },
          shouldSearch: true,
          searchType: hasFullSearchParams ? 'available' : 'city' // Thêm thông tin loại tìm kiếm
        },
      });
    } catch (error) {
      console.error('Search error:', error);
      alert('Failed to search rooms: ' + (error.response?.data?.message || error.message));
    } finally {
      setIsSearching(false);
    }
  };

  return (
    <div className="search-component">
      <h1 className="display-5 fw-bold mb-3 text-dark">Find Your Next Stay</h1>
      <p className="lead mb-4 text-muted">Search low prices on hotels, homes, and much more...</p>

      <Container>
        <Card className="border-0 shadow-sm">
          <Card.Body>
            <Form onSubmit={handleSearchSubmit} className="mb-3">
              <Row className="g-3 align-items-end">
                <Col md={3}>
                  <Form.Group>
                    <Form.Label className="fw-semibold text-dark">City *</Form.Label>
                    <FormControl
                      type="text"
                      name="city"
                      value={searchParams.city}
                      onChange={handleSearchChange}
                      placeholder="Where are you going?"
                      aria-label="City"
                      required
                      className="rounded-pill border-light shadow-sm transition-all duration-300 focus:ring focus:ring-primary focus:ring-opacity-50"
                    />
                  </Form.Group>
                </Col>
                <Col md={2}>
                  <Form.Group>
                    <Form.Label className="fw-semibold text-dark">Check-in</Form.Label>
                    <FormControl
                      type="date"
                      name="checkIn"
                      value={searchParams.checkIn}
                      onChange={handleSearchChange}
                      aria-label="Check-in date"
                      min={new Date().toISOString().split('T')[0]}
                      className="rounded-pill border-light shadow-sm transition-all duration-300 focus:ring focus:ring-primary focus:ring-opacity-50"
                    />
                  </Form.Group>
                </Col>
                <Col md={2}>
                  <Form.Group>
                    <Form.Label className="fw-semibold text-dark">Check-out</Form.Label>
                    <FormControl
                      type="date"
                      name="checkOut"
                      value={searchParams.checkOut}
                      onChange={handleSearchChange}
                      aria-label="Check-out date"
                      min={searchParams.checkIn || new Date().toISOString().split('T')[0]}
                      className="rounded-pill border-light shadow-sm transition-all duration-300 focus:ring focus:ring-primary focus:ring-opacity-50"
                    />
                  </Form.Group>
                </Col>
                <Col md={3}>
                  <Form.Group 
                    onMouseEnter={() => setGuestDropdownOpen(true)} 
                    onMouseLeave={() => setGuestDropdownOpen(false)} 
                    style={{ position: 'relative' }}
                  >
                    <Form.Label className="fw-semibold text-dark">Guests</Form.Label>
                    <FormControl
                      type="text"
                      readOnly
                      value={`${searchParams.adults || 0} Adults, ${searchParams.children || 0} Children`}
                      className="rounded-pill border-light shadow-sm transition-all duration-300"
                    />
                    {guestDropdownOpen && (
                      <div 
                        className="p-3 shadow-lg bg-white rounded-3 border dropdown-guest" 
                        style={{
                          position: 'absolute',
                          top: '100%',
                          left: 0,
                          width: '100%',
                          zIndex: 1050,
                          transition: 'opacity 0.3s ease, transform 0.3s ease',
                          transform: guestDropdownOpen ? 'translateY(0)' : 'translateY(-10px)',
                          opacity: guestDropdownOpen ? 1 : 0,
                        }}
                      >
                        <div className="d-flex justify-content-between align-items-center mb-3">
                          <span className="text-dark fw-medium">Adults</span>
                          <div className="d-flex align-items-center">
                            <Button
                              variant="outline-primary"
                              size="sm"
                              onClick={() => handleGuestChange('adults', false)}
                              disabled={(searchParams.adults || 0) <= 1}
                              className="rounded-circle me-2 border-2"
                              style={{ width: '32px', height: '32px' }}
                            >-</Button>
                            <span className="mx-2 text-dark">{searchParams.adults || 0}</span>
                            <Button
                              variant="outline-primary"
                              size="sm"
                              onClick={() => handleGuestChange('adults', true)}
                              className="rounded-circle ms-2 border-2"
                              style={{ width: '32px', height: '32px' }}
                            >+</Button>
                          </div>
                        </div>
                        <div className="d-flex justify-content-between align-items-center">
                          <span className="text-dark fw-medium">Children</span>
                          <div className="d-flex align-items-center">
                            <Button
                              variant="outline-primary"
                              size="sm"
                              onClick={() => handleGuestChange('children', false)}
                              disabled={(searchParams.children || 0) <= 0}
                              className="rounded-circle me-2 border-2"
                              style={{ width: '32px', height: '32px' }}
                            >-</Button>
                            <span className="mx-2 text-dark">{searchParams.children || 0}</span>
                            <Button
                              variant="outline-primary"
                              size="sm"
                              onClick={() => handleGuestChange('children', true)}
                              className="rounded-circle ms-2 border-2"
                              style={{ width: '32px', height: '32px' }}
                            >+</Button>
                          </div>
                        </div>
                      </div>
                    )}
                  </Form.Group>
                </Col>
                <Col md={2} className="d-flex align-items-end">
                  <Button 
                    type="submit" 
                    variant="primary" 
                    className="rounded-pill px-4 py-2 fw-bold w-100 transition-all duration-300 hover:shadow-lg"
                    disabled={isSearching}
                  >
                    {isSearching ? (
                      <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                    ) : null}
                    {isSearching ? 'Searching...' : 'Search'}
                  </Button>
                </Col>
              </Row>
            </Form>
          </Card.Body>
        </Card>
      </Container>
    </div>
  );
};

export default Search;