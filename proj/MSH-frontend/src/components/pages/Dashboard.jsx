import {useEffect} from 'react'
import Navbar from '../layout/Navbar'
import { useNavigate } from 'react-router-dom';

const Dashboard = () => {
  const navigate = useNavigate();

  useEffect(() => {
    if (!localStorage.getItem('user')) {
      navigate('/login?redirect=dashboard');
    }
  }, []);

  return (
    <div>
      <DeviceCard />
    </div>
  )
}

export default Dashboard
