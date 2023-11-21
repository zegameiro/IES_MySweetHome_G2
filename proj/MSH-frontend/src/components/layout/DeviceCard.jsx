import React from 'react';
import { useState } from 'react';
import { FaLightbulb, FaRegLightbulb } from 'react-icons/fa6';

const DeviceCard = (props) => {
    const [isChecked, setIsChecked] = useState(false);
    const isBig = props.isBig;
    return (
        <>
            {isBig ? 
                <div className={`card w-[500px] border-solid border-[3px] ${isChecked ? 'border-primary' : 'border-accent'} flex flex-col justify-between hover:shadow-xl transition-shadow duration-300`}>
                    <div className='flex justify-between items-center p-5'>
                        <div className='flex p-1'> 
                            { isChecked ? <h1 className='text-3xl font-medium text-primary'>On</h1> : <h1 className='text-3xl font-medium text-accent'>Off</h1> }
                        </div>
                        <input type="checkbox" className="toggle toggle-primary peer" checked={isChecked} onChange={() => setIsChecked(!isChecked)} />
                    </div>
                    <div className='justify-between flex'>
                        <div className="card-body pb-10 flex justify-between">
                            <div className='pl-5'>
                                {isChecked ?
                                    <FaLightbulb className="text-8xl mb-[10px] font-medium text-primary" />
                                    :
                                    <FaRegLightbulb className="text-8xl mb-[10px] font-medium text-accent" />
                                }
                                
                            </div>
                            <div className={`text-3xl font-medium ${isChecked ? 'text-primary' : 'text-accent'}`}>
                                <h1>Ambient LEDS</h1>
                            </div>
                            <p className='items-center'>On <strong>Kitchen</strong></p>
                        </div>
                        <div className='flex justify-end p-5'>
                            { isChecked ?
                                <div className='flex-col'>
                                    <div className='flex'>
                                        <div>
                                            <span className='bg-[#EA80FF] font-semibold'>#EA80FF</span>
                                        </div>
                                    </div>
                                    <div className='flex flex-col items-center pt-[120px] text-slate-500'>
                                        <p>Uptime</p>
                                        <p className='text-xl font-bold'> 2h 46min </p>
                                    </div>
                                </div>
                            :
                                <div>
                                    <span className='bg-'></span>
                                </div>
                            }
                        </div>
                    </div>
                
                </div>
            :
                <div className={`card w-80 border-solid border-[3px] ${isChecked ? 'border-primary' : 'border-accent'} flex flex-col text-center justify-between hover:shadow-xl transition-shadow duration-300`}>
                    <div className='flex justify-between p-5'>
                        <div className='flex p-1'> 
                            { isChecked ? <h1 className='text-3xl font-medium text-primary'>On</h1> : <h1 className='text-3xl font-medium text-accent'>Off</h1> }
                        </div>
                        <input type="checkbox" className={`toggle ${isChecked ? 'toggle-primary': 'toggle-accent'}`} checked={isChecked} onChange={() => setIsChecked(!isChecked)} />
                    </div>
                    <div className="card-body items-center pb-16">    
                        {isChecked ?
                            <FaLightbulb className="text-8xl mb-[10px] font-medium text-primary" />
                            :
                            <FaRegLightbulb className="text-8xl mb-[10px] font-medium text-accent" />
                        }
                        <div className={`justify-end text-3xl font-medium ${isChecked ? 'text-primary' : 'text-accent'}`}>
                            <h1>Light Bulb</h1>
                        </div>
                    </div>
                </div>
            }
        </>
    )
}

export default DeviceCard;