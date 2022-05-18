import AdditionalInfo from "@components/additional_info/additionalInfo";
import { useState, useEffect, useRef } from 'react';
import './addItems.scss';
import { postData, getData } from '@api/api';
import { SERVER_PRODUCT, SERVER_SHIPMENT_USER } from '@endpoints';
import { getEmail } from '@utils/auth';
import { LOGOUT, PROFILE, MSG_ERROR, SELLER } from '@constants';
import { useNavigate } from "react-router";
import SetPrices from "./setPrices";
import AddItemPage from "./addItemPage";
import Circles from "./circles.js";
import Shipment from "./shipment";
import { Icon } from '@iconify/react';
import Message from '@components/message/message';
import { correctDate } from "@utils/functions";
import { validateProduct } from "@utils/validators";

const AddItem = () => {
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()
  const [, setRefresh] = useState(true)
  const [page, setPage] = useState(0)
  const [subcategory, setSubcategory] = useState(null)
  const [subcategoryName, setSubcategoryName] = useState()
  const [category, setCategory] = useState(null)
  const [categoryName, setCategoryName] = useState()
  const name = useRef(null)
  const description = useRef(null)
  const price = useRef(null)
  const [startDate, setStartDate] = useState(null)
  const [endDate, setEndDate] = useState(null)
  const [street, setStreet] = useState(null)
  const [city, setCity] = useState()
  const [zip, setZip] = useState(null)
  const [country, setCountry] = useState()
  const [email, setEmail] = useState(getEmail())
  const [phone, setPhone] = useState(null)
  const [message, setMessage] = useState("")
  const style = MSG_ERROR
  const [startTime, setStartTime] = useState(null)
  const [endTime, setEndTime] = useState(null)

  useEffect(() => {
    async function fetchData() {
      const data = await getData(SERVER_SHIPMENT_USER)
      if(data.isSuccess === undefined) {
        if(data.address) {
          setStreet(data.address.street)
          setCity(data.address.city)
          setZip(data.address.zipCode.toString())
          setCountry(data.address.country)
        }
        if(data.phoneNumber) {
          setPhone(data.phoneNumber.number)
        } 
      }
      else if(data.logout === undefined) {
        setMessage(data.message)
      }  
    }
      fetchData()
  }, [])

  const update = async () => {
    if(startTime === null || endTime === null
       || startDate === null || endDate === null) {
      window.scrollTo(0, 0)
      setMessage("Missing date or time values!")
      return
    }
    const data = new FormData()
    if(file) {
      for(const f of file) {
        data.append('files', f)
      }
    }
    else {
      data.append('files', null)
    }
    const startT = startTime.split(":")
    const endT = endTime.split(":")
    startDate.setHours(parseInt(startT[0]), parseInt(startT[1]), 0)
    endDate.setHours(parseInt(endT[0]), parseInt(endT[1]), 0)
    const startD = correctDate(startDate)
    const endD = correctDate(endDate)
    const prod = {
      name: name.current.value,
      categoryId: subcategory ? subcategory : category,  
      description: description.current.value,
      price: price.current.value,
      startDate: startD.toISOString(),
      endDate: endD.toISOString(),
      street: street,
      city: city,
      zipCode: zip,  
      country: country,
      email: email,
      phone: phone
    }
    const msg = validateProduct(prod, file, startD, endD)
    if(!msg.isSuccess) {
      window.scrollTo(0, 0)
      setMessage(msg.message)
      return
    }
    data.append("product", JSON.stringify(prod))
    setLoading(true)
    const res = await postData(SERVER_PRODUCT, data)
    if(res.logout) {
      navigate(LOGOUT)
      return
    }
    else if(!res.isSuccess) {
      setLoading(false)
      window.scrollTo(0, 0)
      setMessage(res.message)
      return
    }
    navigate(SELLER)
  }

  return (
    <div>
      <AdditionalInfo 
        leftText={"Seller"} 
        rightText={{main: "My Account ->", page: "Add item"}} 
      />
      {message && <Message message={message} style={style} />}
      {loading ? 
        <div style={{height: "40vh"}}>
         <Icon icon="eos-icons:bubble-loading" 
           style={{marginTop: "15vh"}}
           width="100"
         />
        </div> : 
        <>
          <Circles page={page} />
          <div className="prod-info-container">
            {<div style={page !== 0 ? {display: 'none'} : {}}> 
              <AddItemPage 
                file={file}
                setFile={setFile}
                setRefresh={setRefresh}
                name={name}
                description={description}
                category={category}
                setCategory={setCategory}
                categoryName={categoryName}
                setCategoryName={setCategoryName}
                setSubcategory={setSubcategory}
                subcategoryName={subcategoryName}
                setSubcategoryName={setSubcategoryName}
              />
            </div>}
              {<div style={page !== 1 ? {display: 'none'} : {}}>  
                <SetPrices 
                  price={price}
                  startDate={startDate}
                  setStartDate={setStartDate}
                  endDate={endDate}
                  setEndDate={setEndDate}
                  startTime={startTime}
                  setStartTime={setStartTime}
                  endTime={endTime}
                  setEndTime={setEndTime}
                />
              </div>}
              {page === 2 && 
                <Shipment 
                  street={street}
                  setStreet={setStreet}
                  city={city}
                  setCity={setCity}
                  zip={zip}
                  setZip={setZip}
                  country={country}
                  setCountry={setCountry}
                  email={email}
                  setEmail={setEmail}
                  phone={phone}
                  setPhone={setPhone}
                />}
            <div className="ai-circle-line">
              {page === 0 ? 
              <>
                <button className={"ai-buttons pi-label-addr-left"} 
                  onClick={() => {navigate(PROFILE)}}>CANCEL</button>
                <button className={"ai-buttons-reverse"} 
                  onClick={() => {if(page !== 2) {setPage(page+1)}}}>NEXT</button>
              </> :
              <>
                <button className={"aib-smaller pi-label-addr-left"} 
                  onClick={() => {navigate(PROFILE)}}>CANCEL</button>
                <div className="aib-invisible"></div>
                <button className={"aib-smaller-halfreverse"} 
                  onClick={() => {if(page !== 0) {setPage(page-1)}}}>BACK</button>
                {page !== 2 ? 
                  <button className={"aib-smaller-reverse"} 
                    onClick={() => {if(page !== 2) {setPage(page+1)}}}>NEXT</button> :
                  <button className={"aib-smaller-reverse"} 
                    onClick={update}>DONE</button>}
              </>} 
            </div>
            
          </div>
        </>}
    </div>
  )
}

export default AddItem