import React from 'react'

const AboutSeller = ({ seller }) => {
  return (
    <div className="img-name" style={{maxHeight: "50px", justifyContent: "center"}}>
      <div 
        className="start-from-info"
        style={{marginRight: "10px"}}
      >
        Seller:
      </div>
      <div  className="img-bidders-container">
        <img 
          className="img-bidders" 
          src={seller.profilePicture} 
          alt="" 
        />
      </div>
      <div 
        className="st-name"
        style={{marginLeft: "10px", marginRight: "50px"}}
      >
        {seller.firstName + " " + seller.lastName}
      </div>
    </div>
  )
}

export default AboutSeller
