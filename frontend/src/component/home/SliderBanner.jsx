import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation, Pagination, Autoplay } from "swiper/modules";
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";

// Import your banner images
import Banner1 from "../../assets/Banner1.jpg";
import Banner2 from "../../assets/Banner2.jpg";
import Banner3 from "../../assets/Banner3.jpg";

const SliderBanner = () => {

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 mt-6">
      <Swiper
        modules={[Navigation, Pagination, Autoplay]}
        spaceBetween={0}
        slidesPerView={1}
        navigation={{
          nextEl: '.swiper-button-next',
          prevEl: '.swiper-button-prev',
        }}
        pagination={{
          clickable: true,
          dynamicBullets: true,
        }}
        autoplay={{
          delay: 5000,
          disableOnInteraction: false,
          pauseOnMouseEnter: true,
        }}
        loop={true}
        style={{ height: "400px", width: "100%" }}
        className="slider-banner rounded-lg overflow-hidden shadow-2xl"
      >
        <SwiperSlide>
          <div className="relative h-full">
            {/* Background Image */}
            <img
              src={Banner1}
              alt="Slider Banner"
              className="w-full h-full object-cover"
            />

            {/* Overlay Content */}
            <div className="absolute inset-0 bg-gradient-to-r from-black/60 to-black/20 flex items-center">
              <div className="w-full px-8 md:px-12">
                <div className="max-w-lg text-white">
                  <h1 className="text-3xl md:text-4xl font-bold mb-4 font-fun animate-fade-in-up">
                    🧸 Fun Starts Here!
                  </h1>
                  <p className="mb-6 text-base md:text-lg opacity-90">
                    Explore joyful toys for kids of all ages — safe, colorful, and exciting!
                  </p>
                </div>
              </div>
            </div>
          </div>
        </SwiperSlide>
                
        <SwiperSlide>
          <div className="relative h-full">
            {/* Background Image */}
            <img
              src={Banner2}
              alt="Special Offers"
              className="w-full h-full object-cover"
            />

            {/* Overlay Content */}
            <div className="absolute inset-0 bg-gradient-to-r from-red-600/70 to-red-400/30 flex items-center">
              <div className="w-full px-8 md:px-12">
                <div className="max-w-lg text-white">
                  <h1 className="text-3xl md:text-4xl font-bold mb-4 font-fun animate-fade-in-up">
                    🎉 Special Offers
                  </h1>
                  <p className="mb-6 text-base md:text-lg opacity-90">
                    Grab up to 50% off on selected toys. Limited time only!
                  </p>
                </div>
              </div>
            </div>
          </div>
        </SwiperSlide>
                
        <SwiperSlide>
          <div className="relative h-full">
            {/* Background Image */}
            <img
              src={Banner3}
              alt="New Arrivals"
              className="w-full h-full object-cover"
            />
            
            {/* Overlay Content */}
            <div className="absolute inset-0 bg-gradient-to-r from-blue-600/70 to-blue-400/30 flex items-center">
              <div className="w-full px-8 md:px-12">
                <div className="max-w-lg text-white">
                  <h1 className="text-3xl md:text-4xl font-bold mb-4 font-fun animate-fade-in-up">
                    🎈 New Arrivals
                  </h1>
                  <p className="mb-6 text-base md:text-lg opacity-90">
                    Check out our latest collection of trending toys and games!
                  </p>
                </div>
              </div>
            </div>
          </div>
        </SwiperSlide>
            </Swiper>
        </div>
  );
};

export default SliderBanner;