import './footer.scss';
import { Link } from "react-router-dom";
import SocialMedia from '@components/social_media/socialMedia';

const Footer = () => {
    const boxContent = "box-content"
    const boxContentDarker = "box-content-dark"

  return (
    
    <footer className="black-footer">
      <div className="left-box">
        <p className={boxContentDarker}>AUCTION</p>
        <Link className={boxContent} to="/about-us">About us</Link>
        <Link className={boxContent} to="/terms-and-conditions">Terms and Conditions</Link>
        <Link className={boxContent} to="/privacy-policy">Privacy and Policy</Link>
      </div>
      <div className="right-box">
        <p className={boxContentDarker}>GET IN TOUCH</p>
        <p className={boxContent}>Call Us at +123 797-567-2535</p>
        <p className={boxContent}>support@auction.com</p>
        <div className="social-replace">
          <SocialMedia />
        </div>
      </div>
    </footer>
    
  )
}

export default Footer