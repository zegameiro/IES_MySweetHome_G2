import axios from 'axios';
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { BASE_API_URL } from '../../constants';

import { getRoomImage } from '../../utils';

import Navbar from '../layout/Navbar';
import Header from '../layout/Header';

const RoomPage = () => {
  const { id } = useParams();
  const [room, setRoom] = useState(null);

  const user = JSON.parse(localStorage.getItem('user'))
    ? JSON.parse(localStorage.getItem('user'))
    : null;

  useEffect(() => {
    axios
      .get(`${BASE_API_URL}/room/view?id=${id}`)
      .then((res) => {
        if (res.status === 200) {
          console.log(res.data);
          setRoom(res.data);
        }
      })
      .catch((err) => {
        console.log(err);
      });
  }, [id]);

  return (
    <div className="mx-[5%] mt-4 flex justify-between«">
      <Navbar />
      <div className="flex flex-col w-full h-full">
        <Header />
        <div className="w-full mx-4">
          <div
            className="w-full h-[40vh] bg-cover bg-center rounded-2xl overflow-hidden"
            style={{
              backgroundImage: `url(${getRoomImage(room?.type)})`,
            }}
          >
            <span className="flex items-end justify-start w-full h-full p-4 text-5xl font-semibold text-white hero-overlay ">
              <h1>{room?.name}</h1>
            </span>
          </div>

          <div className="w-full mx-4">
            <h1 className="my-4 text-4xl font-bold">Connected Devices</h1>
            <span className='divider' />
          </div>
        </div>
      </div>
    </div>
  );
};

export default RoomPage;
