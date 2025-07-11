import React from "react";
import { GiShoppingCart } from "react-icons/gi";
import { FaHeart, FaRegHeart, FaStar, FaStarHalfAlt, FaTimes } from "react-icons/fa";
import { useState } from "react";
const ProductCard = ({ product }) => {
  const [isFavorited, setIsFavorited] = useState(false);
  const [showModal, setShowModal] = useState(false);

  // Age ranges for different toys
  const ageRange = [
    "0-6 Months",
    "6-12 Months",
    "1-2 Years",
    "2-3 Years",
    "3-4 Years",
    "4-5 Years",
    "5+ Years",
  ];


  const getProductAge = (productId) => {
    const index = parseInt(productId) % ageRange.length;
    return ageRange[index];
  };

  const getProductRating = (productId) => {

    const seed = parseInt(productId);
    const rating = 4.0 + (seed * 0.123456789) % 1.0; // Creates variation between 4.0-5.0
    return Math.round(rating * 10) / 10; // Round to 1 decimal place
  };

  const renderStars = (rating) => {
    const stars = [];
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 !== 0;


    for (let i = 0; i < fullStars; i++) {
      stars.push(<FaStar key={i} className="text-yellow-400 w-4 h-4" />);
    }

    if (hasHalfStar) {
      stars.push(
        <FaStarHalfAlt key="half" className="text-yellow-400 w-4 h-4" />
      );
    }

    // Empty stars
    const emptyStars = 5 - Math.ceil(rating);
    for (let i = 0; i < emptyStars; i++) {
      stars.push(
        <FaStar key={`empty-${i}`} className="text-gray-300 w-4 h-4" />
      );
    }

    return stars;
  };

  const handleAddToCart = (e) => {
    e.stopPropagation(); 
    console.log("Added to cart:", product.name);
  };

  const toggleFavorite = (e) => {
    e.stopPropagation();
    setIsFavorited(!isFavorited);
  };

  const openModal = () => {
    setShowModal(true);
    document.body.style.overflow = 'hidden'; 
  };

  const closeModal = () => {
    setShowModal(false);
    document.body.style.overflow = 'unset'; 
  };

  const productAge = getProductAge(product.productId);
  const productRating = getProductRating(product.productId);

  return (
    <>
      <div 
        className="bg-white rounded-xl shadow-lg hover:shadow-xl transition-all duration-300 transform hover:-translate-y-1 overflow-hidden group h-full flex flex-col cursor-pointer"
        onClick={openModal}
      >
        <div className="relative overflow-hidden bg-gradient-to-br from-pink-50 to-blue-50 p-4 h-64">
          <img
            src={product.imageUrl}
            alt={product.name}
            className="w-full h-full object-contain transition-transform duration-500 group-hover:scale-110"
            onError={(e) => {
              e.target.src = `https://via.placeholder.com/300x300?text=${encodeURIComponent(
                product.name
              )}`;
            }}
          />

          <button
            onClick={toggleFavorite}
            className="absolute top-2 right-2 p-2 rounded-full bg-white/80 hover:bg-white shadow-md transition-all duration-200 hover:scale-110 z-10"
          >
            {isFavorited ? (
              <FaHeart className="text-red-500 text-sm" />
            ) : (
              <FaRegHeart className="text-gray-600 text-sm" />
            )}
          </button>


          <div className="absolute top-2 left-2 bg-yellow-400 text-yellow-900 px-2 py-1 rounded-full text-xs font-semibold">
            {productAge}
          </div>


          <div className="absolute inset-0 bg-black/0 group-hover:bg-black/10 transition-all duration-300 flex items-center justify-center">
            <div className="opacity-0 group-hover:opacity-100 transition-opacity duration-300 bg-white/90 px-3 py-1 rounded-full text-sm font-medium text-gray-800">
              Click for details
            </div>
          </div>
        </div>


        <div className="p-4 sm:p-5 flex flex-col flex-grow">
          <h3 className="text-lg sm:text-xl font-bold text-gray-800 mb-2 line-clamp-2 hover:text-blue-600 transition-colors duration-200 min-h-[3.5rem]">
            {product.name}
          </h3>

          <p className="text-gray-600 text-sm sm:text-base mb-3 line-clamp-2 leading-relaxed min-h-[2.5rem]">
            {product.description}
          </p>

          <div className="flex items-center mb-3 min-h-[1.5rem]">
            <div className="flex space-x-1">{renderStars(productRating)}</div>
            <span className="text-gray-500 text-sm ml-2">({productRating})</span>
          </div>

          <div className="mb-4 min-h-[2rem]">
            <div className="flex items-center space-x-2 flex-wrap gap-1">
              <div className="bg-green-100 text-green-800 px-2 py-1 rounded-full text-xs font-medium">
                ✓ Safe Materials
              </div>
              <div className="bg-blue-100 text-blue-800 px-2 py-1 rounded-full text-xs font-medium">
                ✓ Educational
              </div>
            </div>
          </div>

          <div className="flex items-center justify-between mt-auto">
            <div className="flex flex-col">
              <span className="text-xl sm:text-2xl font-bold text-green-600">
                ${product.price}
              </span>
              <span className="text-gray-500 text-xs line-through">
                ${(product.price * 1.2).toFixed(2)}
              </span>
            </div>

            <button
              onClick={handleAddToCart}
              className="bg-gradient-to-r from-blue-500 to-purple-600 hover:from-blue-600 hover:to-purple-700 text-white px-3 py-2 rounded-lg flex items-center space-x-2 transition-all duration-200 transform hover:scale-105 shadow-lg hover:shadow-xl min-w-[80px] justify-center"
            >
              <GiShoppingCart className="text-base" />
              <span className="font-semibold text-sm">Add</span>
            </button>
          </div>
        </div>
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl max-w-4xl w-full max-h-[90vh] overflow-y-auto">
            {/* Modal Header */}
            <div className="flex items-center justify-between p-6 border-b">
              <h2 className="text-2xl font-bold text-gray-800">Product Details</h2>
              <button
                onClick={closeModal}
                className="p-2 hover:bg-gray-100 rounded-full transition-colors duration-200"
              >
                <FaTimes className="text-gray-600" />
              </button>
            </div>

            <div className="p-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                <div className="bg-gradient-to-br from-pink-50 to-blue-50 rounded-xl p-6 flex items-center justify-center">
                  <img
                    src={product.imageUrl}
                    alt={product.name}
                    className="w-full h-96 object-contain"
                    onError={(e) => {
                      e.target.src = `https://via.placeholder.com/400x400?text=${encodeURIComponent(
                        product.name
                      )}`;
                    }}
                  />
                </div>

                <div className="space-y-6">
                  <div>
                    <h3 className="text-3xl font-bold text-gray-800 mb-2">{product.name}</h3>
                    <div className="flex items-center space-x-3 mb-4">
                      <div className="bg-yellow-400 text-yellow-900 px-3 py-1 rounded-full text-sm font-semibold">
                        Age: {productAge}
                      </div>
                      <div className="flex items-center">
                        <div className="flex space-x-1 mr-2">{renderStars(productRating)}</div>
                        <span className="text-gray-600">({productRating})</span>
                      </div>
                    </div>
                  </div>

                  <div>
                    <h4 className="text-lg font-semibold text-gray-800 mb-2">Description</h4>
                    <p className="text-gray-600 leading-relaxed">{product.description}</p>
                  </div>

                  <div>
                    <h4 className="text-lg font-semibold text-gray-800 mb-3">Features</h4>
                    <div className="grid grid-cols-2 gap-3">
                      <div className="bg-green-50 border border-green-200 rounded-lg p-3">
                        <div className="flex items-center space-x-2">
                          <div className="w-2 h-2 bg-green-500 rounded-full"></div>
                          <span className="text-green-800 font-medium">Safe Materials</span>
                        </div>
                      </div>
                      <div className="bg-blue-50 border border-blue-200 rounded-lg p-3">
                        <div className="flex items-center space-x-2">
                          <div className="w-2 h-2 bg-blue-500 rounded-full"></div>
                          <span className="text-blue-800 font-medium">Educational</span>
                        </div>
                      </div>
                      <div className="bg-purple-50 border border-purple-200 rounded-lg p-3">
                        <div className="flex items-center space-x-2">
                          <div className="w-2 h-2 bg-purple-500 rounded-full"></div>
                          <span className="text-purple-800 font-medium">Durable</span>
                        </div>
                      </div>
                      <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-3">
                        <div className="flex items-center space-x-2">
                          <div className="w-2 h-2 bg-yellow-500 rounded-full"></div>
                          <span className="text-yellow-800 font-medium">Fun & Engaging</span>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div className="bg-gray-50 rounded-xl p-6">
                    <div className="flex items-center justify-between mb-4">
                      <div>
                        <span className="text-3xl font-bold text-green-600">${product.price}</span>
                        <span className="text-gray-500 text-lg line-through ml-2">
                          ${(product.price * 1.2).toFixed(2)}
                        </span>
                      </div>
                      <button
                        onClick={toggleFavorite}
                        className="p-3 rounded-full hover:bg-white shadow-md transition-all duration-200"
                      >
                        {isFavorited ? (
                          <FaHeart className="text-red-500 text-xl" />
                        ) : (
                          <FaRegHeart className="text-gray-600 text-xl" />
                        )}
                      </button>
                    </div>
                    
                    <button
                      onClick={handleAddToCart}
                      className="w-full bg-gradient-to-r from-blue-500 to-purple-600 hover:from-blue-600 hover:to-purple-700 text-white py-3 rounded-lg flex items-center justify-center space-x-2 transition-all duration-200 transform hover:scale-105 shadow-lg hover:shadow-xl text-lg font-semibold"
                    >
                      <GiShoppingCart className="text-xl" />
                      <span>Add to Cart</span>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default ProductCard;