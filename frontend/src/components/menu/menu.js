import './menu.scss'
import { NavLink } from "react-router-dom";
import { ALL } from '@constants';
import { useNavigate } from "react-router";
import { PROFILE, SELLER, BIDS, SETTINGS, WISHLIST } from "@constants";

const Menu = ({ activeTab }) => {
  const navigate = useNavigate()
  const btnDropdownMa = "btn-dropdown-ma"

  return (
    <div className="menu">
      <NavLink className={activeTab === "home" ? "home active" : "home"} to="/">HOME</NavLink>
      <NavLink className={activeTab === "shop" ? "shop active" : "shop"} to="/shop" state={ALL}>SHOP</NavLink>
      <div className="div-hover" >
        <button className={activeTab === "profile" ? "profile-button active" : "profile-button"} 
          onClick={() => { navigate(PROFILE) }}>
          MY ACCOUNT
        </button>
        <div className="dropdown-my-account-invisible">
          <div className="dropdown-my-account">
            <button className={btnDropdownMa}
              onClick={() => { navigate(PROFILE) }}>
              Profile
            </button>
            <button className={btnDropdownMa}
              onClick={() => { navigate(SELLER) }}>
              Become Seller
            </button>
            <button className={btnDropdownMa}
              onClick={() => { navigate(BIDS) }}>
              Your Bids
            </button>
            <button className={btnDropdownMa}
              onClick={() => { navigate(WISHLIST) }}>
              Wishlist
            </button>
            <button className={btnDropdownMa}
              onClick={() => { navigate(SETTINGS) }}>
              Settings
            </button>
          </div>
        </div>
      </div>
      
  </div>
  )
}

export default Menu
