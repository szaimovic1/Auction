const Category = ({
    categoryArray,
    categoriesNames,
    checkBoxClicked
  }) => {

  return (
    <div className="tof-section tof-bigger">
      <div className="tof-title">Category</div>
      <div className={
          categoryArray.length === 0 ?
          "tof-all" : "tof-all-hide"}
      >
        All
      </div>
      <div className="tof-section-controller">{ 
        categoryArray.map((value, index) => {
          return (
            <div key={value}>
              <div className="tof-section-container" id={index}>
                <div className="tof-name" id={index}>{categoriesNames[index]}</div>
                <button className="tof-x" id={index} onClick={() =>
                    {checkBoxClicked(value)}}>x</button>
              </div>
            </div>
          )
        })
        }</div> 
        </div>
  )
}

export default Category