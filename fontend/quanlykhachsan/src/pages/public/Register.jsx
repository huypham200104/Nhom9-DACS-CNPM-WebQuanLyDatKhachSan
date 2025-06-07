import React, { useState } from 'react';
import Navbar from './Navbar';
import customerRoute from '../../routes/CustomerRoute';
import { useNavigate } from 'react-router-dom'; // Import useNavigate
import './HomePage.css';
import third_background from './images/third_background.jpg'; // Import hình nền nếu cần

const Register = () => {
  const [formData, setFormData] = useState({
    customerName: '',
    phone: '',
    email: '',
    password: '',
    confirmPassword: ''
  });

  const [successMsg, setSuccessMsg] = useState('');
  const [errorMsg, setErrorMsg] = useState('');
  
  const navigate = useNavigate(); // Tạo đối tượng navigate

  const handleChange = (e) => {
    setFormData(prev => ({
      ...prev,
      [e.target.name]: e.target.value
    }));
  };

  const handleRegister = async () => {
    setErrorMsg('');
    setSuccessMsg('');

    if (formData.password !== formData.confirmPassword) {
      setErrorMsg("Mật khẩu xác nhận không khớp.");
      return;
    }

    const customer = {
      customerName: formData.customerName,
      phone: formData.phone,
      email: formData.email,
      password: formData.password,
      createdAt: new Date().toISOString().split('T')[0]
    };

    try {
      const response = await customerRoute.registerCustomer(customer);
      setSuccessMsg(response.message || "Đăng ký thành công!");
      
      // Chuyển hướng sau khi đăng ký thành công
      setTimeout(() => {
        navigate('/login'); // Chuyển hướng đến trang đăng nhập
      }, 2000); // Sau 2 giây chuyển hướng
    } catch (err) {
      // Nếu API trả về lỗi với format ApiResponse
      if (err.response && err.response.data) {
        const { message, errors } = err.response.data;

        if (errors && Array.isArray(errors)) {
          // Hiển thị lỗi đầu tiên
          setErrorMsg(errors[0].message || "Có lỗi xảy ra.");
        } else {
          setErrorMsg(message || "Có lỗi xảy ra.");
        }
      } else {
        setErrorMsg("Không thể kết nối tới máy chủ.");
      }
    }
  };

  return (
    <div
      className="d-flex align-items-center justify-content-center min-vh-100"
      style={{
        backgroundImage: `url(${third_background})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat',
        padding: '20px'
      }}
    >
      <div className="card p-4 shadow" style={{ maxWidth: '400px', width: '100%' }}>
        <Navbar />
        <h2 className="text-center mb-3">Create Account</h2>

        <input
          type="text"
          className="form-control mb-2"
          placeholder="Full Name"
          name="customerName"
          value={formData.customerName}
          onChange={handleChange}
        />
        <input
          type="text"
          className="form-control mb-2"
          placeholder="Phone"
          name="phone"
          value={formData.phone}
          onChange={handleChange}
        />
        <input
          type="email"
          className="form-control mb-2"
          placeholder="Email"
          name="email"
          value={formData.email}
          onChange={handleChange}
        />
        <input
          type="password"
          className="form-control mb-2"
          placeholder="Password"
          name="password"
          value={formData.password}
          onChange={handleChange}
        />
        <input
          type="password"
          className="form-control mb-3"
          placeholder="Confirm Password"
          name="confirmPassword"
          value={formData.confirmPassword}
          onChange={handleChange}
        />

        {errorMsg && <div className="alert alert-danger">{errorMsg}</div>}
        {successMsg && <div className="alert alert-success">{successMsg}</div>}

        <button className="btn btn-dark w-100 mb-3" onClick={handleRegister}>
          Register
        </button>

        <p className="text-center text-muted small">
          Already have an account? <a href="/login">Sign in</a>
        </p>
      </div>
    </div>
  );
};

export default Register;
