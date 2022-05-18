import './productGallery.scss';
import { useState } from 'react';

const ProductGallery = ({ images }) => {
  const [display, setDisplay] = useState(images[0])

  if(images.indexOf(display) === -1)
      setDisplay(images[0])

  return (
    <div className="gallery-container">
      <div className="gallery-img-container">
        <img className="gallery-img" src={display} alt=""/>
      </div>
      <div className="gallery-imgs-main-container">{
        images.map((value, index) => {
          return <div key={index} 
                    id={index}
                    className="gallery-imgs-container"
                    onClick={(e) => {setDisplay(images[parseInt(e.target.id)])}}>
                  <img id={index} 
                    alt=""
                    className={ images.indexOf(display) === parseInt(index) 
                      ? "gallery-img active-img" : "gallery-img"} 
                    src={value}/>
                </div>
      })}</div>
    </div>
  )
}

export default ProductGallery