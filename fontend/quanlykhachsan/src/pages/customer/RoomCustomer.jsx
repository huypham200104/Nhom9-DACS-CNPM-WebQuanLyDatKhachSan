import React, { useState, useEffect } from 'react';
import roomRoute from '../../routes/RoomRoute';
import hotelRoute from '../../routes/HotelRoute';
import roomTypeRoute from '../../routes/RoomTypeRoute';
import Navbar from '../public/Navbar';
import Footer from '../public/Footer';
import Search from '../public/Search';
import {
  Button,
  Card,
  Col,
  Form,
  Row,
  Alert,
  Spinner,
  Pagination,
  Container,
} from 'react-bootstrap';
import { useNavigate, useLocation } from 'react-router-dom';
import './RoomCustomer.css';

const RoomCustomer = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const [rooms, setRooms] = useState([]);
  const [hotels, setHotels] = useState({});
  const [roomTypes, setRoomTypes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [loadingHotels, setLoadingHotels] = useState(false);
  const [loadingRoomTypes, setLoadingRoomTypes] = useState(false);
  const [error, setError] = useState(null);
  const [searchParams, setSearchParams] = useState({
    city: '',
    checkIn: '',
    checkOut: '',
    adults: 1,
    children: 0,
  });
  const [filters, setFilters] = useState({
    starRating: [],
    priceRange: [0, Infinity],
  });
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [totalElements, setTotalElements] = useState(0);
  const [isSearchActive, setIsSearchActive] = useState(false);
  const pageSize = 6; // Set page size to 6 rooms per page

  // Load room types once on component mount
  useEffect(() => {
    const fetchRoomTypes = async () => {
      setLoadingRoomTypes(true);
      try {
        const response = await roomTypeRoute.getAllRoomTypes();
        setRoomTypes(response || []);
      } catch (err) {
        console.error('Error loading room types:', err);
      } finally {
        setLoadingRoomTypes(false);
      }
    };
    fetchRoomTypes();
  }, []);

  const fetchHotelDetails = async (hotelIds) => {
    const newHotelIds = hotelIds.filter(id => !hotels[id]);
    if (newHotelIds.length === 0) return;

    setLoadingHotels(true);
    try {
      const hotelPromises = newHotelIds.map((hotelId) =>
        hotelRoute.getHotelById(hotelId).catch(err => {
          console.error(`Error loading hotel ${hotelId}:`, err);
          return null;
        })
      );

      const hotelResponses = await Promise.all(hotelPromises);
      const hotelData = hotelResponses.reduce((acc, res, index) => {
        if (res) {
          const hotel = res.data || res;
          acc[newHotelIds[index]] = {
            city: hotel.city,
            district: hotel.district,
            starRating: hotel.hotelRating || 0,
            ward: hotel.ward,
            street: hotel.street,
            houseNumber: hotel.houseNumber,
            hotelName: hotel.hotelName,
          };
        }
        return acc;
      }, {});
      setHotels((prev) => ({ ...prev, ...hotelData }));
    } catch (err) {
      console.error('Error fetching hotel details:', err);
    } finally {
      setLoadingHotels(false);
    }
  };

  const applyClientSideFilters = (roomsData) => {
    return roomsData.filter((room) => {
      const matchesPrice = room.price >= filters.priceRange[0] && 
                          (filters.priceRange[1] === Infinity || room.price <= filters.priceRange[1]);
      const hotelRating = hotels[room.hotelId]?.starRating || 0;
      const matchesStars = filters.starRating.length === 0 || 
                          filters.starRating.includes(hotelRating);
      return matchesPrice && matchesStars;
    });
  };

  const processRoomsData = async (response) => {
    const roomsData = response.content || response || [];
    const totalElements = response.totalElements || roomsData.length;
    const totalPages = response.totalPages || Math.ceil(totalElements / pageSize);

    console.log('API Response:', response); // Debug API response
    console.log('Processing rooms data:', {
      totalRooms: roomsData.length,
      totalElements,
      totalPages,
      currentPage: page,
      pageSize
    });

    // Process room images
    const processedRooms = roomsData.map((room) => ({
      ...room,
      imageRooms: Array.isArray(room.imageRooms) ? room.imageRooms : [],
    }));

    // Apply client-side filters
    const filteredRooms = applyClientSideFilters(processedRooms);

    console.log('Filtered rooms:', {
      filteredCount: filteredRooms.length,
      filtersApplied: filters
    });

    setRooms(filteredRooms);
    setTotalPages(totalPages);
    setTotalElements(totalElements);

    // Fetch hotel details for new rooms
    const hotelIds = [...new Set(filteredRooms.map((room) => room.hotelId).filter(id => id != null))];
    if (hotelIds.length > 0) {
      await fetchHotelDetails(hotelIds);
    }

    setError(null);
  };

  const fetchAllRooms = async (pageNum = page) => {
    setLoading(true);
    try {
      console.log(`Fetching all rooms - page: ${pageNum}, size: ${pageSize}`);
      const response = await roomRoute.getAllRooms(pageNum, pageSize);
      await processRoomsData(response);
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to fetch rooms');
      setRooms([]);
      setTotalPages(1);
      setTotalElements(0);
      console.error('Fetch all rooms error:', err);
    } finally {
      setLoading(false);
    }
  };

  const searchRoomsByCity = async (city, pageNum = page) => {
    setLoading(true);
    try {
      console.log(`Searching rooms by city: ${city} - page: ${pageNum}, size: ${pageSize}`);
      const response = await roomRoute.getRoomsByCity(city, pageNum, pageSize);
      await processRoomsData(response);
      setIsSearchActive(true);
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to fetch rooms by city');
      setRooms([]);
      setTotalPages(1);
      setTotalElements(0);
      console.error('Search rooms by city error:', err);
    } finally {
      setLoading(false);
    }
  };

  const searchAvailableRooms = async (params, pageNum = page) => {
    setLoading(true);
    try {
      const { city, checkIn, checkOut, adults, children } = params;
      if (!checkIn || !checkOut) {
        throw new Error('Check-in and check-out dates are required');
      }

      console.log(`Searching available rooms - page: ${pageNum}, size: ${pageSize}`);
      const response = await roomRoute.getAvailableRoomsWithDateFormat(
        city || '', checkIn, checkOut, adults || 1, children || 0, pageNum, pageSize
      );
      await processRoomsData(response);
      setIsSearchActive(true);
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to fetch available rooms');
      setRooms([]);
      setTotalPages(1);
      setTotalElements(0);
      console.error('Search available rooms error:', err);
    } finally {
      setLoading(false);
    }
  };

  // Handle URL state on component mount
  useEffect(() => {
    if (location.state?.searchParams && location.state?.shouldSearch) {
      const incomingSearchParams = location.state.searchParams;
      setSearchParams(incomingSearchParams);

      if (location.state.roomsData) {
        processRoomsData(location.state.roomsData);
        setIsSearchActive(true);
      } else if (incomingSearchParams.city && incomingSearchParams.checkIn && incomingSearchParams.checkOut) {
        searchAvailableRooms(incomingSearchParams, 0);
      } else if (incomingSearchParams.city) {
        searchRoomsByCity(incomingSearchParams.city, 0);
      }
      window.history.replaceState({}, document.title);
    } else if (!isSearchActive) {
      fetchAllRooms(0);
    }
  }, [location.state]);

  // Handle page changes
  useEffect(() => {
    if (location.state?.searchParams && location.state?.shouldSearch) {
      return;
    }

    if (isSearchActive) {
      if (searchParams.city && searchParams.checkIn && searchParams.checkOut) {
        searchAvailableRooms(searchParams, page);
      } else if (searchParams.city) {
        searchRoomsByCity(searchParams.city, page);
      }
    } else {
      fetchAllRooms(page);
    }
  }, [page]);

  // Handle filter changes
  useEffect(() => {
    if (isSearchActive) {
      if (searchParams.city && searchParams.checkIn && searchParams.checkOut) {
        searchAvailableRooms(searchParams, 0);
      } else if (searchParams.city) {
        searchRoomsByCity(searchParams.city, 0);
      }
    } else {
      fetchAllRooms(0);
    }
    setPage(0); // Reset to first page when filters change
  }, [filters]);

  const getRoomTypeName = (roomTypeId) => {
    const roomType = roomTypes.find((rt) => rt.roomTypeId === roomTypeId);
    return roomType ? roomType.roomTypeName : 'Unknown';
  };

  const handleSearchFromComponent = async (newSearchParams) => {
    setSearchParams(newSearchParams);
    setPage(0);

    if (newSearchParams.city && newSearchParams.checkIn && newSearchParams.checkOut) {
      await searchAvailableRooms(newSearchParams, 0);
    } else if (newSearchParams.city) {
      await searchRoomsByCity(newSearchParams.city, 0);
    } else {
      setIsSearchActive(false);
      await fetchAllRooms(0);
    }
  };

  const handleStarFilterChange = (star) => {
    const newFilters = {
      ...filters,
      starRating: filters.starRating.includes(star)
        ? filters.starRating.filter((s) => s !== star)
        : [...filters.starRating, star],
    };
    setFilters(newFilters);
  };

  const handlePriceRangeChange = (min, max) => {
    const newFilters = { ...filters, priceRange: [min, max] };
    setFilters(newFilters);
  };

  const handleViewDetails = (room) => {
    const hotel = hotels[room.hotelId] || {};
    const enrichedRoom = { ...room, hotel };
    navigate('/booking', { state: { room: enrichedRoom } });
  };

  const clearAllFilters = () => {
    const newFilters = { starRating: [], priceRange: [0, Infinity] };
    setFilters(newFilters);
  };

  const handlePageChange = (newPage) => {
    console.log(`Changing to page: ${newPage}`);
    setPage(newPage);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  return (
    <div className="room-customer-page">
      <Navbar />
      <div className="search-section">
        <Search onSearch={handleSearchFromComponent} />
      </div>
      <Container className="py-5">
        <Row>
          <Col md={3} className="mb-4">
            <Card className="filter-card">
              <Card.Body>
                <h5 className="mb-4">Filters</h5>
                <div className="mb-4">
                  <h6 className="mb-3">Star Rating</h6>
                  <div className="d-flex flex-column">
                    {[5, 4, 3, 2, 1].map((star) => (
                      <Form.Check
                        key={star}
                        type="checkbox"
                        id={`star-${star}`}
                        label={`${'★'.repeat(star)}${'☆'.repeat(5 - star)}`}
                        checked={filters.starRating.includes(star)}
                        onChange={() => handleStarFilterChange(star)}
                        className="mb-2"
                        aria-label={`Filter by ${star} star rating`}
                      />
                    ))}
                  </div>
                </div>
                <div className="mb-4">
                  <h6 className="mb-3">Price Range (VND)</h6>
                  <div className="px-2">
                    <Form.Range
                      value={filters.priceRange[0]}
                      onChange={(e) => handlePriceRangeChange(Number(e.target.value), filters.priceRange[1])}
                      min={0}
                      max={10000000}
                      step={100000}
                      className="mb-3"
                      aria-label="Minimum price range"
                    />
                    <Form.Range
                      value={filters.priceRange[1] === Infinity ? 10000000 : filters.priceRange[1]}
                      onChange={(e) => handlePriceRangeChange(filters.priceRange[0], Number(e.target.value))}
                      min={0}
                      max={10000000}
                      step={100000}
                      className="mb-2"
                      aria-label="Maximum price range"
                    />
                  </div>
                  <div className="d-flex justify-content-between price-range-display">
                    <span>{filters.priceRange[0].toLocaleString()} VND</span>
                    <span>{filters.priceRange[1] === Infinity ? '∞' : filters.priceRange[1].toLocaleString() + ' VND'}</span>
                  </div>
                </div>
                <Button
                  variant="outline-primary"
                  className="w-100 mt-3"
                  onClick={clearAllFilters}
                  aria-label="Clear all filters"
                >
                  Clear All Filters
                </Button>
              </Card.Body>
            </Card>
          </Col>

          <Col md={9}>
            {error && <Alert variant="danger">{error}</Alert>}
            
            {/* Results summary */}
            {!loading && !loadingHotels && !loadingRoomTypes && (
              <div className="mb-3">
                <small className="text-muted">
                  Showing {rooms.length} of {totalElements} rooms 
                  {totalPages > 1 && ` (Page ${page + 1} of ${totalPages})`}
                </small>
              </div>
            )}

            {(loading || loadingHotels || loadingRoomTypes) && (
              <div className="text-center my-5">
                <Spinner animation="border" variant="primary" />
                <p className="mt-2">Loading...</p>
              </div>
            )}

            <Row>
              {Array.isArray(rooms) && rooms.length > 0 ? (
                rooms.map((room) => (
                  <Col lg={4} md={6} key={room.roomId} className="mb-4">
                    <Card className="room-card">
                      <div className="room-image-container">
                        {Array.isArray(room.imageRooms) && room.imageRooms.length > 0 && room.imageRooms[0].imageData?.startsWith('data:image') ? (
                          <Card.Img
                            variant="top"
                            src={room.imageRooms[0].imageData}
                            alt={`Room ${room.description || 'image'}`}
                            className="room-image"
                          />
                        ) : (
                          <Card.Img
                            variant="top"
                            src="https://via.placeholder.com/300x200?text=No+Image"
                            alt="No image available"
                            className="room-image"
                          />
                        )}
                        <div className="price-badge">
                          {room.price ? room.price.toLocaleString() + ' VND' : 'N/A'}
                        </div>
                      </div>
                      <Card.Body className="d-flex flex-column">
                        <Card.Title>{room.description || 'N/A'}</Card.Title>
                        <Card.Text className="flex-grow-1">
                          <small className="text-muted d-block mb-2">
                            <i className="bi bi-building me-2"></i>
                            {hotels[room.hotelId]?.hotelName || 'Hotel Name'}
                          </small>
                          <small className="text-muted d-block mb-2">
                            <i className="bi bi-geo-alt-fill me-2"></i>
                            {hotels[room.hotelId]?.city || 'N/A'}, {hotels[room.hotelId]?.district || 'N/A'}
                          </small>
                          <small className="d-block mb-2">
                            <i className="bi bi-door-open-fill me-2"></i>
                            {getRoomTypeName(room.roomTypeId)}
                          </small>
                          <small className="d-block mb-2">
                            <i className="bi bi-people-fill me-2"></i>
                            Max {room.maxGuests || 'N/A'} guests
                          </small>
                          <small className="d-block">
                            <i className="bi bi-arrows-fullscreen me-2"></i>
                            {room.roomSize ? room.roomSize + ' m²' : 'N/A'}
                          </small>
                        </Card.Text>
                        <Button
                          variant="primary"
                          onClick={() => handleViewDetails(room)}
                          className="mt-2"
                          aria-label={`View details for room ${room.description || 'ID ' + room.roomId}`}
                        >
                          View Details
                        </Button>
                      </Card.Body>
                    </Card>
                  </Col>
                ))
              ) : !loading && !loadingHotels && !loadingRoomTypes && (
                <Col>
                  <Alert variant="info" className="text-center">
                    No rooms available matching your criteria
                  </Alert>
                </Col>
              )}
            </Row>

            {totalPages > 1 && (
              <div className="d-flex justify-content-center mt-4">
                <Pagination>
                  <Pagination.Prev
                    onClick={() => handlePageChange(Math.max(page - 1, 0))}
                    disabled={page === 0}
                    aria-label="Previous page"
                  />
                  {[...Array(totalPages)].map((_, i) => (
                    <Pagination.Item
                      key={i}
                      active={i === page}
                      onClick={() => handlePageChange(i)}
                      aria-label={`Page ${i + 1}`}
                    >
                      {i + 1}
                    </Pagination.Item>
                  ))}
                  <Pagination.Next
                    onClick={() => handlePageChange(Math.min(page + 1, totalPages - 1))}
                    disabled={page === totalPages - 1}
                    aria-label="Next page"
                  />
                </Pagination>
              </div>
            )}
          </Col>
        </Row>
      </Container>
      <Footer />
    </div>
  );
};

export default RoomCustomer;