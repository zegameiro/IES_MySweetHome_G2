import { useNavigate } from 'react-router-dom';

import Navbar from '../layout/Navbar';
import Header from '../layout/Header';

import { Devices, Rooms, Routines } from '../../assets/images';

const AddPage = () => {
  const navigate = useNavigate();

  return (
    <div className="mx-[5%] mt-4 flex justify-between">
      <Navbar />
      <div className="flex flex-col w-full h-full">
        <Header />
        <div className="flex flex-row justify-between ">
          <div className="w-full h-full m-2">
            <button
              className=" bg-cover rounded-xl shadow-lg overflow-hidden m-4 flex justify-start items-end font-bold text-3xl w-full h-[80vh] "
              style={{ backgroundImage:  `url(${Rooms})`}}
              onClick={() => navigate('/addroom')}
            >
              <span className="flex items-end justify-start w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
              <h1>ADD ROOM</h1>
              </span>
            </button>
          </div>
          <div className="w-full h-full m-2">
            <button
              className="bg-cover rounded-xl shadow-lg overflow-hidden m-4 flex justify-start items-end font-bold text-3xl w-full h-[80vh] "
              style={{ backgroundImage: `url(${Devices})`}}
              onClick={() => navigate('/adddevice')}
            >
              <span className="flex items-end justify-start w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
              <h1>ADD DEVICE</h1>
              </span>
            </button>
          </div>
          <div className="w-full h-full m-2">
            <button
              className="bg-cover rounded-xl shadow-lg overflow-hidden m-4 flex justify-start items-end font-bold text-3xl w-full h-[80vh] "
              style={{ backgroundImage: `url(${Routines})` }}
              onClick={() => navigate('/addroutine')}
            >
              <span className="flex items-end justify-start w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
              <h1>ADD ROUTINE</h1>
              </span>
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AddPage;
