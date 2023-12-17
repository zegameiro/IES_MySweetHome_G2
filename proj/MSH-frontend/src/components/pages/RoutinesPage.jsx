import React from "react";

import Header from "../layout/Header";
import Navbar from "../layout/Navbar";
import RoutineCard from "../layout/RoutinesCard";


const RoutinesPage = () => {
    return (
        <div className="mx-[5%] mt-4 flex justify-between">
            <Navbar />
            <div className="flex flex-col w-full h-full">
                <Header />
                <h1>Routines Page</h1>
                <RoutineCard />
            </div>
        </div>
    )
};

export default RoutinesPage;