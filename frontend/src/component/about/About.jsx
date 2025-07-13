import React from 'react';
import aboutImage from '../../assets/aboutImage.jpg';
import Navigation from '../shared/Navigation';

const About = () => {
  return (
    <div className="min-h-screen bg-white">
      <Navigation />
      
      <section className="relative bg-gradient-to-r from-pink-50 to-blue-50 py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center">
            <h1 className="text-4xl sm:text-5xl lg:text-6xl font-bold text-gray-900 mb-6">
              About   
              <span className="text-2xl sm:text-3xl font-fun font-bold tracking-wide"> </span>
              <span className="text-blue-500">M</span>
              <span className="text-yellow-500">H</span>
              <span className="text-purple-500">O</span>
              <span> </span>
              <span className="text-green-500">T</span>
              <span className="text-red-500">O</span>
              <span className="text-indigo-500">Y</span>
              <span className="text-orange-500">S</span>
           </h1>
            <p className="text-xl sm:text-2xl text-gray-600 max-w-3xl mx-auto leading-relaxed">
              Creating magical moments and sparking imagination through safe, educational, and fun toys for children of all ages.
            </p>
          </div>
        </div>
      </section>

      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">
            <div className="relative">
              <div className="relative overflow-hidden rounded-2xl shadow-2xl">
                <img
                  src={aboutImage}
                  alt="About MHO TOYS - Children playing with educational toys"
                  className="w-full h-96 lg:h-[500px] object-cover transition-transform duration-500 hover:scale-105"
                  onError={(e) => {
                    e.target.src = 'https://via.placeholder.com/600x500?text=MHO+TOYS+About+Image';
                  }}
                />
                <div className="absolute -top-4 -right-4 w-20 h-20 bg-yellow-400 rounded-full opacity-80"></div>
                <div className="absolute -bottom-4 -left-4 w-16 h-16 bg-pink-400 rounded-full opacity-80"></div>
              </div>
            </div>

            {/* Content Section */}
            <div className="space-y-8">
              <div>
                <h2 className="text-3xl sm:text-4xl font-bold text-gray-900 mb-6">
                  Our Story 📖
                </h2>
                <p className="text-lg text-gray-600 leading-relaxed mb-6">
                  Founded with a passion for childhood development and learning, MHO TOYS has been dedicated to providing 
                  high-quality, safe, and educational toys that inspire creativity and foster growth in children.
                </p>
                <p className="text-lg text-gray-600 leading-relaxed">
                  We believe that play is the foundation of learning, and every toy we offer is carefully selected to 
                  ensure it meets the highest standards of safety, quality, and educational value.
                </p>
              </div>

              <div className="bg-gradient-to-r from-blue-50 to-purple-50 rounded-xl p-6">
                <h3 className="text-2xl font-bold text-gray-900 mb-4">Our Mission 🎯</h3>
                <p className="text-gray-700 leading-relaxed">
                  To create a world where every child has access to toys that not only entertain but also educate, 
                  inspire, and help them develop essential skills for their future.
                </p>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section className="py-16 bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl sm:text-4xl font-bold text-gray-900 mb-4">
              Our Values 💝
            </h2>
            <p className="text-xl text-gray-600 max-w-2xl mx-auto">
              The principles that guide everything we do
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
            <div className="bg-white rounded-xl p-6 shadow-lg hover:shadow-xl transition-shadow duration-300">
              <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mb-4 mx-auto">
                <span className="text-2xl">🛡️</span>
              </div>
              <h3 className="text-xl font-bold text-gray-900 mb-3 text-center">Safety First</h3>
              <p className="text-gray-600 text-center">
                All our toys meet or exceed international safety standards, ensuring worry-free play for children.
              </p>
            </div>

            <div className="bg-white rounded-xl p-6 shadow-lg hover:shadow-xl transition-shadow duration-300">
              <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mb-4 mx-auto">
                <span className="text-2xl">🧠</span>
              </div>
              <h3 className="text-xl font-bold text-gray-900 mb-3 text-center">Educational Value</h3>
              <p className="text-gray-600 text-center">
                Every toy is designed to promote learning, creativity, and cognitive development.
              </p>
            </div>

            <div className="bg-white rounded-xl p-6 shadow-lg hover:shadow-xl transition-shadow duration-300">
              <div className="w-16 h-16 bg-purple-100 rounded-full flex items-center justify-center mb-4 mx-auto">
                <span className="text-2xl">⭐</span>
              </div>
              <h3 className="text-xl font-bold text-gray-900 mb-3 text-center">Quality Materials</h3>
              <p className="text-gray-600 text-center">
                We use only the finest, non-toxic materials to ensure durability and child safety.
              </p>
            </div>

            <div className="bg-white rounded-xl p-6 shadow-lg hover:shadow-xl transition-shadow duration-300">
              <div className="w-16 h-16 bg-yellow-100 rounded-full flex items-center justify-center mb-4 mx-auto">
                <span className="text-2xl">🎉</span>
              </div>
              <h3 className="text-xl font-bold text-gray-900 mb-3 text-center">Fun & Engagement</h3>
              <p className="text-gray-600 text-center">
                Above all, our toys are designed to bring joy and excitement to children's playtime.
              </p>
            </div>
          </div>
        </div>
      </section>

      <section className="py-16 bg-gradient-to-r from-pink-500 to-purple-600">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-2 lg:grid-cols-4 gap-8">
            <div className="text-center">
              <div className="text-4xl sm:text-5xl font-bold text-white mb-2">1000+</div>
              <div className="text-pink-100 text-lg">Happy Children</div>
            </div>
            <div className="text-center">
              <div className="text-4xl sm:text-5xl font-bold text-white mb-2">500+</div>
              <div className="text-pink-100 text-lg">Quality Toys</div>
            </div>
            <div className="text-center">
              <div className="text-4xl sm:text-5xl font-bold text-white mb-2">50+</div>
              <div className="text-pink-100 text-lg">Partner Brands</div>
            </div>
            <div className="text-center">
              <div className="text-4xl sm:text-5xl font-bold text-white mb-2">5⭐</div>
              <div className="text-pink-100 text-lg">Customer Rating</div>
            </div>
          </div>
        </div>
      </section>

      <section className="py-16 bg-white">
        <div className="max-w-4xl mx-auto text-center px-4 sm:px-6 lg:px-8">
          <h2 className="text-3xl sm:text-4xl font-bold text-gray-900 mb-6">
            Ready to Explore Our Collection? 🎮
          </h2>
          <p className="text-xl text-gray-600 mb-8">
            Discover amazing toys that will spark your child's imagination and support their development.
          </p>

        </div>
      </section>
    </div>
  );
};

export default About;