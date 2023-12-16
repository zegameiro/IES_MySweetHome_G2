import React from "react";

import { PiPlugChargingFill } from "react-icons/pi";
import { TbWind } from "react-icons/tb";
import { IoCloudyNight } from "react-icons/io5";
import { FaHouseUser, FaThermometerHalf } from "react-icons/fa";

const StatisticsCard = ({ isChecked, inputDevice }) => {

    const getIcon = (device) => {
        let category = device.category;
        switch (category) {
            case 1:
                return <FaThermometerHalf />

            case 2:
                return <PiPlugChargingFill />

            case 3:
                return <FaHouseUser />

            case 4:
                return <IoCloudyNight />

            case 5:
                return <TbWind />
        
        }
    };

    return (
        <div className={`flex flex-col card h-[100%] w-[22vh] border-radius-5 items-center text-center hover:shadow-xl transition-shadow duration-300 ${isChecked ? "bg-primary text-white": "border-solid border-[3px] border-accent text-accent"}`}>
            <div className="text-5xl pt-3 pb-3">
                {getIcon(inputDevice)}
            </div>
            <div className="text-xl pb-3">
                <h1 className="font-bold">{inputDevice.name}</h1>
            </div>
        </div>
    )
}

export default StatisticsCard;