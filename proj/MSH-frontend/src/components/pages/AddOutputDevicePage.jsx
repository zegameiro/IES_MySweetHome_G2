import React, {useState, useEffect } from "react";

import Header from "../layout/Header";
import Navbar from "../layout/Navbar";

import { useNavigate } from 'react-router-dom';

import { TbAirConditioning } from 'react-icons/tb';
import { MdSpeaker } from 'react-icons/md';
import { PiMonitorFill } from 'react-icons/pi';
import { FaRegLightbulb } from 'react-icons/fa6';
import { WiHumidity } from 'react-icons/wi';

import { getRoomImage } from '../../utils';
import '../../utils/index.css';

import { BASE_API_URL } from "../../constants";
import axios from "axios";

const AddOutputDevicePage = () => {

    const [allRooms, setAllRooms] = useState([]);
    const [allCategories, setAllCategories] = useState([]);
    const [room, setRoom] = useState("");
    const [category, setCategory] = useState("");
    const [deviceName, setDeviceName] = useState("");
    const navigate = useNavigate();

    const getAllRooms = async () => {
        try {
            const res = await axios.get(`${BASE_API_URL}/room/list`);
            if (res.status === 200) {
                setAllRooms(res.data);
            }
        } catch (error) {
            console.log(error);
        }
    }

    const getAllCategories = async () => {
        try {
            const res = await axios.get(`${BASE_API_URL}/outputs/listCategories`);
            if (res.status === 200) {
                setAllCategories(res.data);
            }
        } catch (error) {
            console.log(error);
        }
    }

    const getIcon = (category_name) => {
        switch (category_name) {
            case 'LIGHT':
                return <FaRegLightbulb />;
            
            case 'AIR_CONDITIONER':
                return <TbAirConditioning />;
            
            case 'SPEAKER':
                return <MdSpeaker />;

            case 'TELEVISION':
                return <PiMonitorFill />;

            case 'DEHUMIDIFER':
                return <WiHumidity />;

            default:
                break;
        }
    }
    

  const handleSubmit = () => {
    console.log(room, deviceName, category);
    if (room === '' || deviceName === '' || category === '') {
      alert('Please fill in all the fields');
      return;
    }
    
    axios.post(`${BASE_API_URL}/outputs/add`, null, {
        params: {
          category: Number(category),
          state: "0",
          name: deviceName,
          roomID: room,
        },
      })
      .then((res) => {
        if (res.status === 200) {
          alert('Output Device added successfully');
          navigate('/dashboard');
        }
      })
      .catch((err) => {
        alert('Error adding output device. Please try again.');
        console.log(err);
      });
  };

    useEffect(() => {
        if (localStorage.getItem("user")) {
            getAllRooms();
            getAllCategories();
        } else {
            navigate("/login?redirect=adddevice");
        }
    }, []);

    return (
        <div className="mx-[5%] mt-4 flex justify-between">
            <Navbar />
            <div className="flex flex-col w-full h-full">
                <Header />
                <div className="flex flex-col pl-5 w-[100%]">
                    <h1 className="m-4 text-4xl font-bold">Add a new device</h1>
                    <h2 className="m-4 text-3xl font-bold text-slate-500">Select a room</h2>
                    <hr className="pb-5 max-w-[90%]" />  
                    <div className="flex flex-row overflow-x-auto space-x-5 w-[90%] h-[50%] custom-scrollbar p-2">
                        
                        { allRooms.length > 0 ?
                            allRooms.map((r) => (
                                <div className={`justify-between cursor-pointer rounded-2xl ${room === r.id ? "ring-4 ring-primary" : null }`} onClick={() => setRoom(r.id)} key={r.id} >
                                    <div className="flex flex-col rounded-2xl w-[20vw] h-[20vh] bg-cover"  
                                        style={{backgroundImage: `url(${getRoomImage(r?.type)})`}}
                                    >
                                        <span className="flex flex-col justify-end w-full h-full p-4 rounded-2xl  text-white hero-overlay ">
                                            <h1 className="text-xl font-semibold">{r?.name}</h1>
                                            <h1>{r?.devices.length} {r.devices.length === 1 ? "device" : "devices"}</h1>
                                        </span>
                                    </div>
                                </div>
                            ))
                        :   
                            <span>
                                <h1>
                                    No rooms saved yet.{' '}
                                    <span className="font-bold no-underline cursor-pointer text-primary" onClick={() => navigate('/addroom')}>
                                        Add one!
                                    </span>
                                </h1>
                            </span>
                        }   
                    </div>
                    <h2 className="m-4 text-3xl font-bold text-slate-500">Select a category</h2>
                    <hr className="pb-5 max-w-[90%]" />  
                    <div className="flex flex-row space-x-5 pr-[10%]">
                        { allCategories.length > 0 ?
                            allCategories.map((c) => (
                                <div 
                                    className={`card w-full cursor-pointer h-[15vh] border-solid border-[3px] border-primary flex flex-col text-center justify-between hover:shadow-xl transition-shadow duration-300 ${category === c.id ? "ring-4 ring-primary" : null}`} 
                                    key={c.id} 
                                    onClick={() => setCategory(c.id)}
                                >
                                    <div
                                        className="card-body items-center pt-5 text-6xl mb-[10px] font-medium text-primary">
                                        <div className="text-5xl mb-[10px]">
                                            {getIcon(c.name)}
                                        </div>
                                        <div className="text-base">
                                            <p>{c.name}</p>
                                        </div>
                                    </div>
                                </div>
                            ))
                        :
                            <span>
                                <h1>
                                    No routines saved yet.{' '}
                                    <span className="font-bold no-underline cursor-pointer text-primary" onClick={() => navigate('/addroom')}>
                                        Add one!
                                    </span>
                                </h1>
                            </span>
                        }
                    </div>
                    <div className="flex justify-between my-4 pr-[40%]">
                        <div className="flex w-full">
                            <span className="flex flex-col w-1/2 mx-4">
                                <h2 className="my-4 text-3xl font-bold text-slate-500">
                                    Name the device
                                </h2>
                                <input
                                    className="text-lg input input-primary "
                                    type="text"
                                    placeholder='e.g. "Light Bulb"'
                                    onChange={(e) => setDeviceName(e.target.value)}
                                />
                            </span>
                        </div>
                        <button
                            className="self-end w-1/3 mx-4 text-xl btn btn-primary"
                            onClick={handleSubmit}
                        >
                            Add Output Device
                        </button>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default AddOutputDevicePage;