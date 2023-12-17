import { useState, useEffect } from 'react';

import Navbar from '../layout/Navbar';
import Header from '../layout/Header';
import OutputDeviceCard from '../layout/OutputDeviceCard';

import axios from 'axios';
import { BASE_API_URL } from '../../constants';
import { useNavigate } from 'react-router-dom';

const AddRoutine = () => {
  const [timeTab, setTimeTab] = useState(true);
  const [devices, setDevices] = useState([]);
  const [selectDevice, setSelectDevice] = useState(null);

  const navigate = useNavigate();

  const user = JSON.parse(localStorage.getItem('user'))
    ? JSON.parse(localStorage.getItem('user'))
    : null;

  const getDevices = async () => {
    try {
      const res = await axios.get(`${BASE_API_URL}/outputs/list`, null);
      if (res.status === 200) {
        setDevices(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    if (user) {
      getDevices();
    } else {
      navigate('/login?redirect=dashboard');
    }
  }, []);

  return (
    <div className="mx-[5%] mt-4 flex justify-between">
      <Navbar />
      <div className="flex flex-col w-full h-full">
        <Header />
        <div className="w-full mx-4">
          <h1 className="m-4 text-4xl font-bold">Add new routine</h1>
          <div>
            <h2 className="m-4 text-3xl text-slate-500">Select a known device</h2>
            <span className="divider" />
            <div className="flex flex-row justify-start overflow-auto">
              {devices.map((device) => (
                <span
                  className={`m-2 rounded-2xl ${
                    selectDevice == device && 'ring-4 ring-primary'
                  }}`}
                  key={device?.id}
                  onClick={() => {
                    setSelectDevice(device);
                  }}
                >
                  <OutputDeviceCard
                    device={device}
                    room={device?.location}
                  />
                </span>
              ))}
            </div>
            <div className="flex flex-row justify-between">
              {timeTab ? (
                <div className="flex flex-col items-center justify-center m-4">
                  <h1 className="text-2xl font-bold">Sensor Routine</h1>
                  <button className="btn btn-primary">Add</button>
                </div>
              ) : null}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AddRoutine;
