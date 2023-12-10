import React from "react";

import { BASE_API_URL } from "../../constants";

import { IoIosInformationCircleOutline } from "react-icons/io";
import { IoWarningOutline, IoCloseCircleOutline } from "react-icons/io5";
import { MdErrorOutline } from "react-icons/md";
import { FaRegClock } from "react-icons/fa";
import axios from "axios";


const Alert = ({ alert, removeAl }) => {

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
        console.log(alertId);
        try {
            const res = await axios.post(`${BASE_API_URL}/alerts/mark?id=${alertId}`);
            removeAl(alertId);
        } catch (error) {
            console.log(error);
        }
    }
    
    return (
        <div role="alert" class={`alert ${backgroundColor} shadow-lg text-white`}>
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