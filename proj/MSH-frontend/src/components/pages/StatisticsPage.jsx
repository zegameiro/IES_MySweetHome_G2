import React, { useEffect, useState } from "react";
import { AreaChart, BarChart, Bar, Legend, Rectangle, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import axios from "axios";

import Navbar from "../layout/Navbar";
import Header from "../layout/Header";
import StatisticsCard from "../layout/StatisticsCard";

import { BASE_API_URL } from "../../constants";

const data = [
    {
      name: 'A',
      uv: 4000,
      pv: 2400,
      amt: 2400,
    },
    {
      name: 'B',
      uv: 3000,
      pv: 1398,
      amt: 2210,
    },
    {
      name: 'C',
      uv: 2000,
      pv: 9800,
      amt: 2290,
    },
    {
      name: 'D',
      uv: 2780,
      pv: 3908,
      amt: 2000,
    },
    {
      name: 'E',
      uv: 1890,
      pv: 4800,
      amt: 2181,
    },
    {
      name: 'F',
      uv: 2390,
      pv: 3800,
      amt: 2500,
    },
    {
      name: 'G',
      uv: 3490,
      pv: 4300,
      amt: 2100,
    },
];
  

const StatisticsPage = () => {

    const [inputDevices, setInputDevices] = useState([]);
    const [selectedCard, setSelectedCard] = useState(null);

    const getInputDevices = async () => {
        try {
            const res = await axios.get(`${BASE_API_URL}/sources/list`);
            if (res.status === 200) {
                console.log("received data");
                setInputDevices(res.data);
            }
        } catch (error) {
            console.log(error);
        }
    }

    useEffect(() => {
        if (localStorage.getItem("user")) {
          getInputDevices();
        } else {
          navigate("/login?redirect=dashboard");
        }
    }, [selectedCard]);

    return (
        <div className="mx-[5%] mt-4 flex justify-between pb-[10vh]">
            <Navbar />
            <div className="flex flex-col w-full h-full">
                <Header />
                <div className="pl-[5%] pt-[3%]">
                    <h1 className="text-5xl">Statistics</h1>
                    <div className="flex flex-row pt-[3%] pb-[5%] justify-center space-x-[10%] w-[70%]">
                        {inputDevices.length > 0
                            ? 
                                inputDevices.map((inputDevice) => {
                                    return (
                                        <button onClick={() => setSelectedCard(inputDevice.id)} key={inputDevice.id}>
                                            <StatisticsCard 
                                                isChecked={inputDevice.id === selectedCard} 
                                                inputDevice={inputDevice}
                                            />
                                        </button>
                                    )
                                })
                            : 
                                <h1>No input devices found</h1>
                        }
                    </div>
                    <div className="flex flex-row space-x-5">
                        <div className="flex flex-col text-center">
                            <h1 className="text-xl font-semibold">Daily consume</h1>
                            <ResponsiveContainer width={500} height={500}>
                                <div className="card border-3px border-solid border-accent">
                                    <AreaChart
                                        width={500}
                                        height={400}
                                        data={data}
                                        margin={{
                                        top: 10,
                                        right: 30,
                                        left: 0,
                                        bottom: 0,
                                        }}
                                    >
                                        <CartesianGrid strokeDasharray="3 3" />
                                        <XAxis dataKey="name" />
                                        <YAxis />
                                        <Tooltip />
                                        <Area type="monotone" dataKey="uv" stroke="#8884d8" fill="#8884d8" />
                                    </AreaChart>
                                </div>
                            </ResponsiveContainer>
                        </div>
                        <div className="flex flex-col text-center">
                            <h1 className="text-xl font-semibold">Weekly Consume</h1>
                            <ResponsiveContainer width={500} height={435}>
                                <BarChart
                                    width={500}
                                    height={300}
                                    data={data}
                                    margin={{
                                        top: 5,
                                        right: 30,
                                        left: 20,
                                        bottom: 5,
                                    }}
                                >
                                    <CartesianGrid strokeDasharray="6 6" />
                                    <XAxis dataKey="name" />
                                    <YAxis />
                                    <Tooltip />
                                    <Legend />
                                    <Bar dataKey="pv" fill="#8884d8" activeBar={<Rectangle fill="pink" stroke="blue" />} barSize={20}/>
                                </BarChart>
                            </ResponsiveContainer>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default StatisticsPage;