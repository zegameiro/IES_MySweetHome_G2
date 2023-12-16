import React, { useEffect, useState } from "react";
import { AreaChart, BarChart, Bar, Area, XAxis, YAxis, CartesianGrid, Tooltip, Cell, Label } from 'recharts';
import axios from "axios";

import Navbar from "../layout/Navbar";
import Header from "../layout/Header";
import StatisticsCard from "../layout/StatisticsCard";

import { MdElectricBolt } from "react-icons/md";
import { FaRegSun } from "react-icons/fa";
import { FiWind } from "react-icons/fi";

import { BASE_API_URL } from "../../constants";
 

const StatisticsPage = () => {

    const [inputDevices, setInputDevices] = useState([]);
    const [selectedCard, setSelectedCard] = useState(null);
    const [statsDaily, setStatsDaily] = useState([]);
    const [statsWeekly, setStatsWeekly] = useState([]);
    const [currentInformation, setCurrentInformation] = useState([]);
    const [category, setCategory] = useState(null); // [1, 2, 3] -> [temperature, electricity, wind]
    const [unit, setUnit] = useState(null);
    const [timeInterval, setTimeInterval] = useState(null);

    const getStatsWeekly = async (deviceId) => {
        try {
            const res = await axios.get(`${BASE_API_URL}/stats/sensor/view/weekly?sensor_id=${deviceId}`);
            if (res.status === 200) {
                handleStats(true, res.data)
            }
        } catch (error) {
            console.log(error);
        }
    }

    const getStatsDaily = async (deviceId) => {
        try {
            const res = await axios.get(`${BASE_API_URL}/stats/sensor/view/daily?sensor_id=${deviceId}`);
            if (res.status === 200) {
                handleStats(false, res.data)
                setCategory(res.data.category);
                setUnit(res.data.unit);
            }
        } catch (error) {
            console.log(error);
        }
    }

    const getStatsCurrent = async (deviceId) => {
        try {
            const res = await axios.get(`${BASE_API_URL}/data/view`, {
                params: {
                    sensor_id: deviceId,
                    filter: "latest"
                },
            });
            if (res.status === 200) {
                setCurrentInformation(res.data);
            }
        } catch (error) {
            console.log(error);
        }
    }

    const handleStats = (isWeekly, data) => {
        const stats = [];
        if (isWeekly) {
            const days = ["Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat"];
            const shiftedDays = days.slice(new Date().getDay()).concat(days.slice(0, new Date().getDay())); 
            for (let i = 0; i < data.values.length; i++) {
                let stat = {}
                stat.day = shiftedDays[i];

                if (data.category === 1)
                    stat.temperature = Number(data.values[i]).toFixed(2);
                else if (data.category === 2)
                    stat.electricity = Number(data.values[i]).toFixed(2);
                else
                    stat.wind = Number(data.values[i]).toFixed(2);

                stat.cat = data.category;
                stat.unit = data.unit;
                stats.push(stat);
            }
            setStatsWeekly(stats);

        } else {
            const hours = ["00h", "01h", "02h", "03h", "04h", "05h", "06h", "07h", "08h", "09h", "10h", "11h", "12h", "13h", "14h", "15h", "16h", "17h", "18h", "19h", "20h", "21h", "22h", "23h"];
            const shiftedHours = hours.slice(new Date().getHours()).concat(hours.slice(0, new Date().getHours()));

            for (let i = 0; i < data.values.length; i++) {
                let stat = {}
                stat.hour = shiftedHours[i];

                if (data.category === 1)
                    stat.temperature = Number(data.values[i]).toFixed(2);
                else if (data.category === 2)
                    stat.electricity = Number(data.values[i]).toFixed(2);
                else
                    stat.wind = Number(data.values[i]).toFixed(2);

                stat.cat = data.category;
                stat.unit = data.unit;
                stats.push(stat);
            }
            setStatsDaily(stats);
        }
    }

    const getInputDevices = async () => {
        try {
            const res = await axios.get(`${BASE_API_URL}/sources/list`);
            if (res.status === 200) {
                setInputDevices(res.data);
            }
        } catch (error) {
            console.log(error);
        }
    }

    const getKeyName = (data) => {
        const category = data[0].cat;
        switch (category) {
            case 1:
                return "temperature";

            case 2:
                return "electricity";

            case 3:
                return "wind";

            default:
                return "unknown";
        }
    }


    const getTitle = (cat) => {
        switch (cat) {
            case 1:
                return "Temperature";

            case 2:
                return "Eletricity Consume";

            case 3:
                return "Wind Strength";

            default:
                return "Unknown";
        }
    
    }

    const getIcon = (category) => {
        switch (category) {
            case 1:
                return <FaRegSun />

            case 2:
                return <MdElectricBolt />
                
            case 3:
                return <FiWind />

            default:
                return null;
        }
    }

    const barColors = ["#58CEFF", "#00A28A", "#FF5B5A", "#AB53DB", "#FFBC54", "#4A52FF", "#e377c2", "#7f7f7f", "#bcbd22", "#17becf"];


    useEffect(() => {
        if (localStorage.getItem("user")) {
            getInputDevices();
        } else {
          navigate("/login?redirect=dashboard");
        }
    }, [selectedCard]);


    useEffect(() => {
        if (selectedCard) {
            getStatsWeekly(selectedCard);
            getStatsDaily(selectedCard);
            getStatsCurrent(selectedCard);
        }
    }, [selectedCard]);


    useEffect(() => {
        if (selectedCard) {

            const intervalId = setInterval(() => {
                getStatsCurrent(selectedCard);
            }, 5000); // Set an interval of 5 seconds to make calls to the API
            setTimeInterval(intervalId);
    
        } else
            clearInterval(timeInterval);

        return () => {
            clearInterval(timeInterval);
        };
      }, [selectedCard, currentInformation]);


    useEffect(() => {
        const now = new Date();
        const startOfDay = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 0, 0, 0);
        let delay = startOfDay.getTime() - now.getTime();
        if (delay < 0) 
            delay += 24 * 60 * 60 * 1000; // If it's already past midnight, wait until the next midnight
        
        const timeout = setTimeout(() => {
            if (selectedCard) {
                getStatsWeekly(selectedCard);
            }
        }, delay);

        return () => clearTimeout(timeout);
    }, [selectedCard]);


    useEffect(() => {
        const now = new Date();
        const startOfHour = new Date(now.getFullYear(), now.getMonth(), now.getDate(), now.getHours(), 0, 0);
        let delay = startOfHour.getTime() - now.getTime();
        if (delay < 0)
            delay += 60 * 60 * 1000; // If it's already past the hour, wait until the next hour

        const timeout = setTimeout(() => {
            if (selectedCard) {
                getStatsDaily(selectedCard);
            }
        }, delay);

        return () => clearTimeout(timeout);
    }, [selectedCard])


    return (
        <div className="mx-[5%] mt-4 flex justify-between pb-[10vh]">
            <Navbar />
            <div className="flex flex-col w-full h-full">
                <Header />
                <div className="pl-[5%] pt-[3%]">
                    <h1 className="text-5xl font-bold pb-5">Statistics</h1>
                    <div className="w-[70%] text-center pl-[10%]">
                        <p className="text-xl">Welcome to the statistics page, here you can obtain all the information about your input devices.<br/> Check out the data collected through a whole week or the daily data provided by your data sources. </p>
                    </div>
                    <div className="flex flex-row pt-[3%] pb-[5%] justify-center space-x-[10%] w-[86%]">
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
                    {selectedCard ? 
                        <div className="flex flex-col w-[86%] text-center items-center pb-5">
                            <div className="stats shadow">
                                <div className="stat">
                                    <div className="stat-title text-error text-lg font-semibold pb-2"> {selectedCard ? "Current " + getTitle(category) : null}</div>
                                    {currentInformation.length > 0 ?    
                                        <div className="flex flex-row text-center justify-center stat-value">
                                            {category === 2 ?
                                                Number(currentInformation[0].sensor_information).toFixed(4)
                                            :
                                                Number(currentInformation[0].sensor_information).toFixed(2)
                                            }

                                            {currentInformation[0].unit} ​ ​ <span className="text-warning">{getIcon(category)}</span>
                                        </div>
                                    :
                                        <div className="stat-value text-lg"><span className="loading loading-spinner loading-lg"></span></div>
                                    }
                                </div>
                            </div>
                        </div>
                    : 
                        null
                    }
                    <div className="flex flex-row space-x-5 items-center ">
                        { statsDaily.length > 0 ?
                            <div className="flex flex-col text-center">
                                <h1 className="text-xl font-semibold">Daily {getTitle(category)}</h1>
                                <div className="border-3px border-solid border-accent ">
                                    <AreaChart
                                        width={650}
                                        height={560}
                                        data={statsDaily}
                                        margin={{
                                            top: 10,
                                            right: 30,
                                            left: 0,
                                            bottom: 10,
                                        }}
                                    >
                                        <defs>
                                            <linearGradient id="gradient-chart" x1="0" y1="0" x2="0" y2="1">
                                            <stop offset="-0.54%" stopColor="rgba(254, 213, 118, 0.40)" stopOpacity={1}></stop>
                                            <stop offset="100%" stopColor="white" stopOpacity={1}></stop>
                                            </linearGradient>
                                        </defs>
                                        <CartesianGrid strokeDasharray="6 6" />
                                        <XAxis dataKey="hour"> 
                                            <Label value="Hours" offset={-5} position="insideBottom" />
                                        </XAxis>
                                        <YAxis> 
                                            <Label value={getTitle(category) + " (" + unit + ")"} angle={-90} offset={10} position="insideLeft" />
                                        </YAxis>
                                        <Tooltip formatter={(value) => `${value} ${unit}`} />
                                        <Area type="monotone" dataKey={getKeyName(statsDaily)} stroke="red" fill={`url(#gradient-chart)`} />
                                    </AreaChart>
                                </div>
                            </div>
                        :
                            null
                        }
                        { statsWeekly.length > 0 ?
                            <div className="flex flex-col text-center h-[10%]">
                                <h1 className="text-xl font-semibold">Weekly {getTitle(category)}</h1>
                                <BarChart
                                    width={650}
                                    height={560}
                                    data={statsWeekly}
                                    margin={{
                                        top: 5,
                                        right: 30,
                                        left: 20,
                                        bottom: 15,
                                    }}
                                >
                                    
                                    <CartesianGrid strokeDasharray="6 6" />
                                    <XAxis dataKey="day">
                                        <Label value="Days of the Week" offset={-10} position="insideBottom" />
                                    </XAxis>
                                    <YAxis> 
                                        <Label value={getTitle(category) + " (" + unit + ")"} angle={-90} offset={5} position="insideLeft" />
                                    </YAxis>
                                    <Tooltip formatter={(value) => `${value} ${unit}`}/>
                                    <Bar dataKey={getKeyName(statsWeekly)} barSize={20} radius={[5, 5, 0, 0]}>
                                        {statsWeekly.map((entry, index) => (
                                            <Cell key={`cell-${index}`} fill={barColors[index % barColors.length]} />
                                        ))}
                                    </Bar>
                                </BarChart>
                            </div>
                        :
                           null
                        }
                    </div>
                </div>
            </div>
        </div>
    )
}

export default StatisticsPage;