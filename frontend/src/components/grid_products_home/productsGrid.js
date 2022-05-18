import './gridProductsHome.scss';
import ProductHome from '../product_home/productHome';
import { useCallback, useRef } from 'react';
import { PRODUCT_INFO_DISPLAY, FEATURED_PRODUCTS } from '@constants';

const GridProductsHome = ({
    products,
    hasMore,
    numberOfProducts,
    page,
    setPage = null, 
    display = null,
    setId = null,
    setDoClearData = null
  }) => {

  const observer = useRef()

  const lastProduct = useCallback(node => {
    if (observer.current) observer.current.disconnect()
    observer.current = new IntersectionObserver(entries => {
      if (entries[0].isIntersecting && hasMore) {
          setPage(prevPageNumber => prevPageNumber + 1)
      }
    })
    if (node) observer.current.observe(node)
  }, [hasMore, setPage])

  return (
    <div className="button-explore">
      <div id="products" className={display === null ? "grid-home-content" : display}>
        {
        products.map((value, index) => {
          if(products.length === index + 1 && (display === null 
              || display === FEATURED_PRODUCTS))
            return <ProductHome   
              key={index} 
              product={value}
              customRef={lastProduct}
              setId={setId}
              display={display === FEATURED_PRODUCTS ? display : null}
            ></ProductHome>
            else return <ProductHome   
              key={index} 
              product={value}
              display={display === "list-display" || 
                  display === PRODUCT_INFO_DISPLAY ||
                  display === FEATURED_PRODUCTS ? display : null}
              setId={setId}
            ></ProductHome>
        })}
      </div>
      {display !== null && display !== FEATURED_PRODUCTS 
        && (numberOfProducts > (page + 1) * 9) && 
          <button 
            onClick={() => {
              setDoClearData(false) 
              setPage(p => p+1)
            }}
            className="explore-more"
          >
            Explore more
          </button>
      }
    </div>
  )
}

export default GridProductsHome