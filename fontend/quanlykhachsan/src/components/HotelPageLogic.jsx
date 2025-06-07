import React, { useState, useEffect } from 'react';
import hotelRoute from '../routes/HotelRoute';
import HotelPageUI from './HotelPageUI';

const HotelPageLogic = () => {
  const [hotels, setHotels] = useState([]);
  const [formData, setFormData] = useState({
    name: '',
    city: '',
    district: '',
    ward: '',
    street: '',
    houseNumber: '',
    hotelRating: '',
  });
  const [files, setFiles] = useState([]);
  const [editingId, setEditingId] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  const [searchParams, setSearchParams] = useState({
    city: '',
    district: '',
    ward: '',
    street: '',
    houseNumber: '',
  });
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);

  useEffect(() => {
    fetchHotels();
  }, [currentPage]);

  const fetchHotels = async () => {
    setLoading(true);
    try {
      console.log('Đang lấy khách sạn từ:', `${hotelRoute.API_BASE_URL}/hotels/all?page=${currentPage}&size=8`);
      const response = await hotelRoute.getAllHotels(currentPage, 8);
      console.log('Phản hồi API:', response);

      const hotelList = response.data?.content || [];
      const pageInfo = response.data?.page || {};
      if (Array.isArray(hotelList)) {
        setHotels(hotelList);
        setTotalPages(pageInfo.totalPages || 1);
        console.log('Danh sách khách sạn:', hotelList);
      } else {
        console.error('Dữ liệu không phải mảng:', hotelList);
        setHotels([]);
        setTotalPages(1);
        setError('Dữ liệu khách sạn không đúng định dạng');
      }
      setError(null);
    } catch (err) {
      console.error('Lỗi khi lấy khách sạn:', err.response?.data || err.message);
      setError('Không thể lấy danh sách khách sạn: ' + (err.response?.data?.message || err.message));
      setHotels([]);
      setTotalPages(1);
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleFileChange = (e) => {
    setFiles(Array.from(e.target.files));
  };

  // *** SỬA HÀM NÀY ***
const handleAddHotel = async (e) => {
  e.preventDefault();
  setLoading(true);
  
  try {
    // Kiểm tra name có tồn tại không
    if (!formData.name || formData.name.trim() === '') {
      setError('Tên khách sạn không được để trống');
      setLoading(false);
      return;
    }
    
    // THAY ĐỔI: Sử dụng camelCase để khớp với HotelRoute.js
    const mappedDto = {
      name: formData.name.trim(), // Dùng 'name' thay vì 'hotel_name'
      city: formData.city || '',
      district: formData.district || '',
      ward: formData.ward || '',
      street: formData.street || '',
      houseNumber: formData.houseNumber || '', // camelCase
      hotelRating: formData.hotelRating ? parseInt(formData.hotelRating) : null
    };
    
    console.log('Dữ liệu gửi lên API:', mappedDto);
    
    await hotelRoute.addHotel(mappedDto, files);
    setFormData({ name: '', city: '', district: '', ward: '', street: '', houseNumber: '', hotelRating: '' });
    setFiles([]);
    setCurrentPage(1);
    fetchHotels();
    setError(null);
  } catch (err) {
    console.error('Lỗi khi thêm khách sạn:', err.response?.data || err.message);
    setError('Không thể thêm khách sạn: ' + (err.response?.data?.message || err.message));
  } finally {
    setLoading(false);
  }
};

  const handleEditHotel = async (hotelId) => {
    try {
      const hotel = await hotelRoute.getHotelById(hotelId);
      console.log('Khách sạn để chỉnh sửa:', hotel);
      setFormData({
        name: hotel.hotelName || '',
        city: hotel.city || '',
        district: hotel.district || '',
        ward: hotel.ward || '',
        street: hotel.street || '',
        houseNumber: hotel.houseNumber || '',
        hotelRating: hotel.hotelRating?.toString() || '',
      });
      setEditingId(hotelId);
      setError(null);
    } catch (err) {
      console.error('Lỗi khi lấy chi tiết khách sạn:', err.response?.data || err.message);
      setError('Không thể lấy chi tiết khách sạn: ' + (err.response?.data?.message || err.message));
    }
  };

  // *** SỬA HÀM NÀY ***
  const handleUpdateHotel = async (e) => {
    e.preventDefault();
    setLoading(true);
    
    try {
      if (!formData.name || formData.name.trim() === '') {
        setError('Tên khách sạn không được để trống');
        setLoading(false);
        return;
      }
      
      // Mapping theo đúng tên cột trong database (snake_case)
      const mappedDto = {
        hotel_name: formData.name.trim(),
        city: formData.city || '',
        district: formData.district || '',
        ward: formData.ward || '',
        street: formData.street || '',
        house_number: formData.houseNumber || '',
        hotel_rating: formData.hotelRating ? parseInt(formData.hotelRating) : null
      };
      
      console.log('Dữ liệu cập nhật gửi lên API:', mappedDto);
      
      await hotelRoute.updateHotel(editingId, mappedDto, files);
      setFormData({ name: '', city: '', district: '', ward: '', street: '', houseNumber: '', hotelRating: '' });
      setFiles([]);
      setEditingId(null);
      fetchHotels();
      setError(null);
    } catch (err) {
      console.error('Lỗi khi cập nhật khách sạn:', err.response?.data || err.message);
      setError('Không thể cập nhật khách sạn: ' + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteHotel = async (hotelId) => {
    setLoading(true);
    try {
      await hotelRoute.deleteHotel(hotelId);
      fetchHotels();
      setError(null);
    } catch (err) {
      console.error('Lỗi khi xóa khách sạn:', err.response?.data || err.message);
      setError('Không thể xóa khách sạn: ' + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  const handleApproveHotel = async (hotelId) => {
    setLoading(true);
    try {
      await hotelRoute.approveHotel(hotelId);
      fetchHotels();
      setError(null);
    } catch (err) {
      console.error('Lỗi khi phê duyệt khách sạn:', err.response?.data || err.message);
      setError('Không thể phê duyệt khách sạn: ' + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  const handleRejectHotel = async (hotelId) => {
    setLoading(true);
    try {
      await hotelRoute.rejectHotel(hotelId);
      fetchHotels();
      setError(null);
    } catch (err) {
      console.error('Lỗi khi từ chối khách sạn:', err.response?.data || err.message);
      setError('Không thể từ chối khách sạn: ' + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  const handleSearchHotels = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await hotelRoute.searchHotels(searchParams, currentPage, 8);
      console.log('Phản hồi tìm kiếm API:', response);
      const hotelList = response.data?.content || [];
      const pageInfo = response.data?.page || {};
      if (Array.isArray(hotelList)) {
        setHotels(hotelList);
        setTotalPages(pageInfo.totalPages || 1);
        console.log('Danh sách khách sạn tìm kiếm:', hotelList);
      } else {
        console.error('Dữ liệu tìm kiếm không phải mảng:', hotelList);
        setHotels([]);
        setTotalPages(1);
        setError('Dữ liệu tìm kiếm không đúng định dạng');
      }
      setError(null);
    } catch (err) {
      console.error('Lỗi khi tìm kiếm khách sạn:', err.response?.data || err.message);
      setError('Không thể tìm kiếm khách sạn: ' + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  const handleSearchInputChange = (e) => {
    const { name, value } = e.target;
    setSearchParams((prev) => ({ ...prev, [name]: value }));
    setCurrentPage(1);
  };

  const handleCancelEdit = () => {
    setFormData({ name: '', city: '', district: '', ward: '', street: '', houseNumber: '', hotelRating: '' });
    setFiles([]);
    setEditingId(null);
  };

  const handlePageChange = (newPage) => {
    if (newPage >= 1 && newPage <= totalPages) {
      setCurrentPage(newPage);
    }
  };

  return (
    <HotelPageUI
      hotels={hotels}
      formData={formData}
      files={files}
      editingId={editingId}
      error={error}
      loading={loading}
      handleInputChange={handleInputChange}
      handleFileChange={handleFileChange}
      handleAddHotel={handleAddHotel}
      handleEditHotel={handleEditHotel}
      handleUpdateHotel={handleUpdateHotel}
      handleDeleteHotel={handleDeleteHotel}
      handleApproveHotel={handleApproveHotel}
      handleRejectHotel={handleRejectHotel}
      handleSearchHotels={handleSearchHotels}
      handleSearchInputChange={handleSearchInputChange}
      handleCancelEdit={handleCancelEdit}
      searchParams={searchParams}
      currentPage={currentPage}
      totalPages={totalPages}
      handlePageChange={handlePageChange}
    />
  );
};

export default HotelPageLogic;