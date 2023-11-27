import React from 'react';
import SearchBar from './SearchBar';
import ProfileButton from './ProfileButton';

const Header = () => {
  return (
    <div className="flex flex-row items-center justify-between m-2 ">
      <SearchBar />
      <ProfileButton />
    </div>
  );
};

export default Header;
