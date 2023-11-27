import { BrowserRouter, Route, Routes } from 'react-router-dom'
import Dashboard from './components/pages/Dashboard'
import LandPage from './components/pages/LandPage'
import LoginPage from './components/pages/LoginPage'
import RegisterPage from './components/pages/RegisterPage'
import AddPage from './components/pages/AddPage'
import DevicesPage from './components/pages/DevicesPage'

function App() {

  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<LandPage/>} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/add" element={<AddPage />} />
          <Route path="/devices" element={<DevicesPage />} />
          <Route path="*" element={<h1>Not Found</h1>} />
        </Routes>
      </BrowserRouter>
      
    </>
  )
}

export default App
