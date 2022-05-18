const Size = ({
    sizeArray,
    checkBoxSizeClicked
  }) => {

  return (
    <div className="tof-section tof-smaller">
      <div className="tof-title">Size</div>
      <div className={
        sizeArray.length === 0 ? 
        "tof-all" : "tof-all-hide"
      }>
        All
      </div>
      <div className="tof-section-controller">
        {
          sizeArray.map((value, index) => {
            return (
              <div key={value}>
                <div id={index} className="tof-section-container">
                    <div className="tof-name" id={index}>{value}</div>
                    <button className="tof-x" id={index} onClick={() =>
                        {checkBoxSizeClicked(value)}}>x</button>
                </div>
              </div>
            )
          })
        }
      </div>
    </div>
  )
}

export default Size