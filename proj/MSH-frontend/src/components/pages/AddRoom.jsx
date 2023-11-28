import { useEffect, useState } from 'react';

import Navbar from '../layout/Navbar';
import Header from '../layout/Header';

const AddRoom = () => {
  return (
    <div className="mx-[5%] mt-4 flex justify-between">
      <Navbar />
      <div className="flex flex-col w-full h-full">
        <Header />
      </div>
    </div>
  );
};

export default AddRoom;
