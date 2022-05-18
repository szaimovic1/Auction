import { useState } from "react"
import { Icon } from "@iconify/react";
import { LinearProgress, Rating } from "@mui/material";
import AboutSeller from "./aboutSeller";

const ProductDetails = ({ product }) => {
    const productDetails = "productDetails"
    const sellerInfo = "sellerInfo"
    const [active, setActive] = useState(productDetails)
    const starNumber = [product.seller.review.fiveStars,
        product.seller.review.fourStars,
        product.seller.review.threeStars,
        product.seller.review.twoStars,
        product.seller.review.oneStar]

  return (
    <div>
      <div className="btn-container">
        <button 
          className={"prod-info-btn-style" + 
            (active === productDetails ? " prod-info-btn-style-chosen" : "")}
          onClick={() => {setActive(productDetails)}}
        >
          Details
        </button>
        <button 
           className={"prod-info-btn-style" + 
             (active === sellerInfo ? " prod-info-btn-style-chosen" : "")}
           onClick={() => {setActive(sellerInfo)}}
          >
            Seller Information
        </button>
      </div>
      <div className="line-btn" style={{marginBottom: "10px"}}></div>
      {active === productDetails ?
        <div className="product-info-description"
        >
          {product.product.description}
        </div> :
        <div>
          <AboutSeller seller={product.seller} />
          <div style={{display: "flex", padding: "10px 15% 0 15%"}}>
            <div style={{textAlign: "center", margin: "auto"}}>
              <div className="related-bidders"
              >
                {product.seller.review.rating ? product.seller.review.rating.toFixed(2) : 0}
              </div>
              <div>
                <Rating 
                  value={product.seller.review.rating ? product.seller.review.rating : 0}
                  readOnly
                  icon={<Icon icon="ant-design:star-filled" color="#8367d8" />}
                  emptyIcon={<Icon icon="bx:star" color="#8367d8" />}
                />
              </div>
              <div style={{display: "flex", justifyContent: "center"}}>
                <Icon icon="emojione-monotone:speaking-head" />
                <div style={{marginLeft: "20px", fontWeight: "bold"}}
                >
                  {product.seller.review.numberOfVotes}
                </div>
              </div>
            </div>
            <div style={{marginLeft: "15%", width: "100%"}}>
              {
                starNumber.map((value, index) => {
                  return(
                    <div 
                      key={index} 
                      style={{marginTop: "10px"}}
                    >
                      <div style={{display: "flex", justifyContent: "center"}}>
                        <Icon icon="ant-design:star-filled" color="#8367d8" width="20" />
                        <div style={{fontWeight: "bold", marginLeft: "10px"}}
                        >
                          {value.toFixed(2)}%
                        </div>
                      </div>
                      <LinearProgress variant="determinate" value={value} color="inherit" />
                    </div>
                  )
                })
              }
            </div>
          </div>
        </div>
      }
    </div>
  )
}

export default ProductDetails
