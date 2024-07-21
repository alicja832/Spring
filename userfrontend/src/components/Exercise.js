import React, {useEffect, useState } from 'react';
import { makeStyles } from '@mui/styles';
import { useNavigate } from 'react-router-dom';
import {Paper,Button,Box} from '@mui/material';
import { BorderStyle } from '@mui/icons-material';

const useStyles = makeStyles({
  points: {
    width: '30px',
    height: '30px',
    display: 'flex',
    alignItems: 'center',
    border: '3px',
    borderStyle: 'solid',
    borderColor:"blue",
    borderRadius : '5px',
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

export default function Exercise() {
  const paperStyle={padding:'50px 20px', width:600,margin:"20px auto"}
  const classes = useStyles();
  const[exercises,setExercises]=useState([]);
  const navigate = useNavigate();

  const openSolution=(e)=>{
    navigate('/solution/:'+e.target.value);
  }


useEffect(()=>{
  fetch("http://localhost:8080/exercise/")
  .then(res=>res.json())
  .then((result)=>{
    console.log('Fetched students:', result); // Dodaj t
    setExercises(result);
  }
).catch(error => console.error('Error fetching students:', error));
},[])
  
  return (
    <div className={classes.container}>
      <h1>Zadania</h1>
    {
    <Paper elevation={3} style={paperStyle}>
        
      {exercises.map(exercise=>(
        <Paper elevation={6} style={{margin:"10px",padding:"15px", textAlign:"left"}} key={exercise.id}>
       <div className={classes.headerContainer}> <h3> {exercise.name}</h3><Box className={classes.points}>{exercise.maxPoints}</Box></div>
        
    <div>
       <Box display="flex" flexDirection="column" gap={2}>
      <Button variant="contained" value={exercise.id} color="secondary" onClick={openSolution}>
        Wykonaj
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
}
