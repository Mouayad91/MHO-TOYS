
import './App.css'
import Home from './component/home/Home.jsx'    
import { Routes, Route } from "react-router-dom";

function App() {

  return (
    <div className="min-h-screen bg-white">
      <Routes>
        <Route path="/" element={<Home />} />
      </Routes>
    </div>              
  )
}

export default App
