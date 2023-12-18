import { useEffect, useState } from 'react';

import { BASE_API_URL } from '../../constants';
import axios from 'axios';

import Header from '../layout/Header';
import Navbar from '../layout/Navbar';
import RoutineCard from '../layout/RoutinesCard';

import { useNavigate } from 'react-router-dom';

import { IoMdAddCircle } from 'react-icons/io';

const RoutinesPage = () => {
  const user = JSON.parse(localStorage.getItem('user'))
    ? JSON.parse(localStorage.getItem('user'))
    : null;

  const [routinesSensor, setRoutinesSensor] = useState([]);
  const [routinesTime, setRoutinesTime] = useState([]);
  const navigate = useNavigate();

  const getRoutines = async () => {
    try {
      const res1 = await axios.get(`${BASE_API_URL}/routines/listSB`);
      const res2 = await axios.get(`${BASE_API_URL}/routines/listTB`);

            if (res1.status === 200 && res2.status === 200) {
                setRoutinesSensor(res1.data);
                setRoutinesTime(res2.data);

                console.log(res1.data);
                console.log(res2.data);

            }
        } catch (error) {
            console.log(error);
        }
    }

    useEffect(() => {
        if (localStorage.getItem("user")) {
            getRoutines();
        } else {
          navigate("/login?redirect=dashboard");
        }
    }, []);

  return (
    <div className="mx-[5%] mt-4 flex justify-between">
      <Navbar />
      <div className="flex flex-col w-full h-full">
        <Header />
        <div className="w-full mx-4">
          <div className="flex flex-row justify-between max-w-[70%] m-4">
            <h1 className="text-4xl font-bold"> {user.firstName}'s Routines</h1>
            <button className="text-2xl text-white btn btn-primary" onClick={() => navigate('/addroutine')}>
              {' '}
              Add <IoMdAddCircle />
            </button>
          </div>
          <div className="flex flex-col max-w-[60vw] pl-[5%]">
            {routinesSensor.length > 0 ? (
              routinesSensor.map((routine) => (
                <RoutineCard
                  key={routine.id}
                  routine={routine}
                  isSensorRoutine
                />
              ))
            ) : (
              <span>
                <h1>
                  No routines saved yet.{' '}
                  <span
                    className="font-bold no-underline cursor-pointer text-primary"
                    onClick={() =>
                      navigate('/addroutine')
                    }
                  >
                    Add one!
                  </span>
                </h1>
              </span>
            )}
            {routinesTime.length > 0
              ? routinesTime.map((routine) => (
                  <RoutineCard
                    key={routine.id}
                    routine={routine}
                  />
                ))
              : null}
          </div>
        </div>
      </div>
    </div>
  );
};

export default RoutinesPage;
