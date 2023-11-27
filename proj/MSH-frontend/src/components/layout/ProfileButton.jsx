import React from "react";
import { Link } from 'react-router-dom';

import { IoIosArrowDown, IoMdSettings, IoIosInformationCircle } from "react-icons/io";
import { MdAccountCircle } from "react-icons/md";
import { FaHouseSignal } from "react-icons/fa6";
import { FaLightbulb, FaChartLine, FaSignOutAlt } from "react-icons/fa";
import "../../utils/index.css";

const ProfileButton = () => {
    const user = JSON.parse(localStorage.getItem('user')) ? JSON.parse(localStorage.getItem('user')) : null;

    return (
        <details className="dropdown relative">
            <summary className="btn rounded-[50px] text-white h-[5%] w-auto gradient-blue inline-flex">
                <div className="text-lg flex flex-row items-center">
                    {user?.firstname} <IoIosArrowDown />
                </div>
            </summary>
            <ul className="shadow menu dropdown-content z-[0] gradient-blue text-white text-lg font-bold rounded-box w-60 text-center absolute top-full left-1/2 transform -translate-x-1/2">
                <li>
                    <Link to='/profile'><MdAccountCircle /> My account </Link>
                </li>
                <li>
                    <Link to='/rooms'><FaHouseSignal /> Manage rooms</Link>
                </li>
                <li>
                    <Link to='/devices'><FaLightbulb /> Manage Devices</Link>
                </li>
                <li>
                    <Link to='/statistics'><FaChartLine /> Analytics</Link>
                </li>
                <li>
                    <Link to='/setting'><IoMdSettings /> Settings</Link>
                </li>
                <li>
                    <Link to='/help'><IoIosInformationCircle /> Help</Link>
                </li>
                <li className="pt-5">
                    <Link href="/login" onClick={() => {localStorage.removeItem('user');}}>
                        <FaSignOutAlt /> Logout
                    </Link>
                </li>
                
            </ul>
        </details>
    );
}

export default ProfileButton;
