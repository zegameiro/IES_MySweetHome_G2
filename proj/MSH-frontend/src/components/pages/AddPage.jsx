import { useNavigate } from 'react-router-dom';

import Navbar from '../layout/Navbar';
import ProfileButton from '../layout/ProfileButton';
import SearchBar from '../layout/SearchBar';
import Header from '../layout/Header';

const AddPage = () => {
  const navigate = useNavigate();

  return (
    <div className="mx-[5%] mt-4 flex justify-between">
      <Navbar />
      <div className="flex flex-col w-full h-full">
        <Header />

        <div className="">
          <button className="bg-primary rounded-xl shadow-lg p-4 m-4 flex justify-start items-end font-bold text-4xl w-full h-[27vh] m-full">
            <h1>ADD ROOM</h1>
          </button>
        </div>
        <div className="w-full h-full">
          <button className="bg-primary rounded-xl shadow-lg p-4 m-4 flex justify-start items-end font-bold text-4xl w-full h-[27vh] m-full">
            <h1>ADD DEVICE</h1>
          </button>
        </div>
        <div className="w-full h-full">
          <button
            className="bg-primary rounded-xl shadow-lg p-4 m-4 flex justify-start items-end font-bold text-4xl w-full h-[27vh] m-full"
            onClick={() => navigate('/')}
          >
            <h1>ADD ROUTINE</h1>
          </button>
        </div>
      </div>
    </div>
  );
};

export default AddPage;
