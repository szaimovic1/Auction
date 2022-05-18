import './blackHeader.scss';
import SocialMedia from '@components/social_media/socialMedia';
import LogRegister from '@components/log_register/logRegister';
import Notifications from './notifications';
import { isAuthenticated } from "@utils/auth";

const BlackHeader = () => {
  return (
    <header className="black-header">
      <SocialMedia />
      {isAuthenticated() && <Notifications />}
      <LogRegister />
    </header>
  )
}

export default BlackHeader
