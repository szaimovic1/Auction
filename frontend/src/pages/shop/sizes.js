import CheckBox from '@components/check_box/checkBox';

const Sizes = ({
    sizes,
    checkBoxesSizes,
    checkBoxSizeClicked
  }) => {

    const checkStyleSizes = ["label-category-size" , 
          "checked-category", "unchecked-category"] 
      
  return (
    <div className="shop-categories-container-size">
      <div className="title-filter">Size</div>{
        sizes.map((value, index) => {
          let id = value.size
          return (
            <div key={index} >
              <div id={id} key={index} >
                <CheckBox id={id} 
                  showing={checkBoxesSizes.get(id)} 
                  clicked={() => {checkBoxSizeClicked(id)}} 
                  text={value.size + " (" + value.products +")"} 
                  classes={checkStyleSizes}/>  
                </div>  
            </div>  
          )})}
      </div>
  )
}

export default Sizes