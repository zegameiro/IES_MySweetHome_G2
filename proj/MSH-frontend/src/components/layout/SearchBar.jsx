import React from "react";
import "../../utils/index.css";

const SearchBar = () => {
    return (
        <div className="flex flex-row justify-center align-center">
            <input
                className="w-full lg:w-[60vh] h-[50px] p-2 text-2xl rounded-l-xl border-2 border-primary"
                type="text"
                placeholder="Search..."
            />
            <button
                className="w-full lg:w-[100px] h-[50px] gradient-blue text-white text-lg rounded-r-xl"
            >
                SEARCH
            </button>
        </div>
    );
}

export default SearchBar;