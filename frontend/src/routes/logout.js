import { Context } from '../App.js';
import { useContext, useEffect } from 'react';
import { isAuthenticated } from '@utils/auth';
import { Navigate } from 'react-router-dom';

const Logout = () => {
    const setAuthenticated = useContext(Context);

    useEffect(() => {
        localStorage.clear()
        sessionStorage.clear()
        setAuthenticated(false)
    });

  return (
      <>
      {isAuthenticated() && <Navigate to="/" />}
      </>
  )
}

export default Logout
