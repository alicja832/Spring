import React, { useState,useEffect } from 'react';
import DrawerAppBar from './DrawerAppBar';
import Register from './Register';
import { getLogin } from './api/TokenService'; 

export default function ParentComponent() {
   
  const [navItems,setNavItems] = useState(['Zadania', 'Główna','Zaloguj','Zarejestruj']);
  const [purposes,setPurposes] = useState(["/tasks","/","/login","/register"]);
  const [register, setRegister] = useState('');
  
  
  useEffect(()=>{
    console.log(register);
    if(getLogin() && !navItems.includes('Profil'))
    {
        navItems.unshift('Profil');
        purposes.unshift("/profil");
        navItems.push("Wyloguj");
        purposes.push("/logout");
        setNavItems(navItems.filter((element) => element!=='Zaloguj'&& element!=='Zarejestruj'));
        setPurposes(purposes.filter((element) => element!=="/login" && element!=="/register"));
    }
  })
  
  // Funkcja zmieniająca stan, co spowoduje ponowne renderowanie ChildComponent
    function changeProperties()
    {
        console.log(navItems);
        navItems.unshift('Profil');
        purposes.unshift("/profil");
        navItems.push("Wyloguj");
        purposes.push("/logout");
        setNavItems(navItems.filter((element) => element!=='Zaloguj'&& element!=='Zarejestruj'));
        setPurposes(purposes.filter((element) => element!=="/login" && element!=="/register"));

        setRegister("fff");
        //this.forceUpdate();
    }
//chce wyrenderowac wszystkie rzeczy!!!
  return (
    <div>
      <DrawerAppBar register = {register}/> 
      <Register changeProperties={changeProperties}/>
      </div>
  );
}