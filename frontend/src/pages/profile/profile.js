import { putData, getData } from '@api/api';
import { useState, useRef, useEffect } from 'react';
import AdditionalInfo from "@components/additional_info/additionalInfo";
import ProfileSelector from '@components/profile_selector/profileSelector';
import { PROFILE_SELECTOR, LOGOUT, MSG_ERROR, MSG_WIN } from "@constants";
import { getEmail } from '@utils/auth';
import './profile.scss';
import { useNavigate } from 'react-router';
import { Icon } from '@iconify/react';
import Message from '@components/message/message';
import { SERVER_USER, SERVER_PROFILE_PHOTO, SERVER_UPDATE_USER } from '@endpoints';
import { correctDate } from "@utils/functions";

const Profile = () => {
  const navigate = useNavigate()
  const [image, setImage] = useState('')
  const [loading, setLoading] = useState(false)
  const hiddenFileInput = useRef(null);
  const [customerInfo, setCustomerInfo] = useState()
  const days = [...Array(32).keys()].slice(1)
  const months = [...Array(13).keys()].slice(1)
  const years = ([...Array((new Date().getFullYear()) - 17).keys()].slice(1922)).reverse();
  const yearsYY = ([...Array(parseInt((new Date().getFullYear()).toString().slice(-2)) + 5).keys()]
      .slice(parseInt((new Date().getFullYear()).toString().slice(-2)))).reverse();
  const [showDD, setShowDD] = useState(false)
  const [showMM, setShowMM] = useState(false)
  const [showYYYY, setShowYYYY] = useState(false)
  const firstName = useRef()
  const lastName = useRef()
  const email = useRef()
  const [day, setDay] = useState()
  const [month, setMonth] = useState()
  const [year, setYear] = useState()
  const phone = useRef()
  const piTitle = "pi-title"
  const personalInfoContainer = "personal-info-container"
  const piInfoDiv = "pi-info-div"
  const piLabel = "pi-label"
  const piInputs = "pi-inputs"
  const dateBirthContainer = "date-birth-container"
  const dayBirthDiv = "day-birth-div"
  const dayBirthBtn = "day-birth-btn"
  const dayBirthDropdown = "day-birth-dropdown"
  const piBtnDropdown = "pi-btn-dropdown"
  const piTitleBtn = "pi-title-btn"
  const [cardInfo, setCardInfo] = useState(false)
  const [addressInfo, setAddressInfo] = useState(false)
  const cardName = useRef()
  const cardNumber = useRef()
  const [monthCard, setMonthCard] = useState()
  const [yearCard, setYearCard] = useState()
  const [showMMCard, setShowMMCard] = useState(false)
  const [showYYYYCard, setShowYYYYCard] = useState(false)
  const cVCCVV = useRef()
  const street = useRef()
  const city = useRef()
  const zip = useRef()
  const state = useRef()
  const country = useRef()
  const [message, setMessage] = useState("")
  const style = useRef()
  const [rerender, setRerender] = useState(false)

  const uploadImage = async e => {
    const data = new FormData()
    data.append('file', e.target.files[0]);
    setLoading(true)
    const res = await putData(SERVER_PROFILE_PHOTO, data)
    if(res.logout) {
      navigate(LOGOUT)
    }
    else if(res.isSuccess) {
      setImage(res.message)
      setLoading(false)
    }
    else {
      setErrorMsg(res.message)
    }
  }

  useEffect(() => {
    async function fetchData() {
      const data = await getData(SERVER_USER)
      if(data.logout) {
        navigate(LOGOUT)
      }
      else if(data.isSuccess) {
        setCustomerInfo(data)
        setImage(data.user.profilePicture)  
      }
      else {
        setErrorMsg(data.message)
      }    
    }
    fetchData()
  }, [navigate, rerender])

  const addZero = (num) => {
    return num < 10 ? "0" + num : num
  }

  const updateUserInfo = async () => {
    let date 
    if(year || month || day) {
      if(customerInfo.dateOfBirth === null) {
        if(!year || !month || !day) {
          setErrorMsg("Missing date infomation!")
          return
        }
      }
      const oldDate = new Date(customerInfo.dateOfBirth)
      date = new Date(year ? year : oldDate.getFullYear(),
        month ? month-1 : oldDate.getMonth(),
        day ? day : oldDate.getDate())
      date = correctDate(date)
    }
    const data = {
      name: (firstName.current.value === "" ? null : firstName.current.value),
      surname: (lastName.current.value === "" ? null : lastName.current.value),
      email: (email.current.value === "" ? null : email.current.value),
      phone: (phone.current.value === "" ? null : phone.current.value),
      dateOfBirth: date ? date.toISOString() : date,
      nameCard: (cardName.current.value === "" ? null : cardName.current.value),
      numberCard: (cardNumber.current.value === "" ? null : cardNumber.current.value),
      monthExpCard: monthCard,
      yearExpCard: yearCard,
      cvcCvv: (cVCCVV.current.value === "" ? null : cVCCVV.current.value),
      street: (street.current.value === "" ? null : street.current.value),
      city: (city.current.value === "" ? null : city.current.value),
      zipCode: (zip.current.value === "" ? null : zip.current.value),  
      state: (state.current.value === "" ? null : state.current.value),
      country: (country.current.value === "" ? null : country.current.value)
    }
    const result = await putData(SERVER_UPDATE_USER, data)
    if(result.logout) {
      navigate(LOGOUT)
      return
    }
    if(result.isSuccess) {
      style.current = MSG_WIN
      setRerender(!rerender)
      reset()
    }
    else {
      style.current = MSG_ERROR
    }
    setMessage(result.message)
    window.scrollTo(0, 0)
  }

  const setErrorMsg = (msg) => {
    style.current = MSG_ERROR
    setMessage(msg)
    window.scrollTo(0, 0)
  }

  const reset = () => {
    firstName.current.value = ""
    lastName.current.value = ""
    email.current.value = ""
    phone.current.value = ""
    cardName.current.value = ""
    cardNumber.current.value = ""
    cVCCVV.current.value = ""
    street.current.value = ""
    city.current.value = ""
    zip.current.value = ""
    state.current.value = ""
    country.current.value = ""
    setYear(null)
    setDay(null)
    setMonth(null)
    setMonthCard(null)
    setYearCard(null)
  }

  return (
    <div>
      <AdditionalInfo 
        leftText={"Profile"} 
        rightText={{main: "My Account ->", page: "Profile"}} 
      />
      {message && <Message message={message} style={style.current} />}
      <ProfileSelector page={PROFILE_SELECTOR} />
      {customerInfo && <div  className="profile-container">
        <div className={piTitle}>Personal information</div>
        <div className={personalInfoContainer}>
          <div className="profile-img-btn">
            <div className="profile-img-button-align">
            {loading ? (
              <h3>Loading...</h3>
            ) : (
              <div className="profile-img-div">
                <img src={image} className="profile-img" alt="no file"/>
              </div>
            )}
            <input
              type="file"
              name="file"
              placeholder="Upload an image"
              onChange={uploadImage}
              ref={hiddenFileInput}
              style={{display: 'none'}}
            />
            <button className="load-img-button" onClick={() => {
              hiddenFileInput.current.click()
            }}>Change photo</button>
            </div>
          </div>
          <div className={piInfoDiv}>
            <label className={piLabel}>First Name</label>
            <input className={piInputs}
                id={"firstName"} 
                type="text"
                ref={firstName}
                placeholder={customerInfo.user.firstName} />
            <label className={piLabel}>Last Name</label>
            <input className={piInputs} 
                id={"lastName"} 
                type="text"
                ref={lastName}
                placeholder={customerInfo.user.lastName} />
            <label className={piLabel}>Email Address</label>
            <input className={piInputs + " pi-dark-background"} 
                id={"email"} 
                type="text"
                ref={email}
                placeholder={getEmail()} />
            <label className={piLabel}>Date of Birth</label>
            <div className={dateBirthContainer}>
              <div className={dayBirthDiv}>
                <button className={dayBirthBtn}
                  onClick={() => {setShowDD(!showDD)}}>  
                  {day ? addZero(day) : (customerInfo.dateOfBirth ? addZero(new Date(customerInfo.dateOfBirth).getDate()) : "DD")}
                  <Icon style={{float: "right"}}
                      width="30"
                      height="24" 
                      icon="ri:arrow-drop-down-line" />
                </button>
                {showDD && <div className={dayBirthDropdown}>{
                  days.map((value, index) => {
                    return(
                      <button className={piBtnDropdown} 
                        onClick={() => {setDay(value); setShowDD(false);}}
                        key={index}>{addZero(value)}</button>
                    )
                })}</div>}
              </div>
              <div className={dayBirthDiv + " db-middle"}>
                <button className={dayBirthBtn}
                  onClick={() => {setShowMM(!showMM)}}>
                  {month ? addZero(month) : (customerInfo.dateOfBirth ? addZero(new Date(customerInfo.dateOfBirth).getMonth()+1) : "MM")}
                  <Icon style={{float: "right"}}
                      width="30"
                      height="24" 
                      icon="ri:arrow-drop-down-line" />
                </button>
                {showMM && <div className={dayBirthDropdown}>{
                  months.map((value, index) => {
                    return(
                      <button className={piBtnDropdown} 
                        onClick={() => {setMonth(value); setShowMM(false);}}
                        key={index + 32}>{addZero(value)}</button>
                    )
                })}</div>}
              </div>
              <div className={dayBirthDiv}>
                <button className={dayBirthBtn}
                  onClick={() => {setShowYYYY(!showYYYY)}}>
                  {year ? year : (customerInfo.dateOfBirth ? new Date(customerInfo.dateOfBirth).getFullYear() : "YYYY")}
                  <Icon style={{float: "right"}}
                      width="30"
                      height="24" 
                      icon="ri:arrow-drop-down-line" />
                </button>
                {showYYYY && <div className={dayBirthDropdown}>{
                  years.map((value, index) => {
                    return(
                      <button className={piBtnDropdown} 
                        onClick={() => {setYear(value); setShowYYYY(false);}}
                        key={index + 44}>{value}</button>
                    )
                })}</div>}
              </div>
            </div>
            <label className={piLabel}>Phone Number</label>
            <div className={dateBirthContainer}>
              <input className="pi-input-phone" 
                  id={"phone"} 
                  type="text"
                  ref={phone}
                  placeholder={customerInfo.phoneNumber ? customerInfo.phoneNumber.number : "+387"} />
              <div className="pi-not-verified">Not verified</div>
            </div>
          </div>
        </div>
        {!cardInfo && <button className={piTitleBtn + " title-btn-hide"}
            onClick={() => {setCardInfo(!cardInfo)}}>&or; Card Information (Optional)</button>}
          {cardInfo && <button className={piTitleBtn}
              onClick={() => {setCardInfo(!cardInfo)}}>&and; Card Information (Optional)</button>}   
          <div className={cardInfo ? personalInfoContainer : "pi-hide"}>
            <div></div>
            <div className={piInfoDiv}>
              <label className={piLabel}>Name on Card</label>
              <input className={piInputs} 
                  id={"cardName"} 
                  type="text"
                  ref={cardName}
                  placeholder={customerInfo.card ? customerInfo.card.name : "JOHN DOE"} />
              <label className={piLabel}>Card Number</label>
              <input className={piInputs} 
                  id={"cardNumber"} 
                  type="text"
                  ref={cardNumber}
                  placeholder={customerInfo.card ?
                      customerInfo.card.number : "XXXX-XXXX-XXXX-XXXX"} />
              <div className={dateBirthContainer}>
                <label className={piLabel + " pi-label-bigger"}>Expiration Date</label>
                <label className={piLabel + " " + dayBirthDiv}>CVC/CVV</label>
              </div>        
              <div className={dateBirthContainer}>
                <div className={dayBirthDiv}>
                  <button className={dayBirthBtn}
                    onClick={() => {setShowMMCard(!showMMCard)}}>
                    {monthCard ? addZero(monthCard) : (customerInfo.card ? addZero(customerInfo.card.expirationMonth) : "MM")}
                    <Icon style={{float: "right"}}
                        width="30"
                        height="24" 
                        icon="ri:arrow-drop-down-line" />
                  </button>
                  {showMMCard && <div className={dayBirthDropdown}>{
                    months.map((value, index) => {
                      return(
                        <button className={piBtnDropdown} 
                          onClick={() => {setMonthCard(value); setShowMMCard(false);}}
                          key={index + 32}>{addZero(value)}</button>
                      )
                  })}</div>}
              </div>
              <div className={dayBirthDiv + " db-middle"}>
                <button className={dayBirthBtn}
                  onClick={() => {setShowYYYYCard(!showYYYYCard)}}>
                  {yearCard ? addZero(yearCard) : (customerInfo.card ? addZero(customerInfo.card.expirationYear) : "YY")}
                  <Icon style={{float: "right"}}
                      width="30"
                      height="24" 
                      icon="ri:arrow-drop-down-line" />
                </button>
                {showYYYYCard && <div className={dayBirthDropdown}>{
                  yearsYY.map((value, index) => {
                    return(
                      <button className={piBtnDropdown} 
                        onClick={() => {setYearCard(value); setShowYYYYCard(false);}}
                        key={index + 44}>{value}</button>
                    )
                })}</div>}
              </div>              
              <input className={piInputs + " " + dayBirthDiv} 
                  id={"cVCCVV"} 
                  type="text"
                  ref={cVCCVV}
                  placeholder={customerInfo.card ? customerInfo.card.cvcCvv : "***"} />
              </div>        
            </div>
          </div>    
        {!addressInfo && <button className={piTitleBtn + " title-btn-hide"}
            onClick={() => {setAddressInfo(!addressInfo)}}>&or; Shipping Address (Optional)</button>}
        {addressInfo && <button className={piTitleBtn}
              onClick={() => {setAddressInfo(!addressInfo)}}>&and; Shipping Address (Optional)</button>}    
          <div className={addressInfo ? personalInfoContainer : "pi-hide"}>
            <div></div>
            <div className={piInfoDiv}>
              <label className={piLabel}>Street</label>
              <input className={piInputs} 
                  id={"street"}
                  type="text"
                  ref={street}
                  placeholder={customerInfo.address ? customerInfo.address.street : "123 Main Street"} />
              <div className={dateBirthContainer}>
                <label className={piLabel + " pi-label-addr-left"}>City</label>
                <label className={piLabel + " pi-label-addr"}>Zip Code</label>
              </div>    
              <div className={dateBirthContainer}>
                <input className={piInputs + " pi-label-addr-left"} 
                    id={"city"}
                    type="text"
                    ref={city}
                    placeholder={customerInfo.address ? customerInfo.address.city : "eg. Madrid"} />
                <input className={piInputs + " pi-label-addr"} 
                    id={"zip"}
                    type="text"
                    ref={zip}
                    placeholder={customerInfo.address ? customerInfo.address.zipCode : "XXXXXXX"} />
              </div>      
              <label className={piLabel}>State</label>
              <input className={piInputs} 
                  id={"state"}
                  type="text"
                  ref={state}
                  placeholder={customerInfo.address ? customerInfo.address.state : "eg. Asturias"} />   
              <label className={piLabel}>Country</label>
              <input className={piInputs} 
                  id={"country"}
                  type="text"
                  ref={country}
                  placeholder={customerInfo.address ? customerInfo.address.country : "eg. Spain"} />        
            </div>
          </div>    
        <button className="pi-save-info" onClick={updateUserInfo}>SAVE INFO</button>
      </div>}
    </div>
  )
}

export default Profile
