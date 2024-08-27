import TextField from "@mui/material/TextField";
import { makeStyles } from "@mui/styles";
import { Container, Paper, Button, Box } from "@mui/material";
import MyParticles from "./MyParticles";
import React, { useState } from "react";
import { FilledInput, IconButton, InputAdornment } from "@mui/material";
import { VisibilityOffOutlined, VisibilityOutlined } from "@mui/icons-material";

const useStyles = makeStyles((theme) => ({}));

export default function PasswordReminder() {
    const [code, setCode] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [psw, setPsw] = useState(false);
    const [errorMessage, seterrorMessage] = useState(false);
    const [errorWindowShown, seterrorInfoWindowShown] = useState(false);
    const [isFormVisible,setIsFormVisible] = useState(false);
    const [isFormCodeVisible,setIsFormCodeVisible] = useState(false);
    const [infoWindowShown, setInfoWindowShown] = useState(false);
    const [emailFormShow, setemailFormShow] = useState(true);
    const [infoTwoWindowShown, setInfoTwoWindowShown] = useState(false);

    const handleShowPsw = () => setPsw((show) => !show);
    const handleHidePsw = (e) => {
      e.preventDefault();
    };
    const classes = useStyles();
  
    function showCodeForm()
    {
      
      // setemailFormShow(false);
      setIsFormCodeVisible(true);
      //window.location.reload();
    }
    function closeCodeForm()
    {
      setIsFormCodeVisible(false);
      //window.location.reload();
    }
    function showForm()
    {
      setIsFormVisible(true);
     // window.location.reload();
    }
    function closeForm()
    {
      setIsFormVisible(false);
      //window.location.reload();
    }
    const paperStyle = {
      top: "4em",
      padding: "50px 20px",
      width: 800,
      margin: "20px auto",
      gap: "10px",
      position: "relative",
    };
  function send()
  {
    const url = "http://localhost:8080/user/remindPassword/";
    fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body:email,
      })
      .then((response) => {
        
        if (!response.ok) {
          
          const promise1 = Promise.resolve(response.body.getReader().read());
          promise1.then((value) => {
            const decoder = new TextDecoder('utf-8');
            const text = decoder.decode(value.value);
            seterrorMessage(text);
          })
          setInfoWindowShown(false);
          seterrorInfoWindowShown(true);
          setTimeout(() => {
            seterrorInfoWindowShown(false);
          }, 3000);
        
        }
        else
        {
          setEmail("");
          setInfoWindowShown(true);
          setTimeout(() => {
            setInfoWindowShown(false);
          }, 3000);
          showCodeForm();
        }
      });
  };
  function verificationCode()
  {
    const verification = {email,code,password};
    const url = "http://localhost:8080/user/CodeVerification";
    fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body:JSON.stringify(verification)
      })
      .then((response) => {
        if (!response.ok) {
          const promise1 = Promise.resolve(response.body.getReader().read());
          promise1.then((value) => {
            const decoder = new TextDecoder('utf-8');
            const text = decoder.decode(value.value);
            seterrorMessage(text);
          })
          setInfoWindowShown(false);
          seterrorInfoWindowShown(true);
          setTimeout(() => {
            seterrorInfoWindowShown(false);
          }, 3000);
        }
        else
        {
          setCode("");
          closeCodeForm();
          showForm();
        }
      });
  }
  function changePassword()
  {
    const verification = {email,code,password};
    fetch("http://localhost:8080/user/changePassword",
    {
      method: "PUT",
      headers : { "Content-Type": "application/json" },
      body: JSON.stringify(verification)
    }).then((response)=>{
      if (!response.ok) {
        const promise1 = Promise.resolve(response.body.getReader().read());
        promise1.then((value) => {
          const decoder = new TextDecoder('utf-8');
          const text = decoder.decode(value.value);
          seterrorMessage(text);
        })
        setInfoWindowShown(false);
        seterrorInfoWindowShown(true);
        setTimeout(() => {
          seterrorInfoWindowShown(false);
        }, 3000);
      }
      else
      {
        setPassword("");
        setInfoTwoWindowShown(true);
        setTimeout(() => {
          setInfoTwoWindowShown(false);
        }, 3000);
      }
     
    })
  }
  
  function Toast({ message }) {
    return <div className="toast">{message}</div>;
  }

  return (
    <div>
    <MyParticles></MyParticles>
    <div id= "sthelse">
    {(emailFormShow)&&
    <Container>
          <Paper elevation={3} style={paperStyle}>
          <h5>Wprowadź adres email, na który ma zostać wysłany kod pozwalający na zmianę hasła</h5>

            <form className={classes.root} noValidate autoComplete="off">
            
              <TextField
                id="outlined-basic"
                label="email"
                variant="outlined"
                fullWidth
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                sx={{ marginBottom: "16px" }}
              />
              <div
                style={{
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                  marginBottom: "16px", // Add space below the first div
                }}
              >
                <Box display="flex" flexDirection="column" gap={2}>
                  <Button
                    variant="contained"
                    color="primary"
                    onClick={send}
                  >
                    Wyślij
                  </Button>
                </Box>
              </div>
              <div
                style={{
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                }}
              >
                <Box display="flex" flexDirection="column" gap={2}>
                  {infoWindowShown && <Toast message="Na podany adres email wysłano wiadomość z kodem do zmiany hasła" />}
                  {errorWindowShown && <Toast message={errorMessage} />}
                </Box>
              </div>
            </form>
          </Paper>
        </Container>
    }
        {(isFormCodeVisible)&&
          <Container>
          <Paper elevation={3} style={paperStyle}>
          <h5>Wprowadź kod, który wysłano na podany adres email</h5>

            <form className={classes.root} noValidate autoComplete="off">
            
              <TextField
                id="outlined-basic"
                label="email"
                variant="outlined"
                fullWidth
                value={code}
                onChange={(e) => setCode(e.target.value)}
                sx={{ marginBottom: "16px" }}
              />
              <div
                style={{
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                }}
              >
                <Box display="flex" flexDirection="column" gap={2}>
                  <Button
                    variant="contained"
                    color="primary"
                    onClick={verificationCode}
                  >
                    Zweryfikuj
                  </Button>
                </Box>
              </div>
              <div
                style={{
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                }}
              >
                <Box display="flex" flexDirection="column" gap={2}>
                  {infoWindowShown && <Toast message="Na podany adres email wysłano wiadomość z kodem do zmiany hasła" />}
                </Box>
              </div>
            </form>
          </Paper>
        </Container>
      }
        {(isFormVisible)&&
        <Container>
          <Paper elevation={3} style={paperStyle}>
            <form className={classes.root} noValidate autoComplete="off">
              <FilledInput
                value={password}
                placeholder="hasło"
                onChange={(e) => setPassword(e.target.value)}
                type={psw ? "text" : "password"}
                fullWidth
                sx={{ marginBottom: "16px" }}
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
              <div
                style={{
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                }}
              >
                <Box display="flex" flexDirection="column" gap={2}>
                  <Button
                    variant="contained"
                    color="primary"
                    onClick={changePassword}
                  >
                   Zmień
                  </Button>
                </Box>
              </div>
              <div
                style={{
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                }}
              >
                <Box display="flex" flexDirection="column" gap={2}>
                  {infoTwoWindowShown && <Toast message="Hasło zostało zmienione" />}
                </Box>
              </div>
            </form>
          </Paper>
        </Container>}
    </div>
    </div>
  );
}
