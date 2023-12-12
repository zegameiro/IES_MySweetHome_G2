import React from 'react';
import { Link } from 'react-router-dom';

import {
  IoIosArrowDown,
  IoMdSettings,
  IoIosInformationCircle,
} from 'react-icons/io';
import { MdAccountCircle } from 'react-icons/md';
import { FaHouseSignal } from 'react-icons/fa6';
import { FaLightbulb, FaChartLine, FaSignOutAlt } from 'react-icons/fa';
import '../../utils/index.css';

const ProfileButton = () => {
  const user = JSON.parse(localStorage.getItem('user'))
    ? JSON.parse(localStorage.getItem('user'))
    : null;

  return (
    <details className="dropdown">
      <summary className="btn rounded-full h-20 min-w-[10vw] p-2 text-3xl text-white flex justify-between gradient-blue">
        <div className="avatar ">
          <div className="w-16 rounded-full">
            <img src="https://daisyui.com/images/stock/photo-1534528741775-53994a69daeb.jpg" />
          </div>
        </div>
        {user?.firstName} <IoIosArrowDown />
      </summary>
      <ul className="shadow menu dropdown-content z-[1] gradient-blue text-white text-xl font-bold rounded-box w-60">
        <li>
          <Link to="/profile">
            <MdAccountCircle /> My account{' '}
          </Link>
        </li>
        <li>
          <Link to="/rooms">
            <FaHouseSignal /> Manage rooms
          </Link>
        </li>
        <li>
          <Link to="/devices">
            <FaLightbulb /> Manage Devices
          </Link>
        </li>
        <li>
          <Link to="/statistics">
            <FaChartLine /> Analytics
          </Link>
        </li>
        <li>
          <Link to="/setting">
            <IoMdSettings /> Settings
          </Link>
        </li>
        <li>
          <Link to="/help">
            <IoIosInformationCircle /> Help
          </Link>
        </li>
        <li className="pt-5">
          <Link
            to="/login"
            onClick={() => {
              localStorage.removeItem('user');
            }}
          >
            <FaSignOutAlt /> Logout
          </Link>
        </li>
      </ul>
    </details>
  );
};

export default ProfileButton;
