import { useState, useEffect } from 'react';
import Navbar from '../layout/Navbar';

import { BASE_API_URL } from '../../constants';

import axios from 'axios';
import Header from '../layout/Header';
import OutputDeviceCard from '../layout/OutputDeviceCard';

import { useNavigate } from 'react-router-dom';

const DevicesPage = () => {
  const navigate = useNavigate();
  const [devices, setDevices] = useState([]);
  const [rooms, setRooms] = useState([]);
  const search = new URLSearchParams(window.location.search).get('search');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (localStorage.getItem('user')) {
      getDevices();
      getRooms();
    } else {
      navigate('/login?redirect=dashboard');
    }
  }, []);

  useEffect(() => {
    console.log('search changed -> ', search);
  }, [search]);

  const user = JSON.parse(localStorage.getItem('user'))
    ? JSON.parse(localStorage.getItem('user'))
    : null;

  const getDevices = async () => {
    try {
      const res = await axios.get(`${BASE_API_URL}/outputs/list`);
      if (res.status === 200) {
        console.log('received data');
        console.log('Devices -> ', res.data);
        setDevices(res.data);
        setLoading(false);
      }
    } catch (error) {
      console.log(error);
      setLoading(true);
    }
  };

  const getRooms = async () => {
    try {
      const res = await axios.get(`${BASE_API_URL}/room/list`);
      if (res.status === 200) {
        console.log('received data');
        console.log('Rooms -> ', res.data);
        setRooms(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const getRoomDevice = (location) => {
    const room = rooms.find((room) => room.id === location);
    return room;
  }

  return (
    <div className="mx-[5%] mt-4 flex justify-between">
      <Navbar />
      <div className="flex flex-col w-full h-full">
        <Header />
        <div>
          <h1 className="m-4 text-4xl font-bold">
            {user?.firstname}'s devices
          </h1>
          {loading ? (
            <h1 className="m-4 text-4xl font-bold">Loading...</h1>
          ) : (
            <div className="flex flex-wrap mx-4">
              {devices != [] ? (
                devices
                  .filter((device) => {
                    if (search === null) {
                      return device;
                    } else if (
                      device?.name.toLowerCase().includes(search.toLowerCase())
                    ) {
                      return device;
                    }
                  })
                  .map((device) => {
                    return (
                      <span
                        className="m-2"
                        key={device?.id}
                      >
                        <OutputDeviceCard
                          device={device}
                          isBig
                          room={getRoomDevice(device?.location)}
                        />
                      </span>
                    );
                  })
              ) : (
                <h1 className="m-4 text-4xl font-bold">No devices found</h1>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default DevicesPage;
