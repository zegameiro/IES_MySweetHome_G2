import React from 'react';
import { FaTemperatureHalf } from "react-icons/fa6";
import { IoIosCloudy } from "react-icons/io";

const Carroussel = () => {
    const user = JSON.parse(localStorage.getItem('user'))
        ? JSON.parse(localStorage.getItem('user'))
        : null;


  return (
    <div className="carousel w-full h-[30vh] rounded-3xl">
      <div
        id="slide1"
        className="carousel-item relative w-[100%]"
      >
        <div className="absolute flex justify-between transform -translate-y-1/2 left-5 right-5 top-1/2">
          <a
            href="#slide3"
            className="btn btn-outline btn-circle btn-secondary"
          >
            ❮
          </a>
          <a
            href="#slide2"
            className="btn btn-outline btn-circle btn-secondary"
          >
            ❯
          </a>
        </div>
        <div
          className={`hero rounded-3xl w-[100%] h-[100%] bg-[url('/src/assets/images/home1.jpg')]`}
        >
          <div className="hero-overlay rounded-3xl w-[100%] h-[100%] bg-[#101010] bg-opacity-50 "></div>
          <div className="text-center text-white hero-content">
            <div className="max-w-xl">
              <h2 className="mb-5 text-5xl font-bold">
                Welcome Back {user.firstname}!
              </h2>
              <p className="mb-5 text-2xl">
                Welcome home! We hope you're having a great day.
              </p>
            </div>
          </div>
        </div>
      </div>
      <div
        id="slide2"
        className="carousel-item relative w-[100%]"
      >
        <div className="absolute flex justify-between transform -translate-y-1/2 left-5 right-5 top-1/2">
          <a
            href="#slide1"
            className="btn btn-outline btn-circle btn-secondary"
          >
            ❮
          </a>
          <a
            href="#slide3"
            className="btn btn-outline btn-circle btn-secondary"
          >
            ❯
          </a>
        </div>
        <div
          className={`hero rounded-3xl bg-[url('/src/assets/images/home2.jpg')]`}
        >
          <div className="hero-overlay rounded-3xl bg-[#101010] bg-opacity-50 "></div>
          <div className="text-center text-white hero-content">
            <div className="max-w-xl">
              <h1 className="mb-5 text-5xl font-bold">Today's Weather</h1>
              <div className="flex flex-row items-center">
                <FaTemperatureHalf className="text-5xl" />{' '}
                <p className="text-2xl">
                  Enjoy a comfortable home at a cozy 25°C.
                </p>
              </div>
              <div className="flex flex-row items-center">
                <IoIosCloudy className="text-5xl" />{' '}
                <p className="text-2xl">It's a cloudy day today.</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div
        id="slide3"
        className="carousel-item relative w-[100%]"
      >
        <div className="absolute flex justify-between transform -translate-y-1/2 left-5 right-5 top-1/2">
          <a
            href="#slide2"
            className="btn btn-outline btn-circle btn-secondary"
          >
            ❮
          </a>
          <a
            href="#slide1"
            className="btn btn-outline btn-circle btn-secondary"
          >
            ❯
          </a>
        </div>
        <div
          className={`hero rounded-3xl bg-[url('/src/assets/images/home3.jpg')]`}
        >
          <div className="hero-overlay rounded-3xl bg-[#101010] bg-opacity-50 "></div>
          <div className="text-center text-white hero-content">
            <div className="max-w-xl">
              <h1 className="mb-5 text-5xl font-bold">Missing Something</h1>
              <p className="mb-5 text-2xl">
                Help us to know what it is, create you're own routines that make
                you feel more confortable at you're home
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Carroussel;
