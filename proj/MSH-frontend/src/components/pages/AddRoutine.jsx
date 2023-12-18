import { useState, useEffect } from 'react';

import Navbar from '../layout/Navbar';
import Header from '../layout/Header';
import OutputDeviceCard from '../layout/OutputDeviceCard';

import { HexColorPicker } from 'react-colorful';

import axios from 'axios';
import { BASE_API_URL } from '../../constants';
import { useNavigate } from 'react-router-dom';
import {
  BsBrightnessLow,
  BsBrightnessHighFill,
  BsVolumeMuteFill,
  BsVolumeUpFill,
} from 'react-icons/bs';
import { FaFire, FaSnowflake } from 'react-icons/fa6';

import { GiDrop, GiDroplets } from 'react-icons/gi'

const AddRoutine = () => {
  const [timeTab, setTimeTab] = useState(true);
  const [devices, setDevices] = useState([]);
  const [actions, setActions] = useState(null);
  const [action, setAction] = useState(null);

  const [color, setColor] = useState('#ffffff');
  const [slider, setSlider] = useState(50);

  const [temperature, setTemperature] = useState(20);
  const [humidity, setHumidity] = useState(25);

  const [categories, setCategories] = useState(null);
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

  const getActions = async (device) => {
    try {
      console.log('id -> ', device.id);
      const res = await axios.get(
        `${BASE_API_URL}/routines/check?device_id=${device.id}`
      );
      if (res.status === 200) {
        console.log(res.data);
        setActions(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const getCategories = async () => {
    try {
      const res = await axios.get(
        `${BASE_API_URL}/outputs/listCategories`,
        null
      );
      if (res.status === 200) {
        setCategories(res.data);
        console.log('cats -> ', res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    if (user) {
      getDevices();
      getCategories();
    } else {
      navigate('/login?redirect=dashboard');
    }
  }, []);

  useEffect(() => {
    if (selectDevice) {
      console.log(selectDevice);
      getActions(selectDevice);
    }
  }, [selectDevice]);

  const per_temperature = temperature * 1.6 + 16;

  return (
    <div className="mx-[5%] mt-4 flex justify-between">
      <Navbar />
      <div className="flex flex-col w-full h-full">
        <Header />
        <div className="w-full mx-4">
          <h1 className="m-4 text-4xl font-bold">Add new routine</h1>
          <div className="flex flex-col justify-between h-full w-[90%] ">
            <h2 className="m-4 text-3xl text-slate-500">
              Select a known device
            </h2>
            <span className="divider" />
            <div className="flex flex-row justify-start m-4 overflow-auto">
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
            <h2 className="mx-4 text-3xl text-slate-500">Select an action</h2>
            <span className="divider" />

            <div
              className={`flex flex-row justify-around m-4 overflow-auto transition-transform ease-in-out`}
            >
              {actions ? (
                actions.map((act, idx) => (
                  <div
                    key={idx}
                    className={`flex flex-col items-center justify-center p-4 m-4 text-2xl badge ${
                      action == act
                        ? 'badge-primary'
                        : 'badge-outline badge-accent'
                    } badge-xl `}
                    onClick={() => setAction(act)}
                  >
                    <h1>{act}</h1>
                  </div>
                ))
              ) : (
                <p className="text-2xl font-medium text-slate-500">
                  Select a device to see available actions
                </p>
              )}
            </div>

            <div className="flex flex-row justify-between">
              {(() => {
                switch (action) {
                  case 'Adjust Brightness':
                    return (
                      <div className="flex flex-col items-center justify-center w-full p-4 m-4 text-2xl shadow-lg bg-base-100 rounded-xl">
                        <div className="flex flex-row items-center justify-center w-full p-4 text-2xl font-medium">
                          <BsBrightnessLow className="mr-2 text-4xl text-slate-500" />
                          <input
                            type="range"
                            className={`range range-xl ${
                              slider >= 30 && slider < 70
                                ? 'range-primary'
                                : slider >= 70 && slider < 90
                                ? 'range-warning'
                                : slider >= 90
                                ? 'range-error'
                                : 'range-accent'
                            }`}
                            onChange={(e) => setSlider(e.target.value)}
                            step={5}
                            min="0"
                            max="100"
                            value={slider}
                          />
                          <BsBrightnessHighFill className="ml-2 text-4xl text-slate-500" />
                        </div>
                        <div className="flex flex-row items-center justify-center w-full p-4 text-2xl font-medium">
                          <h1>Selected brightness: {slider}% </h1>
                        </div>
                      </div>
                    );

                  case 'Change Color':
                    return (
                      <div className="flex flex-row items-center justify-center w-full p-4 m-4 text-2xl shadow-lg bg-base-100 rounded-xl">
                        <HexColorPicker
                          color={color}
                          onChange={setColor}
                          style={{
                            width: '50%',
                          }}
                        />
                        <div className="flex flex-col items-center justify-center w-1/4 p-4 text-2xl font-medium">
                          <h1>Selected color</h1>
                          <div
                            className="w-full h-[4rem] shadow-xl rounded-xl"
                            style={{
                              backgroundColor: color,
                            }}
                          />
                        </div>
                      </div>
                    );

                  case 'Adjust Temperature':
                    return (
                      <div className="flex flex-col items-center justify-center w-full p-4 m-4 text-2xl shadow-lg bg-base-100 rounded-xl">
                        <div
                          className={`text-4xl border radial-progress border-primary ${
                            temperature >= 0 && temperature < 15
                              ? 'text-primary'
                              : temperature >= 15 && temperature < 25
                              ? 'text-amber-400'
                              : temperature >= 25 && temperature < 35
                              ? 'text-warning'
                              : temperature >= 35
                              ? 'text-error'
                              : 'text-accent'
                          }`}
                          style={{
                            '--value': per_temperature.toFixed(0),
                            '--size': '12rem',
                            '--thickness': '10px',
                          }}
                          role="progressbar"
                        >
                          {temperature}°C
                        </div>
                        <div className="flex flex-row items-center justify-center w-full p-4 m-4 text-2xl shadow-lg text-slate-500 bg-base-100 rounded-xl">
                          <FaSnowflake />
                          <input
                            type="range"
                            min="-10"
                            max="50"
                            className={`m-4 range ${
                              temperature >= 0 && temperature < 15
                                ? 'range-primary'
                                : temperature >= 15 && temperature < 35
                                ? 'range-warning'
                                : temperature >= 35
                                ? 'range-error'
                                : 'range-accent'
                            }`}
                            onChange={(e) => setTemperature(e.target.value)}
                            value={temperature}
                          />
                          <FaFire />
                        </div>
                      </div>
                    );

                  case 'Adjust Volume':
                    return (
                      <div className="flex flex-col items-center justify-center w-full p-4 m-4 text-2xl shadow-lg bg-base-100 rounded-xl">
                        <div className="flex flex-row items-center justify-center w-full p-4 text-2xl font-medium">
                          <BsVolumeMuteFill className="mr-2 text-4xl text-slate-500" />
                          <input
                            type="range"
                            className={`range range-xl ${
                              slider >= 30 && slider < 70
                                ? 'range-primary'
                                : slider >= 70 && slider < 90
                                ? 'range-warning'
                                : slider >= 90
                                ? 'range-error'
                                : 'range-accent'
                            }`}
                            onChange={(e) => setSlider(e.target.value)}
                            step={5}
                            min="0"
                            max="100"
                            value={slider}
                          />
                          <BsVolumeUpFill className="ml-2 text-4xl text-slate-500" />
                        </div>
                        <div className="flex flex-row items-center justify-center w-full p-4 text-2xl font-medium">
                          <h1>Selected Volume: {slider}% </h1>
                        </div>
                      </div>
                    );

                  case 'Change Channel':
                    return <p>Change channel action selected</p>;

                  case 'Adjust Humidity level':
                    return (
                      <div className="flex flex-col items-center justify-center w-full p-4 m-4 text-2xl shadow-lg bg-base-100 rounded-xl">
                        <div
                          className={`text-4xl border radial-progress border-primary ${
                            humidity >= 0 && humidity < 25
                              ? 'text-accent'
                              : humidity >= 25 && humidity < 50
                              ? 'text-primary'
                              : humidity >= 50 && humidity < 75
                              ? 'text-warning'
                              : 'text-error'
                          }`}
                          style={{
                            '--value': humidity,
                            '--size': '12rem',
                            '--thickness': '10px',
                          }}
                          role="progressbar"
                        >
                          {humidity}%
                        </div>
                        <div className="flex flex-row items-center justify-center w-full p-4 m-4 text-4xl shadow-lg text-slate-500 bg-base-100 rounded-xl">
                          <GiDrop />
                          <input
                            type="range"
                            min="0"
                            max="100"
                            className={`m-4 range ${
                              humidity >= 0 && humidity < 25
                                ? 'range-accent'
                                : humidity >= 25 && humidity < 50
                                ? 'range-primary'
                                : humidity >= 50 && humidity < 75
                                ? 'range-warning'
                                : 'range-error'
                            }`}
                            onChange={(e) => setHumidity(e.target.value)}
                            value={humidity}
                          />
                          <GiDroplets />
                          
                        </div>
                      </div>
                    );

                  default:
                    return null;
                }
              })()}
            </div>
          </div>

          <h2 className="mx-4 text-3xl text-slate-500">Select Trigger</h2>
          <span className="divider" />
        </div>
      </div>
    </div>
  );
};

export default AddRoutine;
