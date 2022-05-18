import './searchBar.scss';
import { MDBIcon } from "mdbreact";
import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router';
import { SHOP } from '@constants';

const SearchBar = () => {
  const[offers, setOffers] = useState()
  const navigate = useNavigate()
  const location = useLocation().pathname

  const searchInput =  async (event) => {
    setOffers(event.target.value)
  }

  const searchProduct = (reset = false) => {
      navigate("/shop", {state: {id: null, name: reset === false ? offers : ""}})
  }

  const deleteSearch = () => {
    setOffers("");
    document.getElementById("search-input").value = "";
    if([SHOP].indexOf(location) > -1) {
      searchProduct(true);
    }
  }

  return (
    <div className={"search-bar"}>
      <input className="input-search"
          type="text" 
          placeholder="Try enter: Shoes" 
          aria-label="Search" 
          id={"search-input"}
          onChange={searchInput}
          onKeyPress={(e) => {if(e.key === 'Enter') {searchProduct()}}} />
      <button id={"clearSearch"} onClick={() => {deleteSearch()}}
          className={offers ? "tof-x" : "tof-x-hide"}>x</button>
      <MDBIcon onClick={() => {searchProduct()}} className="search-icon" icon="search" />
    </div>
  );
}

export default SearchBar