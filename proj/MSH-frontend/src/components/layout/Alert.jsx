import React, { useEffect, useState } from "react";

import { BASE_API_URL } from "../../constants";

import { IoIosInformationCircleOutline } from "react-icons/io";
import { IoWarningOutline, IoCloseCircleOutline } from "react-icons/io5";
import { MdErrorOutline } from "react-icons/md";
import { FaRegClock } from "react-icons/fa";

import axios from "axios";


const Alert = ({ alert, removeAl }) => {

    const [isHiding, setIsHiding] = useState(false);
    const [isInitial, setIsInitial] = useState(true);

    const convertTimestamp = (timestamp) => {
        const date = new Date(timestamp);
        const hours = date.getUTCHours();
        const minutes = date.getUTCMinutes();
        return `${hours}h ${minutes}min`;
    };

    const backgroundColor =
        alert.alert_level === 3
        ? "alert-info"
        : alert.alert_level === 2
        ? "alert-warning"
        : "alert-error";
    
    const icon = 
        alert.alert_level === 3
        ? <IoIosInformationCircleOutline />
        : alert.alert_level === 2
        ? <IoWarningOutline />
        : <MdErrorOutline />;

    const removeAlert = async (alertId) => {
        setIsHiding(true);
        setTimeout(async () => {
            try {
              const res = await axios.post(`${BASE_API_URL}/alerts/mark?id=${alertId}`);
              removeAl(alertId);
            } catch (error) {
              console.log(error);
            }
        }, 500);
    }

    useEffect(() => {
        const timer = setTimeout(() => {
          removeAlert(alert.id);
        }, 15000);
    
        return () => clearTimeout(timer);
    }, []);

    useEffect(() => {
        if (isInitial) {
          setTimeout(() => {
            setIsInitial(false);
          }, 50);
        }
      }, [isInitial]);
    
    return (
        <div role="alert" className={`alert ${backgroundColor} shadow-lg text-white transform transition-all ease-in-out duration-500 ${isHiding ? 'opacity-0 scale-0' : (isInitial ? 'opacity-0 scale-0' : 'opacity-100 scale-100')}`}>
            <div className="text-3xl font-bold">{icon}</div>
            <div>
                <div className="flex flex-row font-bold">
                    <h3 className="text-lg">{alert.alert_header}</h3>
                    <span className="flex flex-row text-lg"><span className=" pl-5 pt-[5px]"><FaRegClock/> </span>{convertTimestamp(alert.timestamp)}</span>
                </div>
                <div className="text-md">{alert.alert_information}</div>
            </div>
            <button onClick={() => removeAlert(alert.id)}> <span className="text-3xl font-bold"><IoCloseCircleOutline /></span></button>
        </div>
    )
    
};

export default Alert;