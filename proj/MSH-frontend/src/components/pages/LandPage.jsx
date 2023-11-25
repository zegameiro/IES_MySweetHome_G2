import { useNavigate } from 'react-router-dom';
import logo from '/src/assets/icon/msh_logo.png';
import Footer from '../layout/Footer';

import { FaUser, FaHouse } from 'react-icons/fa6';
import NavbarSimple from '../layout/NavbarSimple';

const LandPage = () => {
  const navigate = useNavigate();
  return (
    <div>
      <div className={`hero min-h-screen bg-[url('/src/assets/images/smarthome_bg.jpg')]`}>
        <div className="hero-overlay bg-[#1e1e1e] bg-opacity-40 "></div>
        <div className="hero-content text-center text-neutral-content">
          <NavbarSimple fixed />
          <div className="max-w-xl">
            <h1 className="mb-5 text-7xl font-bold">Welcome to innovation</h1>
            <p className="mb-5 text-2xl">
              Are you ready to start taking control of your home?
              <br />
              My Sweet Home empowers you to command your smart devices
              seamlessly.
            </p>
            <button
              className="btn btn-primary text-lg"
              onClick={() => navigate('/login')}
            >
              Get Started
            </button>
          </div>
        </div>
      </div>

      <div className="flex justify-center m-4 mt-8">
        <h1 className="text-5xl font-bold text-accent">
          Join an evergrowing IoT hub!
        </h1>
      </div>
      <div className="flex justify-center m-4 my-8 ">
        <div className="flex justify-around join border-2 rounded rounded-2xl w-2/3">
          <div className="join-item p-4 w-1/4">
            <h1 className="text-success font-semibold text-4xl">Users</h1>
            <div className="flex justify-between p-2 items-center">
              <h1 className="font-black text-7xl">31k</h1>
              <FaUser className="text-6xl text-success" />
            </div>
            <h3 className="text-slate-500 font-light text-2xl">
              Registered users
            </h3>
          </div>
          <div className="join-item p-4 w-1/4">
            <h1 className="text-warning font-semibold text-4xl">New Users</h1>
            <div className="flex justify-between p-2 items-center">
              <h1 className="font-black text-7xl">4,200</h1>
              <FaUser className="text-6xl text-warning" />
            </div>
            <h3 className="text-slate-500 font-light text-2xl">Last month</h3>
          </div>
          <div className="join-item p-4 w-1/4">
            <h1 className="text-error font-semibold text-4xl">Houses</h1>
            <div className="flex justify-between p-2 items-center">
              <h1 className="font-black text-7xl">500+</h1>
              <FaHouse className="text-6xl text-error" />
            </div>
            <h3 className="text-slate-500 font-light text-2xl">
              Using our services
            </h3>
          </div>
        </div>
      </div>

      <div className=" flex flex-col justify-center m-2 text-xl items-center">
        <h1 className="text-3xl font-bold text-accent my-8">
          Start your smart home journey today!
        </h1>
        <div className="my-[5%] flex  justify-center">
          <h1 className="text-2xl text-slate-500 font-light max-w-lg">
            Sign in and start taking control of your smart devices today!
          </h1>
          <button
            className="btn btn-secondary text-lg"
            onClick={() => navigate('/login')}
          >
            Sign in
          </button>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default LandPage
