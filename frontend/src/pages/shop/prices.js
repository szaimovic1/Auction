import ReactSlider from 'react-slider';

const Prices = ({
    filterStartPrice,
    filterEndPrice,
    startPrice,
    endPrice,
    handleSliderChange
  }) => {
      
  return (
    <div className="shop-categories-container-size">
      <div className="title-filter">Price Range</div>
      <div className="price-range-div">
        <div className="price-range-left">${filterStartPrice ? filterStartPrice : 0}</div>
        <div className="price-range-divider">-</div>
        <div className="price-range-right">${filterEndPrice ? filterEndPrice : 0}</div>
      </div>
      {filterStartPrice && filterEndPrice && 
        <ReactSlider 
          className="horizontal-slider"
          thumbClassName="example-thumb"
          trackClassName="example-track"
          defaultValue={[filterStartPrice, filterEndPrice]}
          renderThumb={(props) => <div {...props}></div>}
          pearling
          minDistance={10}
          min={filterStartPrice}
          max={filterEndPrice}
          onChange={handleSliderChange}
          value={[startPrice !== 0 ? startPrice : filterStartPrice,
              endPrice !== 0 ? endPrice : filterEndPrice]}
        />
      }
    </div>
  )
}

export default Prices