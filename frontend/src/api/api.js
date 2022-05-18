import { SERVER_BASE } from '@constants';
import axios from 'axios';
import { configure, checkToken } from '@utils/auth';
import { msgResponse, msgLogout } from '@utils/functions';
import { checkErrorResponse } from '@utils/validators';

export const postData = async (path, body = null, params = null) => {
    if(checkToken()) {
        return msgLogout()
    }
    return await axios.post(SERVER_BASE + path, body, {
        params: params,
        headers: configure()
    }).then(function (response) {
        return response.data
    }).catch(function (error) {
        return checkErrorResponse(error)
  })
}

export const putData = async (path, body = null, params = null) => { 
    if(checkToken()) {
        return msgLogout()
    }
    return await axios.put(SERVER_BASE + path, body ,{
        params: params,
        headers: configure()
    }).then(function (response) {
        return response.data
    }).catch(function (error) {
        return checkErrorResponse(error)
  })
}

export const deleteData = async (path, params = null) => { 
    if(checkToken()) {
        return msgLogout()
    }
    return await axios.delete(SERVER_BASE + path ,{
        params: params,
        headers: configure()
    }).then(function () {
        return msgResponse()
    }).catch(function (error) {
        return checkErrorResponse(error)
  })
}

export const getData = async (path, params = null) => { 
    if(checkToken()) {
        return msgLogout()
    }
    return await axios.get(SERVER_BASE + path, {
        params: params,
        headers: configure()
    }).then(function (response) {
        return response.data
    }).catch(function (error) {
        return checkErrorResponse(error)
  })
}