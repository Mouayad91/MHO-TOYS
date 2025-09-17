import React, { useState, useEffect } from 'react';
import api from '../../api/api';
import toast from 'react-hot-toast';

const AdminStats = () => {
  const [stats, setStats] = useState({
    totalUsers: 0,
    activeUsers: 0,
    lockedAccounts: 0,
    newUsersLastWeek: 0,
    newUsersLastMonth: 0,
    usersWithFailedAttempts: 0
  });
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchStats();
    fetchProducts();
  }, []);

  const fetchStats = async () => {
    try {
      const response = await api.get('/admin/statistics');
      setStats(response.data);
    } catch (error) {
      
      toast.error('Failed to load statistics');
    }
  };

  const fetchProducts = async () => {
    try {
      const response = await api.get('/products');
      setProducts(response.data);
    } catch (error) {
      
    } finally {
      setLoading(false);
    }
  };

  const StatCard = ({ title, value, icon, color = "blue" }) => (
    <div className="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
      <div className="flex items-center justify-between">
        <div>
          <p className="text-sm font-medium text-gray-600">{title}</p>
          <p className={`text-2xl font-bold text-${color}-600`}>{value}</p>
        </div>
        <div className={`w-12 h-12 bg-${color}-100 rounded-lg flex items-center justify-center`}>
          <span className="text-2xl">{icon}</span>
        </div>
      </div>
    </div>
  );

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-2xl font-bold text-gray-900 mb-6">Dashboard Overview</h2>
        
        {/* Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
          <StatCard 
            title="Total Products" 
            value={products.length} 
            icon="🧸" 
            color="blue"
          />
          <StatCard 
            title="Total Users" 
            value={stats.totalUsers} 
            icon="👥" 
            color="green"
          />
          <StatCard 
            title="Active Users" 
            value={stats.activeUsers} 
            icon="✅" 
            color="emerald"
          />
          <StatCard 
            title="Locked Accounts" 
            value={stats.lockedAccounts} 
            icon="🔒" 
            color="red"
          />
          <StatCard 
            title="New Users (Week)" 
            value={stats.newUsersLastWeek} 
            icon="📈" 
            color="purple"
          />
          <StatCard 
            title="New Users (Month)" 
            value={stats.newUsersLastMonth} 
            icon="📊" 
            color="indigo"
          />
        </div>

        {/* Recent Products */}
        <div className="bg-white rounded-lg shadow-sm border border-gray-200">
          <div className="px-6 py-4 border-b border-gray-200">
            <h3 className="text-lg font-semibold text-gray-900">Recent Products</h3>
          </div>
          <div className="p-6">
            {products.length === 0 ? (
              <p className="text-gray-500 text-center py-4">No products available</p>
            ) : (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {products.slice(0, 6).map((product) => (
                  <div key={product.productId} className="border border-gray-200 rounded-lg p-4">
                    {product.imageUrl && (
                      <img 
                        src={product.imageUrl} 
                        alt={product.name}
                        className="w-full h-32 object-cover rounded-md mb-3"
                      />
                    )}
                    <h4 className="font-medium text-gray-900 mb-1">{product.name}</h4>
                    <p className="text-sm text-gray-600 mb-2">{product.description?.substring(0, 80)}...</p>
                    <div className="flex justify-between items-center">
                      <span className="text-lg font-bold text-blue-600">${product.price}</span>
                      <span className="text-xs text-gray-500">{product.ageRange}</span>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>

        {/* Quick Actions */}
        <div className="bg-white rounded-lg shadow-sm border border-gray-200">
          <div className="px-6 py-4 border-b border-gray-200">
            <h3 className="text-lg font-semibold text-gray-900">Quick Actions</h3>
          </div>
          <div className="p-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <button 
                onClick={() => window.location.href = '/admin/products/add'}
                className="flex items-center justify-center px-6 py-4 bg-blue-50 border border-blue-200 rounded-lg hover:bg-blue-100 transition-colors"
              >
                <span className="text-2xl mr-3">➕</span>
                <span className="font-medium text-blue-700">Add New Product</span>
              </button>
              <button 
                onClick={() => window.location.href = '/admin/users'}
                className="flex items-center justify-center px-6 py-4 bg-green-50 border border-green-200 rounded-lg hover:bg-green-100 transition-colors"
              >
                <span className="text-2xl mr-3">👥</span>
                <span className="font-medium text-green-700">Manage Users</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminStats; 