import React, { useEffect, useState } from 'react';
import { Paper} from '@mui/material';


const StudentProfile = () => {
  const paperStyle={padding:'50px 20px', width:600,margin:"20px auto"}
  const [student, setStudent] = useState(null);
  const [solutions, setSolutions] = useState([]);
  const [exercisesWithScores, setExercisesWithScores] = useState([]);

  useEffect(() => {
    // Fetch student profile data
    const fetchStudentProfile = async () => {
      const response = await fetch("http://localhost:8080/user/student");
      
      if (response.ok) {
        const studentData = await response.json();
        setStudent(studentData[0]); // Assuming the API returns an array with the student data
      }
      
    
      fetch("http://localhost:8080/exercise/solutions?id="+student.id,{
        method: "GET",
        headers: { "Content-Type": "application/json" }
      })
      .then(res=>res.json())
      .then((result)=>{
        console.log('Fetched students:', result); // Dodaj t
        setExercisesWithScores(result);       
      }
    ).catch(error => console.error('Error fetching students:', error));
  };
    fetchStudentProfile();
  },[]);

  if (!student) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <h2>Student Profile</h2>
      <p>Username: {student.name}</p>
      <p>Email: {student.email}</p>
      <p>Twój wynik: {student.score}</p>
      <h1>Rozwiązane:</h1>
    {
    <Paper elevation={3} style={paperStyle}>
        
      {solutions.map(solution=>(
        <Paper elevation={6} style={{margin:"10px",padding:"15px", textAlign:"left"}} key={solution.id}>
         Content:{solution.exercise.content}<br/>
         Your Solution:{solution.solutionContent}
         <button value={solution.id} >Rozwiąż</button>
        </Paper>
      ))
    }
    </Paper>
    }
    {
       <Paper elevation={3} style={paperStyle}>
        
       {solutions.map(solution=>(
         <Paper elevation={6} style={{margin:"10px",padding:"15px", textAlign:"left"}} key={solution.id}>
          Content:{solution.exercise.content}<br/>
          Your Solution:{solution.solutionContent}
          <button value={solution.id} >Rozwiąż</button>
         </Paper>
       ))
     }
     </Paper>
    }
    </div>
  );
};

export default StudentProfile;