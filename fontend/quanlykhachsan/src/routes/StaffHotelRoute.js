import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/staff';

/**
 * Class to handle API routes for staff hotel operations.
 */
class StaffHotelRoute {
  /**
   * Add a new staff member.
   * @param {Object} staff - Staff data (StaffUserDto).
   * @returns {Promise<Object>} API response data.
   */
  async addStaff(staff) {
    try {
      const response = await axios.post(`${API_BASE_URL}/add`, staff, {
        withCredentials: true,
      });
      return response.data || {};
    } catch (error) {
      this.handleError('Error adding staff', error);
    }
  }

  /**
   * Update an existing staff member.
   * @param {string} staffId - Staff ID.
   * @param {Object} staff - Updated staff data (StaffUserDto).
   * @returns {Promise<Object>} API response data.
   */
  async updateStaff(staffId, staff) {
    try {
      const response = await axios.put(
        `${API_BASE_URL}/${encodeURIComponent(staffId)}`,
        staff,
        { withCredentials: true }
      );
      return response.data || {};
    } catch (error) {
      this.handleError(`Error updating staff with ID ${staffId}`, error);
    }
  }

  /**
   * Retrieve a staff member by ID.
   * @param {string} staffId - Staff ID.
   * @returns {Promise<Object>} API response data.
   */
  async getStaffById(staffId) {
    try {
      const response = await axios.get(
        `${API_BASE_URL}/${encodeURIComponent(staffId)}`,
        { withCredentials: true }
      );
      return response.data || {};
    } catch (error) {
      this.handleError(`Error retrieving staff with ID ${staffId}`, error);
    }
  }

  /**
   * Retrieve hotel ID by user ID.
   * @param {string} userId - User ID.
   * @returns {Promise<Object>} API response data.
   */
  async getHotelIdByUserId(userId) {
    try {
      const response = await axios.get(
        `${API_BASE_URL}/hotel/${encodeURIComponent(userId)}`,
        { withCredentials: true }
      );
      return response.data || {};
    } catch (error) {
      this.handleError(`Error retrieving hotel ID for user ID ${userId}`, error);
    }
  }

  /**
   * Retrieve all staff members with pagination.
   * @param {number} [page=0] - Page number.
   * @param {number} [size=10] - Page size.
   * @returns {Promise<Object>} API response data.
   */
  async getAllStaff(page = 0, size = 10) {
    try {
      const response = await axios.get(`${API_BASE_URL}/all`, {
        params: { page, size },
        withCredentials: true,
      });
      return response.data || {};
    } catch (error) {
      this.handleError('Error retrieving staff list', error);
    }
  }

  /**
   * Delete a staff member by ID.
   * @param {string} staffId - Staff ID.
   * @returns {Promise<Object>} API response data.
   */
  async deleteStaff(staffId) {
    try {
      const response = await axios.delete(
        `${API_BASE_URL}/${encodeURIComponent(staffId)}`,
        { withCredentials: true }
      );
      return response.data || {};
    } catch (error) {
      this.handleError(`Error deleting staff with ID ${staffId}`, error);
    }
  }

  /**
   * Centralized error handling for API requests.
   * @param {string} message - Error message.
   * @param {Object} error - Error object.
   * @throws {Error} Rethrows the error after logging.
   */
  handleError(message, error) {
    const errorDetails = error.response?.data || error.message;
    console.error(`${message}: ${errorDetails}`);
    throw error;
  }
}

const staffHotelRoute = new StaffHotelRoute();
export default staffHotelRoute;