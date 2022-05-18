import '@components/login_form/loginForm.scss';
import logoGoogle from '@assets/google.png';
import logoFb from '@assets/facebook.png';

const SocialLogin = ({ type }) => {
  return (
    <div className="oauth">
        <a className="button-left" href={process.env.REACT_APP_API_URL + "/oauth2/authorization/facebook"}>
            <img className="img-icon" src={logoFb} alt=""></img>
            {type} With Facebook
        </a>
        <a className="button-right" href={process.env.REACT_APP_API_URL + "/oauth2/authorization/google"}>
            <img className="img-icon" src={logoGoogle} alt=""></img>
            {type} With Gmail
        </a>
    </div>
  )
}

export default SocialLogin