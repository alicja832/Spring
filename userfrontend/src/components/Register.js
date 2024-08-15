import React, {useState } from 'react';
import { makeStyles } from '@mui/styles';
import TextField from '@mui/material/TextField';
import { Container ,Paper,Button,Box} from '@mui/material';
import { FilledInput, IconButton,InputAdornment } from "@mui/material";
import { VisibilityOffOutlined, VisibilityOutlined } from "@mui/icons-material";
import {MenuItem} from '@mui/material';
import {Select,InputLabel,FormControl} from '@mui/material';



const useStyles = makeStyles((theme) => ({
 
}));

export default function Register() {

    const paperStyle={padding:'50px 20px', width:600,margin:"20px auto"}
    const[name,setName]=useState('')
    const[email,setEmail]=useState('')
    const[role,setRole]=useState('')
    const[password,setPassword]=useState([])
    const [psw, setPsw] = useState(false);
    const handleShowPsw = () => setPsw((show) => !show);
    const handleHidePsw = (e) => {
      e.preventDefault();
   };
    const classes = useStyles();
    const [infoWindowShown, setInfoWindowShown] = useState(false);
    const [errorWindowShown, seterrorInfoWindowShown] = useState(false);

    const register=(e)=>{  
    e.preventDefault()
    const student={name,email,password}
    const url = role === 1 
    ? "http://localhost:8080/user/teacher"
    : "http://localhost:8080/user/student";

    
        fetch(url,{
          method:"POST",
          headers:{"Content-Type":"application/json"},
          body:JSON.stringify(student)
    
      }).then(()=>{

        setName('');
        setEmail('');
        setPassword('');
        setRole('');

           setInfoWindowShown(isShown => !isShown);
           setTimeout(() => {
            setInfoWindowShown(false);
          }, 3000); // Okienko zniknie po 3 sekundach
          // if the maps api closes the infowindow, we have to synchronize our state
          
        }).catch((error) => {
          console.error('Error:', error);
          seterrorInfoWindowShown(true);
        });
    }

  function Toast({ message }) {
    return (
      <div className="toast">
        {message}
      </div>
    );
  }

 
  
  return (

    <Container>
        <Paper elevation={3} style={paperStyle}>

    <form className={classes.root} noValidate autoComplete="off">
    
      <TextField id="outlined-basic" label="login" variant="outlined" fullWidth 
      value={name}
      onChange={(e)=>setName(e.target.value)}
      />
      <TextField id="outlined-basic" label="email" variant="outlined" fullWidth
      value={email}
      onChange={(e)=>setEmail(e.target.value)}
      />
      <FilledInput
              value={password}
              placeholder="Hasło"
              onChange={(e)=>setPassword(e.target.value)}
              type={psw ? 'text' : 'password'}
              fullWidth
              endAdornment={
                  <InputAdornment position="start">
                     <IconButton
                        onClick={handleShowPsw}
                        onMouseDown={handleHidePsw}
                        edge="end"
                     >
                        {psw ? <VisibilityOffOutlined /> : <VisibilityOutlined />}
                        
                     </IconButton>
                  
                  </InputAdornment>
               }
            />
          <FormControl fullWidth>
    <InputLabel id="role-label">Rola</InputLabel>
    <Select
    labelId="role-label"
    value={role}
    // fullWidth
    onChange={(e)=>setRole(e.target.value)}
  >
    <MenuItem value={0}>Uczeń</MenuItem>
    <MenuItem value={1}>Nauczyciel</MenuItem>
  </Select>
  </FormControl>
       <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>

       <Box display="flex" flexDirection="column" gap={2}>
      <Button variant="contained" color="secondary" onClick={register}>
        Zarejestruj
      </Button>
      {infoWindowShown && (
   <Toast 
   message="Zarejestrowano!"
 />
)}
 {errorWindowShown && (
   <Toast 
   message="Podana nazwa użytkownika już istnieje!"
 />
)}
      </Box>
    </div>
    </form>
   
    </Paper>
  
    </Container>


  );
}