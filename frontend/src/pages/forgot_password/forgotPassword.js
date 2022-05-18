import { validateInputs } from '@utils/validators';
import { useRef, useState } from 'react';
import { SERVER_FORGOT } from '@endpoints';
import { postData } from '@api/api';
import { msgResponse } from '@utils/functions';

const ForgotPassword = () => {
  const userName = useRef("")
  const [errorMsg, setErrorMsg] = useState("")
  const inputLabel = "input-label"

  const forgotPass = async (event) => {
    event.preventDefault()
    const validResponse = validation()
    if(!validResponse.isSuccess) {
        setErrorMsg(validResponse.message)
        return
    }
    const data = await postData(SERVER_FORGOT, {}, {email: userName.current.value})
    setErrorMsg(data.message)
  }

  const validation = () => {
    let validate = ""
    validate = validateInputs(userName.current.value, "email")
    if(validate !== "") {
      return msgResponse(false, validate) 
    }
    return msgResponse()
  }

  return (
    <div className="flex-container">
        <div className="line"></div>
        <form className="form-container">
            <div className="login-title">FORGOT PASSWORD</div>
            <div className="error-msg">{errorMsg !== "" && errorMsg}</div>
            <div 
              className="set-prices-explanation"
              style={{textAlign: "left"}}
            >
              <div>
                Lost your password? Please enter your username or email address.
                You will receive a link to create a new
              </div>
              <div>
                password via email.
              </div>
            </div>
            <label className={inputLabel}>
                Enter Email:
                <input className="input-field" 
                type="text" 
                ref={userName}
                placeholder="user@domain.com"/>
            </label>
            <button 
              className="button-login" 
              type="submit" 
              onClick={forgotPass}>
                RESET PASSWORD
            </button>
        </form>   
    </div>
  )
}

export default ForgotPassword