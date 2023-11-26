import React from 'react';
import { useState } from 'react';
import axios from 'axios';
import { BASE_API_URL } from '../../constants';

import { TbAirConditioningDisabled, TbAirConditioning } from "react-icons/tb";
import { MdOutlineSpeaker, MdSpeaker } from "react-icons/md";
import { PiMonitorBold, PiMonitorFill } from "react-icons/pi";
import { FaLightbulb, FaRegLightbulb  } from 'react-icons/fa6';

const DeviceCard = (props) => {
    const isBig = props.isBig;
    let device = props.device;
    const rooms = props.rooms;
    const [isChecked, setIsChecked] = useState(device["state"] === "on" ? true : false);
    

    const getIcon = (category, state) => {
        switch (category) {
            case "0":
                if (state) {
                    return <FaLightbulb />
                } else {
                    return <FaRegLightbulb />
                }
            case "1":
                if (state) {
                    return <TbAirConditioning/>
                } else {
                    return <TbAirConditioningDisabled/>
                }
            case "2":
                if (state) {
                    return <PiMonitorBold />
                } else {
                    return <PiMonitorFill />
                }
            case "3":
                if (state) {
                    return <MdSpeaker />
                } else {
                    return <MdOutlineSpeaker />
                }

        }
    }

    const getName = (category) => {
        switch (category) {
            case "0":
                return "Ceiling Lights";
            case "1":
                return "Air Conditioner";
            case "2":
                return "Tv";
            case "3":
                return "Speakers";
        }
    }

    const changeState = async (deviceID, newState) => {
        try {
            const res = await axios.post(`${BASE_API_URL}/outputs/changeState?id=${deviceID}`, {
                state: newState
            });
            if (res.status === 200) {
                console.log('changed state');
                device = res.data;
            }
        } catch (error) {
            console.log(error);
        }
      }

    return (
        <>
            {isBig ? 
                <div className={`card w-[280px] h-[180px] border-solid border-[3px] ${isChecked ? "border-primary" : "border-accent"} flex flex-col justify-between hover:shadow-xl transition-shadow duration-300`}>
                    <div className='flex justify-between items-center'>
                        <div className='flex text-lg font-medium pl-5 pt-2'> 
                            { isChecked ? <h1 className='text-primary'>On</h1> : <h1 className='text-accent'>Off</h1> }
                        </div>
                        <div className='flex pr-5 pt-2'>
                            <input type="checkbox" className={`toggle ${isChecked ? 'toggle-primary' : 'toggle-accent'} peer`} 
                                checked={isChecked ? true : false} 
                                onChange={(e) => {
                                    setIsChecked(e.target.checked);
                                    changeState(device["id"], e.target.checked ? 'on' : 'off');
                                }}
                            />
                        </div>
                    </div>
                    <div className='justify-between flex'>
                        <div className="card-body flex justify-between pl-4 pt-2">
                            <div className={`text-6xl pl-5 mb-[10px] font-medium ${isChecked ? "text-primary" : "text-accent"}`}>
                                {getIcon(device["category"], isChecked)}
        
                            </div>
                            <div>
                                <div className={`text-base font-semibold ${isChecked ? "text-primary" : "text-accent"}`}>
                                    <h1>Ambient LEDS</h1>
                                </div>
                                {rooms[0]["devices"].includes(device["id"]) ? 
                                    <p className='items-centers text-sm'> On <strong>{ rooms[0]["name"] }</strong> </p> 
                                : 
                                    <p className='text-sm'>Room Unknown</p> 
                                }
                            </div>
                        </div>
                        <div className='flex justify-end'>
                            { isChecked ?
                                <div className='flex-col pr-4'>
                                    <div className='flex text-sm pt-[30px]'>
                                        <span className='bg-[#EA80FF] font-semibold'>#EA80FF</span>
                                    </div>
                                    <div className='flex flex-col items-center pt-[40px] text-slate-500'>
                                        <p className='text-sm'>Uptime</p>
                                        <p className='text-sm font-bold'> 2h 46min </p>
                                    </div>
                                </div>
                            :
                                <div></div>
                            }
                        </div>
                    </div>
                
                </div>
            :
                <div className={`card w-[180px] h-[180px] border-solid border-[3px] ${isChecked ? 'border-primary' : 'border-accent'} flex flex-col text-center justify-between hover:shadow-xl transition-shadow duration-300`}>
                    <div className='flex justify-between pt-3 pr-3 pl-3'>
                        <div className='flex'> 
                            { isChecked ? <h1 className='text-md font-medium text-primary'>On</h1> : <h1 className='text-md font-medium text-accent'>Off</h1> }
                        </div>

                        <input type="checkbox" className={`toggle ${isChecked ? 'toggle-primary' : 'toggle-accent'} peer`} 
                            checked={isChecked ? true : false} 
                            onChange={(e) => {
                                setIsChecked(e.target.checked);
                                changeState(device["id"], e.target.checked ? 'on' : 'off');
                            }}
                        />

                    </div>
                    <div className={`card-body items-center pt-5 text-6xl mb-[10px] font-medium ${isChecked ? 'text-primary' : 'text-accent' }`}>  
                        <div className='text-5xl mb-[10px]'>
                            {getIcon(device["category"], isChecked)}
                        </div>  
                        <div className="text-base">
                            <p>{getName(device["category"])}</p>
                        </div>
                    </div>
                </div>
            }
        </>
    )
}

export default DeviceCard;