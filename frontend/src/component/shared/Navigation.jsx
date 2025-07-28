
import { GiShoppingCart } from "react-icons/gi";
import { FaUser, FaSignOutAlt, FaUserShield } from "react-icons/fa";
import { Link } from "react-router-dom";
import { useState } from "react";
import { useAuth } from "../../hooks/useAuth";
import toast from "react-hot-toast";

const Navigation = () => {
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const { isAuthenticated, user, logout, isAdmin } = useAuth();

  const toggleMobileMenu = () => {
    setIsMobileMenuOpen(!isMobileMenuOpen);
  };

  const handleLogout = async () => {
    try {
      await logout();
      toast.success("Logged out successfully!");
      setIsMobileMenuOpen(false);
    } catch (error) {
      toast.error("Logout failed");
    }
  };

  // Auth Navigation Items for Desktop
  const AuthNavItems = () => {
    if (isAuthenticated) {
      return (
        <div className="flex items-center gap-4 text-softBlue text-sm xl:text-base font-primary">
          <span className="text-gray-600">Welcome, {user?.username}</span>
          
          {isAdmin() && (
            <Link 
              to="/admin" 
              className="flex items-center gap-1 hover:text-softGreen transition-colors"
              title="Admin Dashboard"
            >
              <FaUserShield />
              <span>Admin</span>
            </Link>
          )}
          
          <Link 
            to="/profile" 
            className="flex items-center gap-1 hover:text-softGreen transition-colors"
          >
            <FaUser />
            <span>Profile</span>
          </Link>
          
          <button
            onClick={handleLogout}
            className="flex items-center gap-1 hover:text-red-500 transition-colors"
          >
            <FaSignOutAlt />
            <span>Logout</span>
          </button>
        </div>
      );
    }

    return (
      <div className="flex items-center gap-4 text-softBlue text-sm xl:text-base font-primary">
        <Link 
          to="/auth/login" 
          className="hover:text-softGreen transition-colors"
        >
          Login
        </Link>
        <Link 
          to="/auth/register" 
          className="bg-primary hover:bg-primary/90 text-white px-4 py-2 rounded-lg transition-colors"
        >
          Sign Up
        </Link>
      </div>
    );
  };

  // Auth Navigation Items for Mobile
  const MobileAuthNavItems = () => {
    if (isAuthenticated) {
      return (
        <>
          <li className="pt-2 border-t border-gray-100">
            <span className="block text-gray-600 py-2">Welcome, {user?.username}</span>
          </li>
          
          {isAdmin() && (
            <li>
              <Link 
                to="/admin" 
                className="flex items-center gap-2 hover:text-softGreen transition-colors py-2"
                onClick={() => setIsMobileMenuOpen(false)}
              >
                <FaUserShield />
                <span>Admin Dashboard</span>
              </Link>
            </li>
          )}
          
          <li>
            <Link 
              to="/profile" 
              className="flex items-center gap-2 hover:text-softGreen transition-colors py-2"
              onClick={() => setIsMobileMenuOpen(false)}
            >
              <FaUser />
              <span>Profile</span>
            </Link>
          </li>
          
          <li>
            <button
              onClick={handleLogout}
              className="flex items-center gap-2 hover:text-red-500 transition-colors py-2 w-full text-left"
            >
              <FaSignOutAlt />
              <span>Logout</span>
            </button>
          </li>
        </>
      );
    }

    return (
      <>
        <li className="pt-2 border-t border-gray-100">
          <Link 
            to="/auth/login" 
            className="block hover:text-softGreen transition-colors py-2"
            onClick={() => setIsMobileMenuOpen(false)}
          >
            Login
          </Link>
        </li>
        <li>
          <Link 
            to="/auth/register" 
            className="block bg-primary hover:bg-primary/90 text-white px-4 py-2 rounded-lg transition-colors text-center"
            onClick={() => setIsMobileMenuOpen(false)}
          >
            Sign Up
          </Link>
        </li>
      </>
    );
  };

  return (
    <header className="bg-white shadow-sm sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <Link
            to="/"
            className="flex items-center hover:scale-105 transition-transform flex-shrink-0"
          >
            <span className="text-2xl sm:text-3xl font-fun font-bold tracking-wide">
              <span className="text-blue-500">M</span>
              <span className="text-yellow-500">H</span>
              <span className="text-purple-500">O</span>
              <span> </span>
              <span className="text-green-500">T</span>
              <span className="text-red-500">O</span>
              <span className="text-indigo-500">Y</span>
              <span className="text-orange-500">S</span>
            </span>
          </Link>

          {/* Desktop Navigation */}
          <div className="hidden lg:flex items-center gap-6">
            <ul className="flex items-center gap-6 font-primary text-softBlue text-sm xl:text-base">
              <li>
                <Link to="/" className="hover:text-softGreen transition-colors">
                  Home
                </Link>
              </li>
              <li>
                <Link to="/about" className="hover:text-softGreen transition-colors">
                  About
                </Link>
              </li>
              <li>
                <Link to="/contact" className="hover:text-softGreen transition-colors">
                  Contact
                </Link>
              </li>
            </ul>

            <div className="flex items-center gap-4">
              <Link to="/cart" className="text-softBlue hover:text-softGreen transition-colors text-xl relative">
                <GiShoppingCart />
                <span className="absolute -top-2 -right-2 bg-primary text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">0</span>
              </Link>
              
              <AuthNavItems />
            </div>
          </div>

          {/* Mobile Menu Button & Cart */}
          <div className="flex items-center gap-3 lg:hidden">
            <Link to="/cart" className="text-softBlue hover:text-softGreen transition-colors text-xl relative">
              <GiShoppingCart />
              <span className="absolute -top-2 -right-2 bg-primary text-white text-xs rounded-full h-4 w-4 flex items-center justify-center">0</span>
            </Link>

            <button
              onClick={toggleMobileMenu}
              className="text-softBlue hover:text-softGreen transition-colors p-2"
              aria-label="Toggle mobile menu"
            >
              <svg
                className="w-6 h-6"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                {isMobileMenuOpen ? (
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                ) : (
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
                )}
              </svg>
            </button>
          </div>
        </div>

        {/* Mobile Menu */}
        <div className={`lg:hidden transition-all duration-300 ease-in-out ${
          isMobileMenuOpen ? 'max-h-96 opacity-100' : 'max-h-0 opacity-0'
        } overflow-hidden`}>
          <div className="py-4 border-t border-gray-100">
            <ul className="space-y-4 font-primary text-softBlue">
              <li>
                <Link 
                  to="/" 
                  className="block hover:text-softGreen transition-colors py-2"
                  onClick={() => setIsMobileMenuOpen(false)}
                >
                  Home
                </Link>
              </li>
              <li>
                <Link 
                  to="/about" 
                  className="block hover:text-softGreen transition-colors py-2"
                  onClick={() => setIsMobileMenuOpen(false)}
                >
                  About
                </Link>
              </li>
              <li>
                <Link 
                  to="/contact" 
                  className="block hover:text-softGreen transition-colors py-2"
                  onClick={() => setIsMobileMenuOpen(false)}
                >
                  Contact
                </Link>
              </li>
              
              <MobileAuthNavItems />
            </ul>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Navigation;