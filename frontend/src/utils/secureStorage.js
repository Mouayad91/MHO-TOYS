// Secure storage utility for handling authentication tokens
// Uses httpOnly cookies for tokens and sessionStorage for non-sensitive data

class SecureStorage {
  // Store authentication token via API call to set httpOnly cookie
  static async setAuthToken(token) {
    try {
      const response = await fetch(`${import.meta.env.VITE_API_URL || 'http://localhost:8080/api'}/auth/set-token`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({ token }),
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.error || `HTTP error! status: ${response.status}`);
      }

      return true;
    } catch (error) {
      // Error handled silently in production
      throw error;
    }
  }

  static async getAuthToken() {
    try {
      const response = await fetch(`${import.meta.env.VITE_API_URL || 'http://localhost:8080/api'}/auth/validate-token`, {
        method: 'GET',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (response.ok) {
        const data = await response.json();
        return data.valid ? data.token : null;
      }
      return null;
    } catch (error) {
      // Error handled silently in production
      return null;
    }
  }

  static async clearAuthToken() {
    try {
      const response = await fetch(`${import.meta.env.VITE_API_URL || 'http://localhost:8080/api'}/auth/logout`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        // Silent warning in production
      }
    } catch (error) {
      
    }
  }

  // For non-sensitive data, use sessionStorage
  static setUserData(key, data) {
    try {
      sessionStorage.setItem(key, JSON.stringify(data));
      return true;
    } catch (error) {
      
      return false;
    }
  }

  static getUserData(key) {
    try {
      const data = sessionStorage.getItem(key);
      return data ? JSON.parse(data) : null;
    } catch (error) {
      
      return null;
    }
  }

  static clearUserData(key) {
    try {
      if (key) {
        sessionStorage.removeItem(key);
      } else {
        sessionStorage.clear();
      }
      return true;
    } catch (error) {
      
      return false;
    }
  }

  // Migration utility to move existing localStorage data
  static migrateFromLocalStorage() {
    try {
      // Get existing data from localStorage
      const user = localStorage.getItem('authUser');
      const roles = localStorage.getItem('authRoles');
      
      // Store in sessionStorage
      if (user) {
        this.setUserData('authUser', JSON.parse(user));
      }
      if (roles) {
        this.setUserData('authRoles', JSON.parse(roles));
      }
      
      // Clear localStorage
      localStorage.removeItem('authToken');
      localStorage.removeItem('authUser');
      localStorage.removeItem('authRoles');
      
      return true;
    } catch (error) {
      
      return false;
    }
  }
}

export default SecureStorage; 