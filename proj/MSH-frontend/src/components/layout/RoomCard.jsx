/* eslint-disable react/prop-types */
import React from 'react';

const RoomCard = ({ roomType, roomName, numDevices, value, image, select }) => {
  return (
    <div
      className=" flex flex-col cursor-pointer justify-center items-center w-[32%] h-[25vh] bg-primary bg-cover m-2 rounded-2xl overflow-hidden"
      style={{
        backgroundImage: `url(${image})`,
      }}
    >
      <span className="flex items-end justify-start w-full h-full p-4 text-3xl font-semibold text-white hero-overlay">
        <span className="flex flex-col">
          <h2>{roomName}</h2>
          {numDevices && (
            <p className="font-normal text-md text-slate-200">
              {numDevices} devices
            </p>
          )}
        </span>
      </span>
    </div>
  );
};

export default RoomCard;
