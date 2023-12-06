import React from "react";

import { HiMiniBellAlert } from "react-icons/hi2";
import { FaClock } from "react-icons/fa";

const Alert = ({ alert }) => {
    const convertTimestamp = (timestamp) => {
        const date = new Date(timestamp);
        const hours = date.getUTCHours();
        const minutes = date.getUTCMinutes();
        return `${hours}h ${minutes}min`;
    };

    const backgroundColor =
        alert.alert_level === 3
        ? "bg-primary"
        : alert.alert_level === 2
        ? "bg-accent"
        : "bg-secondary";
    
    return (
        <div className={`rounded-lg p-4 mb-4 ${backgroundColor} text-white relative w-[100%] h-[100%]`}>
            <div className="flex flex-row text-3xl text-center font-semibold">  
                <p> <HiMiniBellAlert /> </p>
                <p> {alert.alert_header} </p>
            </div>
            <div className="flex flex-row text-xl">
                <div className="font-medium">
                    <p>{alert.alert_information}</p>
                </div>
                <div className="flex flex-row items-center font-bold text-lg absolute top-0 right-5 mt-4">
                    <p> <FaClock /> </p>
                    <p>{convertTimestamp(alert.timestamp)}</p>
                </div>
            </div>
        </div>
    )
    
};

export default Alert;