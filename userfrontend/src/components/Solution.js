import React, {useEffect, useState } from 'react';
import { TextField, IconButton, Paper, Typography,Button,Box } from '@mui/material';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';
import { makeStyles } from '@mui/styles';


const useStyles = makeStyles({
  container: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    gap: '10px', // Odstęp między elementami
  },
  textFieldContainer: {
    width:'600px',
    border: '4px',
    borderStyle:"solid",
    borderColor:"blue",
    height:'400px',
    backgroundColor:'grey',
    display: 'flex',
    alignItems: 'center',
    gap: '10px', // Odstęp między polem tekstowym a przyciskiem
  },
  textField: {
    // Szerokość pola tekstowego
    height:'392px',
    backgroundColor: '#000', // Czarny kolor tła
    color: '#fff', // Biały kolor tekstu
    '& .MuiInputBase-input': {
      color: '#fff', // Biały kolor tekstu
    },
    '& .MuiFormLabel-root': {
      color: '#fff', // Biały kolor etykiety
    },
    
  },
  button: {
    color: '#fff', // Biały kolor ikony
    backgroundColor: '#000', // Czarny kolor tła
    '&:hover': {
      backgroundColor: '#333', // Ciemniejszy czarny na hover
    },
  },
  output: {
    backgroundColor: '#000', // Czarny kolor tła
    color: '#fff', // Biały kolor tekstu
    padding: '10px',
    height: '200px', // Stała wysokość
    width: '200px', // Stała szerokość
    marginTop: '10px',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    textAlign: 'center',
  },
});

export default function Solution({task}) {

  console.log(task);
  const paperStyle={padding:'50px 20px', width:600,margin:"20px auto"}
  const classes = useStyles();
  const [ solutionContent, setSolutionContent] = useState('');
  const [compare, setCompare] = useState(false);
  const [output, setOutput] = useState('');
  const [exercise, setExercise] = useState(null);
  const [visible, setVisible] = useState(false);
  const [score, setScore] = useState(0);

  const handleInputChange = (e) => {
    setSolutionContent(e.target.value);
  };
  const handleKeyDown = (e) => {
    if (e.key === 'Tab') {
      e.preventDefault();
      const { selectionStart, selectionEnd } = e.target;
      setSolutionContent(prevContent => 
        prevContent.substring(0, selectionStart) + '\t' + prevContent.substring(selectionEnd)
      );
      setTimeout(() => {
        e.target.selectionStart = selectionStart + 1;
        e.target.selectionEnd = selectionStart + 1;
      }, 0);
    }
  };

  const save=()=>{
    var student = null; 
    console.log(solutionContent);
    const solution = {solutionContent,exercise,student,score};
    fetch("http://localhost:8080/user/solution", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(solution)
    }).then(res => res.text())
      .then((result) => {
        console.log('Exec', result);
      })
      .catch((error) => {
        console.error('Error:', error);
      });
  };
  const handleButtonClick = () => {
    console.log(solutionContent);
    fetch("http://localhost:8080/exercise/interpreter", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(solutionContent)
    }).then(res => res.text())
      .then((result) => {
        console.log('Exec', result);
        setOutput(result);
      })
      .catch((error) => {
        console.error('Error:', error);
        setOutput('Error occurred');
      });
  };
  const check = () => {
    setCompare((output.localeCompare(exercise.expectedOutput)));
    setScore(0);
    setVisible(true);
  };


useEffect(()=>{
 
  fetch("http://localhost:8080/exercise/id?id="+task,{
    method: "POST",
    headers: { "Content-Type": "application/json" }
  })
  .then(res=>res.json())
  .then((result)=>{
    console.log('Fetched students:', result); // Dodaj t
    setExercise(result [0]);
   
  }
).catch(error => console.error('Error fetching students:', error));
},[])
  if (!exercise) {
    return <div>Loading...</div>;
  }
 
  return (
    <div>
      <h2>Zadanie</h2>
      <p>Treść: {exercise.content}</p>
      <p>Maksymalna ilość punktów: {exercise.maxPoints}</p>
      {
         <div className={classes.container}>
         <div className={classes.textFieldContainer}>
           <TextField
               className={classes.textField}
               variant="outlined"
               placeholder="Miejsce na rozwiązanie..."
               value={solutionContent}
               onChange={handleInputChange}
               onKeyDown={handleKeyDown}
               fullWidth
               multiline
               maxRows={15}
            />
           <IconButton
             className={classes.button}
             onClick={handleButtonClick}
             aria-label="submit"
           >
             <ArrowForwardIcon />
           </IconButton>
         </div>
         <Paper className={classes.output}>
           <Typography variant="body1">
             {output}
           </Typography>
         </Paper>
         <Paper elevation={3} style={paperStyle}>
          {visible&&((compare)?
         <h3>RozwiazanoOk</h3> :  <h3>Rozwiazano nie ok</h3> 
          )}
          </Paper>
          <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>

          <Box display="flex" flexDirection="column" gap={2}>
          <Button variant="contained" color="secondary" onClick={check}>
            Sprawdz
          </Button>
          </Box>
          </div>
          <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>

          <Box display="flex" flexDirection="column" gap={2}>
          <Button variant="contained" color="secondary" onClick={save}>
            Zapisz
          </Button>
          </Box>
          </div>
         </div>
      }
    </div>
  );

};