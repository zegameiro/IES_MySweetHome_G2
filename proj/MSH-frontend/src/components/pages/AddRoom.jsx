import { useEffect, useState } from 'react';

import Navbar from '../layout/Navbar';
import Header from '../layout/Header';

import axios from 'axios';
import { BASE_API_URL } from '../../constants';

import { useNavigate } from 'react-router-dom';

import {
  Bedroom1,
  Bathroom1,
  Attic1,
  LivingRoom1,
  Kitchen1,
} from '../../assets/images';

const AddRoom = () => {
  const [roomName, setRoomName] = useState('');
  const [roomType, setRoomType] = useState('');
  const [roomFloor, setRoomFloor] = useState('');

  const navigate = useNavigate();

  const handleSubmit = () => {
    console.log(roomType, roomFloor, roomName);
    if (roomType === '' || roomFloor === '' || roomName === '') {
      alert('Please fill in all the fields');
      return;
    }
    
    axios
      .post(`${BASE_API_URL}/room/add`, null, {
        params: {
          name: roomName,
          floornumber: roomFloor,
          type: roomType,
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
        alert('Error adding room. Please try again.');
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
            <span
              className={`${
                roomType == 'Bedroom' && 'ring-4 ring-primary'
              } flex flex-col cursor-pointer justify-center items-center w-[32%] h-[25vh] bg-cover m-2 rounded-2xl overflow-hidden`}
              style={{
                backgroundImage: `url(${Bedroom1})`,
              }}
              onClick={() => setRoomType('Bedroom')}
            >
              <span className="flex items-end justify-start w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
                <h2>Bedroom</h2>
              </span>
            </span>
            <div
              className={`${
                roomType == 'Bathroom' && 'ring-4 ring-primary'
              } flex cursor-pointer flex-col justify-center items-center w-[32%] h-[25vh] m-2 rounded-2xl bg-cover overflow-hidden`}
              style={{
                backgroundImage: `url(${Bathroom1})`,
              }}
              onClick={() => setRoomType('Bathroom')}
            >
              <span className="flex items-end justify-start w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
                <h2>Bathroom</h2>
              </span>
            </div>
            <div
              className={`${
                roomType == 'Attic' && 'ring-4 ring-primary'
              } flex flex-col cursor-pointer justify-center items-center w-[32%] h-[25vh] m-2 rounded-2xl bg-cover overflow-hidden`}
              style={{
                backgroundImage: `url(${Attic1})`,
              }}
              onClick={() => setRoomType('Attic')}
            >
              <span className="flex items-end justify-start w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
                <h2>Attic</h2>
              </span>
            </div>
            <div
              className={`${
                roomType == 'Living_Room' && 'ring-4 ring-primary'
              } flex flex-col cursor-pointer justify-center items-center w-[32%] h-[25vh] m-2 rounded-2xl bg-cover overflow-hidden`}
              style={{
                backgroundImage: `url(${LivingRoom1})`,
              }}
              onClick={() => setRoomType('Living_Room')}
            >
              <span className="flex items-end justify-start w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
                <h2>Livinig Room</h2>
              </span>
            </div>

            <div
              className={`${
                roomType == 'Kitchen' && 'ring-4 ring-primary'
              } flex flex-col cursor-pointer justify-center items-center w-[32%] h-[25vh] m-2 rounded-2xl bg-cover overflow-hidden`}
              style={{
                backgroundImage: `url(${Kitchen1})`,
              }}
              onClick={() => setRoomType('Kitchen')}
            >
              <span className="flex items-end justify-start w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
                <h2>Kitchen</h2>
              </span>
            </div>

            <div
              className={`${
                roomType == 'Other' && 'ring-4 ring-primary'
              } flex flex-col cursor-pointer justify-center items-center w-[32%] h-[25vh] m-2 rounded-2xl bg-cover overflow-hidden`}
              onClick={() => setRoomType('Other')}
            >
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
