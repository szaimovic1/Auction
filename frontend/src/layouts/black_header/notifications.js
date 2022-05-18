import { useState, useEffect } from 'react';
import { over } from 'stompjs';
import SockJS from 'sockjs-client';
import { getEmail } from "@utils/auth";
import Noty from './noty';

var stompClient = null;

const Notifications = () => {
    const [messages, setMessages] = useState([]);
    const [, setRefresh] = useState(false)

    useEffect(() => {  
      const onConnected = () => {
        console.log("Connected")
        //try-catch when token exp?
        stompClient.subscribe('/topic/message/' + getEmail(), onMessageReceived);
      }

      const onMessageReceived = (payload)=> {
        var payloadData = JSON.parse(payload.body);
        setMessages(m => m.concat(payloadData))
      }
  
      const onError = (err) => {
        console.log(err);
      }

      const connect = () => {
        let Sock = new SockJS(process.env.REACT_APP_API_URL + '/ws');
        stompClient = over(Sock);
        stompClient.connect({}, onConnected, onError);
      }

      connect()
    }, [])

    return (
      <div>
        <Noty 
          width={"25px"} 
          color={"white"} 
          count={messages.length} 
          messages={messages} 
          setRefresh={setRefresh}
        />
      </div>
    );
}

export default Notifications
