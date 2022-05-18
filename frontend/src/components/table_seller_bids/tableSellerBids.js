import React from 'react'
import { useNavigate } from 'react-router'
import { PRODUCT } from '@constants';
import '@pages/seller/seller.scss';
import { Icon } from "@iconify/react";
import { ADD_ITEM, SHOP } from '@constants';

const TableSellerBids = ({ products, bidder }) => {
    const activeSoldBtns = "active-sold-btns-v2"
    const navigate = useNavigate()

  return (
    <div className={activeSoldBtns}>
      <table className="table-container-seller">
        <tbody>
          <tr>
            <td className="st-col1">Item</td>
            <td className="st-col2">Name</td>
            <td className="st-col1">Time left</td>
            <td className="st-col4">Your price</td>
            <td className="st-col1">No. bids</td>
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
                        ? " st-win-bid" : "")
                      }>
                      ${bidder ? value.highestPersonalBid.toFixed(2) : value.product.price.toFixed(2)}
                  </td>
                  <td className="st-time">{value.numberOfBids}</td>
                  <td className={
                        "st-time" + (value.isHighestBidMine
                        ? " st-win-bid" : " st-regular-bid")}>
                        ${value.highestBid.toFixed(2)}
                  </td>
                  <td className="st-button">
                    <button className={
                      "bid-seller" + 
                      (value.isHighestBidMine
                      ? " bs-purple-border" : "")
                      }
                      onClick={() => 
                      {navigate(PRODUCT + "/" + value.product.id)}
                    }>{bidder ? (new Date(value.product.endDate) < new Date() ?
                         "PAY" : "BID") : "VIEW"}
                    </button>
                  </td>
                </tr>
              </tbody>
            )
          })
        }
      </table>
      {products && 
        products.length === 0 && 
        ((bidder && 
        <div className="si-cart-hammer">
           <Icon icon="ic:twotone-gavel" 
            color="#8367d8" 
            width="150"
          />
          <div className="si-plain-div">You don’t have any bids and there are so many cool products available for sale.</div>
          <button 
            onClick={() => {navigate(SHOP)}}
            className="bid-seller-v2">START BIDDING</button>
        </div>) || 
        <div className="si-cart-hammer">
          <Icon icon="eva:shopping-cart-outline" 
            color="#8367d8" 
            width="150"
          />
          <div className="si-plain-div">You do not have any scheduled items for sale.</div>
          <button 
            onClick={() => {navigate(ADD_ITEM)}}
            className="bid-seller-v2">START SELLING</button>
        </div>)
      }
    </div>
  )
}

export default TableSellerBids