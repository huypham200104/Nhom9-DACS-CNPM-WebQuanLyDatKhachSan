import React, { useState, useEffect, useCallback } from 'react';
import { BookingAPI } from '../routes/BookingRoute';

const BookingLogic = ({ children }) => {
  const [bookings, setBookings] = useState([]);
  const [selectedBooking, setSelectedBooking] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [pagination, setPagination] = useState({
    currentPage: 0,
    pageSize: 10,
    totalPages: 0,
    totalElements: 0
  });

  // Form states
  const [bookingForm, setBookingForm] = useState({
    customerId: '',
    roomIds: [],
    checkInDate: '',
    checkOutDate: '',
    numberOfGuests: 1,
    specialRequests: '',
    discountCode: '',
    priceBeforeDiscount: 0
  });

  const [filters, setFilters] = useState({
    customerId: '',
    roomId: '',
    hotelId: '', // Thêm hotelId vào filters
    status: '',
    startDate: '',
    endDate: ''
  });

  // Error handling
  const handleError = useCallback((error) => {
    console.error('Booking Error:', error);
    const errorMessage = error.response?.data?.message || error.message || 'Đã xảy ra lỗi';
    setError(errorMessage);
    setTimeout(() => setError(null), 5000);
  }, []);

  // Success handling
  const [successMessage, setSuccessMessage] = useState('');
  const handleSuccess = useCallback((message) => {
    setSuccessMessage(message);
    setTimeout(() => setSuccessMessage(''), 3000);
  }, []);

  // Load all bookings with pagination
  const loadBookings = useCallback(async (page = 0, size = 10) => {
    setLoading(true);
    try {
      const response = await BookingAPI.getAllBookings(page, size);
      setBookings(response.data.content || []);
      setPagination({
        currentPage: response.data.number || 0,
        pageSize: response.data.size || 10,
        totalPages: response.data.totalPages || 0,
        totalElements: response.data.totalElements || 0
      });
    } catch (error) {
      handleError(error);
    } finally {
      setLoading(false);
    }
  }, [handleError]);

  // Create new booking
  const createBooking = useCallback(async (bookingData) => {
    setLoading(true);
    try {
      const response = await BookingAPI.createBooking(bookingData);
      handleSuccess('Đặt phòng thành công!');
      await loadBookings();
      return response.data;
    } catch (error) {
      handleError(error);
      throw error;
    } finally {
      setLoading(false);
    }
  }, [handleError, handleSuccess, loadBookings]);

  // Get booking by ID
  const getBookingById = useCallback(async (bookingId) => {
    setLoading(true);
    try {
      const response = await BookingAPI.getBookingById(bookingId);
      setSelectedBooking(response.data);
      return response.data;
    } catch (error) {
      handleError(error);
      return null;
    } finally {
      setLoading(false);
    }
  }, [handleError]);

  // Update booking
  const updateBooking = useCallback(async (bookingId, bookingData) => {
    setLoading(true);
    try {
      const response = await BookingAPI.updateBooking(bookingId, bookingData);
      handleSuccess('Cập nhật đặt phòng thành công!');
      await loadBookings();
      return response.data;
    } catch (error) {
      handleError(error);
      throw error;
    } finally {
      setLoading(false);
    }
  }, [handleError, handleSuccess, loadBookings]);

  // Cancel booking (Chưa được hỗ trợ bởi backend)
  const cancelBooking = useCallback(async (bookingId) => {
    console.warn('cancelBooking is not supported by the backend');
    setLoading(true);
    try {
      // await BookingAPI.cancelBooking(bookingId); // Không có endpoint này
      handleSuccess('Hủy đặt phòng thành công! (Mocked)');
      await loadBookings();
    } catch (error) {
      handleError(error);
    } finally {
      setLoading(false);
    }
  }, [handleError, handleSuccess, loadBookings]);

  // Check room availability
  const checkRoomAvailability = useCallback(async (roomId, checkInDate, checkOutDate) => {
    try {
      const response = await BookingAPI.checkRoomAvailability(roomId, checkInDate, checkOutDate);
      return response.data;
    } catch (error) {
      handleError(error);
      return false;
    }
  }, [handleError]);

  // Calculate total price
  const calculateTotalPrice = useCallback(async (roomId, checkInDate, checkOutDate, discountId) => {
    try {
      const response = await BookingAPI.calculateTotalPrice(roomId, checkInDate, checkOutDate, discountId);
      return response.data;
    } catch (error) {
      handleError(error);
      return 0;
    }
  }, [handleError]);

  // Update payment status (Chưa được hỗ trợ bởi backend)
  const updatePaymentStatus = useCallback(async (bookingId, paymentStatus) => {
    console.warn('updatePaymentStatus is not supported by the backend');
    setLoading(true);
    try {
      // await BookingAPI.updatePaymentStatus(bookingId, paymentStatus); // Không có endpoint này
      handleSuccess('Cập nhật trạng thái thanh toán thành công! (Mocked)');
      await loadBookings();
    } catch (error) {
      handleError(error);
    } finally {
      setLoading(false);
    }
  }, [handleError, handleSuccess, loadBookings]);

  // Check-in booking (Chưa được hỗ trợ bởi backend)
  const checkInBooking = useCallback(async (bookingId) => {
    console.warn('checkInBooking is not supported by the backend');
    setLoading(true);
    try {
      // await BookingAPI.checkInBooking(bookingId); // Không có endpoint này
      handleSuccess('Check-in thành công! (Mocked)');
      await loadBookings();
    } catch (error) {
      handleError(error);
    } finally {
      setLoading(false);
    }
  }, [handleError, handleSuccess, loadBookings]);

  // Complete booking (check-out) (Chưa được hỗ trợ bởi backend)
  const completeBooking = useCallback(async (bookingId) => {
    console.warn('completeBooking is not supported by the backend');
    setLoading(true);
    try {
      // await BookingAPI.completeBooking(bookingId); // Không có endpoint này
      handleSuccess('Check-out thành công! (Mocked)');
      await loadBookings();
    } catch (error) {
      handleError(error);
    } finally {
      setLoading(false);
    }
  }, [handleError, handleSuccess, loadBookings]);

  // Mark as no-show (Chưa được hỗ trợ bởi backend)
  const markAsNoShow = useCallback(async (bookingId) => {
    console.warn('markAsNoShow is not supported by the backend');
    setLoading(true);
    try {
      // await BookingAPI.markAsNoShow(bookingId); // Không có endpoint này
      handleSuccess('Đánh dấu no-show thành công! (Mocked)');
      await loadBookings();
    } catch (error) {
      handleError(error);
    } finally {
      setLoading(false);
    }
  }, [handleError, handleSuccess, loadBookings]);

  // Refund booking (Chưa được hỗ trợ bởi backend)
  const refundBooking = useCallback(async (bookingId) => {
    console.warn('refundBooking is not supported by the backend');
    setLoading(true);
    try {
      // await BookingAPI.refundBooking(bookingId); // Không có endpoint này
      handleSuccess('Hoàn tiền thành công! (Mocked)');
      await loadBookings();
    } catch (error) {
      handleError(error);
    } finally {
      setLoading(false);
    }
  }, [handleError, handleSuccess, loadBookings]);

  // Filter bookings
  const filterBookings = useCallback(async (filterType, filterValue) => {
    setLoading(true);
    try {
      let response;
      switch (filterType) {
        case 'customer':
          response = await BookingAPI.getBookingsByCustomerId(filterValue);
          break;
        case 'room':
          response = await BookingAPI.getBookingsByRoomId(filterValue);
          break;
        case 'hotel':
          response = await BookingAPI.getBookingsByHotelId(filterValue, pagination.currentPage, pagination.pageSize);
          setPagination({
            currentPage: response.data.number || 0,
            pageSize: response.data.size || 10,
            totalPages: response.data.totalPages || 0,
            totalElements: response.data.totalElements || 0
          });
          break;
        case 'status':
          response = await BookingAPI.getBookingsByStatus(filterValue);
          break;
        case 'dateRange':
          response = await BookingAPI.getBookingsByDateRange(filterValue.startDate, filterValue.endDate, pagination.currentPage, pagination.pageSize);
          setPagination({
            currentPage: response.data.number || 0,
            pageSize: response.data.size || 10,
            totalPages: response.data.totalPages || 0,
            totalElements: response.data.totalElements || 0
          });
          break;
        default:
          return;
      }
      setBookings(Array.isArray(response.data) ? response.data : response.data.content || []);
    } catch (error) {
      handleError(error);
    } finally {
      setLoading(false);
    }
  }, [handleError, pagination]);

  // Form handlers
  const updateBookingForm = useCallback((field, value) => {
    setBookingForm(prev => ({
      ...prev,
      [field]: value
    }));
  }, []);

  const resetBookingForm = useCallback(() => {
    setBookingForm({
      customerId: '',
      roomIds: [],
      checkInDate: '',
      checkOutDate: '',
      numberOfGuests: 1,
      specialRequests: '',
      discountCode: '',
      priceBeforeDiscount: 0
    });
  }, []);

  const updateFilters = useCallback((field, value) => {
    setFilters(prev => ({
      ...prev,
      [field]: value
    }));
  }, []);

  // Pagination handlers
  const nextPage = useCallback(() => {
    if (pagination.currentPage < pagination.totalPages - 1) {
      loadBookings(pagination.currentPage + 1, pagination.pageSize);
    }
  }, [pagination, loadBookings]);

  const prevPage = useCallback(() => {
    if (pagination.currentPage > 0) {
      loadBookings(pagination.currentPage - 1, pagination.pageSize);
    }
  }, [pagination, loadBookings]);

  const goToPage = useCallback((page) => {
    loadBookings(page, pagination.pageSize);
  }, [pagination.pageSize, loadBookings]);

  // Load initial data
  useEffect(() => {
    loadBookings();
  }, [loadBookings]);

  // Context value
  const contextValue = {
    // State
    bookings,
    selectedBooking,
    loading,
    error,
    successMessage,
    pagination,
    bookingForm,
    filters,

    // Actions
    loadBookings,
    createBooking,
    getBookingById,
    updateBooking,
    cancelBooking,
    checkRoomAvailability,
    calculateTotalPrice,
    updatePaymentStatus,
    checkInBooking,
    completeBooking,
    markAsNoShow,
    refundBooking,
    filterBookings,

    // Form handlers
    updateBookingForm,
    resetBookingForm,
    updateFilters,

    // Pagination
    nextPage,
    prevPage,
    goToPage,

    // Utility
    setSelectedBooking,
    setError
  };

  return children(contextValue);
};

export default BookingLogic;