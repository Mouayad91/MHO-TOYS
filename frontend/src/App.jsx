
import './App.css'
import Footer from './component/footer/Footer.jsx';
import Home from './component/home/Home.jsx'    
import { Routes, Route } from "react-router-dom";
import About from './component/about/About.jsx';
function App() {

  return (
    <div className="App">
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/about" element={<About />} />
       
      </Routes>
    </div>              
  )
}

export default App
