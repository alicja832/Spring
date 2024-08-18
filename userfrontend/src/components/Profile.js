import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const Profile = () => {
  const navigate = useNavigate();

  useEffect(() => {
    // Fetch user profile data
    const fetchProfile = async () => {
      try {
        const response = await fetch("http://localhost:8080/user/role", {
        });

        if (response.ok) {
          const role = (await response.text()).trim();
          console.log(role);
          const url = role === "Teacher" ? "http://localhost:8080/user/teacher" : "http://localhost:8080/user/student";

          //const res = await fetch(url);
          //if (res.ok) {
           // const result = await res.json();
           //  console.log('Fetched profile:', result[0]);
            if (role === "Teacher") {
              navigate('/teacherprofil');
            } else {
              navigate('/studentprofil');
            }
          } else {
            console.error('Error fetching profile data:');  
          }
        }
      //}
       catch (error) {
        console.error('Error fetching role:', error);
      }
    };

    fetchProfile();
  }, [navigate]);

  return <div>Loading...</div>;
};

export default Profile;
