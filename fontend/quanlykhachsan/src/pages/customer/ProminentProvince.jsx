import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import roomRoute from '../../routes/RoomRoute'; // Adjust the path based on your project structure
import hanoiImg from '../public/images/hanoi.jpg';
import saigonImg from '../public/images/saigon.jpg';
import vungtauImg from '../public/images/vungtau.jpg';
import danangImg from '../public/images/danang.jpg';
import dalatImg from '../public/images/dalat.jpg';

const provinces = [
  { name: 'Hồ Chí Minh', image: saigonImg },
  { name: 'Đà Nẵng', image: danangImg },
  { name: 'Hà Nội', image: hanoiImg },
  { name: 'Nha Trang', image: vungtauImg },
  { name: 'Đà Lạt', image: dalatImg },
];

const ProminentProvince = () => {
  const navigate = useNavigate();
  const [isSearching, setIsSearching] = useState(false);
  const [searchingCity, setSearchingCity] = useState('');

  const handleCityClick = async (cityName) => {
    if (isSearching) return; // Prevent multiple clicks while searching
    
    setIsSearching(true);
    setSearchingCity(cityName);

    try {
      // Call getRoomsByCity similar to Search component
      const response = await roomRoute.getRoomsByCity(
        cityName.trim(),
        0, // page mặc định
        5  // size mặc định
      );

      // Normalize roomsData
      const roomsData = response.content || response || [];

      // Navigate to RoomCustomer with search results
      navigate('/rooms', {
        state: {
          searchParams: {
            city: cityName.trim(),
            checkIn: '',
            checkOut: '',
            adults: 1,
            children: 0,
          },
          roomsData: {
            content: Array.isArray(roomsData) ? roomsData : [],
            totalPages: response.totalPages || 1
          },
          shouldSearch: true,
          searchType: 'city' // Search type for city-only search
        },
      });
    } catch (error) {
      console.error('City search error:', error);
      alert('Failed to search rooms in ' + cityName + ': ' + (error.response?.data?.message || error.message));
    } finally {
      setIsSearching(false);
      setSearchingCity('');
    }
  };

  return (
    <div className="px-3">
      <h2 className="fw-bold mb-2">Trending destinations</h2>
      <p className="text-muted mb-4">Travellers searching for Vietnam also booked these</p>
      
      <div className="row g-3">
        {/* Ảnh lớn chiếm 2/3 và 1 ảnh nhỏ */}
        <div className="col-md-8">
          <ProvinceCard 
            {...provinces[0]} 
            height="300px" 
            onClick={() => handleCityClick(provinces[0].name)}
            isLoading={isSearching && searchingCity === provinces[0].name}
          />
        </div>
        <div className="col-md-4">
          <ProvinceCard 
            {...provinces[1]} 
            height="300px" 
            onClick={() => handleCityClick(provinces[1].name)}
            isLoading={isSearching && searchingCity === provinces[1].name}
          />
        </div>

        {/* 3 ảnh dưới */}
        {provinces.slice(2).map((province, idx) => (
          <div className="col-md-4" key={idx}>
            <ProvinceCard 
              {...province} 
              height="200px" 
              onClick={() => handleCityClick(province.name)}
              isLoading={isSearching && searchingCity === province.name}
            />
          </div>
        ))}
      </div>
    </div>
  );
};

const ProvinceCard = ({ name, image, height, onClick, isLoading }) => {
  return (
    <div 
      className="position-relative w-100 rounded overflow-hidden shadow"
      style={{ 
        height,
        cursor: isLoading ? 'not-allowed' : 'pointer',
        transition: 'transform 0.2s ease, box-shadow 0.2s ease',
        opacity: isLoading ? 0.7 : 1
      }}
      onClick={onClick}
      onMouseEnter={(e) => {
        if (!isLoading) {
          e.currentTarget.style.transform = 'scale(1.02)';
          e.currentTarget.style.boxShadow = '0 8px 25px rgba(0,0,0,0.2)';
        }
      }}
      onMouseLeave={(e) => {
        if (!isLoading) {
          e.currentTarget.style.transform = 'scale(1)';
          e.currentTarget.style.boxShadow = '0 4px 6px rgba(0,0,0,0.1)';
        }
      }}
    >
      <img
        src={image}
        alt={name}
        className="w-100 h-100"
        style={{ objectFit: 'cover' }}
      />
      
      {/* Overlay gradient for better text readability */}
      <div className="position-absolute top-0 start-0 w-100 h-100 bg-gradient" 
           style={{ background: 'linear-gradient(45deg, rgba(0,0,0,0.3) 0%, rgba(0,0,0,0.1) 50%, rgba(0,0,0,0.2) 100%)' }}>
      </div>
      
      {/* City name */}
      <div className="position-absolute top-0 start-0 p-3">
        <div className="text-white fw-bold fs-5 bg-dark bg-opacity-50 rounded px-3 py-1">
          {name} 🇻🇳
        </div>
      </div>
      
      {/* Loading indicator */}
      {isLoading && (
        <div className="position-absolute top-50 start-50 translate-middle">
          <div className="spinner-border text-white" role="status">
            <span className="visually-hidden">Searching...</span>
          </div>
          <div className="text-white text-center mt-2 fw-bold">
            Searching...
          </div>
        </div>
      )}
      
      {/* Click hint */}
      {!isLoading && (
        <div className="position-absolute bottom-0 end-0 p-2">
          <small className="text-white bg-primary bg-opacity-75 rounded px-2 py-1">
            Click to explore
          </small>
        </div>
      )}
    </div>
  );
};

export default ProminentProvince;