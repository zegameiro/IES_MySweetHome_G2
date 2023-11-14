import { BrowserRouter, Route, Routes } from 'react-router-dom'
import './App.css'

import Dashboard from './components/pages/Dashboard'
import LandPage from './components/pages/LandPage'

function App() {

  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<LandPage/>} />
          <Route path="/about" element={<h1>About</h1>} />
          <Route path="/dashboard" element={<Dashboard />} />
        </Routes>
      </BrowserRouter>
      
    </>
  )
}

export default App
