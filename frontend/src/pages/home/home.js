import { useState, useEffect } from 'react';
import CategoryMenuHome from '@components/category_menu_home/categoryMenuHome';
import './home.scss';
import { Navigate, useNavigate } from 'react-router';
import HighlightedProduct from '@components/highlighted_product/highlightedProduct';
import GridProductsTypeHome from '@components/grid_products_type_home/gridProductsTypeHome';
import GridProductsHome from '@components/grid_products_home/productsGrid';
import { FEATURED_PRODUCTS, LOGOUT, NEW_ARRIVALS } from '@constants';
import { isAuthenticated } from '@utils/auth';
import { getData } from '@api/api';
import { SERVER_RECOMMENDED, SERVER_PRODUCT } from '@endpoints';

const Home = () => {
  const [category, setCategory] = useState("")
  const [type, setType] = useState(NEW_ARRIVALS)
  const navigate = useNavigate()

  const [page, setPage] = useState(0)
  const [products, setProducts] = useState()
  const [hasMore, setHasMore] = useState(false)
  const [numberOfProducts, setNumberOfProducts] = useState(0)

  const [pageRecommended, setPageRecommended] = useState(0)
  const [productsRec, setProductsRec] = useState([])
  const [hasMoreRec, setHasMoreRec] = useState(false)
  const [numberOfProductsRec, setNumberOfProductsRec] = useState(0)

  useEffect(() => {
    async function fetchData() {
      const params = {
        page: pageRecommended,
        prods: 4
      }
      const data = await getData(SERVER_RECOMMENDED, params)
      if(data.logout) {
        navigate(LOGOUT)
      }
      else if(data.isSuccess) {
        setProductsRec(p =>  p.concat(data.products))
        setHasMoreRec(data.products.length > 0)
        setNumberOfProductsRec(data.numberOfProducts)
      }
    }
    if(isAuthenticated()) {
      fetchData()
    }
  }, [pageRecommended, navigate])

  useEffect(() => {
    async function fetchData() {
      let asc = 1
      let field = "endDate"
      if(type === NEW_ARRIVALS) {
        field = "startDate"
        asc = 0
      }
      const params = {
        categories: "",
        colors: "",
        sizes: "",
        startPrice: 0,
        endPrice: 0,
        page: page,
        prods: 8,
        field: field,
        asc: asc
      }
      const data = await getData(SERVER_PRODUCT, params)
      if(data.logout) {
        navigate(LOGOUT)
      }
      else if(data.isSuccess) {
        if(page !== 0) {
          setProducts(p =>  p.concat(data.products))
        }
        else {
          setProducts(data.products)
        }
        setHasMore(data.products.length > 0)
        setNumberOfProducts(data.numberOfProducts)
      }
    }  
    fetchData() 
  }, [navigate, page, type])
  
  return (
    <div className="home-container">
      {category !== "" && <Navigate to="/shop" state={{id: category, name: null, new: false}} />}
      <div className="category-heighlight-container">
        <CategoryMenuHome category={setCategory} />
        <HighlightedProduct /> 
      </div>
      {isAuthenticated() && productsRec.length !== 0 &&
        <>
          <div className="only-btn">Recommended Products</div>
          <div className="line-btn"></div>
          <GridProductsHome 
            products={productsRec}
            hasMore={hasMoreRec}
            numberOfProducts={numberOfProductsRec}
            page={pageRecommended} 
            setPage={setPageRecommended} 
            display={FEATURED_PRODUCTS}
          />
        </>
      }
      <GridProductsTypeHome 
        chooseType={setType} 
        onPage={setPage} 
        oldType={type} 
      />
      {products &&
        <GridProductsHome 
          products={products}
          hasMore={hasMore}
          numberOfProducts={numberOfProducts}
          page={page} 
          setPage={setPage} 
        />
      }
    </div>
  )
}

export default Home