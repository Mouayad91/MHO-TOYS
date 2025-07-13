import React from "react";
import ProductCard from "./ProductCard";

const ProductLists = ({ products = [] }) => {
  console.log('ProductLists received:', products?.length, 'products');

  return (
    <section className="py-12 bg-gradient-to-b from-white to-gray-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center mb-12">
          <h2 className="text-3xl sm:text-4xl lg:text-5xl font-bold text-gray-900 mb-4">
            Our Amazing Products 
          </h2>
          <p className="text-lg sm:text-xl text-gray-600 max-w-2xl mx-auto leading-relaxed">
            Discover safe, educational, and fun products that spark imagination and
            creativity in children
          </p>
          <div className="mt-4 h-1 w-20 bg-gradient-to-r from-pink-500 to-purple-500 mx-auto rounded-full"></div>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6 items-stretch">
          {products && products.length > 0 ? (
            products.map((product, index) => (
              <ProductCard 
                key={product.productId || product.id || index} 
                product={product} 
              />
            ))
          ) : (
            <div className="col-span-full text-center py-12">
              <p className="text-gray-500 text-lg">No products found in database</p>
              <p className="text-gray-400 text-sm mt-2">
                Check your database connection and data
              </p>
            </div>
          )}
        </div>
      </div>
    </section>
  );
};

export default ProductLists;