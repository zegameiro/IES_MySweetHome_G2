import { useEffect, useState } from 'react';
import Navbar from '../layout/Navbar';
import Header from '../layout/Header';

import axios from 'axios';
import { BASE_API_URL } from '../../constants';
import { useNavigate } from 'react-router-dom';

import { FaPlusCircle } from 'react-icons/fa';

import { getRoomImage } from '../../utils';


const RoomsPage = () => {
  const [rooms, setRooms] = useState([]);
  const navigate = useNavigate();

  const user = JSON.parse(localStorage.getItem('user'))
    ? JSON.parse(localStorage.getItem('user'))
    : null;

  useEffect(() => {
    axios
      .get(`${BASE_API_URL}/room/list`)
      .then((res) => {
        if (res.status === 200) {
          console.log(res.data);
          setRooms(res.data);
        }
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  return (
    <div className="mx-[5%] mt-4 flex justify-between«">
      <Navbar />
      <div className="flex flex-col w-full h-full">
        <Header />
        <div className="w-full mx-4">
          <h1 className="m-4 text-4xl font-bold">{user.firstName}'s Rooms</h1>

          <div className="flex flex-wrap justify-start">
            {rooms.map((room) => (
              <div
                key={room.id}
                className=" flex flex-col cursor-pointer justify-center items-center w-[32%] h-[25vh] bg-primary bg-cover m-2 rounded-2xl overflow-hidden"
                style={{
                  backgroundImage: `url(${getRoomImage(room.type)})`,
                }}
                onClick={() => navigate(`/room/${room.id}`)}
              >
                <span className="flex items-end justify-start w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
                  <span className="flex flex-col">
                    <h2>{room.name}</h2>
                    <p className="font-normal text-md text-slate-200">
                      {room.devices.length} devices
                    </p>
                  </span>
                </span>
              </div>
            ))}
            <div
              className=" flex flex-col cursor-pointer justify-center items-center w-[32%] h-[25vh] bg-primary bg-cover m-2 rounded-2xl overflow-hidden"
              onClick={() => navigate('/addroom')}
            >
              <span className="flex items-center justify-center w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
                <span className="flex flex-col items-center">
                  <FaPlusCircle size={75} />
                  <h2 className="p-2">Add Room</h2>
                </span>
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RoomsPage;
