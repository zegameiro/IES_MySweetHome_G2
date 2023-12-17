import React, {useEffect, useState} from "react";

import axios from "axios";
import { BASE_API_URL } from "../../constants";

import { FaTrashAlt } from "react-icons/fa";
import { MdEditSquare } from "react-icons/md";

const RoutineCard = ({ routine, isSensorRoutine }) => {
    const [outputDevice, setOutputDevice] = useState(null);
    const [inputDevice, setInputDevice] = useState(null);

    const convertTimestamp = (timestamp) => {
        const date = new Date(timestamp);
  
        const hours = date.getHours();
        const minutes = date.getMinutes();
        return `${hours}h ${minutes}min`;
      };

    const getOutputDevice = async (outdeviceId) => {
        try {
            const res = await axios.get(`${BASE_API_URL}/outputs/view?id=${outdeviceId}`);
            if (res.status === 200)
                setOutputDevice(res.data);
        } catch (error) {
            console.log(error);
        }
    }

    const getInputDevice = async (indeviceId) => {
        try {
            const res = await axios.get(`${BASE_API_URL}/sources/view?id=${indeviceId}`);
            if (res.status === 200)
                setInputDevice(res.data);
                console.log(res.data)
        } catch (error) {
            console.log(error);
        }
    }

    const getRoutineDescription = () => {
        if (routine) {
            if (isSensorRoutine) {
                if (routine.trigger_type === "range") {
                    return <p className="text-xl">If the 
                        <span className="font-semibold"> {inputDevice? inputDevice.reading_type : null} reaches </span>
                        <span className="font-semibold">{routine.input_ranges[0]}</span> then the output device <span className="font-semibold"> {outputDevice ? outputDevice.name : null}</span> should <span className="font-semibold">{routine.associated_action.action_description} { outputDevice ? outputDevice.hasOwnProperty('location') ? " in the " + outputDevice.location : null : null }</span> 
                        until it reaches the value <span className="font-semibold">{routine.input_ranges[1]}</span> </p>;
                        
                } else {
                    return <p className="text-xl">If the 
                        <span className="font-semibold"> {inputDevice? inputDevice.reading_type : null} reaches </span>
                        <span className="font-semibold">{routine.exact_value}</span> then the output device <span className="font-semibold"> {outputDevice ? outputDevice.name : null}</span> should <span className="font-semibold">{routine.associated_action.action_description} { outputDevice ? outputDevice.hasOwnProperty('location') ? " in the " + outputDevice.location : null : null }</span> </p>;
                }
            } else {
                return <p className="text-xl">At 
                        <span className="font-semibold"> {convertTimestamp(routine.trigger_timestamp)} </span>
                        the output device <span className="font-semibold"> {outputDevice ? outputDevice.name : null}</span> should <span className="font-semibold">{routine.associated_action.action_description} { outputDevice ? outputDevice.hasOwnProperty('location') ? " in the " + outputDevice.location : null : null }</span> </p>;
            }
        }
    
    }

    useEffect(() => {
        if (routine) {
            getOutputDevice(routine.associated_action.outputDeviceID);
            if (isSensorRoutine) 
                getInputDevice(routine.source_id);
        }
    }, [routine]);

    return (   
        <div className="pt-3">
            <div className="card w-[100%] h-[100%] border border-[3px] border-primary flex flex-col p-4">
                <div className="flex flex-row text-center justify-between pb-3">
                    <h1 className="text-lg">On</h1>
                    <input type="checkbox" className="toggle toggle-primary" />
                </div>
                <div className="pl-4 pr-4 pb-2">
                    <h1 className="text-xl font-bold">{routine.routine_name !== null ? routine.routine_name : "Routine"}</h1>
                    {getRoutineDescription()}
                </div>
                <div className="flex flex-row justify-end space-x-5">
                    <button className="btn btn-error text-white text-lg"> <FaTrashAlt/> Delete</button>
                    <button className="btn btn-success text-white text-lg"> <MdEditSquare/> Edit</button>
                </div>
            </div>
        </div>
    );
};

export default RoutineCard;