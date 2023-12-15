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
  const [sensorDataHour, setSensorDataHour] = useState(null);
  const [sensorDataLatest, setSensorDataLatest] = useState(null);
  const [avgSensorData, setAvgSensorData] = useState(null);
  const [latestSensorData, setLatestSensorData] = useState(null);
  const [timeInterval, setTimeInterval] = useState(null);
  const [deviceUnit, setDeviceUnit] = useState(null);

  const getAverageData = (data) => {
    const informationVal = data.map((item) => Number(item.sensor_information));

    const sum = informationVal.reduce((a, b) => a + b, 0);

    return (sum / informationVal.length).toFixed(4);
  };

  const getSensorInformationHour = async (deviceID) => {
    try {
      const res = await axios.get(`${BASE_API_URL}/data/view`, {
        params: {
          sensor_id: deviceID,
          filter: "last_hour",
        },
      });

      if (res.status === 200) {
        setSensorDataHour(res.data);
        setAvgSensorData(getAverageData(res.data));
      }
    } catch (error) {
      console.log(error);
    }
  };

  const getSensorInformationLatest = async (deviceID) => {
    try {
      console.log(deviceID);
      const res = await axios.get(`${BASE_API_URL}/data/view`, {
        params: {
          sensor_id: deviceID,
          filter: "latest",
        },
      });

      if (res.status === 200) {
        setSensorDataLatest(res.data);
        //console.log("dados sensor -> " , res.data)
        //console.log("unit -> " , res.data[0].unit)
        setDeviceUnit(res.data[0].unit);
        setLatestSensorData(res.data[0].sensor_information);
      }
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    if (isChecked) {
      getSensorInformationHour(device?.id);
      getSensorInformationLatest(device?.id);

      const intervalId = setInterval(() => {
        getSensorInformationHour(device?.id);
        getSensorInformationLatest(device?.id);
      }, 5000); // Set an interval of 5 seconds to make calls to the API
      setTimeInterval(intervalId);
    } else clearInterval(timeInterval);

    return () => {
      clearInterval(timeInterval);
    };
  }, [isChecked, device]);

  const getIcon = (category, state) => {
    switch (category) {
      case 1:
        return state ? <WiThermometer /> : <WiThermometerExterior />;

      case 2:
        return <MdElectricBolt />;

      case 3:
        return state ? <TbWind /> : <TbWindOff />;

      case 4:
        return state ? <FaUserCheck /> : <FaUserAltSlash />;

      case 5:
        return state ? <IoCloudyNight /> : <IoCloudyNightOutline />;
    }
  };

  const getDescriptionAvg = (device, info) => {
    //console.log(device)
    console.log(info);
    return (
      <p>
        {" "}
        Last Hour Average : <br />{" "}
        <strong>
          {" "}
          {Number(info).toPrecision(2)} {deviceUnit}{" "}
        </strong>
      </p>
    );
    /*
    switch (device.category) {
      case 1:
        return (<p>Avg Temperature (last hour): <br /> <strong>{info}°C</strong></p>);
      
      case 2:
        return (<p>Avg Consume (last hour): <br /> <strong>{info}kWh</strong></p>);

      case 3:
        return (<p>People (last hour): <br /> <strong>{info}</strong></p>);

      case 4:
        return (<p>Avg Wind Strength (last hour): <br /> <strong>{info} km/h</strong></p>);
    }
    */
  };

  const getDescriptionLatest = (device, info) => {
    let infor = Number(info);
    return (
      <p>
        {device.reading_type}: <br />{" "}
        <strong>
          {" "}
          {Number(infor).toPrecision(2)} {deviceUnit}{" "}
        </strong>
      </p>
    );
    /*
    switch (device.category) {
      case 1:
        return (<p>Current Temperature: <br /> <strong>{infor}°C</strong></p>);
      
      case 2:
        return (<p>Current Consume: <br /> <strong>{infor.toFixed(4)}kWh</strong></p>);

      case 3:
        return (<p>Current People: <br /> <strong>{infor}</strong></p>);

      case 4:
        return (<p>Current Wind Strength: <br /> <strong>{infor}km/h</strong></p>);
    }
    */
  };

  return (
    <>
      <div
        className={`card w-[40vh] h-[100%] border-solid border-[3px] ${
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
          <div className="flex pr-5 pt-3 pb-4">
            <input
              type="checkbox"
              className={`toggle ${
                isChecked ? "toggle-primary" : "toggle-accent"
              } peer`}
              checked={isChecked ? true : false}
              onChange={(e) => {
                setIsChecked(e.target.checked);
                getSensorInformationHour(device?.id);
                getSensorInformationLatest(device?.id);
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
          <div className="flex justify-end">
            {isChecked && sensorDataHour && sensorDataLatest ? (
              <div className="flex-col">
                <div className="flex text-sm pr-4">
                  <span className="flex flex-col">
                    {getDescriptionLatest(device, latestSensorData)}
                    <br />
                    {getDescriptionAvg(device, avgSensorData)}
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
