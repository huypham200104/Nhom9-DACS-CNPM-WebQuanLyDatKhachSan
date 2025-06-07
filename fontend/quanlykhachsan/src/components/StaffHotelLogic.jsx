import { useState, useEffect } from 'react';
import StaffHotelRoute from '../routes/StaffHotelRoute';
import HotelRoute from '../routes/HotelRoute';

const useStaffHotelLogic = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [staffList, setStaffList] = useState([]);
  const [hotels, setHotels] = useState([]);
  const [hotelsLoaded, setHotelsLoaded] = useState(false); // Thêm state để track hotels đã load

  // Fetch all staff members
  const getAllStaff = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await StaffHotelRoute.getAllStaff();
      if (response.title === 'All staff hotels') {
        const staffData = response.data || [];
        setStaffList(staffData);
      } else {
        throw new Error('Unexpected response format');
      }
      return { success: true, data: response.data };
    } catch (error) {
      console.error('Get all staff error:', error.response?.data || error.message);
      setError(error.message || 'Failed to fetch staff');
      return { success: false, message: error.message };
    } finally {
      setLoading(false);
    }
  };

  const fetchHotels = async () => {
  setLoading(true);
  setError(null);
  setHotelsLoaded(false);
  try {
    const response = await HotelRoute.getAllHotels();
    console.log('Raw API response:', response); // Debug log

    let hotelArray = [];

    if (response.title === 'All hotels' && response.data && Array.isArray(response.data.content)) {
      hotelArray = response.data.content; // Lấy mảng từ response.data.content
    } else if (Array.isArray(response)) {
      hotelArray = response;
    } else if (response.data && Array.isArray(response.data)) {
      hotelArray = response.data;
    } else {
      throw new Error('Unexpected response format');
    }

    console.log('Processed hotels:', hotelArray); // Debug log
    setHotels(hotelArray);
    setHotelsLoaded(true);
    return { success: true, data: hotelArray };
  } catch (err) {
    console.error('Hotel fetch error:', err.response?.data || err.message);
    setError(err.message || 'Failed to fetch hotels');
    setHotels([]);
    setHotelsLoaded(true); // Đặt true để giao diện không bị treo
    return { success: false, message: err.message };
  } finally {
    setLoading(false);
  }
};
  // Load data sequentially để đảm bảo hotels load trước
  const loadInitialData = async () => {
    setLoading(true);
    try {
      // Load hotels trước
      await fetchHotels();
      // Sau đó load staff
      await getAllStaff();
    } catch (error) {
      console.error('Error loading initial data:', error);
    } finally {
      setLoading(false);
    }
  };

  // Fetch staff and hotels on mount
  useEffect(() => {
    loadInitialData();
  }, []);

  // Add new staff member
  const addStaff = async (staffData) => {
    setLoading(true);
    setError(null);
    try {
      const staffPayload = {
        email: staffData.email,
        password: staffData.password,
        role: 'STAFF',
        position: staffData.position?.toUpperCase(),
        hotel: { hotelId: staffData.hotelId },
        startDate: staffData.startDate,
        endDate: staffData.endDate,
      };
      
      console.log('Adding staff with payload:', staffPayload); // Debug log
      
      const response = await StaffHotelRoute.addStaff(staffPayload);
      
      // Refresh staff list after adding
      await getAllStaff();
      
      return { success: true, data: response };
    } catch (error) {
      console.error('Add staff error:', error.response?.data || error.message);
      setError(error.response?.data?.message || 'Failed to add staff');
      return { success: false, message: error.response?.data?.message || error.message };
    } finally {
      setLoading(false);
    }
  };

  // Update existing staff member
  const updateStaff = async (staffId, staffData) => {
    setLoading(true);
    setError(null);
    try {
      const staffPayload = {
        email: staffData.email,
        role: 'STAFF',
        position: staffData.position?.toUpperCase(),
        hotel: { hotelId: staffData.hotelId },
        startDate: staffData.startDate,
        endDate: staffData.endDate,
      };
      
      // Only include password if provided (non-empty)
      if (staffData.password) {
        staffPayload.password = staffData.password;
      }
      
      console.log('Updating staff with payload:', staffPayload); // Debug log
      
      const response = await StaffHotelRoute.updateStaff(staffId, staffPayload);
      
      // Refresh staff list after updating
      await getAllStaff();
      
      return { success: true, data: response };
    } catch (error) {
      console.error('Update staff error:', error.response?.data || error.message);
      setError(error.response?.data?.message || `Failed to update staff with ID ${staffId}`);
      return { success: false, message: error.response?.data?.message || error.message };
    } finally {
      setLoading(false);
    }
  };

  // Delete staff member
  const deleteStaff = async (staffId) => {
    setLoading(true);
    setError(null);
    try {
      await StaffHotelRoute.deleteStaff(staffId);
      setStaffList(staffList.filter((s) => s.staffHotelId !== staffId));
      return { success: true };
    } catch (error) {
      console.error('Delete staff error:', error.response?.data || error.message);
      setError(error.response?.data?.message || 'Failed to delete staff');
      return { success: false, message: error.response?.data?.message || error.message };
    } finally {
      setLoading(false);
    }
  };

  return {
    loading,
    error,
    staffList,
    hotels,
    hotelsLoaded, // Export state này để UI có thể check
    addStaff,
    updateStaff,
    deleteStaff,
    setError,
    fetchHotels, // Export function này để có thể gọi lại nếu cần
  };
};

export default useStaffHotelLogic;