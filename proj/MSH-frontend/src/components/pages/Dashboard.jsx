import { useEffect, useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { BASE_API_URL } from '../../constants';

import { IoIosArrowDown } from 'react-icons/io';

import OutputDeviceCard from '../layout/OutputDeviceCard';
import InputDeviceCard from '../layout/InputDeviceCard';
import Header from '../layout/Header';
import Navbar from '../layout/Navbar';
import Alert from '../layout/Alert';

import axios from 'axios';
import Carroussel from '../layout/Carroussel';

const Dashboard = () => {
  const navigate = useNavigate();

  const user = JSON.parse(localStorage.getItem('user'))
    ? JSON.parse(localStorage.getItem('user'))
    : null;

  const [selectedRoom, setSelectedRoom] = useState(null);
  const [filteredOutDevices, setFilteredOutDevices] = useState([]);
  const [outputDevices, setOutputDevices] = useState([]);
  const [inputDevices, setInputDevices] = useState([]);
  const [alerts, setAlerts] = useState([]);
  const [rooms, setRooms] = useState([]);

  const getOutputDevices = async () => {
    try {
      const res = await axios.get(`${BASE_API_URL}/outputs/list`, null);
      if (res.status === 200) {
        setOutputDevices(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const getInputDevices = async () => {
    try {
      const res = await axios.get(`${BASE_API_URL}/sources/list`, null);
      if (res.status === 200) {
        setInputDevices(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const getAllRooms = async () => {
    try {
      const res = await axios.get(`${BASE_API_URL}/room/list`, null);
      if (res.status === 200) {
        setRooms(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const getAlerts = async () => {
    try {
      const res = await axios.get(`${BASE_API_URL}/alerts/list`, null);
      if (res.status === 200) {
        setAlerts(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const removeAlert = (alertId) => {
    setAlerts(alerts.filter((alert) => alert.id !== alertId));
  };

  useEffect(() => {
    if (localStorage.getItem('user')) {
      getOutputDevices();
      getInputDevices();
      getAllRooms();
      getAlerts();
    } else {
      navigate('/login?redirect=dashboard');
    }
  }, []);

  useEffect(() => {
    const interval = setInterval(() => {
      getAlerts();
    }, 1); // Fetch new alerts every 5 seconds

    return () => clearInterval(interval); // Clean up on unmount
  }, []);

  useEffect(() => {
    if (selectedRoom) {
      const roomOutDevices = outputDevices.filter((outdevice) =>
        selectedRoom.devices.includes(outdevice.id)
      );
      setFilteredOutDevices(roomOutDevices);
    } else {
      setFilteredOutDevices(outputDevices.filter((outdevice) => outdevice));
    }
  }, [selectedRoom, outputDevices]);

  return (
    <div className="flex flex-col overflow-y-auto pb-[10vh]">
          <div className="mx-[5%] mt-4 flex flex-row  justify-between">

        <Navbar />
        <div className="flex flex-col max-w-[83vw]">
          <Header />
          <div className="flex flex-row mx-4">
            <div className="flex flex-col w-[66%] p-4">
              <Carroussel />
              <div className="flex flex-col w-full m-4">
                <div className="flex flex-row items-center justify-between w-full p-4 text-center">
                  <h1 className="text-6xl font-bold">Devices</h1>
                  <div className="dropdown">
                    <div
                      tabIndex={0}
                      role="button"
                      className="m-1 text-xl text-white btn btn-primary"
                    >
                      {selectedRoom ? selectedRoom.name : 'Select Room'}
                      <IoIosArrowDown />
                    </div>
                    <ul
                      tabIndex="0"
                      className="menu dropdown-content z-[1] p-2 shadow bg-base-100 text-xl rounded-box w-52 border border-primary"
                    >
                      <li>
                        <button
                          onClick={(e) => {
                            e.stopPropagation();
                            setSelectedRoom(null);
                          }}
                        >
                          Select Room
                        </button>
                      </li>
                      {rooms.map((room, idx) => (
                        <li
                          key={idx}
                          onClick={(e) => {
                            e.stopPropagation();
                            setSelectedRoom(room);
                          }}
                        >
                          <button>{room.name}</button>
                        </li>
                      ))}
                    </ul>
                  </div>
                </div>

                <div className="w-full text-xl font-semibold divider ">
                  Output Devices
                </div>
                <div className="flex flex-row">
                  {selectedRoom !== '' ? (
                    filteredOutDevices.length > 0 ? (
                      <div className="flex flex-row overflow-x-scroll custom-scrollbar">
                        {filteredOutDevices.map((device) => (
                          <span
                            key={device.id}
                            className="m-2"
                          >
                            <OutputDeviceCard
                              device={device}
                              room={selectedRoom}
                            />
                          </span>
                        ))}
                      </div>
                    ) : (
                      <span className="flex flex-row justify-center w-full text-xl">
                        <h2 className="m-8 text-xl">No devices found</h2>
                      </span>
                    )
                  ) : (
                    <span className="flex flex-row justify-center w-full text-xl">
                      <h2 className="m-8 text-xl">No devices found</h2>
                    </span>
                  )}
                </div>
                <div className="w-full text-xl font-semibold divider">
                  Input Devices
                </div>
                <div className="flex flex-row w-full">
                  {inputDevices.length > 0 ? (
                    <div className="flex flex-row w-full space-x-4 overflow-x-auto overflow-y-hidden custom-scrollbar">
                      {inputDevices.map((device) => (
                        <InputDeviceCard
                          key={device.id}
                          device={device}
                        />
                      ))}
                    </div>
                  ) : (
                    <h2 className="text-xl">No devices found</h2>
                  )}
                </div>
              </div>
            </div>
            <div className=" absolute right-[2rem] flex flex-col items-start w-[30%] p-4 rounded-2xl">
              <h1 className="p-2 text-4xl font-bold">Alerts</h1>
              <hr />
              {alerts.length > 0 ? (
                alerts.map((alert) => (
                  <span key={alert.id}>
                    <Alert
                      alert={alert}
                      removeAl={removeAlert}
                    />
                  </span>
                ))
              ) : (
                <span className="flex justify-center p-8">
                  <p className="text-xl">No alerts found</p>
                </span>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
