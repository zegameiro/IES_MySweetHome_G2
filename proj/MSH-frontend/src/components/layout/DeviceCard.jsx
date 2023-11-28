/* eslint-disable no-unused-vars */
import React, { useEffect } from 'react';
import { useState } from 'react';
import axios from 'axios';
import { BASE_API_URL } from '../../constants';

import { TbAirConditioningDisabled, TbAirConditioning } from 'react-icons/tb';
import { MdOutlineSpeaker, MdSpeaker } from 'react-icons/md';
import { PiMonitorBold, PiMonitorFill } from 'react-icons/pi';
import { FaLightbulb, FaRegLightbulb } from 'react-icons/fa6';
import { WiHumidity } from 'react-icons/wi';

const DeviceCard = ({ isBig, rooms, Device, ...props}) => {
  const [device, setDevice] = useState(Device ? Device : null);

  const [isChecked, setIsChecked] = useState(
    device['state'] === 'on' ? true : false
  );
  const [durationTime, setDurationTime] = useState(`0min`);

  const getDurationTime = (device) => {
    const laststatechange = device['laststatechange'];
    const now = Date.now();
    let diff = now - laststatechange;

    const hours = Math.floor(diff / (1000 * 60 * 60));

    diff -= hours * (1000 * 60 * 60);

    const minutes = Math.floor(diff / 1000 / 60);

    if (hours === 0) {
      setDurationTime(`${minutes}m`);
    } else {
      setDurationTime(`${hours}h ${minutes}m`);
    }
  };

  useEffect(() => {
    getDurationTime(device);
  }, [device]);

  const getIcon = (category, state) => {
    switch (category) {
      case '0':
        if (state) {
          return <FaLightbulb />;
        } else {
          return <FaRegLightbulb />;
        }
      case '1':
        if (state) {
          return <TbAirConditioning />;
        } else {
          return <TbAirConditioningDisabled />;
        }
      case '2':
        if (state) {
          return <PiMonitorBold />;
        } else {
          return <PiMonitorFill />;
        }
      case '3':
        if (state) {
          return <MdSpeaker />;
        } else {
          return <MdOutlineSpeaker />;
        }
      case '4':
        return <WiHumidity />;
    }
  };

  const getDescription = (device) => {
    let category = device['category'];

    switch (category) {
      case '0':
        if (device['state'] === 'on') {
          if (device['color'] !== 'white') return `Color: ${device['color']}`;
          else return `Light on`;
        }
        break;

      case '1':
        if (device['state'] === 'on') {
          if (device['temperature'] !== 0)
            return `Temperature: ${device['temperature']}°C`;
          else return `Air conditioner on`;
        }
        break;
      case '2':
        if (device['state'] === 'on') {
          if (device['channel'] !== 'None')
            return `Tv on channel ${device['channel']}`;
          else return `Reproducing Tv`;
        }
        break;

      case '3':
        if (device['state'] === 'on') {
          if (device['music'] !== 'None') return `Playing ${device['music']}`;
          else return `Playing music`;
        }
        break;
      case '4':
        if (device['state'] === 'on') {
          return `Dehumidifier on`;
        }
        break;
    }
  };

  const changeState = async (deviceID, newState) => {
    try {
      const res = await axios.post(
        `${BASE_API_URL}/outputs/changeState?id=${deviceID}`,
        {
          state: newState,
        }
      );
      if (res.status === 200) {
        console.log('changed state');
        setDevice(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <>
      {isBig ? (
        <div
          className={`card w-[310px] h-[180px] border-solid border-[3px] ${
            isChecked ? 'border-primary' : 'border-accent'
          } flex flex-col justify-between hover:shadow-xl transition-shadow duration-300`}
        >
          <div className="flex justify-between items-center">
            <div className="flex text-lg font-medium pl-5 pt-2">
              {isChecked ? (
                <h1 className="text-primary">On</h1>
              ) : (
                <h1 className="text-accent">Off</h1>
              )}
            </div>
            <div className="flex pr-5 pt-2">
              <input
                type="checkbox"
                className={`toggle ${
                  isChecked ? 'toggle-primary' : 'toggle-accent'
                } peer`}
                checked={isChecked ? true : false}
                onChange={(e) => {
                  setIsChecked(e.target.checked);
                  changeState(device?.id, e.target.checked ? 'on' : 'off');
                }}
              />
            </div>
          </div>
          <div className="justify-between flex">
            <div className="card-body flex justify-between pl-4 pt-2">
              <div
                className={`text-6xl pl-5 mb-[10px] font-medium ${
                  isChecked ? 'text-primary' : 'text-accent'
                }`}
              >
                {getIcon(device['category'], isChecked)}
              </div>
              <div>
                <div
                  className={`text-base font-semibold ${
                    isChecked ? 'text-primary' : 'text-accent'
                  }`}
                >
                  <h1>{device['name']}</h1>
                </div>
                {rooms[0]['devices'].includes(device['id']) ? (
                  <p className="items-centers text-sm">
                    {' '}
                    On <strong>{rooms[0]['name']}</strong>{' '}
                  </p>
                ) : (
                  <p className="text-sm">Room Unknown</p>
                )}
              </div>
            </div>
            <div className="flex justify-end">
              {isChecked ? (
                <div className="flex-col pr-4">
                  <div className="flex text-sm pt-[30px]">
                    <span className="font-semibold">
                      {getDescription(device)}
                    </span>
                  </div>
                  <div className="flex flex-col items-center pt-[40px] text-slate-500">
                    <p className="text-sm">Uptime</p>
                    <p className="text-sm font-bold"> {durationTime} </p>
                  </div>
                </div>
              ) : (
                <div>
                  <div className="flex flex-col items-center pt-[90px] pr-4 text-slate-500">
                    <p className="text-sm">Uptime</p>
                    <p className="text-sm font-bold"> OFF</p>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
      ) : (
        <div
          className={`card w-[180px] h-[180px] border-solid border-[3px] ${
            isChecked ? 'border-primary' : 'border-accent'
          } flex flex-col text-center justify-between hover:shadow-xl transition-shadow duration-300`}
        >
          <div className="flex justify-between pt-3 pr-3 pl-3">
            <div className="flex">
              {isChecked ? (
                <h1 className="text-md font-medium text-primary">On</h1>
              ) : (
                <h1 className="text-md font-medium text-accent">Off</h1>
              )}
            </div>

            <input
              type="checkbox"
              className={`toggle ${
                isChecked ? 'toggle-primary' : 'toggle-accent'
              } peer`}
              checked={isChecked ? true : false}
              onChange={(e) => {
                setIsChecked(e.target.checked);
                changeState(device['id'], e.target.checked ? 'on' : 'off');
              }}
            />
          </div>
          <div
            className={`card-body items-center pt-5 text-6xl mb-[10px] font-medium ${
              isChecked ? 'text-primary' : 'text-accent'
            }`}
          >
            <div className="text-5xl mb-[10px]">
              {getIcon(device['category'], isChecked)}
            </div>
            <div className="text-base">
              <p>{device['name']}</p>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default DeviceCard;
