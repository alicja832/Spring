import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
// import LoginInformation from '../api/LoginInformation'
const Profile = () => {
  const navigate = useNavigate();

  useEffect(() => {
    // Fetch user profile data
    const fetchProfile = async () => {
     
       
          //const role = LoginInformation.getLoggedInRole();
          const role = "Teacher";
          console.log(role);
          if (role === "teacher") {
            
              navigate('/teacherprofil');
          } else {
             
              navigate('/studentprofil');
          }
          
        
    };

    fetchProfile();
  }, [navigate]);

  return <div>Loading...</div>;
};

export default Profile;
