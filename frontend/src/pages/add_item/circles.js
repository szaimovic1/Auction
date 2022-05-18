const Circles = ({ page }) => {

  return (
    <div className="ai-circle-line">
      <div className={"ai-page-circle" + (page >= 0 ? " ai-circle-active" : "")}></div>
      <div className={"ai-page-line" + (page >= 1 ? " ai-line-active" : "")}></div>
      <div className={"ai-page-circle" + (page >= 1 ? " ai-circle-active" : "")}></div>
      <div className={"ai-page-line" + (page === 2 ? " ai-line-active" : "")}></div>
      <div className={"ai-page-circle" + (page === 2 ? " ai-circle-active" : "")}></div>
    </div>
  )
}

export default Circles