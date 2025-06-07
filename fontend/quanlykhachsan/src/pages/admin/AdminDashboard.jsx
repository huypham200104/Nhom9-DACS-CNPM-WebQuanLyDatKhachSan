import React from "react";
import { Link, Routes, Route, useLocation, useNavigate } from "react-router-dom";
import authenticationRoute from "../../routes/AuthRoute";
import "./AdminDashboard.css";
import HotelPageLogic from "../../components/HotelPageLogic";
import HotelPageUI from "../../components/HotelPageUI";
import AmenityUI from "../../components/AmenityUI";
import StaffHotelUI from "../../components/StaffHotelUI";
import CustomerForAdmin from "../../components/CustomerForAdmin";
import RoomTypeUI from "../../components/RoomTypeUI";

// Admin Home Component
const AdminHome = () => {
  const userEmail = sessionStorage.getItem("userEmail") || "Admin";
  const userId = sessionStorage.getItem("userId");
  
  return (
    <div className="admin-home">
      <div className="welcome-section">
        <div className="welcome-card">
          <div className="welcome-header">
            <div className="admin-avatar">
              <i className="fas fa-user-shield"></i>
            </div>
            <div className="welcome-content">
              <h1>Welcome back, Administrator!</h1>
              <div className="admin-info">
                <span className="admin-email">
                  <i className="fas fa-envelope"></i>
                  {userEmail}
                </span>
                {userId && (
                  <span className="admin-id">
                    <i className="fas fa-id-badge"></i>
                    ID: {userId}
                  </span>
                )}
              </div>
              <p className="welcome-text">
                Manage your hotel system efficiently with comprehensive admin controls.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

const AdminDashboard = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const userEmail = sessionStorage.getItem("userEmail") || "Admin";

  const handleLogout = async () => {
    try {
      const response = await authenticationRoute.logout();

      if (response.message !== "Logout successful") {
        console.error(`🚨 Logout thất bại: ${response?.message || "Không có phản hồi từ server"}`);
        return;
      }

      sessionStorage.removeItem("userId");
      sessionStorage.removeItem("userEmail");

      navigate("/");
    } catch (error) {
      console.error("Lỗi đăng xuất:", error.response?.data || error.message);
      alert(`Lỗi đăng xuất: ${error.response?.data?.message || error.message || "Đã xảy ra lỗi không xác định!"}`);
    }
  };

  return (
    <div className="admin-dashboard">
      <div className="admin-sidebar">
        <div className="sidebar-header">
          <div className="logo">
            <i className="fas fa-shield-alt"></i>
            <h2>Admin Portal</h2>
          </div>
          <p className="subtitle">Hotel Management System</p>
          <div className="admin-badge">
            <i className="fas fa-crown"></i>
            <span>Administrator</span>
          </div>
        </div>

        <nav className="sidebar-nav">
          <ul>
            <li>
              <Link to="/admin" className={location.pathname === "/admin" ? "active" : ""}>
                <i className="fas fa-tachometer-alt"></i>
                <span>Dashboard</span>
              </Link>
            </li>
            <li>
              <Link
                to="/admin/hotels"
                className={location.pathname.includes("/admin/hotels") ? "active" : ""}
              >
                <i className="fas fa-hotel"></i>
                <span>Hotels</span>
              </Link>
            </li>
            <li>
              <Link
                to="/admin/amenities"
                className={location.pathname.includes("/admin/amenities") ? "active" : ""}
              >
                <i className="fas fa-concierge-bell"></i>
                <span>Amenities</span>
              </Link>
            </li>
            <li>
              <Link
                to="/admin/room-types"
                className={location.pathname.includes("/admin/room-types") ? "active" : ""}
              >
                <i className="fas fa-bed"></i>
                <span>Room Types</span>
              </Link>
            </li>
            <li>
              <Link
                to="/admin/staff"
                className={location.pathname.includes("/admin/staff") ? "active" : ""}
              >
                <i className="fas fa-users"></i>
                <span>Staff</span>
              </Link>
            </li>
            <li>
              <Link
                to="/admin/customers"
                className={location.pathname.includes("/admin/customers") ? "active" : ""}
              >
                <i className="fas fa-user-friends"></i>
                <span>Customers</span>
              </Link>
            </li>
          </ul>
          
          <div className="sidebar-footer">
            <div className="user-info">
              <div className="user-avatar">
                <i className="fas fa-user"></i>
              </div>
              <div className="user-details">
                <span className="user-name">{userEmail}</span>
                <span className="user-role">Administrator</span>
              </div>
            </div>
            <button onClick={handleLogout} className="logout-btn">
              <i className="fas fa-sign-out-alt"></i>
              <span>Logout</span>
            </button>
          </div>
        </nav>
      </div>

      <div className="admin-main-content">
        <Routes>
          <Route path="" element={<AdminHome />} />
          <Route
            path="hotels"
            element={<HotelPageLogic render={(props) => <HotelPageUI {...props} />} />}
          />
          <Route path="amenities" element={<AmenityUI />} />
          <Route path="room-types" element={<RoomTypeUI />} />
          <Route path="staff" element={<StaffHotelUI />} />
          <Route path="customers" element={<CustomerForAdmin />} />
        </Routes>
      </div>
    </div>
  );
};

export default AdminDashboard;