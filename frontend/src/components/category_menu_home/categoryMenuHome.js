import { getData } from '@api/api';
import { useState, useEffect } from 'react';
import { ALL } from '@constants';
import './categoryMenuHome.scss';
import { SERVER_CATEGORY } from '@endpoints';

const CategoryMenuHome = ( {category} ) => {
    const [categories, setCategories] = useState([])

    useEffect(() => {
      async function fetchData() {
        const params = {
          type: "main"
        }
        const data = await getData(SERVER_CATEGORY, params)
        if(data.isSuccess === undefined) {
          setCategories(data)
        }
      }
      fetchData()
  }, [])

  return (
    <div className="category-layout">
        <div className="category">CATEGORIES</div>{
            categories.map((value, index) => {
                return <button 
                  id={value.id}
                  className="btn-focus" 
                  key={index} 
                  onClick={() => {category(value.id)}}>{value.name}</button>
        })}
        <button 
            className="btn-focus" 
            onClick={() => {category(ALL)}}>All categories</button>  
    </div>
  )
}

export default CategoryMenuHome