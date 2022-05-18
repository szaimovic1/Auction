import DatePicker from 'react-date-picker/dist/entry.nostyle';
import './calendar.css';
import './datePicker.css';
import TimePicker from 'react-time-picker';
import './time.scss';

const SetPrices = ({ 
    price,
    startDate, setStartDate,
    endDate, setEndDate,
    startTime, setStartTime,
    endTime, setEndTime
  }) => {
    const piInputs = "pi-inputs"
    const piLabel = "pi-label"

  return (
    <>
      <div className="add-item-txt">SET PRICES</div>
      <label className={piLabel}>Your start price</label>
      <div className="ai-circle-line">
        <div className={piInputs + " price-dollar"}>$</div>
        <input 
          className={piInputs}
          type="number"
          ref={price}
        />
      </div>
      <div className="ai-circle-line">
        <label className={piLabel + " pi-label-addr-left"}>Start date</label>
        <label className={piLabel + " pi-label-addr"}>End date</label>
      </div>
      <div className="ai-circle-line">
        <DatePicker
          minDate={new Date()}
          value={startDate}
          onChange={(v) => {setStartDate(v)}}
          locale="en-GB"
        />
        <div style={{marginRight: "5%"}}></div>
        <DatePicker
          minDate={new Date()}
          value={endDate}
          onChange={(v) => {setEndDate(v)}}
        />
      </div>
      <div className="set-prices-explanation">The auction will be automatically closed when the end time comes. The</div>
      <div className="set-prices-explanation">highest bid will win the auction.</div>
      <div className="ai-circle-line">
        <label className={piLabel + " pi-label-addr-left"}>Start time</label>
        <label className={piLabel + " pi-label-addr"}>End time</label>
      </div>
      <div className="ai-circle-line">
        <TimePicker
          value={startTime}
          onChange={(v) => {setStartTime(v)}}
          locale="en-GB"
        />
        <div style={{marginRight: "5%"}}></div>
        <TimePicker
          value={endTime}
          onChange={(v) => {setEndTime(v)}}
          locale="en-GB"
        />
      </div>
    </>
  )
}

export default SetPrices