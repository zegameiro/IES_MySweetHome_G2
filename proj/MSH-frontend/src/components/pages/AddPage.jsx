import { useNavigate } from 'react-router-dom';

import Navbar from '../layout/Navbar';
import Header from '../layout/Header';

const AddPage = () => {
  const navigate = useNavigate();

  return (
    <div className="mx-[5%] mt-4 flex justify-between">
      <Navbar />
      <div className="flex flex-col w-full h-full">
        <Header />
        <div className="flex flex-row justify-between">
          <div className="w-full h-full m-2">
            <button
              className="bg-[url('/src/assets/images/home1.jpg')] rounded-xl shadow-lg p-4 m-4 flex justify-start items-end font-bold text-3xl w-full h-[80vh] "
              onClick={() => navigate('/addroom')}
            >
              <h1>ADD ROOM</h1>
            </button>
          </div>
          <div className="w-full h-full m-2">
            <button
              className="bg-[url('/src/assets/images/home2.jpg')] rounded-xl shadow-lg p-4 m-4 flex justify-start items-end font-bold text-3xl w-full h-[80vh] "
              onClick={() => navigate('/adddevice')}
            >
              <h1>ADD DEVICE</h1>
            </button>
          </div>
          <div className="w-full h-full m-2">
            <button
              className="bg-[url('/src/assets/images/home3.jpg')] rounded-xl shadow-lg p-4 m-4 flex justify-start items-end font-bold text-3xl w-full h-[80vh] "
              onClick={() => navigate('/')}
            >
              <h1>ADD ROUTINE</h1>
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AddPage;
