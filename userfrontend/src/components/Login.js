import React, { useEffect, useState } from "react";
import { makeStyles } from "@mui/styles";
import TextField from "@mui/material/TextField";
import { Container, Paper, Button, Box } from "@mui/material";
import { FilledInput, IconButton, InputAdornment } from "@mui/material";
import { VisibilityOffOutlined, VisibilityOutlined } from "@mui/icons-material";
import { MenuItem } from "@mui/material";
import { Select, InputLabel, FormControl } from "@mui/material";
import { useNavigate } from "react-router-dom";
import MyParticles from "./MyParticles";
import { Link } from "react-router-dom";
// import TokenService from '../api/TokenService';
// import LoginInformation from '../api/LoginInformation'
// import axios from 'axios';
const useStyles = makeStyles((theme) => ({}));

export default function Login() {
  const paperStyle = {
    top: "4em",
    padding: "50px 20px",
    width: 470,
    margin: "20px auto",
    position: "relative",
     backgroundColor : "#FDF5E6",
  };
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [role, setRole] = useState("");
  const [password, setPassword] = useState([]);
  const [psw, setPsw] = useState(false);
  const handleShowPsw = () => setPsw((show) => !show);
  const handleHidePsw = (e) => {
    e.preventDefault();
  };
  const classes = useStyles();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState({});
  const [credentials, setCredentials] = useState({
    id: 1,
    name: "",
    email: "a",
    password: "",
    role: "TEACHER",
  });

  const [loginState, setLoginState] = useState({
    hasLoginFailed: false,
    showSuccessMessage: false,
  });

  const validate = () => {
    const errors = {};

    if (!credentials.name) {
      errors.name = "name required";
    } else if (credentials.name.length < 4) {
      errors.name = "Minimum 4 characters";
    }

    if (!credentials.password) {
      errors.password = "A password is required";
    }

    return errors;
  };

  const loginClicked = async (event) => {
    //   console.log(credentials);
    //   event.preventDefault();
    //   // setCredentials(name,password)
    //   let errors = validate(credentials);
    //   setErrors(errors);
    //   console.log(errors);
    //   const toSend = JSON.stringify(credentials);
    //   if (Object.keys(errors).length === 0) {
    //     setLoading(true);
    //     const one = credentials.name;
    //     const two = credentials.password;
    //      axios
    //  .post(`http://localhost:8080/user/authenticate`,
    //    credentials
    //   )
    //    .then((res)=>{
    //     if (res != null) {
    //       console.log(res);
    //       const token = res.data.jwtToken;
    //       console.log(token);
    //     // const res = TokenService(
    //     //   credentials.name,
    //     //   credentials.password
    //     // );
    //     // console.log(res.text());
    //     // if (res.status !== 200) {
    //     //   setLoading(false);
    //     //   setLoginState((prevState) => ({ ...prevState, hasLoginFailed: true }));
    //     //   setLoginState((prevState) => ({
    //     //     ...prevState,
    //     //     showSuccessMessage: false,
    //     //   }));
    //     // } else {
    //     //   let jwtToken = res;
    //     //   const token = `Bearer ${jwtToken}`;
    //     //   LoginInformation.setUpToken(token);
    //     let config = {
    //       headers: {
    //         Authorization: `Bearer ${token}`
    //       }
    //     }
    //     // LoginInformation.setUpToken(token);
    //     axios.post(`http://localhost:8080/user/login`,
    //       credentials,
    //       config
    //      ).then((response)=>{
    //     console.log(response.text);
    //     if (response.status !== 200) {
    //       setLoading(false);
    //       setLoginState((prevState) => ({
    //         ...prevState,
    //         hasLoginFailed: true,
    //       }));
    //       setLoginState((prevState) => ({
    //         ...prevState,
    //         showSuccessMessage: false,
    //       }));
    //     } else if (response.data === "STUDENT") {
    //       // AuthenticationService.registerSuccessfulLoginUser(
    //       //   credentials.name
    //       console.log("Student");
    //       navigate("/studentprofil");
    //     } else if (response.data === "TEACHER") {
    //      console.log("Teacher");
    //       navigate("/teacherprofil");
    //     }
    //   }).catch((error) => {
    //     console.error('Error:', error);
    //   });
    //    }
    //   }).catch((err) => {
    //     console.log(err);
    // })
  };
  return (
    <div>
      <MyParticles></MyParticles>
      <div id="sthelse">
        <Container>
          <Paper elevation={3} style={paperStyle}>
            <form className={classes.root} noValidate autoComplete="off">
              <TextField
                id="outlined-basic"
                label="login"
                variant="outlined"
                fullWidth
                value={name}
                onChange={(e) => setName(e.target.value)}
                sx={{ marginBottom: "16px" }}
              />
              <TextField
                id="outlined-basic"
                label="email"
                variant="outlined"
                fullWidth
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                sx={{ marginBottom: "16px" }}
              />
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
              <FormControl fullWidth>
                <InputLabel id="role-label">rola</InputLabel>
                <Select
                  labelId="role-label"
                  value={role}
                  // fullWidth
                  onChange={(e) => setRole(e.target.value)}
                  sx={{ marginBottom: "16px" }}
                >
                  <MenuItem value={0}>Uczeń</MenuItem>
                  <MenuItem value={1}>Nauczyciel</MenuItem>
                </Select>
              </FormControl>
              <div
                style={{
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                  textDecoration: "none",
                }}
              >
                <Button
                  style = {{backgroundColor:'#001f3f'}}
                  variant="contained"
                  color="primary"
                  component={Link}
                  to={"/password"}
                  sx={{ textAlign: "center" ,marginBottom: '16px'}}
                >
                  Zapomniałeś hasła?  
                </Button>
              </div>
              <div
                style={{
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                }}
              >
                <Box display="flex" flexDirection="column" gap={2}>
                  <Button
                    style = {{backgroundColor:'#001f3f'}}
                    variant="contained"
                    color="primary"
                    onClick={loginClicked}
                  >
                    Zaloguj
                  </Button>
                </Box>
              </div>
            </form>
          </Paper>
        </Container>
      </div>
    </div>
  );
}
