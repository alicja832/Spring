export function getLogin(){
    return window.localStorage.getItem('login');
}

export function setLogin(login) {
    if (login !== null) {
      window.localStorage.setItem('login', login);
    } else {
      window.localStorage.removeItem('login');
    }
    return;
}