import { useEffect, useState } from 'react'
import { useNavigate, Link } from 'react-router-dom';
import { BASE_API_URL } from '../../constants';

import { IoIosCloudy, IoIosArrowDown } from "react-icons/io";
import { FaTemperatureHalf } from "react-icons/fa6";

import DeviceCard from '../layout/DeviceCard'
import axios from 'axios';

import Header from '../layout/Header';
import Navbar from '../layout/Navbar';

const Dashboard = () => {
  const navigate = useNavigate();

  const user = JSON.parse(localStorage.getItem('user')) ? JSON.parse(localStorage.getItem('user')) : null;
  
  const [selectedRoom, setSelectedRoom] = useState("");
  const [filteredOutDevices, setFilteredOutDevices] = useState([]);
  const [filteredInputDevices, setFilteredInputDevices] = useState([]);
  const [outputDevices, setOutputDevices] = useState([]);
  const [rooms, setRooms] = useState([]);
  const [inputDevices, setInputDevices] = useState([]);



  const getOutputDevices = async () => {
    try {
      const res = await axios.get(`${BASE_API_URL}/outputs/list`, null);
      if (res.status === 200) {
        console.log('received output devices');
        setOutputDevices(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  }

  const getInputDevices = async () => {
    try {
      const res = await axios.get(`${BASE_API_URL}/sources/list`, null);
      if (res.status === 200) {
        console.log('received input devices (sensors)');
        setInputDevices(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  }

 const getAllRooms = async () => {
   try {
       const res = await axios.get(`${BASE_API_URL}/room/list`, null);
       if (res.status === 200) {
           console.log('received rooms');
           setRooms(res.data);
       }
   } catch (error) {
       console.log(error);
   }
 }

  useEffect(() => {
    if (localStorage.getItem('user')) {
      getOutputDevices();
      getAllRooms();
    } else {
      navigate('/login?redirect=dashboard');
    }
  }, []);

  useEffect(() => {
    if (selectedRoom) {
      console.log(selectedRoom.id);
      const roomOutDevices = outputDevices.filter(outdevice => outdevice.location === selectedRoom.name);
      setFilteredOutDevices(roomOutDevices);
      setFilteredInputDevices(inputDevices);
    } else {
      setFilteredOutDevices([]);
    }
  }, [selectedRoom, outputDevices]);

 return (
   <div className='flex flex-col pt-4 ml-5'>
    <div className="mx-[5%] mt-4 flex justify-between">
      <Navbar />
      <div className="flex flex-col w-full h-full">
        <Header />
      <div className='flex flex-col pl-[10%] pt-[2%] w-[100%]'>
        <div className="carousel w-[70%] rounded-3xl">
          <div id="slide1" className="carousel-item relative w-[100%]">
            <div className="absolute flex justify-between transform -translate-y-1/2 left-5 right-5 top-1/2">
                <a href="#slide3" className="btn btn-outline btn-circle btn-secondary">❮</a> 
                <a href="#slide2" className="btn btn-outline btn-circle btn-secondary">❯</a>
            </div>
            <div className={`hero rounded-3xl w-[100%] h-[100%] bg-[url('/src/assets/images/home1.jpg')]`}>
              <div className="hero-overlay rounded-3xl w-[100%] h-[100%] bg-[#101010] bg-opacity-50 "></div>
              <div className="text-center text-white hero-content">
                <div className="max-w-xl">
                  <h2 className="mb-5 text-5xl font-bold">Welcome Back {user.firstname}!</h2>
                  <p className="mb-5 text-2xl">
                    Welcome home! We hope you're having a great day.
                  </p>
                </div>
              </div>
            </div>
          </div> 
          <div id="slide2" className="carousel-item relative w-[100%]">
          <div className="absolute flex justify-between transform -translate-y-1/2 left-5 right-5 top-1/2">
                <a href="#slide1" className="btn btn-outline btn-circle btn-secondary">❮</a> 
                <a href="#slide3" className="btn btn-outline btn-circle btn-secondary">❯</a>
            </div>
            <div className={`hero rounded-3xl bg-[url('/src/assets/images/home2.jpg')]`}>
              <div className="hero-overlay rounded-3xl bg-[#101010] bg-opacity-50 "></div>
              <div className="text-center text-white hero-content">
                <div className="max-w-xl">
                  <h1 className="mb-5 text-5xl font-bold">Today's Weather</h1>
                  <div className='flex flex-row items-center'>
                    <FaTemperatureHalf className='text-5xl'/> <p className="text-2xl">Enjoy a comfortable home at a cozy 25°C.</p>
                  </div>
                  <div className='flex flex-row items-center'>
                    <IoIosCloudy className='text-5xl'/> <p className="text-2xl">It's a cloudy day today.</p>
                  </div>
                </div>
              </div>
            </div>
          </div> 
          <div id="slide3" className="carousel-item relative w-[100%]">
          <div className="absolute flex justify-between transform -translate-y-1/2 left-5 right-5 top-1/2">
                <a href="#slide2" className="btn btn-outline btn-circle btn-secondary">❮</a> 
                <a href="#slide1" className="btn btn-outline btn-circle btn-secondary">❯</a>
            </div>
            <div className={`hero rounded-3xl bg-[url('/src/assets/images/home3.jpg')]`}>
              <div className="hero-overlay rounded-3xl bg-[#101010] bg-opacity-50 "></div>
              <div className="text-center text-white hero-content">
                <div className="max-w-xl">
                  <h1 className="mb-5 text-5xl font-bold">Missing Something</h1>
                  <p className="mb-5 text-2xl">
                    Help us to know what it is, create you're own routines that make you feel more confortable at you're home
                  </p>
                </div>
              </div>
            </div>
          </div> 
        </div>
        <div className='pt-5 flex flex-row text-center items-center space-x-[15%]'>
          <h1 className='text-6xl font-bold'>Devices</h1>
          <div className="dropdown">
            <div tabIndex={0} role="button" className="m-1 text-xl text-white btn btn-primary">{selectedRoom ? selectedRoom.name : "Select Room"} <IoIosArrowDown/></div>
            <ul className="dropdown-content z-[10] menu p-2 shadow bg-base-100 rounded-box border primary border-[4px] border-primary w-52 text-xl">
              <li onClick={(e) => {e.stopPropagation(); setSelectedRoom("");}}>
                <button>Select Room</button>
              </li>
              {rooms.map((room, idx) => (
                <li key={idx} onClick={(e) => {e.stopPropagation(); setSelectedRoom(room);}}>
                  <button>{room.name}</button>
                </li>
              ))}
            </ul>
          </div>
          <Link to='/devices'><p className='font-semibold text-neutral'>See more</p></Link>
        </div>
        <div className='divider w-[70%] text-xl font-semibold pt-[4%] pb-[1%]'> Output Devices </div>
        <div className='flex flex-row pl-[10%] max-w-[85vw] overflow-x-scroll overflow-y-hidden'>
          {selectedRoom !== "" ? 
            filteredOutDevices.length > 0 ?
              <div className='flex flex-row space-x-8'>
                {filteredOutDevices.map((device) => (
                  <DeviceCard key={device.id} device={device} room={selectedRoom}/>
                ))}
              </div>
            :
              <h2 className='text-xl'>No devices found</h2>
          :
            <h2 className='text-xl'>No devices found</h2>
          }
        </div>
        <div className='divider w-[70%] text-xl font-semibold pt-[4%] pb-[1%]'> Input Devices </div>
        <div className='flex flex-row pl-[10%] pb-[10%] max-w-[85vw] overflow-x-scroll overflow-y-hidden'>
          {selectedRoom !== "" ? 
            filteredInputDevices.length > 0 ?
              <div className='flex flex-row space-x-8'>
                {filteredInputDevices.map((device) => (
                  <DeviceCard key={device.id} device={device} room={selectedRoom}/>
                ))}
              </div>
            :
              <h2 className='text-xl'>No devices found</h2>
          :
            <h2 className='text-xl'>No devices found</h2>
          }
        </div>
      </div>
   </div>
    </div>
</div>  
  
 )
}

export default Dashboard
