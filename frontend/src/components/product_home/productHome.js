import { useState } from 'react';
import { Navigate } from 'react-router';
import { PRODUCT_INFO_DISPLAY, FEATURED_PRODUCTS } from '@constants';
import './productHome.scss';
import { SERVER_WISHLIST } from '@endpoints';
import { putData } from "@api/api";

const ProductHome = ({ product, customRef, display = null, setId = null }) => {
  const [productClicked, setProductClicked] = useState("")
  let gridListClasses = []
  const gridClasses = ["details-container", "img-grid-container", "img-buttons-container", 
      "img-two-buttons", "img-smaller", null, "item-name", "text-price", "start-from-grid", "price-grid"]
  const infiniteRowClasses = ["details-container-vol2", "img-grid-container", "img-buttons-container-vol2", 
      "img-two-buttons-vol2", "img-smaller-vol2", null, "item-name-vol2", "text-price", "start-from-grid", "price-grid"]
  const listClasses = ["details-container-list", "img-list-container", "img-list-buttons", "img-two-buttons",
      "img-smaller", "display-block-list", "item-name", "text-price-list", "start-from-list", "price-list"]
  if(display === null)
      gridListClasses = gridClasses
  else if(display === PRODUCT_INFO_DISPLAY || display === FEATURED_PRODUCTS)
      gridListClasses = infiniteRowClasses                   
  else gridListClasses = listClasses        

  const getProduct = (event) => {
    const id = event.target.id
    if(display === PRODUCT_INFO_DISPLAY) {
      window.history.pushState({}, '', "/product/" + id)
      setId(id)
      window.scrollTo(0, 0) 
      return;
    }
    setProductClicked(id)
  }     
  
  const removeFromWishlist = async () => {
    //refresh in context or navigate again? 
    const body = {
      productId: product.id,
      add: false
    }
    const data = await putData(SERVER_WISHLIST, body)
    console.log(data)
  }

  const addOnWishlist = async () => {
    const body = {
      productId: product.id,
      add: true
    }
    const data = await putData(SERVER_WISHLIST, body)
    console.log(data)
  }

  return (
    <div className={gridListClasses[0]}>
      {productClicked && <Navigate to={"/product/" + productClicked}/>}
      <div className={gridListClasses[1]}>
        <div className={gridListClasses[2]} />
        <div className={gridListClasses[3]}>
          {product.isOnWishlist !== null && 
            <button 
              id={product.id} 
              className="whishlist-bid space"
              onClick={product.isOnWishlist ? removeFromWishlist : addOnWishlist}
            >
              {product.isOnWishlist ? "Remove " : "Whishlist "}
              &#x2661;
            </button>
          }
          <button id={product.id} className="whishlist-bid top-setting" 
              onClick={getProduct}>Bid &#x24;</button>
        </div>
        <img src={product.image} 
            className={gridListClasses[4]} alt=""></img>
      </div> 
      <div id={product.id} onClick={getProduct}  
          className={gridListClasses[5]}> 
        <div id={product.id} className={gridListClasses[6]}>{product.name}</div>
        {display === "list-display" && <div id={product.id} 
            className="list-description">{product.description}</div>}
        <div id={product.id} className={gridListClasses[7]}>
          <div id={product.id} ref={customRef} className={gridListClasses[8]}>Start From</div>
          <div id={product.id} className={gridListClasses[9]}>${product.price.toFixed(2)}</div>
        </div>
      </div>
    </div>
  )
}

export default ProductHome
