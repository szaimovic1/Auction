import './sortGridList.scss';
import { useState } from 'react';

const SortGridList = ({ display, setDisplay, setField, setPage, setAsc, clearData }) => {
    const [dropdown, setDropdown] = useState("Default Sorting")
    const [list, setList] = useState(false)
    const classBtn = "btn-sort"
    
    const sort = (event, id, asc = 1) => {
        setField(id)
        setting(event.target.innerText)
        setAsc(asc)
    }

    const setting = (textValue) => {
        setDropdown(textValue)
        setList(false)
        clearData(true)
        setPage(0)
    }

  return (
    <div className="container-sortgridlist">
        <button className="btn-main-sort" onClick={() => {setList(!list)}}>{dropdown} {"⮟"}</button>
        <div className={list ? "dropdown-bootstrap" : "dropdown-hide"}>
          <div className="block-display">
            <button className={classBtn} onClick={(e) => {sort(e, "name")}}>Default Sorting</button>
            <button className={classBtn} onClick={(e) => {sort(e, "price", 0)}}>Sort by price &#8595;</button>
            <button className={classBtn} onClick={(e) => {sort(e, "price")}}>Sort by price &#8593;</button>
            <button className={classBtn} onClick={(e) => {sort(e, "startDate", 0)}}>New to Old</button>
            <button className={classBtn} onClick={(e) => {sort(e, "endDate")}}>Sort by time left</button>
          </div>
        </div>
        <div className="grid-list-display">
          <button className={display === "grid-display" ? 
                "selected-btn" : "not-selected"}
                onClick={() => {setDisplay("grid-display")}}>&#x25a6; Grid</button>
          <button className={display === "list-display" ? 
                "selected-btn" : "not-selected"}
                onClick={() => {setDisplay("list-display")}}>&#x2630; List</button>
        </div>
    </div>
  )
}

export default SortGridList