import { useEffect, useState } from 'react';

import Navbar from '../layout/Navbar';
import Header from '../layout/Header';

const AddRoom = () => {
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
            <div className=" flex flex-col justify-center items-center w-[32%] h-[25vh] m-2 rounded-2xl bg-[url('/src/assets/images/smarthome_wpp.jpg')] overflow-hidden">
              <span className="flex items-end justify-start w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
                <h2>Bedroom</h2>
              </span>
            </div>
            <div className=" flex flex-col justify-center items-center w-[32%] h-[25vh] m-2 rounded-2xl bg-[url('/src/assets/images/smarthome_wpp.jpg')] overflow-hidden">
              <span className="flex items-end justify-start w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
                <h2>Bedroom</h2>
              </span>
            </div>
            <div className=" flex flex-col justify-center items-center w-[32%] h-[25vh] m-2 rounded-2xl bg-[url('/src/assets/images/smarthome_wpp.jpg')] overflow-hidden">
              <span className="flex items-end justify-start w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
                <h2>Bedroom</h2>
              </span>
            </div>
            <div className=" flex flex-col justify-center items-center w-[32%] h-[25vh] m-2 rounded-2xl bg-[url('/src/assets/images/smarthome_wpp.jpg')] overflow-hidden">
              <span className="flex items-end justify-start w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
                <h2>Bedroom</h2>
              </span>
            </div>

            <div className=" flex flex-col justify-center items-center w-[32%] h-[25vh] m-2 rounded-2xl bg-[url('/src/assets/images/smarthome_wpp.jpg')] overflow-hidden">
              <span className="flex items-end justify-start w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
                <h2>Bedroom</h2>
              </span>
            </div>

            <div className=" flex flex-col justify-center items-center w-[32%] h-[25vh] m-2 rounded-2xl bg-[url('/src/assets/images/smarthome_wpp.jpg')] overflow-hidden">
              <span className="flex items-end justify-start w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
                <h2>Bedroom</h2>
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AddRoom;
