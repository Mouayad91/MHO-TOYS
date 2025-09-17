import React, { useState, useEffect } from 'react';
import api from '../../api/api';
import toast from 'react-hot-toast';

const UserManagement = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [actionLoading, setActionLoading] = useState({});
  const [editingUser, setEditingUser] = useState(null);
  const [showEditModal, setShowEditModal] = useState(false);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await api.get('/admin/users');
      setUsers(response.data);
    } catch (error) {
      // Error logging removed for production
      toast.error('Failed to load users');
    } finally {
      setLoading(false);
    }
  };

  const handleRoleUpdate = async (userId, newRole) => {
    setActionLoading(prev => ({ ...prev, [`role-${userId}`]: true }));
    try {
      await api.put(`/admin/users/${userId}/role?roleName=${newRole}`);
      toast.success('User role updated successfully');
      fetchUsers();
    } catch (error) {
      // Error logging removed for production
      toast.error('Failed to update user role');
    } finally {
      setActionLoading(prev => ({ ...prev, [`role-${userId}`]: false }));
    }
  };

  const handleUserAction = async (userId, action) => {
    const actionKey = `${action}-${userId}`;
    setActionLoading(prev => ({ ...prev, [actionKey]: true }));
    
    try {
      let endpoint = '';
      let successMessage = '';
      
      switch (action) {
        case 'enable':
          endpoint = `/admin/users/${userId}/enable`;
          successMessage = 'User enabled successfully';
          break;
        case 'disable':
          endpoint = `/admin/users/${userId}/disable`;
          successMessage = 'User disabled successfully';
          break;
        case 'lock':
          endpoint = `/admin/users/${userId}/lock`;
          successMessage = 'User account locked successfully';
          break;
        case 'unlock':
          endpoint = `/admin/users/${userId}/unlock`;
          successMessage = 'User account unlocked successfully';
          break;
        case 'delete':
          if (!window.confirm('Are you sure you want to delete this user? This action cannot be undone.')) {
            return;
          }
          endpoint = `/admin/users/${userId}`;
          successMessage = 'User deleted successfully';
          break;
        default:
          return;
      }

      if (action === 'delete') {
        await api.delete(endpoint);
      } else {
        await api.put(endpoint);
      }
      
      toast.success(successMessage);
      fetchUsers();
    } catch (error) {
      // Error logging removed for production
      toast.error(error.response?.data?.message || `Failed to ${action} user`);
    } finally {
      setActionLoading(prev => ({ ...prev, [actionKey]: false }));
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'Never';
    return new Date(dateString).toLocaleDateString();
  };

  const getUserStatusBadge = (user) => {
    if (!user.enabled) {
      return <span className="px-2 py-1 text-xs font-semibold rounded-full bg-red-100 text-red-800">Disabled</span>;
    }
    if (!user.accountNonLocked) {
      return <span className="px-2 py-1 text-xs font-semibold rounded-full bg-yellow-100 text-yellow-800">Locked</span>;
    }
    return <span className="px-2 py-1 text-xs font-semibold rounded-full bg-green-100 text-green-800">Active</span>;
  };

  const getRoleBadge = (roleName) => {
    const isAdmin = roleName === 'ROLE_ADMIN';
    return (
      <span className={`px-2 py-1 text-xs font-semibold rounded-full ${
        isAdmin ? 'bg-purple-100 text-purple-800' : 'bg-blue-100 text-blue-800'
      }`}>
        {isAdmin ? 'Admin' : 'User'}
      </span>
    );
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold text-gray-900">User Management</h2>
        <div className="text-sm text-gray-600">
          Total Users: {users.length}
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  User
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Role
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Status
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Last Login
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Failed Attempts
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {users.map((user) => (
                <tr key={user.userId} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center">
                      <div className="flex-shrink-0 h-10 w-10">
                        <div className="h-10 w-10 rounded-full bg-blue-500 flex items-center justify-center text-white font-medium">
                          {user.userName?.charAt(0)?.toUpperCase()}
                        </div>
                      </div>
                      <div className="ml-4">
                        <div className="text-sm font-medium text-gray-900">
                          {user.userName}
                        </div>
                        <div className="text-sm text-gray-500">
                          {user.email}
                        </div>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center space-x-2">
                      {getRoleBadge(user.role?.roleName)}
                      <select
                        value={user.role?.roleName || 'ROLE_USER'}
                        onChange={(e) => handleRoleUpdate(user.userId, e.target.value)}
                        disabled={actionLoading[`role-${user.userId}`]}
                        className="text-xs border border-gray-300 rounded px-2 py-1 focus:ring-1 focus:ring-blue-500 focus:border-blue-500"
                      >
                        <option value="ROLE_USER">User</option>
                        <option value="ROLE_ADMIN">Admin</option>
                      </select>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {getUserStatusBadge(user)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {formatDate(user.lastLoginDate)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`text-sm ${user.failedLoginAttempts > 0 ? 'text-red-600 font-medium' : 'text-gray-500'}`}>
                      {user.failedLoginAttempts || 0}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <div className="flex items-center space-x-2">
                      {user.enabled ? (
                        <button
                          onClick={() => handleUserAction(user.userId, 'disable')}
                          disabled={actionLoading[`disable-${user.userId}`]}
                          className="text-orange-600 hover:text-orange-900 bg-orange-50 px-2 py-1 rounded text-xs disabled:opacity-50"
                        >
                          {actionLoading[`disable-${user.userId}`] ? '...' : 'Disable'}
                        </button>
                      ) : (
                        <button
                          onClick={() => handleUserAction(user.userId, 'enable')}
                          disabled={actionLoading[`enable-${user.userId}`]}
                          className="text-green-600 hover:text-green-900 bg-green-50 px-2 py-1 rounded text-xs disabled:opacity-50"
                        >
                          {actionLoading[`enable-${user.userId}`] ? '...' : 'Enable'}
                        </button>
                      )}
                      
                      {user.accountNonLocked ? (
                        <button
                          onClick={() => handleUserAction(user.userId, 'lock')}
                          disabled={actionLoading[`lock-${user.userId}`]}
                          className="text-yellow-600 hover:text-yellow-900 bg-yellow-50 px-2 py-1 rounded text-xs disabled:opacity-50"
                        >
                          {actionLoading[`lock-${user.userId}`] ? '...' : 'Lock'}
                        </button>
                      ) : (
                        <button
                          onClick={() => handleUserAction(user.userId, 'unlock')}
                          disabled={actionLoading[`unlock-${user.userId}`]}
                          className="text-blue-600 hover:text-blue-900 bg-blue-50 px-2 py-1 rounded text-xs disabled:opacity-50"
                        >
                          {actionLoading[`unlock-${user.userId}`] ? '...' : 'Unlock'}
                        </button>
                      )}
                      
                      <button
                        onClick={() => handleUserAction(user.userId, 'delete')}
                        disabled={actionLoading[`delete-${user.userId}`]}
                        className="text-red-600 hover:text-red-900 bg-red-50 px-2 py-1 rounded text-xs disabled:opacity-50"
                      >
                        {actionLoading[`delete-${user.userId}`] ? '...' : 'Delete'}
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {users.length === 0 && (
          <div className="text-center py-12">
            <div className="text-gray-400 text-6xl mb-4">👥</div>
            <h3 className="text-lg font-medium text-gray-900 mb-2">No users found</h3>
            <p className="text-gray-500">Users will appear here once they register.</p>
          </div>
        )}
      </div>

      {/* Quick Actions */}
      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <div className="px-6 py-4 border-b border-gray-200">
          <h3 className="text-lg font-semibold text-gray-900">Quick Actions</h3>
        </div>
        <div className="p-6">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="text-center p-4 bg-blue-50 rounded-lg">
              <div className="text-2xl font-bold text-blue-600">
                {users.filter(u => u.enabled && u.accountNonLocked).length}
              </div>
              <div className="text-sm text-blue-700 font-medium">Active Users</div>
            </div>
            <div className="text-center p-4 bg-yellow-50 rounded-lg">
              <div className="text-2xl font-bold text-yellow-600">
                {users.filter(u => !u.accountNonLocked).length}
              </div>
              <div className="text-sm text-yellow-700 font-medium">Locked Accounts</div>
            </div>
            <div className="text-center p-4 bg-red-50 rounded-lg">
              <div className="text-2xl font-bold text-red-600">
                {users.filter(u => !u.enabled).length}
              </div>
              <div className="text-sm text-red-700 font-medium">Disabled Users</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserManagement; 