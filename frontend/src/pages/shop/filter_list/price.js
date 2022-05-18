const Price = ({
    filterStartPrice,
    filterEndPrice,
    startPrice,
    endPrice,
    resetPriceTag
  }) => {

  return (
    <div className="tof-section">
      <div className="tof-title">Price range</div>
      {((startPrice !== 0 && startPrice !== filterStartPrice) ||
       (endPrice !== 0 && endPrice !== filterEndPrice)) 
        ? <div className="tof-section-container">
            <div className="tof-name">
              ${startPrice ? startPrice :
              (filterStartPrice ? filterStartPrice : 0)}
              -
              ${endPrice ? endPrice :
              (filterEndPrice ? filterEndPrice : 0)}</div>
            <button className="tof-x" onClick={() => {resetPriceTag()}}>x</button>
          </div> 
        : <div className="tof-all">All</div>
        }
    </div>
  )
}

export default Price