import { Link } from 'react-router-dom';
import {
  FaHouse,
  FaLightbulb,
  FaHouseSignal,
  FaCirclePlus,
  FaGears,
  FaRightFromBracket,
} from 'react-icons/fa6';
import { FaChartLine } from 'react-icons/fa';
import '../../utils/index.css';

const navbar = () => {
  return (
    <div className=" flex flex-col justify-evenly gradient-blue rounded-3xl text-4xl h-[95vh] text-white p-8 sticky  ">
      <Link
        to="/dashboard"
        className="flex justify-center align-center"
      >
        <FaHouse />
      </Link>
      <Link
        to="/devices"
        className="flex justify-center align-center"
      >
        <FaLightbulb />
      </Link>
      <Link
        to="/rooms"
        className="flex justify-center align-center"
      >
        <FaHouseSignal />
      </Link>
      <Link
        to="/statistics"
        className="flex justify-center align-center"
      >
        <FaChartLine />
      </Link>
      <Link
        to="/add"
        className="flex justify-center align-center"
      >
        <FaCirclePlus />
      </Link>
      <Link
        to="/routines"
        className="flex justify-center align-center"
      >
        <FaGears />
      </Link>
      <Link
        to="/login"
        onClick={() => {
          localStorage.removeItem('user');
        }}
        className="flex justify-center align-center"
      >
        <FaRightFromBracket />
      </Link>
    </div>
  );
};

export default navbar;
