import React from 'react';
import { GiShoppingCart } from "react-icons/gi";

const Cart = () => {
  return (
    <div className="container mx-auto px-4 py-8">
      <div className="text-center">
        <div className="flex justify-center mb-4">
          <GiShoppingCart className="text-6xl text-softBlue" />
        </div>
        <h1 className="text-3xl font-bold text-gray-800 mb-4">Shopping Cart</h1>
        <p className="text-gray-600 mb-8">Your cart is currently empty.</p>
        <div className="bg-gray-50 rounded-lg p-8">
          <p className="text-gray-500">Cart functionality is coming soon!</p>
          <p className="text-sm text-gray-400 mt-2">
            Add products to your cart and manage your purchases here.
          </p>
        </div>
      </div>
    </div>
  );
};

export default Cart;
