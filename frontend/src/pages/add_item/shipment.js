const Shipment = ({
    street, setStreet,
    city, setCity,
    zip, setZip,
    country, setCountry,
    email, setEmail,
    phone, setPhone
  }) => {
    const piLabel = "pi-label"
    const piInputs = "pi-inputs"
    const dateBirthContainer = "date-birth-container"

  return (
    <>
      <div className="add-item-txt">{"LOCATION & SHIPPING"}</div>
      <label className={piLabel}>Address</label>
      <input className={piInputs} 
        id={"street"}
        type="text"
        value={street ? street : ""}
        onChange={(e) => {setStreet(e.target.value)}}
        placeholder={street ? street : "123 Main Street"} />
      <label className={piLabel}>Enter Email</label>
        <input className={piInputs} 
          id={"email"} 
          type="text"
          value={email ? email : ""}
          onChange={(e) => {setEmail(e.target.value)}}
          placeholder={email} />
      <div className={dateBirthContainer}>
        <label className={piLabel + " pi-label-addr-left"}>City</label>
        <label className={piLabel + " pi-label-addr"}>Zip Code</label>
      </div>    
      <div className={dateBirthContainer}>
        <input className={piInputs + " pi-label-addr-left"} 
          id={"city"}
          type="text"
          value={city ? city : ""}
          onChange={(e) => {setCity(e.target.value)}}
          placeholder={city ? city : "eg. Madrid"} />
        <input className={piInputs + " pi-label-addr"} 
          id={"zip"}
          type="text"
          value={zip ? zip : ""}
          onChange={(e) => {setZip(e.target.value)}}
          placeholder={zip ? zip : "XXXXXXX"} />
      </div>    
      <label className={piLabel}>Country</label>
        <input className={piInputs} 
          id={"country"}
          type="text"
          value={country ? country : ""}
          onChange={(e) => {setCountry(e.target.value)}}
          placeholder={country ? country : "eg. Spain"} />   
      <label className={piLabel}>Phone Number</label>
        <div className={dateBirthContainer}>
          <input className="pi-input-phone" 
            id={"phone"} 
            type="text"
            value={phone ? phone : ""}
            onChange={(e) => {setPhone(e.target.value)}}
            placeholder={phone ? phone : "+387"} />
          <div className="pi-not-verified">Not verified</div>
        </div>
    </>
  )
}

export default Shipment