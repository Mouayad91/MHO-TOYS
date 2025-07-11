import React from "react";
import ProductCard from "./ProductCard";

const ProductLists = ({ products = [] }) => {
  return (
    <section className="py-12 bg-gradient-to-b from-white to-gray-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center mb-12">
          <h2 className="text-3xl sm:text-4xl lg:text-5xl font-bold text-gray-900 mb-4">
            🧸 Our Amazing Toys
          </h2>
          <p className="text-lg sm:text-xl text-gray-600 max-w-2xl mx-auto leading-relaxed">
            Discover safe, educational, and fun toys that spark imagination and
            creativity in children
          </p>
          <div className="mt-4 h-1 w-20 bg-gradient-to-r from-pink-500 to-purple-500 mx-auto rounded-full"></div>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 sm:gap-8">
          {products && products.length > 0 ? (
            products.map((product) => (
              <ProductCard key={product.productId} product={product} />
            ))
          ) : (
            <div className="col-span-full text-center py-12">
              <p className="text-gray-500 text-lg">No products available</p>
            </div>
          )}
        </div>
      </div>
    </section>
  );
};

export default ProductLists;