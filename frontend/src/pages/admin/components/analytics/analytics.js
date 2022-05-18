import LineChartComponent from "./lineChart";
import { useState } from "react";
import PieChartComponent from "./pieChart";

const Analytics = () => {
  const lineChartId = "lineChart"
  const pieChartId = "pieChart"
  const [activeBtn, setActiveBtn] = useState(lineChartId)

  return (
    <div style={{marginTop: "10px", width: "100%"}}>
      <div 
        className="category-layout" 
        style={{
          height: "fit-content",
          width: "240px",
          marginRight: "20px"
        }}
      >
        <button 
          className="btn-focus" 
          style={activeBtn === lineChartId ?
            {fontWeight: "bold", color: "#8367D8"} : {}}
          onClick={() => {if(activeBtn !== lineChartId) {
            setActiveBtn(lineChartId)
          }}}
        >
          Login activity
        </button>
        <button 
          className="btn-focus" 
          style={activeBtn === pieChartId ?
            {fontWeight: "bold", color: "#8367D8"} : {}}
          onClick={() => {if(activeBtn !== pieChartId) {
            setActiveBtn(pieChartId)
          }}}
        >
          Age structure
        </button>
      </div>
      {activeBtn === lineChartId &&
        <LineChartComponent />
      }
      {activeBtn === pieChartId &&
        <PieChartComponent />
      }
    </div>
  )
}

export default Analytics
