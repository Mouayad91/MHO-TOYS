import React, { useReducer, useEffect } from 'react';
import api from '../api/api';
import { AuthContext } from './AuthContext.js';
import SecureStorage from '../utils/secureStorage';

// Auth Actions
const AUTH_ACTIONS = {
  LOGIN_START: 'LOGIN_START',
  LOGIN_SUCCESS: 'LOGIN_SUCCESS',
  LOGIN_FAILURE: 'LOGIN_FAILURE',
  LOGOUT: 'LOGOUT',
  RESTORE_AUTH: 'RESTORE_AUTH',
  SET_LOADING: 'SET_LOADING',
  UPDATE_USER: 'UPDATE_USER',
  CLEAR_ERROR: 'CLEAR_ERROR'
};

// Initial State
const initialState = {
  user: null,
  token: null,
  isAuthenticated: false,
  isLoading: true, // Start with loading true
  error: null,
  roles: []
};

// Auth Reducer
const authReducer = (state, action) => {
  switch (action.type) {
    case AUTH_ACTIONS.LOGIN_START:
      return {
        ...state,
        isLoading: true,
        error: null
      };

    case AUTH_ACTIONS.LOGIN_SUCCESS:
      return {
        ...state,
        user: action.payload.user,
        token: action.payload.token,
        roles: action.payload.roles || [],
        isAuthenticated: true,
        isLoading: false,
        error: null
      };

    case AUTH_ACTIONS.LOGIN_FAILURE:
      return {
        ...state,
        user: null,
        token: null,
        roles: [],
        isAuthenticated: false,
        isLoading: false,
        error: action.payload.error
      };

    case AUTH_ACTIONS.LOGOUT:
      return {
        ...initialState,
        isLoading: false
      };

    case AUTH_ACTIONS.RESTORE_AUTH:
      return {
        ...state,
        user: action.payload.user,
        token: action.payload.token,
        roles: action.payload.roles || [],
        isAuthenticated: true,
        isLoading: false
      };

    case AUTH_ACTIONS.SET_LOADING:
      return {
        ...state,
        isLoading: action.payload
      };

    case AUTH_ACTIONS.UPDATE_USER:
      return {
        ...state,
        user: { ...state.user, ...action.payload.user }
      };

    case AUTH_ACTIONS.CLEAR_ERROR:
      return {
        ...state,
        error: null
      };

    default:
      return state;
  }
};

