import CheckBox from '@components/check_box/checkBox';

const Categories = ({
    categories,
    checkBoxes,
    checkBoxClicked,
    btnClicked,
    showHideBtns
  }) => {

    const checkStyle = ["label-category", 
          "checked-category", "unchecked-category"] 

  return (
    <div className="shop-categories-container">
      <div className="title-filter">PRODUCT CATEGORIES</div>{
        categories.map((value, index) => {
          let id = value.id.toString()
          return (
            <div key={index} >
              <div className="buttons-categories">
              <button 
                id={id} 
                className={checkBoxes.get(id) ? "basic-active" : "basic-buttons"}
                onClick={() => {checkBoxClicked(id)}}>{value.name}</button>
              <button id={id} 
                className="right-buttons" 
                onClick={() => {btnClicked(id)}}>{showHideBtns.get(id) ? "-" : "+"}</button>
              </div>  
              <div className={showHideBtns.get(id) ? "show-subcategories" 
                    : "hide-subcategories"}>
              {
                value.subcategories.map((subValue, subIndex) => {
                  let subId = subValue.id.toString()
                  return (
                    <div id={id} key={subIndex} >
                      <CheckBox id={subId} 
                        showing={checkBoxes.get(subId)} 
                        clicked={() => {checkBoxClicked(subId)}} 
                        text={subValue.name + " (" + subValue.products +")"} 
                        classes={checkStyle}/>  
                    </div>  
                )})
              }
              </div>
            </div>  
          )})}
        </div>
  )
}

export default Categories