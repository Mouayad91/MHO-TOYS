import Navigation from "../shared/Navigation";
import SliderBanner from "./SliderBanner";
import ProductLists from "../product/ProductLists";
import products from "../../data/products";
const Home = () => {
  
  return (
    <div className="min-h-screen bg-white">
      <Navigation />
      <SliderBanner />
      <ProductLists products={products} />
    </div>
  );
};

export default Home;
