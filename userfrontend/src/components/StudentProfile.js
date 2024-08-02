import React, { useEffect, useState } from 'react';
import { Paper} from '@mui/material';
import { makeStyles } from '@mui/styles';
import { Button, Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import LoginInformation from '../api/LoginInformation';
import { Login } from '@mui/icons-material';
const useStyles = makeStyles({
  points: {
    width: '30px',
    height: '30px',
    display: 'flex',
    alignItems: 'center',
    border: '3px',
    borderStyle: 'solid',
    borderColor: "blue",
    borderRadius: '5px',
    backgroundColor: '#6495ED',
    justifyContent: 'center',
    textAlign: 'center',
  },
  headerContainer: {
    display: 'flex',
    alignItems: 'center',
    gap: '20px', // Odstęp między h3 a Box
  },

});

const StudentProfile = () => {
  const classes = useStyles();
  const navigate = useNavigate();
  const paperStyle={padding:'50px 20px', width:600,margin:"20px auto"}
  const [student, setStudent] = useState(null);
  const [exercisesWithScores, setExercisesWithScores] = useState([]);


  const retake = (e) => {
    navigate('/solutionRetake/:' + e.target.value);
  }
  
  useEffect(() => {
    // Fetch student profile data 
   
        fetch("http://localhost:8080/user/student/" + LoginInformation.getLoggedInUser(), {
          method:"GET",
          headers: { "Content-Type": "application/json" }
        })
          .then(res => res.json())
          .then((result) => {
            console.log('Fetched students:', result); // Dodaj t
            setStudent(result);
    
          }
          ).catch(error => console.error('Error fetching students:', error));
    
      
      

      fetch("http://localhost:8080/exercise/solutions/"+LoginInformation.getLoggedInUser(),{
        method: "GET",
        headers: { "Content-Type": "application/json" }
      })
      .then(res=>res.json())
      .then((result)=>{
        console.log('Fetched studxxxxents:', result); // Dodaj t
        setExercisesWithScores(result);       
      }
    ).catch(error => console.error('Error fetching students:', error));
  }, []);
  
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

          {exercisesWithScores.map(exercise => (
            <Paper elevation={6} style={{ margin: "10px", padding: "15px", textAlign: "left" }} /*key={exercise.id}*/>
              <div className={classes.headerContainer}> <h3> {exercise.name}</h3><p>Twój wynik:</p><Box className={classes.points}>{exercise.score}</Box></div>

              <div>
                <Box display="flex" flexDirection="column" gap={2}>
                  <Button variant="contained" value={parseInt(exercise.id)} onClick={retake} color="secondary" >
                    Wykonaj ponownie
                  </Button>
                </Box>
              </div>
            </Paper>
          ))
          }
        </Paper>
      }
    </div>
  );
};

export default StudentProfile;