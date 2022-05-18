import './socialMedia.scss';
import { EntypoTwitterWithCircle } from 'react-entypo';
import { FacebookFill } from 'akar-icons';
import { Icon } from '@iconify/react';

const SocialMedia = () => {
  const link = "https://github.com/szaimovic1"
  const target = "_blank"
  const rel = "noreferrer noopener"

  return (
    <div className="social-div">
      <a href={link} target={target} rel={rel}>
        <FacebookFill className="facebook"/>
      </a>
      <a href={link} target={target} rel={rel}>
        <Icon icon="entypo-social:instagram-with-circle" className="instagram"/>
      </a>
      <a href={link} target={target} rel={rel}>
        <EntypoTwitterWithCircle className="twitter"/>
      </a>
    </div>
  )
}

export default SocialMedia
