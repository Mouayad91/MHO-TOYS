
import { GiShoppingCart } from "react-icons/gi";
import { Link } from "react-router-dom";
import { useState } from "react";

const Navigation = ()=>{

 const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  const toggleMobileMenu = () => {
    setIsMobileMenuOpen(!isMobileMenuOpen);
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

            <div className="flex items-center gap-4 text-softBlue text-sm xl:text-base font-primary">
              <Link to="/cart" className="hover:text-softGreen transition-colors text-xl relative">
                <GiShoppingCart />
                <span className="absolute -top-2 -right-2 bg-primary text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">0</span>
              </Link>
              <Link to="/login" className="hover:text-softGreen transition-colors">
                Login
              </Link>
            </div>
          </div>

          <div className="flex items-center gap-3 lg:hidden">
            <Link to="/cart" className="text-softBlue hover:text-softGreen transition-colors text-xl relative">
              <GiShoppingCart />
              <span className="absolute -top-2 -right-2 bg-primary text-white text-xs rounded-full h-4 w-4 flex items-center justify-center">0</span>
            </Link>

            {/* Mobile Menu Button */}
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

        {/* Mobile Menu - Slides down when open */}
        <div className={`lg:hidden transition-all duration-300 ease-in-out ${
          isMobileMenuOpen ? 'max-h-64 opacity-100' : 'max-h-0 opacity-0'
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
              <li className="pt-2 border-t border-gray-100">
                <Link 
                  to="/login" 
                  className="block hover:text-softGreen transition-colors py-2"
                  onClick={() => setIsMobileMenuOpen(false)}
                >
                  Login
                </Link>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Navigation;