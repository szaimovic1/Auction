import AdditionalInfo from "@components/additional_info/additionalInfo";
import ProfileSelector from '@components/profile_selector/profileSelector';
import { BIDS_SELECTOR } from "@constants";
import { useEffect, useState } from "react";
import { LOGOUT } from '@constants';
import { SERVER_PRODUCT_BID } from "@endpoints";
import { getData } from "@api/api";
import { useNavigate } from "react-router";
import TableSellerBids from "@components/table_seller_bids/tableSellerBids";
import '@pages/seller/seller.scss';

const Bids = () => {
  const [products, setProducts] = useState()
  const [numOfProducts, setNumOfProducts] = useState()
  const [page, setPage] = useState(0)
  const navigate = useNavigate()

  useEffect(() => {
    async function fetchData() {
      let data
      const params = {
        bidder: true,
        page: page,
        prods: 5
      }
      data = await getData(SERVER_PRODUCT_BID, params)
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
  }, [page, navigate, setProducts])

  return (
    <div>
      <AdditionalInfo 
        leftText={"Bids"} 
        rightText={{main: "My Account ->", page: "Bids"}} 
      />
      <ProfileSelector page={BIDS_SELECTOR} />
      <div className="seller-container">
      <TableSellerBids products={products}
          bidder={true}
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

export default Bids