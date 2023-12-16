import axios from 'axios';
import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { BASE_API_URL } from '../../constants';

import { getRoomImage } from '../../utils';

import Navbar from '../layout/Navbar';
import Header from '../layout/Header';
import OutputDeviceCard from '../layout/OutputDeviceCard';
import { FaPlusCircle } from 'react-icons/fa';

const RoomPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [room, setRoom] = useState(null);
  const [devices, setDevices] = useState(null);
  const [selectDevice, setSelectDevice] = useState(null);

  const user = JSON.parse(localStorage.getItem('user'))
    ? JSON.parse(localStorage.getItem('user'))
    : null;

  useEffect(() => {
    const initialize = async () => {
      axios
        .get(`${BASE_API_URL}/room/view?id=${id}`)
        .then((res) => {
          if (res.status === 200) {
            console.log('room details -> ', res.data);
            setRoom(res.data);
          }
        })
        .catch((err) => {
          console.log(err);
        });

      axios
        .get(`${BASE_API_URL}/outputs/list`)
        .then((res) => {
          if (res.status === 200) {
            console.log('devices -> ', res.data);
            setDevices(res.data);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    };
    initialize();
  }, [id]);

  const handleSubmit = () => {
    let res1 = axios.post(`${BASE_API_URL}/room/addDevice`, null, {
      params: {
        roomID: room?.id,
        deviceID: selectDevice?.id,
      },
    })
    let res2 = axios.post(`${BASE_API_URL}/outputs/associate`, null, {
      params: {
        deviceID: selectDevice?.id,
        roomID: room?.id,
      },
    })

    console.log(res1, res2);

    Promise.all([res1, res2]).then((res) => {
      if (res[0].status === 200 && res[1].status === 200) {
        console.log('added device to room');
        alert('Device added to room successfully!');
        window.location.reload();
      }
    }
    ).catch((err) => {
      console.log(err);
    })
  }

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
            {room?.devices.length != 0 ? (
              <div className="flex flex-wrap justify-start">
                {devices
                  ?.filter((d) => {
                    return room?.devices.includes(d.id);
                  })
                  .map((device) => (
                    <span
                      className="m-2"
                      key={device?.id}
                    >
                      <OutputDeviceCard
                        device={device}
                        room={room.name}
                      />
                    </span>
                  ))}
                <div
                  className={`card w-[180px] bg-primary h-[180px] m-2 border-[3px] border-primary flex text-white justify-center items-center cursor-pointer hover:shadow-xl transition-shadow duration-300`}
                  onClick={() =>
                    document.getElementById('add_device_to_room').showModal()
                  }
                >
                  <FaPlusCircle size={65} />
                </div>
              </div>
            ) : (
              <span>
                <h1>
                  No devices in this room yet.{' '}
                  <span
                    className="font-bold no-underline cursor-pointer text-primary"
                    onClick={() =>
                      document.getElementById('add_device_to_room').showModal()
                    }
                  >
                    Add one!
                  </span>
                </h1>
              </span>
            )}
          </div>
        </div>
      </div>

      <dialog
        id="add_device_to_room"
        className="border border-accent modal"
      >
        <div className="modal-box min-w-[85vw] rounded-2xl overflow-hidden">
          <h1 className="text-4xl font-bold ">Add new device to room</h1>
          <h2 className="text-3xl text-slate-500">Select a known device</h2>
          <span className="divider" />

          <div className={`flex flex-row justify-start overflow-x-scroll `}>
            {devices
              ?.filter((d) => {
                return !room?.devices.includes(d.id);
              })
              .map((device) => (
                <span
                  className={`m-2 rounded-2xl ${
                    selectDevice == device && 'ring-4 ring-primary'
                  }}`}
                  key={device?.id}
                  onClick={() => {
                    setSelectDevice(device);
                  }}
                >
                  <OutputDeviceCard
                    device={device}
                    room={room.name}
                  />
                </span>
              ))}
          </div>
          <span className="divider" />
          <div>
            <h2 className="text-3xl text-slate-500">Selected Device:</h2>
            <div className="flex flex-wrap">
              {selectDevice && (
                <div className="w-auto p-2 m-2 text-xl border-2 rounded-full border-primary">
                  <h1 className="m-2">{selectDevice?.name}</h1>
                </div>
              )}
            </div>
          </div>

          <div className="modal-action">
            <form
              method="dialog"
              className="flex justify-around m-2"
            >
              <button className="m-2 btn">Close</button>
              <button
                className="m-2 btn btn-primary"
                onClick={() => {
                  handleSubmit();
                }}
              >
                Add Devices
              </button>
            </form>
          </div>
        </div>
      </dialog>
    </div>
  );
};

export default RoomPage;
