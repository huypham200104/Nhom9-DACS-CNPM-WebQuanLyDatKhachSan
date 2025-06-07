import React from "react";
import { useLocation, useNavigate, NavLink } from "react-router-dom";
import authenticationRoute from "../../routes/AuthRoute";
import "./Navbar.css";

const Navbar = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const hideMenu =
    location.pathname.startsWith("/staff") ||
    location.pathname.startsWith("/admin") ||
    location.pathname === "/login" ||
    location.pathname === "/register";
  const userEmail = sessionStorage.getItem("userEmail");

  const handleLogout = async () => {
    try {
      const response = await authenticationRoute.logout();

      if (response.message !== "Logout successful") {
        console.error(
          `🚨 Logout failed: ${response?.message || "No response from server"}`
        );
        return;
      }

      sessionStorage.removeItem("userId");
      sessionStorage.removeItem("userEmail");
      sessionStorage.removeItem("customerId");

      navigate("/");
    } catch (error) {
      console.error("Logout error:", error.response?.data || error.message);
      alert(
        `Logout error: ${
          error.response?.data?.message || error.message || "Unknown error!"
        }`
      );
    }
  };

  const scrollToFooter = () => {
    const footerElement = document.getElementById("footer");
    if (footerElement) {
      footerElement.scrollIntoView({ behavior: "smooth" });
    }
  };

  return (
    <nav className="navbar">
      <div className="logo">
        <NavLink to="/">
          Booking<span>đi</span>
        </NavLink>
      </div>

      {!hideMenu && (
        <ul>
          <li>
            <NavLink to="/" end className={({ isActive }) => isActive ? 'active' : ''}>
              Home
            </NavLink>
          </li>
          <li>
            <NavLink to="/rooms" className={({ isActive }) => isActive ? 'active' : ''}>
              Our Rooms
            </NavLink>
          </li>
          <li>
            <a href="#footer" className="nav-link-button" onClick={scrollToFooter}>
              About Us
            </a>
          </li>

          {userEmail ? (
            <>
              <li className="user-menu">
                <span className="user-email">{userEmail}</span>
                <ul className="dropdown">
                  <li>
                    <NavLink to="/booking-history" className={({ isActive }) => isActive ? 'active' : ''}>
                      Booking History
                    </NavLink>
                  </li>
                  <li>
                    <NavLink to="/edit-profile" className={({ isActive }) => isActive ? 'active' : ''}>
                      Edit Profile
                    </NavLink>
                  </li>
                </ul>
              </li>
              <li>
                <a
                  href="/logout"
                  onClick={(e) => {
                    e.preventDefault();
                    handleLogout();
                  }}
                >
                  Logout
                </a>
              </li>
            </>
          ) : (
            <>
              <li>
                <NavLink to="/login" className={({ isActive }) => isActive ? 'active' : ''}>
                  Sign In
                </NavLink>
              </li>
              <li>
                <NavLink to="/register" className={({ isActive }) => isActive ? 'active' : ''}>
                  Sign Up
                </NavLink>
              </li>
            </>
          )}
        </ul>
      )}
    </nav>
  );
};

export default Navbar;