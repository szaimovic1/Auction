import { Navigate } from 'react-router-dom';
import { isAuthenticated, saveUser } from '@utils/auth';
import { Context } from '../App.js';
import { useContext, useEffect } from 'react';
import { useLocation } from "react-router-dom";
import { getUrlParameter } from '@utils/functions.js';

const OAuth2Login = () => {
    const setAuthenticated = useContext(Context)
    const location = useLocation()
  
    saveUser(getUrlParameter("token", location),  getUrlParameter("name", location))
    
    useEffect(() => {
      setAuthenticated(true)
    })

  return(    
      <Navigate to={isAuthenticated() ? "/" : "/login"} />
  );
}

export default OAuth2Login;

