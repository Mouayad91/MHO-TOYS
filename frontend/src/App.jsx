
import './App.css'
import Footer from './component/footer/Footer.jsx';
import Home from './component/home/Home.jsx'    
import { Routes, Route } from "react-router-dom";

function App() {

  return (
    <div className="min-h-screen bg-white">
      <Home />
      <Footer />
    </div>              
  )
}

export default App
