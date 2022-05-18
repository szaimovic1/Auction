import { Icon } from '@iconify/react';
import { SERVER_CATEGORY } from '@endpoints';
import { ALL, LOGOUT } from '@constants';
import { useState, useCallback, useEffect } from 'react';
import { getData } from "@api/api";
import { useDropzone } from 'react-dropzone';
import { useNavigate } from 'react-router';

const AddItemPage = ({ 
        file, setFile,
        setRefresh,
        name,
        description,
        category, setCategory,
        categoryName, setCategoryName,
        setSubcategory,
        subcategoryName, setSubcategoryName
    }) => {
    const piInputs = "pi-inputs"
    const piLabel = "pi-label"
    const [categories, setCategories] = useState()
    const [subcategories, setSubcategories] = useState()
    const [showMC, setShowMC] = useState(false)
    const [showSC, setShowSC] = useState(false)
    const [subcategoryHolder, ] = useState(category) 
    const navigate = useNavigate()

    useEffect(() => {
      async function fetchData() {
        const params = {
          type: ALL
        }
        const data = await getData(SERVER_CATEGORY, params)
        if(data.logout) {
          navigate(LOGOUT)
        }
        else if(data.isSuccess === undefined) {
          setCategories(data);
          if(subcategoryHolder) {
            for(const c of data) {
                if(c.id === subcategoryHolder) {
                  setSubcategories(c.subcategories)
                }
            }  
          }          
        }
      }
      fetchData()
    }, [subcategoryHolder, navigate])

    const onDrop = useCallback(async (files) => {
      setFile(files)
    }, [setFile])
    const {getRootProps, getInputProps} = useDropzone({onDrop})
    
    const removeFile = (index) => {
      file.splice(index, 1)
      setRefresh(r => !r)
    }

  return (
    <>
      <div className="add-item-txt">ADD ITEM</div>
      <label className={piLabel}>What do you sell?</label>
      <input 
        className={piInputs}
        type="text"
        ref={name}
        placeholder="Enter a product name" 
      />
      <div className={"date-birth-container"}>
        <div className={"ai-category-div pi-label-addr-left"}>
          <button className={"add-item-category"}
            onClick={() => {setShowMC(!showMC)}}>  
            {categoryName ? categoryName : "Select Category"}
            <Icon style={{float: "right"}}
                width="30"
                height="24" 
                icon="ri:arrow-drop-down-line" />
          </button>
          {categories && showMC &&
            <div className={"day-birth-dropdown ai-dropdown-width"}>{
            categories.map((value) => {
              return(
                <button className={"pi-btn-dropdown"} 
                  onClick={() => {
                    setCategory(value.id) 
                    setCategoryName(value.name)
                    setShowMC(false)
                    setSubcategories(value.subcategories)}}
                  key={value.name}>{value.name}</button>
              )
            })}</div>
          }
        </div>
        <div className={"ai-category-div"}>
          <button className={"add-item-category"}
            onClick={() => {setShowSC(!showSC)}}>  
            {subcategoryName ? subcategoryName : "Select Subcategory"}
            <Icon style={{float: "right"}}
                width="30"
                height="24" 
                icon="ri:arrow-drop-down-line" />
          </button>
          {subcategories && showSC &&
            <div className={"day-birth-dropdown ai-dropdown-width"}>{
            subcategories.map((value) => {
              return(
                <button className={"pi-btn-dropdown"} 
                  onClick={() => {
                    setSubcategory(value.id) 
                    setSubcategoryName(value.name)
                    setShowSC(false)}}
                  key={value.name + value.id}>{value.name}</button>
              )
            })}</div>
          }
        </div>
      </div>
      <label className={piLabel}>Description</label>
      <textarea
        className={piInputs + " pi-inputs-description"}
        ref={description}
      />
      <div className="description-capacity">100 words (700 characters)</div>
      <div {...getRootProps()} className = "add-item-fu-container">
        <input {...getInputProps()} />
        <div className="ai-align">
          <div className="upload-photos">Upload photos</div>
          <div className="drag-drop">or just drag and drop</div>
          <div className="num-photos">(Add at least 3 photos)</div>
        </div>
      </div>
      {file && 
        file.map((value, index) => {
          return(
            <div key={index}
              className="tof-section-container">
              <div className="tof-name">{value.name}</div>
              <button className="tof-x" onClick={() =>
                {removeFile(index)}}>x</button>
            </div>
          )
        })
      }
    </>
  )
}

export default AddItemPage