// AuthProvider Component
export const AuthProvider = ({ children }) => {
  const [state, dispatch] = useReducer(authReducer, initialState);

  // Restore authentication state on app load
  useEffect(() => {
    const restoreAuth = async () => {
      try {
        dispatch({ type: AUTH_ACTIONS.SET_LOADING, payload: true });
        
        // Migrate from localStorage if it exists
        SecureStorage.migrateFromLocalStorage();
        
        // Get user data from sessionStorage first
        const user = SecureStorage.getUserData('authUser');
        const roles = SecureStorage.getUserData('authRoles');

        if (user && roles) {
          // Try to validate existing token from httpOnly cookie
          try {
            const isValidToken = await SecureStorage.getAuthToken();
            
            if (isValidToken) {
              dispatch({
                type: AUTH_ACTIONS.RESTORE_AUTH,
                payload: {
                  user,
                  token: 'secure_cookie', // Token is in httpOnly cookie
                  roles: roles || []
                }
              });
              return;
            }
          } catch (error) {
            console.warn('Token validation failed:', error);
          }
        }
        
        // If we get here, auth restoration failed
        await SecureStorage.clearAuthToken();
        SecureStorage.clearUserData();
        dispatch({ type: AUTH_ACTIONS.SET_LOADING, payload: false });
        
      } catch (error) {
        console.error('Error restoring authentication:', error);
        // Clear corrupted auth data
        await SecureStorage.clearAuthToken();
        SecureStorage.clearUserData();
        dispatch({ type: AUTH_ACTIONS.SET_LOADING, payload: false });
      }
    };

    restoreAuth();
  }, []);

  // Login function
  const login = async (credentials) => {
    dispatch({ type: AUTH_ACTIONS.LOGIN_START });

    try {
      // Use the correct backend endpoint for login
      const response = await api.post('/auth/public/signin', credentials);
      const { jwtToken, username, email, userId, roles, twoFactorRequired } = response.data;

      if (twoFactorRequired) {
        dispatch({
          type: AUTH_ACTIONS.LOGIN_FAILURE,
          payload: { error: 'Two-factor authentication required' }
        });
        return { success: false, twoFactorRequired: true, user: { username, email, userId } };
      }

      const user = { username, email, userId };

      // Store token securely in httpOnly cookie
      const tokenStored = await SecureStorage.setAuthToken(jwtToken);
      if (!tokenStored) {
        dispatch({
          type: AUTH_ACTIONS.LOGIN_FAILURE,
          payload: { error: 'Failed to store authentication token securely' }
        });
        return { success: false, error: 'Authentication storage failed' };
      }

      // Store user data in sessionStorage (non-sensitive)
      SecureStorage.setUserData('authUser', user);
      SecureStorage.setUserData('authRoles', roles);

      dispatch({
        type: AUTH_ACTIONS.LOGIN_SUCCESS,
        payload: {
          user,
          token: 'secure_cookie',
          roles
        }
      });

      return { success: true };
    } catch (error) {
      let errorMessage = 'Login failed';
      if (error.response?.data?.message) {
        errorMessage = error.response.data.message;
      } else if (error.response?.status === 401) {
        errorMessage = 'Invalid username or password';
      } else if (error.response?.status === 423) {
        errorMessage = 'Account is locked due to multiple failed login attempts';
      } else if (error.response?.status === 403) {
        errorMessage = 'Account is disabled';
      } else if (error.message === 'Network Error') {
        errorMessage = 'Unable to connect to server';
      }
      dispatch({
        type: AUTH_ACTIONS.LOGIN_FAILURE,
        payload: { error: errorMessage }
      });
      return { success: false, error: errorMessage };
    }
  };

  // Register function
  const register = async (userData) => {
    try {
      const response = await api.post('/auth/public/signup', userData);
      return { success: true, message: response.data.message };
    } catch (error) {
      let errorMessage = 'Registration failed';
      
      if (error.response?.data?.message) {
        errorMessage = error.response.data.message;
      } else if (error.response?.data) {
        // Handle validation errors
        if (typeof error.response.data === 'object') {
          errorMessage = Object.values(error.response.data).join(', ');
        }
      }

      return { success: false, error: errorMessage };
    }
  };

  // Logout function
  const logout = async () => {
    try {
      // Clear secure token (this also calls backend logout)
      await SecureStorage.clearAuthToken();
    } catch (error) {
      console.error('Logout API call failed:', error);
    } finally {
      // Clear session data regardless of API call success
      SecureStorage.clearUserData();

      dispatch({ type: AUTH_ACTIONS.LOGOUT });
    }
  };

  // Change password function
  const changePassword = async (currentPassword, newPassword) => {
    try {
      const response = await api.post('/auth/change-password', {
        currentPassword,
        newPassword
      });
      return { success: true, message: response.data.message };
    } catch (error) {
      let errorMessage = 'Failed to change password';
      
      if (error.response?.data?.message) {
        errorMessage = error.response.data.message;
      }

      return { success: false, error: errorMessage };
    }
  };

  // Get user profile
  const getUserProfile = async () => {
    try {
      const response = await api.get('/auth/user');
      const userProfile = response.data;

      dispatch({
        type: AUTH_ACTIONS.UPDATE_USER,
        payload: { user: userProfile }
      });

      return { success: true, user: userProfile };
    } catch (error) {
      return { success: false, error: 'Failed to fetch user profile' };
    }
  };

  // Clear error
  const clearError = () => {
    dispatch({ type: AUTH_ACTIONS.CLEAR_ERROR });
  };

  // Check if user has role
  const hasRole = (role) => {
    return state.roles.includes(role);
  };

  // Check if user is admin
  const isAdmin = () => {
    return hasRole('ROLE_ADMIN');
  };

  // Check if user is customer
  const isCustomer = () => {
    return hasRole('ROLE_USER');
  };

  const value = {
    // State
    user: state.user,
    token: state.token,
    isAuthenticated: state.isAuthenticated,
    isLoading: state.isLoading,
    error: state.error,
    roles: state.roles,

    // Actions
    login,
    register,
    logout,
    changePassword,
    getUserProfile,
    clearError,

    // Utility functions
    hasRole,
    isAdmin,
    isCustomer
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}; 