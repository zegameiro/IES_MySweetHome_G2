import { useEffect, useState } from 'react'
import Navbar from '../layout/Navbar'
import DeviceCard from '../layout/DeviceCard'
import { useNavigate } from 'react-router-dom';
import { BASE_API_URL } from '../../constants';
import axios from 'axios';

import home1 from '../../assets/images/home1.jpg';

const Dashboard = () => {
  const navigate = useNavigate();

  const [user, setUser] = useState(JSON.parse(localStorage.getItem('user')));
  const [devices, setDevices] = useState([]);
  const [rooms, setRooms] = useState([])

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
        const res = await axios.get(`${BASE_API_URL}/room/list`, null);
        if (res.status === 200) {
            console.log('received data');
            console.log(res.data);
            setRooms(res.data);
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
    <div className='flex'>
      <Navbar/>
      <div className='flex flex-col'>
        <div className='pb-4'>
          <img src={home1} className='w-[450px] h-[270px]'/>
          <p>{user["firstname"]}</p>
        </div>
        <div className='flex flex-row'>
          {devices.map(device => (
            <DeviceCard device={device} isBig rooms={rooms}/>
          ))}
        </div>
      </div>
    </div>
  )
}

export default Dashboard
