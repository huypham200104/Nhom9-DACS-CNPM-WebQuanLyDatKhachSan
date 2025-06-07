import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css'; // Import Bootstrap CSS
import 'bootstrap-icons/font/bootstrap-icons.css'; // Import Bootstrap Icons

const Footer = () => {
  return (
    <footer id="footer" className="bg-dark text-white py-4">
      <div className="container">
        <div className="row justify-content-center">
          {/* Hotel Info */}
          <div className="col-md-4 mb-3 text-center">
            <h5 className="fw-bold mb-3">Booking đi</h5>
            <p>
              Địa chỉ: 1 Đường A, phường B, quận C, Tp.Hồ Chí Minh<br />
              Email: ngochuy200104@gmail.com<br />
              Điện thoại: (+84) 123 456 789
            </p>
          </div>

          {/* Social Media Links */}
          <div className="col-md-4 mb-3 text-center">
            <h5 className="fw-bold mb-3">Kết nối với chúng tôi</h5>
            <ul className="list-unstyled d-flex justify-content-center gx-3">
              <li>
                <a href="https://facebook.com" className="text-white text-decoration-none mx-2">
                  <i className="bi bi-facebook"></i> Facebook
                </a>
              </li>
              <li>
                <a href="https://instagram.com" className="text-white text-decoration-none mx-2">
                  <i className="bi bi-instagram"></i> Instagram
                </a>
              </li>
              <li>
                <a href="https://twitter.com" className="text-white text-decoration-none mx-2">
                  <i className="bi bi-twitter"></i> Twitter
                </a>
              </li>
            </ul>
          </div>
        </div>

        {/* Copyright */}
        <div className="text-center mt-3 border-top pt-3">
          <p>© {new Date().getFullYear()} Family Hotel. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;