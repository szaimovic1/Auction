import CheckBox from '@components/check_box/checkBox';

const Colors = ({
    colors,
    checkBoxesColors,
    checkBoxColorClicked
  }) => {

    const checkStyleSizes = ["label-category-size" , 
          "checked-category", "unchecked-category"]

  return (
    <div className="shop-categories-container-size">
      <div className="title-filter">Colors</div>{
        colors.map((value, index) => {
          let id = value.color
          return (
            <div key={index} >
              <div id={id} key={index} >
                <CheckBox id={id} 
                  showing={checkBoxesColors.get(id)} 
                  clicked={() => {checkBoxColorClicked(id)}} 
                  text={value.color} 
                  classes={checkStyleSizes}/>  
                </div>  
            </div>  
        )})}
      </div>
  )
}

export default Colors