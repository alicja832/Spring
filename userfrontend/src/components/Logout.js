import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { MyParticles } from './MyParticles'
import { setLogin } from './api/TokenService';
import ParentComponent from './ParentComponent';

export default function Logout(){
  
  const navigate = useNavigate();
  useEffect(() => {
    // Fetch user profile data
    setLogin(null);
  });

  return <ParentComponent/>;
};


