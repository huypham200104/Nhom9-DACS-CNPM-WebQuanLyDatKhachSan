import axios from 'axios';

const API_BASE_URL = "http://localhost:8080/users";

class UserRoute {
    // Register a new user
    registerUser(user) {
        return axios.post(`${API_BASE_URL}/register`, user)
            .then(response => response.data)
            .catch(error => {
                console.error("Error registering user:", error.response?.data || error.message);
                throw error;
            });
    }

    // Get user by ID
    getUserById(userId) {
        return axios.get(`${API_BASE_URL}/${userId}`)
            .then(response => response.data)
            .catch(error => {
                console.error(`Error fetching user with ID ${userId}:`, error.response?.data || error.message);
                throw error;
            });
    }

    // Get all users with pagination
    getAllUsers(page = 0, size = 10) {
        return axios.get(`${API_BASE_URL}/all`, {
            params: { page, size }
        })
        .then(response => response.data)
        .catch(error => {
            console.error("Error fetching users:", error.response?.data || error.message);
            throw error;
        });
    }

    // Update user
    updateUser(userId, user) {
        return axios.put(`${API_BASE_URL}/${userId}`, user)
            .then(response => response.data)
            .catch(error => {
                console.error(`Error updating user with ID ${userId}:`, error.response?.data || error.message);
                throw error;
            });
    }

    // Delete user
    deleteUser(userId) {
        return axios.delete(`${API_BASE_URL}/${userId}`)
            .then(response => response.data)
            .catch(error => {
                console.error(`Error deleting user with ID ${userId}:`, error.response?.data || error.message);
                throw error;
            });
    }

    // Find user by username
    findByUsername(username) {
        return axios.get(`${API_BASE_URL}/username/${username}`)
            .then(response => response.data)
            .catch(error => {
                console.error(`Error finding user with username ${username}:`, error.response?.data || error.message);
                throw error;
            });
    }

    // Additional methods that might be useful
    // Activate user
    activateUser(userId) {
        return axios.put(`${API_BASE_URL}/${userId}/activate`)
            .then(response => response.data)
            .catch(error => {
                console.error(`Error activating user with ID ${userId}:`, error.response?.data || error.message);
                throw error;
            });
    }

    // Deactivate user
    deactivateUser(userId) {
        return axios.put(`${API_BASE_URL}/${userId}/deactivate`)
            .then(response => response.data)
            .catch(error => {
                console.error(`Error deactivating user with ID ${userId}:`, error.response?.data || error.message);
                throw error;
            });
    }

    // Search users by criteria
    searchUsers({ username, role, status }) {
        const params = {};
        if (username) params.username = username;
        if (role) params.role = role;
        if (status) params.status = status;

        return axios.get(`${API_BASE_URL}/search`, {
            params: params
        })
        .then(response => response.data)
        .catch(error => {
            console.error("Error searching users:", error.response?.data || error.message);
            throw error;
        });
    }

    // Reset password
    resetPassword(email) {
        return axios.post(`${API_BASE_URL}/reset-password`, null, {
            params: { email }
        })
        .then(response => response.data)
        .catch(error => {
            console.error(`Error resetting password for email ${email}:`, error.response?.data || error.message);
            throw error;
        });
    }
}

const userRoute = new UserRoute();
export default userRoute;