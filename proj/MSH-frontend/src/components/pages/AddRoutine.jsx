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

import { GiDrop, GiDroplets } from 'react-icons/gi';

const channels = [
  'Channel 1',
  'Channel 2',
  'Channel 3',
  'Channel 4',
  'Channel 5',
  'Channel 6',
  'Channel 7',
  'Channel 8',
  'Channel 9',
  'Channel 10',
];

const AddRoutine = () => {
  const [trigger, setTrigger] = useState(null);
  const [devices, setDevices] = useState(null);
  const [actions, setActions] = useState(null);
  const [action, setAction] = useState(null);
  const [sensors, setSensors] = useState(null);
  const [name, setName] = useState(null);

  const [channel, setChannel] = useState(null);
  const [music, setMusic] = useState(null);
  const [time, setTime] = useState(null);

  const [color, setColor] = useState('#ffffff');
  const [slider, setSlider] = useState(50);

  const [temperature, setTemperature] = useState(20);
  const [humidity, setHumidity] = useState(25);

  const [selectDevice, setSelectDevice] = useState(null);
  const [selectSensor, setSelectSensor] = useState(null);
  const [sensorValue, setSensorValue] = useState(null);
  const [sensorRange, setSensorRange] = useState([]);

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

  const getSensors = async () => {
    try {
      const res = await axios.get(`${BASE_API_URL}/sources/list`, null);
      if (res.status === 200) {
        setSensors(res.data);
        console.log('sensors -> ', res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    if (user) {
      getDevices();
      getSensors();
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

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('submit');

    console.log('trigger -> ', trigger);
    console.log('action -> ', action);
    console.log('selectDevice -> ', selectDevice);
    console.log('name -> ', name);
    console.log('time -> ', time);
    console.log('sensorValue -> ', sensorValue);
    console.log('selectSensor -> ', selectSensor);
    console.log('channel -> ', channel);
    console.log('music -> ', music);
    console.log('color -> ', color);
    console.log('slider -> ', slider);
    console.log('temperature -> ', temperature);
    console.log('humidity -> ', humidity);

    // Check if routine changes an output device
    let value = null;

    if (action == 'Adjust Brightness ' || action == 'Adjust Volume') {
      value = slider;
    }
    if (action == 'Adjust Temperature') {
      value = temperature;
    }
    if (action == 'Adjust Humidity level') {
      value = humidity;
    }
    if (action == 'Change Color') {
      value = color;
    }
    if (action == 'Change Channel') {
      value = channel;
    }
    if (action == 'Change Music') {
      value = music;
    }

    // Check if routine is triggered by a timestamp
    if (trigger && action && selectDevice && name) {
      if (trigger == 'Time' && time) {
        axios
          .post(`${BASE_API_URL}/routines/addTB`, {
            trigger_timestamp: time,
            routine_name: name,
            associated_action: {
              action_title: action + ' ' + selectDevice.name,
              action_description: action,
              outputDeviceID: selectDevice.id,
              action_newValue: value,
            },
          })
          .then((res) => {
            console.log(res);
            navigate('/routines');
          })
          .catch((err) => {
            console.log(err);
          });
      }

      // Check if routine is triggered by a range of values
      if (trigger == 'Interval' && sensorRange) {
        axios
          .post(`${BASE_API_URL}/routines/addSB`, {
            trigger_type: 'range',
            input_ranges: sensorRange,
            source_id: selectSensor,
            routine_name: name,
            associated_action: {
              action_title: action + ' - ' + selectDevice.name,
              action_description: action,
              outputDeviceID: selectDevice.id,
              action_newValue: value,
            },
          })
          .then((res) => {
            console.log(res);
            navigate('/dashboard');
          })
          .catch((err) => {
            console.log(err);
          });
      }

      // Check if routine is triggered by a specific value
      if (trigger == 'Event' && sensorValue) {
        axios
          .post(`${BASE_API_URL}/routines/addSB`, {
            trigger_type: 'exact',
            exact_value: sensorValue,
            source_id: selectSensor,
            routine_name: name,
            associated_action: {
              action_title: action + ' - ' + selectDevice.name,
              action_description: action,
              outputDeviceID: selectDevice.id,
              action_newValue: value,
            },
          })
          .then((res) => {
            console.log(res);
            navigate('/dashboard');
          })
          .catch((err) => {
            console.log(err);
          });
      }
    }
  };

  return (
    <div className="mx-[5%] mt-4 flex justify-between">
      <Navbar />
      <div className="flex flex-col w-full h-full overflow-hidden">
        <Header />
        <form className="w-full mx-4">
          <h1 className="m-4 text-4xl font-bold">Add new routine</h1>
          <div className="flex flex-col justify-between h-full w-[90%]">
            <h2 className="m-4 text-3xl text-slate-500">
              Select a known device
            </h2>
            <span className="divider" />
            <div className="flex flex-row justify-start m-4 overflow-auto">
              {devices?.map((device) => (
                <span
                  className={`m-2 rounded-2xl ${
                    selectDevice == device && 'ring-4 ring-primary'
                  }}`}
                  key={device?.id}
                  onClick={() => {
                    setSelectDevice(device);
                    setAction(null);
                  }}
                  required
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
                    required
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

            <div className="flex flex-col justify-between">
              {action && action != 'Turn ON' && action != 'Turn OFF' && (
                <h1 className="m-4 text-3xl text-slate-500">
                  Select target value
                </h1>
              )}
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
                            required
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
                          required
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
                            required
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
                            required
                          />
                          <BsVolumeUpFill className="ml-2 text-4xl text-slate-500" />
                        </div>
                        <div className="flex flex-row items-center justify-center w-full p-4 text-2xl font-medium">
                          <h1>Selected Volume: {slider}% </h1>
                        </div>
                      </div>
                    );

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
                            required
                          />
                          <GiDroplets />
                        </div>
                      </div>
                    );

                  case 'Change Music':
                    return (
                      <div className="flex flex-col items-center justify-center w-full p-4 m-4 text-2xl shadow-lg bg-base-100 rounded-xl">
                        <input
                          className="w-full text-2xl border-2 input input-primary input-bordered"
                          type="text"
                          placeholder="Enter a music"
                          onChange={(e) => setMusic(e.target.value)}
                          required
                        />
                      </div>
                    );

                  case 'Change Channel':
                    return (
                      <div className="flex flex-col items-center justify-center w-full p-4 text-2xl shadow-lg bg-base-100 rounded-xl">
                        <select
                          className="w-full text-2xl select select-bordered select-primary"
                          required
                        >
                          <option
                            disabled
                            selected
                          >
                            Select a channel
                          </option>
                          {channels.map((channel, idx) => (
                            <option
                              key={idx}
                              value={channel}
                              onClick={(e) => setChannel(e.target.value)}
                              className="text-2xl select-bordered"
                            >
                              {channel}
                            </option>
                          ))}
                        </select>
                      </div>
                    );

                  default:
                    return null;
                }
              })()}
            </div>

            <div>
              <h2 className="m-4 text-3xl text-slate-500">Select Trigger</h2>
              <span className="w-full divider" />
              <div className="flex flex-row justify-around w-full m-4 overflow-auto">
                <span
                  className={`flex flex-col items-center justify-center p-4 m-4 text-2xl badge ${
                    trigger == 'Time'
                      ? 'badge-primary'
                      : 'badge-outline badge-accent'
                  } badge-xl `}
                  onClick={() => setTrigger('Time')}
                >
                  Time
                </span>
                <span
                  className={`flex flex-col items-center justify-center p-4 m-4 text-2xl badge ${
                    trigger == 'Interval'
                      ? 'badge-primary'
                      : 'badge-outline badge-accent'
                  } badge-xl `}
                  onClick={() => setTrigger('Interval')}
                >
                  Interval
                </span>
                <span
                  className={`flex flex-col items-center justify-center p-4 m-4 text-2xl badge ${
                    trigger == 'Event'
                      ? 'badge-primary'
                      : 'badge-outline badge-accent'
                  } badge-xl `}
                  onClick={() => setTrigger('Event')}
                >
                  Event
                </span>
              </div>

              {trigger == 'Time' ? (
                <div className="flex flex-col items-start justify-center w-full p-4 m-4 text-2xl shadow-lg bg-base-100 rounded-xl">
                  <h1 className="m-4 text-2xl font-medium text-slate-500">
                    Choose the Time
                  </h1>
                  <input
                    className="w-full text-2xl border-2 input input-primary input-bordered"
                    type="datetime-local"
                    onChange={(e) =>
                      setTime(new Date(e.target.value).getTime())
                    }
                    required
                  />
                </div>
              ) : (
                trigger && (
                  <div className="flex flex-wrap items-center justify-around w-full p-4 m-4 text-2xl shadow-lg bg-base-100 rounded-xl">
                    {sensors &&
                      trigger &&
                      trigger != 'Time' &&
                      sensors.map((sensor) => (
                        <span
                          key={sensor.id}
                          className={`flex flex-col items-center justify-center p-4 m-4 text-2xl badge ${
                            selectSensor == sensor.id
                              ? 'badge-primary'
                              : 'badge-outline badge-accent'
                          } badge-xl `}
                          onClick={() => setSelectSensor(sensor.id)}
                        >
                          {sensor.name}
                        </span>
                      ))}
                    {trigger == 'Interval' ? (
                      <div className="flex flex-row items-center justify-center w-full p-4 m-4 text-2xl shadow-lg bg-base-100 rounded-xl">
                        <input
                          className="w-1/3 text-2xl border-2 input input-primary input-bordered "
                          type="number"
                          placeholder="Min Value"
                          onChange={(e) => setSensorRange([e.target.value])}
                          required
                        />
                        <h1 className="m-4 text-3xl font-bold">-</h1>
                        <input
                          className="w-1/3 text-2xl border-2 input input-primary input-bordered "
                          type="number"
                          placeholder="Max Value"
                          onChange={(e) => setSensorRange([...sensorRange, e.target.value])}
                          required
                        />
                      </div>
                    ) : (
                      <div className="flex flex-row items-center justify-center w-full p-4 m-4 text-2xl bg-base-100 rounded-xl">
                        <input
                          className="text-2xl border-2 input input-primary input-bordered "
                          type="number"
                          placeholder="Value"
                          onChange={(e) => setSensorValue(e.target.value)}
                          required
                        />
                      </div>
                    )}
                  </div>
                )
              )}
            </div>
            <div className="flex flex-col justify-between">
              <h2 className="m-4 text-3xl text-slate-500">
                Select a name for the routine
              </h2>
              <div className="flex flex-row justify-between my-4 mb-8">
                <input
                  className="w-full mx-4 text-2xl border-2 input input-primary input-bordered"
                  type="text"
                  placeholder="Enter a name for the routine"
                  onChange={(e) => setName(e.target.value)}
                  required
                />
                <button
                  type="submit"
                  className="text-2xl btn btn-primary"
                  onClick={(e) => handleSubmit(e)}
                >
                  Add Routine
                </button>
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddRoutine;
