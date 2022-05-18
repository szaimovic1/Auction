import { Rating } from "@mui/material";
import { useEffect, useState } from "react";
import { Icon } from "@iconify/react";
import { SERVER_REVIEW, SERVER_EMAIL_REMINDER } from "@endpoints";
import { getData, postData } from "@api/api.js";

const ReviewSeller = ({ setHide, seller, productId }) => {
    const [rating, setRating] = useState(0)
    const [message, setMessage] = useState(null)
    const [startRating, setStartRating] = useState(null)

    useEffect(() => {
        async function fetchData() {
            const params = {
                seller_id: seller.id
            }
            const data = await getData(SERVER_REVIEW, params)
            if(typeof data === "number" && data !==null) {
                setRating(data)
                setStartRating(data)
            }
        }
        fetchData()
    }, [seller.id])

  return (
    <div>
        <div 
          className="jump-up-payment"
          style={
              {
                  width: "25%",
                  minWidth: "fit-content",
                  padding: "0 0 20px 0"
              }
          }
        >
            <div 
              className="related-bidders"
              style={{marginBottom: "40px"}}
            >
              SELLER RATING
            </div>
            <div 
              style={
                {
                  width: "100%", 
                  textAlign: "center",
                  marginBottom: "20px"
                }
              }
            >
                <div  
                  className="img-bidders-container" 
                  style={{margin: "auto"}}
                >
                    <img 
                      className="img-bidders" 
                      src={seller.profilePicture} 
                      alt="" 
                    />
                </div>
            </div>
            <div 
              className="start-from-info"
              style={{marginBottom: "30px"}}
            >
              {seller.firstName + " " + seller.lastName}</div>
            <div className="related-bidders">Please, rate a seller!</div>
            <div style={{margin: "30px 0 30px 0"}}>
                <Rating 
                  value={rating}
                  precision={1}
                  onChange={(event, newValue) => {
                    setRating(newValue);
                  }}
                  icon={<Icon icon="ant-design:star-filled" color="#8367d8" width="20" />}
                  emptyIcon={<Icon icon="bx:star" color="#8367d8" width="20" />}
                />
            </div>
            {message && <div id="payment-message" className="payment-message">{message}</div>}
            <div className="ai-circle-line">
                <button 
                  className={"aib-smaller pi-label-addr-left"} 
                  onClick={async () => {
                      if(rating === 0) {
                        const params = {
                          productId: productId
                        }
                        const data = await postData(SERVER_EMAIL_REMINDER, null, params)
                        console.log(data)
                      }
                      setHide(true)
                  }}
                >
                  CANCEL
                </button>
                <div className="aib-invisible"></div>
                <button 
                  className={"aib-smaller-reverse"} 
                  onClick={async () => {
                    if(rating !== 0 && startRating !== rating) {
                        const body = {
                            sellerId: seller.id,
                            rating: rating
                        }
                        const data = await postData(SERVER_REVIEW, body)
                        if(data.isSuccess) {
                            setHide(true)
                            //could cancel email, if it is not sent already..
                        }
                        else {
                            setMessage(data.message)
                        }
                    }
                    else if(startRating === null) {
                        setMessage("Rating missing!")
                    }
                    else {
                        setHide(true)
                    }
                }}
                >
                  DONE
                </button> 
            </div> 
        </div>
        <div className="settings-overlay"></div>
    </div>
  )
}

export default ReviewSeller
