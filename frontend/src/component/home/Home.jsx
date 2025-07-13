import SliderBanner from "./SliderBanner";
import ProductLists from "../product/ProductLists";
import api from "../../api/api";
import { useEffect, useState } from "react";

const Home = () => {
  const [products, setProducts] = useState([]); 
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchProducts(); 
  }, []);

  const fetchProducts = async () => { 
    try {
      setLoading(true);
      setError(null);

      const response = await api.get('/products'); 

      if (Array.isArray(response.data)) {
        setProducts(response.data); 
      } else {
        setError('Invalid data format received');
      }

    } catch (error) {
      setError(`Failed to load products: ${error.message}`);
    } finally {
      setLoading(false);
    }
  };

  return (

    
    <div className="bg-white">
      <SliderBanner />
      {loading ? (
        <div className="flex flex-col items-center justify-center h-[calc(100vh-80px)] text-center">
          <div className="animate-spin rounded-full h-16 w-16 border-b-2 border-blue-600 mb-4"></div>
          <p className="text-gray-600">Loading products from database...</p>
        </div>
      ) : error ? (
        <div className="flex justify-center mt-10">
          <div className="bg-white border border-red-300 text-red-600 shadow-lg rounded-xl w-full max-w-md px-6 py-5">
            <div className="flex items-start gap-4">
              <div className="text-3xl">🚫</div>
              <div>
                <h2 className="text-lg font-bold mb-1">Oops! Something went wrong.</h2>
                <p className="text-sm mb-3">{error}</p>
                <button
                  onClick={fetchProducts}
                  className="inline-block bg-red-500 hover:bg-red-600 text-white text-sm font-medium px-4 py-2 rounded-lg transition duration-200"
                >
                  Try Again
                </button>
              </div>
            </div>
          </div>
        </div>
      ) : (
        <>
          <SliderBanner />
          <ProductLists products={products} />
        </>
      )}
    </div>
  );
};

export default Home;
