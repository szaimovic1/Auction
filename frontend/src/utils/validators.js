import validator from 'validator';
import { msgResponse } from '@utils/functions';

export const validateInputs = (value, type) => {
    let errorMsg = ""
    switch(type) {
      case 'name':
        if(!/^[a-zA-Z]+$/.test(value) || value.length === 0) errorMsg = "Name not valid!"
        return errorMsg
      case 'email':
        errorMsg = validator.isEmail(value) ? "" : "Email not valid!"
        return errorMsg 
      case 'password':
        if(value.length < 8) errorMsg = "Password must be at least 8 characters long!"
        else if(!value.match(".*[a-z].*")) errorMsg = "Password must contain at least one lowercase letter!"
        else if(!value.match(".*[A-Z].*")) errorMsg = "Password must contain at least one capital letter!"
        else if(!value.match(".*[0-9].*")) errorMsg = "Password must contain at least one number!"
        return errorMsg
      default: return errorMsg  
    }
}

export const validateProduct = (prod, file, startD, endD) => {
  if((startD < new Date()) || (startD >= endD)) {
    return msgResponse(false, "Incorrect date values!")
  }
  else if(prod.name === null || prod.name.length < 3 || prod.name.length > 100) {
    return msgResponse(false, "Name not valid!")
  }
  else if(prod.categoryId === null) {
    return msgResponse(false, "Missing category!")
  }
  else if(prod.description === null || prod.description.length < 3 || prod.description.length > 700) {
    return msgResponse(false, "Description not valid!")
  }
  else if(file === null || file.length < 3) {
    return msgResponse(false, "Load at least 3 images!")
  }
  else if(prod.price === null || prod.price < 0 || prod.price === "") {
    return msgResponse(false, "Bad price value!")
  }
  else if(checkStreet(prod.street)) {
    return msgResponse(false, "Incorrect street value!")
  }
  else if(validateInputs(prod.city, "name") !== "") {
    return msgResponse(false, "Incorrect city value!")
  }
  else if(validateInputs(prod.country, "name") !== "") {
    return msgResponse(false, "Incorrect country value!")
  }
  else if(validateInputs(prod.email, "email") !== "") {
    return msgResponse(false, "Incorrect email value!")
  }
  else if(checkPhoneNumber(prod.phone)) {
    return msgResponse(false, "Incorrect phone number!")
  }
  else if(checkZipCode(prod.zipCode)) {
    return msgResponse(false, "Incorrect zip code number!")
  }
  return msgResponse()
}

const checkStreet = (s) => {
  return s === null || s.length < 3 || s.length > 100
}

const checkZipCode = (zip) => {
  return !(zip !== null && /^[0-9]*$/.test(zip) && zip.length === 5)
}

const checkPhoneNumber = (num) => {
  return !(num !== null && /^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$/im.test(num))
}

export const checkErrorResponse = (error) => {
  //check if this network part will be needed
  if(error.message === "Network Error") {
    return msgResponse(false, "Problems with server!")
  }
  //more checks?
  if(error.response.data.message) {
    return msgResponse(false, error.response.data.message) 
  }
  return msgResponse(false, "Something went wrong!") 
}