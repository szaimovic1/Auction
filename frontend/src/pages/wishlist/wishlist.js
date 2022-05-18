import { useState, useEffect } from "react";
import { useNavigate } from "react-router";
import { getData } from "@api/api";
import { SERVER_PRODUCT_BID } from "@endpoints";
import { WISHLIST_SELECTOR, LOGOUT } from "@constants";
import WishlistTable from "./wishlistTable";
import AdditionalInfo from "@components/additional_info/additionalInfo";
import ProfileSelector from '@components/profile_selector/profileSelector';

const Wishlist = () => {
    const [products, setProducts] = useState()
    const [numOfProducts, setNumOfProducts] = useState()
    const [page, setPage] = useState(0)
    const navigate = useNavigate()
    const [rerender, setRerender] = useState(false)

    useEffect(() => {
      //similar to bids and seller => refactor?
        async function fetchData() {
          let data
          const params = {
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
    }, [page, navigate, setProducts, rerender])

  return (
    <div>
      <AdditionalInfo 
        leftText={"Wishlist"} 
        rightText={{main: "My Account ->", page: "Wishlist"}} 
      />
      <ProfileSelector page={WISHLIST_SELECTOR} />
      <div className="seller-container">
      <WishlistTable 
        products={products} 
        rerender={rerender} 
        setRerender={setRerender} 
        setPage={setPage}
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

export default Wishlist
