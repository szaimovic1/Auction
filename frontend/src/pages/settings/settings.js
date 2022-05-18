import AdditionalInfo from "@components/additional_info/additionalInfo";
import ProfileSelector from '@components/profile_selector/profileSelector';
import { SETTINGS_SELECTOR, LOGOUT, MSG_ERROR } from "@constants";
import { useState } from "react";
import './settings.scss';
import { deleteData } from "@api/api";
import { useNavigate } from "react-router";
import Message from '@components/message/message';
import { SERVER_USER } from "@endpoints";

const Settings = () => {
  const [deactivate, setDeactivate] = useState(false)
  const navigate = useNavigate()
  const [message, setMessage] = useState(false)

  const deactivation = async () => {
    const data = await deleteData(SERVER_USER)
    if(data.isSuccess) {
      navigate(LOGOUT)
    }
    else {
      setDeactivate(false)
      setMessage(data.message)
    }
  }

  return (
    <div>
      <AdditionalInfo 
        leftText={"Settings"} 
        rightText={{main: "My Account ->", page: "Settings"}} 
      />
      {message && <Message message={message} style={MSG_ERROR} />}
      <ProfileSelector page={SETTINGS_SELECTOR} />
      <div className="settings-container">
        <div className="settings-part-container">
          <div className="settings-title">Account</div>
          <div className="settings-question">Do you want to deactivate account?</div>
          <button 
            onClick={() => {setDeactivate(true)}}
            className="btn-settings"
            >DEACTIVATE
          </button>
        </div>
      </div>
      {deactivate && <div className="jump-up-container">
        <div className="jump-up-deactivate">
          <button 
            className="jump-up-exit"
            onClick={() => {setDeactivate(false)}}
            >x
          </button>
          <div className="settings-question">If you really want to deactivate</div>
          <button
            className="btn-settings" 
            onClick={() => {deactivation()}}
            >Click me!
          </button>
        </div>
        <div className="settings-overlay">
        </div>
      </div>}
    </div>
  )
}

export default Settings