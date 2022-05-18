import { JWT, TYPE, NAME } from '@constants';
import jwt_decode from 'jwt-decode';

export const isAuthenticated = () => {
    return localStorage.getItem(JWT) !== null || sessionStorage.getItem(JWT) !== null
}

export const saveUser = (token, name) => {
    const type = localStorage.getItem(TYPE)
    if( type === null || type === "local"){
        localStorage.setItem(JWT, token)
        localStorage.setItem(NAME, name)
    }
    else{
        sessionStorage.setItem(JWT, token)
        sessionStorage.setItem(NAME, name)
    }
}

export const configure = () => {
    if(!isAuthenticated())
        return { 
            Accept : 'application/json' 
        }
    return { 
        Accept : 'application/json',
        Authorization: `Bearer ${decideTokenValue()}`  
    }
}

export const decideTokenValue = () => {
    return localStorage.getItem(JWT) ? localStorage.getItem(JWT) : sessionStorage.getItem(JWT)
}

export const tokenCheck = (error, setLogout) => {
    if(error.response.data.trace.includes("ExpiredJwtException")) {
        setLogout(true)
        return true
    }
}

export const getEmail = () => {
    return localStorage.getItem(JWT) ? jwt_decode(localStorage.getItem(JWT)).sub : jwt_decode(sessionStorage.getItem(JWT)).sub
}

export const isAdmin = () => {
    const roles = localStorage.getItem(JWT) ? 
      jwt_decode(localStorage.getItem(JWT)).roles : 
      jwt_decode(sessionStorage.getItem(JWT)).roles
    for(let i = 0; i < roles.length; i++) {
        if(roles[i].authority === "ROLE_ADMIN") {
            return true;
        }
    }
    return false;
}

export const isTokenExpiered = () => {
    try {
      return localStorage.getItem(JWT) ?
        Date.now() > jwt_decode(localStorage.getItem(JWT)).exp * 1000 : 
        Date.now() > jwt_decode(sessionStorage.getItem(JWT)).exp * 1000
    }
    catch(e) {
        return true
    }
}

export const checkToken = () => {
    if(isAuthenticated() && isTokenExpiered()) {
        return true
    }
    return false
}
