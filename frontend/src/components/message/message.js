import { MSG_ERROR, MSG_WIN } from '@constants';
import './message.scss';

const Message = ({ message, style }) => {
  return (
    <div className={style !== MSG_ERROR ? 
      style === MSG_WIN ? "msg-container" : "msg-container-notif" 
      : "msg-container-error"}>
        <div className={style}>{message}</div>
    </div>
  )
}

export default Message