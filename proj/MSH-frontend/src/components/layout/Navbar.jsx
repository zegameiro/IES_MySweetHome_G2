import React from 'react';
import { Link } from 'react-router-dom';

import ProfileButton from './ProfileButton';
import SearchBar from './SearchBar';

import { FaHouse, FaLightbulb, FaHouseSignal, FaCirclePlus, FaGears, FaRightFromBracket } from "react-icons/fa6";
import { FaChartLine } from "react-icons/fa";
import '../../utils/index.css';

const navbar = ({ fixed }) => {
 return (
 <div className='flex flex-row space-x-1'>
   <div className={`flex flex-col justify-evenly gradient-blue rounded-3xl text-4xl text-white w-[5%] h-[80vh] ${ fixed ? 'fixed left-4' : 'p-4'}`}>
     <Link to='/dashboard' className='flex justify-center align-center'>
       <FaHouse />
     </Link>
     <Link to='/devices' className='flex justify-center align-center'>
       <FaLightbulb />
     </Link>
     <Link to='/rooms' className='flex justify-center align-center'>
       <FaHouseSignal />
     </Link>
     <Link to='/statistics' className='flex justify-center align-center'>
       <FaChartLine />
     </Link>
     <Link to='/add' className='flex justify-center align-center'>
       <FaCirclePlus />
     </Link>
     <Link to='/routines' className='flex justify-center align-center'>
       <FaGears />
     </Link>
     <Link to='/login' onClick={() => {
       localStorage.removeItem('user');
     }} className='flex justify-center align-center'>
       <FaRightFromBracket />
     </Link>
   </div>
   <div className='flex flex-row w-[90%] space-x-0 ml-[6%]'>
     <SearchBar />
     <ProfileButton />
   </div>
 </div>
 );
};

export default navbar;
