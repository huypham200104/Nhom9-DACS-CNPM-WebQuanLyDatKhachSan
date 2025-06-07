import React, { useState, useEffect } from 'react';
import BookingLogic from './BookingLogic';
import { BookingAPI } from '../routes/BookingRoute'; // Adjust the path as needed

const BookingUI = () => {
  const [hotelId, setHotelId] = useState(null);
  const [showEditModal, setShowEditModal] = useState(false);
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [pagination, setPagination] = useState({ currentPage: 0, totalPages: 0, size: 10 });

  // Load hotelId from sessionStorage
  useEffect(() => {
    const id = sessionStorage.getItem('hotelId');
    if (id) {
      setHotelId(id);
    }
  }, []);

  // Fetch bookings when hotelId changes
  useEffect(() => {
    if (hotelId) {
      fetchBookings(hotelId, pagination.currentPage, pagination.size);
    }
  }, [hotelId, pagination.currentPage]);

  // Function to fetch bookings using getBookingsByHotelId
  const fetchBookings = async (hotelId, page, size) => {
    setLoading(true);
    try {
      const response = await BookingAPI.getBookingsByHotelId(hotelId, page, size);
      setBookings(response.content || []);
      setPagination({
        currentPage: response.number,
        totalPages: response.totalPages,
        size: response.size
      });
      setError(null);
    } catch (err) {
      setError('Failed to fetch bookings: ' + (err.message || 'Unknown error'));
      setBookings([]);
    } finally {
      setLoading(false);
    }
  };

  // Inner component to handle BookingLogic render props
  const BookingContent = ({
    selectedBooking,
    setSelectedBooking,
    bookingForm,
    updateBookingForm,
    resetBookingForm,
    updateBooking,
    cancelBooking,
    updatePaymentStatus,
    checkInBooking,
    completeBooking,
    markAsNoShow,
    refundBooking,
  }) => {
    // Handle form input changes
    const handleInputChange = (field, value) => {
      updateBookingForm(field, value);
    };

    // Handle edit button click
    const handleEditClick = (booking) => {
      setSelectedBooking(booking);
      updateBookingForm('customerId', booking.customerId || '');
      updateBookingForm('roomIds', booking.roomIds || []);
      updateBookingForm('checkInDate', booking.checkInDate || '');
      updateBookingForm('checkOutDate', booking.checkOutDate || '');
      updateBookingForm('numberOfGuests', booking.numberOfGuests || 1);
      updateBookingForm('specialRequests', booking.specialRequests || '');
      updateBookingForm('discountCode', booking.discountCode || '');
      updateBookingForm('priceBeforeDiscount', booking.priceBeforeDiscount || 0);
      updateBookingForm('bookingStatus', booking.bookingStatus || '');
      setShowEditModal(true);
    };

    // Handle form submission for updating booking
    const handleUpdateBooking = async (e) => {
      e.preventDefault();
      try {
        await updateBooking(selectedBooking.bookingId, bookingForm);
        setShowEditModal(false);
        resetBookingForm();
        // Reload bookings
        if (hotelId) {
          fetchBookings(hotelId, pagination.currentPage, pagination.size);
        }
      } catch (error) {
        console.error('Update failed:', error);
        setError('Failed to update booking: ' + (error.message || 'Unknown error'));
      }
    };

    // Handle status change actions
    const handleStatusChange = async (bookingId, action) => {
      try {
        switch (action) {
          case 'cancel':
            await cancelBooking(bookingId);
            break;
          case 'updatePayment':
            await updatePaymentStatus(bookingId, 'PAID');
            break;
          case 'checkIn':
            await checkInBooking(bookingId);
            break;
          case 'complete':
            await completeBooking(bookingId);
            break;
          case 'noShow':
            await markAsNoShow(bookingId);
            break;
          case 'refund':
            await refundBooking(bookingId);
            break;
          default:
            break;
        }
        // Reload bookings
        if (hotelId) {
          fetchBookings(hotelId, pagination.currentPage, pagination.size);
        }
      } catch (error) {
        console.error('Status change failed:', error);
        setError('Status change failed: ' + (error.message || 'Unknown error'));
      }
    };

    // Pagination handlers
    const nextPage = () => {
      if (pagination.currentPage < pagination.totalPages - 1) {
        setPagination(prev => ({ ...prev, currentPage: prev.currentPage + 1 }));
      }
    };

    const prevPage = () => {
      if (pagination.currentPage > 0) {
        setPagination(prev => ({ ...prev, currentPage: prev.currentPage - 1 }));
      }
    };

    const goToPage = (page) => {
      setPagination(prev => ({ ...prev, currentPage: page }));
    };

    // Show loading state if no hotelId yet
    if (!hotelId) {
      return (
        <div className="container mt-5">
          <div className="text-center">
            <p>Loading hotel information...</p>
          </div>
        </div>
      );
    }

    return (
      <div className="container mt-5">
        <h1 className="mb-4">Booking Management for Hotel {hotelId}</h1>

        {/* Error Messages */}
        {error && (
          <div className="alert alert-danger" role="alert">
            {error}
          </div>
        )}

        {/* Loading Indicator */}
        {loading && (
          <div className="text-center">
            <div className="spinner-border" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
          </div>
        )}

        {/* Bookings Table */}
        {!loading && (
          <div className="card">
            <div className="card-header">
              <h3 className="card-title">Bookings List for {bookings[0]?.hotelName || 'Hotel'}</h3>
            </div>
            <div className="card-body">
              <table className="table table-striped table-bordered">
                <thead>
                  <tr>
                    <th>Booking ID</th>
                    <th>Customer ID</th>
                    <th>Room IDs</th>
                    <th>Room Type</th>
                    <th>Check-In</th>
                    <th>Check-Out</th>
                    <th>Guests</th>
                    <th>Total Price</th>
                    <th>Status</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {bookings && bookings.length > 0 ? (
                    bookings.map((booking) => (
                      <tr key={booking.bookingId}>
                        <td>{booking.bookingId}</td>
                        <td>{booking.customerId}</td>
                        <td>{booking.roomIds?.join(', ')}</td>
                        <td>{booking.roomType}</td>
                        <td>{booking.checkInDate}</td>
                        <td>{booking.checkOutDate}</td>
                        <td>{booking.numberOfGuests}</td>
                        <td>{booking.totalPrice?.toLocaleString()} VND</td>
                        <td>{booking.bookingStatus}</td>
                        <td>
                          <button
                            className="btn btn-primary btn-sm me-2"
                            onClick={() => handleEditClick(booking)}
                            title="Edit booking"
                          >
                            Edit
                          </button>
                          <div className="dropdown d-inline-block">
                            <button
                              className="btn btn-secondary btn-sm dropdown-toggle"
                              type="button"
                              data-bs-toggle="dropdown"
                              aria-expanded="false"
                              title="Change status"
                            >
                              Status
                            </button>
                            <ul className="dropdown-menu">
                              <li>
                                <button
                                  className="dropdown-item"
                                  onClick={() => handleStatusChange(booking.bookingId, 'cancel')}
                                >
                                  Cancel
                                </button>
                              </li>
                              <li>
                                <button
                                  className="dropdown-item"
                                  onClick={() => handleStatusChange(booking.bookingId, 'updatePayment')}
                                >
                                  Update Payment
                                </button>
                              </li>
                              <li>
                                <button
                                  className="dropdown-item"
                                  onClick={() => handleStatusChange(booking.bookingId, 'checkIn')}
                                >
                                  Check-In
                                </button>
                              </li>
                              <li>
                                <button
                                  className="dropdown-item"
                                  onClick={() => handleStatusChange(booking.bookingId, 'complete')}
                                >
                                  Complete
                                </button>
                              </li>
                              <li>
                                <button
                                  className="dropdown-item"
                                  onClick={() => handleStatusChange(booking.bookingId, 'noShow')}
                                >
                                  No-Show
                                </button>
                              </li>
                              <li>
                                <button
                                  className="dropdown-item"
                                  onClick={() => handleStatusChange(booking.bookingId, 'refund')}
                                >
                                  Refund
                                </button>
                              </li>
                            </ul>
                          </div>
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan="10" className="text-center">
                        No bookings found
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* Pagination */}
        {pagination.totalPages > 1 && (
          <nav className="mt-3" aria-label="Bookings pagination">
            <ul className="pagination justify-content-center">
              <li className={`page-item ${pagination.currentPage === 0 ? 'disabled' : ''}`}>
                <button 
                  className="page-link" 
                  onClick={prevPage}
                  disabled={pagination.currentPage === 0}
                  aria-label="Previous page"
                >
                  Previous
                </button>
              </li>
              {[...Array(pagination.totalPages).keys()].map((page) => (
                <li
                  key={page}
                  className={`page-item ${pagination.currentPage === page ? 'active' : ''}`}
                >
                  <button 
                    className="page-link" 
                    onClick={() => goToPage(page)}
                    aria-label={`Go to page ${page + 1}`}
                    aria-current={pagination.currentPage === page ? 'page' : undefined}
                  >
                    {page + 1}
                  </button>
                </li>
              ))}
              <li
                className={`page-item ${
                  pagination.currentPage === pagination.totalPages - 1 ? 'disabled' : ''
                }`}
              >
                <button 
                  className="page-link" 
                  onClick={nextPage}
                  disabled={pagination.currentPage === pagination.totalPages - 1}
                  aria-label="Next page"
                >
                  Next
                </button>
              </li>
            </ul>
          </nav>
        )}

        {/* Edit Booking Modal */}
        <div
          className={`modal fade ${showEditModal ? 'show' : ''}`}
          style={{ display: showEditModal ? 'block' : 'none' }}
          tabIndex="-1"
          aria-labelledby="editBookingModalLabel"
          aria-hidden={!showEditModal}
          role="dialog"
        >
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title" id="editBookingModalLabel">
                  Edit Booking
                </h5>
                <button
                  type="button"
                  className="btn-close"
                  onClick={() => {
                    setShowEditModal(false);
                    resetBookingForm();
                  }}
                  aria-label="Close modal"
                ></button>
              </div>
              <div className="modal-body">
                <form onSubmit={handleUpdateBooking}>
                  <div className="mb-3">
                    <label htmlFor="customerId" className="form-label">
                      Customer ID
                    </label>
                    <input
                      type="text"
                      className="form-control"
                      id="customerId"
                      value={bookingForm.customerId || ''}
                      onChange={(e) => handleInputChange('customerId', e.target.value)}
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label htmlFor="roomIds" className="form-label">
                      Room IDs (comma-separated)
                    </label>
                    <input
                      type="text"
                      className="form-control"
                      id="roomIds"
                      value={bookingForm.roomIds?.join(',') || ''}
                      onChange={(e) =>
                        handleInputChange('roomIds', e.target.value.split(',').map((id) => id.trim()).filter(id => id))
                      }
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label htmlFor="checkInDate" className="form-label">
                      Check-In Date
                    </label>
                    <input
                      type="date"
                      className="form-control"
                      id="checkInDate"
                      value={bookingForm.checkInDate || ''}
                      onChange={(e) => handleInputChange('checkInDate', e.target.value)}
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label htmlFor="checkOutDate" className="form-label">
                      Check-Out Date
                    </label>
                    <input
                      type="date"
                      className="form-control"
                      id="checkOutDate"
                      value={bookingForm.checkOutDate || ''}
                      onChange={(e) => handleInputChange('checkOutDate', e.target.value)}
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label htmlFor="numberOfGuests" className="form-label">
                      Number of Guests
                    </label>
                    <input
                      type="number"
                      className="form-control"
                      id="numberOfGuests"
                      value={bookingForm.numberOfGuests || 1}
                      onChange={(e) => handleInputChange('numberOfGuests', parseInt(e.target.value) || 1)}
                      min="1"
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label htmlFor="specialRequests" className="form-label">
                      Special Requests
                    </label>
                    <textarea
                      className="form-control"
                      id="specialRequests"
                      value={bookingForm.specialRequests || ''}
                      onChange={(e) => handleInputChange('specialRequests', e.target.value)}
                    ></textarea>
                  </div>
                  <div className="mb-3">
                    <label htmlFor="discountCode" className="form-label">
                      Discount Code
                    </label>
                    <input
                      type="text"
                      className="form-control"
                      id="discountCode"
                      value={bookingForm.discountCode || ''}
                      onChange={(e) => handleInputChange('discountCode', e.target.value)}
                    />
                  </div>
                  <div className="mb-3">
                    <label htmlFor="bookingStatus" className="form-label">
                      Booking Status
                    </label>
                    <select
                      className="form-control"
                      id="bookingStatus"
                      value={bookingForm.bookingStatus || ''}
                      onChange={(e) => handleInputChange('bookingStatus', e.target.value)}
                      required
                    >
                      <option value="">Select Status</option>
                      <option value="PENDING">PENDING</option>
                      <option value="PAID">PAID</option>
                      <option value="CHECKED_IN">CHECKED_IN</option>
                      <option value="CHECKED_OUT">CHECKED_OUT</option>
                      <option value="CANCELLED">CANCELLED</option>
                      <option value="REFUNDED">REFUNDED</option>
                    </select>
                  </div>
                  <div className="d-flex justify-content-between">
                    <button 
                      type="button" 
                      className="btn btn-secondary"
                      onClick={() => {
                        setShowEditModal(false);
                        resetBookingForm();
                      }}
                    >
                      Cancel
                    </button>
                    <button type="submit" className="btn btn-primary">
                      Save Changes
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
        {showEditModal && <div className="modal-backdrop fade show"></div>}
      </div>
    );
  };

  return (
    <BookingLogic>{(props) => <BookingContent {...props} />}</BookingLogic>
  );
};

export default BookingUI;