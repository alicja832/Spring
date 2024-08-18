import React, { useEffect, useState } from 'react';
import { makeStyles } from '@mui/styles';
import { useNavigate } from 'react-router-dom';
import { Paper, Button, Box } from '@mui/material';
import CheckIcon from '@mui/icons-material/Check';
import {getLogin} from './api/TokenService';
import MyParticles from './MyParticles';
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
    position: "relative"
  },
  headerContainer: {
    display: 'flex',
    alignItems: 'center',
    gap: '20px', // Odstęp między h3 a Box
    position: "relative",
  },

});

export default function Exercise() {
  const paperStyle = { padding: '50px 20px', width: 600, margin: "20px auto" ,position: "relative",}
  const classes = useStyles();
  const [exercises, setExercises] = useState([]);
  const navigate = useNavigate();

  const openSolution = (e) => {
    navigate('/solution/:' + e.target.value);
  }


  useEffect(() => {
    fetch("http://localhost:8080/exercise/"+getLogin())
      .then(res => res.json())
      .then((result) => {
        console.log('Fetched students:', result); // Dodaj t
        setExercises(result);
      
      }
      ).catch(error => console.error('Error fetching students:', error));
  }, [])

  return (
   <div>
    <MyParticles></MyParticles>
    <div className={classes.container}>
      
      <h1>Zadania</h1>
      {
       <Paper elevation={3} style={paperStyle}>
       {exercises.map(exercise => (
         <Paper elevation={6} style={{ margin: "10px", padding: "15px", textAlign: "left" }} key={exercise.key.id}>
           <div className={classes.headerContainer} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
             <h3>{exercise.key.name}</h3>
             <Box className={classes.points}>{exercise.key.maxPoints}</Box>
     
             {exercise.value ? (
               <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', marginLeft: 'auto' }}>
                 <CheckIcon />
                 <span>Zrobione</span>
               </div>
             ) : null}
           </div>
     
           <div>
             <Box display="flex" flexDirection="column" gap={2}>
               <Button variant="contained" value={exercise.key.id} color="secondary" onClick={openSolution}>
                 Wykonaj
               </Button>
             </Box>
           </div>
         </Paper>
       ))}
     </Paper>
     
      }
    </div>
    </div>


  );
}
