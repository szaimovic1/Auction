import { useState } from 'react';
import './gridProductsTypeHome.scss';
import { NEW_ARRIVALS, LAST_CHANCE } from '@constants';

const GridProductsTypeHome = ({ chooseType, onPage, oldType }) => {
    const [classBtn, setClassBtn] = useState([true, false])

    const btnClass = (value) => {
        switch(value) {
            case NEW_ARRIVALS:
                classBtn[0] = true
                classBtn[1] = false
                break
            default: 
                classBtn[1] = true
                classBtn[0] = false
                break
        }
        if(oldType !== value) {
            setClassBtn(classBtn)
            chooseType(value)
            onPage(0)
            document.getElementById("products").scrollTop = 0
        }
    }

  return (
    <>
    <div className="btn-container">
        <button id={NEW_ARRIVALS} 
            onClick={() => {btnClass(NEW_ARRIVALS)}} 
            className={classBtn[0] ? 
                "first-btn btn-style-chosen" : "first-btn"}>New Arrivals</button>
        <button id={LAST_CHANCE} 
            onClick={() => {btnClass(LAST_CHANCE)}} 
            className={classBtn[1] ? 
                "btn-style btn-style-chosen" : "btn-style"}>Last Chance</button>
    </div>
    <div className="line-btn"></div>
    </>
  )
}

export default GridProductsTypeHome