import AdditionalInfo from "@components/additional_info/additionalInfo";
import ProfileSelector from '@components/profile_selector/profileSelector';
import { SELLER_SELECTOR } from "@constants";
import { useEffect, useState } from "react";
import { getData } from "@api/api";
import { useNavigate } from "react-router";
import { LOGOUT } from '@constants';
import './seller.scss';
import { SERVER_PRODUCT_BID } from "@endpoints";
import TableSellerBids from "@components/table_seller_bids/tableSellerBids";

const Seller = () => {
  const [active, setActive] = useState(true)
  const [products, setProducts] = useState()
  const [numOfProducts, setNumOfProducts] = useState()
  const [page, setPage] = useState(0)
  const navigate = useNavigate()
  const activeSoldBtns = "active-sold-btns"
  const buttonSellerActive = "button-seller-active"
  const buttonSellerNormal = "button-seller-normal"

  useEffect(() => {
    async function fetchData() {
      let data
      const params = {
        bidder: false,
        active: true,
        page: page,
        prods: 5
      }
      if(active) {
        data = await getData(SERVER_PRODUCT_BID, params)
      }
      else {
        params.active = false
        data = await getData(SERVER_PRODUCT_BID, params)
      }
      if(data.logout) {
        navigate(LOGOUT)
      }
      else if(data.isSuccess) {
        if(page === 0) {
          setProducts(data.products)
        }
        else {
          setProducts(p =>  p.concat(data.products))
        }
        setNumOfProducts(data.totalNumber)
      }
    }
    fetchData()
  }, [active, page, navigate, setProducts])

  return (
    <div>
      <AdditionalInfo 
        leftText={"Seller"} 
        rightText={{
          main: "My Account ->",
          page: "Seller"
        }} 
      />
      <ProfileSelector page={SELLER_SELECTOR} />
      <div className="seller-container">
        <div className={activeSoldBtns}>
          <button onClick={
            () => {if(!active){
              setActive(true)
              setPage(0)
            }}}
            className={active ? buttonSellerActive
               : buttonSellerNormal
          }>Active</button>
          <button onClick={
            () => {if(active){
              setActive(false)
              setPage(0)
            }}}
            className={active ? buttonSellerNormal
               : buttonSellerActive}
            >Sold</button>
        </div>
        <TableSellerBids products={products}
          bidder={false}
        />
        {products &&
          products.length < numOfProducts && 
          <div className="smb-container">
            <button className="seller-more-bidders" onClick={
                () => {setPage(page+1)}
            }>+</button>
          </div>}
      </div>
    </div>
  )
}

export default Seller