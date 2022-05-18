import { Icon } from "@iconify/react";
import { PROFILE_SELECTOR, SELLER_SELECTOR, BIDS_SELECTOR, WISHLIST_SELECTOR, SETTINGS_SELECTOR,
  PROFILE, SELLER, BIDS, SETTINGS, ADD_ITEM, WISHLIST, ADMIN, ADMIN_SELECTOR } from "@constants";
import './profileSelector.scss';
import { useNavigate } from "react-router";
import { isAdmin } from "@utils/auth";
import { useState } from "react";

const ProfileSelector = ({ page }) => {
    const navigate = useNavigate()
    const iconSelector = "icon-selector"
    const buttonProfileActive = "button-profile-active"
    const buttonProfileNormal = "button-profile-normal"
    const [ifAdmin, ] = useState(isAdmin())

  return (
    <div className="profile-selector-container">
      <div className="profile-buttons-container">
        <button className={page === PROFILE_SELECTOR ?
                buttonProfileActive :
                buttonProfileNormal}
            onClick={() => { navigate(PROFILE) }}>
          <Icon 
            icon="bxs:user"
            width="27"
            height="25"
            className={iconSelector}
          />  
          Profile
        </button>
        <button className={page === SELLER_SELECTOR ?
                buttonProfileActive :
                buttonProfileNormal}
            onClick={() => { navigate(SELLER) }}>
          <Icon 
            icon="ant-design:unordered-list-outlined" 
            width="30"
            height="17"
            className={iconSelector}
          />  
          Seller
        </button>
        <button className={page === BIDS_SELECTOR ?
                buttonProfileActive :
                buttonProfileNormal}
            onClick={() => { navigate(BIDS) }}>
          <Icon 
            icon="ic:outline-monetization-on" 
            width="25"
            height="25"
            className={iconSelector}
          />  
          Bids
        </button>
        <button className={page === WISHLIST_SELECTOR ?
                buttonProfileActive :
                buttonProfileNormal}
            onClick={() => { navigate(WISHLIST) }}>
          <Icon 
            icon="ant-design:heart-outlined" 
            width="25"
            height="25"
            className={iconSelector}
          />  
          Wishlist
        </button>
        <button className={page === SETTINGS_SELECTOR ?
                buttonProfileActive :
                buttonProfileNormal}
            onClick={() => { navigate(SETTINGS) }}>
          <Icon 
            icon="clarity:settings-line" 
            width="25"
            height="25"
            className={iconSelector}
          />  
          Settings
        </button>
        {ifAdmin &&
         <button className={page === ADMIN_SELECTOR ?
                buttonProfileActive :
                buttonProfileNormal}
              onClick={() => { navigate(ADMIN) }}>
            <Icon 
              icon="ri:admin-line" 
              width="25"
              height="25"
              className={iconSelector}
            />
            Admin
        </button>}
      </div>
      <button className={"button-profile-normal-active"}
          onClick={() => { navigate(ADD_ITEM) }}>
        <Icon 
          icon="ant-design:plus-outlined" 
          width="25"
          height="25"
          className={iconSelector}
        />  
        ADD ITEM
      </button>
    </div>
  )
}

export default ProfileSelector
