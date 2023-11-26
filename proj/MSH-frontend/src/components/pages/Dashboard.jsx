import { useEffect, useState } from 'react'
import Navbar from '../layout/Navbar'
import DeviceCard from '../layout/DeviceCard'
import { useNavigate } from 'react-router-dom';
import { BASE_API_URL } from '../../constants';
import axios from 'axios';

const Dashboard = () => {
  const navigate = useNavigate();
  const [devices, setDevices] = useState([]);

  const getDevices = async () => {
    try {
      const res = await axios.get(`${BASE_API_URL}/outputs/list`, null);
      if (res.status === 200) {
        console.log('received data');
        setDevices(res.data);

      }
    } catch (error) {
      console.log(error);
    }
  }

  const getAllRooms = async () => {
    try {
        const rooms = await axios.get(`${BASE_API_URL}/room/list`, null);
        if (res.status === 200) {
            console.log('received data');
            rooms = res.data;
        }
    } catch (error) {
        console.log(error);
    }
  }

  useEffect(() => {
    if (!localStorage.getItem('user')) {
      navigate('/login?redirect=dashboard');
    } else {
      getDevices();
      getAllRooms();
    }
  }, []);

  return (
    <div className='flex flex row'>
      <Navbar/>
      {devices.map(device => (
        <DeviceCard device={device} isBig rooms={rooms}/>
      ))}
    </div>
  )
}

export default Dashboard
