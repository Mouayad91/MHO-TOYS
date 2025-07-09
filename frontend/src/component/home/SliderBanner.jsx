import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation, Pagination, Autoplay } from "swiper/modules";
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";

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
          delay: 3000,
          disableOnInteraction: false,
          pauseOnMouseEnter: true,
        }}
        loop={true}
        className="slider-banner rounded-lg overflow-hidden shadow-2xl h-64 sm:h-80 md:h-96 lg:h-[400px] w-full"
      >
        <SwiperSlide>
          <div className="relative h-full">
            <img
              src={Banner1}
              alt="Slider Banner"
              className="w-full h-full object-cover"
            />

            <div className="absolute inset-0 bg-gradient-to-r from-black/60 to-black/20 flex items-center">
              <div className="w-full px-4 sm:px-6 md:px-8 lg:px-12">
                <div className="max-w-xs sm:max-w-sm md:max-w-lg text-white">
                  <h1 className="text-xl sm:text-2xl md:text-3xl lg:text-4xl font-bold mb-2 sm:mb-3 md:mb-4 font-fun animate-fade-in-up">
                    🧸 Fun Starts Here!
                  </h1>
                  <p className="mb-4 sm:mb-5 md:mb-6 text-sm sm:text-base md:text-lg opacity-90 leading-relaxed">
                    Explore joyful toys for kids of all ages — safe, colorful, and exciting!
                  </p>
                </div>
              </div>
            </div>
          </div>
        </SwiperSlide>
                
        <SwiperSlide>
          <div className="relative h-full">
            <img
              src={Banner2}
              alt="Special Offers"
              className="w-full h-full object-cover"
            />

            <div className="absolute inset-0 bg-gradient-to-r from-red-600/70 to-red-400/30 flex items-center">
              <div className="w-full px-4 sm:px-6 md:px-8 lg:px-12">
                <div className="max-w-xs sm:max-w-sm md:max-w-lg text-white">
                  <h1 className="text-xl sm:text-2xl md:text-3xl lg:text-4xl font-bold mb-2 sm:mb-3 md:mb-4 font-fun animate-fade-in-up">
                    🎉 Special Offers
                  </h1>
                  <p className="mb-4 sm:mb-5 md:mb-6 text-sm sm:text-base md:text-lg opacity-90 leading-relaxed">
                    Grab up to 50% off on selected toys. Limited time only!
                  </p>
                </div>
              </div>
            </div>
          </div>
        </SwiperSlide>
                
        <SwiperSlide>
          <div className="relative h-full">
            <img
              src={Banner3}
              alt="New Arrivals"
              className="w-full h-full object-cover"
            />
            
            <div className="absolute inset-0 bg-gradient-to-r from-blue-600/70 to-blue-400/30 flex items-center">
              <div className="w-full px-4 sm:px-6 md:px-8 lg:px-12">
                <div className="max-w-xs sm:max-w-sm md:max-w-lg text-white">
                  <h1 className="text-xl sm:text-2xl md:text-3xl lg:text-4xl font-bold mb-2 sm:mb-3 md:mb-4 font-fun animate-fade-in-up">
                    🎈 New Arrivals
                  </h1>
                  <p className="mb-4 sm:mb-5 md:mb-6 text-sm sm:text-base md:text-lg opacity-90 leading-relaxed">
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