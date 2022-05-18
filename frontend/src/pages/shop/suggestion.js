const Suggestion = ({
    suggestion,
    correction
  }) => {

  return (
    <div className="did-u-mean-container">
      <div className="did-u-mean">Did you mean?</div>
      <button
        className="did-u-mean-btn"
        onClick={() => {suggestion()}}
      >
        {correction}
      </button>
    </div>
  )
}

export default Suggestion