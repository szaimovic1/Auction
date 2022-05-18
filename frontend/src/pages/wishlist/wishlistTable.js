import { Icon } from "@iconify/react";
import { SERVER_WISHLIST } from "@endpoints";
import { PRODUCT, SHOP } from "@constants";
import { useNavigate } from "react-router";
import { putData } from "@api/api";

const WishlistTable = ({ products, setRerender, rerender, setPage }) => {
    const navigate = useNavigate()
    const activeSoldBtns = "active-sold-btns-v2"

  return (
    <div className={activeSoldBtns}>
      <table className="table-container-seller">
        <tbody>
          <tr>
            <td className="st-col1">Item</td>
            <td className="st-col2">Name</td>
            <td className="st-col1">Time left</td>
            <td className="st-col4">Highest bid</td>
            <td className="st-col3"></td>
          </tr>
        </tbody>
        {products &&
          products.length !== 0 &&
          products.map((value, index) => {
            return(
              <tbody key={index} 
                className={value.isHighestBidMine ? "si-win-background" : null}>
                <tr>
                  <td>
                    <div className="img-seller-container">
                      <img className="img-seller" src={value.product.image} alt="" />
                    </div>
                  </td>
                  <td className="st-name">
                    <div>
                      <div>{value.product.name}</div>
                      <div className="unknown-code">#12345678</div>
                    </div>
                  </td>
                  <td className="st-time">{
                     Math.trunc(Math.abs(new Date(value.product.endDate)
                     - new Date()) / 36e5)}h
                  </td>
                  <td className={
                        "st-time" + (value.isHighestBidMine
                        ? " st-win-bid" : " st-regular-bid")}>
                        ${value.highestBid.toFixed(2)}
                  </td>
                  <td className="st-button">
                    <div style={{display: "flex"}}>
                      <button 
                        className="bid-seller"
                        style={{backgroundColor: "#E3E3E3", marginRight: "10px"}}
                        onClick={async () => {
                            const body = {
                              productId: value.product.id,
                              add: false
                            }
                            const data = await putData(SERVER_WISHLIST, body)
                            if(data.isSuccess) {
                                setPage(0)
                                setRerender(!rerender)
                            }
                        }}>REMOVE
                      </button>
                      <button className={
                        "bid-seller" + 
                        (value.isHighestBidMine
                        ? " bs-purple-border" : "")
                        }
                        onClick={() => 
                        {navigate(PRODUCT + "/" + value.product.id)}
                      }>VIEW
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            )
          })
        }
      </table>
      {products && 
        products.length === 0 && 
        <div className="si-cart-hammer">
          <Icon icon="akar-icons:heart" 
            color="#8367d8" 
            width="150"
          />
          <div className="si-plain-div">Your wishlist is empty! Start browsing the shop.</div>
          <button 
            onClick={() => {navigate(SHOP)}}
            className="bid-seller-v2">VISIT SHOP</button>
        </div>
      }
    </div>
  )
}

export default WishlistTable
