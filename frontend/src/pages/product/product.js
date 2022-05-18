import { useNavigate, useParams } from 'react-router';
import Message from '@components/message/message';
import AdditionalInfo from '@components/additional_info/additionalInfo';
import { useEffect, useState } from 'react';
import { postData, getData } from '@api/api';
import './product.scss';
import { MSG_ERROR, MSG_WIN, HOME } from '@constants';
import ProductGallery from '@components/product_gallery/productGallery';
import { isAuthenticated } from '@utils/auth';
import GridProductsHome from '@components/grid_products_home/productsGrid';
import { PRODUCT_INFO_DISPLAY, LOGOUT } from '@constants';
import { SERVER_PRODUCT_INFO, SERVER_BIDDERS,
    SERVER_BID, SERVER_PRODUCT, SERVER_PAYMENT_INTENT } from '@endpoints';
import { loadStripe } from "@stripe/stripe-js";
import { Elements } from "@stripe/react-stripe-js";
import CheckoutForm from './checkoutForm';
import { useLocation } from "react-router-dom";
import { getUrlParameter } from '@utils/functions.js';
import ReviewSeller from './reviewSeller';
import ProductDetails from './productDetails';
import AboutSeller from './aboutSeller';

const stripePromise = loadStripe(process.env.REACT_APP_STRIPE_KEY);

