import React from 'react';
import { useState } from 'react';
import { FaLightbulb } from 'react-icons/fa6';

const deviceCard = (isBig) => {

    const [isChecked, setIsChecked] = useState(false);

    return (
        <div className={`card w-80 border-solid border-[3px] ${isChecked ? 'border-primary' : 'border-accent'} flex flex-col text-center justify-between hover:shadow-xl transition-shadow duration-300`}>
            <div className='flex justify-end p-2'>
                <input type="checkbox" className="toggle toggle-primary peer" checked={isChecked} onChange={() => setIsChecked(!isChecked)} />
            </div>
            <div className="card-body items-center">    
                <FaLightbulb className={`text-8xl mb-[10px] font-medium ${isChecked ? 'text-primary' : 'text-accent'}`} />
                <div className={`justify-end text-3xl font-medium ${isChecked ? 'text-primary' : 'text-accent'}`}>
                    <h1>Light Bulb</h1>
                </div>
            </div>
        </div>
    )
}

export default deviceCard;