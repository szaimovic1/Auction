export const clearElement = (id) => {
    document.getElementById(id).innerHTML = ""
}

export const setElementValue = (id, value) => {
    document.getElementById(id).value = value
}

export const getElementValue = (id) => {
    return document.getElementById(id).value
}

export const getElement = (id) => {
    return document.getElementById(id)
}

export const msgResponse = (success = true, msg = "Success") => {
    return {
        isSuccess: success,
        message: msg
    }
}

export const msgLogout = (success = false, msg = "Logout", logout = true) => {
    return {
        isSuccess: success,
        message: msg,
        logout: logout
    }
}

export const getUrlParameter = (name, location) => {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    var results = regex.exec(location.search);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
  }

export const correctDate = (date) => {
    return new Date(date.getTime() - date.getTimezoneOffset() * 60000)
}
