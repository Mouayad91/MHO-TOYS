import Navigation from "../shared/Navigation";
import SliderBanner from "./SliderBanner";
import ProductLists from "../product/ProductLists";
import api from "../../api/api";
import { useEffect, useState } from "react";

const Home = () => {
  const [toys, setToys] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchToys();
  }, []);

  const fetchToys = async () => {
    try {
      setLoading(true);
      setError(null);
      
      console.log('Fetching toys from database...');
      const response = await api.get('/toys');
      
      console.log('API Response:', response.data);
      
      if (Array.isArray(response.data)) {
        setToys(response.data);
        console.log('Successfully loaded', response.data.length, 'toys');
      } else {
        setError('Invalid data format received');
      }
      
    } catch (error) {
      console.error('Error fetching toys:', error);
      setError(`Failed to load toys: ${error.message}`);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-white flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-16 w-16 border-b-2 border-blue-600 mx-auto mb-4"></div>
          <p className="text-gray-600">Loading toys from database...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-white">
      <Navigation />
      <SliderBanner />
      
      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mx-4 mb-4">
          <p className="font-semibold">Error:</p>
          <p>{error}</p>
          <button 
            onClick={fetchToys}
            className="mt-2 bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700"
          >
            Retry
          </button>
        </div>
      )}
      
      <ProductLists products={toys} />
    </div>
  );
};

export default Home;


