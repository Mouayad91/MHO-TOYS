import { Routes, Route } from "react-router-dom";
import React from "react";
import Home from "./component/home/Home";
import About from "./component/about/About";
import Contact from "./component/contact/Contact";
import Navigation from "./component/shared/Navigation";
import Footer from "./component/footer/Footer";

function App() {
  return (
    <div className="flex flex-col min-h-screen">
      <Navigation />
      <main className="flex-grow">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/about" element={<About />} />
          <Route path="/contact" element={<Contact />} />
        </Routes>
      </main>
      <Footer />
    </div>
  );
}

export default App;
