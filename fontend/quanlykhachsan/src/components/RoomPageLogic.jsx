import { useState, useEffect } from 'react';
import roomRoute from '../routes/RoomRoute';
import roomTypeRoute from '../routes/RoomTypeRoute';
import hotelRoute from '../routes/HotelRoute';

const RoomPageLogic = () => {
  const [rooms, setRooms] = useState([]);
  const [roomTypes, setRoomTypes] = useState([]);
  const [hotels, setHotels] = useState({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [currentRoom, setCurrentRoom] = useState(null);
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
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
  const [availableDates, setAvailableDates] = useState({});
  const [isSearchActive, setIsSearchActive] = useState(false);

  // Get hotelId from sessionStorage
  const getSessionHotelId = () => {
    return sessionStorage.getItem('hotelId');
  };

  useEffect(() => {
    fetchRoomTypes();
    if (!isSearchActive) {
      fetchRooms();
    }
  }, [page, filters, isSearchActive]);

  const fetchRooms = async () => {
    setLoading(true);
    try {
      const sessionHotelId = getSessionHotelId();
      
      // If no hotelId in session, show error
      if (!sessionHotelId) {
        setError('No hotel selected. Please login with a valid hotel account.');
        setRooms([]);
        return;
      }

      const response = await roomRoute.getAllRooms(page, 8);
      let roomsData = response.content || response || [];
      
      // Filter rooms by hotelId from session
      roomsData = roomsData.filter((room) => room.hotelId === sessionHotelId);
      
      // Apply client-side filters
      roomsData = roomsData.filter((room) => {
        const matchesPrice = room.price >= filters.priceRange[0] && room.price <= filters.priceRange[1];
        const matchesStars = filters.starRating.length
          ? filters.starRating.includes(hotels[room.hotelId]?.starRating)
          : true;
        return matchesPrice && matchesStars;
      });

      setRooms(
        roomsData.map((room) => ({
          ...room,
          imageRooms: Array.isArray(room.imageRooms) ? room.imageRooms : [],
        }))
      );
      setTotalPages(response.totalPages || 1);
      
      // Fetch hotel details for the current hotel only
      if (sessionHotelId && roomsData.length > 0) {
        try {
          const hotelResponse = await hotelRoute.getHotelById(sessionHotelId);
          const hotelData = {
            [sessionHotelId]: {
              city: hotelResponse.city,
              district: hotelResponse.district,
              starRating: hotelResponse.hotelRating || 0,
              ward: hotelResponse.ward,
              street: hotelResponse.street,
              houseNumber: hotelResponse.houseNumber,
              hotelName: hotelResponse.hotelName,
            }
          };
          setHotels((prev) => ({ ...prev, ...hotelData }));
        } catch (hotelErr) {
          console.warn('Failed to fetch hotel details:', hotelErr);
        }
      }
      
      setError(null);
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to fetch rooms');
    } finally {
      setLoading(false);
    }
  };

  const searchAvailableRooms = async (params) => {
    setLoading(true);
    try {
      const sessionHotelId = getSessionHotelId();
      
      // If no hotelId in session, show error
      if (!sessionHotelId) {
        setError('No hotel selected. Please login with a valid hotel account.');
        setRooms([]);
        return;
      }

      const { city, checkIn, checkOut, adults, children } = params;
      
      if (!checkIn || !checkOut) {
        throw new Error('Check-in and check-out dates are required');
      }
      
      const formattedCheckIn = roomRoute.formatDateForAPI(checkIn);
      const formattedCheckOut = roomRoute.formatDateForAPI(checkOut);
      
      const response = await roomRoute.getAvailableRooms(
        city || '',
        formattedCheckIn,
        formattedCheckOut,
        adults || 1,
        children || 0,
        page,
        8
      );
      
      let roomsData = response.content || response || [];

      // Filter rooms by hotelId from session
      roomsData = roomsData.filter((room) => room.hotelId === sessionHotelId);

      // Apply client-side filters
      roomsData = roomsData.filter((room) => {
        const matchesPrice = room.price >= filters.priceRange[0] && room.price <= filters.priceRange[1];
        const matchesStars = filters.starRating.length
          ? filters.starRating.includes(hotels[room.hotelId]?.starRating)
          : true;
        return matchesPrice && matchesStars;
      });

      setRooms(
        roomsData.map((room) => ({
          ...room,
          imageRooms: Array.isArray(room.imageRooms) ? room.imageRooms : [],
        }))
      );
      setTotalPages(response.totalPages || 1);

      // Fetch hotel details for the current hotel only if not already loaded
      if (sessionHotelId && !hotels[sessionHotelId] && roomsData.length > 0) {
        try {
          const hotelResponse = await hotelRoute.getHotelById(sessionHotelId);
          const hotelData = {
            [sessionHotelId]: {
              city: hotelResponse.city,
              district: hotelResponse.district,
              starRating: hotelResponse.hotelRating || 0,
              ward: hotelResponse.ward,
              street: hotelResponse.street,
              houseNumber: hotelResponse.houseNumber,
              hotelName: hotelResponse.hotelName,
            }
          };
          setHotels((prev) => ({ ...prev, ...hotelData }));
        } catch (hotelErr) {
          console.warn('Failed to fetch hotel details:', hotelErr);
        }
      }

      setIsSearchActive(true);
      setError(null);
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to fetch available rooms');
    } finally {
      setLoading(false);
    }
  };

  const fetchRoomTypes = async () => {
    try {
      const roomTypesData = await roomTypeRoute.getAllRoomTypes();
      setRoomTypes(roomTypesData || []);
      setError(null);
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to fetch room types');
      setRoomTypes([]);
    }
  };

  const fetchAvailableDates = async (roomId, startDate, endDate) => {
    setLoading(true);
    try {
      const start = startDate || searchParams.checkIn;
      const end = endDate || searchParams.checkOut;
      
      if (!start || !end) {
        throw new Error('Start date and end date are required');
      }
      
      const formattedStart = roomRoute.formatDateForAPI(start);
      const formattedEnd = roomRoute.formatDateForAPI(end);
      
      const dates = await roomRoute.getAvailableDatesForRoom(roomId, formattedStart, formattedEnd);
      setAvailableDates((prev) => ({
        ...prev,
        [roomId]: dates
      }));
      setError(null);
      return dates;
    } catch (err) {
      setError(err.response?.data?.[0] || err.message || 'Failed to fetch available dates');
      return [];
    } finally {
      setLoading(false);
    }
  };

  const addRoom = async (roomData, files) => {
    setLoading(true);
    try {
      const sessionHotelId = getSessionHotelId();
      
      // Validate hotelId from session
      if (!sessionHotelId) {
        throw new Error('No hotel selected. Please login with a valid hotel account.');
      }

      const roomType = roomTypes.find((rt) => rt.roomTypeName === roomData.roomTypeName);
      if (!roomType) {
        throw new Error('Invalid room type selected');
      }

      const roomDto = {
        description: roomData.description || '',
        maxGuests: parseInt(roomData.maxGuests) || 0,
        price: parseInt(roomData.price) || 0,
        roomSize: parseInt(roomData.roomSize) || 0,
        roomStatus: roomData.roomStatus || 'AVAILABLE',
        bedType: roomData.bedType || '',
        hotelId: sessionHotelId, // Always use hotelId from session
        roomTypeId: roomType.roomTypeId,
      };

      const response = await roomRoute.addRoom(roomDto, files);
      setRooms([
        ...rooms,
        { ...response, imageRooms: Array.isArray(response.imageRooms) ? response.imageRooms : [] },
      ]);
      setError(null);
      return true;
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to add room');
      return false;
    } finally {
      setLoading(false);
    }
  };

  const updateRoom = async (roomId, roomData, files) => {
    setLoading(true);
    try {
      const sessionHotelId = getSessionHotelId();
      
      // Validate hotelId from session
      if (!sessionHotelId) {
        throw new Error('No hotel selected. Please login with a valid hotel account.');
      }

      const roomType = roomTypes.find((rt) => rt.roomTypeName === roomData.roomTypeName);
      if (!roomType) {
        throw new Error('Invalid room type selected');
      }

      const roomDto = {
        description: roomData.description || '',
        maxGuests: parseInt(roomData.maxGuests) || 0,
        price: parseInt(roomData.price) || 0,
        roomSize: parseInt(roomData.roomSize) || 0,
        roomStatus: roomData.roomStatus || 'AVAILABLE',
        bedType: roomData.bedType || '',
        hotelId: sessionHotelId, // Always use hotelId from session
        roomTypeId: roomType.roomTypeId,
      };

      const response = await roomRoute.updateRoom(roomId, roomDto, files);
      setRooms(
        rooms.map((room) =>
          room.roomId === roomId
            ? { ...response, imageRooms: Array.isArray(response.imageRooms) ? response.imageRooms : [] }
            : room
        )
      );
      setError(null);
      return true;
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to update room');
      return false;
    } finally {
      setLoading(false);
    }
  };

  const deleteRoom = async (roomId) => {
    setLoading(true);
    try {
      // Check if room belongs to current hotel
      const room = rooms.find(r => r.roomId === roomId);
      const sessionHotelId = getSessionHotelId();
      
      if (!sessionHotelId) {
        throw new Error('No hotel selected. Please login with a valid hotel account.');
      }
      
      if (room && room.hotelId !== sessionHotelId) {
        throw new Error('You can only delete rooms from your own hotel.');
      }

      await roomRoute.deleteRoom(roomId);
      setRooms(rooms.filter((room) => room.roomId !== roomId));
      setError(null);
      return true;
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to delete room');
      return false;
    } finally {
      setLoading(false);
    }
  };

  const openAddModal = () => {
    setCurrentRoom(null);
    setIsAddModalOpen(true);
  };

  const openEditModal = (room) => {
    const sessionHotelId = getSessionHotelId();
    
    // Check if room belongs to current hotel
    if (room.hotelId !== sessionHotelId) {
      setError('You can only edit rooms from your own hotel.');
      return;
    }

    const roomType = roomTypes.find((rt) => rt.roomTypeId === room.roomTypeId);
    setCurrentRoom({
      ...room,
      roomTypeName: roomType ? roomType.roomTypeName : '',
    });
    setIsEditModalOpen(true);
  };

  const closeModals = () => {
    setIsAddModalOpen(false);
    setIsEditModalOpen(false);
    setCurrentRoom(null);
  };

  const updateSearchParams = (params) => {
    setSearchParams(params);
    setPage(0);
    
    if (params.checkIn && params.checkOut && params.adults > 0) {
      searchAvailableRooms(params);
    } else {
      setIsSearchActive(false);
      fetchRooms();
    }
  };

  const updateFilters = (newFilters) => {
    setFilters(newFilters);
    setPage(0);
    
    if (isSearchActive && searchParams.checkIn && searchParams.checkOut) {
      searchAvailableRooms(searchParams);
    } else {
      fetchRooms();
    }
  };

  const clearSearch = () => {
    setSearchParams({
      city: '',
      checkIn: '',
      checkOut: '',
      adults: 1,
      children: 0,
    });
    setIsSearchActive(false);
    setPage(0);
    fetchRooms();
  };

  const isSearchValid = (params = searchParams) => {
    return params.checkIn && 
           params.checkOut && 
           params.adults > 0 && 
           new Date(params.checkIn) < new Date(params.checkOut);
  };

  const getRoomTypeName = (roomTypeId) => {
    const roomType = roomTypes.find((rt) => rt.roomTypeId === roomTypeId);
    return roomType ? roomType.roomTypeName : 'Unknown';
  };

  return {
    rooms,
    roomTypes,
    hotels,
    loading,
    error,
    currentRoom,
    isAddModalOpen,
    isEditModalOpen,
    searchParams,
    filters,
    page,
    totalPages,
    availableDates,
    isSearchActive,
    fetchRooms,
    fetchRoomTypes,
    fetchAvailableDates,
    searchAvailableRooms,
    addRoom,
    updateRoom,
    deleteRoom,
    openAddModal,
    openEditModal,
    closeModals,
    updateSearchParams,
    updateFilters,
    setPage,
    clearSearch,
    isSearchValid,
    getRoomTypeName,
  };
};

export default RoomPageLogic;