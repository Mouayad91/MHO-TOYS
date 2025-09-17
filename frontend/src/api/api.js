import axios from 'axios';
import toast from 'react-hot-toast';

// Create axios instance with base configuration
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
  timeout: 10000,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor (removed debug logging)
api.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => {
    
    return Promise.reject(error);
  }
);

// Response interceptor for handling responses and errors
api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    // Handle different types of errors
    if (error.response) {
      // Server responded with error status
      const { status, data } = error.response;
      
      switch (status) {
        case 401:
          toast.error('Authentication required. Please log in.');
          if (window.location.pathname !== '/auth/login') {
            window.location.href = '/auth/login';
          }
          break;
        case 403:
          toast.error('Access denied. Insufficient permissions.');
          break;
        case 404:
          toast.error('Resource not found.');
          break;
        case 422:
          // Validation errors
          if (data && typeof data === 'object') {
            const errorMessages = Object.values(data).flat();
            errorMessages.forEach(msg => toast.error(msg));
          } else {
            toast.error('Validation error occurred.');
          }
          break;
        case 500:
          toast.error('Internal server error. Please try again later.');
          break;
        default:
          toast.error(data?.message || `Error: ${status}`);
      }
    } else if (error.request) {
      // Network error or no response
      toast.error('Network error. Please check your connection.');
    } else {
      // Other errors
      toast.error('An unexpected error occurred.');
    }
    
    return Promise.reject(error);
  }
);

export default api;