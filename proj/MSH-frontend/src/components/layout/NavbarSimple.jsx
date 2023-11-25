/* eslint-disable react/prop-types */
import { useNavigate } from 'react-router-dom';
import logo from '/src/assets/icon/msh_logo.png';

const NavbarSimple = ({ fixed }) => {
  const navigate = useNavigate();
  return (
    <div className={`flex justify-between w-full ${ fixed ? 'fixed top-0 p-4' : 'p-4'}`}>
      <span className="flex justify-center bg-white bg-opacity-75 rounded-full h-32 w-32 items-center">
        <img
          className="h-28 cursor-pointer"
          src={logo}
          alt="logo"
          onClick={() => navigate('/')}
        />
      </span>
      <span>
        <button
          className="btn gradient-blue text-white border-none text-lg"
          onClick={() => navigate('/login')}
        >
          Sign in
        </button>
      </span>
    </div>
  );
};

export default NavbarSimple;
