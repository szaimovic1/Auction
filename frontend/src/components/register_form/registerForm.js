import { useRef, useState } from 'react';
import { validateInputs } from '@utils/validators';
import { postData } from '@api/api';
import '../login_form/loginForm.scss';
import SocialLogin from '../social_login/socialLogin';
import { SERVER_REGISTRATION } from '@endpoints';
import { useNavigate } from 'react-router-dom';
import { LOGIN } from '@constants';
import { msgResponse } from '@utils/functions';

const RegisterForm = () => {
    const firstName = useRef("")
    const lastName = useRef("")
    const email = useRef("")
    const password = useRef("")
    const [errorMsg, setErrorMsg] = useState("")
    const inputLabel = "input-label"
    const navigate = useNavigate()

    const register = async (event) => {
        event.preventDefault()
        const validResponse = validation()
        if(!validResponse.isSuccess) {
            setErrorMsg(validResponse.message)
            return
        }
        const data = {
            firstName: firstName.current.value,
            lastName: lastName.current.value,
            email: email.current.value,
            password: password.current.value
        }
        const response = await postData(SERVER_REGISTRATION, data)
        if(response.isSuccess) {
            document.getElementById("registerform").reset()
            setErrorMsg(response.message)
        }
        else {
            setErrorMsg(response.message)
        }
    }

    const validation = () => {
        let validate = ""
        validate = validateInputs(firstName.current.value, "name")
        if(validate !== "") {
            return msgResponse(false, validate) 
        }
        validate = validateInputs(lastName.current.value, "name")
        if(validate !== "") {
            return msgResponse(false, validate) 
        }
        validate = validateInputs(email.current.value, "email")
        if(validate !== "") {
            return msgResponse(false, validate) 
        }
        validate = validateInputs(password.current.value, "password")
        if(validate !== "") {
            return msgResponse(false, validate) 
        }
        return msgResponse()
    }
    
  return (
    <div className="flex-container">
        <div className="line"></div>
        <form className="form-container" id="registerform">
            <div className="login-title">REGISTER</div>
            <div className="error-msg">{errorMsg !== "" && errorMsg}</div>

            <label className={inputLabel}>
                First Name:
                <input className="input-field" 
                type="text" 
                ref={firstName}
                placeholder="John"/>
            </label>

            <label className={inputLabel}>
                Last Name:
                <input className="input-field"
                type="text" 
                ref={lastName}
                placeholder="Doe"/>
            </label>

            <label className={inputLabel}>
                Email:
                <input className="input-field" 
                type="text" 
                ref={email}
                placeholder="user@domain.com"/>
            </label>

            <label className={inputLabel}>
                Password:
                <input className="input-field"
                type="password" 
                ref={password}
                placeholder="********"/>
            </label>

            <button className="button-login" type="submit" onClick={register}>REGISTER</button> 
            <SocialLogin type="Register" />
            <div className="goto-login">
                <p className="basic-text">Already have an account?</p>
                <button className="password" onClick={(e) => {
                    e.preventDefault()
                    navigate(LOGIN)
                }}>Login</button>  
            </div>
        </form>   
    </div>   
  )
}

export default RegisterForm