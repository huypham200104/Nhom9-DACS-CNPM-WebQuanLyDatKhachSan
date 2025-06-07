import React, { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import staffHotelRoute from '../../routes/StaffHotelRoute';

const OAuth2CallbackHandler = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  useEffect(() => {
    const handleOAuth2Callback = async () => {
      try {
        // Lấy parameters từ URL
        const token = searchParams.get('token');
        const role = searchParams.get('role');
        const userId = searchParams.get('userId');
        const email = searchParams.get('email');
        const error = searchParams.get('error');

        // Nếu có lỗi
        if (error) {
          console.error('OAuth2 Error:', error);
          navigate('/login?error=' + encodeURIComponent(error));
          return;
        }

        // Kiểm tra thông tin cần thiết
        if (!token || !role || !userId || !email) {
          navigate('/login?error=' + encodeURIComponent('Thiếu thông tin xác thực'));
          return;
        }

        // Lưu thông tin vào sessionStorage
        sessionStorage.setItem('userId', userId);
        sessionStorage.setItem('userEmail', email);

        // Nếu là STAFF, lấy hotelId
        if (role === 'STAFF') {
          try {
            const hotelResponse = await staffHotelRoute.getHotelIdByUserId(userId);
            if (hotelResponse?.data) {
              sessionStorage.setItem('hotelId', hotelResponse.data);
            } else {
              console.warn('Không tìm thấy hotelId cho userId:', userId);
            }
          } catch (hotelError) {
            console.error('Lỗi khi lấy hotelId:', hotelError);
            // Không chặn đăng nhập nếu lấy hotelId thất bại
          }
        }

        // Chuyển hướng dựa trên role
        switch (role) {
          case 'ADMIN':
            navigate('/admin');
            break;
          case 'STAFF':
            navigate('/staff');
            break;
          case 'CUSTOMER':
            navigate('/');
            break;
          default:
            navigate('/dashboard');
        }

      } catch (error) {
        console.error('OAuth2 callback error:', error);
        navigate('/login?error=' + encodeURIComponent('Xử lý callback thất bại'));
      }
    };

    handleOAuth2Callback();
  }, [navigate, searchParams]);

  return (
    <div className="d-flex align-items-center justify-content-center min-vh-100">
      <div className="text-center">
        <div className="spinner-border" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
        <p className="mt-3">Đang xử lý đăng nhập...</p>
      </div>
    </div>
  );
};

export default OAuth2CallbackHandler;