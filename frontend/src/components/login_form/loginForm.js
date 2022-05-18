import { TYPE, FORGOT_PASSWORD } from '@constants';
import { useState, useContext, useEffect, useRef } from 'react';
import { validateInputs } from '@utils/validators';
import { postData } from '@api/api';
import './loginForm.scss';
import CheckBox from '@components/check_box/checkBox';
import SocialLogin from '../social_login/socialLogin';
import { isAuthenticated, saveUser } from '@utils/auth';
import { Navigate, useNavigate } from 'react-router-dom';
import { Context } from '../../App';
import { SERVER_LOGIN } from '@endpoints';
import { msgResponse } from '@utils/functions';

const LoginForm = () => {
    const [isChecked, setIsChecked] = useState(true)
    const userName = useRef("")
    const password = useRef("")
    const [errorMsg, setErrorMsg] = useState("")
    const [isAuth, setIsAuth] = useState(isAuthenticated())
    const inputLabel = "input-label"
    const setAuthenticated = useContext(Context);
    const navigate = useNavigate()

    useEffect(() => {
        localStorage.removeItem(TYPE)
    }, []);

    const remember = () => {
        setIsChecked(!isChecked)
        if(!isChecked)
            localStorage.setItem(TYPE, "local")
        else localStorage.setItem(TYPE, "session")
    }

    const logIn = async (event) => {
        event.preventDefault()
        const validResponse = validation()
        if(!validResponse.isSuccess) {
            setErrorMsg(validResponse.message)
            return
        }
        const response = await fillStorage()
        if(response.isSuccess) {
            setIsAuth(true)
            setAuthenticated(true)
        }
        else {
            setErrorMsg(response.message)
        }
    }

    const validation = () => {
        let validate = ""
        validate = validateInputs(userName.current.value, "email")
        if(validate !== "") {
            return msgResponse(false, validate) 
        }
        validate = validateInputs(password.current.value, "password")
        if(validate !== "") {
            return msgResponse(false, validate)
        }
        return msgResponse()
    }

    const fillStorage = async () => {
        const body = {
            email: userName.current.value,
            password: password.current.value
        }
        const response = await postData(SERVER_LOGIN, body)
        if(response.isSuccess) {
            saveUser(response.jwt, response.fullName)
        }
        return response
    }
    
  return (
    <>
    {isAuth ? <Navigate to="/" /> :
    <div className="flex-container">
        <div className="line"></div>
        <form className="form-container">
            <div className="login-title">LOGIN</div>
            <div className="error-msg">{errorMsg !== "" && errorMsg}</div>

            <label className={inputLabel}>
                Email:
                <input className="input-field" 
                type="text" 
                ref={userName}
                placeholder="user@domain.com"/>
            </label>

            <label className={inputLabel}>
                Password:
                <input className="input-field"
                type="password" 
                ref={password}
                placeholder="********"/>
            </label>

            <CheckBox clicked={remember} showing={isChecked} />
            <button className="button-login" type="submit" onClick={logIn}>LOGIN</button>
            <SocialLogin type="Login" />
            <button className="password" onClick={(e) => {
                e.preventDefault()
                navigate(FORGOT_PASSWORD)
            }}>Forgot password?</button>  
        </form>   
    </div>}</>)
}

export default LoginForm