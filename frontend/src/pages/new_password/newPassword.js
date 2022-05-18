import { useNavigate, useParams } from 'react-router';
import { LOGIN } from '@constants';
import { validateInputs } from '@utils/validators';
import { useState, useRef } from 'react';
import { SERVER_NEW_PASS } from '@endpoints';
import { postData } from '@api/api';
import { Icon } from '@iconify/react';
import { msgResponse } from '@utils/functions';

const NewPassword = () => {
    let { token } = useParams()
    const navigate = useNavigate()
    const password = useRef("")
    const [passwordRepeat, setPasswordRepeat] = useState("")
    const [errorMsg, setErrorMsg] = useState("")
    const inputLabel = "input-label"
    const [passMatch, setPassMatch] = useState(true)
    const [seePass, setSeePass] = useState(false)
    const [seePassRec, setSeePassRec] = useState(false)
    
    const resetPass = async (event) => {
        event.preventDefault()
        const validResponse = validation()
        if(!validResponse.isSuccess) {
            setErrorMsg(validResponse.message)
            return
        }
        const data = await postData(SERVER_NEW_PASS, {
            token: token,
            password: password.current.value
        })
        if(data.isSuccess) {
            navigate(LOGIN)
        }
        else {
            setErrorMsg(data.message)
        }
    }

    const validation = () => {
        let validate = ""
        validate = validateInputs(password.current.value, "password")
        if(validate !== "") {
            return msgResponse(false, validate) 
        }
        if(password.current.value !== passwordRepeat) {
            return msgResponse(false, "Passwords not matching")
        }
        return msgResponse()
    }

    const setPassRepeat = (event) => {
        const newPass = event.target.value
        setPasswordRepeat(newPass)
        if(newPass !== password.current.value) {
            setPassMatch(false)
        }
        else {
            setPassMatch(true)
        }
    }

  return (
    <div className="flex-container">
        <div className="line"></div>
        <form className="form-container">
            <div className="login-title">RESET PASSWORD</div>
            <div className="error-msg">{errorMsg !== "" && errorMsg}</div>
            <label className={inputLabel}>
                Enter new Password:
                <div className="input-field-div">
                  <input
                  ref={password}
                  className="input-field-indiv"
                  type={seePass ? "text" : "password"} 
                  placeholder="********"/>
                  {seePass ? 
                  <Icon
                   icon="ant-design:eye-filled" 
                   style={{marginTop: "20px"}}
                   onClick={() => {setSeePass(false)}}
                  /> : 
                  <Icon
                  icon="ant-design:eye-invisible-filled"  
                   style={{marginTop: "20px"}}
                   onClick={() => {setSeePass(true)}}
                  />
                  }
                </div>
            </label>
            <label className={inputLabel}>
                Reenter Password:
                <div className={"input-field-div" + (passMatch ? "" : " pass-red")}>
                  <input
                  className={"input-field-indiv"}
                  type={seePassRec ? "text" : "password"} 
                  onChange={setPassRepeat}
                  placeholder="********"/>
                  {seePassRec ? 
                  <Icon
                   icon="ant-design:eye-filled" 
                   style={{marginTop: "20px"}}
                   onClick={() => {setSeePassRec(false)}}
                  /> : 
                  <Icon
                  icon="ant-design:eye-invisible-filled"  
                   style={{marginTop: "20px"}}
                   onClick={() => {setSeePassRec(true)}}
                  />
                  }
                </div>
            </label>
            <button className="button-login" type="submit" onClick={resetPass}>RESET</button>  
        </form>   
    </div>
  )
}

export default NewPassword