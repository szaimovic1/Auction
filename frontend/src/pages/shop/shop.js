import { useLocation, useNavigate } from "react-router-dom";
import './shop.scss';
import { useState, useEffect } from 'react';
import GridProductsHome from "@components/grid_products_home/productsGrid";
import SortGridList from "@components/sort_grid_list/sortGridList";
import { ALL, LOGOUT } from "@constants";
import './tableOfFilters.scss';
import './categoryShop.scss';
import AdditionalInfo from "@components/additional_info/additionalInfo";
import { SERVER_SIZE, SERVER_COLOR, SERVER_CATEGORY, 
  SERVER_PRODUCT, SERVER_CATEGORY_PATHNAME,
  SERVER_SEARCH_CORRECT} from "@endpoints";
import { setElementValue } from "@utils/functions";
import { getData } from "@api/api";
import Sizes from "./sizes";
import Categories from "./categories";
import Prices from "./prices";
import Suggestion from "./suggestion";
import Colors from "./colors";
import Category from "./filter_list/category";
import Price from "./filter_list/price";
import Size from "./filter_list/size";
import Color from "./filter_list/color";

const Shop = () => {
  const location = useLocation()
  const navigate = useNavigate()
  const [type, setType] = useState((location.state === null ||
      location.state.id === null || location.state === ALL) 
      ? ALL : location.state.id.toString())
  const [typeSize, setTypeSize] = useState("")
  const [typeColor, setTypeColor] = useState("")
  const [startPrice, setStartPrice] = useState(0)
  const [endPrice, setEndPrice] = useState(0)
  const [page, setPage] = useState(0)
  const [display, setDisplay] = useState("grid-display")
  const [field, setField] = useState("name")
  const [asc, setAsc] = useState(1)
  const [clearData, setClearData] = useState(true)
  const [correction, setCorrection] = useState(null)
 
  const [categoriesNames, setCategoriesNames] = useState([]) 
  const categoryArray = type !== ALL && type !== "" ? type.split(",") : []
  const sizeArray = typeSize === "" ? [] : typeSize.split(",")
  const colorArray = typeColor === "" ? [] : typeColor.split(",")

  const [categories, setCategories] = useState([])
  const [sizes, setSizes] = useState([])
  const [colors, setColors] = useState([])
  const [showHideBtns, setShowHideBtns] = useState(new Map())
  const [checkBoxes, setCheckBoxes] = useState(new Map())
  const [checkBoxesSizes, setCheckBoxesSizes] = useState(new Map())
  const [checkBoxesColors, setCheckBoxesColors] = useState(new Map())       
  const [startingType, ] = useState(type)  
  const [filterStartPrice, setFilterStartPrice] = useState(null)
  const [filterEndPrice, setFilterEndPrice] = useState(null)
  const [search, setSearch] = useState((location.state 
      && location.state.name) ? location.state.name : "")
  const [searchFix, setSearchFix] = useState(false)

  const [products, setProducts] = useState()
  const [hasMore, setHasMore] = useState(false)
  const [numberOfProducts, setNumberOfProducts] = useState(0)

  useEffect(() => {
    async function fetchData() {
      const params = {
        type: ALL
      }
      const data = await getData(SERVER_CATEGORY, params)
      const newBtns = new Map()
      let newCB = new Map()
      if(data.isSuccess === undefined) {
        setCategories(data)
        data.forEach(element => {
          newBtns.set(element.id.toString(), false)
          newCB.set(element.id.toString(), false)    
          element.subcategories.forEach(e => {
            newCB.set(e.id.toString(), false)
          })
        })
        newCB.set(ALL, false)
        newCB.set(startingType, true)   
        setShowHideBtns(newBtns)
        setCheckBoxes(newCB)
      }
      const dataSizes = await getData(SERVER_SIZE)
      if(dataSizes.isSuccess === undefined) {
        setSizes(dataSizes)
        newCB = new Map()
        dataSizes.forEach(element => {
          newCB.set(element.size, false)    
        })
        setCheckBoxesSizes(newCB)
      }
      const dataColors = await getData(SERVER_COLOR)
      if(dataColors.isSuccess === undefined)  {
        setColors(dataColors)
        newCB = new Map()
        dataColors.forEach(element => {
          newCB.set(element.color, false)    
        })
        setCheckBoxesColors(newCB) 
      }
    }
    fetchData()
  }, [startingType]);

  const resetPriceTag = () => {
    setStartPrice(filterStartPrice)
    setEndPrice(filterEndPrice)
  }

  const clearSearch = () => {
    if(document.getElementById("clearSearch")) {
      document.getElementById("clearSearch").click()
    }
  }

  useEffect(() => {
    return () => {
      clearSearch()   
    }
  }, [])

  useEffect(() => {
    async function fetchData() {
      if(search !== "" && searchFix) {
        const data = await getData(SERVER_SEARCH_CORRECT,
          {search: search})
        if(data.isSuccess === undefined) {
          setCorrection(data)
        } 
      }
      else {
        setCorrection(null)
      }
    }
    fetchData()
  }, [searchFix, search])

  useEffect(() => {
    async function fetchData() {
      const params = {
        categories: type === ALL ? "" : type,
        colors: typeColor,
        sizes: typeSize,
        startPrice: startPrice,
        endPrice: endPrice,
        page: page,
        prods: 9,
        field: field,
        asc: asc,
        search: search
      }
      const data = await getData(SERVER_PRODUCT, params)
      if(data.logout) {
        //setting start prices to 0?
        navigate(LOGOUT)
      }
      else if(data.isSuccess) {
        if(!clearData) {
          setProducts(p =>  p.concat(data.products))
        }
        else {
          setProducts(data.products)
        }
        setHasMore(data.products.length > 0)
        setNumberOfProducts(data.numberOfProducts)
        if(data.startPrice && (filterStartPrice === null 
            || filterStartPrice !== data.startPrice)) {
          setFilterStartPrice(data.startPrice)
          setStartPrice(0)
        }
        if(data.endPrice && (filterEndPrice === null 
          || filterEndPrice !== data.endPrice)) {
          setFilterEndPrice(data.endPrice)
          setEndPrice(0)
        }
        if(data.products !== null && data.products.length === 0 &&
            data.numberOfProducts === 0) {
          setSearchFix(true)
        }
        else {
          setSearchFix(false)
        }
      } 
    }
    fetchData()
  }, [type, typeSize, typeColor, search, startPrice, 
        endPrice, page, field, asc, filterStartPrice, 
        filterEndPrice, clearData, navigate])

  useEffect(() => {
    setStartPrice(0)
    setEndPrice(0)
  }, [type, typeSize, typeColor, search])

  useEffect(() => {
    async function fetchData() {
      const params = {
        categories: type === ALL ? "" : type
      }
      const names = await getData(SERVER_CATEGORY_PATHNAME, params)
      if(names.isSuccess === undefined) {
        setCategoriesNames(names)
      }
    }
    fetchData()
  }, [type])

  const btnClicked = (id) => {
    const newData = new Map()  
    for (const [key, value] of showHideBtns.entries()) {
      if(id === key)
          newData.set(key, !value)
      else newData.set(key, value)
    }
    setShowHideBtns(newData)     
  }  

  const rewriteMap = (id, clearAll, cb) => {
    const newData = new Map()
    let add = false
    for (const [key, value] of cb.entries()) {
      if(clearAll) {
        newData.set(key, false)
      }
      else if(id === key) {
        newData.set(key, !value) 
        if(!value) {
          add = true
        }
      }   
      else {
        newData.set(key, value)
      }
    }
    return {newData: newData, add: add} 
  }

  const checkBoxClicked = (id, clearAll = false) => {
    const data = rewriteMap(id, clearAll, checkBoxes)
    const add = data.add
    setCheckBoxes(data.newData) 
    refresh()
    if(clearAll) {
      setType((location.state && location.state.new) 
          ? location.state.id.toString() : ALL)
      return
    }
    if(add) {
      setType(type === ALL ? id : (type + "," + id))
    }
    else {
      if(type.includes(",")) {
        setType(removeType(type, id))
      }
      else {
        setType(ALL)
      }
    }   
  }
  
  const checkBoxSizeClicked = (id, clearAll = false) => {
    const data = rewriteMap(id, clearAll, checkBoxesSizes)
    const add = data.add     
    setCheckBoxesSizes(data.newData) 
    refresh()
    if(clearAll) {
      setTypeSize("")
      return
    }
    if(add) {
      setTypeSize(s => id + (s === "" ? "" : ",") + s)
    } 
    else {
      setTypeSize(s => removeType(s, id))
    }  
  }

  const checkBoxColorClicked = (id, clearAll = false) => {
    const data = rewriteMap(id, clearAll, checkBoxesColors)
    const add = data.add        
    setCheckBoxesColors(data.newData) 
    refresh()
    if(clearAll) {
      setTypeColor("")
      return
    }
    if(add) {
      setTypeColor(s => id + (s === "" ? "" : ",") + s)
    } 
    else {
      setTypeColor(s => removeType(s, id))
    }  
  }

  const refresh = () => {
    setPage(0)
    setClearData(true)
  }

  const removeType = (previous, newType) => {
    return previous.replace(newType, "")
              .replace(",,", ",")
              .replace(/^\,/, "")
              .replace(/\,$/, "")
  }

  const handleSliderChange = (v) => {
    refresh()
    if(v[0] !== startPrice) {
      setStartPrice(v[0])
    }
    if(v[1] !== filterEndPrice) {
      setEndPrice(v[1])
    }
  }

  const clearAll = () => {
    checkBoxClicked(0, true)
    checkBoxColorClicked(0, true)
    checkBoxSizeClicked(0, true)
    setStartPrice(filterStartPrice)
    setEndPrice(filterEndPrice)
    clearSearch()
  }

  useEffect(() => {
    if(location.state && (location.state.name
         || location.state.name === "")) {
      setSearch(location.state.name)
    }
  }, [location.state])  

  const suggestion = () => {
    setSearchFix(false)
    setSearch(correction)
    setCorrection(null)
    setElementValue("search-input", correction)
  }

  return (
    <div>
      {location.state && location.state.name && 
        <>
          {correction &&
            <Suggestion 
              suggestion={suggestion}
              correction={correction}
            />
          }
          <AdditionalInfo 
            leftText={null} 
            rightText={{
              main: "Home ->", 
              page: "Search result for " + search
            }} 
          />
        </>
      }
      <div className="shop-container">
        <div>
          <Categories 
            categories={categories}
            checkBoxes={checkBoxes}
            checkBoxClicked={checkBoxClicked}
            btnClicked={btnClicked}
            showHideBtns={showHideBtns}
          />
          <Prices 
            filterStartPrice={filterStartPrice}
            filterEndPrice={filterEndPrice}
            startPrice={startPrice}
            endPrice={endPrice}
            handleSliderChange={handleSliderChange}
          />
          <Sizes 
             sizes={sizes}
             checkBoxesSizes={checkBoxesSizes}
             checkBoxSizeClicked={checkBoxSizeClicked}
           />
          <Colors 
            colors={colors}
            checkBoxesColors={checkBoxesColors}
            checkBoxColorClicked={checkBoxColorClicked}
          />
        </div>
        <div>
        <div className="table-of-filters">
          <Category 
            categoryArray={categoryArray}
            categoriesNames={categoriesNames}
            checkBoxClicked={checkBoxClicked}
          />
          <Price 
            filterStartPrice={filterStartPrice}
            filterEndPrice={filterEndPrice}
            startPrice={startPrice}
            endPrice={endPrice}
            resetPriceTag={resetPriceTag}
          />
          <Size 
            sizeArray={sizeArray}
            checkBoxSizeClicked={checkBoxSizeClicked}
          />
          <Color 
            colorArray={colorArray}
            checkBoxColorClicked={checkBoxColorClicked}
          />
          <div className="tof-section-controller">
            <div className="tof-section-container-2">
              <div className="tof-name">Clear all</div>
              <button 
                id={"button-clear"} 
                className="tof-x" 
                onClick={clearAll}
              >
                x
              </button>
            </div>
          </div>
          </div>
          <SortGridList 
            display={display} 
            setDisplay={setDisplay}
            setField={setField} 
            setPage={setPage} 
            setAsc={setAsc} 
            clearData={setClearData} 
          />
          {products &&
            <GridProductsHome 
              products={products}
              hasMore={hasMore}
              numberOfProducts={numberOfProducts}
              page={page} 
              setPage={setPage}
              display={display} 
              setDoClearData={setClearData}
            />}
        </div>
      </div>
    </div>
  )
}

export default Shop