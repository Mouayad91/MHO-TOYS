import React from "react";
import { GiShoppingCart } from "react-icons/gi";
import {
  FaHeart,
  FaRegHeart,
  FaStar,
  FaStarHalfAlt,
  FaTimes,
} from "react-icons/fa";
import Zoom from "react-medium-image-zoom";
import "react-medium-image-zoom/dist/styles.css";

const ProductCard = ({ product }) => {
  const [isFavorited, setIsFavorited] = React.useState(false);
  const [showModal, setShowModal] = React.useState(false);

  const getImageSrc = (imageUrl) => {
    if (!imageUrl) return "https://via.placeholder.com/300x300?text=No+Image";
    if (imageUrl.startsWith("http") || imageUrl.startsWith("data:")) {
      return imageUrl;
    }
    return `http://localhost:8080/images/${imageUrl}`;
  };

  const getProductRating = (productId) => {
    const seed = parseInt(productId);
    const rating = 4.0 + (seed * 0.123456789) % 1.0;
    return Math.round(rating * 10) / 10;
  };

  const renderStars = (rating) => {
    const stars = [];
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 !== 0;

    for (let i = 0; i < fullStars; i++) {
      stars.push(<FaStar key={i} className="text-yellow-400" />);
    }
    if (hasHalfStar) {
      stars.push(<FaStarHalfAlt key="half" className="text-yellow-400" />);
    }
    const emptyStars = 5 - stars.length;
    for (let i = 0; i < emptyStars; i++) {
      stars.push(
        <FaStar key={`empty-${i}`} className="text-gray-300" />
      );
    }
    return stars;
  };

  const productAge = product.ageRange || "Age not specified";
  const productRating = getProductRating(product.productId);

  return (
    <>
      <div className="bg-white rounded-2xl shadow-md hover:shadow-xl transition duration-300 overflow-hidden flex flex-col h-full">
        <div className="relative h-64 bg-gradient-to-br from-pink-50 to-purple-50 flex items-center justify-center overflow-hidden">
          <img
            src={getImageSrc(product.imageUrl)}
            alt={product.name}
            className="max-w-full max-h-full object-contain transition-transform duration-500 group-hover:scale-105"
            onError={(e) => {
              e.target.src =
                "https://via.placeholder.com/300x300?text=Image+Not+Found";
            }}
          />

          <button
            onClick={() => setIsFavorited(!isFavorited)}
            className="absolute top-3 right-3 w-10 h-10 bg-white/80 backdrop-blur-sm rounded-full flex items-center justify-center shadow-md hover:bg-white transition-transform duration-200 transform hover:scale-110"
          >
            {isFavorited ? (
              <FaHeart className="text-red-500 text-lg" />
            ) : (
              <FaRegHeart className="text-gray-600 text-lg" />
            )}
          </button>

          <div className="absolute top-3 left-3 bg-gradient-to-r from-purple-500 to-pink-500 text-white px-3 py-1 rounded-full text-xs font-semibold shadow-md">
            {productAge}
          </div>

          <button
            onClick={() => setShowModal(true)}
            className="absolute inset-0 bg-black/20 opacity-0 hover:opacity-100 transition duration-300 flex items-center justify-center"
          >
            <span className="bg-white text-gray-800 px-4 py-2 rounded-full font-semibold shadow-md transform translate-y-4 hover:translate-y-0 transition-transform duration-300">
              Quick View
            </span>
          </button>
        </div>

        <div className="flex flex-col justify-between flex-grow p-6">
          <div className="flex items-center gap-2 mb-3">
            <div className="flex items-center gap-1">
              {renderStars(productRating)}
            </div>
            <span className="text-sm text-gray-600 font-medium">
              {productRating}
            </span>
            <span className="text-xs text-gray-400">
              ({Math.floor(Math.random() * 50) + 10} reviews)
            </span>
          </div>

          <h3 className="text-lg font-bold text-gray-900 mb-1 line-clamp-2">
            {product.name}
          </h3>

          <p className="text-sm text-gray-600 line-clamp-2 mb-4">
            {product.description}
          </p>

          <div className="flex items-end justify-between mt-auto">
            <span className="text-2xl font-bold text-purple-600">
              ${product.price}
            </span>

            <button className="bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 text-white px-5 py-2.5 rounded-full font-semibold shadow-md hover:shadow-lg transition-transform duration-200 transform hover:scale-105 flex items-center gap-2">
              <GiShoppingCart className="text-lg" />
              <span className="text-sm">Buy Now</span>
            </button>
          </div>
        </div>
      </div>

      {/* Product Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl max-w-4xl w-full max-h-[95vh] overflow-y-auto">
            <div className="relative">
              <button
                onClick={() => setShowModal(false)}
                className="absolute top-4 right-4 w-10 h-10 bg-gray-100 hover:bg-gray-200 rounded-full flex items-center justify-center z-10 transition-colors duration-200"
              >
                <FaTimes className="text-gray-600" />
              </button>

              <div className="p-8">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                  <div className="relative">
                    <Zoom>
                      <img
                        src={getImageSrc(product.imageUrl)}
                        alt={product.name}
                        className="w-full h-72 md:h-[28rem] object-contain rounded-xl"
                        onError={(e) => {
                          e.target.src =
                            "https://via.placeholder.com/400x400?text=Image+Not+Found";
                        }}
                      />
                    </Zoom>

                    <div className="absolute top-3 left-3 bg-gradient-to-r from-purple-500 to-pink-500 text-white px-3 py-1 rounded-full text-sm font-semibold shadow-md">
                      {productAge}
                    </div>
                  </div>

                  <div className="space-y-4">
                    <div className="flex items-center gap-2 mb-2">
                      <div className="flex items-center gap-1">
                        {renderStars(productRating)}
                      </div>
                      <span className="text-sm text-gray-600">
                        {productRating}
                      </span>
                    </div>

                    <h2 className="text-3xl font-bold text-gray-900">
                      {product.name}
                    </h2>

                    <p className="text-gray-600 leading-relaxed text-base">
                      {product.description}
                    </p>

                    <div className="text-4xl font-bold text-purple-600">
                      ${product.price}
                    </div>

                    <button className="w-full bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 text-white py-3 px-6 rounded-xl font-semibold shadow-lg hover:shadow-xl transition-all duration-200 transform hover:scale-105 flex items-center justify-center gap-2">
                      <GiShoppingCart className="text-xl" />
                      Add to Cart
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
