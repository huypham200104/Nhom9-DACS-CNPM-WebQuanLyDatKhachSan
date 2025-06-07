import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';

import HomePage from './pages/public/HomePage';
import LoginPage from './pages/public/LoginPage';
import AdminDashboard from './pages/admin/AdminDashboard';
import Register from './pages/public/Register';
import StaffDashboard from './pages/staff/StaffDashboard';
import RoomCustomer from './pages/customer/RoomCustomer';
import Profile from './pages/customer/Profile';
import BookingCustomer from './pages/customer/BookingCustomer';
import OAuth2CallbackHandler from './components/auth/OAuth2CallbackHandler';
import PaymentCallback from './pages/customer/PaymentCallback';
import PaymentError from './pages/customer/PaymentError';
import BookingHistory from './pages/customer/BookingHistory';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/admin/*" element={<AdminDashboard />} />
        <Route path="/hotels" element={<Navigate to="/admin/hotels" replace />} />
        <Route path="/staff/*" element={<StaffDashboard />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<Register />} />
        <Route path="/rooms" element={<RoomCustomer />} />
        <Route path="/edit-profile" element={<Profile />} />
        <Route path="/booking-history" element={<BookingHistory />} />
        
        {/* Redirect root path to home page */}
        <Route path="/booking" element={<BookingCustomer />} />

        <Route path="/oauth2/callback" element={<OAuth2CallbackHandler />} />
        <Route path="/payment-callback" element={<PaymentCallback />} />
        <Route path="/payment-error" element={<PaymentError />} />
        <Route path="/" element={<HomePage />} />
      </Routes>
    </Router>
  );
}

export default App;