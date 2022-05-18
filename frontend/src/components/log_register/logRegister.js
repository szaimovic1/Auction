import { Link } from "react-router-dom";
import './logRegister.scss';
import { NAME } from '@constants'
import React, { useState } from 'react';

const LogRegister = () => {
  const [logOut, setLogOut] = useState(false)
  const name = localStorage.getItem(NAME) === null ? 
    sessionStorage.getItem(NAME) : localStorage.getItem(NAME)

  const showLogout = () => {
    setLogOut(!logOut)
  }

  return (
    <div className="blackheader-navbar">
      {name === null ?
      <>
        <Link className="login" to="/login">Login</Link>
        <p className="or">or</p>
        <Link className="create-account" to="/register">Create an account</Link>
      </> : 
      <>
        <button className="user" onClick={showLogout}>Hi, {name}</button>
        {logOut && <Link className="or" to="/logout" onClick={showLogout}>Log out</Link>}
      </>  
      }
    </div> 
  )
}

export default LogRegister
