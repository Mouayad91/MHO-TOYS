import React, { useState } from "react";

const Contact = () => {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    message: "",
    privacyAccepted: false,
  });

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!formData.privacyAccepted) {
      alert("Please accept the privacy policy.");
      return;
    }
    console.log("Submitted contact form:", formData);
    alert("Thank you! We will get back to you soon.");
  };

  return (
    <div className="max-w-4xl mx-auto px-4 py-10">
      <div className="text-center mb-8">
        <h1 className="text-4xl font-bold text-purple-700 mb-3">Contact Us</h1>
        <p className="text-gray-600 text-lg">
          Have a question, feedback, or need help? Our customer support team is always here for you.
          Fill out the form below and we’ll respond as soon as possible.
        </p>
      </div>

      <form
        onSubmit={handleSubmit}
        className="bg-white p-6 rounded-2xl shadow-md space-y-6"
      >
        <div>
          <label htmlFor="name" className="block font-semibold text-gray-700 mb-1">
            Your Name
          </label>
          <input
            type="text"
            id="name"
            name="name"
            value={formData.name}
            onChange={handleChange}
            placeholder="e.g. Sarah Thompson"
            required
            className="w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-purple-400 focus:outline-none"
          />
        </div>

        <div>
          <label htmlFor="email" className="block font-semibold text-gray-700 mb-1">
            Email Address
          </label>
          <input
            type="email"
            id="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            placeholder="you@example.com"
            required
            className="w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-purple-400 focus:outline-none"
          />
        </div>

        <div>
          <label htmlFor="message" className="block font-semibold text-gray-700 mb-1">
            Your Message
          </label>
          <textarea
            id="message"
            name="message"
            rows="5"
            value={formData.message}
            onChange={handleChange}
            placeholder="Write your message here..."
            required
            className="w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-purple-400 focus:outline-none resize-none"
          ></textarea>
        </div>

        <div className="flex items-start">
          <input
            type="checkbox"
            id="privacyAccepted"
            name="privacyAccepted"
            checked={formData.privacyAccepted}
            onChange={handleChange}
            className="mt-1 mr-2"
          />
          <label htmlFor="privacyAccepted" className="text-sm text-gray-600">
            I agree to the <span className="text-purple-600 underline cursor-pointer">privacy policy</span>.
          </label>
        </div>

        <button
          type="submit"
          className="w-full bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 text-white py-3 rounded-xl font-semibold shadow-lg hover:shadow-xl transition-all duration-200"
        >
          Send Message
        </button>
      </form>
    </div>
  );
};

export default Contact;
