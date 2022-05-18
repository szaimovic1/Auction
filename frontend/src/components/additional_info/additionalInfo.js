import './additionalInfo.scss';

const AdditionalInfo = ({ leftText, rightText }) => {
  return (
    <div className="ai-container">
        <div className="ai-left">{leftText}</div>
        <div className={leftText ? "ai-right" : "ai-right-left"}>
            <div className="ai-right-pt1">{rightText.main}</div>
            <div className="ai-margin">{rightText.page}</div>
        </div>
    </div>
  )
}

export default AdditionalInfo