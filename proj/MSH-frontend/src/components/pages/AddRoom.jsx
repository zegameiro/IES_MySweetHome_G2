import { useEffect, useState } from 'react';

import Navbar from '../layout/Navbar';
import Header from '../layout/Header';
import axios from 'axios';
import { BASE_API_URL } from '../../constants';

import { useNavigate } from 'react-router-dom';

const AddRoom = () => {
  const [roomName, setRoomName] = useState('');
  const [roomType, setRoomType] = useState('');
  const [roomFloor, setRoomFloor] = useState('');

  const navigate = useNavigate();

  const handleSubmit = () => {
    axios
      .post(`${BASE_API_URL}/room/add`, null, {
        params: {
          name: roomName,
          floornumber: roomFloor,
        },
      })
      .then((res) => {
        if (res.status === 200) {
          console.log('registered');
          alert('Room added successfully');
          navigate('/dashboard');
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="mx-[5%] mt-4 flex justify-between">
      <Navbar />
      <div className="flex flex-col w-full h-full">
        <Header />
        <div className="w-full mx-4">
          <h1 className="m-4 text-4xl font-bold">Add new room</h1>
          <h2 className="m-4 text-3xl font-bold text-slate-500">
            Select a room type
          </h2>
          <hr />
          <div className="flex flex-wrap justify-around">
            <div className=" flex flex-col justify-center items-center w-[32%] h-[25vh] m-2 rounded-2xl bg-[url('/src/assets/images/bedroom1.jpg')] bg-contain overflow-hidden">
              <span className="flex items-end justify-start w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
                <h2>Bedroom</h2>
              </span>
            </div>
            <div className=" flex flex-col justify-center items-center w-[32%] h-[25vh] m-2 rounded-2xl bg-[url('/src/assets/images/bathroom1.jpg')] bg-contain overflow-hidden">
              <span className="flex items-end justify-start w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
                <h2>Bathroom</h2>
              </span>
            </div>
            <div className=" flex flex-col justify-center items-center w-[32%] h-[25vh] m-2 rounded-2xl bg-[url('/src/assets/images/attic1.jpg')] bg-contain overflow-hidden">
              <span className="flex items-end justify-start w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
                <h2>Attic</h2>
              </span>
            </div>
            <div className=" flex flex-col justify-center items-center w-[32%] h-[25vh] m-2 rounded-2xl bg-[url('/src/assets/images/living_room1.jpg')] bg-contain overflow-hidden">
              <span className="flex items-end justify-start w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
                <h2>Livinig Room</h2>
              </span>
            </div>

            <div className=" flex flex-col justify-center items-center w-[32%] h-[25vh] m-2 rounded-2xl bg-[url('/src/assets/images/kitchen1.jpg')] bg-contain overflow-hidden">
              <span className="flex items-end justify-start w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
                <h2>Kitchen</h2>
              </span>
            </div>

            <div className=" flex flex-col justify-center items-center w-[32%] h-[25vh] m-2 rounded-2xl bg-primary overflow-hidden">
              <span className="flex items-end justify-start w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
                <h2>Other</h2>
              </span>
            </div>
          </div>
          <hr className="my-4" />
          <div className="flex justify-between my-4">
            <div className="flex w-full">
              <span className="flex flex-col w-1/2 mx-4">
                <h2 className="my-4 text-3xl font-bold text-slate-500">
                  Name the room
                </h2>
                <input
                  className="text-lg input input-primary "
                  type="text"
                  placeholder='e.g. "Bedroom 1"'
                  onChange={(e) => setRoomName(e.target.value)}
                />
              </span>
              <span>
                <h2 className="my-4 text-3xl font-bold text-slate-500">
                  Select the floor
                </h2>
                <input
                  className="text-lg input input-primary"
                  type="number"
                  placeholder="e.g. 1"
                  onChange={(e) => setRoomFloor(e.target.value)}
                />
              </span>
            </div>
            <button
              className="self-end w-1/3 mx-4 text-2xl btn btn-primary"
              onClick={handleSubmit}
            >
              Add room
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AddRoom;
