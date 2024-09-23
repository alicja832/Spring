export function getLogin(){
    return window.localStorage.getItem('login');
}

export function getRole(){
  return window.localStorage.getItem('role');
}

export function setLogin(login) {
    if (login !== null) {
      window.localStorage.setItem('login', login);
    } else {
      window.localStorage.removeItem('login');
    }
    return;
}


export function setRole(role) {
  if (role !== null) {
    window.localStorage.setItem('role', role);
  } else {
    window.localStorage.removeItem('role');
  }
  return;
}