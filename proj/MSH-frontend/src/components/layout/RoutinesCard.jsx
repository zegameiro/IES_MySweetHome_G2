/* eslint-disable react/prop-types */
import React, { useEffect, useState } from 'react';

import axios from 'axios';
import { BASE_API_URL } from '../../constants';

import { FaTrashAlt } from 'react-icons/fa';
import { MdEditSquare } from 'react-icons/md';

const RoutineCard = ({ routine, isSensorRoutine }) => {
  const [outputDevice, setOutputDevice] = useState(null);
  const [inputDevice, setInputDevice] = useState(null);
  const [inputDeviceUnit, setInputDeviceUnit] = useState(null);
  const [roomName, setRoomName] = useState(null);
  const [isChecked, setIsChecked] = useState(routine.active);

  const convertTimestamp = (timestamp) => {
    const date = new Date(timestamp);

    const hours = date.getHours();
    const minutes = date.getMinutes();
    return `${hours}h ${minutes}min`;
  };

  const getOutputDevice = async (outdeviceId) => {
    try {
      const res = await axios.get(
        `${BASE_API_URL}/outputs/view?id=${outdeviceId}`
      );
      const rn = await axios.get(
        `${BASE_API_URL}/outputs/getRoom?id=${outdeviceId}`
      );

      if (res.status === 200 && rn.status === 200) {
        setOutputDevice(res.data);
        if (rn.data !== 'null') setRoomName(rn.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const getInputDevice = async (indeviceId) => {
    try {
      const res = await axios.get(
        `${BASE_API_URL}/sources/view?id=${indeviceId}`
      );
      const unit = await axios.get(
        `${BASE_API_URL}/sources/unit?source_id=${indeviceId}`
      );

      if (res.status === 200 && unit.status === 200) {
        setInputDevice(res.data);
        setInputDeviceUnit(unit.data);
        console.log(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const deleteRoutine = async () => {
    let res = null;
    try {
      if (isSensorRoutine)
        res = await axios.delete(
          `${BASE_API_URL}/routines/deleteSB?id=${routine.id}`
        );
      else
        res = await axios.delete(
          `${BASE_API_URL}/routines/deleteTB?id=${routine.id}`
        );

      if (res.status === 200) {
        console.log('Routine deleted successfully');
      }
    } catch (error) {
      console.log(error);
    }
  };

  const changeState = async (routineId) => {
    let res = null;
    try {
      if (isSensorRoutine)
        res = await axios.post(
          `${BASE_API_URL}/routines/changeStateSB?id=${routineId}`
        );
      else
        res = await axios.post(
          `${BASE_API_URL}/routines/changeStateTB?id=${routineId}`
        );

      if (res.status === 200) {
        console.log('State changed successfully');
      }
    } catch (error) {
      console.log(error);
    }
  };

  const getRoutineDescription = () => {
    if (routine) {
      if (isSensorRoutine) {
        if (routine.trigger_type === 'range') {
          return (
            <p className="text-2xl">
              If the
              <span className="font-semibold">
                {' '}
                {inputDevice ? inputDevice.reading_type : null} reaches{' '}
              </span>
              <span className="font-semibold">
                {routine.input_ranges[0]}{' '}
                {inputDeviceUnit ? inputDeviceUnit : null}
              </span>{' '}
              then the output device{' '}
              <span className="font-semibold">
                {' '}
                {outputDevice ? outputDevice.name : null}
              </span>{' '}
              should{' '}
              <span className="font-semibold">
                {routine.associated_action.action_description}{' '}
                {roomName ? 'in the ' + roomName : null}
              </span>{' '}
              until it reaches{' '}
              <span className="font-semibold">
                {routine.input_ranges[routine.input_ranges.length - 1]}{' '}
                {inputDeviceUnit ? inputDeviceUnit : null}
              </span>{' '}
            </p>
          );
        } else {
          return (
            <p className="text-2xl">
              If the
              <span className="font-semibold">
                {' '}
                {inputDevice ? inputDevice.reading_type : null} reaches{' '}
              </span>
              <span className="font-semibold">{routine.exact_value}</span>{' '}
              {inputDeviceUnit ? inputDeviceUnit : null} then the output device{' '}
              <span className="font-semibold">
                {' '}
                {outputDevice ? outputDevice.name : null}
              </span>{' '}
              should{' '}
              <span className="font-semibold">
                {routine.associated_action.action_description}
              </span>{' '}
              in the{' '}
              <span className="font-semibold">
                {roomName ? roomName : null}
              </span>{' '}
            </p>
          );
        }
      } else {
        return (
          <p className="text-2xl">
            At
            <span className="font-semibold">
              {' '}
              {convertTimestamp(routine.trigger_timestamp)}{' '}
            </span>
            the output device{' '}
            <span className="font-semibold">
              {' '}
              {outputDevice ? outputDevice.name : null}
            </span>{' '}
            should{' '}
            <span className="font-semibold">
              {routine.associated_action.action_description}{' '}
              {roomName ? ' in the ' + roomName : null}
            </span>{' '}
          </p>
        );
      }
    }
  };

  useEffect(() => {
    if (routine) {
      getOutputDevice(routine.associated_action.outputDeviceID);
      if (isSensorRoutine) getInputDevice(routine.source_id);
    }
  }, [isChecked]);

  return (
    <div className="pt-[30px]">
      <div
        className={`card w-[100%] h-[100%] border border-[3px] ${
          isChecked ? 'border-primary' : 'border-accent'
        }  flex flex-col p-4 hover:shadow-xl transition-shadow duration-300`}
      >
        <div className="flex flex-row justify-between pb-3 text-center">
          <h1 className="text-lg">{isChecked ? 'On' : 'Off'}</h1>
          <input
            type="checkbox"
            className={`toggle ${
              isChecked ? 'toggle-primary' : 'toggle-accent'
            } peer`}
            checked={isChecked ? true : false}
            onChange={(e) => {
              setIsChecked(e.target.checked);
              changeState(routine?.id);
            }}
          />
        </div>
        <div className="pb-2 pl-4 pr-4">
          <div
            className={`badge ${
              isChecked ? 'badge-primary' : 'badge-accent'
            } text-white text-xl font-bold badge-lg mb-4`}
            style={{ width: 'auto', height: '3rem' }}
            
          >
            {routine.routine_name !== null ? routine.routine_name : 'Routine'}
          </div>
          {getRoutineDescription()}
        </div>
        <div className="flex flex-row justify-end space-x-5">
          <button
            className="text-lg text-white btn btn-error"
            onClick={() => deleteRoutine()}
          >
            {' '}
            <FaTrashAlt /> Delete
          </button>
          
        </div>
      </div>
    </div>
  );
};

export default RoutineCard;