const Product = () => {
  const [page, setPage] = useState(0)
  let { id } = useParams()
  const [product, setProduct] = useState(null)
  const [message, setMessage] = useState("")
  const [style, setStyle] = useState()
  const [value, setValue] = useState(0)
  const infoSfPrice = "info-sf-price"
  const priceInfo = "price-info"
  const [saveId, setSaveId] = useState(id)
  const [bidders, setBidders] = useState([])
  const [hasMore, setHasMore] = useState(false)
  const navigate = useNavigate()

  const [products, setProducts] = useState()

  const [clientSecret, setClientSecret] = useState("");
  const appearance = {
    theme: 'stripe',
  }
  const options = {
    clientSecret,
    appearance,
  }
  const location = useLocation()
  const [hideReview, setHideReview] = useState(getUrlParameter("review", location) === "" ? true : false)

  useEffect(() => {
    async function fetchData() {
      const params = { id: parseInt(saveId) }
      const data = await getData(SERVER_PRODUCT_INFO, params)
      if(data.logout) {
        navigate(LOGOUT)
      }
      else if(data.isSuccess) {
        setProduct(data)
      }
      else {
        //navigate home or 404 not found?
        //new else would require rerender state
        //and message dependency would be sufficient
        navigate(HOME)
      }      
    }
    fetchData();
  }, [saveId, value, message, navigate, hideReview]);

  useEffect(() => {
    async function fetchData() {
      if(product && product.bidders) {
        const params = {
          id: parseInt(saveId),
          page: page,
          amount: 5
        }
        const data = await getData(SERVER_BIDDERS, params)
        if(data.isSuccess === undefined) {
          setBidders(p =>  p.concat(data))
          setHasMore(data.length > 0)
        }
        else {
          setError(data.message)
        }
      }
    }
    fetchData();
  }, [saveId, page, product, navigate]);

  useEffect(() => {
    setPage(0)
    setMessage("")
    setValue(0)
    if(document.getElementById("bid-input")) 
        document.getElementById("bid-input").value = ""
  }, [saveId]);

  useEffect(() => {
    async function fetchData() {
      const params = {
        categories: product.categoryId,
        colors: "",
        sizes: "",
        startPrice: 0,
        endPrice: 0,
        page: page,
        prods: 3
      }
      const data = await getData(SERVER_PRODUCT, params)
      if(data.logout) {
        navigate(LOGOUT)
      }
      else if(data.isSuccess) {
        setProducts(data.products)
      }
      else {
        setError(data.message)
      }
    } 
    if(product && product.relatedProducts) {
      fetchData()
    }
  }, [page, navigate, product])

  const timeLeft = (date) => {
    const today = new Date()
    let string = ""
    string += Math.round((new Date(date) - today) / (7 * 24 * 60 * 60 * 1000)).toString() + " Weeks "
    string += Math.round((new Date(date) - today) / (24 * 60 * 60 * 1000)).toString() + " Days"
    return string
  }

  const checkDate = (date) => {
    return new Date(date) > new Date()
  }

  const setError = (msg) => {
    setStyle(MSG_ERROR)
    setMessage(msg)
  }

  const placeBid = async () => {
    if(value && parseFloat(value) > 0) {
      const body = {
        id: parseInt(saveId),
        value: parseFloat(value)
      }
      const data = await postData(SERVER_BID, body)
      if(data.logout) {
        navigate(LOGOUT)
      }
      else if(data.isSuccess) {
        setStyle(data.isSuccess ? MSG_WIN : MSG_ERROR)
        setMessage(data.message)
        setValue(0)
        document.getElementById("bid-input").value = ""
        return
      }
      else {
        setError(data.message)
        return
      }
    }
    setError("Bad input value!")
  }

  const formatDate = (date) => {
    const d = new Date(date).toLocaleString('en-GB',{day: 'numeric', month:'long', year:'numeric'})
    return d
  }

  const payment = async () => {
    const body = {
      productId: id,
      productPrice: product.highestBid
    }
    const data = await postData(SERVER_PAYMENT_INTENT, body)
    if(data.isSuccess) {
      setClientSecret(data.clientSecret)
    }
    else {
      setError(data.message)
    }
  }

  const openReview = () => {
    setHideReview(false)
  }

  return (
    <>
    {product && product.product && <div className="product-page-container">
      <AdditionalInfo leftText={product.product.name} 
          rightText={{main: "Shop ->", page: "Single product"}} />
      {message && <Message message={message} style={style} />}
      <div className="product-body">
        <div className="gallery-info">
          <div className="gallery-info-element gie-left">
            <ProductGallery images={product.images}/>
          </div>
          <div className="gallery-info-element gie-right">
            <div className="name-info">{product.product.name}</div>
            <div className={infoSfPrice}>
              <div className="start-from-info">Start From</div>
              <div className={priceInfo}>${product.product.price.toFixed(2)}</div>
            </div>
            <div className="info-bid-container">
              {product.highestBid && <div className={infoSfPrice}>
                <div className="start-from-dark">Highest bid:</div>
                <div className={priceInfo}>${product.highestBid.toFixed(2)}</div>
              </div>}
              <div className={infoSfPrice}>
                <div className="start-from-dark">Number of bids:</div>
                <div className={priceInfo}>{product.numberOfBids}</div>
              </div>
              <div className={infoSfPrice}>
                <div className="start-from-dark">Time left:</div>
                <div className={priceInfo}>{timeLeft(product.product.endDate)}</div>
              </div>
            </div>
            <div className={isAuthenticated() && product.relatedProducts ? "place-bid" : 
                 "place-bid disable-bidding"}>    
              {checkDate(product.product.endDate) ? 
                <input 
                  className="input-bid-value" 
                  id="bid-input"
                  type="number" 
                  step="any"
                  onChange={(e) => setValue(e.target.value)}
                  placeholder={"Enter $" + ((product.highestBid 
                    ? product.highestBid : 0) + 1).toFixed(2) + " or higher"} 
                /> :
                <AboutSeller seller={product.seller}/>
              }
              <button className="bid-product-info"
                onClick={checkDate(product.product.endDate) ? placeBid : 
                  product.isPaidFor ? openReview : payment}
              >
                {checkDate(product.product.endDate) ? "PLACE BID >" : 
                  product.isPaidFor ? "REVIEW" : "PAY"}
              </button>
            </div>
            <ProductDetails product={product}/>
          </div> 
        </div>
        <div className="related-bidders">{product.relatedProducts ? "Related Products" : "Bidders"}</div>
        <div className="line-btn"></div>
        {products && 
          <GridProductsHome 
            products={products}
            hasMore={false}
            numberOfProducts={0}
            page={0}
            display={PRODUCT_INFO_DISPLAY}  
            setId={setSaveId}
          />
        }
        {product.bidders && bidders &&
          <div className="div-table-bidders">
            <table className="table-container-bidders">
              <tbody><tr>
                <td className="table-col1">Bidder</td>
                <td className="table-col2">Date</td>
                <td className="table-col3">Bid</td>
                </tr></tbody>
            {
            bidders.map((value, index) => {
              return <tbody key={index} 
                      id={index}><tr>
                  <td className="img-name">
                    <div className="img-bidders-container">
                      <img className="img-bidders" src={value.bidder.profilePicture} alt="" />
                    </div>
                    <div className="img-name-bidder">
                      {value.bidder.firstName + " " + value.bidder.lastName}
                    </div>
                  </td>
                  <td className="bidders-date">{formatDate(value.bidTime)}</td>
                  <td className={index ? "bidders-bid" : "bidders-bid-active"}>${value.value}</td>
                  </tr></tbody>
            })}</table>
          {hasMore && bidders.length < product.numberOfBids 
              && <button className="more-bidders" onClick={() => {setPage(page+1)}}>+</button>}
          </div>}
          {/*add btn for closing*/}
          {clientSecret && (
            <Elements options={options} stripe={stripePromise}>
              <CheckoutForm />
            </Elements>
          )}
          {!hideReview && (
            <ReviewSeller 
              setHide={setHideReview}
              seller={product.seller}
              productId={id} 
            />
          )}
      </div>
    </div>}
    </>
  )
}

export default Product
