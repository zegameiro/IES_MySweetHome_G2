import React, { useEffect, useState } from "react";
import axios from "axios";
import { BASE_API_URL } from "../../constants";

import { WiThermometerExterior, WiThermometer } from "react-icons/wi";
import { FaUserCheck, FaUserAltSlash } from "react-icons/fa";
import { IoCloudyNightOutline, IoCloudyNight } from "react-icons/io5";
import { TbWind, TbWindOff } from "react-icons/tb";
import { MdElectricBolt } from "react-icons/md";

const InputDeviceCard = ({ device }) => {
  const [isChecked, setIsChecked] = useState(null);
  const [sensorData, setSensorData] = useState(null);
  const [avgSensorData, setAvgSensorData] = useState(null); 
  const [timeInterval, setTimeInterval] = useState(null);

  const getAverageData = (data) => {
    const informationVal = data.map((item) => Number(item.sensor_information));

    const sum = informationVal.reduce((a,b) => a + b, 0);

    return (sum / informationVal.length).toFixed(4);
  };

  const getSensorInformation = async (deviceID) => {
    try {
      const res = await axios.get(`${BASE_API_URL}/data/view`, {
        params: {
          sensor_id: deviceID,
        },
      });
        
      if (res.status === 200) {
        setSensorData(res.data);
        setAvgSensorData(getAverageData(res.data));
      }
    } catch (error) {
      console.log(error);
    };
  };

  useEffect(() => {
    if (isChecked) {

      getSensorInformation(device?.id);

      const intervalId = setInterval(() => {
        getSensorInformation(device?.id, isChecked);
      }, 5000); // Set an interval of 5 seconds to make calls to the API
      setTimeInterval(intervalId);

    } else
      clearInterval(timeInterval);

    return () => {
      clearInterval(timeInterval);
    };
  }, [isChecked, device]);

  const getIcon = (category, state) => {
    switch (category) {
      case 1:
        return state ? <WiThermometer /> : <WiThermometerExterior />;

      case 2:
        return <MdElectricBolt />
      case 3:
        return state ? <FaUserCheck /> : <FaUserAltSlash />;

      case 4:
        return state ? <IoCloudyNight /> : <IoCloudyNightOutline />;

      case 5:
        return state ? <TbWind /> : <TbWindOff />;
    }
  };

  const getDescription = (device, info) => {
    switch (device.category) {
      case 1:
        return `Avg Temperature: ${info}°C`;
        break;
      
      case 2:
        return `Avg Consume: ${info}%`;
        break;

      case 3:
        return `People: ${info}`;
        break;

      case 4:
        return `Avg Wind Strength: ${info} km/h`;
        break;
    }
  }

  return (
    <>
      <div
        className={`card w-[35vh] h-[100%] border-solid border-[3px] ${
          isChecked ? "border-primary" : "border-accent"
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
                isChecked ? "toggle-primary" : "toggle-accent"
              } peer`}
              checked={isChecked ? true : false}
              onChange={(e) => {
                setIsChecked(e.target.checked);
                getSensorInformation(device?.id)
              }}
            />
          </div>
        </div>
        <div className="justify-between flex">
          <div className="card-body flex justify-between pl-4 pt-2">
            <div
              className={`text-6xl pl-5 mb-[10px] font-medium ${
                isChecked ? "text-primary" : "text-accent"
              }`}
            >
              {getIcon(device.category, isChecked)}
            </div>
            <div>
              <div
                className={`text-base font-semibold ${
                  isChecked ? "text-primary" : "text-accent"
                }`}
              >
                <h1>{device.name}</h1>
              </div>
            </div>
          </div>
          <div className="flex justify-end relative">
            {isChecked && sensorData ? (
              <div className="flex-col">
                <div className="flex text-sm absolute top-5 right-4">
                  <span className="font-semibold">
                    {getDescription(device, avgSensorData)}
                  </span>
                </div>
              </div>
            ) : (
              <div>
                <div className="flex flex-col items-center pt-[90px] pr-4 text-slate-500">
                  <p className="text-sm font-bold"> OFF</p>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </>
  );
};

export default InputDeviceCard;