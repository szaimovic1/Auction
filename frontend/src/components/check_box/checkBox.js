import '@components/login_form/loginForm.scss';
import '@pages/shop/categoryShop.scss';

const CheckBox = ({ clicked, showing, id, text = "Remember Me", classes = null }) => {
  let originalClasses = ["remember-label", "check", "unchecked"]
  if(classes !== null) originalClasses = classes

  return (
    <label className={originalClasses[0]}>
        {showing ? <button id={id} type="button" 
            className={originalClasses[1]} onClick={clicked}>&#x2611;</button>
        : <button id={id} type="button" 
            className={originalClasses[2]} onClick={clicked}></button>}   
        {text}
    </label>
  )
}

export default CheckBox