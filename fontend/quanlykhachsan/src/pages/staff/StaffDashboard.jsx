import React from "react";
import { Link, Routes, Route, useLocation, useNavigate } from "react-router-dom";
import authenticationRoute from "../../routes/AuthRoute";
import "./StaffDashboard.css";
import HotelPageUI from "../../components/HotelPageUI";
import RoomPageUI from "../../components/RoomPageUI";
import DiscountUI from "../../components/DiscountUI";
import BookingUI from "../../components/BookingUI";
import FeedbackHotel from "../../components/FeedBackHotel"; // Import the FeedBackHotel component

// Home Component for Staff Dashboard
const StaffHome = () => {
  const userId = sessionStorage.getItem("userId");
  const userEmail = sessionStorage.getItem("userEmail");
  
  return (
    <div className="staff-home">
      <div className="welcome-section">
        <div className="welcome-card">
          <div className="welcome-header">
            <h1>Welcome back!</h1>
            <p className="user-info">
              <span className="user-id">User ID: {userId || "N/A"}</span>
              {userEmail && <span className="user-email">{userEmail}</span>}
            </p>
          </div>
          <div className="welcome-content">
            <p>Ready to manage your hotel operations efficiently today.</p>
          </div>
        </div>
      </div>
      
      <div className="dashboard-stats">
        <div className="stat-card">
          <div className="stat-icon rooms">
            <i className="fas fa-bed"></i>
          </div>
          <div className="stat-info">
            <h3>Rooms</h3>
            <p>Manage room inventory</p>
          </div>
        </div>
        
        <div className="stat-card">
          <div className="stat-icon discounts">
            <i className="fas fa-percent"></i>
          </div>
          <div className="stat-info">
            <h3>Discounts</h3>
            <p>Manage promotional offers</p>
          </div>
        </div>
        
        <div className="stat-card">
          <div className="stat-icon bookings">
            <i className="fas fa-calendar-check"></i>
          </div>
          <div className="stat-info">
            <h3>Bookings</h3>
            <p>Handle reservations</p>
          </div>
        </div>
        
        <div className="stat-card">
          <div className="stat-icon feedback">
            <i className="fas fa-comment"></i>
          </div>
          <div className="stat-info">
            <h3>Feedback</h3>
            <p>View customer feedback</p>
          </div>
        </div>
      </div>
    </div>
  );
};

const StaffDashboard = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      const response = await authenticationRoute.logout();

      if (response.message !== "Logout successful") {
        console.error(`🚨 Logout thất bại: ${response?.message || "Không có phản hồi từ server"}`);
        alert(`Lỗi đăng xuất: ${response?.message || "Không thể kết nối đến server"}`);
        return;
      }

      // Clear sessionStorage
      sessionStorage.removeItem("userId");
      sessionStorage.removeItem("userEmail");
      sessionStorage.removeItem("hotelId");

      navigate("/");
    } catch (error) {
      console.error("Lỗi đăng xuất:", error.response?.data || error.message);
      alert(`Lỗi đăng xuất: ${error.response?.data?.message || error.message || "Đã xảy ra lỗi không xác định!"}`);
    }
  };

  return (
    <div className="staff-dashboard">
      <div className="staff-sidebar">
        <div className="sidebar-header">
          <div className="logo">
            <i className="fas fa-hotel"></i>
            <h2>Staff Portal</h2>
          </div>
          <p className="subtitle">Hotel Management System</p>
        </div>

        <nav className="sidebar-nav">
          <ul>
            <li>
              <Link to="/staff" className={location.pathname === "/staff" ? "active" : ""}>
                <i className="fas fa-home"></i>
                <span>Dashboard</span>
              </Link>
            </li>
            <li>
              <Link to="/staff/rooms" className={location.pathname.includes("/staff/rooms") ? "active" : ""}>
                <i className="fas fa-bed"></i>
                <span>Rooms</span>
              </Link>
            </li>
            <li>
              <Link to="/staff/discounts" className={location.pathname.includes("/staff/discounts") ? "active" : ""}>
                <i className="fas fa-percent"></i>
                <span>Discounts</span>
              </Link>
            </li>
            <li>
              <Link to="/staff/bookings" className={location.pathname.includes("/staff/bookings") ? "active" : ""}>
                <i className="fas fa-calendar-check"></i>
                <span>Bookings</span>
              </Link>
            </li>
            <li>
              <Link to="/staff/feedback" className={location.pathname.includes("/staff/feedback") ? "active" : ""}>
                <i className="fas fa-comment"></i>
                <span>Feedback</span>
              </Link>
            </li>
          </ul>
          
          <div className="sidebar-footer">
            <button onClick={handleLogout} className="logout-btn">
              <i className="fas fa-sign-out-alt"></i>
              <span>Logout</span>
            </button>
          </div>
        </nav>
      </div>

      <div className="staff-main-content">
        <Routes>
          <Route path="" element={<StaffHome />} />
          <Route path="hotels" element={<HotelPageUI />} />
          <Route path="rooms" element={<RoomPageUI />} />
          <Route path="discounts" element={<DiscountUI />} />
          <Route path="bookings" element={<BookingUI />} />
          <Route path="feedback" element={<FeedbackHotel />} />
        </Routes>
      </div>
    </div>
  );
};

export default StaffDashboard;