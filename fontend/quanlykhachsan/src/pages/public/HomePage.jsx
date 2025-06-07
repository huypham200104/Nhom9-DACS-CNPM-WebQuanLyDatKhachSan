import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './HomePage.css';
import Navbar from './Navbar';
import Footer from './Footer';
import ProminentProvince from '../customer/ProminentProvince';
import Search from './Search';
// Import ảnh trực tiếp
import mainBackground from './images/main_background.jpg';
import secondBackground from './images/second_background.jpg';
import thirdBackground from './images/third_background.jpg';

const HomePage = () => {
  const navigate = useNavigate();
  const [currentBackground, setCurrentBackground] = useState(0);
  
  // Array chứa các background images - sử dụng imported images
  const backgrounds = [
    mainBackground,
    secondBackground,
    thirdBackground
  ];

  // Effect để thay đổi background mỗi 5 giây
  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentBackground(prev => (prev + 1) % backgrounds.length);
    }, 3000); // 5000ms = 5 giây

    return () => clearInterval(interval); // Cleanup khi component unmount
  }, [backgrounds.length]);

  // Hàm xử lý khi search từ HomePage
  const handleSearchFromHomePage = async (searchParams) => {
    // Chuyển hướng đến trang RoomCustomer với search params
    navigate('/rooms', { 
      state: { 
        searchParams: searchParams,
        shouldSearch: true // Flag để biết là cần thực hiện search
      } 
    });
  };

  return (
    <div className="App d-flex flex-column min-vh-100">
      {/* Hero section */}
      <div 
        className="hero-section"
        style={{ backgroundImage: `url(${backgrounds[currentBackground]})` }}
      >
        <Navbar />
        <div className="overlay"></div>
        <div className="hero-content">
          <div className="top-text">MORE THAN A HOTEL... AN EXPERIENCE</div>
          <h1 className="main-heading">
            Hotel for the<br />
            whole<br />
            family, all<br />
            year round.
          </h1>
        </div>
      </div>

      {/* Search section */}
      <div className="search-container">
        <Search onSearch={handleSearchFromHomePage} />
      </div>

      {/* Hiển thị các địa điểm phổ biến */}
      <div className="container my-5">
        <ProminentProvince />
      </div>

      <div className="flex-grow-1"></div>
      <Footer />
    </div>
  );
};

export default HomePage;