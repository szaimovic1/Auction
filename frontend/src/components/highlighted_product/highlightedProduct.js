import { useState, useEffect } from 'react';
import { Navigate } from 'react-router';
import './highlightedProduct.scss';
import { getData } from '@api/api'
import { SERVER_HIGHLIGHT } from '@endpoints';

const HighlightedProduct = () => {
  const description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +  
    "Vestibulum hendrerit odio a erat lobortis auctor. " + 
    "Curabitur sodales pharetra placerat. " +
    "Aenean auctor luctus tempus. " + 
    "Cras laoreet et magna in dignissim. " + 
    "Nam et tincidunt augue. Vivamus quis.."
  const buttonText = "BID NOW >"
  const [isClicked, setIsClicked] = useState(false)
  const [product, setProduct] = useState([])

  useEffect(() => {
    const fetchData = async () => {
      const data = await getData(SERVER_HIGHLIGHT)
      if(data.isSuccess) {
        setProduct(data)
      }
    }
    fetchData()
  }, [])

  return (
    <div className="highlighted-container">
        {isClicked && <Navigate to={"/product/" + product.id}/>}
        {product.length !== 0 ? <>
        <div className="highlight-details"> 
            <div>
                <div className="name">{product.name}</div>
                <div className="price">Start From ${product.price.toFixed(2)}</div>
                <div className="long-text">{description}</div>
                <button onClick={() => {setIsClicked(!isClicked)}} className="bid">{buttonText}</button>
            </div>
        </div>
        <div className="img-container">
            <img className="highlighted-img" src={product.image} alt=""></img>
        </div> 
        </> : <div>No product to show</div>}
    </div> 
  )
}

export default HighlightedProduct