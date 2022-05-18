import React, { useEffect, useState } from "react";
import {
  PaymentElement,
  useStripe,
  useElements
} from "@stripe/react-stripe-js";
import './payment.scss';
import { SERVER_UPDATE_PAYMENT } from "@endpoints";

const CheckoutForm = () => {
    const stripe = useStripe();
    const elements = useElements();
  
    const [message, setMessage] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
  
    useEffect(() => {
      if (!stripe) {
        return;
      }
  
      const clientSecret = new URLSearchParams(window.location.search).get(
        "payment_intent_client_secret"
      );
  
      if (!clientSecret) {
        return;
      }
  
      stripe.retrievePaymentIntent(clientSecret).then(({ paymentIntent }) => {
        switch (paymentIntent.status) {
          case "succeeded":
            setMessage("Payment succeeded!");
            break;
          case "processing":
            setMessage("Your payment is processing.");
            break;
          case "requires_payment_method":
            setMessage("Your payment was not successful, please try again.");
            break;
          default:
            setMessage("Something went wrong.");
            break;
        }
      });
    }, [stripe]);
  
    const handleSubmit = async (e) => {
      e.preventDefault();
  
      if (!stripe || !elements) {
        // Stripe.js has not yet loaded.
        // Make sure to disable form submission until Stripe.js has loaded.
        return;
      }
  
      setIsLoading(true);
  
      const { error } = await stripe.confirmPayment({
        elements,
        confirmParams: {
          return_url: process.env.REACT_APP_API_URL + SERVER_UPDATE_PAYMENT,
        },
      });
      if (error.type === "card_error" || error.type === "validation_error") {
        setMessage(error.message);
      } else {
        setMessage("An unexpected error occured.");
      }
  
      setIsLoading(false);
    };
  
    return (
        <div>
          <div className="jump-up-payment">
            <form id="payment-form" className="payment-form" onSubmit={handleSubmit}>
              <PaymentElement id="payment-element" className="payment-element" />
              <button disabled={isLoading || !stripe || !elements} id="submit" className="button-payment">
                <span id="button-text">
                  {isLoading ? <div id="spinner"></div> : "Pay now"}
                </span>
              </button>
              {message && <div id="payment-message" className="payment-message">{message}</div>}
            </form>
          </div>
          <div className="settings-overlay"></div>
        </div>
    );
}

export default CheckoutForm
