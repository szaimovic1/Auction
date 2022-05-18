import AdditionalInfo from "@components/additional_info/additionalInfo";
import ProfileSelector from '@components/profile_selector/profileSelector';
import { ADMIN_SELECTOR } from "@constants";
import { useState, useRef } from "react";
import Analytics from "./components/analytics/analytics";
import Users from "./components/users/users";
import Message from '@components/message/message';

const Admin = () => {
  const activeSoldBtns = "active-sold-btns"
  const buttonSellerActive = "button-seller-active"
  const buttonSellerNormal = "button-seller-normal"
  const usersId = "users"
  const analyticsId = "analytics"
  const [activeBtn, setActiveBtn] = useState(usersId)
  const [message, setMessage] = useState(null)
  const style = useRef()

  return (
    <div>
      <AdditionalInfo 
        leftText={"Admin"} 
        rightText={{
          main: "My Account ->",
          page: "Admin"
        }} 
      />
      {message && <Message message={message} style={style.current} />}
      <ProfileSelector page={ADMIN_SELECTOR} />
      <div className="seller-container">
        <div className={activeSoldBtns}>
          <button onClick={
            () => {
              if(activeBtn !== usersId){
                setActiveBtn(usersId)
            }}}
            className={activeBtn === usersId ? buttonSellerActive
               : buttonSellerNormal
          }>Users</button>
          <button onClick={
            () => {
              if(activeBtn !== analyticsId){
                setActiveBtn(analyticsId)
            }}}
            className={activeBtn === analyticsId ? buttonSellerActive
               : buttonSellerNormal}
            >Analytics</button>
        </div>
        {activeBtn === usersId &&
          <Users setMessage={setMessage} style={style} />
        }
        {activeBtn === analyticsId &&
          <Analytics />
        }
      </div>
    </div>
  )
}

export default Admin
