import React, { useState, useEffect } from 'react';
import { Table, Alert, Spinner, Container } from 'react-bootstrap';
import feedbackRoute from '../routes/FeedbackRoute'; // Adjust the path as needed

const FeedbackHotel = () => {
  const [feedbacks, setFeedbacks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const hotelId = sessionStorage.getItem('hotelId'); // Retrieve hotelId from sessionStorage

  useEffect(() => {
    const fetchFeedbacks = async () => {
      try {
        setLoading(true);
        if (!hotelId) {
          throw new Error('No hotel ID found in session storage.');
        }
        const data = await feedbackRoute.getFeedbacksByHotelId(hotelId);
        setFeedbacks(data);
        setError(null);
      } catch (err) {
        setError(err.message || 'Failed to fetch feedback. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchFeedbacks();
  }, [hotelId]); // Re-run effect if hotelId changes

  const formatDateTime = (dateTime) => {
    return new Date(dateTime).toLocaleString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  return (
    <Container className="mt-4">
      <h2>Hotel Feedback</h2>
      {loading && (
        <div className="text-center my-4">
          <Spinner animation="border" variant="primary" />
        </div>
      )}
      {error && <Alert variant="danger">{error}</Alert>}
      {!loading && !error && !hotelId && (
        <Alert variant="warning">No hotel ID found in session storage.</Alert>
      )}
      {!loading && !error && hotelId && feedbacks.length === 0 && (
        <Alert variant="info">No feedback available for this hotel.</Alert>
      )}
      {!loading && feedbacks.length > 0 && (
        <Table striped bordered hover responsive>
          <thead>
            <tr>
              <th>Feedback ID</th>
              <th>Content</th>
              <th>Rating</th>
              <th>Created At</th>
              <th>Updated At</th>
              <th>Booking ID</th>
              <th>Customer ID</th>
            </tr>
          </thead>
          <tbody>
            {feedbacks.map((feedback) => (
              <tr key={feedback.feedbackId}>
                <td>{feedback.feedbackId}</td>
                <td>{feedback.content}</td>
                <td>{feedback.rating}</td>
                <td>{formatDateTime(feedback.createdAt)}</td>
                <td>{formatDateTime(feedback.updatedAt)}</td>
                <td>{feedback.bookingId}</td>
                <td>{feedback.customerId}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}
    </Container>
  );
};

export default FeedbackHotel;