const Color = ({
    colorArray,
    checkBoxColorClicked
  }) => {

  return (
    <div className="tof-section tof-smaller">
      <div className="tof-title">Color</div>
      <div className={
        colorArray.length === 0 ? 
        "tof-all" : "tof-all-hide"
       }>
        All
      </div>
      <div className="tof-section-controller">
        {
          colorArray.map((value, index) => {
            return (
              <div key={value}>
                <div id={index} className="tof-section-container">
                  <div className="tof-name" id={index}>{value}</div>
                  <button className="tof-x" id={index} onClick={() =>
                      {checkBoxColorClicked(value)}}>x</button>
                </div>
              </div>
            )
          })
        }
      </div>
    </div>
  )
}

export default Color