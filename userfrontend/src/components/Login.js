import React, { useEffect, useState } from 'react';
import { makeStyles } from '@mui/styles';
import TextField from '@mui/material/TextField';
import { Container ,Paper,Button,Box} from '@mui/material';
import { FilledInput, IconButton,InputAdornment } from "@mui/material";
import { VisibilityOffOutlined, VisibilityOutlined } from "@mui/icons-material";
import {MenuItem} from '@mui/material';
import {Select,InputLabel,FormControl} from '@mui/material';

const useStyles = makeStyles((theme) => ({
 
}));

export default function Login() {

    const paperStyle={padding:'50px 20px', width:600,margin:"20px auto"}
    const[name,setName]=useState('')
    const[address,setAddress]=useState('')
    const[role,setRole]=useState('')
    const[password,setPassword]=useState([])
    const [psw, setPsw] = useState(false);
    const handleShowPsw = () => setPsw((show) => !show);
    const handleHidePsw = (e) => {
      e.preventDefault();
   };
    const classes = useStyles();

  const login=(e)=>{  
    e.preventDefault()
    const student={name,address,password}
    const url = role === 1 
              ? "http://localhost:8080/user/loginTeacher"
              : "http://localhost:8080/user/loginStudent";

  
          fetch(url,{
            method:"POST",
            headers:{"Content-Type":"application/json"},
            body:JSON.stringify(student)
      
        })
        .then(async (response) => {
          const text = await response.text();
          if (!response.ok) {
            throw new Error(text);
          }
          console.log('Success:', text);
        })
        .catch(error => console.error('Error logging in:', error));
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
      value={address}
      onChange={(e)=>setAddress(e.target.value)}
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
      <Button variant="contained" color="secondary" onClick={login}>
        Zaloguj
      </Button>
      </Box>
    </div>
    </form>
   
    </Paper>
    </Container>
  );
}