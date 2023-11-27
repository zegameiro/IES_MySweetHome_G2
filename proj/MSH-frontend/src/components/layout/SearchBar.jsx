import React from "react";
import "../../utils/index.css";

const SearchBar = () => {
   return (
       <div className="flex flex-row justify-center align-center w-full">
           <input
               className="w-[80%] h-[5vh] p-2 text-3xl rounded-l-3xl border-2 border-primary"
               type="text"
               placeholder="Search..."
           />
           <button className="w-[10vh] h-[5vh] gradient-blue text-white text-lg rounded-r-3xl">
               SEARCH
           </button>
       </div>
   );
}

export default SearchBar;