import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../../utils/index.css';

// eslint-disable-next-line react/prop-types
const SearchBar = () => {
  const [search, setSearch] = useState('');
  const navigate = useNavigate();


  return (
    <div
      className={`flex flex-row justify-start w-[75%] mx-2 align-center join`}
    >
      <input
        className="w-full h-16 p-2 text-2xl px-4 rounded-3xl border-2 border-primary join-item"
        type="text"
        placeholder="Search..."
        value={search}
        onChange={(e) => setSearch(e.target.value)}
      />
      <button className="w-full max-w-[10%] h-16 gradient-blue text-white text-xl font-bold rounded-3xl join-item" 
      onClick={() => {
        navigate(`/devices?search=${search}`);
      }}
      >
        SEARCH
      </button>
    </div>
  );
};

export default SearchBar;
